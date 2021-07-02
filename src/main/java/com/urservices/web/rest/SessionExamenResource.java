package com.urservices.web.rest;

import com.urservices.domain.SessionExamen;
import com.urservices.domain.enumeration.TypeExamen;
import com.urservices.repository.SessionExamenRepository;
import com.urservices.service.SessionExamenService;
import com.urservices.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.urservices.domain.SessionExamen}.
 */
@RestController
@RequestMapping("/api")
public class SessionExamenResource {

    private final Logger log = LoggerFactory.getLogger(SessionExamenResource.class);

    private static final String ENTITY_NAME = "sessionExamen";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SessionExamenService sessionExamenService;

    private final SessionExamenRepository sessionExamenRepository;

    public SessionExamenResource(SessionExamenService sessionExamenService, SessionExamenRepository sessionExamenRepository) {
        this.sessionExamenService = sessionExamenService;
        this.sessionExamenRepository = sessionExamenRepository;
    }

    /**
     * {@code POST  /session-examen} : Create a new sessionExamen.
     *
     * @param sessionExamen the sessionExamen to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sessionExamen, or with status {@code 400 (Bad Request)} if the sessionExamen has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/session-examen")
    public ResponseEntity<SessionExamen> createSessionExamen(@Valid @RequestBody SessionExamen sessionExamen) throws URISyntaxException {
        log.debug("REST request to save SessionExamen : {}", sessionExamen);
        if (sessionExamen.getId() != null) {
            throw new BadRequestAlertException("A new sessionExamen cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SessionExamen result = sessionExamenService.save(sessionExamen);
        return ResponseEntity
            .created(new URI("/api/session-examen/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /session-examen/:id} : Updates an existing sessionExamen.
     *
     * @param id the id of the sessionExamen to save.
     * @param sessionExamen the sessionExamen to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionExamen,
     * or with status {@code 400 (Bad Request)} if the sessionExamen is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sessionExamen couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/session-examen/{id}")
    public ResponseEntity<SessionExamen> updateSessionExamen(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SessionExamen sessionExamen
    ) throws URISyntaxException {
        log.debug("REST request to update SessionExamen : {}, {}", id, sessionExamen);
        if (sessionExamen.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionExamen.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionExamenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SessionExamen result = sessionExamenService.save(sessionExamen);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionExamen.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /session-examen/:id} : Partial updates given fields of an existing sessionExamen, field will ignore if it is null
     *
     * @param id the id of the sessionExamen to save.
     * @param sessionExamen the sessionExamen to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionExamen,
     * or with status {@code 400 (Bad Request)} if the sessionExamen is not valid,
     * or with status {@code 404 (Not Found)} if the sessionExamen is not found,
     * or with status {@code 500 (Internal Server Error)} if the sessionExamen couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/session-examen/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SessionExamen> partialUpdateSessionExamen(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SessionExamen sessionExamen
    ) throws URISyntaxException {
        log.debug("REST request to partial update SessionExamen partially : {}, {}", id, sessionExamen);
        if (sessionExamen.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionExamen.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionExamenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SessionExamen> result = sessionExamenService.partialUpdate(sessionExamen);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionExamen.getId().toString())
        );
    }

    /**
     * {@code GET  /session-examen} : get all the sessionExamen.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sessionExamen in body.
     */
    @GetMapping("/session-examen")
    public ResponseEntity<List<SessionExamen>> getAllSessionExamen(Pageable pageable) {
        log.debug("REST request to get a page of SessionExamen");
        Page<SessionExamen> page = sessionExamenService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /session-examen/deliberation} : get all the sessionExamen.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sessionExamen in body.
     */
    @GetMapping("/session-examen/deliberation")
    public ResponseEntity<List<SessionExamen>> getAllSessionExamenDeliberation(Pageable pageable) {
        log.debug("REST request to get a page of SessionExamen for Deliberation");
        Page<SessionExamen> page = sessionExamenService.findByActifTrueAndTypeIsNot(TypeExamen.CONTROLE, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /session-examen/:id} : get the "id" sessionExamen.
     *
     * @param id the id of the sessionExamen to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sessionExamen, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/session-examen/{id}")
    public ResponseEntity<SessionExamen> getSessionExamen(@PathVariable Long id) {
        log.debug("REST request to get SessionExamen : {}", id);
        Optional<SessionExamen> sessionExamen = sessionExamenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sessionExamen);
    }

    /**
     * {@code GET  /session-examen/:id/actif} : get the "id" sessionExamen.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sessionExamen, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/session-examen/actif")
    public ResponseEntity<List<SessionExamen>> getSessionExamenActif() {
        log.debug("REST request to get actives SessionExamen ");
        List<SessionExamen> sessionExamens = sessionExamenService.findAllActiveSession();
        return ResponseEntity.ok(sessionExamens);
    }

    /**
     * {@code DELETE  /session-examen/:id} : delete the "id" sessionExamen.
     *
     * @param id the id of the sessionExamen to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/session-examen/{id}")
    public ResponseEntity<Void> deleteSessionExamen(@PathVariable Long id) {
        log.debug("REST request to delete SessionExamen : {}", id);
        sessionExamenService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
