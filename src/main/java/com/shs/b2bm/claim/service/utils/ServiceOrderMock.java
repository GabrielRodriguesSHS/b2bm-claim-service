package com.shs.b2bm.claim.service.utils;

import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.entities.ServiceOrderPart;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@NoArgsConstructor
@Component
// Just for mocking the initial implementations
public class ServiceOrderMock {
  private final Random random = new Random();

  public int getRandomObligorId() {
    return 1 + random.nextInt(3);
  }

  public String getRandomSerialNumber() {
    int length = random.nextInt(21);
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      sb.append(chars.charAt(random.nextInt(chars.length())));
    }
    return sb.toString();
  }

  public List<ServiceOrderPart> getRandomServiceOrderPart(ServiceOrder serviceOrder) {
    int randomNumberParts = random.nextInt(3);
    List<ServiceOrderPart> listParts = new ArrayList<ServiceOrderPart>();

    for (int i = 0; i < randomNumberParts; i++) {
      ServiceOrderPart serviceOrderPart = new ServiceOrderPart();
      serviceOrderPart.setServiceOrder(serviceOrder);
      serviceOrderPart.setSequenceNumber("123");

      listParts.add(serviceOrderPart);
    }

    return listParts;
  }

  public String getRandomBrand() {
    List<String> brands = Arrays.asList("LG", "Samsung", "Whirlpool");

    return brands.get(random.nextInt(brands.size()));
  }

  public String getRandomModelNumber() {
    List<String> modelNumbers = Arrays.asList("MODEL123", "ModelTest", "NewModel");

    return modelNumbers.get(random.nextInt(modelNumbers.size()));
  }

  public int getRandomProcId() {
    return 1 + random.nextInt(3);
  }

  public String getRandomManualEntrie() {
    List<String> words = Arrays.asList(
            "test",
            "****",
            "xxxxxx",
            "technician",
            "washing machine",
            "error",
            "repair",
            "appointment",
            "schedule",
            "visit",
            "home",
            "service",
            "diagnostic",
            "issue",
            "support",
            "maintenance",
            "client",
            "request",
            "fix",
            "unit",
            "install",
            "confirm"
    );

      return words.get(random.nextInt(words.size())) +
              words.get(random.nextInt(words.size())) +
              words.get(random.nextInt(words.size())) +
              words.get(random.nextInt(words.size()));
  }
}
