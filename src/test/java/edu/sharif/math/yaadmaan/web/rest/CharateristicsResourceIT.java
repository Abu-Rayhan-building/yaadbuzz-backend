package edu.sharif.math.yaadmaan.web.rest;

import edu.sharif.math.yaadmaan.YaadmaanApp;
import edu.sharif.math.yaadmaan.domain.Charateristics;
import edu.sharif.math.yaadmaan.domain.UserPerDepartment;
import edu.sharif.math.yaadmaan.repository.CharateristicsRepository;
import edu.sharif.math.yaadmaan.service.CharateristicsService;
import edu.sharif.math.yaadmaan.service.dto.CharateristicsDTO;
import edu.sharif.math.yaadmaan.service.mapper.CharateristicsMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CharateristicsResource} REST controller.
 */
@SpringBootTest(classes = YaadmaanApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CharateristicsResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_REPETATION = 1;
    private static final Integer UPDATED_REPETATION = 2;

    @Autowired
    private CharateristicsRepository charateristicsRepository;

    @Autowired
    private CharateristicsMapper charateristicsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCharateristicsMockMvc;

    private Charateristics charateristics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Charateristics createEntity(EntityManager em) {
        Charateristics charateristics = new Charateristics()
            .title(DEFAULT_TITLE)
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
        charateristics.setUserPerDepartment(userPerDepartment);
        return charateristics;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Charateristics createUpdatedEntity(EntityManager em) {
        Charateristics charateristics = new Charateristics()
            .title(UPDATED_TITLE)
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
        charateristics.setUserPerDepartment(userPerDepartment);
        return charateristics;
    }

    @BeforeEach
    public void initTest() {
        charateristics = createEntity(em);
    }

    @Test
    @Transactional
    public void createCharateristics() throws Exception {
        int databaseSizeBeforeCreate = charateristicsRepository.findAll().size();
        // Create the Charateristics
        CharateristicsDTO charateristicsDTO = charateristicsMapper.toDto(charateristics);
        restCharateristicsMockMvc.perform(post("/api/charateristics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(charateristicsDTO)))
            .andExpect(status().isCreated());

        // Validate the Charateristics in the database
        List<Charateristics> charateristicsList = charateristicsRepository.findAll();
        assertThat(charateristicsList).hasSize(databaseSizeBeforeCreate + 1);
        Charateristics testCharateristics = charateristicsList.get(charateristicsList.size() - 1);
        assertThat(testCharateristics.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCharateristics.getRepetation()).isEqualTo(DEFAULT_REPETATION);
    }

    @Test
    @Transactional
    public void createCharateristicsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = charateristicsRepository.findAll().size();

        // Create the Charateristics with an existing ID
        charateristics.setId(1L);
        CharateristicsDTO charateristicsDTO = charateristicsMapper.toDto(charateristics);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCharateristicsMockMvc.perform(post("/api/charateristics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(charateristicsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Charateristics in the database
        List<Charateristics> charateristicsList = charateristicsRepository.findAll();
        assertThat(charateristicsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = charateristicsRepository.findAll().size();
        // set the field null
        charateristics.setTitle(null);

        // Create the Charateristics, which fails.
        CharateristicsDTO charateristicsDTO = charateristicsMapper.toDto(charateristics);


        restCharateristicsMockMvc.perform(post("/api/charateristics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(charateristicsDTO)))
            .andExpect(status().isBadRequest());

        List<Charateristics> charateristicsList = charateristicsRepository.findAll();
        assertThat(charateristicsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCharateristics() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        // Get all the charateristicsList
        restCharateristicsMockMvc.perform(get("/api/charateristics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(charateristics.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].repetation").value(hasItem(DEFAULT_REPETATION)));
    }
    
    @Test
    @Transactional
    public void getCharateristics() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        // Get the charateristics
        restCharateristicsMockMvc.perform(get("/api/charateristics/{id}", charateristics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(charateristics.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.repetation").value(DEFAULT_REPETATION));
    }
    @Test
    @Transactional
    public void getNonExistingCharateristics() throws Exception {
        // Get the charateristics
        restCharateristicsMockMvc.perform(get("/api/charateristics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCharateristics() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        int databaseSizeBeforeUpdate = charateristicsRepository.findAll().size();

        // Update the charateristics
        Charateristics updatedCharateristics = charateristicsRepository.findById(charateristics.getId()).get();
        // Disconnect from session so that the updates on updatedCharateristics are not directly saved in db
        em.detach(updatedCharateristics);
        updatedCharateristics
            .title(UPDATED_TITLE)
            .repetation(UPDATED_REPETATION);
        CharateristicsDTO charateristicsDTO = charateristicsMapper.toDto(updatedCharateristics);

        restCharateristicsMockMvc.perform(put("/api/charateristics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(charateristicsDTO)))
            .andExpect(status().isOk());

        // Validate the Charateristics in the database
        List<Charateristics> charateristicsList = charateristicsRepository.findAll();
        assertThat(charateristicsList).hasSize(databaseSizeBeforeUpdate);
        Charateristics testCharateristics = charateristicsList.get(charateristicsList.size() - 1);
        assertThat(testCharateristics.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCharateristics.getRepetation()).isEqualTo(UPDATED_REPETATION);
    }

    @Test
    @Transactional
    public void updateNonExistingCharateristics() throws Exception {
        int databaseSizeBeforeUpdate = charateristicsRepository.findAll().size();

        // Create the Charateristics
        CharateristicsDTO charateristicsDTO = charateristicsMapper.toDto(charateristics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharateristicsMockMvc.perform(put("/api/charateristics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(charateristicsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Charateristics in the database
        List<Charateristics> charateristicsList = charateristicsRepository.findAll();
        assertThat(charateristicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCharateristics() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        int databaseSizeBeforeDelete = charateristicsRepository.findAll().size();

        // Delete the charateristics
        restCharateristicsMockMvc.perform(delete("/api/charateristics/{id}", charateristics.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Charateristics> charateristicsList = charateristicsRepository.findAll();
        assertThat(charateristicsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
