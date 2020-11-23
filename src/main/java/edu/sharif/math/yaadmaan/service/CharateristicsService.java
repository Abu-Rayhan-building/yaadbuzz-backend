package edu.sharif.math.yaadmaan.service;

import java.util.List;
import java.util.Optional;

import edu.sharif.math.yaadmaan.service.dto.CharateristicsDTO;

/**
 * Service Interface for managing {@link edu.sharif.math.yaadmaan.domain.Charateristics}.
 */
public interface CharateristicsService {

    /**
     * Save a charateristics.
     *
     * @param charateristicsDTO the entity to save.
     * @return the persisted entity.
     */
    CharateristicsDTO save(CharateristicsDTO charateristicsDTO);

    /**
     * Get all the charateristics.
     *
     * @return the list of entities.
     */
    List<CharateristicsDTO> findAll();


    /**
     * Get the "id" charateristics.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CharateristicsDTO> findOne(Long id);

    /**
     * Delete the "id" charateristics.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
