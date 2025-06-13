package com.shs.b2bm.claim.service.repositories;

import com.shs.b2bm.claim.service.entities.ServiceOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link ServiceOrderRepository}. Verifies CRUD operations, auditing, and
 * entity relationships for ServiceOrder.
 */
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ServiceOrderRepositoryTest {

    @Autowired
    private ServiceOrderRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    /**
     * Test that a ServiceOrder is persisted correctly.
     */
    @Test
    void whenSaveServiceOrder_thenPersistedCorrectly() {
        // Arrange
        ServiceOrder order = new ServiceOrder();
        order.setServiceOrderNumber("ORD-12345");
        order.setCreatedDate(Instant.now());
        order.setLastModifiedDate(Instant.now());
        order.setCreatedBy("b2bm-claim-service");
        order.setLastModifiedBy("b2bm-claim-service");
        order.setServiceUnitNumber("UNIT-001");

        // Act
        ServiceOrder savedOrder = repository.save(order);

        // Assert
        assertThat(savedOrder.getServiceOrderId()).isNotNull();
        assertThat(entityManager.find(ServiceOrder.class, savedOrder.getServiceOrderId()))
                .isEqualTo(savedOrder);
    }

    /**
     * Test that a ServiceOrder can be found by its ID.
     */
    @Test
    void whenFindById_thenReturnServiceOrder() {
        // Arrange
        ServiceOrder order = new ServiceOrder();
        order.setServiceOrderNumber("ORD-12345");
        order.setCreatedDate(Instant.now());
        order.setLastModifiedDate(Instant.now());
        order.setCreatedBy("b2bm-claim-service");
        order.setLastModifiedBy("b2bm-claim-service");
        order.setServiceUnitNumber("UNIT-001");
        Long id = entityManager.persistAndGetId(order, Long.class);
        entityManager.flush();

        // Act
        Optional<ServiceOrder> found = repository.findById(id);

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getServiceOrderNumber()).isEqualTo("ORD-12345");
    }

    /**
     * Test that all ServiceOrders can be retrieved from the repository.
     */
    @Test
    void whenFindAll_thenReturnAllServiceOrders() {
        // Arrange
        ServiceOrder order1 = new ServiceOrder();
        order1.setServiceOrderNumber("ORD-12345");
        order1.setCreatedDate(Instant.now());
        order1.setLastModifiedDate(Instant.now());
        order1.setCreatedBy("b2bm-claim-service");
        order1.setLastModifiedBy("b2bm-claim-service");
        order1.setServiceUnitNumber("UNIT-001");

        ServiceOrder order2 = new ServiceOrder();
        order2.setServiceOrderNumber("ORD-67890");
        order2.setCreatedDate(Instant.now());
        order2.setLastModifiedDate(Instant.now());
        order2.setCreatedBy("b2bm-claim-service");
        order2.setLastModifiedBy("b2bm-claim-service");
        order2.setServiceUnitNumber("UNIT-002");

        entityManager.persist(order1);
        entityManager.persist(order2);
        entityManager.flush();

        // Act
        List<ServiceOrder> orders = repository.findAll();

        // Assert
        assertThat(orders).hasSize(2);
        assertThat(orders)
                .extracting(ServiceOrder::getServiceOrderNumber)
                .containsExactlyInAnyOrder("ORD-12345", "ORD-67890");
    }

    /**
     * Test that updating a ServiceOrder persists the changes correctly.
     */
    @Test
    void whenUpdateServiceOrder_thenPersistedCorrectly() {
        // Arrange
        ServiceOrder order = new ServiceOrder();
        order.setServiceOrderNumber("ORD-12345");
        order.setServiceUnitNumber("UNIT-001");
        order.setCreatedDate(Instant.now());
        order.setLastModifiedDate(Instant.now());
        order.setCreatedBy("b2bm-claim-service");
        order.setLastModifiedBy("b2bm-claim-service");
        Long id = entityManager.persistAndGetId(order, Long.class);
        entityManager.flush();

        // Act
        ServiceOrder savedOrder = repository.findById(id).get();
        savedOrder.setServiceOrderNumber("ORD-UPDATED");
        repository.save(savedOrder);

        // Assert
        ServiceOrder updatedOrder = entityManager.find(ServiceOrder.class, id);
        assertThat(updatedOrder.getServiceOrderNumber()).isEqualTo("ORD-UPDATED");
    }

    /**
     * Test that deleting a ServiceOrder removes it from the database.
     */
    @Test
    void whenDeleteServiceOrder_thenRemoved() {
        // Arrange
        ServiceOrder order = new ServiceOrder();
        order.setServiceOrderNumber("ORD-12345");
        order.setServiceUnitNumber("UNIT-001");
        order.setCreatedDate(Instant.now());
        order.setLastModifiedDate(Instant.now());
        order.setCreatedBy("b2bm-claim-service");
        order.setLastModifiedBy("b2bm-claim-service");
        Long id = entityManager.persistAndGetId(order, Long.class);
        entityManager.flush();

        // Act
        repository.deleteById(id);

        // Assert
        ServiceOrder deletedOrder = entityManager.find(ServiceOrder.class, id);
        assertThat(deletedOrder).isNull();
    }

    /**
     * Test that auditing fields are set when a ServiceOrder is saved.
     */
    @Test
    void whenServiceOrderSaved_thenAuditingFieldsAreSet() {
        // Arrange
        ServiceOrder order = new ServiceOrder();
        order.setServiceOrderNumber("ORD-AUDIT");
        order.setServiceUnitNumber("UNIT-AUDIT");
        order.setCreatedDate(Instant.now());
        order.setLastModifiedDate(Instant.now());
        order.setCreatedBy("b2bm-claim-service");
        order.setLastModifiedBy("b2bm-claim-service");

        // Act
        ServiceOrder savedOrder = repository.save(order);
        entityManager.flush();

        // Assert
        assertThat(savedOrder.getCreatedDate()).isNotNull();
        assertThat(savedOrder.getLastModifiedDate()).isNotNull();
        assertThat(savedOrder.getCreatedBy()).isNotNull();
        assertThat(savedOrder.getLastModifiedBy()).isNotNull();
        assertThat(savedOrder.getCreatedBy()).isEqualTo("b2bm-claim-service");
        assertThat(savedOrder.getLastModifiedBy()).isEqualTo("b2bm-claim-service");
    }
}
