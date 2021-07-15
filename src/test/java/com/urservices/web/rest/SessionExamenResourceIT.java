package com.urservices.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.urservices.IntegrationTest;
import com.urservices.domain.SessionExamen;
import com.urservices.domain.enumeration.MoisAnnee;
import com.urservices.domain.enumeration.TypeExamen;
import com.urservices.repository.SessionExamenRepository;
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
 * Integration tests for the {@link SessionExamenResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SessionExamenResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final MoisAnnee DEFAULT_MOIS = MoisAnnee.JANVIER;
    private static final MoisAnnee UPDATED_MOIS = MoisAnnee.FEVRIER;

    private static final Integer DEFAULT_ANNEE = 2000;
    private static final Integer UPDATED_ANNEE = 2001;

    private static final TypeExamen DEFAULT_TYPE = TypeExamen.CONTROLE;
    private static final TypeExamen UPDATED_TYPE = TypeExamen.SEMESTRIEL;

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String ENTITY_API_URL = "/api/session-examen";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SessionExamenRepository sessionExamenRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSessionExamenMockMvc;

    private SessionExamen sessionExamen;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionExamen createEntity(EntityManager em) {
        SessionExamen sessionExamen = new SessionExamen()
            .libelle(DEFAULT_LIBELLE)
            .mois(DEFAULT_MOIS)
            .annee(DEFAULT_ANNEE)
            .type(DEFAULT_TYPE)
            .actif(DEFAULT_ACTIF);
        return sessionExamen;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionExamen createUpdatedEntity(EntityManager em) {
        SessionExamen sessionExamen = new SessionExamen()
            .libelle(UPDATED_LIBELLE)
            .mois(UPDATED_MOIS)
            .annee(UPDATED_ANNEE)
            .type(UPDATED_TYPE)
            .actif(UPDATED_ACTIF);
        return sessionExamen;
    }

    @BeforeEach
    public void initTest() {
        sessionExamen = createEntity(em);
    }

    @Test
    @Transactional
    void createSessionExamen() throws Exception {
        int databaseSizeBeforeCreate = sessionExamenRepository.findAll().size();
        // Create the SessionExamen
        restSessionExamenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionExamen)))
            .andExpect(status().isCreated());

        // Validate the SessionExamen in the database
        List<SessionExamen> sessionExamenList = sessionExamenRepository.findAll();
        assertThat(sessionExamenList).hasSize(databaseSizeBeforeCreate + 1);
        SessionExamen testSessionExamen = sessionExamenList.get(sessionExamenList.size() - 1);
        assertThat(testSessionExamen.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testSessionExamen.getMois()).isEqualTo(DEFAULT_MOIS);
        assertThat(testSessionExamen.getAnnee()).isEqualTo(DEFAULT_ANNEE);
        assertThat(testSessionExamen.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSessionExamen.getActif()).isEqualTo(DEFAULT_ACTIF);
    }

    @Test
    @Transactional
    void createSessionExamenWithExistingId() throws Exception {
        // Create the SessionExamen with an existing ID
        sessionExamen.setId(1L);

        int databaseSizeBeforeCreate = sessionExamenRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessionExamenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionExamen)))
            .andExpect(status().isBadRequest());

        // Validate the SessionExamen in the database
        List<SessionExamen> sessionExamenList = sessionExamenRepository.findAll();
        assertThat(sessionExamenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMoisIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionExamenRepository.findAll().size();
        // set the field null
        sessionExamen.setMois(null);

        // Create the SessionExamen, which fails.

        restSessionExamenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionExamen)))
            .andExpect(status().isBadRequest());

        List<SessionExamen> sessionExamenList = sessionExamenRepository.findAll();
        assertThat(sessionExamenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAnneeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionExamenRepository.findAll().size();
        // set the field null
        sessionExamen.setAnnee(null);

        // Create the SessionExamen, which fails.

        restSessionExamenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionExamen)))
            .andExpect(status().isBadRequest());

        List<SessionExamen> sessionExamenList = sessionExamenRepository.findAll();
        assertThat(sessionExamenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionExamenRepository.findAll().size();
        // set the field null
        sessionExamen.setType(null);

        // Create the SessionExamen, which fails.

        restSessionExamenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionExamen)))
            .andExpect(status().isBadRequest());

        List<SessionExamen> sessionExamenList = sessionExamenRepository.findAll();
        assertThat(sessionExamenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSessionExamen() throws Exception {
        // Initialize the database
        sessionExamenRepository.saveAndFlush(sessionExamen);

        // Get all the sessionExamenList
        restSessionExamenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessionExamen.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].mois").value(hasItem(DEFAULT_MOIS.toString())))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())));
    }

    @Test
    @Transactional
    void getSessionExamen() throws Exception {
        // Initialize the database
        sessionExamenRepository.saveAndFlush(sessionExamen);

        // Get the sessionExamen
        restSessionExamenMockMvc
            .perform(get(ENTITY_API_URL_ID, sessionExamen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sessionExamen.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.mois").value(DEFAULT_MOIS.toString()))
            .andExpect(jsonPath("$.annee").value(DEFAULT_ANNEE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSessionExamen() throws Exception {
        // Get the sessionExamen
        restSessionExamenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSessionExamen() throws Exception {
        // Initialize the database
        sessionExamenRepository.saveAndFlush(sessionExamen);

        int databaseSizeBeforeUpdate = sessionExamenRepository.findAll().size();

        // Update the sessionExamen
        SessionExamen updatedSessionExamen = sessionExamenRepository.findById(sessionExamen.getId()).get();
        // Disconnect from session so that the updates on updatedSessionExamen are not directly saved in db
        em.detach(updatedSessionExamen);
        updatedSessionExamen.libelle(UPDATED_LIBELLE).mois(UPDATED_MOIS).annee(UPDATED_ANNEE).type(UPDATED_TYPE).actif(UPDATED_ACTIF);

        restSessionExamenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSessionExamen.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSessionExamen))
            )
            .andExpect(status().isOk());

        // Validate the SessionExamen in the database
        List<SessionExamen> sessionExamenList = sessionExamenRepository.findAll();
        assertThat(sessionExamenList).hasSize(databaseSizeBeforeUpdate);
        SessionExamen testSessionExamen = sessionExamenList.get(sessionExamenList.size() - 1);
        assertThat(testSessionExamen.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSessionExamen.getMois()).isEqualTo(UPDATED_MOIS);
        assertThat(testSessionExamen.getAnnee()).isEqualTo(UPDATED_ANNEE);
        assertThat(testSessionExamen.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSessionExamen.getActif()).isEqualTo(UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void putNonExistingSessionExamen() throws Exception {
        int databaseSizeBeforeUpdate = sessionExamenRepository.findAll().size();
        sessionExamen.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionExamenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionExamen.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionExamen))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionExamen in the database
        List<SessionExamen> sessionExamenList = sessionExamenRepository.findAll();
        assertThat(sessionExamenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSessionExamen() throws Exception {
        int databaseSizeBeforeUpdate = sessionExamenRepository.findAll().size();
        sessionExamen.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionExamenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionExamen))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionExamen in the database
        List<SessionExamen> sessionExamenList = sessionExamenRepository.findAll();
        assertThat(sessionExamenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSessionExamen() throws Exception {
        int databaseSizeBeforeUpdate = sessionExamenRepository.findAll().size();
        sessionExamen.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionExamenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionExamen)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionExamen in the database
        List<SessionExamen> sessionExamenList = sessionExamenRepository.findAll();
        assertThat(sessionExamenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSessionExamenWithPatch() throws Exception {
        // Initialize the database
        sessionExamenRepository.saveAndFlush(sessionExamen);

        int databaseSizeBeforeUpdate = sessionExamenRepository.findAll().size();

        // Update the sessionExamen using partial update
        SessionExamen partialUpdatedSessionExamen = new SessionExamen();
        partialUpdatedSessionExamen.setId(sessionExamen.getId());

        partialUpdatedSessionExamen.libelle(UPDATED_LIBELLE).annee(UPDATED_ANNEE);

        restSessionExamenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionExamen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSessionExamen))
            )
            .andExpect(status().isOk());

        // Validate the SessionExamen in the database
        List<SessionExamen> sessionExamenList = sessionExamenRepository.findAll();
        assertThat(sessionExamenList).hasSize(databaseSizeBeforeUpdate);
        SessionExamen testSessionExamen = sessionExamenList.get(sessionExamenList.size() - 1);
        assertThat(testSessionExamen.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSessionExamen.getMois()).isEqualTo(DEFAULT_MOIS);
        assertThat(testSessionExamen.getAnnee()).isEqualTo(UPDATED_ANNEE);
        assertThat(testSessionExamen.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSessionExamen.getActif()).isEqualTo(DEFAULT_ACTIF);
    }

    @Test
    @Transactional
    void fullUpdateSessionExamenWithPatch() throws Exception {
        // Initialize the database
        sessionExamenRepository.saveAndFlush(sessionExamen);

        int databaseSizeBeforeUpdate = sessionExamenRepository.findAll().size();

        // Update the sessionExamen using partial update
        SessionExamen partialUpdatedSessionExamen = new SessionExamen();
        partialUpdatedSessionExamen.setId(sessionExamen.getId());

        partialUpdatedSessionExamen
            .libelle(UPDATED_LIBELLE)
            .mois(UPDATED_MOIS)
            .annee(UPDATED_ANNEE)
            .type(UPDATED_TYPE)
            .actif(UPDATED_ACTIF);

        restSessionExamenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionExamen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSessionExamen))
            )
            .andExpect(status().isOk());

        // Validate the SessionExamen in the database
        List<SessionExamen> sessionExamenList = sessionExamenRepository.findAll();
        assertThat(sessionExamenList).hasSize(databaseSizeBeforeUpdate);
        SessionExamen testSessionExamen = sessionExamenList.get(sessionExamenList.size() - 1);
        assertThat(testSessionExamen.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSessionExamen.getMois()).isEqualTo(UPDATED_MOIS);
        assertThat(testSessionExamen.getAnnee()).isEqualTo(UPDATED_ANNEE);
        assertThat(testSessionExamen.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSessionExamen.getActif()).isEqualTo(UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void patchNonExistingSessionExamen() throws Exception {
        int databaseSizeBeforeUpdate = sessionExamenRepository.findAll().size();
        sessionExamen.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionExamenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sessionExamen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionExamen))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionExamen in the database
        List<SessionExamen> sessionExamenList = sessionExamenRepository.findAll();
        assertThat(sessionExamenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSessionExamen() throws Exception {
        int databaseSizeBeforeUpdate = sessionExamenRepository.findAll().size();
        sessionExamen.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionExamenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionExamen))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionExamen in the database
        List<SessionExamen> sessionExamenList = sessionExamenRepository.findAll();
        assertThat(sessionExamenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSessionExamen() throws Exception {
        int databaseSizeBeforeUpdate = sessionExamenRepository.findAll().size();
        sessionExamen.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionExamenMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sessionExamen))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionExamen in the database
        List<SessionExamen> sessionExamenList = sessionExamenRepository.findAll();
        assertThat(sessionExamenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSessionExamen() throws Exception {
        // Initialize the database
        sessionExamenRepository.saveAndFlush(sessionExamen);

        int databaseSizeBeforeDelete = sessionExamenRepository.findAll().size();

        // Delete the sessionExamen
        restSessionExamenMockMvc
            .perform(delete(ENTITY_API_URL_ID, sessionExamen.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SessionExamen> sessionExamenList = sessionExamenRepository.findAll();
        assertThat(sessionExamenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
