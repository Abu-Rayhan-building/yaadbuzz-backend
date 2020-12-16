package edu.sharif.math.yaadbuzz.web.rest;

import edu.sharif.math.yaadbuzz.YaadbuzzBackendApp;
import edu.sharif.math.yaadbuzz.domain.UserPerDepartment;
import edu.sharif.math.yaadbuzz.domain.TopicVote;
import edu.sharif.math.yaadbuzz.domain.Picture;
import edu.sharif.math.yaadbuzz.domain.User;
import edu.sharif.math.yaadbuzz.domain.Department;
import edu.sharif.math.yaadbuzz.domain.Topic;
import edu.sharif.math.yaadbuzz.domain.Memory;
import edu.sharif.math.yaadbuzz.repository.UserPerDepartmentRepository;
import edu.sharif.math.yaadbuzz.service.UserPerDepartmentService;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;
import edu.sharif.math.yaadbuzz.service.mapper.UserPerDepartmentMapper;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentCriteria;
import edu.sharif.math.yaadbuzz.service.UserPerDepartmentQueryService;

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
 * Integration tests for the {@link UserPerDepartmentResource} REST controller.
 */
@SpringBootTest(classes = YaadbuzzBackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class UserPerDepartmentResourceIT {

    private static final String DEFAULT_NIC_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NIC_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BIO = "AAAAAAAAAA";
    private static final String UPDATED_BIO = "BBBBBBBBBB";

    @Autowired
    private UserPerDepartmentRepository userPerDepartmentRepository;

    @Autowired
    private UserPerDepartmentMapper userPerDepartmentMapper;

    @Autowired
    private UserPerDepartmentService userPerDepartmentService;

    @Autowired
    private UserPerDepartmentQueryService userPerDepartmentQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserPerDepartmentMockMvc;

    private UserPerDepartment userPerDepartment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserPerDepartment createEntity(EntityManager em) {
        UserPerDepartment userPerDepartment = new UserPerDepartment()
            .nicName(DEFAULT_NIC_NAME)
            .bio(DEFAULT_BIO);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userPerDepartment.setRealUser(user);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        userPerDepartment.setDepartment(department);
        return userPerDepartment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserPerDepartment createUpdatedEntity(EntityManager em) {
        UserPerDepartment userPerDepartment = new UserPerDepartment()
            .nicName(UPDATED_NIC_NAME)
            .bio(UPDATED_BIO);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userPerDepartment.setRealUser(user);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createUpdatedEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        userPerDepartment.setDepartment(department);
        return userPerDepartment;
    }

    @BeforeEach
    public void initTest() {
        userPerDepartment = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserPerDepartment() throws Exception {
        int databaseSizeBeforeCreate = userPerDepartmentRepository.findAll().size();
        // Create the UserPerDepartment
        UserPerDepartmentDTO userPerDepartmentDTO = userPerDepartmentMapper.toDto(userPerDepartment);
        restUserPerDepartmentMockMvc.perform(post("/api/user-per-departments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userPerDepartmentDTO)))
            .andExpect(status().isCreated());

        // Validate the UserPerDepartment in the database
        List<UserPerDepartment> userPerDepartmentList = userPerDepartmentRepository.findAll();
        assertThat(userPerDepartmentList).hasSize(databaseSizeBeforeCreate + 1);
        UserPerDepartment testUserPerDepartment = userPerDepartmentList.get(userPerDepartmentList.size() - 1);
        assertThat(testUserPerDepartment.getNicName()).isEqualTo(DEFAULT_NIC_NAME);
        assertThat(testUserPerDepartment.getBio()).isEqualTo(DEFAULT_BIO);
    }

    @Test
    @Transactional
    public void createUserPerDepartmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userPerDepartmentRepository.findAll().size();

        // Create the UserPerDepartment with an existing ID
        userPerDepartment.setId(1L);
        UserPerDepartmentDTO userPerDepartmentDTO = userPerDepartmentMapper.toDto(userPerDepartment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserPerDepartmentMockMvc.perform(post("/api/user-per-departments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userPerDepartmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserPerDepartment in the database
        List<UserPerDepartment> userPerDepartmentList = userPerDepartmentRepository.findAll();
        assertThat(userPerDepartmentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllUserPerDepartments() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList
        restUserPerDepartmentMockMvc.perform(get("/api/user-per-departments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPerDepartment.getId().intValue())))
            .andExpect(jsonPath("$.[*].nicName").value(hasItem(DEFAULT_NIC_NAME)))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO)));
    }
    
    @Test
    @Transactional
    public void getUserPerDepartment() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get the userPerDepartment
        restUserPerDepartmentMockMvc.perform(get("/api/user-per-departments/{id}", userPerDepartment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userPerDepartment.getId().intValue()))
            .andExpect(jsonPath("$.nicName").value(DEFAULT_NIC_NAME))
            .andExpect(jsonPath("$.bio").value(DEFAULT_BIO));
    }


    @Test
    @Transactional
    public void getUserPerDepartmentsByIdFiltering() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        Long id = userPerDepartment.getId();

        defaultUserPerDepartmentShouldBeFound("id.equals=" + id);
        defaultUserPerDepartmentShouldNotBeFound("id.notEquals=" + id);

        defaultUserPerDepartmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserPerDepartmentShouldNotBeFound("id.greaterThan=" + id);

        defaultUserPerDepartmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserPerDepartmentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllUserPerDepartmentsByNicNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where nicName equals to DEFAULT_NIC_NAME
        defaultUserPerDepartmentShouldBeFound("nicName.equals=" + DEFAULT_NIC_NAME);

        // Get all the userPerDepartmentList where nicName equals to UPDATED_NIC_NAME
        defaultUserPerDepartmentShouldNotBeFound("nicName.equals=" + UPDATED_NIC_NAME);
    }

    @Test
    @Transactional
    public void getAllUserPerDepartmentsByNicNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where nicName not equals to DEFAULT_NIC_NAME
        defaultUserPerDepartmentShouldNotBeFound("nicName.notEquals=" + DEFAULT_NIC_NAME);

        // Get all the userPerDepartmentList where nicName not equals to UPDATED_NIC_NAME
        defaultUserPerDepartmentShouldBeFound("nicName.notEquals=" + UPDATED_NIC_NAME);
    }

    @Test
    @Transactional
    public void getAllUserPerDepartmentsByNicNameIsInShouldWork() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where nicName in DEFAULT_NIC_NAME or UPDATED_NIC_NAME
        defaultUserPerDepartmentShouldBeFound("nicName.in=" + DEFAULT_NIC_NAME + "," + UPDATED_NIC_NAME);

        // Get all the userPerDepartmentList where nicName equals to UPDATED_NIC_NAME
        defaultUserPerDepartmentShouldNotBeFound("nicName.in=" + UPDATED_NIC_NAME);
    }

    @Test
    @Transactional
    public void getAllUserPerDepartmentsByNicNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where nicName is not null
        defaultUserPerDepartmentShouldBeFound("nicName.specified=true");

        // Get all the userPerDepartmentList where nicName is null
        defaultUserPerDepartmentShouldNotBeFound("nicName.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserPerDepartmentsByNicNameContainsSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where nicName contains DEFAULT_NIC_NAME
        defaultUserPerDepartmentShouldBeFound("nicName.contains=" + DEFAULT_NIC_NAME);

        // Get all the userPerDepartmentList where nicName contains UPDATED_NIC_NAME
        defaultUserPerDepartmentShouldNotBeFound("nicName.contains=" + UPDATED_NIC_NAME);
    }

    @Test
    @Transactional
    public void getAllUserPerDepartmentsByNicNameNotContainsSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where nicName does not contain DEFAULT_NIC_NAME
        defaultUserPerDepartmentShouldNotBeFound("nicName.doesNotContain=" + DEFAULT_NIC_NAME);

        // Get all the userPerDepartmentList where nicName does not contain UPDATED_NIC_NAME
        defaultUserPerDepartmentShouldBeFound("nicName.doesNotContain=" + UPDATED_NIC_NAME);
    }


    @Test
    @Transactional
    public void getAllUserPerDepartmentsByBioIsEqualToSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where bio equals to DEFAULT_BIO
        defaultUserPerDepartmentShouldBeFound("bio.equals=" + DEFAULT_BIO);

        // Get all the userPerDepartmentList where bio equals to UPDATED_BIO
        defaultUserPerDepartmentShouldNotBeFound("bio.equals=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    public void getAllUserPerDepartmentsByBioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where bio not equals to DEFAULT_BIO
        defaultUserPerDepartmentShouldNotBeFound("bio.notEquals=" + DEFAULT_BIO);

        // Get all the userPerDepartmentList where bio not equals to UPDATED_BIO
        defaultUserPerDepartmentShouldBeFound("bio.notEquals=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    public void getAllUserPerDepartmentsByBioIsInShouldWork() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where bio in DEFAULT_BIO or UPDATED_BIO
        defaultUserPerDepartmentShouldBeFound("bio.in=" + DEFAULT_BIO + "," + UPDATED_BIO);

        // Get all the userPerDepartmentList where bio equals to UPDATED_BIO
        defaultUserPerDepartmentShouldNotBeFound("bio.in=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    public void getAllUserPerDepartmentsByBioIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where bio is not null
        defaultUserPerDepartmentShouldBeFound("bio.specified=true");

        // Get all the userPerDepartmentList where bio is null
        defaultUserPerDepartmentShouldNotBeFound("bio.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserPerDepartmentsByBioContainsSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where bio contains DEFAULT_BIO
        defaultUserPerDepartmentShouldBeFound("bio.contains=" + DEFAULT_BIO);

        // Get all the userPerDepartmentList where bio contains UPDATED_BIO
        defaultUserPerDepartmentShouldNotBeFound("bio.contains=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    public void getAllUserPerDepartmentsByBioNotContainsSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where bio does not contain DEFAULT_BIO
        defaultUserPerDepartmentShouldNotBeFound("bio.doesNotContain=" + DEFAULT_BIO);

        // Get all the userPerDepartmentList where bio does not contain UPDATED_BIO
        defaultUserPerDepartmentShouldBeFound("bio.doesNotContain=" + UPDATED_BIO);
    }


    @Test
    @Transactional
    public void getAllUserPerDepartmentsByTopicAssignedsIsEqualToSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);
        TopicVote topicAssigneds = TopicVoteResourceIT.createEntity(em);
        em.persist(topicAssigneds);
        em.flush();
        userPerDepartment.addTopicAssigneds(topicAssigneds);
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);
        Long topicAssignedsId = topicAssigneds.getId();

        // Get all the userPerDepartmentList where topicAssigneds equals to topicAssignedsId
        defaultUserPerDepartmentShouldBeFound("topicAssignedsId.equals=" + topicAssignedsId);

        // Get all the userPerDepartmentList where topicAssigneds equals to topicAssignedsId + 1
        defaultUserPerDepartmentShouldNotBeFound("topicAssignedsId.equals=" + (topicAssignedsId + 1));
    }


    @Test
    @Transactional
    public void getAllUserPerDepartmentsByAvatarIsEqualToSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);
        Picture avatar = PictureResourceIT.createEntity(em);
        em.persist(avatar);
        em.flush();
        userPerDepartment.setAvatar(avatar);
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);
        Long avatarId = avatar.getId();

        // Get all the userPerDepartmentList where avatar equals to avatarId
        defaultUserPerDepartmentShouldBeFound("avatarId.equals=" + avatarId);

        // Get all the userPerDepartmentList where avatar equals to avatarId + 1
        defaultUserPerDepartmentShouldNotBeFound("avatarId.equals=" + (avatarId + 1));
    }


    @Test
    @Transactional
    public void getAllUserPerDepartmentsByRealUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User realUser = userPerDepartment.getRealUser();
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);
        Long realUserId = realUser.getId();

        // Get all the userPerDepartmentList where realUser equals to realUserId
        defaultUserPerDepartmentShouldBeFound("realUserId.equals=" + realUserId);

        // Get all the userPerDepartmentList where realUser equals to realUserId + 1
        defaultUserPerDepartmentShouldNotBeFound("realUserId.equals=" + (realUserId + 1));
    }


    @Test
    @Transactional
    public void getAllUserPerDepartmentsByDepartmentIsEqualToSomething() throws Exception {
        // Get already existing entity
        Department department = userPerDepartment.getDepartment();
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);
        Long departmentId = department.getId();

        // Get all the userPerDepartmentList where department equals to departmentId
        defaultUserPerDepartmentShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the userPerDepartmentList where department equals to departmentId + 1
        defaultUserPerDepartmentShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }


    @Test
    @Transactional
    public void getAllUserPerDepartmentsByTopicsVotedIsEqualToSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);
        Topic topicsVoted = TopicResourceIT.createEntity(em);
        em.persist(topicsVoted);
        em.flush();
        userPerDepartment.addTopicsVoted(topicsVoted);
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);
        Long topicsVotedId = topicsVoted.getId();

        // Get all the userPerDepartmentList where topicsVoted equals to topicsVotedId
        defaultUserPerDepartmentShouldBeFound("topicsVotedId.equals=" + topicsVotedId);

        // Get all the userPerDepartmentList where topicsVoted equals to topicsVotedId + 1
        defaultUserPerDepartmentShouldNotBeFound("topicsVotedId.equals=" + (topicsVotedId + 1));
    }


    @Test
    @Transactional
    public void getAllUserPerDepartmentsByTagedInMemoeriesIsEqualToSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);
        Memory tagedInMemoeries = MemoryResourceIT.createEntity(em);
        em.persist(tagedInMemoeries);
        em.flush();
        userPerDepartment.addTagedInMemoeries(tagedInMemoeries);
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);
        Long tagedInMemoeriesId = tagedInMemoeries.getId();

        // Get all the userPerDepartmentList where tagedInMemoeries equals to tagedInMemoeriesId
        defaultUserPerDepartmentShouldBeFound("tagedInMemoeriesId.equals=" + tagedInMemoeriesId);

        // Get all the userPerDepartmentList where tagedInMemoeries equals to tagedInMemoeriesId + 1
        defaultUserPerDepartmentShouldNotBeFound("tagedInMemoeriesId.equals=" + (tagedInMemoeriesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserPerDepartmentShouldBeFound(String filter) throws Exception {
        restUserPerDepartmentMockMvc.perform(get("/api/user-per-departments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPerDepartment.getId().intValue())))
            .andExpect(jsonPath("$.[*].nicName").value(hasItem(DEFAULT_NIC_NAME)))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO)));

        // Check, that the count call also returns 1
        restUserPerDepartmentMockMvc.perform(get("/api/user-per-departments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserPerDepartmentShouldNotBeFound(String filter) throws Exception {
        restUserPerDepartmentMockMvc.perform(get("/api/user-per-departments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserPerDepartmentMockMvc.perform(get("/api/user-per-departments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingUserPerDepartment() throws Exception {
        // Get the userPerDepartment
        restUserPerDepartmentMockMvc.perform(get("/api/user-per-departments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserPerDepartment() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        int databaseSizeBeforeUpdate = userPerDepartmentRepository.findAll().size();

        // Update the userPerDepartment
        UserPerDepartment updatedUserPerDepartment = userPerDepartmentRepository.findById(userPerDepartment.getId()).get();
        // Disconnect from session so that the updates on updatedUserPerDepartment are not directly saved in db
        em.detach(updatedUserPerDepartment);
        updatedUserPerDepartment
            .nicName(UPDATED_NIC_NAME)
            .bio(UPDATED_BIO);
        UserPerDepartmentDTO userPerDepartmentDTO = userPerDepartmentMapper.toDto(updatedUserPerDepartment);

        restUserPerDepartmentMockMvc.perform(put("/api/user-per-departments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userPerDepartmentDTO)))
            .andExpect(status().isOk());

        // Validate the UserPerDepartment in the database
        List<UserPerDepartment> userPerDepartmentList = userPerDepartmentRepository.findAll();
        assertThat(userPerDepartmentList).hasSize(databaseSizeBeforeUpdate);
        UserPerDepartment testUserPerDepartment = userPerDepartmentList.get(userPerDepartmentList.size() - 1);
        assertThat(testUserPerDepartment.getNicName()).isEqualTo(UPDATED_NIC_NAME);
        assertThat(testUserPerDepartment.getBio()).isEqualTo(UPDATED_BIO);
    }

    @Test
    @Transactional
    public void updateNonExistingUserPerDepartment() throws Exception {
        int databaseSizeBeforeUpdate = userPerDepartmentRepository.findAll().size();

        // Create the UserPerDepartment
        UserPerDepartmentDTO userPerDepartmentDTO = userPerDepartmentMapper.toDto(userPerDepartment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPerDepartmentMockMvc.perform(put("/api/user-per-departments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userPerDepartmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserPerDepartment in the database
        List<UserPerDepartment> userPerDepartmentList = userPerDepartmentRepository.findAll();
        assertThat(userPerDepartmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserPerDepartment() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        int databaseSizeBeforeDelete = userPerDepartmentRepository.findAll().size();

        // Delete the userPerDepartment
        restUserPerDepartmentMockMvc.perform(delete("/api/user-per-departments/{id}", userPerDepartment.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserPerDepartment> userPerDepartmentList = userPerDepartmentRepository.findAll();
        assertThat(userPerDepartmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
