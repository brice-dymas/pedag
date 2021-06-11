package com.urservices.repository;

import com.urservices.domain.Note;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Note entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    @Query("SELECT N FROM Note N WHERE N.etudiant.etudiant.id= :id ORDER BY N.id DESC")
    Page<Note> findByEtudiantId(Long id, Pageable pageable);

    Page<Note> findByEtudiant_EtudiantUserIdOrderByIdDesc(Long id, Pageable pageable);

    Page<Note> findByEnseignantUserIdOrderByIdDesc(Long id, Pageable pageable);
}
