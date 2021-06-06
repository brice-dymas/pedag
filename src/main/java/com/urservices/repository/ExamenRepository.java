package com.urservices.repository;

import com.urservices.domain.Examen;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Examen entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamenRepository extends JpaRepository<Examen, Long> {}
