package com.urservices.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.urservices.IntegrationTest;
import com.urservices.domain.Filiere;
import com.urservices.domain.Module;
import com.urservices.repository.ModuleRepository;
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
 * Integration tests for the {@link ModuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ModuleResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CREDIT = 1;
    private static final Integer UPDATED_CREDIT = 2;

    private static final String ENTITY_API_URL = "/api/modules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restModuleMockMvc;

    private Module module;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Module createEntity(EntityManager em) {
        Module module = new Module().libelle(DEFAULT_LIBELLE).code(DEFAULT_CODE).credit(DEFAULT_CREDIT);
        // Add required entity
        Filiere filiere;
        if (TestUtil.findAll(em, Filiere.class).isEmpty()) {
            filiere = FiliereResourceIT.createEntity(em);
            em.persist(filiere);
            em.flush();
        } else {
            filiere = TestUtil.findAll(em, Filiere.class).get(0);
        }
        module.setFiliere(filiere);
        return module;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Module createUpdatedEntity(EntityManager em) {
        Module module = new Module().libelle(UPDATED_LIBELLE).code(UPDATED_CODE).credit(UPDATED_CREDIT);
        // Add required entity
        Filiere filiere;
        if (TestUtil.findAll(em, Filiere.class).isEmpty()) {
            filiere = FiliereResourceIT.createUpdatedEntity(em);
            em.persist(filiere);
            em.flush();
        } else {
            filiere = TestUtil.findAll(em, Filiere.class).get(0);
        }
        module.setFiliere(filiere);
        return module;
    }

    @BeforeEach
    public void initTest() {
        module = createEntity(em);
    }

    @Test
    @Transactional
    void createModule() throws Exception {
        int databaseSizeBeforeCreate = moduleRepository.findAll().size();
        // Create the Module
        restModuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(module)))
            .andExpect(status().isCreated());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeCreate + 1);
        Module testModule = moduleList.get(moduleList.size() - 1);
        assertThat(testModule.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testModule.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testModule.getCredit()).isEqualTo(DEFAULT_CREDIT);
    }

    @Test
    @Transactional
    void createModuleWithExistingId() throws Exception {
        // Create the Module with an existing ID
        module.setId(1L);

        int databaseSizeBeforeCreate = moduleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restModuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(module)))
            .andExpect(status().isBadRequest());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = moduleRepository.findAll().size();
        // set the field null
        module.setLibelle(null);

        // Create the Module, which fails.

        restModuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(module)))
            .andExpect(status().isBadRequest());

        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = moduleRepository.findAll().size();
        // set the field null
        module.setCode(null);

        // Create the Module, which fails.

        restModuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(module)))
            .andExpect(status().isBadRequest());

        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllModules() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get all the moduleList
        restModuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(module.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT)));
    }

    @Test
    @Transactional
    void getModule() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get the module
        restModuleMockMvc
            .perform(get(ENTITY_API_URL_ID, module.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(module.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.credit").value(DEFAULT_CREDIT));
    }

    @Test
    @Transactional
    void getNonExistingModule() throws Exception {
        // Get the module
        restModuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewModule() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();

        // Update the module
        Module updatedModule = moduleRepository.findById(module.getId()).get();
        // Disconnect from session so that the updates on updatedModule are not directly saved in db
        em.detach(updatedModule);
        updatedModule.libelle(UPDATED_LIBELLE).code(UPDATED_CODE).credit(UPDATED_CREDIT);

        restModuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedModule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedModule))
            )
            .andExpect(status().isOk());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
        Module testModule = moduleList.get(moduleList.size() - 1);
        assertThat(testModule.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testModule.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testModule.getCredit()).isEqualTo(UPDATED_CREDIT);
    }

    @Test
    @Transactional
    void putNonExistingModule() throws Exception {
        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();
        module.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, module.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(module))
            )
            .andExpect(status().isBadRequest());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchModule() throws Exception {
        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();
        module.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(module))
            )
            .andExpect(status().isBadRequest());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamModule() throws Exception {
        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();
        module.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModuleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(module)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateModuleWithPatch() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();

        // Update the module using partial update
        Module partialUpdatedModule = new Module();
        partialUpdatedModule.setId(module.getId());

        restModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedModule))
            )
            .andExpect(status().isOk());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
        Module testModule = moduleList.get(moduleList.size() - 1);
        assertThat(testModule.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testModule.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testModule.getCredit()).isEqualTo(DEFAULT_CREDIT);
    }

    @Test
    @Transactional
    void fullUpdateModuleWithPatch() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();

        // Update the module using partial update
        Module partialUpdatedModule = new Module();
        partialUpdatedModule.setId(module.getId());

        partialUpdatedModule.libelle(UPDATED_LIBELLE).code(UPDATED_CODE).credit(UPDATED_CREDIT);

        restModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedModule))
            )
            .andExpect(status().isOk());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
        Module testModule = moduleList.get(moduleList.size() - 1);
        assertThat(testModule.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testModule.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testModule.getCredit()).isEqualTo(UPDATED_CREDIT);
    }

    @Test
    @Transactional
    void patchNonExistingModule() throws Exception {
        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();
        module.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, module.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(module))
            )
            .andExpect(status().isBadRequest());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchModule() throws Exception {
        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();
        module.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(module))
            )
            .andExpect(status().isBadRequest());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamModule() throws Exception {
        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();
        module.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModuleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(module)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteModule() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        int databaseSizeBeforeDelete = moduleRepository.findAll().size();

        // Delete the module
        restModuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, module.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
