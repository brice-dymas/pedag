package com.urservices.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.urservices.IntegrationTest;
import com.urservices.domain.Courriel;
import com.urservices.repository.CourrielRepository;
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
 * Integration tests for the {@link CourrielResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourrielResourceIT {

    private static final String DEFAULT_SENDER = "AAAAAAAAAA";
    private static final String UPDATED_SENDER = "BBBBBBBBBB";

    private static final String DEFAULT_RECEIVER = "AAAAAAAAAA";
    private static final String UPDATED_RECEIVER = "BBBBBBBBBB";

    private static final String DEFAULT_OBJET = "AAAAAAAAAA";
    private static final String UPDATED_OBJET = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/courriels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CourrielRepository courrielRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourrielMockMvc;

    private Courriel courriel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Courriel createEntity(EntityManager em) {
        Courriel courriel = new Courriel()
            .sender(DEFAULT_SENDER)
            .receiver(DEFAULT_RECEIVER)
            .objet(DEFAULT_OBJET)
            .message(DEFAULT_MESSAGE)
            .dateCreation(DEFAULT_DATE_CREATION);
        return courriel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Courriel createUpdatedEntity(EntityManager em) {
        Courriel courriel = new Courriel()
            .sender(UPDATED_SENDER)
            .receiver(UPDATED_RECEIVER)
            .objet(UPDATED_OBJET)
            .message(UPDATED_MESSAGE)
            .dateCreation(UPDATED_DATE_CREATION);
        return courriel;
    }

    @BeforeEach
    public void initTest() {
        courriel = createEntity(em);
    }

    @Test
    @Transactional
    void createCourriel() throws Exception {
        int databaseSizeBeforeCreate = courrielRepository.findAll().size();
        // Create the Courriel
        restCourrielMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courriel)))
            .andExpect(status().isCreated());

        // Validate the Courriel in the database
        List<Courriel> courrielList = courrielRepository.findAll();
        assertThat(courrielList).hasSize(databaseSizeBeforeCreate + 1);
        Courriel testCourriel = courrielList.get(courrielList.size() - 1);
        assertThat(testCourriel.getSender()).isEqualTo(DEFAULT_SENDER);
        assertThat(testCourriel.getReceiver()).isEqualTo(DEFAULT_RECEIVER);
        assertThat(testCourriel.getObjet()).isEqualTo(DEFAULT_OBJET);
        assertThat(testCourriel.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testCourriel.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
    }

    @Test
    @Transactional
    void createCourrielWithExistingId() throws Exception {
        // Create the Courriel with an existing ID
        courriel.setId(1L);

        int databaseSizeBeforeCreate = courrielRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourrielMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courriel)))
            .andExpect(status().isBadRequest());

        // Validate the Courriel in the database
        List<Courriel> courrielList = courrielRepository.findAll();
        assertThat(courrielList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = courrielRepository.findAll().size();
        // set the field null
        courriel.setSender(null);

        // Create the Courriel, which fails.

        restCourrielMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courriel)))
            .andExpect(status().isBadRequest());

        List<Courriel> courrielList = courrielRepository.findAll();
        assertThat(courrielList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReceiverIsRequired() throws Exception {
        int databaseSizeBeforeTest = courrielRepository.findAll().size();
        // set the field null
        courriel.setReceiver(null);

        // Create the Courriel, which fails.

        restCourrielMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courriel)))
            .andExpect(status().isBadRequest());

        List<Courriel> courrielList = courrielRepository.findAll();
        assertThat(courrielList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkObjetIsRequired() throws Exception {
        int databaseSizeBeforeTest = courrielRepository.findAll().size();
        // set the field null
        courriel.setObjet(null);

        // Create the Courriel, which fails.

        restCourrielMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courriel)))
            .andExpect(status().isBadRequest());

        List<Courriel> courrielList = courrielRepository.findAll();
        assertThat(courrielList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = courrielRepository.findAll().size();
        // set the field null
        courriel.setMessage(null);

        // Create the Courriel, which fails.

        restCourrielMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courriel)))
            .andExpect(status().isBadRequest());

        List<Courriel> courrielList = courrielRepository.findAll();
        assertThat(courrielList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCourriels() throws Exception {
        // Initialize the database
        courrielRepository.saveAndFlush(courriel);

        // Get all the courrielList
        restCourrielMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courriel.getId().intValue())))
            .andExpect(jsonPath("$.[*].sender").value(hasItem(DEFAULT_SENDER)))
            .andExpect(jsonPath("$.[*].receiver").value(hasItem(DEFAULT_RECEIVER)))
            .andExpect(jsonPath("$.[*].objet").value(hasItem(DEFAULT_OBJET)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())));
    }

    @Test
    @Transactional
    void getCourriel() throws Exception {
        // Initialize the database
        courrielRepository.saveAndFlush(courriel);

        // Get the courriel
        restCourrielMockMvc
            .perform(get(ENTITY_API_URL_ID, courriel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courriel.getId().intValue()))
            .andExpect(jsonPath("$.sender").value(DEFAULT_SENDER))
            .andExpect(jsonPath("$.receiver").value(DEFAULT_RECEIVER))
            .andExpect(jsonPath("$.objet").value(DEFAULT_OBJET))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCourriel() throws Exception {
        // Get the courriel
        restCourrielMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCourriel() throws Exception {
        // Initialize the database
        courrielRepository.saveAndFlush(courriel);

        int databaseSizeBeforeUpdate = courrielRepository.findAll().size();

        // Update the courriel
        Courriel updatedCourriel = courrielRepository.findById(courriel.getId()).get();
        // Disconnect from session so that the updates on updatedCourriel are not directly saved in db
        em.detach(updatedCourriel);
        updatedCourriel
            .sender(UPDATED_SENDER)
            .receiver(UPDATED_RECEIVER)
            .objet(UPDATED_OBJET)
            .message(UPDATED_MESSAGE)
            .dateCreation(UPDATED_DATE_CREATION);

        restCourrielMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCourriel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCourriel))
            )
            .andExpect(status().isOk());

        // Validate the Courriel in the database
        List<Courriel> courrielList = courrielRepository.findAll();
        assertThat(courrielList).hasSize(databaseSizeBeforeUpdate);
        Courriel testCourriel = courrielList.get(courrielList.size() - 1);
        assertThat(testCourriel.getSender()).isEqualTo(UPDATED_SENDER);
        assertThat(testCourriel.getReceiver()).isEqualTo(UPDATED_RECEIVER);
        assertThat(testCourriel.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testCourriel.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testCourriel.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void putNonExistingCourriel() throws Exception {
        int databaseSizeBeforeUpdate = courrielRepository.findAll().size();
        courriel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourrielMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courriel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courriel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Courriel in the database
        List<Courriel> courrielList = courrielRepository.findAll();
        assertThat(courrielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourriel() throws Exception {
        int databaseSizeBeforeUpdate = courrielRepository.findAll().size();
        courriel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourrielMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courriel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Courriel in the database
        List<Courriel> courrielList = courrielRepository.findAll();
        assertThat(courrielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourriel() throws Exception {
        int databaseSizeBeforeUpdate = courrielRepository.findAll().size();
        courriel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourrielMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courriel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Courriel in the database
        List<Courriel> courrielList = courrielRepository.findAll();
        assertThat(courrielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourrielWithPatch() throws Exception {
        // Initialize the database
        courrielRepository.saveAndFlush(courriel);

        int databaseSizeBeforeUpdate = courrielRepository.findAll().size();

        // Update the courriel using partial update
        Courriel partialUpdatedCourriel = new Courriel();
        partialUpdatedCourriel.setId(courriel.getId());

        partialUpdatedCourriel.receiver(UPDATED_RECEIVER).objet(UPDATED_OBJET).dateCreation(UPDATED_DATE_CREATION);

        restCourrielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourriel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourriel))
            )
            .andExpect(status().isOk());

        // Validate the Courriel in the database
        List<Courriel> courrielList = courrielRepository.findAll();
        assertThat(courrielList).hasSize(databaseSizeBeforeUpdate);
        Courriel testCourriel = courrielList.get(courrielList.size() - 1);
        assertThat(testCourriel.getSender()).isEqualTo(DEFAULT_SENDER);
        assertThat(testCourriel.getReceiver()).isEqualTo(UPDATED_RECEIVER);
        assertThat(testCourriel.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testCourriel.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testCourriel.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void fullUpdateCourrielWithPatch() throws Exception {
        // Initialize the database
        courrielRepository.saveAndFlush(courriel);

        int databaseSizeBeforeUpdate = courrielRepository.findAll().size();

        // Update the courriel using partial update
        Courriel partialUpdatedCourriel = new Courriel();
        partialUpdatedCourriel.setId(courriel.getId());

        partialUpdatedCourriel
            .sender(UPDATED_SENDER)
            .receiver(UPDATED_RECEIVER)
            .objet(UPDATED_OBJET)
            .message(UPDATED_MESSAGE)
            .dateCreation(UPDATED_DATE_CREATION);

        restCourrielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourriel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourriel))
            )
            .andExpect(status().isOk());

        // Validate the Courriel in the database
        List<Courriel> courrielList = courrielRepository.findAll();
        assertThat(courrielList).hasSize(databaseSizeBeforeUpdate);
        Courriel testCourriel = courrielList.get(courrielList.size() - 1);
        assertThat(testCourriel.getSender()).isEqualTo(UPDATED_SENDER);
        assertThat(testCourriel.getReceiver()).isEqualTo(UPDATED_RECEIVER);
        assertThat(testCourriel.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testCourriel.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testCourriel.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void patchNonExistingCourriel() throws Exception {
        int databaseSizeBeforeUpdate = courrielRepository.findAll().size();
        courriel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourrielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courriel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courriel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Courriel in the database
        List<Courriel> courrielList = courrielRepository.findAll();
        assertThat(courrielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourriel() throws Exception {
        int databaseSizeBeforeUpdate = courrielRepository.findAll().size();
        courriel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourrielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courriel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Courriel in the database
        List<Courriel> courrielList = courrielRepository.findAll();
        assertThat(courrielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourriel() throws Exception {
        int databaseSizeBeforeUpdate = courrielRepository.findAll().size();
        courriel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourrielMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(courriel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Courriel in the database
        List<Courriel> courrielList = courrielRepository.findAll();
        assertThat(courrielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourriel() throws Exception {
        // Initialize the database
        courrielRepository.saveAndFlush(courriel);

        int databaseSizeBeforeDelete = courrielRepository.findAll().size();

        // Delete the courriel
        restCourrielMockMvc
            .perform(delete(ENTITY_API_URL_ID, courriel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Courriel> courrielList = courrielRepository.findAll();
        assertThat(courrielList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
