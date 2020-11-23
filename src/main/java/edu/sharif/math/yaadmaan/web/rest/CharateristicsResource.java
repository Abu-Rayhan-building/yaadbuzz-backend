package edu.sharif.math.yaadmaan.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.sharif.math.yaadmaan.security.AuthoritiesConstants;
import edu.sharif.math.yaadmaan.service.CharateristicsService;
import edu.sharif.math.yaadmaan.service.dto.CharateristicsDTO;
import edu.sharif.math.yaadmaan.web.rest.errors.BadRequestAlertException;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing
 * {@link edu.sharif.math.yaadmaan.domain.Charateristics}.
 */
@RestController
@RequestMapping("/api")
@RolesAllowed(AuthoritiesConstants.ADMIN)
public class CharateristicsResource {

    private final Logger log = LoggerFactory
	    .getLogger(CharateristicsResource.class);

    private static final String ENTITY_NAME = "charateristics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CharateristicsService charateristicsService;

    public CharateristicsResource(CharateristicsService charateristicsService) {
	this.charateristicsService = charateristicsService;
    }

    /**
     * {@code POST  /charateristics} : Create a new charateristics.
     *
     * @param charateristicsDTO the charateristicsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and
     *         with body the new charateristicsDTO, or with status
     *         {@code 400 (Bad Request)} if the charateristics has already an
     *         ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/charateristics")
    public ResponseEntity<CharateristicsDTO> createCharateristics(
	    @Valid @RequestBody CharateristicsDTO charateristicsDTO)
	    throws URISyntaxException {
	log.debug("REST request to save Charateristics : {}",
		charateristicsDTO);
	if (charateristicsDTO.getId() != null) {
	    throw new BadRequestAlertException(
		    "A new charateristics cannot already have an ID",
		    ENTITY_NAME, "idexists");
	}
	CharateristicsDTO result = charateristicsService
		.save(charateristicsDTO);
	return ResponseEntity
		.created(new URI("/api/charateristics/" + result.getId()))
		.headers(HeaderUtil.createEntityCreationAlert(applicationName,
			true, ENTITY_NAME, result.getId().toString()))
		.body(result);
    }

    /**
     * {@code PUT  /charateristics} : Updates an existing charateristics.
     *
     * @param charateristicsDTO the charateristicsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the updated charateristicsDTO, or with status
     *         {@code 400 (Bad Request)} if the charateristicsDTO is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         charateristicsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/charateristics")
    public ResponseEntity<CharateristicsDTO> updateCharateristics(
	    @Valid @RequestBody CharateristicsDTO charateristicsDTO)
	    throws URISyntaxException {
	log.debug("REST request to update Charateristics : {}",
		charateristicsDTO);
	if (charateristicsDTO.getId() == null) {
	    throw new BadRequestAlertException("Invalid id", ENTITY_NAME,
		    "idnull");
	}
	CharateristicsDTO result = charateristicsService
		.save(charateristicsDTO);
	return ResponseEntity.ok()
		.headers(HeaderUtil.createEntityUpdateAlert(applicationName,
			true, ENTITY_NAME,
			charateristicsDTO.getId().toString()))
		.body(result);
    }

    /**
     * {@code GET  /charateristics} : get all the charateristics.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
     *         list of charateristics in body.
     */
    @GetMapping("/charateristics")
    public List<CharateristicsDTO> getAllCharateristics() {
	log.debug("REST request to get all Charateristics");
	return charateristicsService.findAll();
    }

    /**
     * {@code GET  /charateristics/:id} : get the "id" charateristics.
     *
     * @param id the id of the charateristicsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the charateristicsDTO, or with status
     *         {@code 404 (Not Found)}.
     */
    @GetMapping("/charateristics/{id}")
    public ResponseEntity<CharateristicsDTO> getCharateristics(
	    @PathVariable Long id) {
	log.debug("REST request to get Charateristics : {}", id);
	Optional<CharateristicsDTO> charateristicsDTO = charateristicsService
		.findOne(id);
	return ResponseUtil.wrapOrNotFound(charateristicsDTO);
    }

    /**
     * {@code DELETE  /charateristics/:id} : delete the "id" charateristics.
     *
     * @param id the id of the charateristicsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/charateristics/{id}")
    public ResponseEntity<Void> deleteCharateristics(@PathVariable Long id) {
	log.debug("REST request to delete Charateristics : {}", id);
	charateristicsService.delete(id);
	return ResponseEntity.noContent()
		.headers(HeaderUtil.createEntityDeletionAlert(applicationName,
			true, ENTITY_NAME, id.toString()))
		.build();
    }
}
