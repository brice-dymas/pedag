package com.urservices.web.rest;

import com.urservices.domain.AnneeAcademique;
import com.urservices.repository.AnneeAcademiqueRepository;
import com.urservices.service.AnneeAcademiqueService;
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
 * REST controller for managing {@link com.urservices.domain.AnneeAcademique}.
 */
@RestController
@RequestMapping("/api")
public class AnneeAcademiqueResource {

    private final Logger log = LoggerFactory.getLogger(AnneeAcademiqueResource.class);

    private static final String ENTITY_NAME = "anneeAcademique";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnneeAcademiqueService anneeAcademiqueService;

    private final AnneeAcademiqueRepository anneeAcademiqueRepository;

    public AnneeAcademiqueResource(AnneeAcademiqueService anneeAcademiqueService, AnneeAcademiqueRepository anneeAcademiqueRepository) {
        this.anneeAcademiqueService = anneeAcademiqueService;
        this.anneeAcademiqueRepository = anneeAcademiqueRepository;
    }

    /**
     * {@code POST  /annee-academiques} : Create a new anneeAcademique.
     *
     * @param anneeAcademique the anneeAcademique to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new anneeAcademique, or with status {@code 400 (Bad Request)} if the anneeAcademique has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/annee-academiques")
    public ResponseEntity<AnneeAcademique> createAnneeAcademique(@Valid @RequestBody AnneeAcademique anneeAcademique)
        throws URISyntaxException {
        log.debug("REST request to save AnneeAcademique : {}", anneeAcademique);
        if (anneeAcademique.getId() != null) {
            throw new BadRequestAlertException("A new anneeAcademique cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnneeAcademique result = anneeAcademiqueService.save(anneeAcademique);
        return ResponseEntity
            .created(new URI("/api/annee-academiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /annee-academiques/:id} : Updates an existing anneeAcademique.
     *
     * @param id the id of the anneeAcademique to save.
     * @param anneeAcademique the anneeAcademique to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated anneeAcademique,
     * or with status {@code 400 (Bad Request)} if the anneeAcademique is not valid,
     * or with status {@code 500 (Internal Server Error)} if the anneeAcademique couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/annee-academiques/{id}")
    public ResponseEntity<AnneeAcademique> updateAnneeAcademique(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AnneeAcademique anneeAcademique
    ) throws URISyntaxException {
        log.debug("REST request to update AnneeAcademique : {}, {}", id, anneeAcademique);
        if (anneeAcademique.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, anneeAcademique.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!anneeAcademiqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AnneeAcademique result = anneeAcademiqueService.save(anneeAcademique);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, anneeAcademique.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /annee-academiques/:id} : Partial updates given fields of an existing anneeAcademique, field will ignore if it is null
     *
     * @param id the id of the anneeAcademique to save.
     * @param anneeAcademique the anneeAcademique to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated anneeAcademique,
     * or with status {@code 400 (Bad Request)} if the anneeAcademique is not valid,
     * or with status {@code 404 (Not Found)} if the anneeAcademique is not found,
     * or with status {@code 500 (Internal Server Error)} if the anneeAcademique couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/annee-academiques/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AnneeAcademique> partialUpdateAnneeAcademique(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AnneeAcademique anneeAcademique
    ) throws URISyntaxException {
        log.debug("REST request to partial update AnneeAcademique partially : {}, {}", id, anneeAcademique);
        if (anneeAcademique.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, anneeAcademique.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!anneeAcademiqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnneeAcademique> result = anneeAcademiqueService.partialUpdate(anneeAcademique);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, anneeAcademique.getId().toString())
        );
    }

    /**
     * {@code GET  /annee-academiques} : get all the anneeAcademiques.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of anneeAcademiques in body.
     */
    @GetMapping("/annee-academiques")
    public ResponseEntity<List<AnneeAcademique>> getAllAnneeAcademiques(Pageable pageable) {
        log.debug("REST request to get a page of AnneeAcademiques");
        Page<AnneeAcademique> page = anneeAcademiqueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /annee-academiques/:id} : get the "id" anneeAcademique.
     *
     * @param id the id of the anneeAcademique to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the anneeAcademique, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/annee-academiques/{id}")
    public ResponseEntity<AnneeAcademique> getAnneeAcademique(@PathVariable Long id) {
        log.debug("REST request to get AnneeAcademique : {}", id);
        Optional<AnneeAcademique> anneeAcademique = anneeAcademiqueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(anneeAcademique);
    }

    /**
     * {@code DELETE  /annee-academiques/:id} : delete the "id" anneeAcademique.
     *
     * @param id the id of the anneeAcademique to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/annee-academiques/{id}")
    public ResponseEntity<Void> deleteAnneeAcademique(@PathVariable Long id) {
        log.debug("REST request to delete AnneeAcademique : {}", id);
        anneeAcademiqueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
