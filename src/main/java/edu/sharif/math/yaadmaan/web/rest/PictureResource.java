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
import edu.sharif.math.yaadmaan.service.PictureService;
import edu.sharif.math.yaadmaan.service.dto.PictureDTO;
import edu.sharif.math.yaadmaan.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link edu.sharif.math.yaadmaan.domain.Picture}.
 */
@RestController
@RequestMapping("/api")
@RolesAllowed(AuthoritiesConstants.ADMIN)
public class PictureResource {

    private static final String ENTITY_NAME = "picture";

    private final Logger log = LoggerFactory.getLogger(PictureResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PictureService pictureService;

    public PictureResource(final PictureService pictureService) {
	this.pictureService = pictureService;
    }

    /**
     * {@code POST  /pictures} : Create a new picture.
     *
     * @param pictureDTO the pictureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and
     *         with body the new pictureDTO, or with status
     *         {@code 400 (Bad Request)} if the picture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pictures")
    public ResponseEntity<PictureDTO> createPicture(
	    @Valid @RequestBody final PictureDTO pictureDTO)
	    throws URISyntaxException {
	this.log.debug("REST request to save Picture : {}", pictureDTO);
	if (pictureDTO.getId() != null) {
	    throw new BadRequestAlertException(
		    "A new picture cannot already have an ID",
		    PictureResource.ENTITY_NAME, "idexists");
	}
	final PictureDTO result = this.pictureService.save(pictureDTO);
	return ResponseEntity
		.created(new URI("/api/pictures/" + result.getId()))
		.headers(HeaderUtil.createEntityCreationAlert(
			this.applicationName, true, PictureResource.ENTITY_NAME,
			result.getId().toString()))
		.body(result);
    }

    /**
     * {@code DELETE  /pictures/:id} : delete the "id" picture.
     *
     * @param id the id of the pictureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pictures/{id}")
    public ResponseEntity<Void> deletePicture(@PathVariable final Long id) {
	this.log.debug("REST request to delete Picture : {}", id);
	this.pictureService.delete(id);
	return ResponseEntity.noContent()
		.headers(HeaderUtil.createEntityDeletionAlert(
			this.applicationName, true, PictureResource.ENTITY_NAME,
			id.toString()))
		.build();
    }

    /**
     * {@code GET  /pictures} : get all the pictures.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
     *         list of pictures in body.
     */
    @GetMapping("/pictures")
    public List<PictureDTO> getAllPictures() {
	this.log.debug("REST request to get all Pictures");
	return this.pictureService.findAll();
    }

    /**
     * {@code GET  /pictures/:id} : get the "id" picture.
     *
     * @param id the id of the pictureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the pictureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pictures/{id}")
    public ResponseEntity<PictureDTO> getPicture(@PathVariable final Long id) {
	this.log.debug("REST request to get Picture : {}", id);
	final Optional<PictureDTO> pictureDTO = this.pictureService.findOne(id);
	return ResponseUtil.wrapOrNotFound(pictureDTO);
    }

    /**
     * {@code PUT  /pictures} : Updates an existing picture.
     *
     * @param pictureDTO the pictureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the updated pictureDTO, or with status
     *         {@code 400 (Bad Request)} if the pictureDTO is not valid, or with
     *         status {@code 500 (Internal Server Error)} if the pictureDTO
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pictures")
    public ResponseEntity<PictureDTO> updatePicture(
	    @Valid @RequestBody final PictureDTO pictureDTO)
	    throws URISyntaxException {
	this.log.debug("REST request to update Picture : {}", pictureDTO);
	if (pictureDTO.getId() == null) {
	    throw new BadRequestAlertException("Invalid id",
		    PictureResource.ENTITY_NAME, "idnull");
	}
	final PictureDTO result = this.pictureService.save(pictureDTO);
	return ResponseEntity.ok()
		.headers(HeaderUtil.createEntityUpdateAlert(
			this.applicationName, true, PictureResource.ENTITY_NAME,
			pictureDTO.getId().toString()))
		.body(result);
    }
}
