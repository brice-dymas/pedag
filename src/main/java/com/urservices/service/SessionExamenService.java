package com.urservices.service;

import com.urservices.domain.SessionExamen;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link SessionExamen}.
 */
public interface SessionExamenService {
    /**
     * Save a sessionExamen.
     *
     * @param sessionExamen the entity to save.
     * @return the persisted entity.
     */
    SessionExamen save(SessionExamen sessionExamen);

    /**
     * Partially updates a sessionExamen.
     *
     * @param sessionExamen the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SessionExamen> partialUpdate(SessionExamen sessionExamen);

    /**
     * Get all the sessionExamen.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SessionExamen> findAll(Pageable pageable);

    /**
     * Get all actives sessionExamen.
     *
     * @return the list of entities.
     */
    List<SessionExamen> findAllActiveSession();

    /**
     * Get the "id" sessionExamen.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SessionExamen> findOne(Long id);

    /**
     * Delete the "id" sessionExamen.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
