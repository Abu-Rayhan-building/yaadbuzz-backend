package edu.sharif.math.yaadbuzz.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import edu.sharif.math.yaadbuzz.service.dto.PictureDTO;
import edu.sharif.math.yaadbuzz.web.rest.dto.PictureDTOWithoutAddress;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    PictureDTOWithoutAddress store(MultipartFile file, Long depId);

    Path load(String filename);

    Resource loadAsResource(String filename);

}