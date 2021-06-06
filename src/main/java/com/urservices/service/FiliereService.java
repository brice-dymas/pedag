package com.urservices.service;

import com.urservices.domain.Filiere;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Filiere}.
 */
public interface FiliereService {
    /**
     * Save a filiere.
     *
     * @param filiere the entity to save.
     * @return the persisted entity.
     */
    Filiere save(Filiere filiere);

    /**
     * Partially updates a filiere.
     *
     * @param filiere the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Filiere> partialUpdate(Filiere filiere);

    /**
     * Get all the filieres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Filiere> findAll(Pageable pageable);

    /**
     * Get the "id" filiere.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Filiere> findOne(Long id);

    /**
     * Delete the "id" filiere.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
