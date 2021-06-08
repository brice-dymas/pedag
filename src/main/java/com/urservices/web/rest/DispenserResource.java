package com.urservices.web.rest;

import com.urservices.domain.Dispenser;
import com.urservices.repository.DispenserRepository;
import com.urservices.service.DispenserService;
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
 * REST controller for managing {@link com.urservices.domain.Dispenser}.
 */
@RestController
@RequestMapping("/api")
public class DispenserResource {

    private final Logger log = LoggerFactory.getLogger(DispenserResource.class);

    private static final String ENTITY_NAME = "dispenser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DispenserService dispenserService;

    private final DispenserRepository dispenserRepository;

    public DispenserResource(DispenserService dispenserService, DispenserRepository dispenserRepository) {
        this.dispenserService = dispenserService;
        this.dispenserRepository = dispenserRepository;
    }

    /**
     * {@code POST  /dispensers} : Create a new dispenser.
     *
     * @param dispenser the dispenser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dispenser, or with status {@code 400 (Bad Request)} if the dispenser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dispensers")
    public ResponseEntity<Dispenser> createDispenser(@Valid @RequestBody Dispenser dispenser) throws URISyntaxException {
        log.debug("REST request to save Dispenser : {}", dispenser);
        if (dispenser.getId() != null) {
            throw new BadRequestAlertException("A new dispenser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dispenser result = dispenserService.save(dispenser);
        return ResponseEntity
            .created(new URI("/api/dispensers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dispensers/:id} : Updates an existing dispenser.
     *
     * @param id the id of the dispenser to save.
     * @param dispenser the dispenser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dispenser,
     * or with status {@code 400 (Bad Request)} if the dispenser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dispenser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dispensers/{id}")
    public ResponseEntity<Dispenser> updateDispenser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Dispenser dispenser
    ) throws URISyntaxException {
        log.debug("REST request to update Dispenser : {}, {}", id, dispenser);
        if (dispenser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dispenser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dispenserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Dispenser result = dispenserService.save(dispenser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dispenser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dispensers/:id} : Partial updates given fields of an existing dispenser, field will ignore if it is null
     *
     * @param id the id of the dispenser to save.
     * @param dispenser the dispenser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dispenser,
     * or with status {@code 400 (Bad Request)} if the dispenser is not valid,
     * or with status {@code 404 (Not Found)} if the dispenser is not found,
     * or with status {@code 500 (Internal Server Error)} if the dispenser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dispensers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Dispenser> partialUpdateDispenser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Dispenser dispenser
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dispenser partially : {}, {}", id, dispenser);
        if (dispenser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dispenser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dispenserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dispenser> result = dispenserService.partialUpdate(dispenser);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dispenser.getId().toString())
        );
    }

    /**
     * {@code GET  /dispensers} : get all the dispensers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dispensers in body.
     */
    @GetMapping("/dispensers")
    public ResponseEntity<List<Dispenser>> getAllDispensers(Pageable pageable) {
        log.debug("REST request to get a page of Dispensers");
        Page<Dispenser> page = dispenserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dispensers} : get all the dispensers of a student.
     *
     * @param id the student's Id.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dispensers in body.
     */
    @GetMapping("/dispensers/student/{id}")
    public ResponseEntity<List<Dispenser>> getAllStudentMatieres(@PathVariable Long id) {
        log.debug("REST request to get a Student Matieres");
        List<Dispenser> page = dispenserService.findAllStudentMatieres(id);
        return ResponseEntity.ok(page);
    }

    /**
     * {@code GET  /dispensers} : get all the dispensers of a teacher.
     *
     * @param id the teacher's Id.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dispensers in body.
     */
    @GetMapping("/dispensers/teacher/{id}")
    public ResponseEntity<List<Dispenser>> findByEnseignant(@PathVariable Long id) {
        log.debug("REST request to get a Teachers' Matieres");
        List<Dispenser> page = dispenserService.findByEnseignantId(id);
        return ResponseEntity.ok(page);
    }

    /**
     * {@code GET  /dispensers/:id} : get the "id" dispenser.
     *
     * @param id the id of the dispenser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dispenser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dispensers/{id}")
    public ResponseEntity<Dispenser> getDispenser(@PathVariable Long id) {
        log.debug("REST request to get Dispenser : {}", id);
        Optional<Dispenser> dispenser = dispenserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dispenser);
    }

    /**
     * {@code DELETE  /dispensers/:id} : delete the "id" dispenser.
     *
     * @param id the id of the dispenser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dispensers/{id}")
    public ResponseEntity<Void> deleteDispenser(@PathVariable Long id) {
        log.debug("REST request to delete Dispenser : {}", id);
        dispenserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
