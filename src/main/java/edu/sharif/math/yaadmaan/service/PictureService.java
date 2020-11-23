package edu.sharif.math.yaadmaan.service;

import java.util.List;
import java.util.Optional;

import edu.sharif.math.yaadmaan.service.dto.PictureDTO;

/**
 * Service Interface for managing {@link edu.sharif.math.yaadmaan.domain.Picture}.
 */
public interface PictureService {

    /**
     * Save a picture.
     *
     * @param pictureDTO the entity to save.
     * @return the persisted entity.
     */
    PictureDTO save(PictureDTO pictureDTO);

    /**
     * Get all the pictures.
     *
     * @return the list of entities.
     */
    List<PictureDTO> findAll();


    /**
     * Get the "id" picture.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PictureDTO> findOne(Long id);

    /**
     * Delete the "id" picture.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
