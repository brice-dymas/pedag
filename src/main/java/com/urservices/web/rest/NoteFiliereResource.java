package com.urservices.web.rest;

import com.urservices.service.NotesFiliereService;
import com.urservices.service.dto.NotesFiliereDTO;
import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

/**
 * Projet:  pedag
 * Cree par: Brice dymas
 * Date Creation: 2021, Thursday 17 of June
 *
 * @implSpec REST controller for managing {@link com.urservices.domain.Note}.
 */

@RestController
@RequestMapping("/api/notes-filled")
public class NoteFiliereResource {

    private final Logger log = LoggerFactory.getLogger(NoteResource.class);

    private static final String ENTITY_NAME = "note";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotesFiliereService noteService;

    public NoteFiliereResource(NotesFiliereService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<NotesFiliereDTO> createNote(@Valid @RequestBody NotesFiliereDTO note) throws URISyntaxException {
        log.debug("REST request to save NotesFiliereDTO : {}", note);

        NotesFiliereDTO result = noteService.save(note);
        return ResponseEntity
            .created(new URI("/api/notes/"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, "3615"))
            .body(result);
    }

    @GetMapping("/matiere/{id}")
    public ResponseEntity<NotesFiliereDTO> getFormulaireNoteByDispenser(@PathVariable Long id, Pageable pageable) {
        log.debug("REST request to get a page of Notes for student {}", id);
        final NotesFiliereDTO formulaireNotesForRemplissage = noteService.findByMatiereForRemplissage(id);
        return ResponseEntity.ok(formulaireNotesForRemplissage);
    }

    @GetMapping("/matiere/{id}/session/{is}")
    public ResponseEntity<NotesFiliereDTO> getFormulaireNoteByMatiereSession(
        @PathVariable Long id,
        @PathVariable Long is,
        Pageable pageable
    ) {
        log.debug("REST request to get a page of Notes for matiere {} and session {}", id, is);
        final NotesFiliereDTO formulaireNotesForRemplissage = noteService.findBySessionAndMatiereForRemplissage(is, id);
        return ResponseEntity.ok(formulaireNotesForRemplissage);
    }
}
