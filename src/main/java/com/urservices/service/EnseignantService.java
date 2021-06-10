package com.urservices.service;

import com.urservices.domain.Enseignant;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Enseignant}.
 */
public interface EnseignantService {
    /**
     * Save a enseignant.
     *
     * @param enseignant the entity to save.
     * @return the persisted entity.
     */
    Enseignant save(Enseignant enseignant);

    /**
     * Partially updates a enseignant.
     *
     * @param enseignant the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Enseignant> partialUpdate(Enseignant enseignant);

    /**
     * Get all the enseignants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Enseignant> findAll(Pageable pageable);

    /**
     * Get the "id" enseignant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Enseignant> findOne(Long id);

    /**
     * Delete the "id" enseignant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get the "id" User's enseignant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Enseignant findByUserId(Long id);
}
