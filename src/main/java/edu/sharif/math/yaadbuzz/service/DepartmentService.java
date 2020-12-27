package edu.sharif.math.yaadbuzz.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import edu.sharif.math.yaadbuzz.service.dto.DepartmentDTO;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;
import edu.sharif.math.yaadbuzz.web.rest.dto.MyUserPerDepartmentStatsDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link edu.sharif.math.yaadbuzz.domain.Department}.
 */
public interface DepartmentService extends ServiceWithCurrentUserCrudAccess{

    /**
     * Save a department.
     *
     * @param departmentDTO the entity to save.
     * @return the persisted entity.
     */
    DepartmentDTO save(DepartmentDTO departmentDTO);

    /**
     * Partially updates a department.
     *
     * @param departmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DepartmentDTO> partialUpdate(DepartmentDTO departmentDTO);

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

    MyUserPerDepartmentStatsDTO getMyStatsInDep(Long depId);

    List<UserPerDepartmentDTO> getAllDepartmentUsers(Long depid);
}
