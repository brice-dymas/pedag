package com.urservices.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.urservices.IntegrationTest;
import com.urservices.domain.Deliberation;
import com.urservices.domain.SessionExamen;
import com.urservices.domain.enumeration.ConditionAppliquer;
import com.urservices.domain.enumeration.ConditionSelection;
import com.urservices.repository.DeliberationRepository;
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
 * Integration tests for the {@link DeliberationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeliberationResourceIT {

    private static final ConditionSelection DEFAULT_CRITERE_SELECTION = ConditionSelection.MOYENNE_EGALE_A;
    private static final ConditionSelection UPDATED_CRITERE_SELECTION = ConditionSelection.MOYENNE_INFERIEURE_A;

    private static final Float DEFAULT_VALEUR_SELECTION_DEBUT = 0F;
    private static final Float UPDATED_VALEUR_SELECTION_DEBUT = 1F;

    private static final Float DEFAULT_VALEUR_SELECTION_FIN = 1F;
    private static final Float UPDATED_VALEUR_SELECTION_FIN = 2F;

    private static final ConditionAppliquer DEFAULT_CRITERE_APPLIQUER = ConditionAppliquer.AJOUTER;
    private static final ConditionAppliquer UPDATED_CRITERE_APPLIQUER = ConditionAppliquer.RETRANCHER;

    private static final Float DEFAULT_VALEUR_APPLIQUER = 0F;
    private static final Float UPDATED_VALEUR_APPLIQUER = 1F;

    private static final LocalDate DEFAULT_DATE_DELIBERATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DELIBERATION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/deliberations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeliberationRepository deliberationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeliberationMockMvc;

    private Deliberation deliberation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deliberation createEntity(EntityManager em) {
        Deliberation deliberation = new Deliberation()
            .critereSelection(DEFAULT_CRITERE_SELECTION)
            .valeurSelectionDebut(DEFAULT_VALEUR_SELECTION_DEBUT)
            .valeurSelectionFin(DEFAULT_VALEUR_SELECTION_FIN)
            .critereAppliquer(DEFAULT_CRITERE_APPLIQUER)
            .valeurAppliquer(DEFAULT_VALEUR_APPLIQUER)
            .dateDeliberation(DEFAULT_DATE_DELIBERATION);
        // Add required entity
        SessionExamen sessionExamen;
        if (TestUtil.findAll(em, SessionExamen.class).isEmpty()) {
            sessionExamen = SessionExamenResourceIT.createEntity(em);
            em.persist(sessionExamen);
            em.flush();
        } else {
            sessionExamen = TestUtil.findAll(em, SessionExamen.class).get(0);
        }
        deliberation.setSessionExamen(sessionExamen);
        return deliberation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deliberation createUpdatedEntity(EntityManager em) {
        Deliberation deliberation = new Deliberation()
            .critereSelection(UPDATED_CRITERE_SELECTION)
            .valeurSelectionDebut(UPDATED_VALEUR_SELECTION_DEBUT)
            .valeurSelectionFin(UPDATED_VALEUR_SELECTION_FIN)
            .critereAppliquer(UPDATED_CRITERE_APPLIQUER)
            .valeurAppliquer(UPDATED_VALEUR_APPLIQUER)
            .dateDeliberation(UPDATED_DATE_DELIBERATION);
        // Add required entity
        SessionExamen sessionExamen;
        if (TestUtil.findAll(em, SessionExamen.class).isEmpty()) {
            sessionExamen = SessionExamenResourceIT.createUpdatedEntity(em);
            em.persist(sessionExamen);
            em.flush();
        } else {
            sessionExamen = TestUtil.findAll(em, SessionExamen.class).get(0);
        }
        deliberation.setSessionExamen(sessionExamen);
        return deliberation;
    }

    @BeforeEach
    public void initTest() {
        deliberation = createEntity(em);
    }

    @Test
    @Transactional
    void createDeliberation() throws Exception {
        int databaseSizeBeforeCreate = deliberationRepository.findAll().size();
        // Create the Deliberation
        restDeliberationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliberation)))
            .andExpect(status().isCreated());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeCreate + 1);
        Deliberation testDeliberation = deliberationList.get(deliberationList.size() - 1);
        assertThat(testDeliberation.getCritereSelection()).isEqualTo(DEFAULT_CRITERE_SELECTION);
        assertThat(testDeliberation.getValeurSelectionDebut()).isEqualTo(DEFAULT_VALEUR_SELECTION_DEBUT);
        assertThat(testDeliberation.getValeurSelectionFin()).isEqualTo(DEFAULT_VALEUR_SELECTION_FIN);
        assertThat(testDeliberation.getCritereAppliquer()).isEqualTo(DEFAULT_CRITERE_APPLIQUER);
        assertThat(testDeliberation.getValeurAppliquer()).isEqualTo(DEFAULT_VALEUR_APPLIQUER);
        assertThat(testDeliberation.getDateDeliberation()).isEqualTo(DEFAULT_DATE_DELIBERATION);
    }

    @Test
    @Transactional
    void createDeliberationWithExistingId() throws Exception {
        // Create the Deliberation with an existing ID
        deliberation.setId(1L);

        int databaseSizeBeforeCreate = deliberationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeliberationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliberation)))
            .andExpect(status().isBadRequest());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCritereSelectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliberationRepository.findAll().size();
        // set the field null
        deliberation.setCritereSelection(null);

        // Create the Deliberation, which fails.

        restDeliberationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliberation)))
            .andExpect(status().isBadRequest());

        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValeurSelectionDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliberationRepository.findAll().size();
        // set the field null
        deliberation.setValeurSelectionDebut(null);

        // Create the Deliberation, which fails.

        restDeliberationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliberation)))
            .andExpect(status().isBadRequest());

        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCritereAppliquerIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliberationRepository.findAll().size();
        // set the field null
        deliberation.setCritereAppliquer(null);

        // Create the Deliberation, which fails.

        restDeliberationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliberation)))
            .andExpect(status().isBadRequest());

        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValeurAppliquerIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliberationRepository.findAll().size();
        // set the field null
        deliberation.setValeurAppliquer(null);

        // Create the Deliberation, which fails.

        restDeliberationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliberation)))
            .andExpect(status().isBadRequest());

        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeliberations() throws Exception {
        // Initialize the database
        deliberationRepository.saveAndFlush(deliberation);

        // Get all the deliberationList
        restDeliberationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliberation.getId().intValue())))
            .andExpect(jsonPath("$.[*].critereSelection").value(hasItem(DEFAULT_CRITERE_SELECTION.toString())))
            .andExpect(jsonPath("$.[*].valeurSelectionDebut").value(hasItem(DEFAULT_VALEUR_SELECTION_DEBUT.doubleValue())))
            .andExpect(jsonPath("$.[*].valeurSelectionFin").value(hasItem(DEFAULT_VALEUR_SELECTION_FIN.doubleValue())))
            .andExpect(jsonPath("$.[*].critereAppliquer").value(hasItem(DEFAULT_CRITERE_APPLIQUER.toString())))
            .andExpect(jsonPath("$.[*].valeurAppliquer").value(hasItem(DEFAULT_VALEUR_APPLIQUER.doubleValue())))
            .andExpect(jsonPath("$.[*].dateDeliberation").value(hasItem(DEFAULT_DATE_DELIBERATION.toString())));
    }

    @Test
    @Transactional
    void getDeliberation() throws Exception {
        // Initialize the database
        deliberationRepository.saveAndFlush(deliberation);

        // Get the deliberation
        restDeliberationMockMvc
            .perform(get(ENTITY_API_URL_ID, deliberation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deliberation.getId().intValue()))
            .andExpect(jsonPath("$.critereSelection").value(DEFAULT_CRITERE_SELECTION.toString()))
            .andExpect(jsonPath("$.valeurSelectionDebut").value(DEFAULT_VALEUR_SELECTION_DEBUT.doubleValue()))
            .andExpect(jsonPath("$.valeurSelectionFin").value(DEFAULT_VALEUR_SELECTION_FIN.doubleValue()))
            .andExpect(jsonPath("$.critereAppliquer").value(DEFAULT_CRITERE_APPLIQUER.toString()))
            .andExpect(jsonPath("$.valeurAppliquer").value(DEFAULT_VALEUR_APPLIQUER.doubleValue()))
            .andExpect(jsonPath("$.dateDeliberation").value(DEFAULT_DATE_DELIBERATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDeliberation() throws Exception {
        // Get the deliberation
        restDeliberationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeliberation() throws Exception {
        // Initialize the database
        deliberationRepository.saveAndFlush(deliberation);

        int databaseSizeBeforeUpdate = deliberationRepository.findAll().size();

        // Update the deliberation
        Deliberation updatedDeliberation = deliberationRepository.findById(deliberation.getId()).get();
        // Disconnect from session so that the updates on updatedDeliberation are not directly saved in db
        em.detach(updatedDeliberation);
        updatedDeliberation
            .critereSelection(UPDATED_CRITERE_SELECTION)
            .valeurSelectionDebut(UPDATED_VALEUR_SELECTION_DEBUT)
            .valeurSelectionFin(UPDATED_VALEUR_SELECTION_FIN)
            .critereAppliquer(UPDATED_CRITERE_APPLIQUER)
            .valeurAppliquer(UPDATED_VALEUR_APPLIQUER)
            .dateDeliberation(UPDATED_DATE_DELIBERATION);

        restDeliberationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDeliberation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDeliberation))
            )
            .andExpect(status().isOk());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeUpdate);
        Deliberation testDeliberation = deliberationList.get(deliberationList.size() - 1);
        assertThat(testDeliberation.getCritereSelection()).isEqualTo(UPDATED_CRITERE_SELECTION);
        assertThat(testDeliberation.getValeurSelectionDebut()).isEqualTo(UPDATED_VALEUR_SELECTION_DEBUT);
        assertThat(testDeliberation.getValeurSelectionFin()).isEqualTo(UPDATED_VALEUR_SELECTION_FIN);
        assertThat(testDeliberation.getCritereAppliquer()).isEqualTo(UPDATED_CRITERE_APPLIQUER);
        assertThat(testDeliberation.getValeurAppliquer()).isEqualTo(UPDATED_VALEUR_APPLIQUER);
        assertThat(testDeliberation.getDateDeliberation()).isEqualTo(UPDATED_DATE_DELIBERATION);
    }

    @Test
    @Transactional
    void putNonExistingDeliberation() throws Exception {
        int databaseSizeBeforeUpdate = deliberationRepository.findAll().size();
        deliberation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliberationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliberation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliberation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeliberation() throws Exception {
        int databaseSizeBeforeUpdate = deliberationRepository.findAll().size();
        deliberation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliberationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliberation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeliberation() throws Exception {
        int databaseSizeBeforeUpdate = deliberationRepository.findAll().size();
        deliberation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliberationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliberation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeliberationWithPatch() throws Exception {
        // Initialize the database
        deliberationRepository.saveAndFlush(deliberation);

        int databaseSizeBeforeUpdate = deliberationRepository.findAll().size();

        // Update the deliberation using partial update
        Deliberation partialUpdatedDeliberation = new Deliberation();
        partialUpdatedDeliberation.setId(deliberation.getId());

        partialUpdatedDeliberation.valeurAppliquer(UPDATED_VALEUR_APPLIQUER);

        restDeliberationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliberation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliberation))
            )
            .andExpect(status().isOk());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeUpdate);
        Deliberation testDeliberation = deliberationList.get(deliberationList.size() - 1);
        assertThat(testDeliberation.getCritereSelection()).isEqualTo(DEFAULT_CRITERE_SELECTION);
        assertThat(testDeliberation.getValeurSelectionDebut()).isEqualTo(DEFAULT_VALEUR_SELECTION_DEBUT);
        assertThat(testDeliberation.getValeurSelectionFin()).isEqualTo(DEFAULT_VALEUR_SELECTION_FIN);
        assertThat(testDeliberation.getCritereAppliquer()).isEqualTo(DEFAULT_CRITERE_APPLIQUER);
        assertThat(testDeliberation.getValeurAppliquer()).isEqualTo(UPDATED_VALEUR_APPLIQUER);
        assertThat(testDeliberation.getDateDeliberation()).isEqualTo(DEFAULT_DATE_DELIBERATION);
    }

    @Test
    @Transactional
    void fullUpdateDeliberationWithPatch() throws Exception {
        // Initialize the database
        deliberationRepository.saveAndFlush(deliberation);

        int databaseSizeBeforeUpdate = deliberationRepository.findAll().size();

        // Update the deliberation using partial update
        Deliberation partialUpdatedDeliberation = new Deliberation();
        partialUpdatedDeliberation.setId(deliberation.getId());

        partialUpdatedDeliberation
            .critereSelection(UPDATED_CRITERE_SELECTION)
            .valeurSelectionDebut(UPDATED_VALEUR_SELECTION_DEBUT)
            .valeurSelectionFin(UPDATED_VALEUR_SELECTION_FIN)
            .critereAppliquer(UPDATED_CRITERE_APPLIQUER)
            .valeurAppliquer(UPDATED_VALEUR_APPLIQUER)
            .dateDeliberation(UPDATED_DATE_DELIBERATION);

        restDeliberationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliberation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliberation))
            )
            .andExpect(status().isOk());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeUpdate);
        Deliberation testDeliberation = deliberationList.get(deliberationList.size() - 1);
        assertThat(testDeliberation.getCritereSelection()).isEqualTo(UPDATED_CRITERE_SELECTION);
        assertThat(testDeliberation.getValeurSelectionDebut()).isEqualTo(UPDATED_VALEUR_SELECTION_DEBUT);
        assertThat(testDeliberation.getValeurSelectionFin()).isEqualTo(UPDATED_VALEUR_SELECTION_FIN);
        assertThat(testDeliberation.getCritereAppliquer()).isEqualTo(UPDATED_CRITERE_APPLIQUER);
        assertThat(testDeliberation.getValeurAppliquer()).isEqualTo(UPDATED_VALEUR_APPLIQUER);
        assertThat(testDeliberation.getDateDeliberation()).isEqualTo(UPDATED_DATE_DELIBERATION);
    }

    @Test
    @Transactional
    void patchNonExistingDeliberation() throws Exception {
        int databaseSizeBeforeUpdate = deliberationRepository.findAll().size();
        deliberation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliberationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deliberation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliberation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeliberation() throws Exception {
        int databaseSizeBeforeUpdate = deliberationRepository.findAll().size();
        deliberation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliberationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliberation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeliberation() throws Exception {
        int databaseSizeBeforeUpdate = deliberationRepository.findAll().size();
        deliberation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliberationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(deliberation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeliberation() throws Exception {
        // Initialize the database
        deliberationRepository.saveAndFlush(deliberation);

        int databaseSizeBeforeDelete = deliberationRepository.findAll().size();

        // Delete the deliberation
        restDeliberationMockMvc
            .perform(delete(ENTITY_API_URL_ID, deliberation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
