package com.urservices.service.impl;

import com.urservices.domain.*;
import com.urservices.repository.*;
import com.urservices.service.NotesFiliereService;
import com.urservices.service.dto.NotesFiliereDTO;
import java.util.ArrayList;
import java.util.List;
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
    private final FiliereRepository filiereRepository;
    private final NoteRepository noteRepository;
    private final DispenserRepository dispenserRepository;
    private final EnseignantRepository enseignantRepository;
    private final InscriptionRepository inscriptionRepository;
    private final SessionExamenRepository sessionExamenRepository;
    private final AnneeAcademiqueRepository anneeAcademiqueRepository;

    public NotesFiliereServiceImpl(
        MatiereRepository matiereRepository,
        FiliereRepository filiereRepository,
        NoteRepository noteRepository,
        DispenserRepository dispenserRepository,
        EnseignantRepository enseignantRepository,
        InscriptionRepository inscriptionRepository,
        SessionExamenRepository sessionExamenRepository,
        AnneeAcademiqueRepository anneeAcademiqueRepository
    ) {
        this.matiereRepository = matiereRepository;
        this.filiereRepository = filiereRepository;
        this.noteRepository = noteRepository;
        this.dispenserRepository = dispenserRepository;
        this.enseignantRepository = enseignantRepository;
        this.inscriptionRepository = inscriptionRepository;
        this.sessionExamenRepository = sessionExamenRepository;
        this.anneeAcademiqueRepository = anneeAcademiqueRepository;
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
            note.setEnseignant(dispenser.getEnseignant());
            note.setMatiere(dispenser.getMatiere());
            note.setObservation(getObservation(note.getMoyenne()));
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
        for (Inscription ins : inscriptions) {
            Note n = new Note();
            n.setMatiere(matiere);
            n.setEtudiant(ins);
            notes.add(n);
        }
        result.setNotes(notes);
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

    private final String getObservation(Float moyenne) {
        String obs = "EL";
        if (moyenne <= 9) {
            obs = "NV";
        } else {
            obs = "VA";
        }
        return obs;
    }
}
