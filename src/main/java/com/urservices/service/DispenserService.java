package com.urservices.service;

import com.urservices.domain.Dispenser;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Dispenser}.
 */
public interface DispenserService {
    /**
     * Save a dispenser.
     *
     * @param dispenser the entity to save.
     * @return the persisted entity.
     */
    Dispenser save(Dispenser dispenser);

    /**
     * Partially updates a dispenser.
     *
     * @param dispenser the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Dispenser> partialUpdate(Dispenser dispenser);

    /**
     * Get all the dispensers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Dispenser> findAll(Pageable pageable);

    /**
     * Get the "id" dispenser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Dispenser> findOne(Long id);

    /**
     * Delete the "id" dispenser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
