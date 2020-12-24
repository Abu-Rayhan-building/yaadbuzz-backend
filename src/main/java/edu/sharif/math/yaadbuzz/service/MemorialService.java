package edu.sharif.math.yaadbuzz.service;

import edu.sharif.math.yaadbuzz.service.dto.MemorialDTO;
import edu.sharif.math.yaadbuzz.service.dto.helpers.MemorialUDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link edu.sharif.math.yaadbuzz.domain.Memorial}.
 */
public interface MemorialService extends ServiceWithCurrentUserCrudAccess {

    /**
     * Save a memorial.
     *
     * @param memorialDTO the entity to save.
     * @return the persisted entity.
     */
    MemorialDTO save(MemorialDTO memorialDTO);

    /**
     * Partially updates a memorial.
     *
     * @param memorialDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MemorialDTO> partialUpdate(MemorialDTO memorialDTO);

    /**
     * Get all the memorials.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MemorialDTO> findAll(Pageable pageable);

    /**
     * Get the "id" memorial.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MemorialDTO> findOne(Long id);

    /**
     * Delete the "id" memorial.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    MemorialDTO create(Long depId,  MemorialUDTO memorialUDTO);
}
