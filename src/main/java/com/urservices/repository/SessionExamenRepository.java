package com.urservices.repository;

import com.urservices.domain.SessionExamen;
import com.urservices.domain.enumeration.TypeExamen;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<SessionExamen> findByActifTrueAndTypeIsNot(TypeExamen typeExamen, Pageable pageable);
}
