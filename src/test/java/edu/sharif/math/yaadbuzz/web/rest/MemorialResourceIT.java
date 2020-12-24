package edu.sharif.math.yaadbuzz.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.sharif.math.yaadbuzz.IntegrationTest;
import edu.sharif.math.yaadbuzz.domain.Comment;
import edu.sharif.math.yaadbuzz.domain.Department;
import edu.sharif.math.yaadbuzz.domain.Memorial;
import edu.sharif.math.yaadbuzz.domain.UserPerDepartment;
import edu.sharif.math.yaadbuzz.repository.MemorialRepository;
import edu.sharif.math.yaadbuzz.service.MemorialQueryService;
import edu.sharif.math.yaadbuzz.service.dto.MemorialCriteria;
import edu.sharif.math.yaadbuzz.service.dto.MemorialDTO;
import edu.sharif.math.yaadbuzz.service.mapper.MemorialMapper;
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
 * Integration tests for the {@link MemorialResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MemorialResourceIT {

    @Autowired
    private MemorialRepository memorialRepository;

    @Autowired
    private MemorialMapper memorialMapper;

    @Autowired
    private MemorialQueryService memorialQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMemorialMockMvc;

    private Memorial memorial;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Memorial createEntity(EntityManager em) {
        Memorial memorial = new Memorial();
        // Add required entity
        UserPerDepartment userPerDepartment;
        if (TestUtil.findAll(em, UserPerDepartment.class).isEmpty()) {
            userPerDepartment = UserPerDepartmentResourceIT.createEntity(em);
            em.persist(userPerDepartment);
            em.flush();
        } else {
            userPerDepartment = TestUtil.findAll(em, UserPerDepartment.class).get(0);
        }
        memorial.setWriter(userPerDepartment);
        // Add required entity
        memorial.setRecipient(userPerDepartment);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        memorial.setDepartment(department);
        return memorial;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Memorial createUpdatedEntity(EntityManager em) {
        Memorial memorial = new Memorial();
        // Add required entity
        UserPerDepartment userPerDepartment;
        if (TestUtil.findAll(em, UserPerDepartment.class).isEmpty()) {
            userPerDepartment = UserPerDepartmentResourceIT.createUpdatedEntity(em);
            em.persist(userPerDepartment);
            em.flush();
        } else {
            userPerDepartment = TestUtil.findAll(em, UserPerDepartment.class).get(0);
        }
        memorial.setWriter(userPerDepartment);
        // Add required entity
        memorial.setRecipient(userPerDepartment);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createUpdatedEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        memorial.setDepartment(department);
        return memorial;
    }

    @BeforeEach
    public void initTest() {
        memorial = createEntity(em);
    }

    @Test
    @Transactional
    void createMemorial() throws Exception {
        int databaseSizeBeforeCreate = memorialRepository.findAll().size();
        // Create the Memorial
        MemorialDTO memorialDTO = memorialMapper.toDto(memorial);
        restMemorialMockMvc
            .perform(post("/api/memorials").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memorialDTO)))
            .andExpect(status().isCreated());

        // Validate the Memorial in the database
        List<Memorial> memorialList = memorialRepository.findAll();
        assertThat(memorialList).hasSize(databaseSizeBeforeCreate + 1);
        Memorial testMemorial = memorialList.get(memorialList.size() - 1);
    }

    @Test
    @Transactional
    void createMemorialWithExistingId() throws Exception {
        // Create the Memorial with an existing ID
        memorial.setId(1L);
        MemorialDTO memorialDTO = memorialMapper.toDto(memorial);

        int databaseSizeBeforeCreate = memorialRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemorialMockMvc
            .perform(post("/api/memorials").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memorialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Memorial in the database
        List<Memorial> memorialList = memorialRepository.findAll();
        assertThat(memorialList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMemorials() throws Exception {
        // Initialize the database
        memorialRepository.saveAndFlush(memorial);

        // Get all the memorialList
        restMemorialMockMvc
            .perform(get("/api/memorials?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memorial.getId().intValue())));
    }

    @Test
    @Transactional
    void getMemorial() throws Exception {
        // Initialize the database
        memorialRepository.saveAndFlush(memorial);

        // Get the memorial
        restMemorialMockMvc
            .perform(get("/api/memorials/{id}", memorial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(memorial.getId().intValue()));
    }

    @Test
    @Transactional
    void getMemorialsByIdFiltering() throws Exception {
        // Initialize the database
        memorialRepository.saveAndFlush(memorial);

        Long id = memorial.getId();

        defaultMemorialShouldBeFound("id.equals=" + id);
        defaultMemorialShouldNotBeFound("id.notEquals=" + id);

        defaultMemorialShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMemorialShouldNotBeFound("id.greaterThan=" + id);

        defaultMemorialShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMemorialShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMemorialsByAnonymousCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        memorialRepository.saveAndFlush(memorial);
        Comment anonymousComment = CommentResourceIT.createEntity(em);
        em.persist(anonymousComment);
        em.flush();
        memorial.setAnonymousComment(anonymousComment);
        memorialRepository.saveAndFlush(memorial);
        Long anonymousCommentId = anonymousComment.getId();

        // Get all the memorialList where anonymousComment equals to anonymousCommentId
        defaultMemorialShouldBeFound("anonymousCommentId.equals=" + anonymousCommentId);

        // Get all the memorialList where anonymousComment equals to anonymousCommentId + 1
        defaultMemorialShouldNotBeFound("anonymousCommentId.equals=" + (anonymousCommentId + 1));
    }

    @Test
    @Transactional
    void getAllMemorialsByNotAnonymousCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        memorialRepository.saveAndFlush(memorial);
        Comment notAnonymousComment = CommentResourceIT.createEntity(em);
        em.persist(notAnonymousComment);
        em.flush();
        memorial.setNotAnonymousComment(notAnonymousComment);
        memorialRepository.saveAndFlush(memorial);
        Long notAnonymousCommentId = notAnonymousComment.getId();

        // Get all the memorialList where notAnonymousComment equals to notAnonymousCommentId
        defaultMemorialShouldBeFound("notAnonymousCommentId.equals=" + notAnonymousCommentId);

        // Get all the memorialList where notAnonymousComment equals to notAnonymousCommentId + 1
        defaultMemorialShouldNotBeFound("notAnonymousCommentId.equals=" + (notAnonymousCommentId + 1));
    }

    @Test
    @Transactional
    void getAllMemorialsByWriterIsEqualToSomething() throws Exception {
        // Initialize the database
        memorialRepository.saveAndFlush(memorial);
        UserPerDepartment writer = UserPerDepartmentResourceIT.createEntity(em);
        em.persist(writer);
        em.flush();
        memorial.setWriter(writer);
        memorialRepository.saveAndFlush(memorial);
        Long writerId = writer.getId();

        // Get all the memorialList where writer equals to writerId
        defaultMemorialShouldBeFound("writerId.equals=" + writerId);

        // Get all the memorialList where writer equals to writerId + 1
        defaultMemorialShouldNotBeFound("writerId.equals=" + (writerId + 1));
    }

    @Test
    @Transactional
    void getAllMemorialsByRecipientIsEqualToSomething() throws Exception {
        // Initialize the database
        memorialRepository.saveAndFlush(memorial);
        UserPerDepartment recipient = UserPerDepartmentResourceIT.createEntity(em);
        em.persist(recipient);
        em.flush();
        memorial.setRecipient(recipient);
        memorialRepository.saveAndFlush(memorial);
        Long recipientId = recipient.getId();

        // Get all the memorialList where recipient equals to recipientId
        defaultMemorialShouldBeFound("recipientId.equals=" + recipientId);

        // Get all the memorialList where recipient equals to recipientId + 1
        defaultMemorialShouldNotBeFound("recipientId.equals=" + (recipientId + 1));
    }

    @Test
    @Transactional
    void getAllMemorialsByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        memorialRepository.saveAndFlush(memorial);
        Department department = DepartmentResourceIT.createEntity(em);
        em.persist(department);
        em.flush();
        memorial.setDepartment(department);
        memorialRepository.saveAndFlush(memorial);
        Long departmentId = department.getId();

        // Get all the memorialList where department equals to departmentId
        defaultMemorialShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the memorialList where department equals to departmentId + 1
        defaultMemorialShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMemorialShouldBeFound(String filter) throws Exception {
        restMemorialMockMvc
            .perform(get("/api/memorials?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memorial.getId().intValue())));

        // Check, that the count call also returns 1
        restMemorialMockMvc
            .perform(get("/api/memorials/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMemorialShouldNotBeFound(String filter) throws Exception {
        restMemorialMockMvc
            .perform(get("/api/memorials?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMemorialMockMvc
            .perform(get("/api/memorials/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMemorial() throws Exception {
        // Get the memorial
        restMemorialMockMvc.perform(get("/api/memorials/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateMemorial() throws Exception {
        // Initialize the database
        memorialRepository.saveAndFlush(memorial);

        int databaseSizeBeforeUpdate = memorialRepository.findAll().size();

        // Update the memorial
        Memorial updatedMemorial = memorialRepository.findById(memorial.getId()).get();
        // Disconnect from session so that the updates on updatedMemorial are not directly saved in db
        em.detach(updatedMemorial);
        MemorialDTO memorialDTO = memorialMapper.toDto(updatedMemorial);

        restMemorialMockMvc
            .perform(put("/api/memorials").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memorialDTO)))
            .andExpect(status().isOk());

        // Validate the Memorial in the database
        List<Memorial> memorialList = memorialRepository.findAll();
        assertThat(memorialList).hasSize(databaseSizeBeforeUpdate);
        Memorial testMemorial = memorialList.get(memorialList.size() - 1);
    }

    @Test
    @Transactional
    void updateNonExistingMemorial() throws Exception {
        int databaseSizeBeforeUpdate = memorialRepository.findAll().size();

        // Create the Memorial
        MemorialDTO memorialDTO = memorialMapper.toDto(memorial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemorialMockMvc
            .perform(put("/api/memorials").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memorialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Memorial in the database
        List<Memorial> memorialList = memorialRepository.findAll();
        assertThat(memorialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMemorialWithPatch() throws Exception {
        // Initialize the database
        memorialRepository.saveAndFlush(memorial);

        int databaseSizeBeforeUpdate = memorialRepository.findAll().size();

        // Update the memorial using partial update
        Memorial partialUpdatedMemorial = new Memorial();
        partialUpdatedMemorial.setId(memorial.getId());

        restMemorialMockMvc
            .perform(
                patch("/api/memorials")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMemorial))
            )
            .andExpect(status().isOk());

        // Validate the Memorial in the database
        List<Memorial> memorialList = memorialRepository.findAll();
        assertThat(memorialList).hasSize(databaseSizeBeforeUpdate);
        Memorial testMemorial = memorialList.get(memorialList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateMemorialWithPatch() throws Exception {
        // Initialize the database
        memorialRepository.saveAndFlush(memorial);

        int databaseSizeBeforeUpdate = memorialRepository.findAll().size();

        // Update the memorial using partial update
        Memorial partialUpdatedMemorial = new Memorial();
        partialUpdatedMemorial.setId(memorial.getId());

        restMemorialMockMvc
            .perform(
                patch("/api/memorials")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMemorial))
            )
            .andExpect(status().isOk());

        // Validate the Memorial in the database
        List<Memorial> memorialList = memorialRepository.findAll();
        assertThat(memorialList).hasSize(databaseSizeBeforeUpdate);
        Memorial testMemorial = memorialList.get(memorialList.size() - 1);
    }

    @Test
    @Transactional
    void partialUpdateMemorialShouldThrown() throws Exception {
        // Update the memorial without id should throw
        Memorial partialUpdatedMemorial = new Memorial();

        restMemorialMockMvc
            .perform(
                patch("/api/memorials")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMemorial))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteMemorial() throws Exception {
        // Initialize the database
        memorialRepository.saveAndFlush(memorial);

        int databaseSizeBeforeDelete = memorialRepository.findAll().size();

        // Delete the memorial
        restMemorialMockMvc
            .perform(delete("/api/memorials/{id}", memorial.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Memorial> memorialList = memorialRepository.findAll();
        assertThat(memorialList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
