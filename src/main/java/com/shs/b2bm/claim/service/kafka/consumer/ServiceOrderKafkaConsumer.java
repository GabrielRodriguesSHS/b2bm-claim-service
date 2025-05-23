package com.shs.b2bm.claim.service.kafka.consumer;

import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;
import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.exceptions.ServiceOrderProcessingException;
import com.shs.b2bm.claim.service.mappers.ServiceOrderProtoMapper;
import com.shs.b2bm.claim.service.repositories.ServiceOrderRepository;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Kafka consumer service for processing ServiceOrder messages.
 * Handles the consumption and persistence of service order data from Kafka topics.
 * Implements correlation tracking and metrics collection for monitoring.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceOrderKafkaConsumer {

    private final ServiceOrderRepository serviceOrderRepository;
    private final MeterRegistry meterRegistry;

    private static final String CORRELATION_ID = "correlationId";

    /**
     * Consumes ServiceOrder messages from the specified Kafka topic.
     * Converts the proto message to an entity and persists it to the database.
     * Implements correlation tracking and metrics collection.
     *
     * @param serviceOrder The ServiceOrder proto message
     * @param topic The Kafka topic from which the message was received
     * @param partition The partition from which the message was received
     * @param offset The offset of the message
     * @param correlationId The correlation ID for tracking the message through the system
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
        @Header(KafkaHeaders.OFFSET) Long offset,
        @Header(value = KafkaHeaders.CORRELATION_ID, required = false) String correlationId
    ) {
        String trackingId = correlationId != null ? correlationId : UUID.randomUUID().toString();
        MDC.put(CORRELATION_ID, trackingId);
        
        try {
            log.info("Received ServiceOrder message - Topic: {}, Partition: {}, Offset: {}, ServiceOrder Number: {}",
                topic, partition, offset, serviceOrder.getOrderNumber());

            validateMessage(serviceOrder);
            processServiceOrder(serviceOrder, trackingId);
            
            meterRegistry.counter("kafka.serviceorder.processed", 
                "topic", topic, 
                "result", "success").increment();
                
            log.info("Successfully processed and saved ServiceOrder: {}", serviceOrder.getOrderNumber());
        } catch (Exception e) {
            meterRegistry.counter("kafka.serviceorder.processed", 
                "topic", topic, 
                "result", "error").increment();
                
            handleProcessingError(e, serviceOrder.getOrderNumber(), topic, partition, offset);
        } finally {
            MDC.remove(CORRELATION_ID);
        }
    }

    /**
     * Validates the incoming service order message.
     *
     * @param serviceOrder The service order to validate
     * @throws ServiceOrderProcessingException if validation fails
     */
    private void validateMessage(ServiceOrderProto serviceOrder) {
        if (serviceOrder.getOrderNumber() == null || serviceOrder.getOrderNumber().isEmpty()) {
            throw new ServiceOrderProcessingException("Service order number is required");
        }
        // Add additional validation as needed
    }

    /**
     * Processes the service order message and persists it to the database.
     *
     * @param serviceOrder The service order to process
     * @param correlationId The correlation ID for tracking
     * @throws ServiceOrderProcessingException if processing fails
     */
    private void processServiceOrder(ServiceOrderProto serviceOrder, String correlationId) {
        try {
            ServiceOrder entity = ServiceOrderProtoMapper.INSTANCE.toEntity(serviceOrder);
            serviceOrderRepository.save(entity);
        } catch (Exception e) {
            throw new ServiceOrderProcessingException("Failed to process service order: " + serviceOrder.getOrderNumber(), e);
        }
    }

    /**
     * Handles processing errors by logging and throwing appropriate exceptions.
     *
     * @param e The exception that occurred
     * @param orderNumber The service order number
     * @param topic The Kafka topic
     * @param partition The partition
     * @param offset The offset
     * @throws ServiceOrderProcessingException wrapped exception
     */
    private void handleProcessingError(Exception e, String orderNumber, String topic, Integer partition, Long offset) {
        log.error("Error processing ServiceOrder message - Topic: {}, Partition: {}, Offset: {}, Order Number: {}, Error: {}",
            topic, partition, offset, orderNumber, e.getMessage(), e);
        throw new ServiceOrderProcessingException("Failed to process service order: " + orderNumber, e);
    }
} 