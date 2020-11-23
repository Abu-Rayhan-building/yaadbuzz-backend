package edu.sharif.math.yaadmaan.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadmaan.YaadmaanApp;
import edu.sharif.math.yaadmaan.domain.Charateristics;
import edu.sharif.math.yaadmaan.domain.CharateristicsRepetation;
import edu.sharif.math.yaadmaan.domain.UserPerDepartment;
import edu.sharif.math.yaadmaan.repository.CharateristicsRepetationRepository;
import edu.sharif.math.yaadmaan.service.CharateristicsRepetationService;
import edu.sharif.math.yaadmaan.service.dto.CharateristicsRepetationDTO;
import edu.sharif.math.yaadmaan.service.mapper.CharateristicsRepetationMapper;
import edu.sharif.math.yaadmaan.web.rest.CharateristicsRepetationResource;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CharateristicsRepetationResource} REST controller.
 */
@SpringBootTest(classes = YaadmaanApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CharateristicsRepetationResourceIT {

    private static final Integer DEFAULT_REPETATION = 1;
    private static final Integer UPDATED_REPETATION = 2;

    @Autowired
    private CharateristicsRepetationRepository charateristicsRepetationRepository;

    @Autowired
    private CharateristicsRepetationMapper charateristicsRepetationMapper;

    @Autowired
    private CharateristicsRepetationService charateristicsRepetationService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCharateristicsRepetationMockMvc;

    private CharateristicsRepetation charateristicsRepetation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CharateristicsRepetation createEntity(EntityManager em) {
        CharateristicsRepetation charateristicsRepetation = new CharateristicsRepetation()
            .repetation(DEFAULT_REPETATION);
        // Add required entity
        UserPerDepartment userPerDepartment;
        if (TestUtil.findAll(em, UserPerDepartment.class).isEmpty()) {
            userPerDepartment = UserPerDepartmentResourceIT.createEntity(em);
            em.persist(userPerDepartment);
            em.flush();
        } else {
            userPerDepartment = TestUtil.findAll(em, UserPerDepartment.class).get(0);
        }
        charateristicsRepetation.setUser(userPerDepartment);
        // Add required entity
        Charateristics charateristics;
        if (TestUtil.findAll(em, Charateristics.class).isEmpty()) {
            charateristics = CharateristicsResourceIT.createEntity(em);
            em.persist(charateristics);
            em.flush();
        } else {
            charateristics = TestUtil.findAll(em, Charateristics.class).get(0);
        }
        charateristicsRepetation.setCharactristic(charateristics);
        return charateristicsRepetation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CharateristicsRepetation createUpdatedEntity(EntityManager em) {
        CharateristicsRepetation charateristicsRepetation = new CharateristicsRepetation()
            .repetation(UPDATED_REPETATION);
        // Add required entity
        UserPerDepartment userPerDepartment;
        if (TestUtil.findAll(em, UserPerDepartment.class).isEmpty()) {
            userPerDepartment = UserPerDepartmentResourceIT.createUpdatedEntity(em);
            em.persist(userPerDepartment);
            em.flush();
        } else {
            userPerDepartment = TestUtil.findAll(em, UserPerDepartment.class).get(0);
        }
        charateristicsRepetation.setUser(userPerDepartment);
        // Add required entity
        Charateristics charateristics;
        if (TestUtil.findAll(em, Charateristics.class).isEmpty()) {
            charateristics = CharateristicsResourceIT.createUpdatedEntity(em);
            em.persist(charateristics);
            em.flush();
        } else {
            charateristics = TestUtil.findAll(em, Charateristics.class).get(0);
        }
        charateristicsRepetation.setCharactristic(charateristics);
        return charateristicsRepetation;
    }

    @BeforeEach
    public void initTest() {
        charateristicsRepetation = createEntity(em);
    }

    @Test
    @Transactional
    public void createCharateristicsRepetation() throws Exception {
        int databaseSizeBeforeCreate = charateristicsRepetationRepository.findAll().size();
        // Create the CharateristicsRepetation
        CharateristicsRepetationDTO charateristicsRepetationDTO = charateristicsRepetationMapper.toDto(charateristicsRepetation);
        restCharateristicsRepetationMockMvc.perform(post("/api/charateristics-repetations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(charateristicsRepetationDTO)))
            .andExpect(status().isCreated());

        // Validate the CharateristicsRepetation in the database
        List<CharateristicsRepetation> charateristicsRepetationList = charateristicsRepetationRepository.findAll();
        assertThat(charateristicsRepetationList).hasSize(databaseSizeBeforeCreate + 1);
        CharateristicsRepetation testCharateristicsRepetation = charateristicsRepetationList.get(charateristicsRepetationList.size() - 1);
        assertThat(testCharateristicsRepetation.getRepetation()).isEqualTo(DEFAULT_REPETATION);
    }

    @Test
    @Transactional
    public void createCharateristicsRepetationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = charateristicsRepetationRepository.findAll().size();

        // Create the CharateristicsRepetation with an existing ID
        charateristicsRepetation.setId(1L);
        CharateristicsRepetationDTO charateristicsRepetationDTO = charateristicsRepetationMapper.toDto(charateristicsRepetation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCharateristicsRepetationMockMvc.perform(post("/api/charateristics-repetations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(charateristicsRepetationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CharateristicsRepetation in the database
        List<CharateristicsRepetation> charateristicsRepetationList = charateristicsRepetationRepository.findAll();
        assertThat(charateristicsRepetationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCharateristicsRepetations() throws Exception {
        // Initialize the database
        charateristicsRepetationRepository.saveAndFlush(charateristicsRepetation);

        // Get all the charateristicsRepetationList
        restCharateristicsRepetationMockMvc.perform(get("/api/charateristics-repetations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(charateristicsRepetation.getId().intValue())))
            .andExpect(jsonPath("$.[*].repetation").value(hasItem(DEFAULT_REPETATION)));
    }
    
    @Test
    @Transactional
    public void getCharateristicsRepetation() throws Exception {
        // Initialize the database
        charateristicsRepetationRepository.saveAndFlush(charateristicsRepetation);

        // Get the charateristicsRepetation
        restCharateristicsRepetationMockMvc.perform(get("/api/charateristics-repetations/{id}", charateristicsRepetation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(charateristicsRepetation.getId().intValue()))
            .andExpect(jsonPath("$.repetation").value(DEFAULT_REPETATION));
    }
    @Test
    @Transactional
    public void getNonExistingCharateristicsRepetation() throws Exception {
        // Get the charateristicsRepetation
        restCharateristicsRepetationMockMvc.perform(get("/api/charateristics-repetations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCharateristicsRepetation() throws Exception {
        // Initialize the database
        charateristicsRepetationRepository.saveAndFlush(charateristicsRepetation);

        int databaseSizeBeforeUpdate = charateristicsRepetationRepository.findAll().size();

        // Update the charateristicsRepetation
        CharateristicsRepetation updatedCharateristicsRepetation = charateristicsRepetationRepository.findById(charateristicsRepetation.getId()).get();
        // Disconnect from session so that the updates on updatedCharateristicsRepetation are not directly saved in db
        em.detach(updatedCharateristicsRepetation);
        updatedCharateristicsRepetation
            .repetation(UPDATED_REPETATION);
        CharateristicsRepetationDTO charateristicsRepetationDTO = charateristicsRepetationMapper.toDto(updatedCharateristicsRepetation);

        restCharateristicsRepetationMockMvc.perform(put("/api/charateristics-repetations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(charateristicsRepetationDTO)))
            .andExpect(status().isOk());

        // Validate the CharateristicsRepetation in the database
        List<CharateristicsRepetation> charateristicsRepetationList = charateristicsRepetationRepository.findAll();
        assertThat(charateristicsRepetationList).hasSize(databaseSizeBeforeUpdate);
        CharateristicsRepetation testCharateristicsRepetation = charateristicsRepetationList.get(charateristicsRepetationList.size() - 1);
        assertThat(testCharateristicsRepetation.getRepetation()).isEqualTo(UPDATED_REPETATION);
    }

    @Test
    @Transactional
    public void updateNonExistingCharateristicsRepetation() throws Exception {
        int databaseSizeBeforeUpdate = charateristicsRepetationRepository.findAll().size();

        // Create the CharateristicsRepetation
        CharateristicsRepetationDTO charateristicsRepetationDTO = charateristicsRepetationMapper.toDto(charateristicsRepetation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharateristicsRepetationMockMvc.perform(put("/api/charateristics-repetations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(charateristicsRepetationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CharateristicsRepetation in the database
        List<CharateristicsRepetation> charateristicsRepetationList = charateristicsRepetationRepository.findAll();
        assertThat(charateristicsRepetationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCharateristicsRepetation() throws Exception {
        // Initialize the database
        charateristicsRepetationRepository.saveAndFlush(charateristicsRepetation);

        int databaseSizeBeforeDelete = charateristicsRepetationRepository.findAll().size();

        // Delete the charateristicsRepetation
        restCharateristicsRepetationMockMvc.perform(delete("/api/charateristics-repetations/{id}", charateristicsRepetation.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CharateristicsRepetation> charateristicsRepetationList = charateristicsRepetationRepository.findAll();
        assertThat(charateristicsRepetationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
