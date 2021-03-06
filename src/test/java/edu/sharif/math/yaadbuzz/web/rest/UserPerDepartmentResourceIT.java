package edu.sharif.math.yaadbuzz.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.sharif.math.yaadbuzz.IntegrationTest;
import edu.sharif.math.yaadbuzz.domain.Department;
import edu.sharif.math.yaadbuzz.domain.Memory;
import edu.sharif.math.yaadbuzz.domain.Picture;
import edu.sharif.math.yaadbuzz.domain.Topic;
import edu.sharif.math.yaadbuzz.domain.TopicVote;
import edu.sharif.math.yaadbuzz.domain.User;
import edu.sharif.math.yaadbuzz.domain.UserPerDepartment;
import edu.sharif.math.yaadbuzz.repository.UserPerDepartmentRepository;
import edu.sharif.math.yaadbuzz.service.UserPerDepartmentQueryService;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentCriteria;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;
import edu.sharif.math.yaadbuzz.service.mapper.UserPerDepartmentMapper;
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
 * Integration tests for the {@link UserPerDepartmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserPerDepartmentResourceIT {

    private static final String DEFAULT_NICKNAME = "AAAAAAAAAA";
    private static final String UPDATED_NICKNAME = "BBBBBBBBBB";

    private static final String DEFAULT_BIO = "AAAAAAAAAA";
    private static final String UPDATED_BIO = "BBBBBBBBBB";

    @Autowired
    private UserPerDepartmentRepository userPerDepartmentRepository;

    @Autowired
    private UserPerDepartmentMapper userPerDepartmentMapper;

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
        UserPerDepartment userPerDepartment = new UserPerDepartment().nickname(DEFAULT_NICKNAME).bio(DEFAULT_BIO);
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
        UserPerDepartment userPerDepartment = new UserPerDepartment().nickname(UPDATED_NICKNAME).bio(UPDATED_BIO);
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
    void createUserPerDepartment() throws Exception {
        int databaseSizeBeforeCreate = userPerDepartmentRepository.findAll().size();
        // Create the UserPerDepartment
        UserPerDepartmentDTO userPerDepartmentDTO = userPerDepartmentMapper.toDto(userPerDepartment);
        restUserPerDepartmentMockMvc
            .perform(
                post("/api/user-per-departments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPerDepartmentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserPerDepartment in the database
        List<UserPerDepartment> userPerDepartmentList = userPerDepartmentRepository.findAll();
        assertThat(userPerDepartmentList).hasSize(databaseSizeBeforeCreate + 1);
        UserPerDepartment testUserPerDepartment = userPerDepartmentList.get(userPerDepartmentList.size() - 1);
        assertThat(testUserPerDepartment.getNickname()).isEqualTo(DEFAULT_NICKNAME);
        assertThat(testUserPerDepartment.getBio()).isEqualTo(DEFAULT_BIO);
    }

    @Test
    @Transactional
    void createUserPerDepartmentWithExistingId() throws Exception {
        // Create the UserPerDepartment with an existing ID
        userPerDepartment.setId(1L);
        UserPerDepartmentDTO userPerDepartmentDTO = userPerDepartmentMapper.toDto(userPerDepartment);

        int databaseSizeBeforeCreate = userPerDepartmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserPerDepartmentMockMvc
            .perform(
                post("/api/user-per-departments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPerDepartmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPerDepartment in the database
        List<UserPerDepartment> userPerDepartmentList = userPerDepartmentRepository.findAll();
        assertThat(userPerDepartmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserPerDepartments() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList
        restUserPerDepartmentMockMvc
            .perform(get("/api/user-per-departments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPerDepartment.getId().intValue())))
            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME)))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO)));
    }

    @Test
    @Transactional
    void getUserPerDepartment() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get the userPerDepartment
        restUserPerDepartmentMockMvc
            .perform(get("/api/user-per-departments/{id}", userPerDepartment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userPerDepartment.getId().intValue()))
            .andExpect(jsonPath("$.nickname").value(DEFAULT_NICKNAME))
            .andExpect(jsonPath("$.bio").value(DEFAULT_BIO));
    }

    @Test
    @Transactional
    void getUserPerDepartmentsByIdFiltering() throws Exception {
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
    void getAllUserPerDepartmentsByNicknameIsEqualToSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where nickname equals to DEFAULT_NICKNAME
        defaultUserPerDepartmentShouldBeFound("nickname.equals=" + DEFAULT_NICKNAME);

        // Get all the userPerDepartmentList where nickname equals to UPDATED_NICKNAME
        defaultUserPerDepartmentShouldNotBeFound("nickname.equals=" + UPDATED_NICKNAME);
    }

    @Test
    @Transactional
    void getAllUserPerDepartmentsByNicknameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where nickname not equals to DEFAULT_NICKNAME
        defaultUserPerDepartmentShouldNotBeFound("nickname.notEquals=" + DEFAULT_NICKNAME);

        // Get all the userPerDepartmentList where nickname not equals to UPDATED_NICKNAME
        defaultUserPerDepartmentShouldBeFound("nickname.notEquals=" + UPDATED_NICKNAME);
    }

    @Test
    @Transactional
    void getAllUserPerDepartmentsByNicknameIsInShouldWork() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where nickname in DEFAULT_NICKNAME or UPDATED_NICKNAME
        defaultUserPerDepartmentShouldBeFound("nickname.in=" + DEFAULT_NICKNAME + "," + UPDATED_NICKNAME);

        // Get all the userPerDepartmentList where nickname equals to UPDATED_NICKNAME
        defaultUserPerDepartmentShouldNotBeFound("nickname.in=" + UPDATED_NICKNAME);
    }

    @Test
    @Transactional
    void getAllUserPerDepartmentsByNicknameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where nickname is not null
        defaultUserPerDepartmentShouldBeFound("nickname.specified=true");

        // Get all the userPerDepartmentList where nickname is null
        defaultUserPerDepartmentShouldNotBeFound("nickname.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPerDepartmentsByNicknameContainsSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where nickname contains DEFAULT_NICKNAME
        defaultUserPerDepartmentShouldBeFound("nickname.contains=" + DEFAULT_NICKNAME);

        // Get all the userPerDepartmentList where nickname contains UPDATED_NICKNAME
        defaultUserPerDepartmentShouldNotBeFound("nickname.contains=" + UPDATED_NICKNAME);
    }

    @Test
    @Transactional
    void getAllUserPerDepartmentsByNicknameNotContainsSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where nickname does not contain DEFAULT_NICKNAME
        defaultUserPerDepartmentShouldNotBeFound("nickname.doesNotContain=" + DEFAULT_NICKNAME);

        // Get all the userPerDepartmentList where nickname does not contain UPDATED_NICKNAME
        defaultUserPerDepartmentShouldBeFound("nickname.doesNotContain=" + UPDATED_NICKNAME);
    }

    @Test
    @Transactional
    void getAllUserPerDepartmentsByBioIsEqualToSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where bio equals to DEFAULT_BIO
        defaultUserPerDepartmentShouldBeFound("bio.equals=" + DEFAULT_BIO);

        // Get all the userPerDepartmentList where bio equals to UPDATED_BIO
        defaultUserPerDepartmentShouldNotBeFound("bio.equals=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    void getAllUserPerDepartmentsByBioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where bio not equals to DEFAULT_BIO
        defaultUserPerDepartmentShouldNotBeFound("bio.notEquals=" + DEFAULT_BIO);

        // Get all the userPerDepartmentList where bio not equals to UPDATED_BIO
        defaultUserPerDepartmentShouldBeFound("bio.notEquals=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    void getAllUserPerDepartmentsByBioIsInShouldWork() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where bio in DEFAULT_BIO or UPDATED_BIO
        defaultUserPerDepartmentShouldBeFound("bio.in=" + DEFAULT_BIO + "," + UPDATED_BIO);

        // Get all the userPerDepartmentList where bio equals to UPDATED_BIO
        defaultUserPerDepartmentShouldNotBeFound("bio.in=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    void getAllUserPerDepartmentsByBioIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where bio is not null
        defaultUserPerDepartmentShouldBeFound("bio.specified=true");

        // Get all the userPerDepartmentList where bio is null
        defaultUserPerDepartmentShouldNotBeFound("bio.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPerDepartmentsByBioContainsSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where bio contains DEFAULT_BIO
        defaultUserPerDepartmentShouldBeFound("bio.contains=" + DEFAULT_BIO);

        // Get all the userPerDepartmentList where bio contains UPDATED_BIO
        defaultUserPerDepartmentShouldNotBeFound("bio.contains=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    void getAllUserPerDepartmentsByBioNotContainsSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        // Get all the userPerDepartmentList where bio does not contain DEFAULT_BIO
        defaultUserPerDepartmentShouldNotBeFound("bio.doesNotContain=" + DEFAULT_BIO);

        // Get all the userPerDepartmentList where bio does not contain UPDATED_BIO
        defaultUserPerDepartmentShouldBeFound("bio.doesNotContain=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    void getAllUserPerDepartmentsByTopicAssignedsIsEqualToSomething() throws Exception {
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
    void getAllUserPerDepartmentsByAvatarIsEqualToSomething() throws Exception {
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
    void getAllUserPerDepartmentsByRealUserIsEqualToSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);
        User realUser = UserResourceIT.createEntity(em);
        em.persist(realUser);
        em.flush();
        userPerDepartment.setRealUser(realUser);
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);
        Long realUserId = realUser.getId();

        // Get all the userPerDepartmentList where realUser equals to realUserId
        defaultUserPerDepartmentShouldBeFound("realUserId.equals=" + realUserId);

        // Get all the userPerDepartmentList where realUser equals to realUserId + 1
        defaultUserPerDepartmentShouldNotBeFound("realUserId.equals=" + (realUserId + 1));
    }

    @Test
    @Transactional
    void getAllUserPerDepartmentsByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);
        Department department = DepartmentResourceIT.createEntity(em);
        em.persist(department);
        em.flush();
        userPerDepartment.setDepartment(department);
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);
        Long departmentId = department.getId();

        // Get all the userPerDepartmentList where department equals to departmentId
        defaultUserPerDepartmentShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the userPerDepartmentList where department equals to departmentId + 1
        defaultUserPerDepartmentShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    @Test
    @Transactional
    void getAllUserPerDepartmentsByTopicsVotedIsEqualToSomething() throws Exception {
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
    void getAllUserPerDepartmentsByTagedInMemoeriesIsEqualToSomething() throws Exception {
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
        restUserPerDepartmentMockMvc
            .perform(get("/api/user-per-departments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPerDepartment.getId().intValue())))
            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME)))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO)));

        // Check, that the count call also returns 1
        restUserPerDepartmentMockMvc
            .perform(get("/api/user-per-departments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserPerDepartmentShouldNotBeFound(String filter) throws Exception {
        restUserPerDepartmentMockMvc
            .perform(get("/api/user-per-departments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserPerDepartmentMockMvc
            .perform(get("/api/user-per-departments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserPerDepartment() throws Exception {
        // Get the userPerDepartment
        restUserPerDepartmentMockMvc.perform(get("/api/user-per-departments/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateUserPerDepartment() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        int databaseSizeBeforeUpdate = userPerDepartmentRepository.findAll().size();

        // Update the userPerDepartment
        UserPerDepartment updatedUserPerDepartment = userPerDepartmentRepository.findById(userPerDepartment.getId()).get();
        // Disconnect from session so that the updates on updatedUserPerDepartment are not directly saved in db
        em.detach(updatedUserPerDepartment);
        updatedUserPerDepartment.nickname(UPDATED_NICKNAME).bio(UPDATED_BIO);
        UserPerDepartmentDTO userPerDepartmentDTO = userPerDepartmentMapper.toDto(updatedUserPerDepartment);

        restUserPerDepartmentMockMvc
            .perform(
                put("/api/user-per-departments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPerDepartmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserPerDepartment in the database
        List<UserPerDepartment> userPerDepartmentList = userPerDepartmentRepository.findAll();
        assertThat(userPerDepartmentList).hasSize(databaseSizeBeforeUpdate);
        UserPerDepartment testUserPerDepartment = userPerDepartmentList.get(userPerDepartmentList.size() - 1);
        assertThat(testUserPerDepartment.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testUserPerDepartment.getBio()).isEqualTo(UPDATED_BIO);
    }

    @Test
    @Transactional
    void updateNonExistingUserPerDepartment() throws Exception {
        int databaseSizeBeforeUpdate = userPerDepartmentRepository.findAll().size();

        // Create the UserPerDepartment
        UserPerDepartmentDTO userPerDepartmentDTO = userPerDepartmentMapper.toDto(userPerDepartment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPerDepartmentMockMvc
            .perform(
                put("/api/user-per-departments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPerDepartmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPerDepartment in the database
        List<UserPerDepartment> userPerDepartmentList = userPerDepartmentRepository.findAll();
        assertThat(userPerDepartmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserPerDepartmentWithPatch() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        int databaseSizeBeforeUpdate = userPerDepartmentRepository.findAll().size();

        // Update the userPerDepartment using partial update
        UserPerDepartment partialUpdatedUserPerDepartment = new UserPerDepartment();
        partialUpdatedUserPerDepartment.setId(userPerDepartment.getId());

        restUserPerDepartmentMockMvc
            .perform(
                patch("/api/user-per-departments")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserPerDepartment))
            )
            .andExpect(status().isOk());

        // Validate the UserPerDepartment in the database
        List<UserPerDepartment> userPerDepartmentList = userPerDepartmentRepository.findAll();
        assertThat(userPerDepartmentList).hasSize(databaseSizeBeforeUpdate);
        UserPerDepartment testUserPerDepartment = userPerDepartmentList.get(userPerDepartmentList.size() - 1);
        assertThat(testUserPerDepartment.getNickname()).isEqualTo(DEFAULT_NICKNAME);
        assertThat(testUserPerDepartment.getBio()).isEqualTo(DEFAULT_BIO);
    }

    @Test
    @Transactional
    void fullUpdateUserPerDepartmentWithPatch() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        int databaseSizeBeforeUpdate = userPerDepartmentRepository.findAll().size();

        // Update the userPerDepartment using partial update
        UserPerDepartment partialUpdatedUserPerDepartment = new UserPerDepartment();
        partialUpdatedUserPerDepartment.setId(userPerDepartment.getId());

        partialUpdatedUserPerDepartment.nickname(UPDATED_NICKNAME).bio(UPDATED_BIO);

        restUserPerDepartmentMockMvc
            .perform(
                patch("/api/user-per-departments")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserPerDepartment))
            )
            .andExpect(status().isOk());

        // Validate the UserPerDepartment in the database
        List<UserPerDepartment> userPerDepartmentList = userPerDepartmentRepository.findAll();
        assertThat(userPerDepartmentList).hasSize(databaseSizeBeforeUpdate);
        UserPerDepartment testUserPerDepartment = userPerDepartmentList.get(userPerDepartmentList.size() - 1);
        assertThat(testUserPerDepartment.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testUserPerDepartment.getBio()).isEqualTo(UPDATED_BIO);
    }

    @Test
    @Transactional
    void partialUpdateUserPerDepartmentShouldThrown() throws Exception {
        // Update the userPerDepartment without id should throw
        UserPerDepartment partialUpdatedUserPerDepartment = new UserPerDepartment();

        restUserPerDepartmentMockMvc
            .perform(
                patch("/api/user-per-departments")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserPerDepartment))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteUserPerDepartment() throws Exception {
        // Initialize the database
        userPerDepartmentRepository.saveAndFlush(userPerDepartment);

        int databaseSizeBeforeDelete = userPerDepartmentRepository.findAll().size();

        // Delete the userPerDepartment
        restUserPerDepartmentMockMvc
            .perform(delete("/api/user-per-departments/{id}", userPerDepartment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserPerDepartment> userPerDepartmentList = userPerDepartmentRepository.findAll();
        assertThat(userPerDepartmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
