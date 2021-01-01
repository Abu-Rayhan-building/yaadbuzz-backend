package edu.sharif.math.yaadbuzz.service;

import edu.sharif.math.yaadbuzz.service.dto.CommentDTO;
import edu.sharif.math.yaadbuzz.service.dto.PictureDTO;
import edu.sharif.math.yaadbuzz.web.rest.dto.CommentWithIdUDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing
 * {@link edu.sharif.math.yaadbuzz.domain.Comment}.
 */
public interface CommentService {

    /**
     * Save a comment.
     *
     * @param commentDTO the entity to save.
     * @return the persisted entity.
     */
    CommentDTO save(CommentDTO commentDTO);

    /**
     * Partially updates a comment.
     *
     * @param commentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CommentDTO> partialUpdate(CommentDTO commentDTO);

    /**
     * Get all the comments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CommentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" comment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CommentDTO> findOne(Long id);

    /**
     * Delete the "id" comment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    boolean currentuserHasCreateAccess(Long memid);

    CommentDTO save(CommentWithIdUDTO commentUpdateReqDTO);

    PictureDTO getPicture(Long comId, Long picId);

    Page<CommentDTO> findAllForChildrenComments(Long parentCommentId,
	    Pageable pageable);

    boolean currentuserHasUpdateAccess(Long id);

    boolean currentuserHasGetAccess(Long memId, Long id);

    default boolean currentuserHasDeleteAccess(Long id) {
	return currentuserHasUpdateAccess(id);
    }

}
