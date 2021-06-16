package com.urservices.repository;

import com.urservices.domain.SessionExamen;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SessionExamen entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionExamenRepository extends JpaRepository<SessionExamen, Long> {}
