package com.urservices.service.impl;

import com.urservices.domain.*;
import com.urservices.repository.*;
import com.urservices.service.NotesFiliereService;
import com.urservices.service.dto.NotesFiliereDTO;
import com.urservices.utils.RequeteHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Projet:  pedag
 * Cree par: Brice dymas
 * Date Creation: 2021, Thursday 17 of June
 */
@Service
public class NotesFiliereServiceImpl implements NotesFiliereService {

    private final MatiereRepository matiereRepository;
    private final NoteRepository noteRepository;
    private final DispenserRepository dispenserRepository;
    private final InscriptionRepository inscriptionRepository;
    private final SessionExamenRepository sessionExamenRepository;

    public NotesFiliereServiceImpl(
        MatiereRepository matiereRepository,
        NoteRepository noteRepository,
        DispenserRepository dispenserRepository,
        InscriptionRepository inscriptionRepository,
        SessionExamenRepository sessionExamenRepository
    ) {
        this.matiereRepository = matiereRepository;
        this.noteRepository = noteRepository;
        this.dispenserRepository = dispenserRepository;
        this.inscriptionRepository = inscriptionRepository;
        this.sessionExamenRepository = sessionExamenRepository;
    }

    /**
     * Save all note.
     *
     * @param notes the entity to save.
     * @return the persisted entity.
     */
    @Override
    public NotesFiliereDTO save(NotesFiliereDTO notes) {
        NotesFiliereDTO result = new NotesFiliereDTO();
        List<Note> noteList = new ArrayList<>();
        final Dispenser dispenser = dispenserRepository.findById(notes.getDispenser().getId()).orElse(null);
        final SessionExamen sessionExamen = sessionExamenRepository.findById(notes.getSessionExamen().getId()).orElse(null);

        result.setSessionExamen(sessionExamen);
        result.setDispenser(dispenser);
        result.setEnseignant(result.getDispenser().getEnseignant());
        result.setMatiere(result.getDispenser().getMatiere());

        for (Note note : notes.getNotes()) {
            note.setCreditMatiere(result.getMatiere().getCredit());
            note.setCreditObtenu(note.getMoyenne() < 10 ? 0 : result.getMatiere().getCredit());
            assert dispenser != null;
            note.setEnseignant(dispenser.getEnseignant());
            note.setMatiere(dispenser.getMatiere());
            note.setObservation(RequeteHelper.getObservationNote(note.getMoyenne()));
            note.setEtudiant(inscriptionRepository.findById(note.getEtudiant().getId()).orElse(null));
            note.setMoyenne(note.getMoyenne());
            note.setSessionExamen(sessionExamen);
            final Note noteSaved = noteRepository.save(note);
            noteList.add(noteSaved);
        }
        result.setNotes(noteList);
        return result;
    }

    /**
     * Get the "id" note.
     *
     * @param idSession
     * @param idMatiere the id of the entity.
     * @return the entity.
     */
    @Override
    public NotesFiliereDTO findBySessionAndMatiere(Long idSession, Long idMatiere) {
        NotesFiliereDTO result = new NotesFiliereDTO();
        result.setSessionExamen(sessionExamenRepository.getOne(idSession));
        result.setMatiere(matiereRepository.getOne(idMatiere));
        result.setNotes(noteRepository.findBySessionExamenIdAndMatiereIdOrderByIdDesc(idSession, idMatiere));
        return result;
    }

    @Override
    public NotesFiliereDTO findBySessionAndMatiereForRemplissage(Long idSession, Long idMatiere) {
        NotesFiliereDTO result = new NotesFiliereDTO();
        List<Note> noteList = new ArrayList<>();
        Dispenser dispenser = dispenserRepository.findById(idMatiere).orElse(null);
        final SessionExamen sessionExamen = sessionExamenRepository.findById(idSession).orElse(null);
        result.setSessionExamen(sessionExamen);
        assert dispenser != null;
        result.setMatiere(dispenser.getMatiere());
        result.setDispenser(dispenser);
        result.setEnseignant(dispenser.getEnseignant());
        result.setAnneeAcademique(dispenser.getAnneeAcademique());
        final List<Note> savedNotes = noteRepository.findBySessionExamenIdAndMatiereIdOrderByIdDesc(idSession, idMatiere);
        List<Inscription> inscriptions = inscriptionRepository.findByMatiere(idMatiere);
        //        inscriptions.stream().forEach(ins -> noteList.add(getNote(ins, savedNotes)));
        inscriptions.forEach(ins -> noteList.add(getNote(ins, savedNotes)));
        //        result.setNotes(noteRepository.findBySessionExamenIdAndMatiereIdOrderByIdDesc(idSession, dispenser.getMatiere().getId()));
        result.setNotes(noteList);
        return result;
    }

    private static Note getNote(Inscription ins, List<Note> savedNotes) {
        Predicate<Note> noteExists = note -> note.getEtudiant().equals(ins);
        final List<Note> collected = savedNotes.stream().filter(noteExists).collect(Collectors.toList());
        return (collected.size() < 1) ? getDefaultNote(ins) : collected.get(0);
    }

    private static List<Note> getDefaultClassList(List<Inscription> inscriptions) {
        return inscriptions.stream().map(NotesFiliereServiceImpl::getDefaultNote).collect(Collectors.toList());
    }

    private static Note getDefaultNote(Inscription ins) {
        Note n = new Note();
        n.setEtudiant(ins);
        return n;
    }

    @Override
    public NotesFiliereDTO findByMatiereForRemplissage(Long idMatiere) {
        Dispenser dispenser = dispenserRepository.findById(idMatiere).orElse(null);
        NotesFiliereDTO result = new NotesFiliereDTO();
        assert dispenser != null;
        Matiere matiere = matiereRepository.findById(dispenser.getMatiere().getId()).orElse(null);
        result.setMatiere(matiere);
        result.setDispenser(dispenser);
        result.setEnseignant(dispenser.getEnseignant());
        List<Note> notes = new ArrayList<>();
        List<Inscription> inscriptions = inscriptionRepository.findByMatiere(idMatiere);
        result.setNotes(getDefaultClassList(inscriptions));
        /*for (Inscription ins : inscriptions) {
            Note n = new Note();
            n.setMatiere(matiere);
            n.setEtudiant(ins);
            notes.add(n);
        }
        result.setNotes(notes);*/
        return result;
    }

    /**
     * Get the "id" note.
     *
     * @param idSession the id of the exam session.
     * @param idFiliere The filiere we want to see all notes of
     * @return the entity.
     */
    @Override
    public NotesFiliereDTO findBySessionAndFiliere(Long idSession, Long idFiliere) {
        return null;
    }

    /**
     * Delete the "id" note.
     *
     * @param note the notes to delete
     */
    @Override
    public void delete(NotesFiliereDTO note) {
        noteRepository.deleteAll(note.getNotes());
    }

    /**
     * Get all notes of a Student.
     *
     * @param idEtudiant      the id of the Student.
     * @param idSessionExamen id of the exam session
     * @param pageable
     * @return the list of notes.
     */
    @Override
    public Page<NotesFiliereDTO> findByEtudiantAndSessionExamen(Long idEtudiant, Long idSessionExamen, Pageable pageable) {
        return null;
    }

    /**
     * Get all notes of a Teacher.
     *
     * @param idEnseignant the id of the Teacher Account.
     * @param idMatiere    the id of the Matiere
     * @param pageable
     * @return the list of notes.
     */
    @Override
    public Page<NotesFiliereDTO> findByEnseignantAndMatiere(Long idEnseignant, Long idMatiere, Pageable pageable) {
        return null;
    }
}
