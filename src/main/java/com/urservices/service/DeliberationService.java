package com.urservices.service;

import com.urservices.domain.Deliberation;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Deliberation}.
 */
public interface DeliberationService {
    /**
     * Save a deliberation.
     *
     * @param deliberation the entity to save.
     * @return the persisted entity.
     */
    Deliberation save(Deliberation deliberation);

    /**
     * Partially updates a deliberation.
     *
     * @param deliberation the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Deliberation> partialUpdate(Deliberation deliberation);

    /**
     * Get all the deliberations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Deliberation> findAll(Pageable pageable);

    /**
     * Get the "id" deliberation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Deliberation> findOne(Long id);

    /**
     * Delete the "id" deliberation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
