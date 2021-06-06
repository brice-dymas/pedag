package com.urservices.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.urservices.IntegrationTest;
import com.urservices.domain.Requete;
import com.urservices.domain.enumeration.StatutRequete;
import com.urservices.repository.RequeteRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RequeteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RequeteResourceIT {

    private static final String DEFAULT_OBJET = "AAAAAAAAAA";
    private static final String UPDATED_OBJET = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final StatutRequete DEFAULT_STATUT = StatutRequete.EN_ATTENTE;
    private static final StatutRequete UPDATED_STATUT = StatutRequete.FONDE;

    private static final Boolean DEFAULT_TRAITER = false;
    private static final Boolean UPDATED_TRAITER = true;

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFICATION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/requetes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RequeteRepository requeteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRequeteMockMvc;

    private Requete requete;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Requete createEntity(EntityManager em) {
        Requete requete = new Requete()
            .objet(DEFAULT_OBJET)
            .description(DEFAULT_DESCRIPTION)
            .statut(DEFAULT_STATUT)
            .traiter(DEFAULT_TRAITER)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateModification(DEFAULT_DATE_MODIFICATION);
        return requete;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Requete createUpdatedEntity(EntityManager em) {
        Requete requete = new Requete()
            .objet(UPDATED_OBJET)
            .description(UPDATED_DESCRIPTION)
            .statut(UPDATED_STATUT)
            .traiter(UPDATED_TRAITER)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION);
        return requete;
    }

    @BeforeEach
    public void initTest() {
        requete = createEntity(em);
    }

    @Test
    @Transactional
    void createRequete() throws Exception {
        int databaseSizeBeforeCreate = requeteRepository.findAll().size();
        // Create the Requete
        restRequeteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(requete)))
            .andExpect(status().isCreated());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeCreate + 1);
        Requete testRequete = requeteList.get(requeteList.size() - 1);
        assertThat(testRequete.getObjet()).isEqualTo(DEFAULT_OBJET);
        assertThat(testRequete.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRequete.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testRequete.getTraiter()).isEqualTo(DEFAULT_TRAITER);
        assertThat(testRequete.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testRequete.getDateModification()).isEqualTo(DEFAULT_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void createRequeteWithExistingId() throws Exception {
        // Create the Requete with an existing ID
        requete.setId(1L);

        int databaseSizeBeforeCreate = requeteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequeteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(requete)))
            .andExpect(status().isBadRequest());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkObjetIsRequired() throws Exception {
        int databaseSizeBeforeTest = requeteRepository.findAll().size();
        // set the field null
        requete.setObjet(null);

        // Create the Requete, which fails.

        restRequeteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(requete)))
            .andExpect(status().isBadRequest());

        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = requeteRepository.findAll().size();
        // set the field null
        requete.setDescription(null);

        // Create the Requete, which fails.

        restRequeteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(requete)))
            .andExpect(status().isBadRequest());

        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRequetes() throws Exception {
        // Initialize the database
        requeteRepository.saveAndFlush(requete);

        // Get all the requeteList
        restRequeteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requete.getId().intValue())))
            .andExpect(jsonPath("$.[*].objet").value(hasItem(DEFAULT_OBJET)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].traiter").value(hasItem(DEFAULT_TRAITER.booleanValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(DEFAULT_DATE_MODIFICATION.toString())));
    }

    @Test
    @Transactional
    void getRequete() throws Exception {
        // Initialize the database
        requeteRepository.saveAndFlush(requete);

        // Get the requete
        restRequeteMockMvc
            .perform(get(ENTITY_API_URL_ID, requete.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(requete.getId().intValue()))
            .andExpect(jsonPath("$.objet").value(DEFAULT_OBJET))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.traiter").value(DEFAULT_TRAITER.booleanValue()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.dateModification").value(DEFAULT_DATE_MODIFICATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRequete() throws Exception {
        // Get the requete
        restRequeteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRequete() throws Exception {
        // Initialize the database
        requeteRepository.saveAndFlush(requete);

        int databaseSizeBeforeUpdate = requeteRepository.findAll().size();

        // Update the requete
        Requete updatedRequete = requeteRepository.findById(requete.getId()).get();
        // Disconnect from session so that the updates on updatedRequete are not directly saved in db
        em.detach(updatedRequete);
        updatedRequete
            .objet(UPDATED_OBJET)
            .description(UPDATED_DESCRIPTION)
            .statut(UPDATED_STATUT)
            .traiter(UPDATED_TRAITER)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION);

        restRequeteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRequete.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRequete))
            )
            .andExpect(status().isOk());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
        Requete testRequete = requeteList.get(requeteList.size() - 1);
        assertThat(testRequete.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testRequete.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRequete.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testRequete.getTraiter()).isEqualTo(UPDATED_TRAITER);
        assertThat(testRequete.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testRequete.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void putNonExistingRequete() throws Exception {
        int databaseSizeBeforeUpdate = requeteRepository.findAll().size();
        requete.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequeteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, requete.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRequete() throws Exception {
        int databaseSizeBeforeUpdate = requeteRepository.findAll().size();
        requete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequeteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRequete() throws Exception {
        int databaseSizeBeforeUpdate = requeteRepository.findAll().size();
        requete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequeteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(requete)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRequeteWithPatch() throws Exception {
        // Initialize the database
        requeteRepository.saveAndFlush(requete);

        int databaseSizeBeforeUpdate = requeteRepository.findAll().size();

        // Update the requete using partial update
        Requete partialUpdatedRequete = new Requete();
        partialUpdatedRequete.setId(requete.getId());

        partialUpdatedRequete.statut(UPDATED_STATUT).traiter(UPDATED_TRAITER);

        restRequeteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRequete))
            )
            .andExpect(status().isOk());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
        Requete testRequete = requeteList.get(requeteList.size() - 1);
        assertThat(testRequete.getObjet()).isEqualTo(DEFAULT_OBJET);
        assertThat(testRequete.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRequete.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testRequete.getTraiter()).isEqualTo(UPDATED_TRAITER);
        assertThat(testRequete.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testRequete.getDateModification()).isEqualTo(DEFAULT_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void fullUpdateRequeteWithPatch() throws Exception {
        // Initialize the database
        requeteRepository.saveAndFlush(requete);

        int databaseSizeBeforeUpdate = requeteRepository.findAll().size();

        // Update the requete using partial update
        Requete partialUpdatedRequete = new Requete();
        partialUpdatedRequete.setId(requete.getId());

        partialUpdatedRequete
            .objet(UPDATED_OBJET)
            .description(UPDATED_DESCRIPTION)
            .statut(UPDATED_STATUT)
            .traiter(UPDATED_TRAITER)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION);

        restRequeteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRequete))
            )
            .andExpect(status().isOk());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
        Requete testRequete = requeteList.get(requeteList.size() - 1);
        assertThat(testRequete.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testRequete.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRequete.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testRequete.getTraiter()).isEqualTo(UPDATED_TRAITER);
        assertThat(testRequete.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testRequete.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void patchNonExistingRequete() throws Exception {
        int databaseSizeBeforeUpdate = requeteRepository.findAll().size();
        requete.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequeteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, requete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(requete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRequete() throws Exception {
        int databaseSizeBeforeUpdate = requeteRepository.findAll().size();
        requete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequeteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(requete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRequete() throws Exception {
        int databaseSizeBeforeUpdate = requeteRepository.findAll().size();
        requete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequeteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(requete)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Requete in the database
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRequete() throws Exception {
        // Initialize the database
        requeteRepository.saveAndFlush(requete);

        int databaseSizeBeforeDelete = requeteRepository.findAll().size();

        // Delete the requete
        restRequeteMockMvc
            .perform(delete(ENTITY_API_URL_ID, requete.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Requete> requeteList = requeteRepository.findAll();
        assertThat(requeteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
