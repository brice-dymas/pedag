package com.urservices.service.impl;

import com.urservices.domain.Inscription;
import com.urservices.domain.Note;
import com.urservices.domain.Requete;
import com.urservices.domain.enumeration.StatutRequete;
import com.urservices.repository.InscriptionRepository;
import com.urservices.repository.NoteRepository;
import com.urservices.repository.RequeteRepository;
import com.urservices.service.RequeteService;
import com.urservices.service.dto.NewRequeteDTO;
import com.urservices.utils.RequeteHelper;
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
    private final NoteRepository noteRepository;
    private final InscriptionRepository inscriptionRepository;

    public RequeteServiceImpl(
        RequeteRepository requeteRepository,
        NoteRepository noteRepository,
        InscriptionRepository inscriptionRepository
    ) {
        this.requeteRepository = requeteRepository;
        this.noteRepository = noteRepository;
        this.inscriptionRepository = inscriptionRepository;
    }

    @Override
    public Requete save(Requete requete, Long id) {
        final Inscription inscription = inscriptionRepository.findEtudiantByUserId(id);
        requete.setEtudiant(inscription);
        requete.setStatut(StatutRequete.EN_ATTENTE);
        log.debug("Request to save Requete : {}", requete);
        return requeteRepository.save(requete);
    }

    @Override
    public Requete save(Requete requete) {
        log.debug("Request to save Requete : {}", requete);
        if (requete.getId() == null) requete.setStatut(StatutRequete.EN_ATTENTE);
        return requeteRepository.save(requete);
    }

    /**
     * Save a requete.
     *
     * @param param the entity to sa ve.
     * @return the persisted entity.
     */
    @Override
    public Requete save(NewRequeteDTO param) {
        final Inscription inscription = inscriptionRepository.findEtudiantByUserId(param.getUserId());
        var req = RequeteHelper.newRequeteDtoToRequetePost(param, inscription);
        return requeteRepository.save(req);
    }

    private final String getObservation(Float moyenne) {
        String obs = "EL";
        if (moyenne != null) {
            if (moyenne <= 9) {
                obs = "NV";
            } else {
                obs = "VA";
            }
        }
        return obs;
    }

    void updateStudentNote(Requete requete) {
        if (requete.getNote() != null && requete.getStatut().equals(StatutRequete.FONDE)) {
            Note note = requete.getNote();
            note.setMoyenne(requete.getNoteAttendue());
            note.setCreditObtenu(note.getMoyenne() < 10 ? 0 : note.getMatiere().getCredit());
            note.setObservation(getObservation(note.getMoyenne()));
            noteRepository.save(note);
        }
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
                    updateStudentNote(existingRequete);
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

    /**
     * Get the all requete of a Student.
     *
     * @param id the id of the Student.
     * @return all the request.
     */
    @Override
    public Page<Requete> findAllByEtudiant(Long id, Pageable pageable) {
        log.debug("Request to get all Requete of student : {}", id);
        return requeteRepository.rechercherParEtudiant(id, pageable);
    }

    /**
     * Get the all requete of a Student.
     *
     * @param id       the id of the Student.
     * @param pageable
     * @return all the request.
     */
    @Override
    public Page<Requete> findAllByEtudiant_id(Long id, Pageable pageable) {
        log.debug("Request to get all Requete of student : {}", id);
        return requeteRepository.findByEtudiant_Etudiant_UserId(id, pageable);
    }

    @Override
    public Page<Requete> findByStatut(StatutRequete statutRequete, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Requete> findByEtudiantIdAndNoteIsNull(Long id, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Requete> findByEtudiantIdAndSessionExamen(Long id, int typeExamen, Pageable pageable) {
        return null;
    }
}
