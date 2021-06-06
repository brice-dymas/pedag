package com.urservices.service.impl;

import com.urservices.domain.Examen;
import com.urservices.repository.ExamenRepository;
import com.urservices.service.ExamenService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Examen}.
 */
@Service
@Transactional
public class ExamenServiceImpl implements ExamenService {

    private final Logger log = LoggerFactory.getLogger(ExamenServiceImpl.class);

    private final ExamenRepository examenRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository) {
        this.examenRepository = examenRepository;
    }

    @Override
    public Examen save(Examen examen) {
        log.debug("Request to save Examen : {}", examen);
        return examenRepository.save(examen);
    }

    @Override
    public Optional<Examen> partialUpdate(Examen examen) {
        log.debug("Request to partially update Examen : {}", examen);

        return examenRepository
            .findById(examen.getId())
            .map(
                existingExamen -> {
                    if (examen.getDateExamen() != null) {
                        existingExamen.setDateExamen(examen.getDateExamen());
                    }
                    if (examen.getType() != null) {
                        existingExamen.setType(examen.getType());
                    }
                    if (examen.getSemestre() != null) {
                        existingExamen.setSemestre(examen.getSemestre());
                    }

                    return existingExamen;
                }
            )
            .map(examenRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Examen> findAll(Pageable pageable) {
        log.debug("Request to get all Examen");
        return examenRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Examen> findOne(Long id) {
        log.debug("Request to get Examen : {}", id);
        return examenRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Examen : {}", id);
        examenRepository.deleteById(id);
    }
}
