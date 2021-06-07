package com.urservices.repository;

import com.urservices.domain.CoursFiliere;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CoursFiliere entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoursFiliereRepository extends JpaRepository<CoursFiliere, Long> {}
