package com.urservices.repository;

import com.urservices.domain.Inscription;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Inscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    @Query("SELECT I FROM Inscription I, Etudiant E, User U WHERE I.etudiant.id = E.id AND E.user.id = U.id AND U.id= :id")
    Inscription findEtudiantByUserId(Long id);

    @Query(
        "SELECT I FROM Inscription I, Matiere M, Module MD, AnneeAcademique A WHERE  I.anneeAcademique.id = A.id AND A.current = true AND I.filiere.id = MD.filiere.id AND M.module.id = MD.id AND M.id= :id"
    )
    List<Inscription> findByMatiere(Long id);

    Inscription findByEtudiantUserId(Long id);
}
