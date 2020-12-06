package edu.sharif.math.yaadmaan.web.rest;

import edu.sharif.math.yaadmaan.YaadmaanApp;
import edu.sharif.math.yaadmaan.domain.Topic;
import edu.sharif.math.yaadmaan.domain.TopicRating;
import edu.sharif.math.yaadmaan.domain.Department;
import edu.sharif.math.yaadmaan.domain.UserPerDepartment;
import edu.sharif.math.yaadmaan.repository.TopicRepository;
import edu.sharif.math.yaadmaan.service.TopicService;
import edu.sharif.math.yaadmaan.service.dto.TopicDTO;
import edu.sharif.math.yaadmaan.service.mapper.TopicMapper;
import edu.sharif.math.yaadmaan.service.dto.TopicCriteria;
import edu.sharif.math.yaadmaan.service.TopicQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TopicResource} REST controller.
 */
@SpringBootTest(classes = YaadmaanApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class TopicResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private TopicRepository topicRepository;

    @Mock
    private TopicRepository topicRepositoryMock;

    @Autowired
    private TopicMapper topicMapper;

    @Mock
    private TopicService topicServiceMock;

    @Autowired
    private TopicService topicService;

    @Autowired
    private TopicQueryService topicQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTopicMockMvc;

    private Topic topic;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Topic createEntity(EntityManager em) {
        Topic topic = new Topic()
            .title(DEFAULT_TITLE);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        topic.setDepartment(department);
        return topic;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Topic createUpdatedEntity(EntityManager em) {
        Topic topic = new Topic()
            .title(UPDATED_TITLE);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createUpdatedEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        topic.setDepartment(department);
        return topic;
    }

    @BeforeEach
    public void initTest() {
        topic = createEntity(em);
    }

    @Test
    @Transactional
    public void createTopic() throws Exception {
        int databaseSizeBeforeCreate = topicRepository.findAll().size();
        // Create the Topic
        TopicDTO topicDTO = topicMapper.toDto(topic);
        restTopicMockMvc.perform(post("/api/topics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(topicDTO)))
            .andExpect(status().isCreated());

        // Validate the Topic in the database
        List<Topic> topicList = topicRepository.findAll();
        assertThat(topicList).hasSize(databaseSizeBeforeCreate + 1);
        Topic testTopic = topicList.get(topicList.size() - 1);
        assertThat(testTopic.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createTopicWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = topicRepository.findAll().size();

        // Create the Topic with an existing ID
        topic.setId(1L);
        TopicDTO topicDTO = topicMapper.toDto(topic);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTopicMockMvc.perform(post("/api/topics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(topicDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Topic in the database
        List<Topic> topicList = topicRepository.findAll();
        assertThat(topicList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = topicRepository.findAll().size();
        // set the field null
        topic.setTitle(null);

        // Create the Topic, which fails.
        TopicDTO topicDTO = topicMapper.toDto(topic);


        restTopicMockMvc.perform(post("/api/topics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(topicDTO)))
            .andExpect(status().isBadRequest());

        List<Topic> topicList = topicRepository.findAll();
        assertThat(topicList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTopics() throws Exception {
        // Initialize the database
        topicRepository.saveAndFlush(topic);

        // Get all the topicList
        restTopicMockMvc.perform(get("/api/topics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topic.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllTopicsWithEagerRelationshipsIsEnabled() throws Exception {
        when(topicServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTopicMockMvc.perform(get("/api/topics?eagerload=true"))
            .andExpect(status().isOk());

        verify(topicServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllTopicsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(topicServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTopicMockMvc.perform(get("/api/topics?eagerload=true"))
            .andExpect(status().isOk());

        verify(topicServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getTopic() throws Exception {
        // Initialize the database
        topicRepository.saveAndFlush(topic);

        // Get the topic
        restTopicMockMvc.perform(get("/api/topics/{id}", topic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(topic.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }


    @Test
    @Transactional
    public void getTopicsByIdFiltering() throws Exception {
        // Initialize the database
        topicRepository.saveAndFlush(topic);

        Long id = topic.getId();

        defaultTopicShouldBeFound("id.equals=" + id);
        defaultTopicShouldNotBeFound("id.notEquals=" + id);

        defaultTopicShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTopicShouldNotBeFound("id.greaterThan=" + id);

        defaultTopicShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTopicShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTopicsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        topicRepository.saveAndFlush(topic);

        // Get all the topicList where title equals to DEFAULT_TITLE
        defaultTopicShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the topicList where title equals to UPDATED_TITLE
        defaultTopicShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllTopicsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        topicRepository.saveAndFlush(topic);

        // Get all the topicList where title not equals to DEFAULT_TITLE
        defaultTopicShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the topicList where title not equals to UPDATED_TITLE
        defaultTopicShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllTopicsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        topicRepository.saveAndFlush(topic);

        // Get all the topicList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultTopicShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the topicList where title equals to UPDATED_TITLE
        defaultTopicShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllTopicsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        topicRepository.saveAndFlush(topic);

        // Get all the topicList where title is not null
        defaultTopicShouldBeFound("title.specified=true");

        // Get all the topicList where title is null
        defaultTopicShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllTopicsByTitleContainsSomething() throws Exception {
        // Initialize the database
        topicRepository.saveAndFlush(topic);

        // Get all the topicList where title contains DEFAULT_TITLE
        defaultTopicShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the topicList where title contains UPDATED_TITLE
        defaultTopicShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllTopicsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        topicRepository.saveAndFlush(topic);

        // Get all the topicList where title does not contain DEFAULT_TITLE
        defaultTopicShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the topicList where title does not contain UPDATED_TITLE
        defaultTopicShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllTopicsByRatingsIsEqualToSomething() throws Exception {
        // Initialize the database
        topicRepository.saveAndFlush(topic);
        TopicRating ratings = TopicRatingResourceIT.createEntity(em);
        em.persist(ratings);
        em.flush();
        topic.addRatings(ratings);
        topicRepository.saveAndFlush(topic);
        Long ratingsId = ratings.getId();

        // Get all the topicList where ratings equals to ratingsId
        defaultTopicShouldBeFound("ratingsId.equals=" + ratingsId);

        // Get all the topicList where ratings equals to ratingsId + 1
        defaultTopicShouldNotBeFound("ratingsId.equals=" + (ratingsId + 1));
    }


    @Test
    @Transactional
    public void getAllTopicsByDepartmentIsEqualToSomething() throws Exception {
        // Get already existing entity
        Department department = topic.getDepartment();
        topicRepository.saveAndFlush(topic);
        Long departmentId = department.getId();

        // Get all the topicList where department equals to departmentId
        defaultTopicShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the topicList where department equals to departmentId + 1
        defaultTopicShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }


    @Test
    @Transactional
    public void getAllTopicsByVotersIsEqualToSomething() throws Exception {
        // Initialize the database
        topicRepository.saveAndFlush(topic);
        UserPerDepartment voters = UserPerDepartmentResourceIT.createEntity(em);
        em.persist(voters);
        em.flush();
        topic.addVoters(voters);
        topicRepository.saveAndFlush(topic);
        Long votersId = voters.getId();

        // Get all the topicList where voters equals to votersId
        defaultTopicShouldBeFound("votersId.equals=" + votersId);

        // Get all the topicList where voters equals to votersId + 1
        defaultTopicShouldNotBeFound("votersId.equals=" + (votersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTopicShouldBeFound(String filter) throws Exception {
        restTopicMockMvc.perform(get("/api/topics?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topic.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));

        // Check, that the count call also returns 1
        restTopicMockMvc.perform(get("/api/topics/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTopicShouldNotBeFound(String filter) throws Exception {
        restTopicMockMvc.perform(get("/api/topics?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTopicMockMvc.perform(get("/api/topics/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTopic() throws Exception {
        // Get the topic
        restTopicMockMvc.perform(get("/api/topics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTopic() throws Exception {
        // Initialize the database
        topicRepository.saveAndFlush(topic);

        int databaseSizeBeforeUpdate = topicRepository.findAll().size();

        // Update the topic
        Topic updatedTopic = topicRepository.findById(topic.getId()).get();
        // Disconnect from session so that the updates on updatedTopic are not directly saved in db
        em.detach(updatedTopic);
        updatedTopic
            .title(UPDATED_TITLE);
        TopicDTO topicDTO = topicMapper.toDto(updatedTopic);

        restTopicMockMvc.perform(put("/api/topics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(topicDTO)))
            .andExpect(status().isOk());

        // Validate the Topic in the database
        List<Topic> topicList = topicRepository.findAll();
        assertThat(topicList).hasSize(databaseSizeBeforeUpdate);
        Topic testTopic = topicList.get(topicList.size() - 1);
        assertThat(testTopic.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingTopic() throws Exception {
        int databaseSizeBeforeUpdate = topicRepository.findAll().size();

        // Create the Topic
        TopicDTO topicDTO = topicMapper.toDto(topic);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTopicMockMvc.perform(put("/api/topics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(topicDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Topic in the database
        List<Topic> topicList = topicRepository.findAll();
        assertThat(topicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTopic() throws Exception {
        // Initialize the database
        topicRepository.saveAndFlush(topic);

        int databaseSizeBeforeDelete = topicRepository.findAll().size();

        // Delete the topic
        restTopicMockMvc.perform(delete("/api/topics/{id}", topic.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Topic> topicList = topicRepository.findAll();
        assertThat(topicList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
