package com.urservices.repository;

import com.urservices.domain.Requete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Requete entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequeteRepository extends JpaRepository<Requete, Long> {
    @Query(
        "SELECT R FROM Requete R, Inscription I, Etudiant  E, User U WHERE R.etudiant.id = I.id AND I.etudiant.id = E.id AND E.user.id = U.id AND U.id= :id"
    )
    //    @Query("SELECT R FROM Requete R, Inscription I WHERE R.etudiant.id = I.id AND I.id = :id")
    Page<Requete> rechercherParEtudiant(Long id, Pageable pageable);

    Page<Requete> findByEtudiant_Etudiant_UserId(Long id, Pageable pageable);
}
