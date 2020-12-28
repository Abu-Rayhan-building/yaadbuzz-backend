package edu.sharif.math.yaadbuzz.web.rest.notCrud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.sharif.math.yaadbuzz.service.CommentService;
import edu.sharif.math.yaadbuzz.service.DepartmentService;
import edu.sharif.math.yaadbuzz.service.MemoryService;
import edu.sharif.math.yaadbuzz.service.PictureService;
import edu.sharif.math.yaadbuzz.service.UserPerDepartmentService;
import edu.sharif.math.yaadbuzz.service.dto.PictureDTO;
import edu.sharif.math.yaadbuzz.service.storage.StorageService;

/**
 * REST controller for managing {@link edu.sharif.math.yaadbuzz.domain.Picture}.
 */
@RestController
@RequestMapping("/api/department/{depId}")
public class PictureNotCrudResource {

    private static final String ENTITY_NAME = "picture";

    private final Logger log = LoggerFactory
	    .getLogger(PictureNotCrudResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PictureService pictureService;

    @Autowired
    private StorageService storageService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    UserPerDepartmentService userPerDepartmentService;

    @Autowired
    MemoryService memoryService;

    @Autowired
    CommentService commentService;

    // fuck I'm raping to security
    public PictureNotCrudResource(final PictureService pictureService) {
	this.pictureService = pictureService;
    }

    private ResponseEntity<Resource> createReponse(final Resource file,
	    final String name, final boolean writeAttachment) {
	final var res = ResponseEntity.ok();
	if (MediaTypeFactory.getMediaType(name).isPresent()) {
	    final MediaType mediaType = MediaTypeFactory.getMediaType(name)
		    .get();
	    res.header(HttpHeaders.CONTENT_TYPE, mediaType.toString());
	}

	if (writeAttachment) {
	    res.header(HttpHeaders.CONTENT_DISPOSITION,
		    "attachment;filename=" + name);
	}

	return res.body(file);
    }

    /**
     * {@code GET  /pictures/:depId} : get the "depId" picture.
     *
     * @param depId the depId of the pictureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the pictureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/memory/{memId}/comment/{comId}/picture/{picId}")
    @ResponseBody
    public ResponseEntity<Resource> getCommentPicture(
	    @PathVariable final Long depId, @PathVariable final Long memId,
	    @PathVariable final Long comId, @PathVariable final Long picId) {
	this.log.debug("REST request to get Picture : {}", depId);
	if (this.commentService.currentuserHasGetAccess(comId) == false) {
	    throw new AccessDeniedException("can't get upd");
	}

	final PictureDTO pictureDTO = this.commentService.getPicture(comId,
		picId);
	final Resource file = this.storageService
		.loadAsResource(pictureDTO.getAddress());
	final boolean writeAttachment = false;
	return this.createReponse(file, pictureDTO.getAddress(),
		writeAttachment);
    }

    /**
     * {@code GET  /pictures/:depId} : get the "depId" picture.
     *
     * @param depId the depId of the pictureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the pictureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping(path = "/picture")
    @ResponseBody
    public ResponseEntity<Resource> getDepartmentPicture(
	    @PathVariable final Long depId) {
	this.log.debug("REST request to get Picture : {}", depId);
	if (this.departmentService.currentuserHasGetAccess(depId) == false) {
	    throw new AccessDeniedException("can't get dep");
	}

	final PictureDTO pictureDTO = this.departmentService
		.getDepartmentPicture(depId);
	final Resource file = this.storageService
		.loadAsResource(pictureDTO.getAddress());
	final boolean writeAttachment = false;
	return this.createReponse(file, pictureDTO.getAddress(),
		writeAttachment);
    }

    /**
     * {@code GET  /pictures/:depId} : get the "depId" picture.
     *
     * @param depId the depId of the pictureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the pictureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/memory/{memId}/picture/{picId}")
    @ResponseBody
    public ResponseEntity<Resource> getMemoryPicture(
	    @PathVariable final Long memId, @PathVariable final Long depId,
	    @PathVariable final Long picId) {
	this.log.debug("REST request to get Picture : {}", depId);
	if (this.memoryService.currentuserHasGetAccess(memId) == false) {
	    throw new AccessDeniedException("can't get upd");
	}
	final var comId = this.memoryService.findOne(memId).get()
		.getBaseComment().getId();
	final PictureDTO pictureDTO = this.commentService.getPicture(comId,
		picId);
	final Resource file = this.storageService
		.loadAsResource(pictureDTO.getAddress());
	final boolean writeAttachment = false;
	return this.createReponse(file, pictureDTO.getAddress(),
		writeAttachment);
    }

    /**
     * {@code GET  /pictures/:depId} : get the "depId" picture.
     *
     * @param depId the depId of the pictureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the pictureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/userPerDepartment/{updId}/picture")
    @ResponseBody
    public ResponseEntity<Resource> getUPDPicture(
	    @PathVariable final Long depId, @PathVariable final Long updId) {
	this.log.debug("REST request to get Picture : {}", depId);
	if (this.userPerDepartmentService
		.currentuserHasGetAccess(updId) == false) {
	    throw new AccessDeniedException("can't get upd");
	}

	final PictureDTO pictureDTO = this.userPerDepartmentService
		.getUPDPicture(depId);
	final Resource file = this.storageService
		.loadAsResource(pictureDTO.getAddress());
	final boolean writeAttachment = false;
	return this.createReponse(file, pictureDTO.getAddress(),
		writeAttachment);
    }
}
