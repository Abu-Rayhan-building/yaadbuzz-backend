package edu.sharif.math.yaadbuzz.web.rest.notCrud;

import org.springframework.beans.factory.annotation.Autowired;
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

import edu.sharif.math.yaadbuzz.service.storage.StorageFileNotFoundException;
import edu.sharif.math.yaadbuzz.service.storage.StorageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(final StorageService storageService) {
	this.storageService = storageService;
    }

    @PostMapping(path = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Make a POST request to upload the file", produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String handleFileUpload(
	    @RequestPart("file") @ApiParam(name = "file", value = "Select the file to Upload", required = true) final MultipartFile file,
	    final RedirectAttributes redirectAttributes) {

	this.storageService.store(file);
	redirectAttributes.addFlashAttribute("message",
		"You successfully uploaded " + file.getOriginalFilename()
			+ "!");

	return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(
	    final StorageFileNotFoundException exc) {
	return ResponseEntity.notFound().build();
    }

}
