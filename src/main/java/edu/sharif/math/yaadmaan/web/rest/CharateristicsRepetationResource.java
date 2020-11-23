package edu.sharif.math.yaadmaan.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.sharif.math.yaadmaan.security.AuthoritiesConstants;
import edu.sharif.math.yaadmaan.security.SecurityUtils;
import edu.sharif.math.yaadmaan.service.CharateristicsRepetationService;
import edu.sharif.math.yaadmaan.service.dto.CharateristicsRepetationDTO;
import edu.sharif.math.yaadmaan.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link edu.sharif.math.yaadmaan.domain.CharateristicsRepetation}.
 */
@RestController
@RequestMapping("/api")
@RolesAllowed(AuthoritiesConstants.ADMIN)
public class CharateristicsRepetationResource {
    private static final String ENTITY_NAME = "charateristicsRepetation";

    private final Logger log = LoggerFactory
	    .getLogger(CharateristicsRepetationResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CharateristicsRepetationService charateristicsRepetationService;

    public CharateristicsRepetationResource(
	    final CharateristicsRepetationService charateristicsRepetationService) {
	this.charateristicsRepetationService = charateristicsRepetationService;
    }

    /**
     * {@code POST  /charateristics-repetations} : Create a new
     * charateristicsRepetation.
     *
     * @param charateristicsRepetationDTO the charateristicsRepetationDTO to
     *                                    create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and
     *         with body the new charateristicsRepetationDTO, or with status
     *         {@code 400 (Bad Request)} if the charateristicsRepetation has
     *         already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/charateristics-repetations")
    public ResponseEntity<CharateristicsRepetationDTO> createCharateristicsRepetation(
	    @Valid @RequestBody final CharateristicsRepetationDTO charateristicsRepetationDTO)
	    throws URISyntaxException {
	this.log.debug("REST request to save CharateristicsRepetation : {}",
		charateristicsRepetationDTO);

	if (charateristicsRepetationDTO.getId() != null) {
	    throw new BadRequestAlertException(
		    "A new charateristicsRepetation cannot already have an ID",
		    CharateristicsRepetationResource.ENTITY_NAME, "idexists");
	}
	final CharateristicsRepetationDTO result = this.charateristicsRepetationService
		.save(charateristicsRepetationDTO);
	return ResponseEntity
		.created(new URI(
			"/api/charateristics-repetations/" + result.getId()))
		.headers(HeaderUtil.createEntityCreationAlert(
			this.applicationName, true,
			CharateristicsRepetationResource.ENTITY_NAME,
			result.getId().toString()))
		.body(result);
    }

    /**
     * {@code DELETE  /charateristics-repetations/:id} : delete the "id"
     * charateristicsRepetation.
     *
     * @param id the id of the charateristicsRepetationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/charateristics-repetations/{id}")
    public ResponseEntity<Void> deleteCharateristicsRepetation(
	    @PathVariable final Long id) {
	this.log.debug("REST request to delete CharateristicsRepetation : {}",
		id);
	this.charateristicsRepetationService.delete(id);
	return ResponseEntity.noContent()
		.headers(HeaderUtil.createEntityDeletionAlert(
			this.applicationName, true,
			CharateristicsRepetationResource.ENTITY_NAME,
			id.toString()))
		.build();
    }

    /**
     * {@code GET  /charateristics-repetations} : get all the
     * charateristicsRepetations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
     *         list of charateristicsRepetations in body.
     */
    @GetMapping("/charateristics-repetations")
    public List<CharateristicsRepetationDTO> getAllCharateristicsRepetations() {
	this.log.debug("REST request to get all CharateristicsRepetations");
	return this.charateristicsRepetationService.findAll();
    }

    /**
     * {@code GET  /charateristics-repetations/:id} : get the "id"
     * charateristicsRepetation.
     *
     * @param id the id of the charateristicsRepetationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the charateristicsRepetationDTO, or with status
     *         {@code 404 (Not Found)}.
     */
    @GetMapping("/charateristics-repetations/{id}")
    public ResponseEntity<CharateristicsRepetationDTO> getCharateristicsRepetation(
	    @PathVariable final Long id) {
	this.log.debug("REST request to get CharateristicsRepetation : {}", id);
	final Optional<CharateristicsRepetationDTO> charateristicsRepetationDTO = this.charateristicsRepetationService
		.findOne(id);
	return ResponseUtil.wrapOrNotFound(charateristicsRepetationDTO);
    }

    /**
     * {@code PUT  /charateristics-repetations} : Updates an existing
     * charateristicsRepetation.
     *
     * @param charateristicsRepetationDTO the charateristicsRepetationDTO to
     *                                    update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the updated charateristicsRepetationDTO, or with status
     *         {@code 400 (Bad Request)} if the charateristicsRepetationDTO is
     *         not valid, or with status {@code 500 (Internal Server Error)} if
     *         the charateristicsRepetationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/charateristics-repetations")
    public ResponseEntity<CharateristicsRepetationDTO> updateCharateristicsRepetation(
	    @Valid @RequestBody final CharateristicsRepetationDTO charateristicsRepetationDTO)
	    throws URISyntaxException {
	this.log.debug("REST request to update CharateristicsRepetation : {}",
		charateristicsRepetationDTO);
	if (charateristicsRepetationDTO.getId() == null) {
	    throw new BadRequestAlertException("Invalid id",
		    CharateristicsRepetationResource.ENTITY_NAME, "idnull");
	}
	final CharateristicsRepetationDTO result = this.charateristicsRepetationService
		.save(charateristicsRepetationDTO);
	return ResponseEntity.ok()
		.headers(HeaderUtil.createEntityUpdateAlert(
			this.applicationName, true,
			CharateristicsRepetationResource.ENTITY_NAME,
			charateristicsRepetationDTO.getId().toString()))
		.body(result);
    }
}
