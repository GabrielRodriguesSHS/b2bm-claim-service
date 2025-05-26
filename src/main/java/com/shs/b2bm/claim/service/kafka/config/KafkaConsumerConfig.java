package com.shs.b2bm.claim.service.kafka.config;

import com.shs.b2bm.claim.service.kafka.deserializer.ProtobufDeserialize;
import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.util.backoff.FixedBackOff;

/**
 * Kafka configuration class for setting up consumers and related components. Configures Kafka
 * consumer properties, deserializers, error handling, and health monitoring.
 */
@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConsumerConfig {

  @Value("${spring.kafka.consumer.bootstrap-servers:localhost:29092}")
  private String bootstrapServers;

  @Value("${spring.kafka.consumer.group-id:b2bm-claim-service}")
  private String groupId;

  @Value("${spring.kafka.consumer.heartbeat-interval-ms}")
  private int heartbeatIntervalMs;

  @Value("${spring.kafka.consumer.max-attempts}")
  private int maxAttempts;

  @Value("${spring.kafka.topics.service-order-dead-letter}")
  private String deadLetterTopic;

  private final KafkaTemplate<String, ServiceOrderProto> kafkaTemplate;

  /**
   * Creates a ConsumerFactory for ServiceOrder messages.
   *
   * @return ConsumerFactory configured for ServiceOrder messages
   */
  @Bean
  public ConsumerFactory<String, ServiceOrderProto> serviceOrderConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

    ErrorHandlingDeserializer<ServiceOrderProto> valueDeserializer =
        new ErrorHandlingDeserializer<>(new ProtobufDeserialize<>(ServiceOrderProto.class));

    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), valueDeserializer);
  }

  /**
   * Creates a KafkaListenerContainerFactory for ServiceOrder messages. Configures concurrent
   * consumers, batch listening, and error handling.
   *
   * @return ConcurrentKafkaListenerContainerFactory configured for ServiceOrder messages
   */
  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, ServiceOrderProto>
      serviceOrderKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, ServiceOrderProto> factory =
        new ConcurrentKafkaListenerContainerFactory<>();

    factory.setConsumerFactory(serviceOrderConsumerFactory());
    factory.setCommonErrorHandler(this.deadLetterPublishingRecovererTemplate());

    return factory;
  }

  /**
   * Creates a health indicator for monitoring Kafka connectivity.
   *
   * @return HealthIndicator for Kafka
   */
  @Bean
  public HealthIndicator kafkaHealthIndicator() {
    return () -> {
      try {
        ConsumerFactory<String, ServiceOrderProto> cf = serviceOrderConsumerFactory();

        var consumer = cf.createConsumer();

        try {
          consumer.listTopics();
          return Health.up().build();
        } finally {
          consumer.close();
        }
      } catch (Exception e) {
        return Health.down()
            .withException(e)
            .withDetail("error", "Failed to connect to Kafka brokers")
            .build();
      }
    };
  }

  /**
   * Creates a DefaultErrorHandler for handling failed messages.
   *
   * @return DefaultErrorHandler configured for handling failed messages
   */
  private DefaultErrorHandler deadLetterPublishingRecovererTemplate() {
    return new DefaultErrorHandler(
        deadLetterPublishingRecoverer(), new FixedBackOff(heartbeatIntervalMs, maxAttempts));
  }

  /**
   * Creates a DeadLetterPublishingRecoverer for handling failed messages.
   *
   * @return DeadLetterPublishingRecoverer configured for handling failed messages
   */
  @Bean
  public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer() {
    return new DeadLetterPublishingRecoverer(
        kafkaTemplate,
        (record, ex) -> new org.apache.kafka.common.TopicPartition(deadLetterTopic, 0));
  }
}
