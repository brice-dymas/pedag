package com.urservices.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.urservices.IntegrationTest;
import com.urservices.domain.Dispenser;
import com.urservices.domain.enumeration.Semestre;
import com.urservices.repository.DispenserRepository;
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
 * Integration tests for the {@link DispenserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DispenserResourceIT {

    private static final Semestre DEFAULT_SEMESTRE = Semestre.SEMESTRE1;
    private static final Semestre UPDATED_SEMESTRE = Semestre.SEMESTRE2;

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String ENTITY_API_URL = "/api/dispensers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DispenserRepository dispenserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDispenserMockMvc;

    private Dispenser dispenser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dispenser createEntity(EntityManager em) {
        Dispenser dispenser = new Dispenser().semestre(DEFAULT_SEMESTRE).actif(DEFAULT_ACTIF);
        return dispenser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dispenser createUpdatedEntity(EntityManager em) {
        Dispenser dispenser = new Dispenser().semestre(UPDATED_SEMESTRE).actif(UPDATED_ACTIF);
        return dispenser;
    }

    @BeforeEach
    public void initTest() {
        dispenser = createEntity(em);
    }

    @Test
    @Transactional
    void createDispenser() throws Exception {
        int databaseSizeBeforeCreate = dispenserRepository.findAll().size();
        // Create the Dispenser
        restDispenserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispenser)))
            .andExpect(status().isCreated());

        // Validate the Dispenser in the database
        List<Dispenser> dispenserList = dispenserRepository.findAll();
        assertThat(dispenserList).hasSize(databaseSizeBeforeCreate + 1);
        Dispenser testDispenser = dispenserList.get(dispenserList.size() - 1);
        assertThat(testDispenser.getSemestre()).isEqualTo(DEFAULT_SEMESTRE);
        assertThat(testDispenser.getActif()).isEqualTo(DEFAULT_ACTIF);
    }

    @Test
    @Transactional
    void createDispenserWithExistingId() throws Exception {
        // Create the Dispenser with an existing ID
        dispenser.setId(1L);

        int databaseSizeBeforeCreate = dispenserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDispenserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispenser)))
            .andExpect(status().isBadRequest());

        // Validate the Dispenser in the database
        List<Dispenser> dispenserList = dispenserRepository.findAll();
        assertThat(dispenserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSemestreIsRequired() throws Exception {
        int databaseSizeBeforeTest = dispenserRepository.findAll().size();
        // set the field null
        dispenser.setSemestre(null);

        // Create the Dispenser, which fails.

        restDispenserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispenser)))
            .andExpect(status().isBadRequest());

        List<Dispenser> dispenserList = dispenserRepository.findAll();
        assertThat(dispenserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDispensers() throws Exception {
        // Initialize the database
        dispenserRepository.saveAndFlush(dispenser);

        // Get all the dispenserList
        restDispenserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dispenser.getId().intValue())))
            .andExpect(jsonPath("$.[*].semestre").value(hasItem(DEFAULT_SEMESTRE.toString())))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())));
    }

    @Test
    @Transactional
    void getDispenser() throws Exception {
        // Initialize the database
        dispenserRepository.saveAndFlush(dispenser);

        // Get the dispenser
        restDispenserMockMvc
            .perform(get(ENTITY_API_URL_ID, dispenser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dispenser.getId().intValue()))
            .andExpect(jsonPath("$.semestre").value(DEFAULT_SEMESTRE.toString()))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingDispenser() throws Exception {
        // Get the dispenser
        restDispenserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDispenser() throws Exception {
        // Initialize the database
        dispenserRepository.saveAndFlush(dispenser);

        int databaseSizeBeforeUpdate = dispenserRepository.findAll().size();

        // Update the dispenser
        Dispenser updatedDispenser = dispenserRepository.findById(dispenser.getId()).get();
        // Disconnect from session so that the updates on updatedDispenser are not directly saved in db
        em.detach(updatedDispenser);
        updatedDispenser.semestre(UPDATED_SEMESTRE).actif(UPDATED_ACTIF);

        restDispenserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDispenser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDispenser))
            )
            .andExpect(status().isOk());

        // Validate the Dispenser in the database
        List<Dispenser> dispenserList = dispenserRepository.findAll();
        assertThat(dispenserList).hasSize(databaseSizeBeforeUpdate);
        Dispenser testDispenser = dispenserList.get(dispenserList.size() - 1);
        assertThat(testDispenser.getSemestre()).isEqualTo(UPDATED_SEMESTRE);
        assertThat(testDispenser.getActif()).isEqualTo(UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void putNonExistingDispenser() throws Exception {
        int databaseSizeBeforeUpdate = dispenserRepository.findAll().size();
        dispenser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDispenserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dispenser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dispenser))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispenser in the database
        List<Dispenser> dispenserList = dispenserRepository.findAll();
        assertThat(dispenserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDispenser() throws Exception {
        int databaseSizeBeforeUpdate = dispenserRepository.findAll().size();
        dispenser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispenserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dispenser))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispenser in the database
        List<Dispenser> dispenserList = dispenserRepository.findAll();
        assertThat(dispenserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDispenser() throws Exception {
        int databaseSizeBeforeUpdate = dispenserRepository.findAll().size();
        dispenser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispenserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dispenser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dispenser in the database
        List<Dispenser> dispenserList = dispenserRepository.findAll();
        assertThat(dispenserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDispenserWithPatch() throws Exception {
        // Initialize the database
        dispenserRepository.saveAndFlush(dispenser);

        int databaseSizeBeforeUpdate = dispenserRepository.findAll().size();

        // Update the dispenser using partial update
        Dispenser partialUpdatedDispenser = new Dispenser();
        partialUpdatedDispenser.setId(dispenser.getId());

        partialUpdatedDispenser.semestre(UPDATED_SEMESTRE).actif(UPDATED_ACTIF);

        restDispenserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDispenser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDispenser))
            )
            .andExpect(status().isOk());

        // Validate the Dispenser in the database
        List<Dispenser> dispenserList = dispenserRepository.findAll();
        assertThat(dispenserList).hasSize(databaseSizeBeforeUpdate);
        Dispenser testDispenser = dispenserList.get(dispenserList.size() - 1);
        assertThat(testDispenser.getSemestre()).isEqualTo(UPDATED_SEMESTRE);
        assertThat(testDispenser.getActif()).isEqualTo(UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void fullUpdateDispenserWithPatch() throws Exception {
        // Initialize the database
        dispenserRepository.saveAndFlush(dispenser);

        int databaseSizeBeforeUpdate = dispenserRepository.findAll().size();

        // Update the dispenser using partial update
        Dispenser partialUpdatedDispenser = new Dispenser();
        partialUpdatedDispenser.setId(dispenser.getId());

        partialUpdatedDispenser.semestre(UPDATED_SEMESTRE).actif(UPDATED_ACTIF);

        restDispenserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDispenser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDispenser))
            )
            .andExpect(status().isOk());

        // Validate the Dispenser in the database
        List<Dispenser> dispenserList = dispenserRepository.findAll();
        assertThat(dispenserList).hasSize(databaseSizeBeforeUpdate);
        Dispenser testDispenser = dispenserList.get(dispenserList.size() - 1);
        assertThat(testDispenser.getSemestre()).isEqualTo(UPDATED_SEMESTRE);
        assertThat(testDispenser.getActif()).isEqualTo(UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void patchNonExistingDispenser() throws Exception {
        int databaseSizeBeforeUpdate = dispenserRepository.findAll().size();
        dispenser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDispenserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dispenser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dispenser))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispenser in the database
        List<Dispenser> dispenserList = dispenserRepository.findAll();
        assertThat(dispenserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDispenser() throws Exception {
        int databaseSizeBeforeUpdate = dispenserRepository.findAll().size();
        dispenser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispenserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dispenser))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispenser in the database
        List<Dispenser> dispenserList = dispenserRepository.findAll();
        assertThat(dispenserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDispenser() throws Exception {
        int databaseSizeBeforeUpdate = dispenserRepository.findAll().size();
        dispenser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispenserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dispenser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dispenser in the database
        List<Dispenser> dispenserList = dispenserRepository.findAll();
        assertThat(dispenserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDispenser() throws Exception {
        // Initialize the database
        dispenserRepository.saveAndFlush(dispenser);

        int databaseSizeBeforeDelete = dispenserRepository.findAll().size();

        // Delete the dispenser
        restDispenserMockMvc
            .perform(delete(ENTITY_API_URL_ID, dispenser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dispenser> dispenserList = dispenserRepository.findAll();
        assertThat(dispenserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
