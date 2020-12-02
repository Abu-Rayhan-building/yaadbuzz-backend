package edu.sharif.math.yaadmaan.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import edu.sharif.math.yaadmaan.domain.Department;

import java.util.List;

/**
 * Spring Data  repository for the Department entity.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("select department from Department department where department.owner.login = ?#{principal.username}")
    List<Department> findByOwnerIsCurrentUser();
}
