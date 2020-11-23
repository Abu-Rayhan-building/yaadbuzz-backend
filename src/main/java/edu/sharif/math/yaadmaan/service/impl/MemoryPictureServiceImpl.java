package edu.sharif.math.yaadmaan.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadmaan.domain.MemoryPicture;
import edu.sharif.math.yaadmaan.repository.MemoryPictureRepository;
import edu.sharif.math.yaadmaan.service.MemoryPictureService;
import edu.sharif.math.yaadmaan.service.dto.MemoryPictureDTO;
import edu.sharif.math.yaadmaan.service.mapper.MemoryPictureMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link MemoryPicture}.
 */
@Service
@Transactional
public class MemoryPictureServiceImpl implements MemoryPictureService {

    private final Logger log = LoggerFactory.getLogger(MemoryPictureServiceImpl.class);

    private final MemoryPictureRepository memoryPictureRepository;

    private final MemoryPictureMapper memoryPictureMapper;

    public MemoryPictureServiceImpl(MemoryPictureRepository memoryPictureRepository, MemoryPictureMapper memoryPictureMapper) {
        this.memoryPictureRepository = memoryPictureRepository;
        this.memoryPictureMapper = memoryPictureMapper;
    }

    @Override
    public MemoryPictureDTO save(MemoryPictureDTO memoryPictureDTO) {
        log.debug("Request to save MemoryPicture : {}", memoryPictureDTO);
        MemoryPicture memoryPicture = memoryPictureMapper.toEntity(memoryPictureDTO);
        memoryPicture = memoryPictureRepository.save(memoryPicture);
        return memoryPictureMapper.toDto(memoryPicture);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemoryPictureDTO> findAll() {
        log.debug("Request to get all MemoryPictures");
        return memoryPictureRepository.findAll().stream()
            .map(memoryPictureMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<MemoryPictureDTO> findOne(Long id) {
        log.debug("Request to get MemoryPicture : {}", id);
        return memoryPictureRepository.findById(id)
            .map(memoryPictureMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MemoryPicture : {}", id);
        memoryPictureRepository.deleteById(id);
    }
}
