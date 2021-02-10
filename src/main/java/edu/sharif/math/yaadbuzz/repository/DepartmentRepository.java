package edu.sharif.math.yaadbuzz.repository;

import edu.sharif.math.yaadbuzz.domain.Department;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {
    @Query("select department from Department department where department.owner.login = ?#{principal.username}")
    List<Department> findByOwnerIsCurrentUser();
}
