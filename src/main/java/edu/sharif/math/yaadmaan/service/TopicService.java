package edu.sharif.math.yaadmaan.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.sharif.math.yaadmaan.service.dto.TopicDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link edu.sharif.math.yaadmaan.domain.Topic}.
 */
public interface TopicService {

    /**
     * Save a topic.
     *
     * @param topicDTO the entity to save.
     * @return the persisted entity.
     */
    TopicDTO save(TopicDTO topicDTO);

    /**
     * Get all the topics.
     *
     * @return the list of entities.
     */
    List<TopicDTO> findAll();

    /**
     * Get all the topics with eager load of many-to-many relationships.
     *
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
}
