package com.shs.b2bm.claim.service.kafka.consumer;

import com.shs.b2bm.claim.service.exceptions.ServiceOrderProcessingException;
import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;
import com.shs.b2bm.claim.service.services.ServiceOrderValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * Kafka consumer service for processing ServiceOrder messages. Handles the consumption and
 * persistence of service order data from Kafka topics. Implements correlation tracking and metrics
 * collection for monitoring.
 */
@Slf4j
@Service
public class ServiceOrderKafkaConsumer {

  private final ServiceOrderValidatorService serviceOrderValidatorService;

  public ServiceOrderKafkaConsumer(ServiceOrderValidatorService serviceOrderValidatorService) {
    this.serviceOrderValidatorService = serviceOrderValidatorService;
  }

  /**
   * Consumes ServiceOrder messages from the specified Kafka topic. Converts the proto message to an
   * entity and persists it to the database. Implements correlation tracking and metrics collection.
   *
   * @param serviceOrder The ServiceOrder proto message
   * @param topic The Kafka topic from which the message was received
   * @param partition The partition from which the message was received
   * @param offset The offset of the message
   */
  @KafkaListener(
      topics = "${spring.kafka.topics.service-order-created}",
      groupId = "${spring.kafka.consumer.group-id:b2bm-claim-service}",
      containerFactory = "serviceOrderKafkaListenerContainerFactory")
  public void listenWithHeaders(
      @Payload ServiceOrderProto serviceOrder,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
      @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
      @Header(KafkaHeaders.OFFSET) Long offset) {
    try {
      this.serviceOrderValidatorService.validateMessage(serviceOrder);

      log.info("Successfully processed and saved ServiceOrder: {}", serviceOrder.getOrderNumber());
    } catch (Exception e) {
      handleProcessingError(e, serviceOrder.getOrderNumber(), topic, partition, offset);
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
  private void handleProcessingError(
      Exception e, String orderNumber, String topic, Integer partition, Long offset) {
    log.error(
        "Error processing ServiceOrder message - Topic: {}, Partition: {}, Offset: {}, Order Number: {}, Error: {}",
        topic,
        partition,
        offset,
        orderNumber,
        e.getMessage(),
        e);
    throw new ServiceOrderProcessingException("Failed to process service order: " + orderNumber, e);
  }
}
