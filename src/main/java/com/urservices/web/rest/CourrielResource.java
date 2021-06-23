package com.urservices.web.rest;

import com.urservices.domain.Courriel;
import com.urservices.repository.CourrielRepository;
import com.urservices.service.CourrielService;
import com.urservices.service.MailService;
import com.urservices.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.urservices.domain.Courriel}.
 */
@RestController
@RequestMapping("/api")
public class CourrielResource {

    private final Logger log = LoggerFactory.getLogger(CourrielResource.class);

    private static final String ENTITY_NAME = "courriel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MailService mailService;

    private final CourrielService courrielService;

    private final CourrielRepository courrielRepository;

    public CourrielResource(MailService mailService, CourrielService courrielService, CourrielRepository courrielRepository) {
        this.mailService = mailService;
        this.courrielService = courrielService;
        this.courrielRepository = courrielRepository;
    }

    /**
     * {@code POST  /courriels} : Create a new courriel.
     *
     * @param courriel the courriel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courriel, or with status {@code 400 (Bad Request)} if the courriel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/courriels")
    public ResponseEntity<Courriel> createCourriel(@Valid @RequestBody Courriel courriel) throws URISyntaxException {
        log.debug("REST request to save Courriel : {}", courriel);
        if (courriel.getId() != null) {
            throw new BadRequestAlertException("A new courriel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        courriel.setDateCreation(LocalDate.now());
        Courriel result = courrielService.save(courriel);
        mailService.sendEmail(
            "vanessanjeumen@gmail.com",
            "Nouveau courriel envoye depuis votre application",
            result.getMessage(),
            false,
            false
        );
        mailService.sendEmail(result.getReceiver(), result.getObjet(), result.getMessage(), false, false);
        mailService.sendEmail("briceguemkam@gmail.com", result.getObjet(), result.getMessage(), false, false);
        return ResponseEntity
            .created(new URI("/api/courriels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /courriels/:id} : Updates an existing courriel.
     *
     * @param id the id of the courriel to save.
     * @param courriel the courriel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courriel,
     * or with status {@code 400 (Bad Request)} if the courriel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courriel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/courriels/{id}")
    public ResponseEntity<Courriel> updateCourriel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Courriel courriel
    ) throws URISyntaxException {
        log.debug("REST request to update Courriel : {}, {}", id, courriel);
        if (courriel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courriel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courrielRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Courriel result = courrielService.save(courriel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courriel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /courriels/:id} : Partial updates given fields of an existing courriel, field will ignore if it is null
     *
     * @param id the id of the courriel to save.
     * @param courriel the courriel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courriel,
     * or with status {@code 400 (Bad Request)} if the courriel is not valid,
     * or with status {@code 404 (Not Found)} if the courriel is not found,
     * or with status {@code 500 (Internal Server Error)} if the courriel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/courriels/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Courriel> partialUpdateCourriel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Courriel courriel
    ) throws URISyntaxException {
        log.debug("REST request to partial update Courriel partially : {}, {}", id, courriel);
        if (courriel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courriel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courrielRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Courriel> result = courrielService.partialUpdate(courriel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courriel.getId().toString())
        );
    }

    /**
     * {@code GET  /courriels} : get all the courriels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courriels in body.
     */
    @GetMapping("/courriels")
    public ResponseEntity<List<Courriel>> getAllCourriels(Pageable pageable) {
        log.debug("REST request to get a page of Courriels");
        Page<Courriel> page = courrielService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /courriels/student/:id} : get all the courriels of a given student.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courriels in body.
     */
    @GetMapping("/courriels/student/{id}")
    public ResponseEntity<List<Courriel>> findByEtudiantUserId(@PathVariable Long id, Pageable pageable) {
        log.debug("REST request to get a page of Courriels for Student {}", id);
        Page<Courriel> page = courrielService.findByEtudiantUserId(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /courriels/:id} : get the "id" courriel.
     *
     * @param id the id of the courriel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courriel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/courriels/{id}")
    public ResponseEntity<Courriel> getCourriel(@PathVariable Long id) {
        log.debug("REST request to get Courriel : {}", id);
        Optional<Courriel> courriel = courrielService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courriel);
    }

    /**
     * {@code DELETE  /courriels/:id} : delete the "id" courriel.
     *
     * @param id the id of the courriel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/courriels/{id}")
    public ResponseEntity<Void> deleteCourriel(@PathVariable Long id) {
        log.debug("REST request to delete Courriel : {}", id);
        courrielService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
