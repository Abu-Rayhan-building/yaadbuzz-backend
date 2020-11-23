package edu.sharif.math.yaadmaan.web.rest;

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

import edu.sharif.math.yaadmaan.YaadmaanApp;
import edu.sharif.math.yaadmaan.domain.Comment;
import edu.sharif.math.yaadmaan.domain.Department;
import edu.sharif.math.yaadmaan.domain.Memory;
import edu.sharif.math.yaadmaan.domain.UserPerDepartment;
import edu.sharif.math.yaadmaan.repository.MemoryRepository;
import edu.sharif.math.yaadmaan.service.MemoryService;
import edu.sharif.math.yaadmaan.service.dto.MemoryDTO;
import edu.sharif.math.yaadmaan.service.mapper.MemoryMapper;
import edu.sharif.math.yaadmaan.web.rest.MemoryResource;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MemoryResource} REST controller.
 */
@SpringBootTest(classes = YaadmaanApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class MemoryResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PRIVATE = false;
    private static final Boolean UPDATED_IS_PRIVATE = true;

    private static final Boolean DEFAULT_IS_ANNONYMOS = false;
    private static final Boolean UPDATED_IS_ANNONYMOS = true;

    @Autowired
    private MemoryRepository memoryRepository;

    @Mock
    private MemoryRepository memoryRepositoryMock;

    @Autowired
    private MemoryMapper memoryMapper;

    @Mock
    private MemoryService memoryServiceMock;

    @Autowired
    private MemoryService memoryService;

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
        Memory memory = new Memory()
            .title(DEFAULT_TITLE)
            .isPrivate(DEFAULT_IS_PRIVATE)
            .isAnnonymos(DEFAULT_IS_ANNONYMOS);
        // Add required entity
        Comment comment;
        if (TestUtil.findAll(em, Comment.class).isEmpty()) {
            comment = CommentResourceIT.createEntity(em);
            em.persist(comment);
            em.flush();
        } else {
            comment = TestUtil.findAll(em, Comment.class).get(0);
        }
        memory.setText(comment);
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
        Memory memory = new Memory()
            .title(UPDATED_TITLE)
            .isPrivate(UPDATED_IS_PRIVATE)
            .isAnnonymos(UPDATED_IS_ANNONYMOS);
        // Add required entity
        Comment comment;
        if (TestUtil.findAll(em, Comment.class).isEmpty()) {
            comment = CommentResourceIT.createUpdatedEntity(em);
            em.persist(comment);
            em.flush();
        } else {
            comment = TestUtil.findAll(em, Comment.class).get(0);
        }
        memory.setText(comment);
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
    public void createMemory() throws Exception {
        int databaseSizeBeforeCreate = memoryRepository.findAll().size();
        // Create the Memory
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);
        restMemoryMockMvc.perform(post("/api/memories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memoryDTO)))
            .andExpect(status().isCreated());

        // Validate the Memory in the database
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeCreate + 1);
        Memory testMemory = memoryList.get(memoryList.size() - 1);
        assertThat(testMemory.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMemory.isIsPrivate()).isEqualTo(DEFAULT_IS_PRIVATE);
        assertThat(testMemory.isIsAnnonymos()).isEqualTo(DEFAULT_IS_ANNONYMOS);
    }

    @Test
    @Transactional
    public void createMemoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = memoryRepository.findAll().size();

        // Create the Memory with an existing ID
        memory.setId(1L);
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemoryMockMvc.perform(post("/api/memories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Memory in the database
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMemories() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        // Get all the memoryList
        restMemoryMockMvc.perform(get("/api/memories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memory.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].isPrivate").value(hasItem(DEFAULT_IS_PRIVATE.booleanValue())))
            .andExpect(jsonPath("$.[*].isAnnonymos").value(hasItem(DEFAULT_IS_ANNONYMOS.booleanValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllMemoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(memoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMemoryMockMvc.perform(get("/api/memories?eagerload=true"))
            .andExpect(status().isOk());

        verify(memoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllMemoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(memoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMemoryMockMvc.perform(get("/api/memories?eagerload=true"))
            .andExpect(status().isOk());

        verify(memoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getMemory() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        // Get the memory
        restMemoryMockMvc.perform(get("/api/memories/{id}", memory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(memory.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.isPrivate").value(DEFAULT_IS_PRIVATE.booleanValue()))
            .andExpect(jsonPath("$.isAnnonymos").value(DEFAULT_IS_ANNONYMOS.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingMemory() throws Exception {
        // Get the memory
        restMemoryMockMvc.perform(get("/api/memories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMemory() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        int databaseSizeBeforeUpdate = memoryRepository.findAll().size();

        // Update the memory
        Memory updatedMemory = memoryRepository.findById(memory.getId()).get();
        // Disconnect from session so that the updates on updatedMemory are not directly saved in db
        em.detach(updatedMemory);
        updatedMemory
            .title(UPDATED_TITLE)
            .isPrivate(UPDATED_IS_PRIVATE)
            .isAnnonymos(UPDATED_IS_ANNONYMOS);
        MemoryDTO memoryDTO = memoryMapper.toDto(updatedMemory);

        restMemoryMockMvc.perform(put("/api/memories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memoryDTO)))
            .andExpect(status().isOk());

        // Validate the Memory in the database
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeUpdate);
        Memory testMemory = memoryList.get(memoryList.size() - 1);
        assertThat(testMemory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMemory.isIsPrivate()).isEqualTo(UPDATED_IS_PRIVATE);
        assertThat(testMemory.isIsAnnonymos()).isEqualTo(UPDATED_IS_ANNONYMOS);
    }

    @Test
    @Transactional
    public void updateNonExistingMemory() throws Exception {
        int databaseSizeBeforeUpdate = memoryRepository.findAll().size();

        // Create the Memory
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemoryMockMvc.perform(put("/api/memories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Memory in the database
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMemory() throws Exception {
        // Initialize the database
        memoryRepository.saveAndFlush(memory);

        int databaseSizeBeforeDelete = memoryRepository.findAll().size();

        // Delete the memory
        restMemoryMockMvc.perform(delete("/api/memories/{id}", memory.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Memory> memoryList = memoryRepository.findAll();
        assertThat(memoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
