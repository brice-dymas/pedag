package com.urservices.service.impl;

import com.urservices.domain.Filiere;
import com.urservices.domain.Note;
import com.urservices.domain.SessionExamen;
import com.urservices.domain.enumeration.TypeExamen;
import com.urservices.repository.NoteRepository;
import com.urservices.service.NoteService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Note}.
 */
@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    private final Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public Note save(Note note) {
        log.debug("Request to save Note : {}", note);
        return noteRepository.save(note);
    }

    @Override
    public Optional<Note> partialUpdate(Note note) {
        log.debug("Request to partially update Note : {}", note);

        return noteRepository
            .findById(note.getId())
            .map(
                existingNote -> {
                    if (note.getMoyenne() != null) {
                        existingNote.setMoyenne(note.getMoyenne());
                    }
                    if (note.getObservation() != null) {
                        existingNote.setObservation(note.getObservation());
                    }

                    return existingNote;
                }
            )
            .map(noteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Note> findAll(Pageable pageable) {
        log.debug("Request to get all Notes");
        return noteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Note> findOne(Long id) {
        log.debug("Request to get Note : {}", id);
        return noteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Note : {}", id);
        noteRepository.deleteById(id);
    }

    /**
     * Get all notes of a Student.
     *
     * @param id the id of the Student.
     * @return the list of notes.
     */
    @Override
    public Page<Note> findByEtudiantId(Long id, Pageable pageable) {
        log.debug("Request to get all Motes of student : {}", id);
        //        return noteRepository.findByEtudiantId(id, pageable);
        return noteRepository.findByEtudiant_EtudiantUserIdOrderByIdDesc(id, pageable);
    }

    /**
     * Get all notes of a Teacher.
     *
     * @param id       the id of the Teacher User Account.
     * @param pageable
     * @return the list of notes.
     */
    @Override
    public Page<Note> findByEnseignantId(Long id, Pageable pageable) {
        return noteRepository.findByEnseignantUserIdOrderByIdDesc(id, pageable);
    }

    @Override
    public List<Note> findBySessionExamenAndMoyenneAndExamenType(SessionExamen sessionExamen, Float moyenne, TypeExamen typeExamen) {
        //        return noteRepository.findBySessionExamenIdAndMoyenneEqualsAndSessionExamenType(sessionExamen.getId(), moyenne, typeExamen);
        return null;
    }

    @Override
    public List<Note> findBySessionExamenAndMoyenneAndExamenTypeAndFiliere(
        SessionExamen sessionExamen,
        Float moyenne,
        TypeExamen typeExamen,
        Filiere filiere
    ) {
        //        return noteRepository.findBySessionExamenIdAndMoyenneEqualsAndSessionExamenTypeAndEtudiantFiliere(sessionExamen.getId(), moyenne, typeExamen, filiere.getId());
        return null;
    }

    @Override
    public Page<Note> findByEtudiantAndSessionExamenType(Long inscription, int typeExamen, Pageable pageable) {
        switch (typeExamen) {
            case 0:
                return noteRepository.findByEtudiantIdAndSessionExamenTypeOrderBySessionExamenDesc(
                    inscription,
                    TypeExamen.CONTROLE,
                    pageable
                );
            case 1:
                return noteRepository.findByEtudiantIdAndSessionExamenTypeOrderBySessionExamenDesc(
                    inscription,
                    TypeExamen.SEMESTRIEL,
                    pageable
                );
            default:
                return noteRepository.findByEtudiantIdAndSessionExamenTypeOrderBySessionExamenDesc(
                    inscription,
                    TypeExamen.RATTRAPAGE,
                    pageable
                );
        }
    }
}
