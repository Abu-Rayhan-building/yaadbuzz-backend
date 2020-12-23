package edu.sharif.math.yaadbuzz.web.rest;

import edu.sharif.math.yaadbuzz.service.TopicVoteService;
import edu.sharif.math.yaadbuzz.web.rest.errors.BadRequestAlertException;
import edu.sharif.math.yaadbuzz.service.dto.TopicVoteDTO;
import edu.sharif.math.yaadbuzz.service.dto.TopicVoteCriteria;
import edu.sharif.math.yaadbuzz.security.AuthoritiesConstants;
import edu.sharif.math.yaadbuzz.service.TopicVoteQueryService;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link edu.sharif.math.yaadbuzz.domain.TopicVote}.
 */
@RestController
@RolesAllowed(AuthoritiesConstants.ADMIN)
@RequestMapping("/api")
public class TopicVoteResource {

    private final Logger log = LoggerFactory.getLogger(TopicVoteResource.class);

    private static final String ENTITY_NAME = "topicVote";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TopicVoteService topicVoteService;

    private final TopicVoteQueryService topicVoteQueryService;

    public TopicVoteResource(TopicVoteService topicVoteService, TopicVoteQueryService topicVoteQueryService) {
        this.topicVoteService = topicVoteService;
        this.topicVoteQueryService = topicVoteQueryService;
    }

    /**
     * {@code POST  /topic-votes} : Create a new topicVote.
     *
     * @param topicVoteDTO the topicVoteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new topicVoteDTO, or with status {@code 400 (Bad Request)} if the topicVote has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/topic-votes")
    public ResponseEntity<TopicVoteDTO> createTopicVote(@Valid @RequestBody TopicVoteDTO topicVoteDTO) throws URISyntaxException {
        log.debug("REST request to save TopicVote : {}", topicVoteDTO);
        if (topicVoteDTO.getId() != null) {
            throw new BadRequestAlertException("A new topicVote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TopicVoteDTO result = topicVoteService.save(topicVoteDTO);
        return ResponseEntity.created(new URI("/api/topic-votes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /topic-votes} : Updates an existing topicVote.
     *
     * @param topicVoteDTO the topicVoteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated topicVoteDTO,
     * or with status {@code 400 (Bad Request)} if the topicVoteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the topicVoteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/topic-votes")
    public ResponseEntity<TopicVoteDTO> updateTopicVote(@Valid @RequestBody TopicVoteDTO topicVoteDTO) throws URISyntaxException {
        log.debug("REST request to update TopicVote : {}", topicVoteDTO);
        if (topicVoteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TopicVoteDTO result = topicVoteService.save(topicVoteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, topicVoteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /topic-votes} : get all the topicVotes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of topicVotes in body.
     */
    @GetMapping("/topic-votes")
    public ResponseEntity<List<TopicVoteDTO>> getAllTopicVotes(TopicVoteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TopicVotes by criteria: {}", criteria);
        Page<TopicVoteDTO> page = topicVoteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /topic-votes/count} : count all the topicVotes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/topic-votes/count")
    public ResponseEntity<Long> countTopicVotes(TopicVoteCriteria criteria) {
        log.debug("REST request to count TopicVotes by criteria: {}", criteria);
        return ResponseEntity.ok().body(topicVoteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /topic-votes/:id} : get the "id" topicVote.
     *
     * @param id the id of the topicVoteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the topicVoteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/topic-votes/{id}")
    public ResponseEntity<TopicVoteDTO> getTopicVote(@PathVariable Long id) {
        log.debug("REST request to get TopicVote : {}", id);
        Optional<TopicVoteDTO> topicVoteDTO = topicVoteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(topicVoteDTO);
    }

    /**
     * {@code DELETE  /topic-votes/:id} : delete the "id" topicVote.
     *
     * @param id the id of the topicVoteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/topic-votes/{id}")
    public ResponseEntity<Void> deleteTopicVote(@PathVariable Long id) {
        log.debug("REST request to delete TopicVote : {}", id);
        topicVoteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
