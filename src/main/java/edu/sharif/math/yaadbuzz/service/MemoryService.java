package edu.sharif.math.yaadbuzz.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import edu.sharif.math.yaadbuzz.service.dto.MemoryDTO;

import java.util.Optional;

/**
 * Service Interface for managing {@link edu.sharif.math.yaadbuzz.domain.Memory}.
 */
public interface MemoryService extends ServiceWithCurrentUserCrudAccess {

    /**
     * Save a memory.
     *
     * @param memoryDTO the entity to save.
     * @return the persisted entity.
     */
    MemoryDTO save(MemoryDTO memoryDTO);

    /**
     * Get all the memories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MemoryDTO> findAll(Pageable pageable);

    /**
     * Get all the memories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<MemoryDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" memory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MemoryDTO> findOne(Long id);

    /**
     * Delete the "id" memory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<MemoryDTO> findAllInDepartment(Long depid, Pageable pageable);

    Page<MemoryDTO> findAllWithCurrentUserTagedIn(Long depid,
	    Pageable pageable);

    Page<MemoryDTO> findAllWithUserTagedIn(Long depid, Long userInDepId,
	    Pageable pageable);

    boolean currentuserHasAccessToComments(Long memid);

    boolean currentuserHasCreatAccess(Long depId);
}
