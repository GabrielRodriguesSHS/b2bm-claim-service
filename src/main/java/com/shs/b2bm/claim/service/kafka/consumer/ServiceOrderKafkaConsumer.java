package com.shs.b2bm.claim.service.kafka.consumer;

import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;
import com.shs.b2bm.claim.service.services.ServiceOrderValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
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

  private final ServiceOrderValidationService serviceOrderValidationService;

  public ServiceOrderKafkaConsumer(ServiceOrderValidationService serviceOrderValidationService) {
    this.serviceOrderValidationService = serviceOrderValidationService;
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
      this.serviceOrderValidationService.validateMessage(serviceOrder);

      log.info("Successfully processed and saved ServiceOrder: {}", serviceOrder.getOrderNumber());
    } catch (Exception e) {
      throw new KafkaException("Failed to process Service Order message", e);
    }
  }
}
