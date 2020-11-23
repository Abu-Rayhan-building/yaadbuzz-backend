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
import org.springframework.util.Base64Utils;

import edu.sharif.math.yaadmaan.YaadmaanApp;
import edu.sharif.math.yaadmaan.domain.Memory;
import edu.sharif.math.yaadmaan.domain.MemoryPicture;
import edu.sharif.math.yaadmaan.repository.MemoryPictureRepository;
import edu.sharif.math.yaadmaan.service.MemoryPictureService;
import edu.sharif.math.yaadmaan.service.dto.MemoryPictureDTO;
import edu.sharif.math.yaadmaan.service.mapper.MemoryPictureMapper;
import edu.sharif.math.yaadmaan.web.rest.MemoryPictureResource;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MemoryPictureResource} REST controller.
 */
@SpringBootTest(classes = YaadmaanApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MemoryPictureResourceIT {

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private MemoryPictureRepository memoryPictureRepository;

    @Autowired
    private MemoryPictureMapper memoryPictureMapper;

    @Autowired
    private MemoryPictureService memoryPictureService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMemoryPictureMockMvc;

    private MemoryPicture memoryPicture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemoryPicture createEntity(EntityManager em) {
        MemoryPicture memoryPicture = new MemoryPicture()
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        // Add required entity
        Memory memory;
        if (TestUtil.findAll(em, Memory.class).isEmpty()) {
            memory = MemoryResourceIT.createEntity(em);
            em.persist(memory);
            em.flush();
        } else {
            memory = TestUtil.findAll(em, Memory.class).get(0);
        }
        memoryPicture.setMemory(memory);
        return memoryPicture;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemoryPicture createUpdatedEntity(EntityManager em) {
        MemoryPicture memoryPicture = new MemoryPicture()
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        // Add required entity
        Memory memory;
        if (TestUtil.findAll(em, Memory.class).isEmpty()) {
            memory = MemoryResourceIT.createUpdatedEntity(em);
            em.persist(memory);
            em.flush();
        } else {
            memory = TestUtil.findAll(em, Memory.class).get(0);
        }
        memoryPicture.setMemory(memory);
        return memoryPicture;
    }

    @BeforeEach
    public void initTest() {
        memoryPicture = createEntity(em);
    }

    @Test
    @Transactional
    public void createMemoryPicture() throws Exception {
        int databaseSizeBeforeCreate = memoryPictureRepository.findAll().size();
        // Create the MemoryPicture
        MemoryPictureDTO memoryPictureDTO = memoryPictureMapper.toDto(memoryPicture);
        restMemoryPictureMockMvc.perform(post("/api/memory-pictures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memoryPictureDTO)))
            .andExpect(status().isCreated());

        // Validate the MemoryPicture in the database
        List<MemoryPicture> memoryPictureList = memoryPictureRepository.findAll();
        assertThat(memoryPictureList).hasSize(databaseSizeBeforeCreate + 1);
        MemoryPicture testMemoryPicture = memoryPictureList.get(memoryPictureList.size() - 1);
        assertThat(testMemoryPicture.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testMemoryPicture.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createMemoryPictureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = memoryPictureRepository.findAll().size();

        // Create the MemoryPicture with an existing ID
        memoryPicture.setId(1L);
        MemoryPictureDTO memoryPictureDTO = memoryPictureMapper.toDto(memoryPicture);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemoryPictureMockMvc.perform(post("/api/memory-pictures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memoryPictureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MemoryPicture in the database
        List<MemoryPicture> memoryPictureList = memoryPictureRepository.findAll();
        assertThat(memoryPictureList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMemoryPictures() throws Exception {
        // Initialize the database
        memoryPictureRepository.saveAndFlush(memoryPicture);

        // Get all the memoryPictureList
        restMemoryPictureMockMvc.perform(get("/api/memory-pictures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memoryPicture.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
    
    @Test
    @Transactional
    public void getMemoryPicture() throws Exception {
        // Initialize the database
        memoryPictureRepository.saveAndFlush(memoryPicture);

        // Get the memoryPicture
        restMemoryPictureMockMvc.perform(get("/api/memory-pictures/{id}", memoryPicture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(memoryPicture.getId().intValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }
    @Test
    @Transactional
    public void getNonExistingMemoryPicture() throws Exception {
        // Get the memoryPicture
        restMemoryPictureMockMvc.perform(get("/api/memory-pictures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMemoryPicture() throws Exception {
        // Initialize the database
        memoryPictureRepository.saveAndFlush(memoryPicture);

        int databaseSizeBeforeUpdate = memoryPictureRepository.findAll().size();

        // Update the memoryPicture
        MemoryPicture updatedMemoryPicture = memoryPictureRepository.findById(memoryPicture.getId()).get();
        // Disconnect from session so that the updates on updatedMemoryPicture are not directly saved in db
        em.detach(updatedMemoryPicture);
        updatedMemoryPicture
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        MemoryPictureDTO memoryPictureDTO = memoryPictureMapper.toDto(updatedMemoryPicture);

        restMemoryPictureMockMvc.perform(put("/api/memory-pictures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memoryPictureDTO)))
            .andExpect(status().isOk());

        // Validate the MemoryPicture in the database
        List<MemoryPicture> memoryPictureList = memoryPictureRepository.findAll();
        assertThat(memoryPictureList).hasSize(databaseSizeBeforeUpdate);
        MemoryPicture testMemoryPicture = memoryPictureList.get(memoryPictureList.size() - 1);
        assertThat(testMemoryPicture.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testMemoryPicture.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingMemoryPicture() throws Exception {
        int databaseSizeBeforeUpdate = memoryPictureRepository.findAll().size();

        // Create the MemoryPicture
        MemoryPictureDTO memoryPictureDTO = memoryPictureMapper.toDto(memoryPicture);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemoryPictureMockMvc.perform(put("/api/memory-pictures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memoryPictureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MemoryPicture in the database
        List<MemoryPicture> memoryPictureList = memoryPictureRepository.findAll();
        assertThat(memoryPictureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMemoryPicture() throws Exception {
        // Initialize the database
        memoryPictureRepository.saveAndFlush(memoryPicture);

        int databaseSizeBeforeDelete = memoryPictureRepository.findAll().size();

        // Delete the memoryPicture
        restMemoryPictureMockMvc.perform(delete("/api/memory-pictures/{id}", memoryPicture.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MemoryPicture> memoryPictureList = memoryPictureRepository.findAll();
        assertThat(memoryPictureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
