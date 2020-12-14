package edu.sharif.math.yaadbuzz.web.rest.notCrud;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.sharif.math.yaadbuzz.domain.User;
import edu.sharif.math.yaadbuzz.repository.UserRepository;
import edu.sharif.math.yaadbuzz.service.CommentService;
import edu.sharif.math.yaadbuzz.service.DepartmentService;
import edu.sharif.math.yaadbuzz.service.MailService;
import edu.sharif.math.yaadbuzz.service.MemoryService;
import edu.sharif.math.yaadbuzz.service.UserPerDepartmentService;
import edu.sharif.math.yaadbuzz.service.UserService;
import edu.sharif.math.yaadbuzz.service.dto.CommentDTO;
import edu.sharif.math.yaadbuzz.service.dto.helpers.CommentCreateUDTO;
import edu.sharif.math.yaadbuzz.service.dto.helpers.CommentUpdateUDTO;
import edu.sharif.math.yaadbuzz.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link User} entity, and needs to fetch its
 * collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship
 * between User and Authority, and send everything to the client side: there
 * would be no View Model and DTO, a lot less code, and an outer-join which
 * would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities,
 * because people will quite often do relationships with the user, and we don't
 * want them to get the authorities all the time for nothing (for performance
 * reasons). This is the #1 goal: we should not impact our users' application
 * because of this use-case.</li>
 * <li>Not having an outer join causes n+1 requests to the database. This is not
 * a real issue as we have by default a second-level cache. This means on the
 * first HTTP call we do the n+1 requests, but then all authorities come from
 * the cache, so in fact it's much better than doing an outer join (which will
 * get lots of data from the database, for each HTTP call).</li>
 * <li>As this manages users, for security reasons, we'd rather have a DTO
 * layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this
 * case.
 */
@RestController
@RequestMapping("/api/department/{depId}/memory/{memId}")
public class CommentNotCrudResource {
    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections
	    .unmodifiableList(Arrays.asList("id", "login", "firstName",
		    "lastName", "email", "activated", "langKey"));

    private static final String ENTITY_NAME = "comment";

    private final Logger log = LoggerFactory
	    .getLogger(CommentNotCrudResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommentService commentService;

    private final MemoryService memoryService;
    private final UserPerDepartmentService userPerDepartmentService;

    public CommentNotCrudResource(final DepartmentService departmentService,
	    final UserService userService, final UserRepository userRepository,
	    final MailService mailService, final CommentService commentService,
	    final MemoryService memoryService,
	    UserPerDepartmentService userPerDepartmentService) {
	this.commentService = commentService;
	this.memoryService = memoryService;
	this.userPerDepartmentService = userPerDepartmentService;
    }

    /*
     * {@code POST /comments} : Create a new comment.
     *
     * @param commentDTO the commentDTO to create.
     *
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and
     * with body the new commentDTO, or with status {@code 400 (Bad Request)} if
     * the comment has already an ID.
     *
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comment")
    public ResponseEntity<CommentDTO> createComment(
	    @PathVariable final Long depId, @PathVariable final Long memId,
	    @Valid @RequestBody final CommentCreateUDTO commentCreateReqDTO)
	    throws URISyntaxException {
	this.log.debug("REST request to save Comment : {}",
		commentCreateReqDTO);

	if (!this.commentService.currentuserHasCreateAccess(memId)) {
	    throw new AccessDeniedException("cant create comment");
	}

	CommentDTO dto = new CommentDTO();
	dto.setMemoryId(memId);
	dto.setPictures(commentCreateReqDTO.getPictures());
	dto.setText(commentCreateReqDTO.getText());
	var udpid = this.userPerDepartmentService
		.getCurrentUserUserPerDepeartmentIdInDep(depId);
	dto.setWriterId(udpid);
	final CommentDTO result = this.commentService.save(dto);
	return ResponseEntity
		.created(new URI("/api/comments/" + result.getId()))
		.headers(HeaderUtil.createEntityCreationAlert(
			this.applicationName, true,
			CommentNotCrudResource.ENTITY_NAME,
			result.getId().toString()))
		.body(result);
    }

    /**
     * {@code DELETE  /comments/:id} : delete the "id" comment.
     *
     * @param id the id of the commentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable final Long id) {
	this.log.debug("REST request to delete Comment : {}", id);
	if (!this.commentService.currentuserHasDeleteAccess(id)) {
	    throw new AccessDeniedException("cant delete comment");
	}
	this.commentService.delete(id);
	return ResponseEntity.noContent()
		.headers(HeaderUtil.createEntityDeletionAlert(
			this.applicationName, true,
			CommentNotCrudResource.ENTITY_NAME, id.toString()))
		.build();
    }

    /**
     * {@code GET  /comments} : get all the comments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
     *         list of comments in body.
     */
    @GetMapping("/comment")
    public ResponseEntity<List<CommentDTO>> getAllCommentsForMemory(
	    @PathVariable final Long depId, @PathVariable final Long memId,
	    final Pageable pageable) {

	if (!this.memoryService.currentuserHasAccessToComments(memId)) {
	    throw new AccessDeniedException("cant get comments");
	}

	this.log.debug("REST request to get a page of Comments");
	final Page<CommentDTO> page = this.commentService
		.findAllForMemory(memId, pageable);
	final HttpHeaders headers = PaginationUtil
		.generatePaginationHttpHeaders(
			ServletUriComponentsBuilder.fromCurrentRequest(), page);
	return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code PUT  /comments} : Updates an existing comment.
     *
     * @param commentDTO the commentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the updated commentDTO, or with status
     *         {@code 400 (Bad Request)} if the commentDTO is not valid, or with
     *         status {@code 500 (Internal Server Error)} if the commentDTO
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comment")
    public ResponseEntity<CommentDTO> updateComment(
	    @Valid @RequestBody final CommentUpdateUDTO commentUpdateReqDTO)
	    throws URISyntaxException {
	this.log.debug("REST request to update Comment : {}",
		commentUpdateReqDTO);
	if (commentUpdateReqDTO.getId() == null) {
	    throw new BadRequestAlertException("Invalid id",
		    CommentNotCrudResource.ENTITY_NAME, "idnull");
	}

	final var comId = commentUpdateReqDTO.getId();
	if (!this.commentService.currentuserHasUpdateAccess(comId)) {
	    throw new AccessDeniedException("cant update comment");
	}
	final CommentDTO result = this.commentService.save(commentUpdateReqDTO);
	return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(
		this.applicationName, true, CommentNotCrudResource.ENTITY_NAME,
		result.getId().toString())).body(result);
    }
}
