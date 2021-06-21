package com.urservices.repository;

import com.urservices.domain.SessionExamen;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SessionExamen entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionExamenRepository extends JpaRepository<SessionExamen, Long> {
    List<SessionExamen> findByActifTrueOrderByIdDesc();
}
