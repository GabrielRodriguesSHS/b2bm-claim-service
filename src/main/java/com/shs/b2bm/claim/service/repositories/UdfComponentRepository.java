package com.shs.b2bm.claim.service.repositories;

import com.shs.b2bm.claim.service.entities.UdfComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link UdfComponent} entity. Provides standard CRUD operations and
 * custom query methods.
 */
@Repository
public interface UdfComponentRepository extends JpaRepository<UdfComponent, Long> {
  // Basic CRUD operations are provided by JpaRepository
  // Additional custom query methods can be added here as needed
}
