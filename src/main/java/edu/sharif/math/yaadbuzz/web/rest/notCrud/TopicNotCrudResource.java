package edu.sharif.math.yaadbuzz.web.rest.notCrud;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.sharif.math.yaadbuzz.service.TopicService;
import edu.sharif.math.yaadbuzz.service.dto.DepartmentDTO;
import edu.sharif.math.yaadbuzz.service.dto.TopicDTO;
import edu.sharif.math.yaadbuzz.web.rest.dto.TopicVoteUDTO;
import edu.sharif.math.yaadbuzz.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link edu.sharif.math.yaadbuzz.domain.Topic}.
 */
@RestController
@RequestMapping("/api/department/{depId}")
public class TopicNotCrudResource {

    private static final String ENTITY_NAME = "topic";

    private final Logger log = LoggerFactory
	    .getLogger(TopicNotCrudResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TopicService topicService;

    public TopicNotCrudResource(final TopicService topicService) {
	this.topicService = topicService;
    }

    /**
     * {@code POST  /topics} : Create a new topic.
     *
     * @param topicDTO the topicDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and
     *         with body the new topicDTO, or with status
     *         {@code 400 (Bad Request)} if the topic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/topic")
    public ResponseEntity<TopicDTO> createTopic(@PathVariable final Long depId,
	    @Valid @RequestBody final String title) throws URISyntaxException {
	this.log.debug("REST request to save Topic : {}", title);

	if (!this.topicService.currentuserHasCreateAccess(depId)) {
	    throw new AccessDeniedException("can't create topic upd");
	}
	var input = new TopicDTO();
	input.setTitle(title);
	{
	    var dep = new DepartmentDTO();
	    dep.setId(depId);
	    input.setDepartment(dep);
	}
	final TopicDTO result = this.topicService.save(input);
	return ResponseEntity.created(new URI("/api/topics/" + result.getId()))
		.headers(HeaderUtil.createEntityCreationAlert(
			this.applicationName, true,
			TopicNotCrudResource.ENTITY_NAME,
			result.getId().toString()))
		.body(result);
    }

    /**
     * {@code GET  /topics} : get all the topics.
     *
     * @param pageable  the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is
     *                  applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
     *         list of topics in body.
     */
    @GetMapping("/topic")
    public ResponseEntity<List<TopicDTO>> getAllTopics(final Pageable pageable,
	    @PathVariable final Long depId,
	    @RequestParam(required = false, defaultValue = "false") final boolean eagerload) {
	this.log.debug("REST request to get a page of Topics");
	Page<TopicDTO> page;
	if (eagerload) {
	    page = this.topicService.findAllWithEagerRelationships(pageable);
	} else {
	    page = this.topicService.findAll(pageable);
	}
	final HttpHeaders headers = PaginationUtil
		.generatePaginationHttpHeaders(
			ServletUriComponentsBuilder.fromCurrentRequest(), page);
	return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /topics/:id} : get the "id" topic.
     *
     * @param id the id of the topicDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the topicDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/topics/{id}")
    public ResponseEntity<TopicDTO> getTopic(@PathVariable final Long depId,
	    @PathVariable final Long id) {
	this.log.debug("REST request to get Topic : {}", id);
	if (!this.topicService.currentuserHasGetAccess(id)) {
	    throw new AccessDeniedException("can't create topic upd");
	}
	final Optional<TopicDTO> topicDTO = this.topicService.findOne(id);
	return ResponseUtil.wrapOrNotFound(topicDTO);
    }

    /**
     * {@code POST  /topics} : Create a new topic.
     *
     * @param topicDTO the topicDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and
     *         with body the new topicDTO, or with status
     *         {@code 400 (Bad Request)} if the topic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/topic/vote")
    public ResponseEntity<TopicDTO> vote(@PathVariable final Long depId,
	    @Valid @RequestBody final TopicVoteUDTO topicVoteUDTO)
	    throws URISyntaxException {
	this.log.debug("REST request to save Topic : {}", topicVoteUDTO);
	if (topicVoteUDTO.getTopicId() != null) {
	    throw new BadRequestAlertException("no such id",
		    TopicNotCrudResource.ENTITY_NAME, "idnotexists");
	}
	if (!this.topicService
		.currentuserHasVoteAccess(topicVoteUDTO.getTopicId())) {
	    throw new AccessDeniedException("can't vote for topic");
	}
	final TopicDTO result = this.topicService.vote(depId, topicVoteUDTO);
	return ResponseEntity.created(new URI("/api/topics/" + result.getId()))
		.headers(HeaderUtil.createEntityCreationAlert(
			this.applicationName, true,
			TopicNotCrudResource.ENTITY_NAME,
			result.getId().toString()))
		.body(result);
    }
}