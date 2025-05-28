package com.shs.b2bm.claim.service.kafka.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;

/**
 * Kafka configuration class for setting up consumers and related components. Configures Kafka
 * consumer properties, deserializers, error handling, and health monitoring.
 */
@Configuration
public class KafkaConsumerConfig {

  @Value("${spring.kafka.consumer.bootstrap-servers:localhost:29092}")
  private String bootstrapServers;

  @Value("${spring.kafka.consumer.group-id:b2bm-claim-service}")
  private String groupId;

  @Value("${spring.kafka.consumer.auto-offset-reset}")
  private String autoOffsetReset;

  /**
   * Creates a default ConsumerFactory for ServiceOrder messages.
   *
   * @param valueDeserializer The deserializer for the message value type
   * @return ConsumerFactory configured for ServiceOrder messages
   */
  public <V> ConsumerFactory<String, V> consumerFactory(Deserializer<V> valueDeserializer) {

    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);

    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), valueDeserializer);
  }

  /**
   * Creates a KafkaListenerContainerFactory for ServiceOrder messages. Configures concurrent
   * consumers, batch listening, and error handling.
   *
   * @param consumerFactory the consumer factory to use for the listener container
   * @param errorHandler the error handler for the listener container
   * @return ConcurrentKafkaListenerContainerFactory configured for ServiceOrder messages
   */
  public <V> ConcurrentKafkaListenerContainerFactory<String, V> kafkaListenerContainerFactory(
      ConsumerFactory<String, V> consumerFactory, CommonErrorHandler errorHandler) {

    ConcurrentKafkaListenerContainerFactory<String, V> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    factory.setCommonErrorHandler(errorHandler);
    return factory;
  }
}
