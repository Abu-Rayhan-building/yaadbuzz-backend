package edu.sharif.math.yaadbuzz.repository;

import edu.sharif.math.yaadbuzz.domain.TopicRating;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TopicRating entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TopicRatingRepository extends JpaRepository<TopicRating, Long>, JpaSpecificationExecutor<TopicRating> {
}
