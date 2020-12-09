package edu.sharif.math.yaadbuzz.repository;

import edu.sharif.math.yaadbuzz.domain.Comment;
import edu.sharif.math.yaadbuzz.service.dto.CommentDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Comment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    @Query("select comment from Comment comment where comment.memory.id = :id")
    Page<Comment> findAllForMemory(@Param("id") Long id,Pageable pageable);

}
