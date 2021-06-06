package com.urservices.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.urservices.IntegrationTest;
import com.urservices.domain.Administrateur;
import com.urservices.domain.enumeration.Grade;
import com.urservices.repository.AdministrateurRepository;
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
 * Integration tests for the {@link AdministrateurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdministrateurResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Grade DEFAULT_GRADE = Grade.M;
    private static final Grade UPDATED_GRADE = Grade.Mme;

    private static final String ENTITY_API_URL = "/api/administrateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdministrateurRepository administrateurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdministrateurMockMvc;

    private Administrateur administrateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrateur createEntity(EntityManager em) {
        Administrateur administrateur = new Administrateur()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .email(DEFAULT_EMAIL)
            .grade(DEFAULT_GRADE);
        return administrateur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrateur createUpdatedEntity(EntityManager em) {
        Administrateur administrateur = new Administrateur()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .grade(UPDATED_GRADE);
        return administrateur;
    }

    @BeforeEach
    public void initTest() {
        administrateur = createEntity(em);
    }

    @Test
    @Transactional
    void createAdministrateur() throws Exception {
        int databaseSizeBeforeCreate = administrateurRepository.findAll().size();
        // Create the Administrateur
        restAdministrateurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administrateur))
            )
            .andExpect(status().isCreated());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeCreate + 1);
        Administrateur testAdministrateur = administrateurList.get(administrateurList.size() - 1);
        assertThat(testAdministrateur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testAdministrateur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testAdministrateur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAdministrateur.getGrade()).isEqualTo(DEFAULT_GRADE);
    }

    @Test
    @Transactional
    void createAdministrateurWithExistingId() throws Exception {
        // Create the Administrateur with an existing ID
        administrateur.setId(1L);

        int databaseSizeBeforeCreate = administrateurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdministrateurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administrateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = administrateurRepository.findAll().size();
        // set the field null
        administrateur.setNom(null);

        // Create the Administrateur, which fails.

        restAdministrateurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administrateur))
            )
            .andExpect(status().isBadRequest());

        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = administrateurRepository.findAll().size();
        // set the field null
        administrateur.setEmail(null);

        // Create the Administrateur, which fails.

        restAdministrateurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administrateur))
            )
            .andExpect(status().isBadRequest());

        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGradeIsRequired() throws Exception {
        int databaseSizeBeforeTest = administrateurRepository.findAll().size();
        // set the field null
        administrateur.setGrade(null);

        // Create the Administrateur, which fails.

        restAdministrateurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administrateur))
            )
            .andExpect(status().isBadRequest());

        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdministrateurs() throws Exception {
        // Initialize the database
        administrateurRepository.saveAndFlush(administrateur);

        // Get all the administrateurList
        restAdministrateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(administrateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.toString())));
    }

    @Test
    @Transactional
    void getAdministrateur() throws Exception {
        // Initialize the database
        administrateurRepository.saveAndFlush(administrateur);

        // Get the administrateur
        restAdministrateurMockMvc
            .perform(get(ENTITY_API_URL_ID, administrateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(administrateur.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAdministrateur() throws Exception {
        // Get the administrateur
        restAdministrateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAdministrateur() throws Exception {
        // Initialize the database
        administrateurRepository.saveAndFlush(administrateur);

        int databaseSizeBeforeUpdate = administrateurRepository.findAll().size();

        // Update the administrateur
        Administrateur updatedAdministrateur = administrateurRepository.findById(administrateur.getId()).get();
        // Disconnect from session so that the updates on updatedAdministrateur are not directly saved in db
        em.detach(updatedAdministrateur);
        updatedAdministrateur.nom(UPDATED_NOM).prenom(UPDATED_PRENOM).email(UPDATED_EMAIL).grade(UPDATED_GRADE);

        restAdministrateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAdministrateur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAdministrateur))
            )
            .andExpect(status().isOk());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeUpdate);
        Administrateur testAdministrateur = administrateurList.get(administrateurList.size() - 1);
        assertThat(testAdministrateur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAdministrateur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testAdministrateur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAdministrateur.getGrade()).isEqualTo(UPDATED_GRADE);
    }

    @Test
    @Transactional
    void putNonExistingAdministrateur() throws Exception {
        int databaseSizeBeforeUpdate = administrateurRepository.findAll().size();
        administrateur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, administrateur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(administrateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdministrateur() throws Exception {
        int databaseSizeBeforeUpdate = administrateurRepository.findAll().size();
        administrateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(administrateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdministrateur() throws Exception {
        int databaseSizeBeforeUpdate = administrateurRepository.findAll().size();
        administrateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administrateur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdministrateurWithPatch() throws Exception {
        // Initialize the database
        administrateurRepository.saveAndFlush(administrateur);

        int databaseSizeBeforeUpdate = administrateurRepository.findAll().size();

        // Update the administrateur using partial update
        Administrateur partialUpdatedAdministrateur = new Administrateur();
        partialUpdatedAdministrateur.setId(administrateur.getId());

        partialUpdatedAdministrateur.nom(UPDATED_NOM).prenom(UPDATED_PRENOM).email(UPDATED_EMAIL);

        restAdministrateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdministrateur))
            )
            .andExpect(status().isOk());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeUpdate);
        Administrateur testAdministrateur = administrateurList.get(administrateurList.size() - 1);
        assertThat(testAdministrateur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAdministrateur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testAdministrateur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAdministrateur.getGrade()).isEqualTo(DEFAULT_GRADE);
    }

    @Test
    @Transactional
    void fullUpdateAdministrateurWithPatch() throws Exception {
        // Initialize the database
        administrateurRepository.saveAndFlush(administrateur);

        int databaseSizeBeforeUpdate = administrateurRepository.findAll().size();

        // Update the administrateur using partial update
        Administrateur partialUpdatedAdministrateur = new Administrateur();
        partialUpdatedAdministrateur.setId(administrateur.getId());

        partialUpdatedAdministrateur.nom(UPDATED_NOM).prenom(UPDATED_PRENOM).email(UPDATED_EMAIL).grade(UPDATED_GRADE);

        restAdministrateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdministrateur))
            )
            .andExpect(status().isOk());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeUpdate);
        Administrateur testAdministrateur = administrateurList.get(administrateurList.size() - 1);
        assertThat(testAdministrateur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAdministrateur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testAdministrateur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAdministrateur.getGrade()).isEqualTo(UPDATED_GRADE);
    }

    @Test
    @Transactional
    void patchNonExistingAdministrateur() throws Exception {
        int databaseSizeBeforeUpdate = administrateurRepository.findAll().size();
        administrateur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, administrateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(administrateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdministrateur() throws Exception {
        int databaseSizeBeforeUpdate = administrateurRepository.findAll().size();
        administrateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(administrateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdministrateur() throws Exception {
        int databaseSizeBeforeUpdate = administrateurRepository.findAll().size();
        administrateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(administrateur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdministrateur() throws Exception {
        // Initialize the database
        administrateurRepository.saveAndFlush(administrateur);

        int databaseSizeBeforeDelete = administrateurRepository.findAll().size();

        // Delete the administrateur
        restAdministrateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, administrateur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
