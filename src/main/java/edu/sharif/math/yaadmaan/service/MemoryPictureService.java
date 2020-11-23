package edu.sharif.math.yaadmaan.service;

import java.util.List;
import java.util.Optional;

import edu.sharif.math.yaadmaan.service.dto.MemoryPictureDTO;

/**
 * Service Interface for managing {@link edu.sharif.math.yaadmaan.domain.MemoryPicture}.
 */
public interface MemoryPictureService {

    /**
     * Save a memoryPicture.
     *
     * @param memoryPictureDTO the entity to save.
     * @return the persisted entity.
     */
    MemoryPictureDTO save(MemoryPictureDTO memoryPictureDTO);

    /**
     * Get all the memoryPictures.
     *
     * @return the list of entities.
     */
    List<MemoryPictureDTO> findAll();


    /**
     * Get the "id" memoryPicture.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MemoryPictureDTO> findOne(Long id);

    /**
     * Delete the "id" memoryPicture.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
