package edu.sharif.math.yaadbuzz.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.sharif.math.yaadbuzz.IntegrationTest;
import edu.sharif.math.yaadbuzz.domain.Topic;
import edu.sharif.math.yaadbuzz.domain.TopicVote;
import edu.sharif.math.yaadbuzz.domain.UserPerDepartment;
import edu.sharif.math.yaadbuzz.repository.TopicVoteRepository;
import edu.sharif.math.yaadbuzz.service.TopicVoteQueryService;
import edu.sharif.math.yaadbuzz.service.dto.TopicVoteCriteria;
import edu.sharif.math.yaadbuzz.service.dto.TopicVoteDTO;
import edu.sharif.math.yaadbuzz.service.mapper.TopicVoteMapper;
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
 * Integration tests for the {@link TopicVoteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TopicVoteResourceIT {

    private static final Integer DEFAULT_REPETITIONS = 1;
    private static final Integer UPDATED_REPETITIONS = 2;
    private static final Integer SMALLER_REPETITIONS = 1 - 1;

    @Autowired
    private TopicVoteRepository topicVoteRepository;

    @Autowired
    private TopicVoteMapper topicVoteMapper;

    @Autowired
    private TopicVoteQueryService topicVoteQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTopicVoteMockMvc;

    private TopicVote topicVote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TopicVote createEntity(EntityManager em) {
        TopicVote topicVote = new TopicVote().repetitions(DEFAULT_REPETITIONS);
        // Add required entity
        Topic topic;
        if (TestUtil.findAll(em, Topic.class).isEmpty()) {
            topic = TopicResourceIT.createEntity(em);
            em.persist(topic);
            em.flush();
        } else {
            topic = TestUtil.findAll(em, Topic.class).get(0);
        }
        topicVote.setTopic(topic);
        // Add required entity
        UserPerDepartment userPerDepartment;
        if (TestUtil.findAll(em, UserPerDepartment.class).isEmpty()) {
            userPerDepartment = UserPerDepartmentResourceIT.createEntity(em);
            em.persist(userPerDepartment);
            em.flush();
        } else {
            userPerDepartment = TestUtil.findAll(em, UserPerDepartment.class).get(0);
        }
        topicVote.setUser(userPerDepartment);
        return topicVote;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TopicVote createUpdatedEntity(EntityManager em) {
        TopicVote topicVote = new TopicVote().repetitions(UPDATED_REPETITIONS);
        // Add required entity
        Topic topic;
        if (TestUtil.findAll(em, Topic.class).isEmpty()) {
            topic = TopicResourceIT.createUpdatedEntity(em);
            em.persist(topic);
            em.flush();
        } else {
            topic = TestUtil.findAll(em, Topic.class).get(0);
        }
        topicVote.setTopic(topic);
        // Add required entity
        UserPerDepartment userPerDepartment;
        if (TestUtil.findAll(em, UserPerDepartment.class).isEmpty()) {
            userPerDepartment = UserPerDepartmentResourceIT.createUpdatedEntity(em);
            em.persist(userPerDepartment);
            em.flush();
        } else {
            userPerDepartment = TestUtil.findAll(em, UserPerDepartment.class).get(0);
        }
        topicVote.setUser(userPerDepartment);
        return topicVote;
    }

    @BeforeEach
    public void initTest() {
        topicVote = createEntity(em);
    }

    @Test
    @Transactional
    void createTopicVote() throws Exception {
        int databaseSizeBeforeCreate = topicVoteRepository.findAll().size();
        // Create the TopicVote
        TopicVoteDTO topicVoteDTO = topicVoteMapper.toDto(topicVote);
        restTopicVoteMockMvc
            .perform(
                post("/api/topic-votes").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(topicVoteDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TopicVote in the database
        List<TopicVote> topicVoteList = topicVoteRepository.findAll();
        assertThat(topicVoteList).hasSize(databaseSizeBeforeCreate + 1);
        TopicVote testTopicVote = topicVoteList.get(topicVoteList.size() - 1);
        assertThat(testTopicVote.getRepetitions()).isEqualTo(DEFAULT_REPETITIONS);
    }

    @Test
    @Transactional
    void createTopicVoteWithExistingId() throws Exception {
        // Create the TopicVote with an existing ID
        topicVote.setId(1L);
        TopicVoteDTO topicVoteDTO = topicVoteMapper.toDto(topicVote);

        int databaseSizeBeforeCreate = topicVoteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTopicVoteMockMvc
            .perform(
                post("/api/topic-votes").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(topicVoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TopicVote in the database
        List<TopicVote> topicVoteList = topicVoteRepository.findAll();
        assertThat(topicVoteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTopicVotes() throws Exception {
        // Initialize the database
        topicVoteRepository.saveAndFlush(topicVote);

        // Get all the topicVoteList
        restTopicVoteMockMvc
            .perform(get("/api/topic-votes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topicVote.getId().intValue())))
            .andExpect(jsonPath("$.[*].repetitions").value(hasItem(DEFAULT_REPETITIONS)));
    }

    @Test
    @Transactional
    void getTopicVote() throws Exception {
        // Initialize the database
        topicVoteRepository.saveAndFlush(topicVote);

        // Get the topicVote
        restTopicVoteMockMvc
            .perform(get("/api/topic-votes/{id}", topicVote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(topicVote.getId().intValue()))
            .andExpect(jsonPath("$.repetitions").value(DEFAULT_REPETITIONS));
    }

    @Test
    @Transactional
    void getTopicVotesByIdFiltering() throws Exception {
        // Initialize the database
        topicVoteRepository.saveAndFlush(topicVote);

        Long id = topicVote.getId();

        defaultTopicVoteShouldBeFound("id.equals=" + id);
        defaultTopicVoteShouldNotBeFound("id.notEquals=" + id);

        defaultTopicVoteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTopicVoteShouldNotBeFound("id.greaterThan=" + id);

        defaultTopicVoteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTopicVoteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTopicVotesByRepetitionsIsEqualToSomething() throws Exception {
        // Initialize the database
        topicVoteRepository.saveAndFlush(topicVote);

        // Get all the topicVoteList where repetitions equals to DEFAULT_REPETITIONS
        defaultTopicVoteShouldBeFound("repetitions.equals=" + DEFAULT_REPETITIONS);

        // Get all the topicVoteList where repetitions equals to UPDATED_REPETITIONS
        defaultTopicVoteShouldNotBeFound("repetitions.equals=" + UPDATED_REPETITIONS);
    }

    @Test
    @Transactional
    void getAllTopicVotesByRepetitionsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        topicVoteRepository.saveAndFlush(topicVote);

        // Get all the topicVoteList where repetitions not equals to DEFAULT_REPETITIONS
        defaultTopicVoteShouldNotBeFound("repetitions.notEquals=" + DEFAULT_REPETITIONS);

        // Get all the topicVoteList where repetitions not equals to UPDATED_REPETITIONS
        defaultTopicVoteShouldBeFound("repetitions.notEquals=" + UPDATED_REPETITIONS);
    }

    @Test
    @Transactional
    void getAllTopicVotesByRepetitionsIsInShouldWork() throws Exception {
        // Initialize the database
        topicVoteRepository.saveAndFlush(topicVote);

        // Get all the topicVoteList where repetitions in DEFAULT_REPETITIONS or UPDATED_REPETITIONS
        defaultTopicVoteShouldBeFound("repetitions.in=" + DEFAULT_REPETITIONS + "," + UPDATED_REPETITIONS);

        // Get all the topicVoteList where repetitions equals to UPDATED_REPETITIONS
        defaultTopicVoteShouldNotBeFound("repetitions.in=" + UPDATED_REPETITIONS);
    }

    @Test
    @Transactional
    void getAllTopicVotesByRepetitionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        topicVoteRepository.saveAndFlush(topicVote);

        // Get all the topicVoteList where repetitions is not null
        defaultTopicVoteShouldBeFound("repetitions.specified=true");

        // Get all the topicVoteList where repetitions is null
        defaultTopicVoteShouldNotBeFound("repetitions.specified=false");
    }

    @Test
    @Transactional
    void getAllTopicVotesByRepetitionsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        topicVoteRepository.saveAndFlush(topicVote);

        // Get all the topicVoteList where repetitions is greater than or equal to DEFAULT_REPETITIONS
        defaultTopicVoteShouldBeFound("repetitions.greaterThanOrEqual=" + DEFAULT_REPETITIONS);

        // Get all the topicVoteList where repetitions is greater than or equal to UPDATED_REPETITIONS
        defaultTopicVoteShouldNotBeFound("repetitions.greaterThanOrEqual=" + UPDATED_REPETITIONS);
    }

    @Test
    @Transactional
    void getAllTopicVotesByRepetitionsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        topicVoteRepository.saveAndFlush(topicVote);

        // Get all the topicVoteList where repetitions is less than or equal to DEFAULT_REPETITIONS
        defaultTopicVoteShouldBeFound("repetitions.lessThanOrEqual=" + DEFAULT_REPETITIONS);

        // Get all the topicVoteList where repetitions is less than or equal to SMALLER_REPETITIONS
        defaultTopicVoteShouldNotBeFound("repetitions.lessThanOrEqual=" + SMALLER_REPETITIONS);
    }

    @Test
    @Transactional
    void getAllTopicVotesByRepetitionsIsLessThanSomething() throws Exception {
        // Initialize the database
        topicVoteRepository.saveAndFlush(topicVote);

        // Get all the topicVoteList where repetitions is less than DEFAULT_REPETITIONS
        defaultTopicVoteShouldNotBeFound("repetitions.lessThan=" + DEFAULT_REPETITIONS);

        // Get all the topicVoteList where repetitions is less than UPDATED_REPETITIONS
        defaultTopicVoteShouldBeFound("repetitions.lessThan=" + UPDATED_REPETITIONS);
    }

    @Test
    @Transactional
    void getAllTopicVotesByRepetitionsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        topicVoteRepository.saveAndFlush(topicVote);

        // Get all the topicVoteList where repetitions is greater than DEFAULT_REPETITIONS
        defaultTopicVoteShouldNotBeFound("repetitions.greaterThan=" + DEFAULT_REPETITIONS);

        // Get all the topicVoteList where repetitions is greater than SMALLER_REPETITIONS
        defaultTopicVoteShouldBeFound("repetitions.greaterThan=" + SMALLER_REPETITIONS);
    }

    @Test
    @Transactional
    void getAllTopicVotesByTopicIsEqualToSomething() throws Exception {
        // Initialize the database
        topicVoteRepository.saveAndFlush(topicVote);
        Topic topic = TopicResourceIT.createEntity(em);
        em.persist(topic);
        em.flush();
        topicVote.setTopic(topic);
        topicVoteRepository.saveAndFlush(topicVote);
        Long topicId = topic.getId();

        // Get all the topicVoteList where topic equals to topicId
        defaultTopicVoteShouldBeFound("topicId.equals=" + topicId);

        // Get all the topicVoteList where topic equals to topicId + 1
        defaultTopicVoteShouldNotBeFound("topicId.equals=" + (topicId + 1));
    }

    @Test
    @Transactional
    void getAllTopicVotesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        topicVoteRepository.saveAndFlush(topicVote);
        UserPerDepartment user = UserPerDepartmentResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        topicVote.setUser(user);
        topicVoteRepository.saveAndFlush(topicVote);
        Long userId = user.getId();

        // Get all the topicVoteList where user equals to userId
        defaultTopicVoteShouldBeFound("userId.equals=" + userId);

        // Get all the topicVoteList where user equals to userId + 1
        defaultTopicVoteShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTopicVoteShouldBeFound(String filter) throws Exception {
        restTopicVoteMockMvc
            .perform(get("/api/topic-votes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topicVote.getId().intValue())))
            .andExpect(jsonPath("$.[*].repetitions").value(hasItem(DEFAULT_REPETITIONS)));

        // Check, that the count call also returns 1
        restTopicVoteMockMvc
            .perform(get("/api/topic-votes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTopicVoteShouldNotBeFound(String filter) throws Exception {
        restTopicVoteMockMvc
            .perform(get("/api/topic-votes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTopicVoteMockMvc
            .perform(get("/api/topic-votes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTopicVote() throws Exception {
        // Get the topicVote
        restTopicVoteMockMvc.perform(get("/api/topic-votes/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateTopicVote() throws Exception {
        // Initialize the database
        topicVoteRepository.saveAndFlush(topicVote);

        int databaseSizeBeforeUpdate = topicVoteRepository.findAll().size();

        // Update the topicVote
        TopicVote updatedTopicVote = topicVoteRepository.findById(topicVote.getId()).get();
        // Disconnect from session so that the updates on updatedTopicVote are not directly saved in db
        em.detach(updatedTopicVote);
        updatedTopicVote.repetitions(UPDATED_REPETITIONS);
        TopicVoteDTO topicVoteDTO = topicVoteMapper.toDto(updatedTopicVote);

        restTopicVoteMockMvc
            .perform(
                put("/api/topic-votes").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(topicVoteDTO))
            )
            .andExpect(status().isOk());

        // Validate the TopicVote in the database
        List<TopicVote> topicVoteList = topicVoteRepository.findAll();
        assertThat(topicVoteList).hasSize(databaseSizeBeforeUpdate);
        TopicVote testTopicVote = topicVoteList.get(topicVoteList.size() - 1);
        assertThat(testTopicVote.getRepetitions()).isEqualTo(UPDATED_REPETITIONS);
    }

    @Test
    @Transactional
    void updateNonExistingTopicVote() throws Exception {
        int databaseSizeBeforeUpdate = topicVoteRepository.findAll().size();

        // Create the TopicVote
        TopicVoteDTO topicVoteDTO = topicVoteMapper.toDto(topicVote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTopicVoteMockMvc
            .perform(
                put("/api/topic-votes").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(topicVoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TopicVote in the database
        List<TopicVote> topicVoteList = topicVoteRepository.findAll();
        assertThat(topicVoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTopicVoteWithPatch() throws Exception {
        // Initialize the database
        topicVoteRepository.saveAndFlush(topicVote);

        int databaseSizeBeforeUpdate = topicVoteRepository.findAll().size();

        // Update the topicVote using partial update
        TopicVote partialUpdatedTopicVote = new TopicVote();
        partialUpdatedTopicVote.setId(topicVote.getId());

        partialUpdatedTopicVote.repetitions(UPDATED_REPETITIONS);

        restTopicVoteMockMvc
            .perform(
                patch("/api/topic-votes")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTopicVote))
            )
            .andExpect(status().isOk());

        // Validate the TopicVote in the database
        List<TopicVote> topicVoteList = topicVoteRepository.findAll();
        assertThat(topicVoteList).hasSize(databaseSizeBeforeUpdate);
        TopicVote testTopicVote = topicVoteList.get(topicVoteList.size() - 1);
        assertThat(testTopicVote.getRepetitions()).isEqualTo(UPDATED_REPETITIONS);
    }

    @Test
    @Transactional
    void fullUpdateTopicVoteWithPatch() throws Exception {
        // Initialize the database
        topicVoteRepository.saveAndFlush(topicVote);

        int databaseSizeBeforeUpdate = topicVoteRepository.findAll().size();

        // Update the topicVote using partial update
        TopicVote partialUpdatedTopicVote = new TopicVote();
        partialUpdatedTopicVote.setId(topicVote.getId());

        partialUpdatedTopicVote.repetitions(UPDATED_REPETITIONS);

        restTopicVoteMockMvc
            .perform(
                patch("/api/topic-votes")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTopicVote))
            )
            .andExpect(status().isOk());

        // Validate the TopicVote in the database
        List<TopicVote> topicVoteList = topicVoteRepository.findAll();
        assertThat(topicVoteList).hasSize(databaseSizeBeforeUpdate);
        TopicVote testTopicVote = topicVoteList.get(topicVoteList.size() - 1);
        assertThat(testTopicVote.getRepetitions()).isEqualTo(UPDATED_REPETITIONS);
    }

    @Test
    @Transactional
    void partialUpdateTopicVoteShouldThrown() throws Exception {
        // Update the topicVote without id should throw
        TopicVote partialUpdatedTopicVote = new TopicVote();

        restTopicVoteMockMvc
            .perform(
                patch("/api/topic-votes")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTopicVote))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteTopicVote() throws Exception {
        // Initialize the database
        topicVoteRepository.saveAndFlush(topicVote);

        int databaseSizeBeforeDelete = topicVoteRepository.findAll().size();

        // Delete the topicVote
        restTopicVoteMockMvc
            .perform(delete("/api/topic-votes/{id}", topicVote.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TopicVote> topicVoteList = topicVoteRepository.findAll();
        assertThat(topicVoteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
