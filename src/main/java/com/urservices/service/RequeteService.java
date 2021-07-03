package com.urservices.service;

import com.urservices.domain.Requete;
import com.urservices.domain.enumeration.StatutRequete;
import com.urservices.domain.enumeration.TypeExamen;
import com.urservices.service.dto.NewRequeteDTO;
import java.util.List;
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
     * Save a requete.
     *
     * @param requete the entity to save.
     * @return the persisted entity.
     */
    Requete save(Requete requete, Long id);
    /**
     * Save a requete.
     *
     * @param requete the entity to save.
     * @return the persisted entity.
     */
    Requete save(NewRequeteDTO requete);

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

    /**
     * Get the all requete of a Student.
     *
     * @param id the id of the Student.
     * @return all the request.
     */
    Page<Requete> findAllByEtudiant(Long id, Pageable pageable);

    /**
     * Get the all requete of a Student.
     *
     * @param id the id of the Student.
     * @return all the request.
     */
    Page<Requete> findAllByEtudiant_id(Long id, Pageable pageable);

    Page<Requete> findByStatut(StatutRequete statutRequete, Pageable pageable);

    Page<Requete> findByEtudiantIdAndNoteIsNull(Long id, Pageable pageable);

    Page<Requete> findByEtudiantIdAndSessionExamen(Long id, int typeExamen, Pageable pageable);
}
