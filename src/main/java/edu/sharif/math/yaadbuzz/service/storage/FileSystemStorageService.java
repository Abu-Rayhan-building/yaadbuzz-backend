package edu.sharif.math.yaadbuzz.service.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import edu.sharif.math.yaadbuzz.config.StorageProperties;
import edu.sharif.math.yaadbuzz.service.PictureService;
import edu.sharif.math.yaadbuzz.service.dto.PictureDTO;
import edu.sharif.math.yaadbuzz.web.rest.dto.PictureDTOWithoutAddress;

@Service
@Transactional
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
	this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public PictureDTOWithoutAddress store(MultipartFile file, Long depId) {
	try {
	    if (file.isEmpty()) {
		throw new StorageException("Failed to store empty file.");
	    }
	    UUID uuid = UUID.randomUUID();

	    var p = uuid.toString()
		    + Paths.get(file.getOriginalFilename()).toString();
	    Path destinationFile = this.rootLocation.resolve(p).normalize()
		    .toAbsolutePath();
	    if (!destinationFile.getParent()
		    .equals(this.rootLocation.toAbsolutePath())) {
		// This is a security check
		throw new StorageException(
			"Cannot store file outside current directory.");
	    }
	    try (InputStream inputStream = file.getInputStream()) {
		Files.copy(inputStream, destinationFile,
			StandardCopyOption.REPLACE_EXISTING);
		var result = new PictureDTO();
		result.setAddress(p);
		result = pictureService.save(result);
		var res = new PictureDTOWithoutAddress();
		res.setId(result.getId());
		return res;
	    }
	} catch (IOException e) {
	    throw new StorageException("Failed to store file.", e);
	}
    }

    @Autowired
    private PictureService pictureService;

    @Override
    public Path load(String filename) {
	return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
	try {
	    Path file = load(filename);
	    Resource resource = new UrlResource(file.toUri());
	    if (resource.exists() || resource.isReadable()) {
		return resource;
	    } else {
		throw new StorageFileNotFoundException(
			"Could not read file: " + filename);

	    }
	} catch (MalformedURLException e) {
	    throw new StorageFileNotFoundException(
		    "Could not read file: " + filename, e);
	}
    }

    @Override
    public void init() {
	try {
	    Files.createDirectories(rootLocation);
	} catch (IOException e) {
	    throw new StorageException("Could not initialize storage", e);
	}
    }
}