package com.urservices.repository;

import com.urservices.domain.PieceJointe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PieceJointe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PieceJointeRepository extends JpaRepository<PieceJointe, Long> {
    //    @Query("SELECT new PieceJointe(P.id, P.libelle, P.contenu, P.contenuContentType, P.dateCreation, P.matiere ) FROM PieceJointe P, Matiere M WHERE P.matiere.id = M.id AND M.id= :id")
    @Query("SELECT P FROM PieceJointe P, Matiere M WHERE P.matiere.id = M.id AND M.id= :id")
    Page<PieceJointe> findByMatiereId(Long id, Pageable pageable);
}
