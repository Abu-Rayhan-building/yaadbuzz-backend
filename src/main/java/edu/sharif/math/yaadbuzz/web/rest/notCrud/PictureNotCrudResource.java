package edu.sharif.math.yaadbuzz.web.rest.notCrud;

import edu.sharif.math.yaadbuzz.security.AuthoritiesConstants;
import edu.sharif.math.yaadbuzz.service.PictureService;
import edu.sharif.math.yaadbuzz.service.dto.PictureDTO;
import edu.sharif.math.yaadbuzz.service.storage.StorageService;
import edu.sharif.math.yaadbuzz.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link edu.sharif.math.yaadbuzz.domain.Picture}.
 */
@RestController
@RequestMapping("/api/department/{depId}")
public class PictureNotCrudResource {

    private final Logger log = LoggerFactory
	    .getLogger(PictureNotCrudResource.class);

    private static final String ENTITY_NAME = "picture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PictureService pictureService;

    public PictureNotCrudResource(PictureService pictureService) {
	this.pictureService = pictureService;
    }

    @Autowired
    private StorageService storageService;

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(
	    @PathVariable final String filename) {

	final Resource file = this.storageService.loadAsResource(filename);
	return ResponseEntity.ok()
		.header(HttpHeaders.CONTENT_DISPOSITION,
			"attachment; filename=\"" + file.getFilename() + "\"")
		.body(file);
    }

    /**
     * {@code GET  /pictures/:id} : get the "id" picture.
     *
     * @param id the id of the pictureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the pictureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/picture")
    public ResponseEntity<PictureDTO> getDepartmentPicture(
	    @PathVariable Long id) {
	log.debug("REST request to get Picture : {}", id);
	Optional<PictureDTO> pictureDTO = pictureService.findOne(id);
	return ResponseUtil.wrapOrNotFound(pictureDTO);
    }

    /**
     * {@code GET  /pictures/:id} : get the "id" picture.
     *
     * @param id the id of the pictureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the pictureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/memory/{memId}/picture/{id}")
    public ResponseEntity<PictureDTO> getMemoryPicture(@PathVariable Long id) {
	log.debug("REST request to get Picture : {}", id);
	Optional<PictureDTO> pictureDTO = pictureService.findOne(id);
	return ResponseUtil.wrapOrNotFound(pictureDTO);
    }

    /**
     * {@code GET  /pictures/:id} : get the "id" picture.
     *
     * @param id the id of the pictureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the pictureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/memory/{memId}/comment/{comId}/picture/{id}")
    public ResponseEntity<PictureDTO> getCommentPicture(@PathVariable Long id) {
	log.debug("REST request to get Picture : {}", id);
	Optional<PictureDTO> pictureDTO = pictureService.findOne(id);
	return ResponseUtil.wrapOrNotFound(pictureDTO);
    }

    /**
     * {@code GET  /pictures/:id} : get the "id" picture.
     *
     * @param id the id of the pictureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the pictureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/userPerDepartment/{updId}/picture")
    public ResponseEntity<PictureDTO> getUPDPicture(@PathVariable Long id) {
	log.debug("REST request to get Picture : {}", id);
	Optional<PictureDTO> pictureDTO = pictureService.findOne(id);
	return ResponseUtil.wrapOrNotFound(pictureDTO);
    }
}
