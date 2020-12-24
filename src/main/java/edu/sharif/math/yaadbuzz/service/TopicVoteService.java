package edu.sharif.math.yaadbuzz.service;

import edu.sharif.math.yaadbuzz.service.dto.TopicVoteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link edu.sharif.math.yaadbuzz.domain.TopicVote}.
 */
public interface TopicVoteService extends ServiceWithCurrentUserCrudAccess {

    /**
     * Save a topicVote.
     *
     * @param topicVoteDTO the entity to save.
     * @return the persisted entity.
     */
    TopicVoteDTO save(TopicVoteDTO topicVoteDTO);

    /**
     * Partially updates a topicVote.
     *
     * @param topicVoteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TopicVoteDTO> partialUpdate(TopicVoteDTO topicVoteDTO);

    /**
     * Get all the topicVotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TopicVoteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" topicVote.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TopicVoteDTO> findOne(Long id);

    /**
     * Delete the "id" topicVote.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    TopicVoteDTO addBallot(Long topicId, Long ballotForUPD);

    Optional<TopicVoteDTO> findOne(Long topicId, Long ballotForUPD);

}
