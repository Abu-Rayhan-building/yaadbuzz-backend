package edu.sharif.math.yaadmaan.repository;

import edu.sharif.math.yaadmaan.domain.Department;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {

    @Query("select department from Department department where department.owner.login = ?#{principal.username}")
    List<Department> findByOwnerIsCurrentUser();
}
