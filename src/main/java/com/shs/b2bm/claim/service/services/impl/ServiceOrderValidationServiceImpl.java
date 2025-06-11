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
import com.shs.b2bm.claim.service.utils.ServiceOrderMock;
import jakarta.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

  private final ServiceOrderMock serviceOrderMock; // Just for mocking the initial implementations

  public ServiceOrderValidationServiceImpl(
      List<ValidationStrategyService> listRulesImplementation,
      ServiceOrderRepository serviceOrderRepository,
      MerchandiseRepository merchandiseRepository,
      ServiceOrderMock serviceOrderMock) {
    this.listRulesImplementation = listRulesImplementation;
    this.serviceOrderProtoMapper = ServiceOrderProtoMapper.INSTANCE;
    this.merchandiseRepository = merchandiseRepository;
    this.serviceOrderMock = serviceOrderMock; // Just for mocking the initial implementations
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
    merchandise.setSerialNumber(serviceOrderMock.getRandomSerialNumber());
    merchandise.setBrandName(serviceOrderMock.getRandomBrand());
    merchandise.setModelNumber(serviceOrderMock.getRandomModelNumber());
    merchandiseRepository.save(merchandise);

    ServiceOrder serviceOrder = serviceOrderProtoMapper.toEntity(serviceOrderProto);
    serviceOrder.setObligorId(serviceOrderMock.getRandomObligorId());
    serviceOrder.setMerchandise(merchandise);
    serviceOrder.setParts(serviceOrderMock.getRandomServiceOrderPart(serviceOrder));
    serviceOrder.setProcId(serviceOrderMock.getRandomProcId());
    serviceOrder.setTechnicianNotes(serviceOrderMock.getRandomManualEntrie());
    serviceOrder.setServiceDescriptions(serviceOrderMock.getRandomManualEntrie());

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
}
