package edu.sharif.math.yaadmaan.service;

import java.util.List;
import java.util.Optional;

import edu.sharif.math.yaadmaan.service.dto.TopicRatingDTO;

/**
 * Service Interface for managing {@link edu.sharif.math.yaadmaan.domain.TopicRating}.
 */
public interface TopicRatingService {

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
     * @return the list of entities.
     */
    List<TopicRatingDTO> findAll();


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
}
