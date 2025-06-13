package com.shs.b2bm.claim.service.repositories;

import com.shs.b2bm.claim.service.entities.ValidationConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link ValidationConfig} entity. Provides standard CRUD operations and
 * custom query methods.
 */
@Repository
public interface ValidationConfigRepository extends JpaRepository<ValidationConfig, Long> {
    /**
     * Finds all rule validation configurations where obligorId matches the given value or is null.
     * This allows retrieving both partner-specific rules and global rules in a single query.
     *
     * @param obligorId the obligor to search for
     * @return list of rule validation configurations matching the criteria
     */
    List<ValidationConfig> findByObligorIdOrObligorIdIsNull(Integer obligorId);
}
