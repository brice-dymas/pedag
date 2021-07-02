package com.urservices.service.impl;

import com.urservices.domain.Deliberation;
import com.urservices.domain.Note;
import com.urservices.domain.enumeration.TypeExamen;
import com.urservices.repository.DeliberationRepository;
import com.urservices.repository.NoteRepository;
import com.urservices.service.DeliberationService;
import com.urservices.utils.RequeteHelper;
import java.util.ArrayList;
import java.util.List;
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
    private final NoteRepository noteRepository;

    public DeliberationServiceImpl(DeliberationRepository deliberationRepository, NoteRepository noteRepository) {
        this.deliberationRepository = deliberationRepository;
        this.noteRepository = noteRepository;
    }

    @Override
    public Deliberation save(Deliberation deliberation) {
        log.debug("Request to save Deliberation : {}", deliberation);
        final Deliberation saved = deliberationRepository.save(deliberation);
        List<Note> noteList = getNotesToDeliberer(deliberation);
        List<Note> notesUpdated = delibererNotes(noteList, saved);
        System.out.println("Deliberation DONE . . .");
        System.out.println(notesUpdated);
        return saved;
    }

    private List<Note> getNotesToDeliberer(Deliberation deliberation) {
        TypeExamen controle = TypeExamen.CONTROLE;
        switch (deliberation.getCritereSelection()) {
            case MOYENNE_EGALE_A:
                return (deliberation.getFiliere() == null)
                    ? noteRepository.findBySessionExamenIdAndMoyenneEqualsAndSessionExamenTypeIsNot(
                        deliberation.getSessionExamen().getId(),
                        deliberation.getValeurSelectionDebut(),
                        controle
                    )
                    : noteRepository.findBySessionExamenIdAndMoyenneEqualsAndSessionExamenTypeIsNotAndEtudiantFiliere(
                        deliberation.getSessionExamen().getId(),
                        deliberation.getValeurSelectionDebut(),
                        controle,
                        deliberation.getFiliere().getId()
                    );
            case MOYENNE_INFERIEURE_A:
                return (deliberation.getFiliere() == null)
                    ? noteRepository.findBySessionExamenIdAndMoyenneLessThanAndSessionExamenTypeIsNot(
                        deliberation.getSessionExamen().getId(),
                        deliberation.getValeurSelectionDebut(),
                        controle
                    )
                    : noteRepository.findBySessionExamenIdAndMoyenneLessThanAndSessionExamenTypeIsNotAndEtudiantFiliere(
                        deliberation.getSessionExamen().getId(),
                        deliberation.getValeurSelectionDebut(),
                        controle,
                        deliberation.getFiliere().getId()
                    );
            default:
                return (deliberation.getFiliere() == null)
                    ? noteRepository.findBySessionExamenIdAndMoyenneBetweenAndSessionExamenTypeIsNot(
                        deliberation.getSessionExamen().getId(),
                        deliberation.getValeurSelectionDebut(),
                        deliberation.getValeurSelectionFin(),
                        controle
                    )
                    : noteRepository.findBySessionExamenIdAndMoyenneBetweenAndSessionExamenTypeIsNotAndEtudiantFiliere(
                        deliberation.getSessionExamen().getId(),
                        deliberation.getValeurSelectionDebut(),
                        deliberation.getValeurSelectionFin(),
                        controle,
                        deliberation.getFiliere().getId()
                    );
        }
    }

    private List<Note> delibererNotes(List<Note> noteList, Deliberation deliberation) {
        List<Note> notesToUpdate = new ArrayList<>();
        noteList.forEach(
            note -> {
                switch (deliberation.getCritereAppliquer()) {
                    case RETRANCHER:
                        note.setMoyenne(note.getMoyenne() - deliberation.getValeurAppliquer());
                        break;
                    case DEFINIR_VALEUR_A:
                        note.setMoyenne(deliberation.getValeurAppliquer());
                        break;
                    default:
                        note.setMoyenne(note.getMoyenne() + deliberation.getValeurAppliquer());
                        break;
                }
                note.setObservation(RequeteHelper.getObservationNote(note.getMoyenne()));
                note.setCreditObtenu(note.getMoyenne() < 10 ? 0 : note.getMatiere().getCredit());
                notesToUpdate.add(note);
            }
        );
        return noteRepository.saveAll(notesToUpdate);
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
