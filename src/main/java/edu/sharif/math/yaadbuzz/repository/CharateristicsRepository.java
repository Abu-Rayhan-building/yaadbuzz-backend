package edu.sharif.math.yaadbuzz.repository;

import edu.sharif.math.yaadbuzz.domain.Charateristics;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.sharif.math.yaadbuzz.domain.Charateristics;
import edu.sharif.math.yaadbuzz.domain.Comment;
import edu.sharif.math.yaadbuzz.service.dto.CharateristicsDTO;

/**
 * Spring Data  repository for the Charateristics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CharateristicsRepository
	extends JpaRepository<Charateristics, Long>, JpaSpecificationExecutor<Charateristics> {

    @Query("select charateristics from Charateristics charateristics where charateristics.userPerDepartment.id = :updId")
    Page<Charateristics> findUsersCharactersInDep(@Param("updId") Long updId,
	    Pageable pageable);

    @Query("select charateristics from Charateristics charateristics where charateristics.userPerDepartment.id = :updId and charateristics.title = :title")
    Optional<Charateristics> findOne(@Param("updId") Long userPerDepartmentId,@Param("title") String title);
}
