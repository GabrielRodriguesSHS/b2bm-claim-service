package com.shs.b2bm.claim.service.kafka.consumer;

import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;
import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.mappers.ServiceOrderProtoMapper;
import com.shs.b2bm.claim.service.repositories.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Kafka consumer service for processing ServiceOrder messages.
 * Handles the consumption and persistence of service order data from Kafka topics.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceOrderConsumer {

    private final ServiceOrderRepository serviceOrderRepository;

    /**
     * Consumes ServiceOrder messages from the specified Kafka topic.
     * Converts the proto message to an entity and persists it to the database.
     *
     * @param serviceOrder The ServiceOrder proto message
     * @param topic The Kafka topic from which the message was received
     * @param partition The partition from which the message was received
     * @param offset The offset of the message
     */
    @Transactional
    @KafkaListener(
        topics = "${spring.kafka.topics.service-order-created}",
        groupId = "${spring.kafka.consumer.group-id:b2bm-claim-service}",
        containerFactory = "serviceOrderKafkaListenerContainerFactory"
    )
    public void consume(
        @Payload ServiceOrderProto serviceOrder,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
        @Header(KafkaHeaders.OFFSET) Long offset
    ) {
        log.info("Received ServiceOrder message - Topic: {}, Partition: {}, Offset: {}, ServiceOrder Number: {}",
            topic, partition, offset, serviceOrder.getOrderNumber());

        try {
            ServiceOrder entity = ServiceOrderProtoMapper.INSTANCE.toEntity(serviceOrder);
            serviceOrderRepository.save(entity);
            log.info("Successfully processed and saved ServiceOrder: {}", serviceOrder.getOrderNumber());
        } catch (Exception e) {
            log.error("Error processing ServiceOrder message - Topic: {}, Partition: {}, Offset: {}, Error: {}",
                topic, partition, offset, e.getMessage(), e);
            throw e; // Rethrowing to trigger retry mechanism
        }
    }
} 