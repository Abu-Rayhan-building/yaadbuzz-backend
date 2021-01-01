package edu.sharif.math.yaadbuzz.web.rest.notCrud;

import javax.annotation.security.RolesAllowed;

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

import edu.sharif.math.yaadbuzz.security.AuthoritiesConstants;
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
@RolesAllowed(AuthoritiesConstants.ADMIN)
@RequestMapping("/api")
public class PictureNotCrudForAdminResource {

    private static final String ENTITY_NAME = "picture";

    private final Logger log = LoggerFactory
	    .getLogger(PictureNotCrudForAdminResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PictureService pictureService;

    @Autowired
    private StorageService storageService;

    public PictureNotCrudForAdminResource(final PictureService pictureService) {
	this.pictureService = pictureService;
    }

    /**
     * {@code GET  /pictures/:depId} : get the "depId" picture.
     *
     * @param depId the depId of the pictureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the pictureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping(path = "/pictures/{picId}/file")
    @ResponseBody
    public ResponseEntity<Resource> getDepartmentPicture(
	    @PathVariable final Long picId) {

	final PictureDTO pictureDTO = this.pictureService.findOne(picId).get();
	final Resource file = this.storageService
		.loadAsResource(pictureDTO.getAddress());
	final boolean writeAttachment = false;
	return PictureNotCrudResource.createReponse(file,
		pictureDTO.getAddress(), writeAttachment);
    }

}
