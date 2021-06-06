package com.urservices.service.impl;

import com.urservices.domain.Administrateur;
import com.urservices.repository.AdministrateurRepository;
import com.urservices.service.AdministrateurService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Administrateur}.
 */
@Service
@Transactional
public class AdministrateurServiceImpl implements AdministrateurService {

    private final Logger log = LoggerFactory.getLogger(AdministrateurServiceImpl.class);

    private final AdministrateurRepository administrateurRepository;

    public AdministrateurServiceImpl(AdministrateurRepository administrateurRepository) {
        this.administrateurRepository = administrateurRepository;
    }

    @Override
    public Administrateur save(Administrateur administrateur) {
        log.debug("Request to save Administrateur : {}", administrateur);
        return administrateurRepository.save(administrateur);
    }

    @Override
    public Optional<Administrateur> partialUpdate(Administrateur administrateur) {
        log.debug("Request to partially update Administrateur : {}", administrateur);

        return administrateurRepository
            .findById(administrateur.getId())
            .map(
                existingAdministrateur -> {
                    if (administrateur.getNom() != null) {
                        existingAdministrateur.setNom(administrateur.getNom());
                    }
                    if (administrateur.getPrenom() != null) {
                        existingAdministrateur.setPrenom(administrateur.getPrenom());
                    }
                    if (administrateur.getEmail() != null) {
                        existingAdministrateur.setEmail(administrateur.getEmail());
                    }
                    if (administrateur.getGrade() != null) {
                        existingAdministrateur.setGrade(administrateur.getGrade());
                    }

                    return existingAdministrateur;
                }
            )
            .map(administrateurRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Administrateur> findAll(Pageable pageable) {
        log.debug("Request to get all Administrateurs");
        return administrateurRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Administrateur> findOne(Long id) {
        log.debug("Request to get Administrateur : {}", id);
        return administrateurRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Administrateur : {}", id);
        administrateurRepository.deleteById(id);
    }
}
