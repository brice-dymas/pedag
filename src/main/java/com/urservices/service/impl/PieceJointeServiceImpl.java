package com.urservices.service.impl;

import com.urservices.domain.Matiere;
import com.urservices.domain.PieceJointe;
import com.urservices.repository.MatiereRepository;
import com.urservices.repository.PieceJointeRepository;
import com.urservices.service.PieceJointeService;
import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PieceJointe}.
 */
@Service
@Transactional
public class PieceJointeServiceImpl implements PieceJointeService {

    private final Logger log = LoggerFactory.getLogger(PieceJointeServiceImpl.class);

    private final PieceJointeRepository pieceJointeRepository;

    private final MatiereRepository matiereRepository;

    public PieceJointeServiceImpl(PieceJointeRepository pieceJointeRepository, MatiereRepository matiereRepository) {
        this.pieceJointeRepository = pieceJointeRepository;
        this.matiereRepository = matiereRepository;
    }

    /**
     * Save a pieceJointe of a specific Matiere.
     *
     * @param id
     * @param pieceJointe the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PieceJointe save(Long id, PieceJointe pieceJointe) {
        final Matiere matiere = matiereRepository.findById(id).orElse(null);
        pieceJointe.setMatiere(matiere);
        pieceJointe.setDateCreation(LocalDate.now());
        return pieceJointeRepository.save(pieceJointe);
    }

    @Override
    public PieceJointe save(PieceJointe pieceJointe) {
        log.debug("Request to save PieceJointe : {}", pieceJointe);
        pieceJointe.setDateCreation(LocalDate.now());
        return pieceJointeRepository.save(pieceJointe);
    }

    @Override
    public Optional<PieceJointe> partialUpdate(PieceJointe pieceJointe) {
        log.debug("Request to partially update PieceJointe : {}", pieceJointe);

        return pieceJointeRepository
            .findById(pieceJointe.getId())
            .map(
                existingPieceJointe -> {
                    if (pieceJointe.getLibelle() != null) {
                        existingPieceJointe.setLibelle(pieceJointe.getLibelle());
                    }
                    if (pieceJointe.getContenu() != null) {
                        existingPieceJointe.setContenu(pieceJointe.getContenu());
                    }
                    if (pieceJointe.getContenuContentType() != null) {
                        existingPieceJointe.setContenuContentType(pieceJointe.getContenuContentType());
                    }
                    if (pieceJointe.getDateCreation() != null) {
                        existingPieceJointe.setDateCreation(pieceJointe.getDateCreation());
                    }

                    return existingPieceJointe;
                }
            )
            .map(pieceJointeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PieceJointe> findAll(Pageable pageable) {
        log.debug("Request to get all PieceJointes");
        return pieceJointeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PieceJointe> findOne(Long id) {
        log.debug("Request to get PieceJointe : {}", id);
        return pieceJointeRepository.findById(id);
    }

    /**
     * Get the "id" of a matiere for pieceJointe.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public PieceJointe findOneForSet(Long id) {
        PieceJointe p = new PieceJointe();
        p.setMatiere(matiereRepository.findById(id).orElse(null));
        return p;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PieceJointe : {}", id);
        pieceJointeRepository.deleteById(id);
    }

    /**
     * Get all the pieceJointes of a given Matiere.
     *
     * @param id       The identifier of the desired matiere
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<PieceJointe> findByMatiereId(Long id, Pageable pageable) {
        log.debug("Request to get all PieceJointes of Matiere {}", id);
        return pieceJointeRepository.findByMatiereId(id, pageable);
    }
}
