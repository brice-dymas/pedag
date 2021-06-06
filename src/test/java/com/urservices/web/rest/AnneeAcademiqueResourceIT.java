package com.urservices.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.urservices.IntegrationTest;
import com.urservices.domain.AnneeAcademique;
import com.urservices.repository.AnneeAcademiqueRepository;
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
 * Integration tests for the {@link AnneeAcademiqueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AnneeAcademiqueResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CURRENT = false;
    private static final Boolean UPDATED_CURRENT = true;

    private static final String ENTITY_API_URL = "/api/annee-academiques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AnneeAcademiqueRepository anneeAcademiqueRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnneeAcademiqueMockMvc;

    private AnneeAcademique anneeAcademique;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnneeAcademique createEntity(EntityManager em) {
        AnneeAcademique anneeAcademique = new AnneeAcademique().libelle(DEFAULT_LIBELLE).current(DEFAULT_CURRENT);
        return anneeAcademique;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnneeAcademique createUpdatedEntity(EntityManager em) {
        AnneeAcademique anneeAcademique = new AnneeAcademique().libelle(UPDATED_LIBELLE).current(UPDATED_CURRENT);
        return anneeAcademique;
    }

    @BeforeEach
    public void initTest() {
        anneeAcademique = createEntity(em);
    }

    @Test
    @Transactional
    void createAnneeAcademique() throws Exception {
        int databaseSizeBeforeCreate = anneeAcademiqueRepository.findAll().size();
        // Create the AnneeAcademique
        restAnneeAcademiqueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(anneeAcademique))
            )
            .andExpect(status().isCreated());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeCreate + 1);
        AnneeAcademique testAnneeAcademique = anneeAcademiqueList.get(anneeAcademiqueList.size() - 1);
        assertThat(testAnneeAcademique.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testAnneeAcademique.getCurrent()).isEqualTo(DEFAULT_CURRENT);
    }

    @Test
    @Transactional
    void createAnneeAcademiqueWithExistingId() throws Exception {
        // Create the AnneeAcademique with an existing ID
        anneeAcademique.setId(1L);

        int databaseSizeBeforeCreate = anneeAcademiqueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnneeAcademiqueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(anneeAcademique))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = anneeAcademiqueRepository.findAll().size();
        // set the field null
        anneeAcademique.setLibelle(null);

        // Create the AnneeAcademique, which fails.

        restAnneeAcademiqueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(anneeAcademique))
            )
            .andExpect(status().isBadRequest());

        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAnneeAcademiques() throws Exception {
        // Initialize the database
        anneeAcademiqueRepository.saveAndFlush(anneeAcademique);

        // Get all the anneeAcademiqueList
        restAnneeAcademiqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(anneeAcademique.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].current").value(hasItem(DEFAULT_CURRENT.booleanValue())));
    }

    @Test
    @Transactional
    void getAnneeAcademique() throws Exception {
        // Initialize the database
        anneeAcademiqueRepository.saveAndFlush(anneeAcademique);

        // Get the anneeAcademique
        restAnneeAcademiqueMockMvc
            .perform(get(ENTITY_API_URL_ID, anneeAcademique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(anneeAcademique.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.current").value(DEFAULT_CURRENT.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAnneeAcademique() throws Exception {
        // Get the anneeAcademique
        restAnneeAcademiqueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAnneeAcademique() throws Exception {
        // Initialize the database
        anneeAcademiqueRepository.saveAndFlush(anneeAcademique);

        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();

        // Update the anneeAcademique
        AnneeAcademique updatedAnneeAcademique = anneeAcademiqueRepository.findById(anneeAcademique.getId()).get();
        // Disconnect from session so that the updates on updatedAnneeAcademique are not directly saved in db
        em.detach(updatedAnneeAcademique);
        updatedAnneeAcademique.libelle(UPDATED_LIBELLE).current(UPDATED_CURRENT);

        restAnneeAcademiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAnneeAcademique.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAnneeAcademique))
            )
            .andExpect(status().isOk());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeUpdate);
        AnneeAcademique testAnneeAcademique = anneeAcademiqueList.get(anneeAcademiqueList.size() - 1);
        assertThat(testAnneeAcademique.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testAnneeAcademique.getCurrent()).isEqualTo(UPDATED_CURRENT);
    }

    @Test
    @Transactional
    void putNonExistingAnneeAcademique() throws Exception {
        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();
        anneeAcademique.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnneeAcademiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, anneeAcademique.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anneeAcademique))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnneeAcademique() throws Exception {
        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();
        anneeAcademique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnneeAcademiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anneeAcademique))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnneeAcademique() throws Exception {
        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();
        anneeAcademique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnneeAcademiqueMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(anneeAcademique))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAnneeAcademiqueWithPatch() throws Exception {
        // Initialize the database
        anneeAcademiqueRepository.saveAndFlush(anneeAcademique);

        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();

        // Update the anneeAcademique using partial update
        AnneeAcademique partialUpdatedAnneeAcademique = new AnneeAcademique();
        partialUpdatedAnneeAcademique.setId(anneeAcademique.getId());

        restAnneeAcademiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnneeAcademique.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnneeAcademique))
            )
            .andExpect(status().isOk());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeUpdate);
        AnneeAcademique testAnneeAcademique = anneeAcademiqueList.get(anneeAcademiqueList.size() - 1);
        assertThat(testAnneeAcademique.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testAnneeAcademique.getCurrent()).isEqualTo(DEFAULT_CURRENT);
    }

    @Test
    @Transactional
    void fullUpdateAnneeAcademiqueWithPatch() throws Exception {
        // Initialize the database
        anneeAcademiqueRepository.saveAndFlush(anneeAcademique);

        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();

        // Update the anneeAcademique using partial update
        AnneeAcademique partialUpdatedAnneeAcademique = new AnneeAcademique();
        partialUpdatedAnneeAcademique.setId(anneeAcademique.getId());

        partialUpdatedAnneeAcademique.libelle(UPDATED_LIBELLE).current(UPDATED_CURRENT);

        restAnneeAcademiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnneeAcademique.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnneeAcademique))
            )
            .andExpect(status().isOk());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeUpdate);
        AnneeAcademique testAnneeAcademique = anneeAcademiqueList.get(anneeAcademiqueList.size() - 1);
        assertThat(testAnneeAcademique.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testAnneeAcademique.getCurrent()).isEqualTo(UPDATED_CURRENT);
    }

    @Test
    @Transactional
    void patchNonExistingAnneeAcademique() throws Exception {
        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();
        anneeAcademique.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnneeAcademiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, anneeAcademique.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(anneeAcademique))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnneeAcademique() throws Exception {
        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();
        anneeAcademique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnneeAcademiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(anneeAcademique))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnneeAcademique() throws Exception {
        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();
        anneeAcademique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnneeAcademiqueMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(anneeAcademique))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAnneeAcademique() throws Exception {
        // Initialize the database
        anneeAcademiqueRepository.saveAndFlush(anneeAcademique);

        int databaseSizeBeforeDelete = anneeAcademiqueRepository.findAll().size();

        // Delete the anneeAcademique
        restAnneeAcademiqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, anneeAcademique.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
