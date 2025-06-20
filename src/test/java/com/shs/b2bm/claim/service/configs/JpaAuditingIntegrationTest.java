package com.shs.b2bm.claim.service.configs;

import com.shs.b2bm.claim.service.entities.ServiceOrder;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for JPA auditing configuration. Verifies that auditing fields are set and
 * updated correctly on entities.
 */
@SpringBootTest
@Transactional
class JpaAuditingIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    /**
     * Test that auditing fields are set correctly when a new entity is persisted.
     */
    @Test
    void auditing_ShouldSetAuditFieldsCorrectly() {
        // Arrange
        ServiceOrder serviceOrder =
                ServiceOrder.builder()
                        .serviceUnitNumber("UNIT-INT-001")
                        .serviceOrderNumber("SO-INT-001")
                        .build();

        // Act
        entityManager.persist(serviceOrder);
        entityManager.flush();
        entityManager.clear();

        // Retrieve the persisted entity
        ServiceOrder persistedOrder =
                entityManager.find(ServiceOrder.class, serviceOrder.getServiceOrderId());

        // Assert
        assertThat(persistedOrder).isNotNull();
        assertThat(persistedOrder.getCreatedDate()).isNotNull();
        assertThat(persistedOrder.getLastModifiedDate()).isNotNull();
        assertThat(persistedOrder.getCreatedBy()).isEqualTo("b2bm-claim-service");
        assertThat(persistedOrder.getLastModifiedBy()).isEqualTo("b2bm-claim-service");
    }

    /**
     * Test that last modified fields are updated when an entity is updated.
     */
    @Test
    void auditing_ShouldUpdateLastModifiedFields() {
        // Arrange
        ServiceOrder serviceOrder =
                ServiceOrder.builder()
                        .serviceUnitNumber("UNIT-INT-002")
                        .serviceOrderNumber("SO-INT-002")
                        .build();

        // Act - First save
        entityManager.persist(serviceOrder);
        entityManager.flush();
        entityManager.clear();

        // Retrieve and modify
        ServiceOrder persistedOrder =
                entityManager.find(ServiceOrder.class, serviceOrder.getServiceOrderId());
        persistedOrder.setServiceUnitNumber("UPDATED-UNIT-002");

        // Save changes
        entityManager.merge(persistedOrder);
        entityManager.flush();
        entityManager.clear();

        // Retrieve updated entity
        ServiceOrder updatedOrder =
                entityManager.find(ServiceOrder.class, serviceOrder.getServiceOrderId());

        // Assert
        assertThat(updatedOrder).isNotNull();
        assertThat(updatedOrder.getServiceUnitNumber()).isEqualTo("UPDATED-UNIT-002");
        assertThat(updatedOrder.getCreatedDate()).isNotNull();
        assertThat(updatedOrder.getLastModifiedDate()).isNotNull();
        assertThat(updatedOrder.getCreatedBy()).isEqualTo("b2bm-claim-service");
        assertThat(updatedOrder.getLastModifiedBy()).isEqualTo("b2bm-claim-service");

        // Modified date should be after created date after an update
        assertThat(updatedOrder.getLastModifiedDate()).isAfterOrEqualTo(updatedOrder.getCreatedDate());
    }
}
