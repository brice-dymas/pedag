package com.urservices.service;

import com.urservices.domain.Requete;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Requete}.
 */
public interface RequeteService {
    /**
     * Save a requete.
     *
     * @param requete the entity to save.
     * @return the persisted entity.
     */
    Requete save(Requete requete);

    /**
     * Partially updates a requete.
     *
     * @param requete the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Requete> partialUpdate(Requete requete);

    /**
     * Get all the requetes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Requete> findAll(Pageable pageable);

    /**
     * Get the "id" requete.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Requete> findOne(Long id);

    /**
     * Delete the "id" requete.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
