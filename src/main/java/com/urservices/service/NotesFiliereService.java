package com.urservices.service;

import com.urservices.service.dto.NotesFiliereDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Projet:  pedag
 * Cree par: Brice dymas
 * Date Creation: 2021, Thursday 17 of June
 */
public interface NotesFiliereService {
    /**
     * Save all note.
     *
     * @param note the entity to save.
     * @return the persisted entity.
     */
    NotesFiliereDTO save(NotesFiliereDTO note);

    /**
     * Get the "id" note.
     *
     * @param idMatiere the id of the entity.
     * @return the entity.
     */
    NotesFiliereDTO findByMatiereForRemplissage(Long idMatiere);

    /**
     * Get the "id" note.
     *
     * @param idMatiere the id of the entity.
     * @return the entity.
     */
    NotesFiliereDTO findBySessionAndMatiere(Long idSession, Long idMatiere);
    /**
     * Get the "id" note.
     *
     * @param idSession the id of the exam session.
     * @param idFiliere The filiere we want to see all notes of
     * @return the entity.
     */
    NotesFiliereDTO findBySessionAndFiliere(Long idSession, Long idFiliere);

    /**
     * Delete the "id" note.
     *
     * @param note the notes to delete
     */
    void delete(NotesFiliereDTO note);

    /**
     * Get all notes of a Student.
     *
     * @param idEtudiant the id of the Student.
     * @param idSessionExamen id of the exam session
     * @return the list of notes.
     */
    Page<NotesFiliereDTO> findByEtudiantAndSessionExamen(Long idEtudiant, Long idSessionExamen, Pageable pageable);

    /**
     * Get all notes of a Teacher.
     *
     * @param idEnseignant the id of the Teacher Account.
     * @param idMatiere the id of the Matiere
     * @return the list of notes.
     */
    Page<NotesFiliereDTO> findByEnseignantAndMatiere(Long idEnseignant, Long idMatiere, Pageable pageable);
}
