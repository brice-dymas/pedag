package com.urservices.service;

import com.urservices.domain.Courriel;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Courriel}.
 */
public interface CourrielService {
    /**
     * Save a courriel.
     *
     * @param courriel the entity to save.
     * @return the persisted entity.
     */
    Courriel save(Courriel courriel);

    /**
     * Partially updates a courriel.
     *
     * @param courriel the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Courriel> partialUpdate(Courriel courriel);

    /**
     * Get all the courriels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Courriel> findAll(Pageable pageable);

    /**
     * Get all the courriels of a given student.
     *
     * @param id Student's users id
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Courriel> findByEtudiantUserId(Long id, Pageable pageable);

    /**
     * Get the "id" courriel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Courriel> findOne(Long id);

    /**
     * Delete the "id" courriel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
