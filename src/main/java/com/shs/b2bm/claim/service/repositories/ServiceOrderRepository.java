package com.shs.b2bm.claim.service.repositories;

import com.shs.b2bm.claim.service.entities.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link ServiceOrder} entity. Provides standard CRUD operations and
 * custom query methods.
 */
@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {
    // Basic CRUD operations are provided by JpaRepository
    // Additional custom query methods can be added here as needed
}
