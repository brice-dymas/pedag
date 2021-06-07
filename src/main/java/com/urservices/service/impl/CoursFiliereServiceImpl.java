package com.urservices.service.impl;

import com.urservices.domain.CoursFiliere;
import com.urservices.repository.CoursFiliereRepository;
import com.urservices.service.CoursFiliereService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CoursFiliere}.
 */
@Service
@Transactional
public class CoursFiliereServiceImpl implements CoursFiliereService {

    private final Logger log = LoggerFactory.getLogger(CoursFiliereServiceImpl.class);

    private final CoursFiliereRepository coursFiliereRepository;

    public CoursFiliereServiceImpl(CoursFiliereRepository coursFiliereRepository) {
        this.coursFiliereRepository = coursFiliereRepository;
    }

    @Override
    public CoursFiliere save(CoursFiliere coursFiliere) {
        log.debug("Request to save CoursFiliere : {}", coursFiliere);
        return coursFiliereRepository.save(coursFiliere);
    }

    @Override
    public Optional<CoursFiliere> partialUpdate(CoursFiliere coursFiliere) {
        log.debug("Request to partially update CoursFiliere : {}", coursFiliere);

        return coursFiliereRepository
            .findById(coursFiliere.getId())
            .map(
                existingCoursFiliere -> {
                    if (coursFiliere.getQuotaHoraire() != null) {
                        existingCoursFiliere.setQuotaHoraire(coursFiliere.getQuotaHoraire());
                    }

                    return existingCoursFiliere;
                }
            )
            .map(coursFiliereRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CoursFiliere> findAll(Pageable pageable) {
        log.debug("Request to get all CoursFilieres");
        return coursFiliereRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CoursFiliere> findOne(Long id) {
        log.debug("Request to get CoursFiliere : {}", id);
        return coursFiliereRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CoursFiliere : {}", id);
        coursFiliereRepository.deleteById(id);
    }
}
