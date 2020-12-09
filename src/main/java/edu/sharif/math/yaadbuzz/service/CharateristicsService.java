package edu.sharif.math.yaadbuzz.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.sharif.math.yaadbuzz.service.dto.CharateristicsDTO;
import edu.sharif.math.yaadbuzz.service.dto.helpers.CharateristicsVoteDTO;

/**
 * Service Interface for managing
 * {@link edu.sharif.math.yaadbuzz.domain.Charateristics}.
 */
public interface CharateristicsService
	extends ServiceWithCurrentUserCrudAccess {

    CharateristicsDTO assign(CharateristicsVoteDTO charateristicsDTO);

    boolean currentuserHasCreateAccess(Long depId);

    /**
     * Delete the "id" charateristics.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the charateristics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CharateristicsDTO> findAll(Pageable pageable);

    Page<CharateristicsDTO> findMineInDep(Long depId, Pageable pageable);

    /**
     * Get the "id" charateristics.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CharateristicsDTO> findOne(Long id);

    /**
     * Save a charateristics.
     *
     * @param charateristicsDTO the entity to save.
     * @return the persisted entity.
     */
    CharateristicsDTO save(CharateristicsDTO charateristicsDTO);

    boolean currentuserHasVoteAccess(Long depId);
    

    Page<CharateristicsDTO> findUsersCharactersInDep(Long updId,
	    Pageable pageable);
}
