package com.urservices.repository;

import com.urservices.domain.Courriel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Courriel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourrielRepository extends JpaRepository<Courriel, Long> {
    /*@Query(
        "SELECT C FROM Courriel C, Inscription I, Etudiant E, User U WHERE C.etudiant.id = I.id AND I.etudiant.id= E.id AND E.user.id = U.id AND U.id= :id"
    )*/
    Page<Courriel> findByEtudiant_Etudiant_UserIdOrderByIdDesc(Long id, Pageable pageable);
}
