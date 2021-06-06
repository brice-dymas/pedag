package com.urservices.service.impl;

import com.urservices.domain.AnneeAcademique;
import com.urservices.repository.AnneeAcademiqueRepository;
import com.urservices.service.AnneeAcademiqueService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AnneeAcademique}.
 */
@Service
@Transactional
public class AnneeAcademiqueServiceImpl implements AnneeAcademiqueService {

    private final Logger log = LoggerFactory.getLogger(AnneeAcademiqueServiceImpl.class);

    private final AnneeAcademiqueRepository anneeAcademiqueRepository;

    public AnneeAcademiqueServiceImpl(AnneeAcademiqueRepository anneeAcademiqueRepository) {
        this.anneeAcademiqueRepository = anneeAcademiqueRepository;
    }

    @Override
    public AnneeAcademique save(AnneeAcademique anneeAcademique) {
        log.debug("Request to save AnneeAcademique : {}", anneeAcademique);
        return anneeAcademiqueRepository.save(anneeAcademique);
    }

    @Override
    public Optional<AnneeAcademique> partialUpdate(AnneeAcademique anneeAcademique) {
        log.debug("Request to partially update AnneeAcademique : {}", anneeAcademique);

        return anneeAcademiqueRepository
            .findById(anneeAcademique.getId())
            .map(
                existingAnneeAcademique -> {
                    if (anneeAcademique.getLibelle() != null) {
                        existingAnneeAcademique.setLibelle(anneeAcademique.getLibelle());
                    }
                    if (anneeAcademique.getCurrent() != null) {
                        existingAnneeAcademique.setCurrent(anneeAcademique.getCurrent());
                    }

                    return existingAnneeAcademique;
                }
            )
            .map(anneeAcademiqueRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnneeAcademique> findAll(Pageable pageable) {
        log.debug("Request to get all AnneeAcademiques");
        return anneeAcademiqueRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AnneeAcademique> findOne(Long id) {
        log.debug("Request to get AnneeAcademique : {}", id);
        return anneeAcademiqueRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AnneeAcademique : {}", id);
        anneeAcademiqueRepository.deleteById(id);
    }
}
