package com.urservices.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.urservices.IntegrationTest;
import com.urservices.domain.CoursFiliere;
import com.urservices.repository.CoursFiliereRepository;
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
 * Integration tests for the {@link CoursFiliereResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CoursFiliereResourceIT {

    private static final Integer DEFAULT_QUOTA_HORAIRE = 1;
    private static final Integer UPDATED_QUOTA_HORAIRE = 2;

    private static final String ENTITY_API_URL = "/api/cours-filieres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CoursFiliereRepository coursFiliereRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCoursFiliereMockMvc;

    private CoursFiliere coursFiliere;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoursFiliere createEntity(EntityManager em) {
        CoursFiliere coursFiliere = new CoursFiliere().quotaHoraire(DEFAULT_QUOTA_HORAIRE);
        return coursFiliere;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoursFiliere createUpdatedEntity(EntityManager em) {
        CoursFiliere coursFiliere = new CoursFiliere().quotaHoraire(UPDATED_QUOTA_HORAIRE);
        return coursFiliere;
    }

    @BeforeEach
    public void initTest() {
        coursFiliere = createEntity(em);
    }

    @Test
    @Transactional
    void createCoursFiliere() throws Exception {
        int databaseSizeBeforeCreate = coursFiliereRepository.findAll().size();
        // Create the CoursFiliere
        restCoursFiliereMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coursFiliere)))
            .andExpect(status().isCreated());

        // Validate the CoursFiliere in the database
        List<CoursFiliere> coursFiliereList = coursFiliereRepository.findAll();
        assertThat(coursFiliereList).hasSize(databaseSizeBeforeCreate + 1);
        CoursFiliere testCoursFiliere = coursFiliereList.get(coursFiliereList.size() - 1);
        assertThat(testCoursFiliere.getQuotaHoraire()).isEqualTo(DEFAULT_QUOTA_HORAIRE);
    }

    @Test
    @Transactional
    void createCoursFiliereWithExistingId() throws Exception {
        // Create the CoursFiliere with an existing ID
        coursFiliere.setId(1L);

        int databaseSizeBeforeCreate = coursFiliereRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoursFiliereMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coursFiliere)))
            .andExpect(status().isBadRequest());

        // Validate the CoursFiliere in the database
        List<CoursFiliere> coursFiliereList = coursFiliereRepository.findAll();
        assertThat(coursFiliereList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCoursFilieres() throws Exception {
        // Initialize the database
        coursFiliereRepository.saveAndFlush(coursFiliere);

        // Get all the coursFiliereList
        restCoursFiliereMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coursFiliere.getId().intValue())))
            .andExpect(jsonPath("$.[*].quotaHoraire").value(hasItem(DEFAULT_QUOTA_HORAIRE)));
    }

    @Test
    @Transactional
    void getCoursFiliere() throws Exception {
        // Initialize the database
        coursFiliereRepository.saveAndFlush(coursFiliere);

        // Get the coursFiliere
        restCoursFiliereMockMvc
            .perform(get(ENTITY_API_URL_ID, coursFiliere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(coursFiliere.getId().intValue()))
            .andExpect(jsonPath("$.quotaHoraire").value(DEFAULT_QUOTA_HORAIRE));
    }

    @Test
    @Transactional
    void getNonExistingCoursFiliere() throws Exception {
        // Get the coursFiliere
        restCoursFiliereMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCoursFiliere() throws Exception {
        // Initialize the database
        coursFiliereRepository.saveAndFlush(coursFiliere);

        int databaseSizeBeforeUpdate = coursFiliereRepository.findAll().size();

        // Update the coursFiliere
        CoursFiliere updatedCoursFiliere = coursFiliereRepository.findById(coursFiliere.getId()).get();
        // Disconnect from session so that the updates on updatedCoursFiliere are not directly saved in db
        em.detach(updatedCoursFiliere);
        updatedCoursFiliere.quotaHoraire(UPDATED_QUOTA_HORAIRE);

        restCoursFiliereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCoursFiliere.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCoursFiliere))
            )
            .andExpect(status().isOk());

        // Validate the CoursFiliere in the database
        List<CoursFiliere> coursFiliereList = coursFiliereRepository.findAll();
        assertThat(coursFiliereList).hasSize(databaseSizeBeforeUpdate);
        CoursFiliere testCoursFiliere = coursFiliereList.get(coursFiliereList.size() - 1);
        assertThat(testCoursFiliere.getQuotaHoraire()).isEqualTo(UPDATED_QUOTA_HORAIRE);
    }

    @Test
    @Transactional
    void putNonExistingCoursFiliere() throws Exception {
        int databaseSizeBeforeUpdate = coursFiliereRepository.findAll().size();
        coursFiliere.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoursFiliereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coursFiliere.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coursFiliere))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoursFiliere in the database
        List<CoursFiliere> coursFiliereList = coursFiliereRepository.findAll();
        assertThat(coursFiliereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCoursFiliere() throws Exception {
        int databaseSizeBeforeUpdate = coursFiliereRepository.findAll().size();
        coursFiliere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursFiliereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coursFiliere))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoursFiliere in the database
        List<CoursFiliere> coursFiliereList = coursFiliereRepository.findAll();
        assertThat(coursFiliereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCoursFiliere() throws Exception {
        int databaseSizeBeforeUpdate = coursFiliereRepository.findAll().size();
        coursFiliere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursFiliereMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coursFiliere)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoursFiliere in the database
        List<CoursFiliere> coursFiliereList = coursFiliereRepository.findAll();
        assertThat(coursFiliereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCoursFiliereWithPatch() throws Exception {
        // Initialize the database
        coursFiliereRepository.saveAndFlush(coursFiliere);

        int databaseSizeBeforeUpdate = coursFiliereRepository.findAll().size();

        // Update the coursFiliere using partial update
        CoursFiliere partialUpdatedCoursFiliere = new CoursFiliere();
        partialUpdatedCoursFiliere.setId(coursFiliere.getId());

        restCoursFiliereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoursFiliere.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoursFiliere))
            )
            .andExpect(status().isOk());

        // Validate the CoursFiliere in the database
        List<CoursFiliere> coursFiliereList = coursFiliereRepository.findAll();
        assertThat(coursFiliereList).hasSize(databaseSizeBeforeUpdate);
        CoursFiliere testCoursFiliere = coursFiliereList.get(coursFiliereList.size() - 1);
        assertThat(testCoursFiliere.getQuotaHoraire()).isEqualTo(DEFAULT_QUOTA_HORAIRE);
    }

    @Test
    @Transactional
    void fullUpdateCoursFiliereWithPatch() throws Exception {
        // Initialize the database
        coursFiliereRepository.saveAndFlush(coursFiliere);

        int databaseSizeBeforeUpdate = coursFiliereRepository.findAll().size();

        // Update the coursFiliere using partial update
        CoursFiliere partialUpdatedCoursFiliere = new CoursFiliere();
        partialUpdatedCoursFiliere.setId(coursFiliere.getId());

        partialUpdatedCoursFiliere.quotaHoraire(UPDATED_QUOTA_HORAIRE);

        restCoursFiliereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoursFiliere.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoursFiliere))
            )
            .andExpect(status().isOk());

        // Validate the CoursFiliere in the database
        List<CoursFiliere> coursFiliereList = coursFiliereRepository.findAll();
        assertThat(coursFiliereList).hasSize(databaseSizeBeforeUpdate);
        CoursFiliere testCoursFiliere = coursFiliereList.get(coursFiliereList.size() - 1);
        assertThat(testCoursFiliere.getQuotaHoraire()).isEqualTo(UPDATED_QUOTA_HORAIRE);
    }

    @Test
    @Transactional
    void patchNonExistingCoursFiliere() throws Exception {
        int databaseSizeBeforeUpdate = coursFiliereRepository.findAll().size();
        coursFiliere.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoursFiliereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coursFiliere.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coursFiliere))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoursFiliere in the database
        List<CoursFiliere> coursFiliereList = coursFiliereRepository.findAll();
        assertThat(coursFiliereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCoursFiliere() throws Exception {
        int databaseSizeBeforeUpdate = coursFiliereRepository.findAll().size();
        coursFiliere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursFiliereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coursFiliere))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoursFiliere in the database
        List<CoursFiliere> coursFiliereList = coursFiliereRepository.findAll();
        assertThat(coursFiliereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCoursFiliere() throws Exception {
        int databaseSizeBeforeUpdate = coursFiliereRepository.findAll().size();
        coursFiliere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursFiliereMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(coursFiliere))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoursFiliere in the database
        List<CoursFiliere> coursFiliereList = coursFiliereRepository.findAll();
        assertThat(coursFiliereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCoursFiliere() throws Exception {
        // Initialize the database
        coursFiliereRepository.saveAndFlush(coursFiliere);

        int databaseSizeBeforeDelete = coursFiliereRepository.findAll().size();

        // Delete the coursFiliere
        restCoursFiliereMockMvc
            .perform(delete(ENTITY_API_URL_ID, coursFiliere.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CoursFiliere> coursFiliereList = coursFiliereRepository.findAll();
        assertThat(coursFiliereList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
