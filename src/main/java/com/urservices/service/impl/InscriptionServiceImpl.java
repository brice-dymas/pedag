package com.urservices.service.impl;

import com.urservices.domain.Inscription;
import com.urservices.repository.InscriptionRepository;
import com.urservices.service.InscriptionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Inscription}.
 */
@Service
@Transactional
public class InscriptionServiceImpl implements InscriptionService {

    private final Logger log = LoggerFactory.getLogger(InscriptionServiceImpl.class);

    private final InscriptionRepository inscriptionRepository;

    public InscriptionServiceImpl(InscriptionRepository inscriptionRepository) {
        this.inscriptionRepository = inscriptionRepository;
    }

    @Override
    public Inscription save(Inscription inscription) {
        log.debug("Request to save Inscription : {}", inscription);
        return inscriptionRepository.save(inscription);
    }

    @Override
    public Optional<Inscription> partialUpdate(Inscription inscription) {
        log.debug("Request to partially update Inscription : {}", inscription);

        return inscriptionRepository
            .findById(inscription.getId())
            .map(
                existingInscription -> {
                    if (inscription.getDate() != null) {
                        existingInscription.setDate(inscription.getDate());
                    }

                    return existingInscription;
                }
            )
            .map(inscriptionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Inscription> findAll(Pageable pageable) {
        log.debug("Request to get all Inscriptions");
        return inscriptionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Inscription> findOne(Long id) {
        log.debug("Request to get Inscription : {}", id);
        return inscriptionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Inscription : {}", id);
        inscriptionRepository.deleteById(id);
    }

    /**
     * Get the an inscription using it's User's ID.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Inscription findEtudiantByUserId(Long id) {
        log.debug("Request to get an Etudiant using User ID : {}", id);
        return inscriptionRepository.findEtudiantByUserId(id);
    }

    /**
     * Get the an inscription using it's User's ID
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Inscription findByEtudiantUserId(Long id) {
        log.debug("Request to get an Etudiant using JPA User ID : {}", id);
        return inscriptionRepository.findByEtudiantUserId(id);
    }
}
