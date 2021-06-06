package com.urservices.service.impl;

import com.urservices.domain.Etudiant;
import com.urservices.repository.EtudiantRepository;
import com.urservices.service.EtudiantService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Etudiant}.
 */
@Service
@Transactional
public class EtudiantServiceImpl implements EtudiantService {

    private final Logger log = LoggerFactory.getLogger(EtudiantServiceImpl.class);

    private final EtudiantRepository etudiantRepository;

    public EtudiantServiceImpl(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }

    @Override
    public Etudiant save(Etudiant etudiant) {
        log.debug("Request to save Etudiant : {}", etudiant);
        return etudiantRepository.save(etudiant);
    }

    @Override
    public Optional<Etudiant> partialUpdate(Etudiant etudiant) {
        log.debug("Request to partially update Etudiant : {}", etudiant);

        return etudiantRepository
            .findById(etudiant.getId())
            .map(
                existingEtudiant -> {
                    if (etudiant.getMatricule() != null) {
                        existingEtudiant.setMatricule(etudiant.getMatricule());
                    }
                    if (etudiant.getNom() != null) {
                        existingEtudiant.setNom(etudiant.getNom());
                    }
                    if (etudiant.getDateNaissance() != null) {
                        existingEtudiant.setDateNaissance(etudiant.getDateNaissance());
                    }
                    if (etudiant.getPrenom() != null) {
                        existingEtudiant.setPrenom(etudiant.getPrenom());
                    }
                    if (etudiant.getEmail() != null) {
                        existingEtudiant.setEmail(etudiant.getEmail());
                    }
                    if (etudiant.getTelephone() != null) {
                        existingEtudiant.setTelephone(etudiant.getTelephone());
                    }
                    if (etudiant.getPhoto() != null) {
                        existingEtudiant.setPhoto(etudiant.getPhoto());
                    }
                    if (etudiant.getPhotoContentType() != null) {
                        existingEtudiant.setPhotoContentType(etudiant.getPhotoContentType());
                    }

                    return existingEtudiant;
                }
            )
            .map(etudiantRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Etudiant> findAll(Pageable pageable) {
        log.debug("Request to get all Etudiants");
        return etudiantRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Etudiant> findOne(Long id) {
        log.debug("Request to get Etudiant : {}", id);
        return etudiantRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Etudiant : {}", id);
        etudiantRepository.deleteById(id);
    }
}
