package edu.sharif.math.yaadmaan.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import edu.sharif.math.yaadmaan.domain.Comment;

/**
 * Spring Data  repository for the Comment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
