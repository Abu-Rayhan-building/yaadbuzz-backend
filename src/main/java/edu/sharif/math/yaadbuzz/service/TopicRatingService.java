package edu.sharif.math.yaadbuzz.service;

import edu.sharif.math.yaadbuzz.service.dto.TopicRatingDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link edu.sharif.math.yaadbuzz.domain.TopicRating}.
 */
public interface TopicRatingService extends ServiceWithCurrentUserCrudAccess{

    /**
     * Save a topicRating.
     *
     * @param topicRatingDTO the entity to save.
     * @return the persisted entity.
     */
    TopicRatingDTO save(TopicRatingDTO topicRatingDTO);

    /**
     * Get all the topicRatings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TopicRatingDTO> findAll(Pageable pageable);


    /**
     * Get the "id" topicRating.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TopicRatingDTO> findOne(Long id);

    /**
     * Delete the "id" topicRating.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    TopicRatingDTO addBallot(Long topicId, Long ballotForUPD);

    Optional<TopicRatingDTO> findOne(Long topicId, Long ballotForUPD);
}
