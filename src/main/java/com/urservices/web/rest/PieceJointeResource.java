package com.urservices.web.rest;

import com.urservices.domain.PieceJointe;
import com.urservices.repository.PieceJointeRepository;
import com.urservices.service.PieceJointeService;
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
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.urservices.domain.PieceJointe}.
 */
@RestController
@RequestMapping("/api")
public class PieceJointeResource {

    private final Logger log = LoggerFactory.getLogger(PieceJointeResource.class);

    private static final String ENTITY_NAME = "pieceJointe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PieceJointeService pieceJointeService;

    private final PieceJointeRepository pieceJointeRepository;

    public PieceJointeResource(PieceJointeService pieceJointeService, PieceJointeRepository pieceJointeRepository) {
        this.pieceJointeService = pieceJointeService;
        this.pieceJointeRepository = pieceJointeRepository;
    }

    /**
     * {@code POST  /piece-jointes} : Create a new pieceJointe.
     *
     * @param pieceJointe the pieceJointe to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pieceJointe, or with status {@code 400 (Bad Request)} if the pieceJointe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/piece-jointes")
    public ResponseEntity<PieceJointe> createPieceJointe(@Valid @RequestBody PieceJointe pieceJointe) throws URISyntaxException {
        log.debug("REST request to save PieceJointe : {}", pieceJointe);
        if (pieceJointe.getId() != null) {
            throw new BadRequestAlertException("A new pieceJointe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PieceJointe result = pieceJointeService.save(pieceJointe);
        return ResponseEntity
            .created(new URI("/api/piece-jointes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /piece-jointes} : Create a new pieceJointe of a specific Matiere.
     *
     * @param pieceJointe the pieceJointe to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pieceJointe, or with status {@code 400 (Bad Request)} if the pieceJointe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/piece-jointes/matiere/{id}")
    public ResponseEntity<PieceJointe> createPieceJointe(@PathVariable Long id, @Valid @RequestBody PieceJointe pieceJointe)
        throws URISyntaxException {
        log.debug("REST request to save PieceJointe : {}", pieceJointe);
        if (pieceJointe.getId() != null) {
            throw new BadRequestAlertException("A new pieceJointe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PieceJointe result = pieceJointeService.save(pieceJointe);
        return ResponseEntity
            .created(new URI("/api/piece-jointes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /piece-jointes/:id} : Updates an existing pieceJointe.
     *
     * @param id the id of the pieceJointe to save.
     * @param pieceJointe the pieceJointe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pieceJointe,
     * or with status {@code 400 (Bad Request)} if the pieceJointe is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pieceJointe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/piece-jointes/{id}")
    public ResponseEntity<PieceJointe> updatePieceJointe(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PieceJointe pieceJointe
    ) throws URISyntaxException {
        log.debug("REST request to update PieceJointe : {}, {}", id, pieceJointe);
        if (pieceJointe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pieceJointe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pieceJointeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PieceJointe result = pieceJointeService.save(pieceJointe);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pieceJointe.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /piece-jointes/:id} : Partial updates given fields of an existing pieceJointe, field will ignore if it is null
     *
     * @param id the id of the pieceJointe to save.
     * @param pieceJointe the pieceJointe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pieceJointe,
     * or with status {@code 400 (Bad Request)} if the pieceJointe is not valid,
     * or with status {@code 404 (Not Found)} if the pieceJointe is not found,
     * or with status {@code 500 (Internal Server Error)} if the pieceJointe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/piece-jointes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PieceJointe> partialUpdatePieceJointe(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PieceJointe pieceJointe
    ) throws URISyntaxException {
        log.debug("REST request to partial update PieceJointe partially : {}, {}", id, pieceJointe);
        if (pieceJointe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pieceJointe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pieceJointeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PieceJointe> result = pieceJointeService.partialUpdate(pieceJointe);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pieceJointe.getId().toString())
        );
    }

    /**
     * {@code GET  /piece-jointes} : get all the pieceJointes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pieceJointes in body.
     */
    @GetMapping("/piece-jointes")
    public ResponseEntity<List<PieceJointe>> getAllPieceJointes(Pageable pageable) {
        log.debug("REST request to get a page of PieceJointes");
        Page<PieceJointe> page = pieceJointeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /piece-jointes} : get all the pieceJointes of a given matiere.
     *
     * @param id       The identifier of the desired matiere
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pieceJointes in body.
     */
    @GetMapping("/piece-jointes/matiere/{id}")
    public ResponseEntity<List<PieceJointe>> getAllPieceJointesMatiere(@PathVariable Long id, Pageable pageable) {
        log.debug("REST request to get a page of PieceJointes for Matiere {}", id);
        Page<PieceJointe> page = pieceJointeService.findByMatiereId(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /piece-jointes/:id} : get the "id" pieceJointe.
     *
     * @param id the id of the pieceJointe to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pieceJointe, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/piece-jointes/{id}")
    public ResponseEntity<PieceJointe> getPieceJointe(@PathVariable Long id) {
        log.debug("REST request to get PieceJointe : {}", id);
        Optional<PieceJointe> pieceJointe = pieceJointeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pieceJointe);
    }

    /**
     * {@code GET  /piece-jointes/:id} : get the "id" of a Matiere to prepare a pieceJointe.
     *
     * @param id the id of the pieceJointe to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pieceJointe, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/piece-jointes/matiere/{id}/prepare")
    public ResponseEntity<PieceJointe> preparePieceJointeForUpdate(@NonNull @PathVariable Long id) {
        log.debug("REST request to set PieceJointe  for Matiere: {}", id);
        PieceJointe pieceJointe = pieceJointeService.findOneForSet(id);
        return ResponseEntity.ok(pieceJointe);
    }

    /**
     * {@code DELETE  /piece-jointes/:id} : delete the "id" pieceJointe.
     *
     * @param id the id of the pieceJointe to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/piece-jointes/{id}")
    public ResponseEntity<Void> deletePieceJointe(@PathVariable Long id) {
        log.debug("REST request to delete PieceJointe : {}", id);
        pieceJointeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
