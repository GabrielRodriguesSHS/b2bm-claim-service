package com.shs.b2bm.claim.service.utils;

import com.shs.b2bm.claim.service.entities.ServiceOrder;
import com.shs.b2bm.claim.service.entities.ServiceOrderPart;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@NoArgsConstructor
@Component
// Just for mocking the initial implementations
public class ServiceOrderMock {
    private final Random RANDOM = new Random();;

    public int getRandomObligorId() {
        return 1 + RANDOM.nextInt(3);
    }

    public String getRandomSerialNumber() {
        int length = RANDOM.nextInt(21);
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public List<ServiceOrderPart> getRandomServiceOrderPart(ServiceOrder serviceOrder) {
        int randomNumberParts = RANDOM.nextInt(3);
        List<ServiceOrderPart> listParts = new ArrayList<ServiceOrderPart>();

        ServiceOrderPart serviceOrderPart = new ServiceOrderPart();
        serviceOrderPart.setServiceOrder(serviceOrder);
        serviceOrderPart.setSequenceNumber("123");

        for (int i = 0; i < randomNumberParts; i++) {
            listParts.add(serviceOrderPart);
        }

        return listParts;
    }

}
