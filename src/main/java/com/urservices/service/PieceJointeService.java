package com.urservices.service;

import com.urservices.domain.PieceJointe;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link PieceJointe}.
 */
public interface PieceJointeService {
    /**
     * Save a pieceJointe of a specific Matiere.
     *
     * @param pieceJointe the entity to save.
     * @return the persisted entity.
     */
    PieceJointe save(Long id, PieceJointe pieceJointe);
    /**
     * Save a pieceJointe.
     *
     * @param pieceJointe the entity to save.
     * @return the persisted entity.
     */
    PieceJointe save(PieceJointe pieceJointe);

    /**
     * Partially updates a pieceJointe.
     *
     * @param pieceJointe the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PieceJointe> partialUpdate(PieceJointe pieceJointe);

    /**
     * Get all the pieceJointes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PieceJointe> findAll(Pageable pageable);

    /**
     * Get the "id" pieceJointe.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PieceJointe> findOne(Long id);

    /**
     * Get the "id" of a matiere for pieceJointe.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    PieceJointe findOneForSet(Long id);

    /**
     * Delete the "id" pieceJointe.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the pieceJointes of a given Matiere.
     *
     * @param id The identifier of the desired matiere
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PieceJointe> findByMatiereId(Long id, Pageable pageable);
}
