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

    Inscription findByEtudiantUserId(Long id);
}
