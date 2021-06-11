package com.urservices.service;

import com.urservices.domain.Note;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Note}.
 */
public interface NoteService {
    /**
     * Save a note.
     *
     * @param note the entity to save.
     * @return the persisted entity.
     */
    Note save(Note note);

    /**
     * Partially updates a note.
     *
     * @param note the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Note> partialUpdate(Note note);

    /**
     * Get all the notes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Note> findAll(Pageable pageable);

    /**
     * Get the "id" note.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Note> findOne(Long id);

    /**
     * Delete the "id" note.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all notes of a Student.
     *
     * @param id the id of the Student.
     * @return the list of notes.
     */
    Page<Note> findByEtudiantId(Long id, Pageable pageable);

    /**
     * Get all notes of a Teacher.
     *
     * @param id the id of the Teacher User Account.
     * @return the list of notes.
     */
    Page<Note> findByEnseignantId(Long id, Pageable pageable);
}
