package com.shs.b2bm.claim.service.kafka.config;

import com.shs.b2bm.claim.service.kafka.deserializer.ProtobufDeserializer;
import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.util.backoff.FixedBackOff;

/**
 * Kafka configuration for ServiceOrder messages. Configures deserialization, consumer factory,
 * listener container, and dead letter handling for ServiceOrderProto messages.
 */
@Configuration
@EnableKafka
public class ServiceOrderKafkaConfig {

  @Value("${spring.kafka.topics.service-order-dead-letter}")
  private String deadLetterTopic;

  @Value("${spring.kafka.consumer.heartbeat-interval-ms}")
  private int heartbeatIntervalMs;

  @Value("${spring.kafka.consumer.max-attempts}")
  private int maxAttempts;

  private final KafkaConsumerConfig kafkaConsumerConfig;

  private final KafkaTemplate<String, ServiceOrderProto> serviceOrderConsumerTemplate;

  public ServiceOrderKafkaConfig(
      KafkaConsumerConfig kafkaConsumerConfig,
      KafkaTemplate<String, ServiceOrderProto> serviceOrderConsumerTemplate) {
    this.kafkaConsumerConfig = kafkaConsumerConfig;
    this.serviceOrderConsumerTemplate = serviceOrderConsumerTemplate;
  }

  /**
   * Provides a deserializer for ServiceOrderProto messages.
   *
   * @return Deserializer for ServiceOrderProto
   */
  @Bean
  public Deserializer<ServiceOrderProto> serviceOrderProtoDeserializer() {
    return new ErrorHandlingDeserializer<>(new ProtobufDeserializer<>(ServiceOrderProto.class));
  }

  /**
   * Provides a ConsumerFactory for ServiceOrderProto messages.
   *
   * @return ConsumerFactory for ServiceOrderProto
   */
  @Bean
  public ConsumerFactory<String, ServiceOrderProto> serviceOrderConsumerFactory() {

    ErrorHandlingDeserializer<ServiceOrderProto> valueDeserializer =
        new ErrorHandlingDeserializer<>(new ProtobufDeserializer<>(ServiceOrderProto.class));

    return kafkaConsumerConfig.consumerFactory(valueDeserializer);
  }

  /**
   * Provides a ConcurrentKafkaListenerContainerFactory for ServiceOrderProto messages.
   *
   * @param consumerFactory the consumer factory for ServiceOrderProto
   * @param recoverer the DeadLetterPublishingRecoverer for failed messages
   * @return ConcurrentKafkaListenerContainerFactory for ServiceOrderProto
   */
  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, ServiceOrderProto>
      serviceOrderKafkaListenerContainerFactory(
          ConsumerFactory<String, ServiceOrderProto> consumerFactory,
          DeadLetterPublishingRecoverer recoverer) {
    return kafkaConsumerConfig.kafkaListenerContainerFactory(
        this.serviceOrderConsumerFactory(),
        this.deadLetterPublishingRecovererTemplateServiceOrder());
  }

  /**
   * Creates a DefaultErrorHandler for handling failed messages.
   *
   * @return DefaultErrorHandler configured for handling failed messages
   */
  private DefaultErrorHandler deadLetterPublishingRecovererTemplateServiceOrder() {
    return new DefaultErrorHandler(
        deadLetterPublishingRecovererServiceOrder(),
        new FixedBackOff(heartbeatIntervalMs, maxAttempts));
  }

  /**
   * Creates a DeadLetterPublishingRecoverer for handling failed ServiceOrder messages.
   *
   * @return DeadLetterPublishingRecoverer configured for ServiceOrder dead letter topic
   */
  @Bean
  public DeadLetterPublishingRecoverer deadLetterPublishingRecovererServiceOrder() {
    return new DeadLetterPublishingRecoverer(
        serviceOrderConsumerTemplate,
        (record, ex) -> new org.apache.kafka.common.TopicPartition(deadLetterTopic, 0));
  }
}
