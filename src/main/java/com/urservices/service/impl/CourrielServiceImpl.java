package com.urservices.service.impl;

import com.urservices.domain.Courriel;
import com.urservices.repository.CourrielRepository;
import com.urservices.service.CourrielService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Courriel}.
 */
@Service
@Transactional
public class CourrielServiceImpl implements CourrielService {

    private final Logger log = LoggerFactory.getLogger(CourrielServiceImpl.class);

    private final CourrielRepository courrielRepository;

    public CourrielServiceImpl(CourrielRepository courrielRepository) {
        this.courrielRepository = courrielRepository;
    }

    @Override
    public Courriel save(Courriel courriel) {
        log.debug("Request to save Courriel : {}", courriel);
        return courrielRepository.save(courriel);
    }

    @Override
    public Optional<Courriel> partialUpdate(Courriel courriel) {
        log.debug("Request to partially update Courriel : {}", courriel);

        return courrielRepository
            .findById(courriel.getId())
            .map(
                existingCourriel -> {
                    if (courriel.getSender() != null) {
                        existingCourriel.setSender(courriel.getSender());
                    }
                    if (courriel.getReceiver() != null) {
                        existingCourriel.setReceiver(courriel.getReceiver());
                    }
                    if (courriel.getObjet() != null) {
                        existingCourriel.setObjet(courriel.getObjet());
                    }
                    if (courriel.getMessage() != null) {
                        existingCourriel.setMessage(courriel.getMessage());
                    }
                    if (courriel.getDateCreation() != null) {
                        existingCourriel.setDateCreation(courriel.getDateCreation());
                    }

                    return existingCourriel;
                }
            )
            .map(courrielRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Courriel> findAll(Pageable pageable) {
        log.debug("Request to get all Courriels");
        return courrielRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Courriel> findOne(Long id) {
        log.debug("Request to get Courriel : {}", id);
        return courrielRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Courriel : {}", id);
        courrielRepository.deleteById(id);
    }
}
