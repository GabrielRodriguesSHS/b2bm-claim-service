package com.shs.b2bm.claim.service.kafka.config;

import com.shs.b2bm.claim.service.kafka.deserializer.ProtobufDeserialize;
import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka configuration class for setting up consumers and related components.
 * Configures Kafka consumer properties, deserializers, and error handling.
 */
@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConfig {

    @Value("${spring.kafka.consumer.bootstrap-servers:localhost:29092}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id:b2bm-claim-service}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.consumer.max-poll-records}")
    private int maxPollRecords;

    @Value("${spring.kafka.consumer.max-poll-interval-ms}")
    private int maxPollIntervalMs;

    @Value("${spring.kafka.consumer.session-timeout-ms}")
    private int sessionTimeoutMs;

    @Value("${spring.kafka.consumer.heartbeat-interval-ms}")
    private int heartbeatIntervalMs;

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
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMs);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMs);
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatIntervalMs);

        ErrorHandlingDeserializer<ServiceOrderProto> valueDeserializer =
                new ErrorHandlingDeserializer<>(new ProtobufDeserialize<>(ServiceOrderProto.class));

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                valueDeserializer
        );
    }

    /**
     * Creates a KafkaListenerContainerFactory for ServiceOrder messages.
     * Configures concurrent consumers, batch listening, and error handling.
     *
     * @return ConcurrentKafkaListenerContainerFactory configured for ServiceOrder messages
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ServiceOrderProto> serviceOrderKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ServiceOrderProto> factory =
            new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(serviceOrderConsumerFactory());
        factory.setConcurrency(3); // Number of concurrent consumers
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);

        // Configure error handling with retry
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
            new FixedBackOff(1000L, 3L) // Retry 3 times with 1 second interval
        );
        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }
} 