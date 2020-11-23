package edu.sharif.math.yaadmaan.service;

import java.util.List;
import java.util.Optional;

import edu.sharif.math.yaadmaan.service.dto.CharateristicsRepetationDTO;

/**
 * Service Interface for managing {@link edu.sharif.math.yaadmaan.domain.CharateristicsRepetation}.
 */
public interface CharateristicsRepetationService {

    /**
     * Save a charateristicsRepetation.
     *
     * @param charateristicsRepetationDTO the entity to save.
     * @return the persisted entity.
     */
    CharateristicsRepetationDTO save(CharateristicsRepetationDTO charateristicsRepetationDTO);

    /**
     * Get all the charateristicsRepetations.
     *
     * @return the list of entities.
     */
    List<CharateristicsRepetationDTO> findAll();


    /**
     * Get the "id" charateristicsRepetation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CharateristicsRepetationDTO> findOne(Long id);

    /**
     * Delete the "id" charateristicsRepetation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
