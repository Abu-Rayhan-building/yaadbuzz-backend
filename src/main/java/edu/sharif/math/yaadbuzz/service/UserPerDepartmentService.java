package edu.sharif.math.yaadbuzz.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;
import edu.sharif.math.yaadbuzz.web.rest.dto.MyUserPerDepartmentStatsDTO;

import java.util.Optional;

/**
 * Service Interface for managing {@link edu.sharif.math.yaadbuzz.domain.UserPerDepartment}.
 */
public interface UserPerDepartmentService extends ServiceWithCurrentUserCrudAccess {

    /**
     * Save a userPerDepartment.
     *
     * @param userPerDepartmentDTO the entity to save.
     * @return the persisted entity.
     */
    UserPerDepartmentDTO save(UserPerDepartmentDTO userPerDepartmentDTO);

    /**
     * Partially updates a userPerDepartment.
     *
     * @param userPerDepartmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserPerDepartmentDTO> partialUpdate(UserPerDepartmentDTO userPerDepartmentDTO);

    /**
     * Get all the userPerDepartments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserPerDepartmentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" userPerDepartment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserPerDepartmentDTO> findOne(Long id);

    /**
     * Delete the "id" userPerDepartment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    UserPerDepartmentDTO getCurrentUserInDep(Long depId);

    Long getCurrentUserUserPerDepeartmentIdInDep(Long depId) ;
    
    MyUserPerDepartmentStatsDTO getCurrentUserStatsInDep(Long depId);

    UserPerDepartmentDTO getCurrentUserUserPerDepeartmentInDep(Long depId);
}
