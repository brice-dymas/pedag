package com.urservices.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.urservices.IntegrationTest;
import com.urservices.domain.PieceJointe;
import com.urservices.repository.PieceJointeRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link PieceJointeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PieceJointeResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONTENU = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENU = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONTENU_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENU_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/piece-jointes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PieceJointeRepository pieceJointeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPieceJointeMockMvc;

    private PieceJointe pieceJointe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PieceJointe createEntity(EntityManager em) {
        PieceJointe pieceJointe = new PieceJointe()
            .libelle(DEFAULT_LIBELLE)
            .contenu(DEFAULT_CONTENU)
            .contenuContentType(DEFAULT_CONTENU_CONTENT_TYPE)
            .dateCreation(DEFAULT_DATE_CREATION);
        return pieceJointe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PieceJointe createUpdatedEntity(EntityManager em) {
        PieceJointe pieceJointe = new PieceJointe()
            .libelle(UPDATED_LIBELLE)
            .contenu(UPDATED_CONTENU)
            .contenuContentType(UPDATED_CONTENU_CONTENT_TYPE)
            .dateCreation(UPDATED_DATE_CREATION);
        return pieceJointe;
    }

    @BeforeEach
    public void initTest() {
        pieceJointe = createEntity(em);
    }

    @Test
    @Transactional
    void createPieceJointe() throws Exception {
        int databaseSizeBeforeCreate = pieceJointeRepository.findAll().size();
        // Create the PieceJointe
        restPieceJointeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pieceJointe)))
            .andExpect(status().isCreated());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeCreate + 1);
        PieceJointe testPieceJointe = pieceJointeList.get(pieceJointeList.size() - 1);
        assertThat(testPieceJointe.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testPieceJointe.getContenu()).isEqualTo(DEFAULT_CONTENU);
        assertThat(testPieceJointe.getContenuContentType()).isEqualTo(DEFAULT_CONTENU_CONTENT_TYPE);
        assertThat(testPieceJointe.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
    }

    @Test
    @Transactional
    void createPieceJointeWithExistingId() throws Exception {
        // Create the PieceJointe with an existing ID
        pieceJointe.setId(1L);

        int databaseSizeBeforeCreate = pieceJointeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPieceJointeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pieceJointe)))
            .andExpect(status().isBadRequest());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = pieceJointeRepository.findAll().size();
        // set the field null
        pieceJointe.setLibelle(null);

        // Create the PieceJointe, which fails.

        restPieceJointeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pieceJointe)))
            .andExpect(status().isBadRequest());

        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPieceJointes() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        // Get all the pieceJointeList
        restPieceJointeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pieceJointe.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].contenuContentType").value(hasItem(DEFAULT_CONTENU_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENU))))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())));
    }

    @Test
    @Transactional
    void getPieceJointe() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        // Get the pieceJointe
        restPieceJointeMockMvc
            .perform(get(ENTITY_API_URL_ID, pieceJointe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pieceJointe.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.contenuContentType").value(DEFAULT_CONTENU_CONTENT_TYPE))
            .andExpect(jsonPath("$.contenu").value(Base64Utils.encodeToString(DEFAULT_CONTENU)))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPieceJointe() throws Exception {
        // Get the pieceJointe
        restPieceJointeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPieceJointe() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();

        // Update the pieceJointe
        PieceJointe updatedPieceJointe = pieceJointeRepository.findById(pieceJointe.getId()).get();
        // Disconnect from session so that the updates on updatedPieceJointe are not directly saved in db
        em.detach(updatedPieceJointe);
        updatedPieceJointe
            .libelle(UPDATED_LIBELLE)
            .contenu(UPDATED_CONTENU)
            .contenuContentType(UPDATED_CONTENU_CONTENT_TYPE)
            .dateCreation(UPDATED_DATE_CREATION);

        restPieceJointeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPieceJointe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPieceJointe))
            )
            .andExpect(status().isOk());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
        PieceJointe testPieceJointe = pieceJointeList.get(pieceJointeList.size() - 1);
        assertThat(testPieceJointe.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testPieceJointe.getContenu()).isEqualTo(UPDATED_CONTENU);
        assertThat(testPieceJointe.getContenuContentType()).isEqualTo(UPDATED_CONTENU_CONTENT_TYPE);
        assertThat(testPieceJointe.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void putNonExistingPieceJointe() throws Exception {
        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();
        pieceJointe.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPieceJointeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pieceJointe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pieceJointe))
            )
            .andExpect(status().isBadRequest());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPieceJointe() throws Exception {
        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();
        pieceJointe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPieceJointeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pieceJointe))
            )
            .andExpect(status().isBadRequest());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPieceJointe() throws Exception {
        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();
        pieceJointe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPieceJointeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pieceJointe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePieceJointeWithPatch() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();

        // Update the pieceJointe using partial update
        PieceJointe partialUpdatedPieceJointe = new PieceJointe();
        partialUpdatedPieceJointe.setId(pieceJointe.getId());

        partialUpdatedPieceJointe.libelle(UPDATED_LIBELLE).contenu(UPDATED_CONTENU).contenuContentType(UPDATED_CONTENU_CONTENT_TYPE);

        restPieceJointeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPieceJointe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPieceJointe))
            )
            .andExpect(status().isOk());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
        PieceJointe testPieceJointe = pieceJointeList.get(pieceJointeList.size() - 1);
        assertThat(testPieceJointe.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testPieceJointe.getContenu()).isEqualTo(UPDATED_CONTENU);
        assertThat(testPieceJointe.getContenuContentType()).isEqualTo(UPDATED_CONTENU_CONTENT_TYPE);
        assertThat(testPieceJointe.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
    }

    @Test
    @Transactional
    void fullUpdatePieceJointeWithPatch() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();

        // Update the pieceJointe using partial update
        PieceJointe partialUpdatedPieceJointe = new PieceJointe();
        partialUpdatedPieceJointe.setId(pieceJointe.getId());

        partialUpdatedPieceJointe
            .libelle(UPDATED_LIBELLE)
            .contenu(UPDATED_CONTENU)
            .contenuContentType(UPDATED_CONTENU_CONTENT_TYPE)
            .dateCreation(UPDATED_DATE_CREATION);

        restPieceJointeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPieceJointe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPieceJointe))
            )
            .andExpect(status().isOk());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
        PieceJointe testPieceJointe = pieceJointeList.get(pieceJointeList.size() - 1);
        assertThat(testPieceJointe.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testPieceJointe.getContenu()).isEqualTo(UPDATED_CONTENU);
        assertThat(testPieceJointe.getContenuContentType()).isEqualTo(UPDATED_CONTENU_CONTENT_TYPE);
        assertThat(testPieceJointe.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void patchNonExistingPieceJointe() throws Exception {
        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();
        pieceJointe.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPieceJointeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pieceJointe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pieceJointe))
            )
            .andExpect(status().isBadRequest());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPieceJointe() throws Exception {
        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();
        pieceJointe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPieceJointeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pieceJointe))
            )
            .andExpect(status().isBadRequest());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPieceJointe() throws Exception {
        int databaseSizeBeforeUpdate = pieceJointeRepository.findAll().size();
        pieceJointe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPieceJointeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pieceJointe))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PieceJointe in the database
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePieceJointe() throws Exception {
        // Initialize the database
        pieceJointeRepository.saveAndFlush(pieceJointe);

        int databaseSizeBeforeDelete = pieceJointeRepository.findAll().size();

        // Delete the pieceJointe
        restPieceJointeMockMvc
            .perform(delete(ENTITY_API_URL_ID, pieceJointe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PieceJointe> pieceJointeList = pieceJointeRepository.findAll();
        assertThat(pieceJointeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
