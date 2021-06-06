package com.urservices.service.impl;

import com.urservices.domain.Filiere;
import com.urservices.repository.FiliereRepository;
import com.urservices.service.FiliereService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Filiere}.
 */
@Service
@Transactional
public class FiliereServiceImpl implements FiliereService {

    private final Logger log = LoggerFactory.getLogger(FiliereServiceImpl.class);

    private final FiliereRepository filiereRepository;

    public FiliereServiceImpl(FiliereRepository filiereRepository) {
        this.filiereRepository = filiereRepository;
    }

    @Override
    public Filiere save(Filiere filiere) {
        log.debug("Request to save Filiere : {}", filiere);
        return filiereRepository.save(filiere);
    }

    @Override
    public Optional<Filiere> partialUpdate(Filiere filiere) {
        log.debug("Request to partially update Filiere : {}", filiere);

        return filiereRepository
            .findById(filiere.getId())
            .map(
                existingFiliere -> {
                    if (filiere.getLibelle() != null) {
                        existingFiliere.setLibelle(filiere.getLibelle());
                    }
                    if (filiere.getSigle() != null) {
                        existingFiliere.setSigle(filiere.getSigle());
                    }

                    return existingFiliere;
                }
            )
            .map(filiereRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Filiere> findAll(Pageable pageable) {
        log.debug("Request to get all Filieres");
        return filiereRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Filiere> findOne(Long id) {
        log.debug("Request to get Filiere : {}", id);
        return filiereRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Filiere : {}", id);
        filiereRepository.deleteById(id);
    }
}
