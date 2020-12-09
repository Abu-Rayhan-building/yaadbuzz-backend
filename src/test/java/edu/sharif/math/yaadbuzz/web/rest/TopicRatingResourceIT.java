package edu.sharif.math.yaadbuzz.web.rest;

import edu.sharif.math.yaadbuzz.YaadbuzzBackendApp;
import edu.sharif.math.yaadbuzz.domain.TopicRating;
import edu.sharif.math.yaadbuzz.domain.Topic;
import edu.sharif.math.yaadbuzz.domain.UserPerDepartment;
import edu.sharif.math.yaadbuzz.repository.TopicRatingRepository;
import edu.sharif.math.yaadbuzz.service.TopicRatingService;
import edu.sharif.math.yaadbuzz.service.dto.TopicRatingDTO;
import edu.sharif.math.yaadbuzz.service.mapper.TopicRatingMapper;
import edu.sharif.math.yaadbuzz.service.dto.TopicRatingCriteria;
import edu.sharif.math.yaadbuzz.service.TopicRatingQueryService;

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
 * Integration tests for the {@link TopicRatingResource} REST controller.
 */
@SpringBootTest(classes = YaadbuzzBackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TopicRatingResourceIT {

    private static final Integer DEFAULT_REPETITIONS = 1;
    private static final Integer UPDATED_REPETITIONS = 2;
    private static final Integer SMALLER_REPETITIONS = 1 - 1;

    @Autowired
    private TopicRatingRepository topicRatingRepository;

    @Autowired
    private TopicRatingMapper topicRatingMapper;

    @Autowired
    private TopicRatingService topicRatingService;

    @Autowired
    private TopicRatingQueryService topicRatingQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTopicRatingMockMvc;

    private TopicRating topicRating;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TopicRating createEntity(EntityManager em) {
        TopicRating topicRating = new TopicRating()
            .repetitions(DEFAULT_REPETITIONS);
        // Add required entity
        Topic topic;
        if (TestUtil.findAll(em, Topic.class).isEmpty()) {
            topic = TopicResourceIT.createEntity(em);
            em.persist(topic);
            em.flush();
        } else {
            topic = TestUtil.findAll(em, Topic.class).get(0);
        }
        topicRating.setTopic(topic);
        // Add required entity
        UserPerDepartment userPerDepartment;
        if (TestUtil.findAll(em, UserPerDepartment.class).isEmpty()) {
            userPerDepartment = UserPerDepartmentResourceIT.createEntity(em);
            em.persist(userPerDepartment);
            em.flush();
        } else {
            userPerDepartment = TestUtil.findAll(em, UserPerDepartment.class).get(0);
        }
        topicRating.setUser(userPerDepartment);
        return topicRating;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TopicRating createUpdatedEntity(EntityManager em) {
        TopicRating topicRating = new TopicRating()
            .repetitions(UPDATED_REPETITIONS);
        // Add required entity
        Topic topic;
        if (TestUtil.findAll(em, Topic.class).isEmpty()) {
            topic = TopicResourceIT.createUpdatedEntity(em);
            em.persist(topic);
            em.flush();
        } else {
            topic = TestUtil.findAll(em, Topic.class).get(0);
        }
        topicRating.setTopic(topic);
        // Add required entity
        UserPerDepartment userPerDepartment;
        if (TestUtil.findAll(em, UserPerDepartment.class).isEmpty()) {
            userPerDepartment = UserPerDepartmentResourceIT.createUpdatedEntity(em);
            em.persist(userPerDepartment);
            em.flush();
        } else {
            userPerDepartment = TestUtil.findAll(em, UserPerDepartment.class).get(0);
        }
        topicRating.setUser(userPerDepartment);
        return topicRating;
    }

    @BeforeEach
    public void initTest() {
        topicRating = createEntity(em);
    }

    @Test
    @Transactional
    public void createTopicRating() throws Exception {
        int databaseSizeBeforeCreate = topicRatingRepository.findAll().size();
        // Create the TopicRating
        TopicRatingDTO topicRatingDTO = topicRatingMapper.toDto(topicRating);
        restTopicRatingMockMvc.perform(post("/api/topic-ratings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(topicRatingDTO)))
            .andExpect(status().isCreated());

        // Validate the TopicRating in the database
        List<TopicRating> topicRatingList = topicRatingRepository.findAll();
        assertThat(topicRatingList).hasSize(databaseSizeBeforeCreate + 1);
        TopicRating testTopicRating = topicRatingList.get(topicRatingList.size() - 1);
        assertThat(testTopicRating.getRepetitions()).isEqualTo(DEFAULT_REPETITIONS);
    }

    @Test
    @Transactional
    public void createTopicRatingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = topicRatingRepository.findAll().size();

        // Create the TopicRating with an existing ID
        topicRating.setId(1L);
        TopicRatingDTO topicRatingDTO = topicRatingMapper.toDto(topicRating);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTopicRatingMockMvc.perform(post("/api/topic-ratings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(topicRatingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TopicRating in the database
        List<TopicRating> topicRatingList = topicRatingRepository.findAll();
        assertThat(topicRatingList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTopicRatings() throws Exception {
        // Initialize the database
        topicRatingRepository.saveAndFlush(topicRating);

        // Get all the topicRatingList
        restTopicRatingMockMvc.perform(get("/api/topic-ratings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topicRating.getId().intValue())))
            .andExpect(jsonPath("$.[*].repetitions").value(hasItem(DEFAULT_REPETITIONS)));
    }
    
    @Test
    @Transactional
    public void getTopicRating() throws Exception {
        // Initialize the database
        topicRatingRepository.saveAndFlush(topicRating);

        // Get the topicRating
        restTopicRatingMockMvc.perform(get("/api/topic-ratings/{id}", topicRating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(topicRating.getId().intValue()))
            .andExpect(jsonPath("$.repetitions").value(DEFAULT_REPETITIONS));
    }


    @Test
    @Transactional
    public void getTopicRatingsByIdFiltering() throws Exception {
        // Initialize the database
        topicRatingRepository.saveAndFlush(topicRating);

        Long id = topicRating.getId();

        defaultTopicRatingShouldBeFound("id.equals=" + id);
        defaultTopicRatingShouldNotBeFound("id.notEquals=" + id);

        defaultTopicRatingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTopicRatingShouldNotBeFound("id.greaterThan=" + id);

        defaultTopicRatingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTopicRatingShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTopicRatingsByRepetitionsIsEqualToSomething() throws Exception {
        // Initialize the database
        topicRatingRepository.saveAndFlush(topicRating);

        // Get all the topicRatingList where repetitions equals to DEFAULT_REPETITIONS
        defaultTopicRatingShouldBeFound("repetitions.equals=" + DEFAULT_REPETITIONS);

        // Get all the topicRatingList where repetitions equals to UPDATED_REPETITIONS
        defaultTopicRatingShouldNotBeFound("repetitions.equals=" + UPDATED_REPETITIONS);
    }

    @Test
    @Transactional
    public void getAllTopicRatingsByRepetitionsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        topicRatingRepository.saveAndFlush(topicRating);

        // Get all the topicRatingList where repetitions not equals to DEFAULT_REPETITIONS
        defaultTopicRatingShouldNotBeFound("repetitions.notEquals=" + DEFAULT_REPETITIONS);

        // Get all the topicRatingList where repetitions not equals to UPDATED_REPETITIONS
        defaultTopicRatingShouldBeFound("repetitions.notEquals=" + UPDATED_REPETITIONS);
    }

    @Test
    @Transactional
    public void getAllTopicRatingsByRepetitionsIsInShouldWork() throws Exception {
        // Initialize the database
        topicRatingRepository.saveAndFlush(topicRating);

        // Get all the topicRatingList where repetitions in DEFAULT_REPETITIONS or UPDATED_REPETITIONS
        defaultTopicRatingShouldBeFound("repetitions.in=" + DEFAULT_REPETITIONS + "," + UPDATED_REPETITIONS);

        // Get all the topicRatingList where repetitions equals to UPDATED_REPETITIONS
        defaultTopicRatingShouldNotBeFound("repetitions.in=" + UPDATED_REPETITIONS);
    }

    @Test
    @Transactional
    public void getAllTopicRatingsByRepetitionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        topicRatingRepository.saveAndFlush(topicRating);

        // Get all the topicRatingList where repetitions is not null
        defaultTopicRatingShouldBeFound("repetitions.specified=true");

        // Get all the topicRatingList where repetitions is null
        defaultTopicRatingShouldNotBeFound("repetitions.specified=false");
    }

    @Test
    @Transactional
    public void getAllTopicRatingsByRepetitionsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        topicRatingRepository.saveAndFlush(topicRating);

        // Get all the topicRatingList where repetitions is greater than or equal to DEFAULT_REPETITIONS
        defaultTopicRatingShouldBeFound("repetitions.greaterThanOrEqual=" + DEFAULT_REPETITIONS);

        // Get all the topicRatingList where repetitions is greater than or equal to UPDATED_REPETITIONS
        defaultTopicRatingShouldNotBeFound("repetitions.greaterThanOrEqual=" + UPDATED_REPETITIONS);
    }

    @Test
    @Transactional
    public void getAllTopicRatingsByRepetitionsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        topicRatingRepository.saveAndFlush(topicRating);

        // Get all the topicRatingList where repetitions is less than or equal to DEFAULT_REPETITIONS
        defaultTopicRatingShouldBeFound("repetitions.lessThanOrEqual=" + DEFAULT_REPETITIONS);

        // Get all the topicRatingList where repetitions is less than or equal to SMALLER_REPETITIONS
        defaultTopicRatingShouldNotBeFound("repetitions.lessThanOrEqual=" + SMALLER_REPETITIONS);
    }

    @Test
    @Transactional
    public void getAllTopicRatingsByRepetitionsIsLessThanSomething() throws Exception {
        // Initialize the database
        topicRatingRepository.saveAndFlush(topicRating);

        // Get all the topicRatingList where repetitions is less than DEFAULT_REPETITIONS
        defaultTopicRatingShouldNotBeFound("repetitions.lessThan=" + DEFAULT_REPETITIONS);

        // Get all the topicRatingList where repetitions is less than UPDATED_REPETITIONS
        defaultTopicRatingShouldBeFound("repetitions.lessThan=" + UPDATED_REPETITIONS);
    }

    @Test
    @Transactional
    public void getAllTopicRatingsByRepetitionsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        topicRatingRepository.saveAndFlush(topicRating);

        // Get all the topicRatingList where repetitions is greater than DEFAULT_REPETITIONS
        defaultTopicRatingShouldNotBeFound("repetitions.greaterThan=" + DEFAULT_REPETITIONS);

        // Get all the topicRatingList where repetitions is greater than SMALLER_REPETITIONS
        defaultTopicRatingShouldBeFound("repetitions.greaterThan=" + SMALLER_REPETITIONS);
    }


    @Test
    @Transactional
    public void getAllTopicRatingsByTopicIsEqualToSomething() throws Exception {
        // Get already existing entity
        Topic topic = topicRating.getTopic();
        topicRatingRepository.saveAndFlush(topicRating);
        Long topicId = topic.getId();

        // Get all the topicRatingList where topic equals to topicId
        defaultTopicRatingShouldBeFound("topicId.equals=" + topicId);

        // Get all the topicRatingList where topic equals to topicId + 1
        defaultTopicRatingShouldNotBeFound("topicId.equals=" + (topicId + 1));
    }


    @Test
    @Transactional
    public void getAllTopicRatingsByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        UserPerDepartment user = topicRating.getUser();
        topicRatingRepository.saveAndFlush(topicRating);
        Long userId = user.getId();

        // Get all the topicRatingList where user equals to userId
        defaultTopicRatingShouldBeFound("userId.equals=" + userId);

        // Get all the topicRatingList where user equals to userId + 1
        defaultTopicRatingShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTopicRatingShouldBeFound(String filter) throws Exception {
        restTopicRatingMockMvc.perform(get("/api/topic-ratings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topicRating.getId().intValue())))
            .andExpect(jsonPath("$.[*].repetitions").value(hasItem(DEFAULT_REPETITIONS)));

        // Check, that the count call also returns 1
        restTopicRatingMockMvc.perform(get("/api/topic-ratings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTopicRatingShouldNotBeFound(String filter) throws Exception {
        restTopicRatingMockMvc.perform(get("/api/topic-ratings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTopicRatingMockMvc.perform(get("/api/topic-ratings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTopicRating() throws Exception {
        // Get the topicRating
        restTopicRatingMockMvc.perform(get("/api/topic-ratings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTopicRating() throws Exception {
        // Initialize the database
        topicRatingRepository.saveAndFlush(topicRating);

        int databaseSizeBeforeUpdate = topicRatingRepository.findAll().size();

        // Update the topicRating
        TopicRating updatedTopicRating = topicRatingRepository.findById(topicRating.getId()).get();
        // Disconnect from session so that the updates on updatedTopicRating are not directly saved in db
        em.detach(updatedTopicRating);
        updatedTopicRating
            .repetitions(UPDATED_REPETITIONS);
        TopicRatingDTO topicRatingDTO = topicRatingMapper.toDto(updatedTopicRating);

        restTopicRatingMockMvc.perform(put("/api/topic-ratings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(topicRatingDTO)))
            .andExpect(status().isOk());

        // Validate the TopicRating in the database
        List<TopicRating> topicRatingList = topicRatingRepository.findAll();
        assertThat(topicRatingList).hasSize(databaseSizeBeforeUpdate);
        TopicRating testTopicRating = topicRatingList.get(topicRatingList.size() - 1);
        assertThat(testTopicRating.getRepetitions()).isEqualTo(UPDATED_REPETITIONS);
    }

    @Test
    @Transactional
    public void updateNonExistingTopicRating() throws Exception {
        int databaseSizeBeforeUpdate = topicRatingRepository.findAll().size();

        // Create the TopicRating
        TopicRatingDTO topicRatingDTO = topicRatingMapper.toDto(topicRating);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTopicRatingMockMvc.perform(put("/api/topic-ratings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(topicRatingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TopicRating in the database
        List<TopicRating> topicRatingList = topicRatingRepository.findAll();
        assertThat(topicRatingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTopicRating() throws Exception {
        // Initialize the database
        topicRatingRepository.saveAndFlush(topicRating);

        int databaseSizeBeforeDelete = topicRatingRepository.findAll().size();

        // Delete the topicRating
        restTopicRatingMockMvc.perform(delete("/api/topic-ratings/{id}", topicRating.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TopicRating> topicRatingList = topicRatingRepository.findAll();
        assertThat(topicRatingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
