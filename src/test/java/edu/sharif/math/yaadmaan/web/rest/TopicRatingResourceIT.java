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
import edu.sharif.math.yaadmaan.domain.Topic;
import edu.sharif.math.yaadmaan.domain.TopicRating;
import edu.sharif.math.yaadmaan.domain.UserPerDepartment;
import edu.sharif.math.yaadmaan.repository.TopicRatingRepository;
import edu.sharif.math.yaadmaan.service.TopicRatingService;
import edu.sharif.math.yaadmaan.service.dto.TopicRatingDTO;
import edu.sharif.math.yaadmaan.service.mapper.TopicRatingMapper;
import edu.sharif.math.yaadmaan.web.rest.TopicRatingResource;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TopicRatingResource} REST controller.
 */
@SpringBootTest(classes = YaadmaanApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TopicRatingResourceIT {

    private static final Integer DEFAULT_REPETITIONS = 1;
    private static final Integer UPDATED_REPETITIONS = 2;

    @Autowired
    private TopicRatingRepository topicRatingRepository;

    @Autowired
    private TopicRatingMapper topicRatingMapper;

    @Autowired
    private TopicRatingService topicRatingService;

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
        topicRating.setRating(topic);
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
        topicRating.setRating(topic);
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
