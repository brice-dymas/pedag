package com.urservices.repository;

import com.urservices.domain.Courriel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Courriel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourrielRepository extends JpaRepository<Courriel, Long> {}
