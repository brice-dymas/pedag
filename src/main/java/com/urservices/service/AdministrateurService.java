package com.urservices.service;

import com.urservices.domain.Administrateur;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Administrateur}.
 */
public interface AdministrateurService {
    /**
     * Save a administrateur.
     *
     * @param administrateur the entity to save.
     * @return the persisted entity.
     */
    Administrateur save(Administrateur administrateur);

    /**
     * Partially updates a administrateur.
     *
     * @param administrateur the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Administrateur> partialUpdate(Administrateur administrateur);

    /**
     * Get all the administrateurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Administrateur> findAll(Pageable pageable);

    /**
     * Get the "id" administrateur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Administrateur> findOne(Long id);

    /**
     * Delete the "id" administrateur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
