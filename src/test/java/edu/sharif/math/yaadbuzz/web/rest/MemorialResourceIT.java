package edu.sharif.math.yaadbuzz.web.rest;

import edu.sharif.math.yaadbuzz.YaadbuzzBackendApp;
import edu.sharif.math.yaadbuzz.domain.Memorial;
import edu.sharif.math.yaadbuzz.domain.Comment;
import edu.sharif.math.yaadbuzz.domain.UserPerDepartment;
import edu.sharif.math.yaadbuzz.repository.MemorialRepository;
import edu.sharif.math.yaadbuzz.service.MemorialService;
import edu.sharif.math.yaadbuzz.service.dto.MemorialDTO;
import edu.sharif.math.yaadbuzz.service.mapper.MemorialMapper;
import edu.sharif.math.yaadbuzz.service.dto.MemorialCriteria;
import edu.sharif.math.yaadbuzz.service.MemorialQueryService;

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
 * Integration tests for the {@link MemorialResource} REST controller.
 */
@SpringBootTest(classes = YaadbuzzBackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MemorialResourceIT {

    @Autowired
    private MemorialRepository memorialRepository;

    @Autowired
    private MemorialMapper memorialMapper;

    @Autowired
    private MemorialService memorialService;

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
        return memorial;
    }

    @BeforeEach
    public void initTest() {
        memorial = createEntity(em);
    }

    @Test
    @Transactional
    public void createMemorial() throws Exception {
        int databaseSizeBeforeCreate = memorialRepository.findAll().size();
        // Create the Memorial
        MemorialDTO memorialDTO = memorialMapper.toDto(memorial);
        restMemorialMockMvc.perform(post("/api/memorials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memorialDTO)))
            .andExpect(status().isCreated());

        // Validate the Memorial in the database
        List<Memorial> memorialList = memorialRepository.findAll();
        assertThat(memorialList).hasSize(databaseSizeBeforeCreate + 1);
        Memorial testMemorial = memorialList.get(memorialList.size() - 1);
    }

    @Test
    @Transactional
    public void createMemorialWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = memorialRepository.findAll().size();

        // Create the Memorial with an existing ID
        memorial.setId(1L);
        MemorialDTO memorialDTO = memorialMapper.toDto(memorial);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemorialMockMvc.perform(post("/api/memorials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memorialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Memorial in the database
        List<Memorial> memorialList = memorialRepository.findAll();
        assertThat(memorialList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMemorials() throws Exception {
        // Initialize the database
        memorialRepository.saveAndFlush(memorial);

        // Get all the memorialList
        restMemorialMockMvc.perform(get("/api/memorials?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memorial.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getMemorial() throws Exception {
        // Initialize the database
        memorialRepository.saveAndFlush(memorial);

        // Get the memorial
        restMemorialMockMvc.perform(get("/api/memorials/{id}", memorial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(memorial.getId().intValue()));
    }


    @Test
    @Transactional
    public void getMemorialsByIdFiltering() throws Exception {
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
    public void getAllMemorialsByAnonymousCommentIsEqualToSomething() throws Exception {
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
    public void getAllMemorialsByNotAnonymousCommentIsEqualToSomething() throws Exception {
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
    public void getAllMemorialsByWriterIsEqualToSomething() throws Exception {
        // Get already existing entity
        UserPerDepartment writer = memorial.getWriter();
        memorialRepository.saveAndFlush(memorial);
        Long writerId = writer.getId();

        // Get all the memorialList where writer equals to writerId
        defaultMemorialShouldBeFound("writerId.equals=" + writerId);

        // Get all the memorialList where writer equals to writerId + 1
        defaultMemorialShouldNotBeFound("writerId.equals=" + (writerId + 1));
    }


    @Test
    @Transactional
    public void getAllMemorialsByRecipientIsEqualToSomething() throws Exception {
        // Get already existing entity
        UserPerDepartment recipient = memorial.getRecipient();
        memorialRepository.saveAndFlush(memorial);
        Long recipientId = recipient.getId();

        // Get all the memorialList where recipient equals to recipientId
        defaultMemorialShouldBeFound("recipientId.equals=" + recipientId);

        // Get all the memorialList where recipient equals to recipientId + 1
        defaultMemorialShouldNotBeFound("recipientId.equals=" + (recipientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMemorialShouldBeFound(String filter) throws Exception {
        restMemorialMockMvc.perform(get("/api/memorials?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memorial.getId().intValue())));

        // Check, that the count call also returns 1
        restMemorialMockMvc.perform(get("/api/memorials/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMemorialShouldNotBeFound(String filter) throws Exception {
        restMemorialMockMvc.perform(get("/api/memorials?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMemorialMockMvc.perform(get("/api/memorials/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingMemorial() throws Exception {
        // Get the memorial
        restMemorialMockMvc.perform(get("/api/memorials/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMemorial() throws Exception {
        // Initialize the database
        memorialRepository.saveAndFlush(memorial);

        int databaseSizeBeforeUpdate = memorialRepository.findAll().size();

        // Update the memorial
        Memorial updatedMemorial = memorialRepository.findById(memorial.getId()).get();
        // Disconnect from session so that the updates on updatedMemorial are not directly saved in db
        em.detach(updatedMemorial);
        MemorialDTO memorialDTO = memorialMapper.toDto(updatedMemorial);

        restMemorialMockMvc.perform(put("/api/memorials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memorialDTO)))
            .andExpect(status().isOk());

        // Validate the Memorial in the database
        List<Memorial> memorialList = memorialRepository.findAll();
        assertThat(memorialList).hasSize(databaseSizeBeforeUpdate);
        Memorial testMemorial = memorialList.get(memorialList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingMemorial() throws Exception {
        int databaseSizeBeforeUpdate = memorialRepository.findAll().size();

        // Create the Memorial
        MemorialDTO memorialDTO = memorialMapper.toDto(memorial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemorialMockMvc.perform(put("/api/memorials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memorialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Memorial in the database
        List<Memorial> memorialList = memorialRepository.findAll();
        assertThat(memorialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMemorial() throws Exception {
        // Initialize the database
        memorialRepository.saveAndFlush(memorial);

        int databaseSizeBeforeDelete = memorialRepository.findAll().size();

        // Delete the memorial
        restMemorialMockMvc.perform(delete("/api/memorials/{id}", memorial.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Memorial> memorialList = memorialRepository.findAll();
        assertThat(memorialList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
