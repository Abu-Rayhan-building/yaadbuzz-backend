package edu.sharif.math.yaadmaan.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadmaan.domain.Picture;
import edu.sharif.math.yaadmaan.repository.PictureRepository;
import edu.sharif.math.yaadmaan.service.PictureService;
import edu.sharif.math.yaadmaan.service.dto.PictureDTO;
import edu.sharif.math.yaadmaan.service.mapper.PictureMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Picture}.
 */
@Service
@Transactional
public class PictureServiceImpl implements PictureService {

    private final Logger log = LoggerFactory.getLogger(PictureServiceImpl.class);

    private final PictureRepository pictureRepository;

    private final PictureMapper pictureMapper;

    public PictureServiceImpl(PictureRepository pictureRepository, PictureMapper pictureMapper) {
        this.pictureRepository = pictureRepository;
        this.pictureMapper = pictureMapper;
    }

    @Override
    public PictureDTO save(PictureDTO pictureDTO) {
        log.debug("Request to save Picture : {}", pictureDTO);
        Picture picture = pictureMapper.toEntity(pictureDTO);
        picture = pictureRepository.save(picture);
        return pictureMapper.toDto(picture);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PictureDTO> findAll() {
        log.debug("Request to get all Pictures");
        return pictureRepository.findAll().stream()
            .map(pictureMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<PictureDTO> findOne(Long id) {
        log.debug("Request to get Picture : {}", id);
        return pictureRepository.findById(id)
            .map(pictureMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Picture : {}", id);
        pictureRepository.deleteById(id);
    }
}
