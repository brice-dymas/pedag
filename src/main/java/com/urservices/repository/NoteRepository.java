package com.urservices.repository;

import com.urservices.domain.Filiere;
import com.urservices.domain.Note;
import com.urservices.domain.SessionExamen;
import com.urservices.domain.enumeration.TypeExamen;
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
    List<Note> findBySessionExamenIdAndMatiereIdOrderByIdDesc(Long idSession, Long idMatiere);

    Page<Note> findBySessionExamenIdAndEtudiantFiliereIdOrderByIdDesc(Long idSession, Long idFiliere, Pageable pageable);

    @Query("SELECT N FROM Note N WHERE N.etudiant.etudiant.id= :id ORDER BY N.id DESC")
    Page<Note> findByEtudiantId(Long id, Pageable pageable);

    Page<Note> findByEtudiant_EtudiantUserIdOrderByIdDesc(Long id, Pageable pageable);

    Page<Note> findByEnseignantUserIdOrderByIdDesc(Long id, Pageable pageable);

    List<Note> findBySessionExamenIdAndMoyenneEqualsAndSessionExamenTypeIsNot(Long sessionExamen, Float moyenne, TypeExamen typeExamen);
    List<Note> findBySessionExamenIdAndMoyenneEqualsAndSessionExamenTypeIsNotAndEtudiantFiliere(
        Long sessionExamen,
        Float moyenne,
        TypeExamen typeExamen,
        Long filiere
    );

    List<Note> findBySessionExamenIdAndMoyenneLessThanAndSessionExamenTypeIsNot(Long sessionExamen, Float moyenne, TypeExamen typeExamen);
    List<Note> findBySessionExamenIdAndMoyenneLessThanAndSessionExamenTypeIsNotAndEtudiantFiliere(
        Long sessionExamen,
        Float moyenne,
        TypeExamen typeExamen,
        Long filiere
    );

    List<Note> findBySessionExamenIdAndMoyenneBetweenAndSessionExamenTypeIsNot(
        Long sessionExamen,
        Float moyenne1,
        Float moyenne2,
        TypeExamen typeExamen
    );
    List<Note> findBySessionExamenIdAndMoyenneBetweenAndSessionExamenTypeIsNotAndEtudiantFiliere(
        Long sessionExamen,
        Float moyenne1,
        Float moyenne2,
        TypeExamen typeExamen,
        Long filiere
    );
}
