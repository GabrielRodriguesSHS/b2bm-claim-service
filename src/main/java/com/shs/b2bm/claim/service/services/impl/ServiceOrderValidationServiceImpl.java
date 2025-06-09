package com.shs.b2bm.claim.service.services.impl;

import com.shs.b2bm.claim.service.entities.Merchandise;
import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.entities.ValidationResult;
import com.shs.b2bm.claim.service.kafka.proto.ServiceOrderProto;
import com.shs.b2bm.claim.service.mappers.ServiceOrderProtoMapper;
import com.shs.b2bm.claim.service.repositories.MerchandiseRepository;
import com.shs.b2bm.claim.service.repositories.ServiceOrderRepository;
import com.shs.b2bm.claim.service.services.ServiceOrderValidationService;
import com.shs.b2bm.claim.service.services.ValidationStrategyService;
import jakarta.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Implementation of ServiceOrderValidatorService for validating ServiceOrderProto messages. */
@Service
@Slf4j
public class ServiceOrderValidationServiceImpl implements ServiceOrderValidationService {

  private final List<ValidationStrategyService> listRulesImplementation;
  private final ServiceOrderProtoMapper serviceOrderProtoMapper;
  private final ServiceOrderRepository serviceOrderRepository;
  private final MerchandiseRepository merchandiseRepository;

  private final Random RANDOM; // Just for mocking the initial implementations

  public ServiceOrderValidationServiceImpl(
      List<ValidationStrategyService> listRulesImplementation,
      ServiceOrderRepository serviceOrderRepository,
      MerchandiseRepository merchandiseRepository) {
    this.listRulesImplementation = listRulesImplementation;
    this.serviceOrderProtoMapper = ServiceOrderProtoMapper.INSTANCE;
    this.merchandiseRepository = merchandiseRepository;
    this.RANDOM = new Random(); // Just for mocking the initial implementations
    this.serviceOrderRepository = serviceOrderRepository;
  }

  @Override
  public void validateServiceOrder(ServiceOrderProto serviceOrderProto) {
    if (serviceOrderProto.getServiceOrderNumber() == null
        || serviceOrderProto.getServiceOrderNumber().isEmpty()) {
      throw new ValidationException("Service order number is required");
    }

    // Merchandise flow just for mocking the initial implementations
    Merchandise merchandise = new Merchandise();
    merchandise.setSerialNumber(this.getRandomSerialNumber());
    merchandiseRepository.save(merchandise);

    ServiceOrder serviceOrder = serviceOrderProtoMapper.toEntity(serviceOrderProto);
    serviceOrder.setObligorId(this.getRandomObligorId());
    serviceOrder.setMerchandise(merchandise);

    List<ValidationResult> listValidationResult = this.rulesValidator(serviceOrder);

    serviceOrder.setValidationsResult(listValidationResult);

    serviceOrderRepository.save(serviceOrder);

    if (!listValidationResult.isEmpty()) {
      log.debug("Some rule failed.");
      log.debug(listValidationResult.toString());
    }
  }

  /**
   * Validates the incoming service order message by applying all validation rules.
   *
   * @param serviceOrder The service order to validate
   * @throws ValidationException if any rule fails
   */
  private List<ValidationResult> rulesValidator(ServiceOrder serviceOrder) {
    List<ValidationResult> listValidationResult = new ArrayList<>(Collections.emptyList());

    for (ValidationStrategyService rule : listRulesImplementation) {
      ValidationResult ruleResult = rule.validate(serviceOrder);

      listValidationResult.add(ruleResult);
    }

    return listValidationResult;
  }

  // Just for mocking the initial implementations
  private int getRandomObligorId() {
    return 1 + RANDOM.nextInt(3);
  }

  // Just for mocking the initial implementations
  private String getRandomSerialNumber() {
    int length = RANDOM.nextInt(21);
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
    }
    return sb.toString();
  }
}
