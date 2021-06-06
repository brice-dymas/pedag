package com.urservices.service.impl;

import com.urservices.domain.Dispenser;
import com.urservices.repository.DispenserRepository;
import com.urservices.service.DispenserService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Dispenser}.
 */
@Service
@Transactional
public class DispenserServiceImpl implements DispenserService {

    private final Logger log = LoggerFactory.getLogger(DispenserServiceImpl.class);

    private final DispenserRepository dispenserRepository;

    public DispenserServiceImpl(DispenserRepository dispenserRepository) {
        this.dispenserRepository = dispenserRepository;
    }

    @Override
    public Dispenser save(Dispenser dispenser) {
        log.debug("Request to save Dispenser : {}", dispenser);
        return dispenserRepository.save(dispenser);
    }

    @Override
    public Optional<Dispenser> partialUpdate(Dispenser dispenser) {
        log.debug("Request to partially update Dispenser : {}", dispenser);

        return dispenserRepository
            .findById(dispenser.getId())
            .map(
                existingDispenser -> {
                    if (dispenser.getSemestre() != null) {
                        existingDispenser.setSemestre(dispenser.getSemestre());
                    }

                    return existingDispenser;
                }
            )
            .map(dispenserRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Dispenser> findAll(Pageable pageable) {
        log.debug("Request to get all Dispensers");
        return dispenserRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Dispenser> findOne(Long id) {
        log.debug("Request to get Dispenser : {}", id);
        return dispenserRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dispenser : {}", id);
        dispenserRepository.deleteById(id);
    }
}
