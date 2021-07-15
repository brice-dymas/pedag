package com.urservices.web.rest;

import com.urservices.domain.Deliberation;
import com.urservices.repository.DeliberationRepository;
import com.urservices.service.DeliberationService;
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
 * REST controller for managing {@link com.urservices.domain.Deliberation}.
 */
@RestController
@RequestMapping("/api")
public class DeliberationResource {

    private final Logger log = LoggerFactory.getLogger(DeliberationResource.class);

    private static final String ENTITY_NAME = "deliberation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeliberationService deliberationService;

    private final DeliberationRepository deliberationRepository;

    public DeliberationResource(DeliberationService deliberationService, DeliberationRepository deliberationRepository) {
        this.deliberationService = deliberationService;
        this.deliberationRepository = deliberationRepository;
    }

    /**
     * {@code POST  /deliberations} : Create a new deliberation.
     *
     * @param deliberation the deliberation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deliberation, or with status {@code 400 (Bad Request)} if the deliberation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deliberations")
    public ResponseEntity<Deliberation> createDeliberation(@Valid @RequestBody Deliberation deliberation) throws URISyntaxException {
        log.debug("REST request to save Deliberation : {}", deliberation);
        if (deliberation.getId() != null) {
            throw new BadRequestAlertException("A new deliberation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Deliberation result = deliberationService.save(deliberation);
        return ResponseEntity
            .created(new URI("/api/deliberations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /deliberations/:id} : Updates an existing deliberation.
     *
     * @param id the id of the deliberation to save.
     * @param deliberation the deliberation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliberation,
     * or with status {@code 400 (Bad Request)} if the deliberation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deliberation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deliberations/{id}")
    public ResponseEntity<Deliberation> updateDeliberation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Deliberation deliberation
    ) throws URISyntaxException {
        log.debug("REST request to update Deliberation : {}, {}", id, deliberation);
        if (deliberation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliberation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliberationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Deliberation result = deliberationService.save(deliberation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deliberation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /deliberations/:id} : Partial updates given fields of an existing deliberation, field will ignore if it is null
     *
     * @param id the id of the deliberation to save.
     * @param deliberation the deliberation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliberation,
     * or with status {@code 400 (Bad Request)} if the deliberation is not valid,
     * or with status {@code 404 (Not Found)} if the deliberation is not found,
     * or with status {@code 500 (Internal Server Error)} if the deliberation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/deliberations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Deliberation> partialUpdateDeliberation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Deliberation deliberation
    ) throws URISyntaxException {
        log.debug("REST request to partial update Deliberation partially : {}, {}", id, deliberation);
        if (deliberation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliberation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliberationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Deliberation> result = deliberationService.partialUpdate(deliberation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deliberation.getId().toString())
        );
    }

    /**
     * {@code GET  /deliberations} : get all the deliberations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deliberations in body.
     */
    @GetMapping("/deliberations")
    public ResponseEntity<List<Deliberation>> getAllDeliberations(Pageable pageable) {
        log.debug("REST request to get a page of Deliberations");
        Page<Deliberation> page = deliberationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /deliberations/:id} : get the "id" deliberation.
     *
     * @param id the id of the deliberation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deliberation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deliberations/{id}")
    public ResponseEntity<Deliberation> getDeliberation(@PathVariable Long id) {
        log.debug("REST request to get Deliberation : {}", id);
        Optional<Deliberation> deliberation = deliberationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deliberation);
    }

    /**
     * {@code DELETE  /deliberations/:id} : delete the "id" deliberation.
     *
     * @param id the id of the deliberation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deliberations/{id}")
    public ResponseEntity<Void> deleteDeliberation(@PathVariable Long id) {
        log.debug("REST request to delete Deliberation : {}", id);
        deliberationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
