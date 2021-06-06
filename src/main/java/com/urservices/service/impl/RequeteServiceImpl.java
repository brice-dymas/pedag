package com.urservices.service.impl;

import com.urservices.domain.Requete;
import com.urservices.repository.RequeteRepository;
import com.urservices.service.RequeteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Requete}.
 */
@Service
@Transactional
public class RequeteServiceImpl implements RequeteService {

    private final Logger log = LoggerFactory.getLogger(RequeteServiceImpl.class);

    private final RequeteRepository requeteRepository;

    public RequeteServiceImpl(RequeteRepository requeteRepository) {
        this.requeteRepository = requeteRepository;
    }

    @Override
    public Requete save(Requete requete) {
        log.debug("Request to save Requete : {}", requete);
        return requeteRepository.save(requete);
    }

    @Override
    public Optional<Requete> partialUpdate(Requete requete) {
        log.debug("Request to partially update Requete : {}", requete);

        return requeteRepository
            .findById(requete.getId())
            .map(
                existingRequete -> {
                    if (requete.getObjet() != null) {
                        existingRequete.setObjet(requete.getObjet());
                    }
                    if (requete.getDescription() != null) {
                        existingRequete.setDescription(requete.getDescription());
                    }
                    if (requete.getStatut() != null) {
                        existingRequete.setStatut(requete.getStatut());
                    }
                    if (requete.getTraiter() != null) {
                        existingRequete.setTraiter(requete.getTraiter());
                    }
                    if (requete.getDateCreation() != null) {
                        existingRequete.setDateCreation(requete.getDateCreation());
                    }
                    if (requete.getDateModification() != null) {
                        existingRequete.setDateModification(requete.getDateModification());
                    }

                    return existingRequete;
                }
            )
            .map(requeteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Requete> findAll(Pageable pageable) {
        log.debug("Request to get all Requetes");
        return requeteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Requete> findOne(Long id) {
        log.debug("Request to get Requete : {}", id);
        return requeteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Requete : {}", id);
        requeteRepository.deleteById(id);
    }
}
