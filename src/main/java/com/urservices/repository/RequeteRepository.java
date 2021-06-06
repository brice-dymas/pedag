package com.urservices.repository;

import com.urservices.domain.Requete;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Requete entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequeteRepository extends JpaRepository<Requete, Long> {}
