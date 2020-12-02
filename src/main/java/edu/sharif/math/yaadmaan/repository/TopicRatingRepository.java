package edu.sharif.math.yaadmaan.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import edu.sharif.math.yaadmaan.domain.TopicRating;

/**
 * Spring Data  repository for the TopicRating entity.
 */
@Repository
public interface TopicRatingRepository extends JpaRepository<TopicRating, Long> {
}
