package edu.sharif.math.yaadbuzz.web.rest.notCrud;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.sharif.math.yaadbuzz.service.dto.MemoryDTO;
import edu.sharif.math.yaadbuzz.service.dto.PictureDTO;
import edu.sharif.math.yaadbuzz.service.storage.StorageFileNotFoundException;
import edu.sharif.math.yaadbuzz.service.storage.StorageService;
import edu.sharif.math.yaadbuzz.web.rest.dto.PictureDTOWithoutAddress;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api/department/{depId}")
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(final StorageService storageService) {
	this.storageService = storageService;
    }

    private static final String ENTITY_NAME = "picture";

    private final Logger log = LoggerFactory
	    .getLogger(MemorialNotCrudResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @PostMapping(path = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Make a POST request to upload the file", produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PictureDTOWithoutAddress> handleFileUpload(
	    @PathVariable final Long depId,
	    @RequestPart("file") @ApiParam(name = "file", value = "Select the file to Upload", required = true) final MultipartFile file,
	    final RedirectAttributes redirectAttributes)
	    throws URISyntaxException {

	final var result = this.storageService.store(file, depId);
	return ResponseEntity
		.created(new URI("/api/department/" + depId + "/picture"
			+ result.getId()))
		.headers(HeaderUtil.createEntityCreationAlert(
			this.applicationName, true,
			FileUploadController.ENTITY_NAME,
			result.getId().toString()))
		.body(result);

    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(
	    final StorageFileNotFoundException exc) {
	return ResponseEntity.notFound().build();
    }

}
