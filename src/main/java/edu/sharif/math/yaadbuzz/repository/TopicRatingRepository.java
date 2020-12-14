package edu.sharif.math.yaadbuzz.repository;

import edu.sharif.math.yaadbuzz.domain.TopicRating;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the TopicRating entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TopicRatingRepository extends JpaRepository<TopicRating, Long>,
	JpaSpecificationExecutor<TopicRating> {

    @Query("select topicRating from TopicRating topicRating where topicRating.topic.id =:topicId and topicRating.user.id = :ballotForUPD")
    Optional<TopicRating> findOne(Long topicId, Long ballotForUPD);
}
