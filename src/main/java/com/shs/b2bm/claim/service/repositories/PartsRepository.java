package com.shs.b2bm.claim.service.repositories;

import com.shs.b2bm.claim.service.entities.Parts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Parts} entity. Provides standard CRUD operations and custom query
 * methods.
 */
@Repository
public interface PartsRepository extends JpaRepository<Parts, Long> {
  // Basic CRUD operations are provided by JpaRepository
  // Additional custom query methods can be added here as needed
}
