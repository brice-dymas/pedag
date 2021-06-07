package com.urservices.service;

import com.urservices.domain.CoursFiliere;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link CoursFiliere}.
 */
public interface CoursFiliereService {
    /**
     * Save a coursFiliere.
     *
     * @param coursFiliere the entity to save.
     * @return the persisted entity.
     */
    CoursFiliere save(CoursFiliere coursFiliere);

    /**
     * Partially updates a coursFiliere.
     *
     * @param coursFiliere the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CoursFiliere> partialUpdate(CoursFiliere coursFiliere);

    /**
     * Get all the coursFilieres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CoursFiliere> findAll(Pageable pageable);

    /**
     * Get the "id" coursFiliere.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CoursFiliere> findOne(Long id);

    /**
     * Delete the "id" coursFiliere.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
