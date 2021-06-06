package com.urservices.repository;

import com.urservices.domain.Administrateur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Administrateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdministrateurRepository extends JpaRepository<Administrateur, Long> {}
