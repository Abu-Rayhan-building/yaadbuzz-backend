package edu.sharif.math.yaadbuzz.repository;

import edu.sharif.math.yaadbuzz.domain.TopicVote;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the TopicVote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TopicVoteRepository extends JpaRepository<TopicVote, Long>,
	JpaSpecificationExecutor<TopicVote> {
    @Query("select topicRating from TopicVote topicRating where topicRating.topic.id =:topicId and topicRating.user.id = :ballotForUPD")
    Optional<TopicVote> findOne(Long topicId, Long ballotForUPD);

}
