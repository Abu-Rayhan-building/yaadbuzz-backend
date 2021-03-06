package edu.sharif.math.yaadbuzz.service;

import edu.sharif.math.yaadbuzz.domain.Picture;
import edu.sharif.math.yaadbuzz.repository.PictureRepository;
import edu.sharif.math.yaadbuzz.service.DepartmentService;
import edu.sharif.math.yaadbuzz.service.PictureService;
import edu.sharif.math.yaadbuzz.service.ServiceWithCurrentUserCrudAccess;
import edu.sharif.math.yaadbuzz.service.dto.PictureDTO;
import edu.sharif.math.yaadbuzz.service.mapper.PictureMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Picture}.
 */
@Service
@Transactional
public class PictureService implements ServiceWithCurrentUserCrudAccess {

    private final Logger log = LoggerFactory.getLogger(PictureService.class);

    private final PictureRepository pictureRepository;

    private final PictureMapper pictureMapper;

    public PictureService(PictureRepository pictureRepository, PictureMapper pictureMapper) {
        this.pictureRepository = pictureRepository;
        this.pictureMapper = pictureMapper;
    }

    public PictureDTO save(PictureDTO pictureDTO) {
        log.debug("Request to save Picture : {}", pictureDTO);
        Picture picture = pictureMapper.toEntity(pictureDTO);
        picture = pictureRepository.save(picture);
        return pictureMapper.toDto(picture);
    }

    public Optional<PictureDTO> partialUpdate(PictureDTO pictureDTO) {
        log.debug("Request to partially update Picture : {}", pictureDTO);

        return pictureRepository
            .findById(pictureDTO.getId())
            .map(
                existingPicture -> {
                    return existingPicture;
                }
            )
            .map(pictureRepository::save)
            .map(pictureMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PictureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pictures");
        return pictureRepository.findAll(pageable).map(pictureMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<PictureDTO> findOne(Long id) {
        log.debug("Request to get Picture : {}", id);
        return pictureRepository.findById(id).map(pictureMapper::toDto);
    }

    public void delete(Long id) {
        log.debug("Request to delete Picture : {}", id);
        pictureRepository.deleteById(id);
    }

    @Override
    public boolean currentuserHasUpdateAccess(Long id) {
        return false;
    }

    @Override
    public boolean currentuserHasGetAccess(Long id) {
        return false;
    }

    @Autowired
    DepartmentService departmentService;
}
