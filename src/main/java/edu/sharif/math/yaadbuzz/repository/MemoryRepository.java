package edu.sharif.math.yaadbuzz.repository;

import edu.sharif.math.yaadbuzz.domain.Memory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.sharif.math.yaadbuzz.domain.Memory;
import edu.sharif.math.yaadbuzz.domain.UserPerDepartment;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Memory entity.
 */
@Repository
public interface MemoryRepository extends JpaRepository<Memory, Long>, JpaSpecificationExecutor<Memory> {

    @Query(value = "select distinct memory from Memory memory left join fetch memory.tageds",
        countQuery = "select count(distinct memory) from Memory memory")
    Page<Memory> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct memory from Memory memory left join fetch memory.tageds")
    List<Memory> findAllWithEagerRelationships();

    @Query("select memory from Memory memory left join fetch memory.tageds where memory.id =:id")
    Optional<Memory> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select memory from Memory memory where memory.department.id = :depid and (memory.isPrivate = false or :currentuserPerDepartmend MEMBER OF memory.tageds )")
    Page<Memory> findAllWithDepartmentId(Long depid,
	    UserPerDepartment currentuserPerDepartmend, Pageable pageable);

    @Query("select memory from Memory memory where memory.department.id = :depid and :userPerDepartmend MEMBER OF memory.tageds")
    Page<Memory> findAllWithCurrentUserTagedIn(Long depid,
	    UserPerDepartment userPerDepartmend, Pageable pageable);

    @Query("select memory from Memory memory where memory.department.id=:depid and :userPerDepartmend MEMBER OF memory.tageds and (memory.isPrivate = false or :currentuserPerDepartmend MEMBER OF memory.tageds)")
    Page<Memory> findAllWithUserTagedIn(Long depid,
	    UserPerDepartment currentuserPerDepartmend,
	    UserPerDepartment userPerDepartmend, Pageable pageable);
}
