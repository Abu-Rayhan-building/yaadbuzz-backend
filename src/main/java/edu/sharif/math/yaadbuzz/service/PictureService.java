package edu.sharif.math.yaadbuzz.service;

import edu.sharif.math.yaadbuzz.service.dto.PictureDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link edu.sharif.math.yaadbuzz.domain.Picture}.
 */
public interface PictureService extends ServiceWithCurrentUserCrudAccess {

    /**
     * Save a picture.
     *
     * @param pictureDTO the entity to save.
     * @return the persisted entity.
     */
    PictureDTO save(PictureDTO pictureDTO);

    /**
     * Partially updates a picture.
     *
     * @param pictureDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PictureDTO> partialUpdate(PictureDTO pictureDTO);

    /**
     * Get all the pictures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PictureDTO> findAll(Pageable pageable);

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
