package edu.sharif.math.yaadbuzz.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.sharif.math.yaadbuzz.IntegrationTest;
import edu.sharif.math.yaadbuzz.domain.Comment;
import edu.sharif.math.yaadbuzz.domain.Department;
import edu.sharif.math.yaadbuzz.domain.Memory;
import edu.sharif.math.yaadbuzz.domain.UserPerDepartment;
import edu.sharif.math.yaadbuzz.repository.MemoryRepository;
import edu.sharif.math.yaadbuzz.service.MemoryQueryService;
import edu.sharif.math.yaadbuzz.service.MemoryService;
import edu.sharif.math.yaadbuzz.service.dto.MemoryCriteria;
import edu.sharif.math.yaadbuzz.service.dto.MemoryDTO;
import edu.sharif.math.yaadbuzz.service.mapper.MemoryMapper;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MemoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MemoryResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PRIVATE = false;
    private static final Boolean UPDATED_IS_PRIVATE = true;

    @Autowired
    private MemoryRepository memoryRepository;

    @Mock
    private MemoryRepository memoryRepositoryMock;

    @Autowired
    private MemoryMapper memoryMapper;

    @Mock
    private MemoryService memoryServiceMock;

    @Autowired
    private MemoryQueryService memoryQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMemoryMockMvc;

    private Memory memory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Memory createEntity(EntityManager em) {
        Memory memory = new Memory().title(DEFAULT_TITLE).isPrivate(DEFAULT_IS_PRIVATE);
        // Add required entity
        Comment comment;
        if (TestUtil.findAll(em, Comment.class).isEmpty()) {
            comment = CommentResourceIT.createEntity(em);
            em.persist(comment);
            em.flush();
        } else {
            comment = TestUtil.findAll(em, Comment.class).get(0);
        }
        memory.setBaseComment(comment);
        // Add required entity
        UserPerDepartment userPerDepartment;
        if (TestUtil.findAll(em, UserPerDepartment.class).isEmpty()) {
            userPerDepartment = UserPerDepartmentResourceIT.createEntity(em);
            em.persist(userPerDepartment);
            em.flush();
        } else {
            userPerDepartment = TestUtil.findAll(em, UserPerDepartment.class).get(0);
        }
        memory.setWriter(userPerDepartment);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        memory.setDepartment(department);
        return memory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Memory createUpdatedEntity(EntityManager em) {
        Memory memory = new Memory().title(UPDATED_TITLE).isPrivate(UPDATED_IS_PRIVATE);
        // Add required entity
        Comment comment;
        if (TestUtil.findAll(em, Comment.class).isEmpty()) {
            comment = CommentResourceIT.createUpdatedEntity(em);
            em.persist(comment);
            em.flush();
        } else {
            comment = TestUtil.findAll(em, Comment.class).get(0);
        }
        memory.setBaseComment(comment);
        // Add required entity
        UserPerDepartment userPerDepartment;
        if (TestUtil.findAll(em, UserPerDepartment.class).isEmpty()) {
            userPerDepartment = UserPerDepartmentResourceIT.createUpdatedEntity(em);
            em.persist(userPerDepartment);
            em.flush();
        } else {
            userPerDepartment = TestUtil.findAll(em, UserPerDepartment.class).get(0);
        }
        memory.setWriter(userPerDepartment);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createUpdatedEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        memory.setDepartment(department);
        return memory;
    }

    @BeforeEach
    public void initTest() {
        memory = createEntity(em);
    }

    @Test
    @Transactional
    void createMemory() throws Exception {
        int databaseSizeBeforeCreate = memoryRepository.findAll().size();
        // Create the Memory
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);
        restMemoryMockMvc
            .perform(post("/api/memories").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memoryDTO)))
            .andExpect(status().isCreated());

        // Validate the Memory in the database
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeCreate + 1);
        Memory testMemory = memoryList.get(memoryList.size() - 1);
        assertThat(testMemory.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMemory.getIsPrivate()).isEqualTo(DEFAULT_IS_PRIVATE);
    }

    @Test
    @Transactional
    void createMemoryWithExistingId() throws Exception {
        // Create the Memory with an existing ID
        memory.setId(1L);
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);

        int databaseSizeBeforeCreate = memoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemoryMockMvc
            .perform(post("/api/memories").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Memory in the database
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMemories() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        // Get all the memoryList
        restMemoryMockMvc
            .perform(get("/api/memories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memory.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].isPrivate").value(hasItem(DEFAULT_IS_PRIVATE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMemoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(memoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMemoryMockMvc.perform(get("/api/memories?eagerload=true")).andExpect(status().isOk());

        verify(memoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMemoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(memoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMemoryMockMvc.perform(get("/api/memories?eagerload=true")).andExpect(status().isOk());

        verify(memoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getMemory() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        // Get the memory
        restMemoryMockMvc
            .perform(get("/api/memories/{id}", memory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(memory.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.isPrivate").value(DEFAULT_IS_PRIVATE.booleanValue()));
    }

    @Test
    @Transactional
    void getMemoriesByIdFiltering() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        Long id = memory.getId();

        defaultMemoryShouldBeFound("id.equals=" + id);
        defaultMemoryShouldNotBeFound("id.notEquals=" + id);

        defaultMemoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMemoryShouldNotBeFound("id.greaterThan=" + id);

        defaultMemoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMemoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMemoriesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        // Get all the memoryList where title equals to DEFAULT_TITLE
        defaultMemoryShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the memoryList where title equals to UPDATED_TITLE
        defaultMemoryShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMemoriesByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        // Get all the memoryList where title not equals to DEFAULT_TITLE
        defaultMemoryShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the memoryList where title not equals to UPDATED_TITLE
        defaultMemoryShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMemoriesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        // Get all the memoryList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultMemoryShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the memoryList where title equals to UPDATED_TITLE
        defaultMemoryShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMemoriesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        // Get all the memoryList where title is not null
        defaultMemoryShouldBeFound("title.specified=true");

        // Get all the memoryList where title is null
        defaultMemoryShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllMemoriesByTitleContainsSomething() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        // Get all the memoryList where title contains DEFAULT_TITLE
        defaultMemoryShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the memoryList where title contains UPDATED_TITLE
        defaultMemoryShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMemoriesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        // Get all the memoryList where title does not contain DEFAULT_TITLE
        defaultMemoryShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the memoryList where title does not contain UPDATED_TITLE
        defaultMemoryShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMemoriesByIsPrivateIsEqualToSomething() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        // Get all the memoryList where isPrivate equals to DEFAULT_IS_PRIVATE
        defaultMemoryShouldBeFound("isPrivate.equals=" + DEFAULT_IS_PRIVATE);

        // Get all the memoryList where isPrivate equals to UPDATED_IS_PRIVATE
        defaultMemoryShouldNotBeFound("isPrivate.equals=" + UPDATED_IS_PRIVATE);
    }

    @Test
    @Transactional
    void getAllMemoriesByIsPrivateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        // Get all the memoryList where isPrivate not equals to DEFAULT_IS_PRIVATE
        defaultMemoryShouldNotBeFound("isPrivate.notEquals=" + DEFAULT_IS_PRIVATE);

        // Get all the memoryList where isPrivate not equals to UPDATED_IS_PRIVATE
        defaultMemoryShouldBeFound("isPrivate.notEquals=" + UPDATED_IS_PRIVATE);
    }

    @Test
    @Transactional
    void getAllMemoriesByIsPrivateIsInShouldWork() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        // Get all the memoryList where isPrivate in DEFAULT_IS_PRIVATE or UPDATED_IS_PRIVATE
        defaultMemoryShouldBeFound("isPrivate.in=" + DEFAULT_IS_PRIVATE + "," + UPDATED_IS_PRIVATE);

        // Get all the memoryList where isPrivate equals to UPDATED_IS_PRIVATE
        defaultMemoryShouldNotBeFound("isPrivate.in=" + UPDATED_IS_PRIVATE);
    }

    @Test
    @Transactional
    void getAllMemoriesByIsPrivateIsNullOrNotNull() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        // Get all the memoryList where isPrivate is not null
        defaultMemoryShouldBeFound("isPrivate.specified=true");

        // Get all the memoryList where isPrivate is null
        defaultMemoryShouldNotBeFound("isPrivate.specified=false");
    }

    @Test
    @Transactional
    void getAllMemoriesByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);
        Comment comments = CommentResourceIT.createEntity(em);
        em.persist(comments);
        em.flush();
        memory.addComments(comments);
        memoryRepository.saveAndFlush(memory);
        Long commentsId = comments.getId();

        // Get all the memoryList where comments equals to commentsId
        defaultMemoryShouldBeFound("commentsId.equals=" + commentsId);

        // Get all the memoryList where comments equals to commentsId + 1
        defaultMemoryShouldNotBeFound("commentsId.equals=" + (commentsId + 1));
    }

    @Test
    @Transactional
    void getAllMemoriesByBaseCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);
        Comment baseComment = CommentResourceIT.createEntity(em);
        em.persist(baseComment);
        em.flush();
        memory.setBaseComment(baseComment);
        memoryRepository.saveAndFlush(memory);
        Long baseCommentId = baseComment.getId();

        // Get all the memoryList where baseComment equals to baseCommentId
        defaultMemoryShouldBeFound("baseCommentId.equals=" + baseCommentId);

        // Get all the memoryList where baseComment equals to baseCommentId + 1
        defaultMemoryShouldNotBeFound("baseCommentId.equals=" + (baseCommentId + 1));
    }

    @Test
    @Transactional
    void getAllMemoriesByWriterIsEqualToSomething() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);
        UserPerDepartment writer = UserPerDepartmentResourceIT.createEntity(em);
        em.persist(writer);
        em.flush();
        memory.setWriter(writer);
        memoryRepository.saveAndFlush(memory);
        Long writerId = writer.getId();

        // Get all the memoryList where writer equals to writerId
        defaultMemoryShouldBeFound("writerId.equals=" + writerId);

        // Get all the memoryList where writer equals to writerId + 1
        defaultMemoryShouldNotBeFound("writerId.equals=" + (writerId + 1));
    }

    @Test
    @Transactional
    void getAllMemoriesByTagedIsEqualToSomething() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);
        UserPerDepartment taged = UserPerDepartmentResourceIT.createEntity(em);
        em.persist(taged);
        em.flush();
        memory.addTaged(taged);
        memoryRepository.saveAndFlush(memory);
        Long tagedId = taged.getId();

        // Get all the memoryList where taged equals to tagedId
        defaultMemoryShouldBeFound("tagedId.equals=" + tagedId);

        // Get all the memoryList where taged equals to tagedId + 1
        defaultMemoryShouldNotBeFound("tagedId.equals=" + (tagedId + 1));
    }

    @Test
    @Transactional
    void getAllMemoriesByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);
        Department department = DepartmentResourceIT.createEntity(em);
        em.persist(department);
        em.flush();
        memory.setDepartment(department);
        memoryRepository.saveAndFlush(memory);
        Long departmentId = department.getId();

        // Get all the memoryList where department equals to departmentId
        defaultMemoryShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the memoryList where department equals to departmentId + 1
        defaultMemoryShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMemoryShouldBeFound(String filter) throws Exception {
        restMemoryMockMvc
            .perform(get("/api/memories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memory.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].isPrivate").value(hasItem(DEFAULT_IS_PRIVATE.booleanValue())));

        // Check, that the count call also returns 1
        restMemoryMockMvc
            .perform(get("/api/memories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMemoryShouldNotBeFound(String filter) throws Exception {
        restMemoryMockMvc
            .perform(get("/api/memories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMemoryMockMvc
            .perform(get("/api/memories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMemory() throws Exception {
        // Get the memory
        restMemoryMockMvc.perform(get("/api/memories/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateMemory() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        int databaseSizeBeforeUpdate = memoryRepository.findAll().size();

        // Update the memory
        Memory updatedMemory = memoryRepository.findById(memory.getId()).get();
        // Disconnect from session so that the updates on updatedMemory are not directly saved in db
        em.detach(updatedMemory);
        updatedMemory.title(UPDATED_TITLE).isPrivate(UPDATED_IS_PRIVATE);
        MemoryDTO memoryDTO = memoryMapper.toDto(updatedMemory);

        restMemoryMockMvc
            .perform(put("/api/memories").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memoryDTO)))
            .andExpect(status().isOk());

        // Validate the Memory in the database
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeUpdate);
        Memory testMemory = memoryList.get(memoryList.size() - 1);
        assertThat(testMemory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMemory.getIsPrivate()).isEqualTo(UPDATED_IS_PRIVATE);
    }

    @Test
    @Transactional
    void updateNonExistingMemory() throws Exception {
        int databaseSizeBeforeUpdate = memoryRepository.findAll().size();

        // Create the Memory
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemoryMockMvc
            .perform(put("/api/memories").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(memoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Memory in the database
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMemoryWithPatch() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        int databaseSizeBeforeUpdate = memoryRepository.findAll().size();

        // Update the memory using partial update
        Memory partialUpdatedMemory = new Memory();
        partialUpdatedMemory.setId(memory.getId());

        partialUpdatedMemory.title(UPDATED_TITLE).isPrivate(UPDATED_IS_PRIVATE);

        restMemoryMockMvc
            .perform(
                patch("/api/memories")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMemory))
            )
            .andExpect(status().isOk());

        // Validate the Memory in the database
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeUpdate);
        Memory testMemory = memoryList.get(memoryList.size() - 1);
        assertThat(testMemory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMemory.getIsPrivate()).isEqualTo(UPDATED_IS_PRIVATE);
    }

    @Test
    @Transactional
    void fullUpdateMemoryWithPatch() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        int databaseSizeBeforeUpdate = memoryRepository.findAll().size();

        // Update the memory using partial update
        Memory partialUpdatedMemory = new Memory();
        partialUpdatedMemory.setId(memory.getId());

        partialUpdatedMemory.title(UPDATED_TITLE).isPrivate(UPDATED_IS_PRIVATE);

        restMemoryMockMvc
            .perform(
                patch("/api/memories")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMemory))
            )
            .andExpect(status().isOk());

        // Validate the Memory in the database
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeUpdate);
        Memory testMemory = memoryList.get(memoryList.size() - 1);
        assertThat(testMemory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMemory.getIsPrivate()).isEqualTo(UPDATED_IS_PRIVATE);
    }

    @Test
    @Transactional
    void partialUpdateMemoryShouldThrown() throws Exception {
        // Update the memory without id should throw
        Memory partialUpdatedMemory = new Memory();

        restMemoryMockMvc
            .perform(
                patch("/api/memories")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMemory))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteMemory() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        int databaseSizeBeforeDelete = memoryRepository.findAll().size();

        // Delete the memory
        restMemoryMockMvc
            .perform(delete("/api/memories/{id}", memory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
