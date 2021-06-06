package com.urservices.service;

import com.urservices.domain.AnneeAcademique;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link AnneeAcademique}.
 */
public interface AnneeAcademiqueService {
    /**
     * Save a anneeAcademique.
     *
     * @param anneeAcademique the entity to save.
     * @return the persisted entity.
     */
    AnneeAcademique save(AnneeAcademique anneeAcademique);

    /**
     * Partially updates a anneeAcademique.
     *
     * @param anneeAcademique the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AnneeAcademique> partialUpdate(AnneeAcademique anneeAcademique);

    /**
     * Get all the anneeAcademiques.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnneeAcademique> findAll(Pageable pageable);

    /**
     * Get the "id" anneeAcademique.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnneeAcademique> findOne(Long id);

    /**
     * Delete the "id" anneeAcademique.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
