package edu.sharif.math.yaadbuzz.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.sharif.math.yaadbuzz.IntegrationTest;
import edu.sharif.math.yaadbuzz.domain.Charateristics;
import edu.sharif.math.yaadbuzz.domain.UserPerDepartment;
import edu.sharif.math.yaadbuzz.repository.CharateristicsRepository;
import edu.sharif.math.yaadbuzz.service.CharateristicsQueryService;
import edu.sharif.math.yaadbuzz.service.dto.CharateristicsCriteria;
import edu.sharif.math.yaadbuzz.service.dto.CharateristicsDTO;
import edu.sharif.math.yaadbuzz.service.mapper.CharateristicsMapper;
import java.util.List;
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
 * Integration tests for the {@link CharateristicsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CharateristicsResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_REPETATION = 1;
    private static final Integer UPDATED_REPETATION = 2;
    private static final Integer SMALLER_REPETATION = 1 - 1;

    @Autowired
    private CharateristicsRepository charateristicsRepository;

    @Autowired
    private CharateristicsMapper charateristicsMapper;

    @Autowired
    private CharateristicsQueryService charateristicsQueryService;

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
        Charateristics charateristics = new Charateristics().title(DEFAULT_TITLE).repetation(DEFAULT_REPETATION);
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
        Charateristics charateristics = new Charateristics().title(UPDATED_TITLE).repetation(UPDATED_REPETATION);
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
    void createCharateristics() throws Exception {
        int databaseSizeBeforeCreate = charateristicsRepository.findAll().size();
        // Create the Charateristics
        CharateristicsDTO charateristicsDTO = charateristicsMapper.toDto(charateristics);
        restCharateristicsMockMvc
            .perform(
                post("/api/charateristics")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(charateristicsDTO))
            )
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
    void createCharateristicsWithExistingId() throws Exception {
        // Create the Charateristics with an existing ID
        charateristics.setId(1L);
        CharateristicsDTO charateristicsDTO = charateristicsMapper.toDto(charateristics);

        int databaseSizeBeforeCreate = charateristicsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCharateristicsMockMvc
            .perform(
                post("/api/charateristics")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(charateristicsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Charateristics in the database
        List<Charateristics> charateristicsList = charateristicsRepository.findAll();
        assertThat(charateristicsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = charateristicsRepository.findAll().size();
        // set the field null
        charateristics.setTitle(null);

        // Create the Charateristics, which fails.
        CharateristicsDTO charateristicsDTO = charateristicsMapper.toDto(charateristics);

        restCharateristicsMockMvc
            .perform(
                post("/api/charateristics")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(charateristicsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Charateristics> charateristicsList = charateristicsRepository.findAll();
        assertThat(charateristicsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCharateristics() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        // Get all the charateristicsList
        restCharateristicsMockMvc
            .perform(get("/api/charateristics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(charateristics.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].repetation").value(hasItem(DEFAULT_REPETATION)));
    }

    @Test
    @Transactional
    void getCharateristics() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        // Get the charateristics
        restCharateristicsMockMvc
            .perform(get("/api/charateristics/{id}", charateristics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(charateristics.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.repetation").value(DEFAULT_REPETATION));
    }

    @Test
    @Transactional
    void getCharateristicsByIdFiltering() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        Long id = charateristics.getId();

        defaultCharateristicsShouldBeFound("id.equals=" + id);
        defaultCharateristicsShouldNotBeFound("id.notEquals=" + id);

        defaultCharateristicsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCharateristicsShouldNotBeFound("id.greaterThan=" + id);

        defaultCharateristicsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCharateristicsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCharateristicsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        // Get all the charateristicsList where title equals to DEFAULT_TITLE
        defaultCharateristicsShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the charateristicsList where title equals to UPDATED_TITLE
        defaultCharateristicsShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCharateristicsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        // Get all the charateristicsList where title not equals to DEFAULT_TITLE
        defaultCharateristicsShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the charateristicsList where title not equals to UPDATED_TITLE
        defaultCharateristicsShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCharateristicsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        // Get all the charateristicsList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultCharateristicsShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the charateristicsList where title equals to UPDATED_TITLE
        defaultCharateristicsShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCharateristicsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        // Get all the charateristicsList where title is not null
        defaultCharateristicsShouldBeFound("title.specified=true");

        // Get all the charateristicsList where title is null
        defaultCharateristicsShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllCharateristicsByTitleContainsSomething() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        // Get all the charateristicsList where title contains DEFAULT_TITLE
        defaultCharateristicsShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the charateristicsList where title contains UPDATED_TITLE
        defaultCharateristicsShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCharateristicsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        // Get all the charateristicsList where title does not contain DEFAULT_TITLE
        defaultCharateristicsShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the charateristicsList where title does not contain UPDATED_TITLE
        defaultCharateristicsShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCharateristicsByRepetationIsEqualToSomething() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        // Get all the charateristicsList where repetation equals to DEFAULT_REPETATION
        defaultCharateristicsShouldBeFound("repetation.equals=" + DEFAULT_REPETATION);

        // Get all the charateristicsList where repetation equals to UPDATED_REPETATION
        defaultCharateristicsShouldNotBeFound("repetation.equals=" + UPDATED_REPETATION);
    }

    @Test
    @Transactional
    void getAllCharateristicsByRepetationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        // Get all the charateristicsList where repetation not equals to DEFAULT_REPETATION
        defaultCharateristicsShouldNotBeFound("repetation.notEquals=" + DEFAULT_REPETATION);

        // Get all the charateristicsList where repetation not equals to UPDATED_REPETATION
        defaultCharateristicsShouldBeFound("repetation.notEquals=" + UPDATED_REPETATION);
    }

    @Test
    @Transactional
    void getAllCharateristicsByRepetationIsInShouldWork() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        // Get all the charateristicsList where repetation in DEFAULT_REPETATION or UPDATED_REPETATION
        defaultCharateristicsShouldBeFound("repetation.in=" + DEFAULT_REPETATION + "," + UPDATED_REPETATION);

        // Get all the charateristicsList where repetation equals to UPDATED_REPETATION
        defaultCharateristicsShouldNotBeFound("repetation.in=" + UPDATED_REPETATION);
    }

    @Test
    @Transactional
    void getAllCharateristicsByRepetationIsNullOrNotNull() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        // Get all the charateristicsList where repetation is not null
        defaultCharateristicsShouldBeFound("repetation.specified=true");

        // Get all the charateristicsList where repetation is null
        defaultCharateristicsShouldNotBeFound("repetation.specified=false");
    }

    @Test
    @Transactional
    void getAllCharateristicsByRepetationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        // Get all the charateristicsList where repetation is greater than or equal to DEFAULT_REPETATION
        defaultCharateristicsShouldBeFound("repetation.greaterThanOrEqual=" + DEFAULT_REPETATION);

        // Get all the charateristicsList where repetation is greater than or equal to UPDATED_REPETATION
        defaultCharateristicsShouldNotBeFound("repetation.greaterThanOrEqual=" + UPDATED_REPETATION);
    }

    @Test
    @Transactional
    void getAllCharateristicsByRepetationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        // Get all the charateristicsList where repetation is less than or equal to DEFAULT_REPETATION
        defaultCharateristicsShouldBeFound("repetation.lessThanOrEqual=" + DEFAULT_REPETATION);

        // Get all the charateristicsList where repetation is less than or equal to SMALLER_REPETATION
        defaultCharateristicsShouldNotBeFound("repetation.lessThanOrEqual=" + SMALLER_REPETATION);
    }

    @Test
    @Transactional
    void getAllCharateristicsByRepetationIsLessThanSomething() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        // Get all the charateristicsList where repetation is less than DEFAULT_REPETATION
        defaultCharateristicsShouldNotBeFound("repetation.lessThan=" + DEFAULT_REPETATION);

        // Get all the charateristicsList where repetation is less than UPDATED_REPETATION
        defaultCharateristicsShouldBeFound("repetation.lessThan=" + UPDATED_REPETATION);
    }

    @Test
    @Transactional
    void getAllCharateristicsByRepetationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        // Get all the charateristicsList where repetation is greater than DEFAULT_REPETATION
        defaultCharateristicsShouldNotBeFound("repetation.greaterThan=" + DEFAULT_REPETATION);

        // Get all the charateristicsList where repetation is greater than SMALLER_REPETATION
        defaultCharateristicsShouldBeFound("repetation.greaterThan=" + SMALLER_REPETATION);
    }

    @Test
    @Transactional
    void getAllCharateristicsByUserPerDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);
        UserPerDepartment userPerDepartment = UserPerDepartmentResourceIT.createEntity(em);
        em.persist(userPerDepartment);
        em.flush();
        charateristics.setUserPerDepartment(userPerDepartment);
        charateristicsRepository.saveAndFlush(charateristics);
        Long userPerDepartmentId = userPerDepartment.getId();

        // Get all the charateristicsList where userPerDepartment equals to userPerDepartmentId
        defaultCharateristicsShouldBeFound("userPerDepartmentId.equals=" + userPerDepartmentId);

        // Get all the charateristicsList where userPerDepartment equals to userPerDepartmentId + 1
        defaultCharateristicsShouldNotBeFound("userPerDepartmentId.equals=" + (userPerDepartmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCharateristicsShouldBeFound(String filter) throws Exception {
        restCharateristicsMockMvc
            .perform(get("/api/charateristics?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(charateristics.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].repetation").value(hasItem(DEFAULT_REPETATION)));

        // Check, that the count call also returns 1
        restCharateristicsMockMvc
            .perform(get("/api/charateristics/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCharateristicsShouldNotBeFound(String filter) throws Exception {
        restCharateristicsMockMvc
            .perform(get("/api/charateristics?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCharateristicsMockMvc
            .perform(get("/api/charateristics/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCharateristics() throws Exception {
        // Get the charateristics
        restCharateristicsMockMvc.perform(get("/api/charateristics/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateCharateristics() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        int databaseSizeBeforeUpdate = charateristicsRepository.findAll().size();

        // Update the charateristics
        Charateristics updatedCharateristics = charateristicsRepository.findById(charateristics.getId()).get();
        // Disconnect from session so that the updates on updatedCharateristics are not directly saved in db
        em.detach(updatedCharateristics);
        updatedCharateristics.title(UPDATED_TITLE).repetation(UPDATED_REPETATION);
        CharateristicsDTO charateristicsDTO = charateristicsMapper.toDto(updatedCharateristics);

        restCharateristicsMockMvc
            .perform(
                put("/api/charateristics")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(charateristicsDTO))
            )
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
    void updateNonExistingCharateristics() throws Exception {
        int databaseSizeBeforeUpdate = charateristicsRepository.findAll().size();

        // Create the Charateristics
        CharateristicsDTO charateristicsDTO = charateristicsMapper.toDto(charateristics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharateristicsMockMvc
            .perform(
                put("/api/charateristics")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(charateristicsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Charateristics in the database
        List<Charateristics> charateristicsList = charateristicsRepository.findAll();
        assertThat(charateristicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCharateristicsWithPatch() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        int databaseSizeBeforeUpdate = charateristicsRepository.findAll().size();

        // Update the charateristics using partial update
        Charateristics partialUpdatedCharateristics = new Charateristics();
        partialUpdatedCharateristics.setId(charateristics.getId());

        partialUpdatedCharateristics.title(UPDATED_TITLE);

        restCharateristicsMockMvc
            .perform(
                patch("/api/charateristics")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCharateristics))
            )
            .andExpect(status().isOk());

        // Validate the Charateristics in the database
        List<Charateristics> charateristicsList = charateristicsRepository.findAll();
        assertThat(charateristicsList).hasSize(databaseSizeBeforeUpdate);
        Charateristics testCharateristics = charateristicsList.get(charateristicsList.size() - 1);
        assertThat(testCharateristics.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCharateristics.getRepetation()).isEqualTo(DEFAULT_REPETATION);
    }

    @Test
    @Transactional
    void fullUpdateCharateristicsWithPatch() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        int databaseSizeBeforeUpdate = charateristicsRepository.findAll().size();

        // Update the charateristics using partial update
        Charateristics partialUpdatedCharateristics = new Charateristics();
        partialUpdatedCharateristics.setId(charateristics.getId());

        partialUpdatedCharateristics.title(UPDATED_TITLE).repetation(UPDATED_REPETATION);

        restCharateristicsMockMvc
            .perform(
                patch("/api/charateristics")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCharateristics))
            )
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
    void partialUpdateCharateristicsShouldThrown() throws Exception {
        // Update the charateristics without id should throw
        Charateristics partialUpdatedCharateristics = new Charateristics();

        restCharateristicsMockMvc
            .perform(
                patch("/api/charateristics")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCharateristics))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteCharateristics() throws Exception {
        // Initialize the database
        charateristicsRepository.saveAndFlush(charateristics);

        int databaseSizeBeforeDelete = charateristicsRepository.findAll().size();

        // Delete the charateristics
        restCharateristicsMockMvc
            .perform(delete("/api/charateristics/{id}", charateristics.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Charateristics> charateristicsList = charateristicsRepository.findAll();
        assertThat(charateristicsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
