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
        "SELECT DISTINCT D FROM Dispenser D, Inscription I, CoursFiliere C, Etudiant E, User U WHERE D.matiere.id = C.matiere.id AND C.filiere.id = I.filiere.id AND I.etudiant.id= E.id AND E.user.id = U.id AND U.id= :idEtudiant"
    )
    List<Dispenser> findAllStudentMatieres(Long idEtudiant);

    List<Dispenser> findByEnseignantUserIdOrderByIdDesc(Long idTeacher);
    List<Dispenser> findByEnseignantUserIdAndActifTrueOrderByIdDesc(Long idTeacher);
}
