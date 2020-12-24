package edu.sharif.math.yaadbuzz.service;

import edu.sharif.math.yaadbuzz.service.dto.TopicDTO;
import edu.sharif.math.yaadbuzz.service.dto.helpers.TopicVoteUDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import javax.validation.Valid;

/**
 * Service Interface for managing {@link edu.sharif.math.yaadbuzz.domain.Topic}.
 */
public interface TopicService extends ServiceWithCurrentUserCrudAccess{

    /**
     * Save a topic.
     *
     * @param topicDTO the entity to save.
     * @return the persisted entity.
     */
    TopicDTO save(TopicDTO topicDTO);

    /**
     * Partially updates a topic.
     *
     * @param topicDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TopicDTO> partialUpdate(TopicDTO topicDTO);

    /**
     * Get all the topics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TopicDTO> findAll(Pageable pageable);

    /**
     * Get all the topics with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TopicDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" topic.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TopicDTO> findOne(Long id);

    /**
     * Delete the "id" topic.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    boolean currentuserHasVoteAccess(Long id);

    boolean currentuserHasCreateAccess(Long departmentId);

    TopicDTO vote(Long depId, @Valid TopicVoteUDTO topicVoteUDTO);
}
