package com.urservices.service.impl;

import com.urservices.domain.Deliberation;
import com.urservices.repository.DeliberationRepository;
import com.urservices.service.DeliberationService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Deliberation}.
 */
@Service
@Transactional
public class DeliberationServiceImpl implements DeliberationService {

    private final Logger log = LoggerFactory.getLogger(DeliberationServiceImpl.class);

    private final DeliberationRepository deliberationRepository;

    public DeliberationServiceImpl(DeliberationRepository deliberationRepository) {
        this.deliberationRepository = deliberationRepository;
    }

    @Override
    public Deliberation save(Deliberation deliberation) {
        log.debug("Request to save Deliberation : {}", deliberation);
        return deliberationRepository.save(deliberation);
    }

    @Override
    public Optional<Deliberation> partialUpdate(Deliberation deliberation) {
        log.debug("Request to partially update Deliberation : {}", deliberation);

        return deliberationRepository
            .findById(deliberation.getId())
            .map(
                existingDeliberation -> {
                    if (deliberation.getCritereSelection() != null) {
                        existingDeliberation.setCritereSelection(deliberation.getCritereSelection());
                    }
                    if (deliberation.getValeurSelectionDebut() != null) {
                        existingDeliberation.setValeurSelectionDebut(deliberation.getValeurSelectionDebut());
                    }
                    if (deliberation.getValeurSelectionFin() != null) {
                        existingDeliberation.setValeurSelectionFin(deliberation.getValeurSelectionFin());
                    }
                    if (deliberation.getCritereAppliquer() != null) {
                        existingDeliberation.setCritereAppliquer(deliberation.getCritereAppliquer());
                    }
                    if (deliberation.getValeurAppliquer() != null) {
                        existingDeliberation.setValeurAppliquer(deliberation.getValeurAppliquer());
                    }
                    if (deliberation.getDateDeliberation() != null) {
                        existingDeliberation.setDateDeliberation(deliberation.getDateDeliberation());
                    }

                    return existingDeliberation;
                }
            )
            .map(deliberationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Deliberation> findAll(Pageable pageable) {
        log.debug("Request to get all Deliberations");
        return deliberationRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Deliberation> findOne(Long id) {
        log.debug("Request to get Deliberation : {}", id);
        return deliberationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Deliberation : {}", id);
        deliberationRepository.deleteById(id);
    }
}
