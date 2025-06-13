package com.shs.b2bm.claim.service.repositories;

import com.shs.b2bm.claim.service.entities.Merchandise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Merchandise} entity. Provides standard CRUD operations and custom
 * query methods.
 */
@Repository
public interface MerchandiseRepository extends JpaRepository<Merchandise, Long> {
}
