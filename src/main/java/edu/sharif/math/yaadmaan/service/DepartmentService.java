package edu.sharif.math.yaadmaan.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import edu.sharif.math.yaadmaan.service.dto.DepartmentDTO;
import edu.sharif.math.yaadmaan.service.dto.UserPerDepartmentDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link edu.sharif.math.yaadmaan.domain.Department}.
 */
public interface DepartmentService {

    /**
     * Save a department.
     *
     * @param departmentDTO the entity to save.
     * @return the persisted entity.
     */
    DepartmentDTO save(DepartmentDTO departmentDTO);

    /**
     * Get all the departments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepartmentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" department.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DepartmentDTO> findOne(Long id);

    /**
     * Delete the "id" department.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    DepartmentDTO join(Long departmentId, String password,
	    UserPerDepartmentDTO u);

    List<DepartmentDTO> getMyDeps();

    Page<UserPerDepartmentDTO> getDepartmentUsers(Long id, Pageable pageable);
}
