package com.urservices.web.rest;

import com.urservices.domain.CoursFiliere;
import com.urservices.repository.CoursFiliereRepository;
import com.urservices.service.CoursFiliereService;
import com.urservices.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.urservices.domain.CoursFiliere}.
 */
@RestController
@RequestMapping("/api")
public class CoursFiliereResource {

    private final Logger log = LoggerFactory.getLogger(CoursFiliereResource.class);

    private static final String ENTITY_NAME = "coursFiliere";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CoursFiliereService coursFiliereService;

    private final CoursFiliereRepository coursFiliereRepository;

    public CoursFiliereResource(CoursFiliereService coursFiliereService, CoursFiliereRepository coursFiliereRepository) {
        this.coursFiliereService = coursFiliereService;
        this.coursFiliereRepository = coursFiliereRepository;
    }

    /**
     * {@code POST  /cours-filieres} : Create a new coursFiliere.
     *
     * @param coursFiliere the coursFiliere to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coursFiliere, or with status {@code 400 (Bad Request)} if the coursFiliere has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cours-filieres")
    public ResponseEntity<CoursFiliere> createCoursFiliere(@RequestBody CoursFiliere coursFiliere) throws URISyntaxException {
        log.debug("REST request to save CoursFiliere : {}", coursFiliere);
        if (coursFiliere.getId() != null) {
            throw new BadRequestAlertException("A new coursFiliere cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CoursFiliere result = coursFiliereService.save(coursFiliere);
        return ResponseEntity
            .created(new URI("/api/cours-filieres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cours-filieres/:id} : Updates an existing coursFiliere.
     *
     * @param id the id of the coursFiliere to save.
     * @param coursFiliere the coursFiliere to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coursFiliere,
     * or with status {@code 400 (Bad Request)} if the coursFiliere is not valid,
     * or with status {@code 500 (Internal Server Error)} if the coursFiliere couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cours-filieres/{id}")
    public ResponseEntity<CoursFiliere> updateCoursFiliere(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CoursFiliere coursFiliere
    ) throws URISyntaxException {
        log.debug("REST request to update CoursFiliere : {}, {}", id, coursFiliere);
        if (coursFiliere.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coursFiliere.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coursFiliereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CoursFiliere result = coursFiliereService.save(coursFiliere);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coursFiliere.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cours-filieres/:id} : Partial updates given fields of an existing coursFiliere, field will ignore if it is null
     *
     * @param id the id of the coursFiliere to save.
     * @param coursFiliere the coursFiliere to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coursFiliere,
     * or with status {@code 400 (Bad Request)} if the coursFiliere is not valid,
     * or with status {@code 404 (Not Found)} if the coursFiliere is not found,
     * or with status {@code 500 (Internal Server Error)} if the coursFiliere couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cours-filieres/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CoursFiliere> partialUpdateCoursFiliere(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CoursFiliere coursFiliere
    ) throws URISyntaxException {
        log.debug("REST request to partial update CoursFiliere partially : {}, {}", id, coursFiliere);
        if (coursFiliere.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coursFiliere.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coursFiliereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CoursFiliere> result = coursFiliereService.partialUpdate(coursFiliere);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coursFiliere.getId().toString())
        );
    }

    /**
     * {@code GET  /cours-filieres} : get all the coursFilieres.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of coursFilieres in body.
     */
    @GetMapping("/cours-filieres")
    public ResponseEntity<List<CoursFiliere>> getAllCoursFilieres(Pageable pageable) {
        log.debug("REST request to get a page of CoursFilieres");
        Page<CoursFiliere> page = coursFiliereService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cours-filieres/:id} : get the "id" coursFiliere.
     *
     * @param id the id of the coursFiliere to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the coursFiliere, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cours-filieres/{id}")
    public ResponseEntity<CoursFiliere> getCoursFiliere(@PathVariable Long id) {
        log.debug("REST request to get CoursFiliere : {}", id);
        Optional<CoursFiliere> coursFiliere = coursFiliereService.findOne(id);
        return ResponseUtil.wrapOrNotFound(coursFiliere);
    }

    /**
     * {@code DELETE  /cours-filieres/:id} : delete the "id" coursFiliere.
     *
     * @param id the id of the coursFiliere to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cours-filieres/{id}")
    public ResponseEntity<Void> deleteCoursFiliere(@PathVariable Long id) {
        log.debug("REST request to delete CoursFiliere : {}", id);
        coursFiliereService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
