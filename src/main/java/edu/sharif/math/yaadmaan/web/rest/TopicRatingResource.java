package edu.sharif.math.yaadmaan.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.sharif.math.yaadmaan.security.AuthoritiesConstants;
import edu.sharif.math.yaadmaan.service.TopicRatingService;
import edu.sharif.math.yaadmaan.service.dto.TopicRatingDTO;
import edu.sharif.math.yaadmaan.web.rest.errors.BadRequestAlertException;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link edu.sharif.math.yaadmaan.domain.TopicRating}.
 */
@RestController
@RequestMapping("/api")
@RolesAllowed(AuthoritiesConstants.ADMIN)
public class TopicRatingResource {

    private final Logger log = LoggerFactory.getLogger(TopicRatingResource.class);

    private static final String ENTITY_NAME = "topicRating";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TopicRatingService topicRatingService;

    public TopicRatingResource(TopicRatingService topicRatingService) {
        this.topicRatingService = topicRatingService;
    }

    /**
     * {@code POST  /topic-ratings} : Create a new topicRating.
     *
     * @param topicRatingDTO the topicRatingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new topicRatingDTO, or with status {@code 400 (Bad Request)} if the topicRating has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/topic-ratings")
    public ResponseEntity<TopicRatingDTO> createTopicRating(@Valid @RequestBody TopicRatingDTO topicRatingDTO) throws URISyntaxException {
        log.debug("REST request to save TopicRating : {}", topicRatingDTO);
        if (topicRatingDTO.getId() != null) {
            throw new BadRequestAlertException("A new topicRating cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TopicRatingDTO result = topicRatingService.save(topicRatingDTO);
        return ResponseEntity.created(new URI("/api/topic-ratings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /topic-ratings} : Updates an existing topicRating.
     *
     * @param topicRatingDTO the topicRatingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated topicRatingDTO,
     * or with status {@code 400 (Bad Request)} if the topicRatingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the topicRatingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/topic-ratings")
    public ResponseEntity<TopicRatingDTO> updateTopicRating(@Valid @RequestBody TopicRatingDTO topicRatingDTO) throws URISyntaxException {
        log.debug("REST request to update TopicRating : {}", topicRatingDTO);
        if (topicRatingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TopicRatingDTO result = topicRatingService.save(topicRatingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, topicRatingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /topic-ratings} : get all the topicRatings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of topicRatings in body.
     */
    @GetMapping("/topic-ratings")
    public List<TopicRatingDTO> getAllTopicRatings() {
        log.debug("REST request to get all TopicRatings");
        return topicRatingService.findAll();
    }

    /**
     * {@code GET  /topic-ratings/:id} : get the "id" topicRating.
     *
     * @param id the id of the topicRatingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the topicRatingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/topic-ratings/{id}")
    public ResponseEntity<TopicRatingDTO> getTopicRating(@PathVariable Long id) {
        log.debug("REST request to get TopicRating : {}", id);
        Optional<TopicRatingDTO> topicRatingDTO = topicRatingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(topicRatingDTO);
    }

    /**
     * {@code DELETE  /topic-ratings/:id} : delete the "id" topicRating.
     *
     * @param id the id of the topicRatingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/topic-ratings/{id}")
    public ResponseEntity<Void> deleteTopicRating(@PathVariable Long id) {
        log.debug("REST request to delete TopicRating : {}", id);
        topicRatingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
