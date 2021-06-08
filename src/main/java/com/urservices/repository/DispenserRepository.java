package com.urservices.repository;

import com.urservices.domain.Dispenser;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Dispenser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DispenserRepository extends JpaRepository<Dispenser, Long> {
    @Query(
        "SELECT D FROM Dispenser D, Inscription I, CoursFiliere C WHERE C.filiere.id = I.filiere.id AND D.matiere.id = C.matiere.id AND I.etudiant.id= :idEtudiant"
    )
    List<Dispenser> findAllStudentMatieres(Long idEtudiant);

    List<Dispenser> findByEnseignantId(Long idTeacher);
}
