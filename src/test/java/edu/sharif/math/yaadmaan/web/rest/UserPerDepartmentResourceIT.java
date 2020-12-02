package edu.sharif.math.yaadmaan.web.rest;

import edu.sharif.math.yaadmaan.YaadmaanApp;
import edu.sharif.math.yaadmaan.domain.UserPerDepartment;
import edu.sharif.math.yaadmaan.domain.User;
import edu.sharif.math.yaadmaan.domain.Department;
import edu.sharif.math.yaadmaan.repository.UserPerDepartmentRepository;
import edu.sharif.math.yaadmaan.service.UserPerDepartmentService;
import edu.sharif.math.yaadmaan.service.dto.UserPerDepartmentDTO;
import edu.sharif.math.yaadmaan.service.mapper.UserPerDepartmentMapper;

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
@SpringBootTest(classes = YaadmaanApp.class)
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
