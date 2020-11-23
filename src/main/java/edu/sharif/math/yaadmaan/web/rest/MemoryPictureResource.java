package edu.sharif.math.yaadmaan.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import edu.sharif.math.yaadmaan.security.AuthoritiesConstants;
import edu.sharif.math.yaadmaan.service.MemoryPictureService;
import edu.sharif.math.yaadmaan.service.dto.MemoryPictureDTO;
import edu.sharif.math.yaadmaan.web.rest.errors.BadRequestAlertException;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing
 * {@link edu.sharif.math.yaadmaan.domain.MemoryPicture}.
 */
@RestController
@RequestMapping("/api")
@RolesAllowed(AuthoritiesConstants.ADMIN)
public class MemoryPictureResource {

    private final Logger log = LoggerFactory
	    .getLogger(MemoryPictureResource.class);

    private static final String ENTITY_NAME = "memoryPicture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MemoryPictureService memoryPictureService;

    public MemoryPictureResource(MemoryPictureService memoryPictureService) {
	this.memoryPictureService = memoryPictureService;
    }

    /**
     * {@code POST  /memory-pictures} : Create a new memoryPicture.
     *
     * @param memoryPictureDTO the memoryPictureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and
     *         with body the new memoryPictureDTO, or with status
     *         {@code 400 (Bad Request)} if the memoryPicture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/memory-pictures")
    public ResponseEntity<MemoryPictureDTO> createMemoryPicture(
	    @Valid @RequestBody MemoryPictureDTO memoryPictureDTO)
	    throws URISyntaxException {
	log.debug("REST request to save MemoryPicture : {}", memoryPictureDTO);
	if (memoryPictureDTO.getId() != null) {
	    throw new BadRequestAlertException(
		    "A new memoryPicture cannot already have an ID",
		    ENTITY_NAME, "idexists");
	}
	MemoryPictureDTO result = memoryPictureService.save(memoryPictureDTO);
	return ResponseEntity
		.created(new URI("/api/memory-pictures/" + result.getId()))
		.headers(HeaderUtil.createEntityCreationAlert(applicationName,
			true, ENTITY_NAME, result.getId().toString()))
		.body(result);
    }

    /**
     * {@code PUT  /memory-pictures} : Updates an existing memoryPicture.
     *
     * @param memoryPictureDTO the memoryPictureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the updated memoryPictureDTO, or with status
     *         {@code 400 (Bad Request)} if the memoryPictureDTO is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         memoryPictureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/memory-pictures")
    public ResponseEntity<MemoryPictureDTO> updateMemoryPicture(
	    @Valid @RequestBody MemoryPictureDTO memoryPictureDTO)
	    throws URISyntaxException {
	log.debug("REST request to update MemoryPicture : {}",
		memoryPictureDTO);
	if (memoryPictureDTO.getId() == null) {
	    throw new BadRequestAlertException("Invalid id", ENTITY_NAME,
		    "idnull");
	}
	MemoryPictureDTO result = memoryPictureService.save(memoryPictureDTO);
	return ResponseEntity.ok()
		.headers(HeaderUtil.createEntityUpdateAlert(applicationName,
			true, ENTITY_NAME, memoryPictureDTO.getId().toString()))
		.body(result);
    }

    /**
     * {@code GET  /memory-pictures} : get all the memoryPictures.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
     *         list of memoryPictures in body.
     */
    @GetMapping("/memory-pictures")
    public List<MemoryPictureDTO> getAllMemoryPictures() {
	log.debug("REST request to get all MemoryPictures");
	return memoryPictureService.findAll();
    }

    /**
     * {@code GET  /memory-pictures/:id} : get the "id" memoryPicture.
     *
     * @param id the id of the memoryPictureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the memoryPictureDTO, or with status
     *         {@code 404 (Not Found)}.
     */
    @GetMapping("/memory-pictures/{id}")
    public ResponseEntity<MemoryPictureDTO> getMemoryPicture(
	    @PathVariable Long id) {
	log.debug("REST request to get MemoryPicture : {}", id);
	Optional<MemoryPictureDTO> memoryPictureDTO = memoryPictureService
		.findOne(id);
	return ResponseUtil.wrapOrNotFound(memoryPictureDTO);
    }

    /**
     * {@code DELETE  /memory-pictures/:id} : delete the "id" memoryPicture.
     *
     * @param id the id of the memoryPictureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/memory-pictures/{id}")
    public ResponseEntity<Void> deleteMemoryPicture(@PathVariable Long id) {
	log.debug("REST request to delete MemoryPicture : {}", id);
	memoryPictureService.delete(id);
	return ResponseEntity.noContent()
		.headers(HeaderUtil.createEntityDeletionAlert(applicationName,
			true, ENTITY_NAME, id.toString()))
		.build();
    }
}
