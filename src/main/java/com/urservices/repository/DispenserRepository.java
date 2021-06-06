package com.urservices.repository;

import com.urservices.domain.Dispenser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Dispenser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DispenserRepository extends JpaRepository<Dispenser, Long> {}
