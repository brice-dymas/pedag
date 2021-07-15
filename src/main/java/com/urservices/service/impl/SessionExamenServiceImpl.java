package com.urservices.service.impl;

import com.urservices.domain.SessionExamen;
import com.urservices.domain.enumeration.TypeExamen;
import com.urservices.repository.SessionExamenRepository;
import com.urservices.service.SessionExamenService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SessionExamen}.
 */
@Service
@Transactional
public class SessionExamenServiceImpl implements SessionExamenService {

    private final Logger log = LoggerFactory.getLogger(SessionExamenServiceImpl.class);

    private final SessionExamenRepository sessionExamenRepository;

    public SessionExamenServiceImpl(SessionExamenRepository sessionExamenRepository) {
        this.sessionExamenRepository = sessionExamenRepository;
    }

    @Override
    public SessionExamen save(SessionExamen sessionExamen) {
        log.debug("Request to save SessionExamen : {}", sessionExamen);

        sessionExamen.setLibelle(sessionExamen.getMois().toString().toUpperCase() + "-" + sessionExamen.getAnnee());
        return sessionExamenRepository.save(sessionExamen);
    }

    @Override
    public Optional<SessionExamen> partialUpdate(SessionExamen sessionExamen) {
        log.debug("Request to partially update SessionExamen : {}", sessionExamen);

        return sessionExamenRepository
            .findById(sessionExamen.getId())
            .map(
                existingSessionExamen -> {
                    if (sessionExamen.getLibelle() != null) {
                        existingSessionExamen.setLibelle(sessionExamen.getLibelle());
                    }
                    if (sessionExamen.getMois() != null) {
                        existingSessionExamen.setMois(sessionExamen.getMois());
                    }
                    if (sessionExamen.getAnnee() != null) {
                        existingSessionExamen.setAnnee(sessionExamen.getAnnee());
                    }

                    return existingSessionExamen;
                }
            )
            .map(sessionExamenRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SessionExamen> findAll(Pageable pageable) {
        log.debug("Request to get all SessionExamen");
        return sessionExamenRepository.findAll(pageable);
    }

    /**
     * Get all actives sessionExamen.
     *
     * @return the list of entities.
     */
    @Override
    public List<SessionExamen> findAllActiveSession() {
        return sessionExamenRepository.findByActifTrueOrderByIdDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SessionExamen> findOne(Long id) {
        log.debug("Request to get SessionExamen : {}", id);
        return sessionExamenRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SessionExamen : {}", id);
        sessionExamenRepository.deleteById(id);
    }

    @Override
    public Page<SessionExamen> findByActifTrueAndTypeIsNot(TypeExamen typeExamen, Pageable pageable) {
        return sessionExamenRepository.findByActifTrueAndTypeIsNot(typeExamen, pageable);
    }
}
