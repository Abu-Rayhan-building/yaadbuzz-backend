package edu.sharif.math.yaadmaan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.sharif.math.yaadmaan.domain.Topic;
import edu.sharif.math.yaadmaan.domain.UserPerDepartment;
import edu.sharif.math.yaadmaan.service.dto.UserPerDepartmentDTO;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for the UserPerDepartment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserPerDepartmentRepository
	extends JpaRepository<UserPerDepartment, Long> {

    @Query("select userPerDepartment from UserPerDepartment userPerDepartment where userPerDepartment.realUser.login = ?#{principal.username}")
    List<UserPerDepartment> findByRealUserIsCurrentUser();

    @Query("select userPerDepartment from UserPerDepartment userPerDepartment where userPerDepartment.department.id = :id")
    Page<UserPerDepartment> findByDepatment(@Param("id") Long id,
	    Pageable pageable);

    @Query("select userPerDepartment from UserPerDepartment userPerDepartment where userPerDepartment.department.id = :depid and userPerDepartment.realUser.login = ?#{principal.username}")
    UserPerDepartment getCurrentUserInDep(Long depid);

}
