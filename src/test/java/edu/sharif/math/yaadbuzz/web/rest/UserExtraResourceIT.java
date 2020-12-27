package edu.sharif.math.yaadbuzz.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.sharif.math.yaadbuzz.IntegrationTest;
import edu.sharif.math.yaadbuzz.domain.User;
import edu.sharif.math.yaadbuzz.domain.UserExtra;
import edu.sharif.math.yaadbuzz.domain.UserPerDepartment;
import edu.sharif.math.yaadbuzz.repository.UserExtraRepository;
import edu.sharif.math.yaadbuzz.service.UserExtraQueryService;
import edu.sharif.math.yaadbuzz.service.dto.UserExtraCriteria;
import edu.sharif.math.yaadbuzz.service.dto.UserExtraDTO;
import edu.sharif.math.yaadbuzz.service.mapper.UserExtraMapper;
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
 * Integration tests for the {@link UserExtraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserExtraResourceIT {

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    @Autowired
    private UserExtraRepository userExtraRepository;

    @Autowired
    private UserExtraMapper userExtraMapper;

    @Autowired
    private UserExtraQueryService userExtraQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserExtraMockMvc;

    private UserExtra userExtra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserExtra createEntity(EntityManager em) {
        UserExtra userExtra = new UserExtra().phone(DEFAULT_PHONE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userExtra.setUser(user);
        return userExtra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserExtra createUpdatedEntity(EntityManager em) {
        UserExtra userExtra = new UserExtra().phone(UPDATED_PHONE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userExtra.setUser(user);
        return userExtra;
    }

    @BeforeEach
    public void initTest() {
        userExtra = createEntity(em);
    }

    @Test
    @Transactional
    void createUserExtra() throws Exception {
        int databaseSizeBeforeCreate = userExtraRepository.findAll().size();
        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);
        restUserExtraMockMvc
            .perform(
                post("/api/user-extras").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userExtraDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeCreate + 1);
        UserExtra testUserExtra = userExtraList.get(userExtraList.size() - 1);
        assertThat(testUserExtra.getPhone()).isEqualTo(DEFAULT_PHONE);

        // Validate the id for MapsId, the ids must be same
        assertThat(testUserExtra.getId()).isEqualTo(testUserExtra.getUser().getId());
    }

    @Test
    @Transactional
    void createUserExtraWithExistingId() throws Exception {
        // Create the UserExtra with an existing ID
        userExtra.setId(1L);
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        int databaseSizeBeforeCreate = userExtraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserExtraMockMvc
            .perform(
                post("/api/user-extras").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userExtraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void updateUserExtraMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);
        int databaseSizeBeforeCreate = userExtraRepository.findAll().size();

        // Add a new parent entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();

        // Load the userExtra
        UserExtra updatedUserExtra = userExtraRepository.findById(userExtra.getId()).get();
        assertThat(updatedUserExtra).isNotNull();
        // Disconnect from session so that the updates on updatedUserExtra are not directly saved in db
        em.detach(updatedUserExtra);

        // Update the User with new association value
        updatedUserExtra.setUser(user);
        UserExtraDTO updatedUserExtraDTO = userExtraMapper.toDto(updatedUserExtra);
        assertThat(updatedUserExtraDTO).isNotNull();

        // Update the entity
        restUserExtraMockMvc
            .perform(
                put("/api/user-extras")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserExtraDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeCreate);
        UserExtra testUserExtra = userExtraList.get(userExtraList.size() - 1);
        // Validate the id for MapsId, the ids must be same
        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
        // assertThat(testUserExtra.getId()).isEqualTo(testUserExtra.getUser().getId());
    }

    @Test
    @Transactional
    void getAllUserExtras() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList
        restUserExtraMockMvc
            .perform(get("/api/user-extras?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userExtra.getId().intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }

    @Test
    @Transactional
    void getUserExtra() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get the userExtra
        restUserExtraMockMvc
            .perform(get("/api/user-extras/{id}", userExtra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userExtra.getId().intValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }

    @Test
    @Transactional
    void getUserExtrasByIdFiltering() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        Long id = userExtra.getId();

        defaultUserExtraShouldBeFound("id.equals=" + id);
        defaultUserExtraShouldNotBeFound("id.notEquals=" + id);

        defaultUserExtraShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserExtraShouldNotBeFound("id.greaterThan=" + id);

        defaultUserExtraShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserExtraShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserExtrasByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where phone equals to DEFAULT_PHONE
        defaultUserExtraShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the userExtraList where phone equals to UPDATED_PHONE
        defaultUserExtraShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserExtrasByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where phone not equals to DEFAULT_PHONE
        defaultUserExtraShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the userExtraList where phone not equals to UPDATED_PHONE
        defaultUserExtraShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserExtrasByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultUserExtraShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the userExtraList where phone equals to UPDATED_PHONE
        defaultUserExtraShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserExtrasByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where phone is not null
        defaultUserExtraShouldBeFound("phone.specified=true");

        // Get all the userExtraList where phone is null
        defaultUserExtraShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllUserExtrasByPhoneContainsSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where phone contains DEFAULT_PHONE
        defaultUserExtraShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the userExtraList where phone contains UPDATED_PHONE
        defaultUserExtraShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserExtrasByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where phone does not contain DEFAULT_PHONE
        defaultUserExtraShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the userExtraList where phone does not contain UPDATED_PHONE
        defaultUserExtraShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserExtrasByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = userExtra.getUser();
        userExtraRepository.saveAndFlush(userExtra);
        Long userId = user.getId();

        // Get all the userExtraList where user equals to userId
        defaultUserExtraShouldBeFound("userId.equals=" + userId);

        // Get all the userExtraList where user equals to userId + 1
        defaultUserExtraShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllUserExtrasByDefaultUserPerDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);
        UserPerDepartment defaultUserPerDepartment = UserPerDepartmentResourceIT.createEntity(em);
        em.persist(defaultUserPerDepartment);
        em.flush();
        userExtra.setDefaultUserPerDepartment(defaultUserPerDepartment);
        userExtraRepository.saveAndFlush(userExtra);
        Long defaultUserPerDepartmentId = defaultUserPerDepartment.getId();

        // Get all the userExtraList where defaultUserPerDepartment equals to defaultUserPerDepartmentId
        defaultUserExtraShouldBeFound("defaultUserPerDepartmentId.equals=" + defaultUserPerDepartmentId);

        // Get all the userExtraList where defaultUserPerDepartment equals to defaultUserPerDepartmentId + 1
        defaultUserExtraShouldNotBeFound("defaultUserPerDepartmentId.equals=" + (defaultUserPerDepartmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserExtraShouldBeFound(String filter) throws Exception {
        restUserExtraMockMvc
            .perform(get("/api/user-extras?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userExtra.getId().intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));

        // Check, that the count call also returns 1
        restUserExtraMockMvc
            .perform(get("/api/user-extras/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserExtraShouldNotBeFound(String filter) throws Exception {
        restUserExtraMockMvc
            .perform(get("/api/user-extras?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserExtraMockMvc
            .perform(get("/api/user-extras/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserExtra() throws Exception {
        // Get the userExtra
        restUserExtraMockMvc.perform(get("/api/user-extras/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateUserExtra() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        int databaseSizeBeforeUpdate = userExtraRepository.findAll().size();

        // Update the userExtra
        UserExtra updatedUserExtra = userExtraRepository.findById(userExtra.getId()).get();
        // Disconnect from session so that the updates on updatedUserExtra are not directly saved in db
        em.detach(updatedUserExtra);
        updatedUserExtra.phone(UPDATED_PHONE);
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(updatedUserExtra);

        restUserExtraMockMvc
            .perform(
                put("/api/user-extras").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userExtraDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeUpdate);
        UserExtra testUserExtra = userExtraList.get(userExtraList.size() - 1);
        assertThat(testUserExtra.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void updateNonExistingUserExtra() throws Exception {
        int databaseSizeBeforeUpdate = userExtraRepository.findAll().size();

        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserExtraMockMvc
            .perform(
                put("/api/user-extras").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userExtraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserExtraWithPatch() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        int databaseSizeBeforeUpdate = userExtraRepository.findAll().size();

        // Update the userExtra using partial update
        UserExtra partialUpdatedUserExtra = new UserExtra();
        partialUpdatedUserExtra.setId(userExtra.getId());

        restUserExtraMockMvc
            .perform(
                patch("/api/user-extras")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserExtra))
            )
            .andExpect(status().isOk());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeUpdate);
        UserExtra testUserExtra = userExtraList.get(userExtraList.size() - 1);
        assertThat(testUserExtra.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void fullUpdateUserExtraWithPatch() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        int databaseSizeBeforeUpdate = userExtraRepository.findAll().size();

        // Update the userExtra using partial update
        UserExtra partialUpdatedUserExtra = new UserExtra();
        partialUpdatedUserExtra.setId(userExtra.getId());

        partialUpdatedUserExtra.phone(UPDATED_PHONE);

        restUserExtraMockMvc
            .perform(
                patch("/api/user-extras")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserExtra))
            )
            .andExpect(status().isOk());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeUpdate);
        UserExtra testUserExtra = userExtraList.get(userExtraList.size() - 1);
        assertThat(testUserExtra.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void partialUpdateUserExtraShouldThrown() throws Exception {
        // Update the userExtra without id should throw
        UserExtra partialUpdatedUserExtra = new UserExtra();

        restUserExtraMockMvc
            .perform(
                patch("/api/user-extras")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserExtra))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteUserExtra() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        int databaseSizeBeforeDelete = userExtraRepository.findAll().size();

        // Delete the userExtra
        restUserExtraMockMvc
            .perform(delete("/api/user-extras/{id}", userExtra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
