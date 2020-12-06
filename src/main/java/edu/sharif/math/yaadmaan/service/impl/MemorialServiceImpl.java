package edu.sharif.math.yaadmaan.service.impl;

import edu.sharif.math.yaadmaan.service.MemorialService;
import edu.sharif.math.yaadmaan.domain.Memorial;
import edu.sharif.math.yaadmaan.repository.MemorialRepository;
import edu.sharif.math.yaadmaan.service.dto.MemorialDTO;
import edu.sharif.math.yaadmaan.service.mapper.MemorialMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Memorial}.
 */
@Service
@Transactional
public class MemorialServiceImpl implements MemorialService {

    private final Logger log = LoggerFactory.getLogger(MemorialServiceImpl.class);

    private final MemorialRepository memorialRepository;

    private final MemorialMapper memorialMapper;

    public MemorialServiceImpl(MemorialRepository memorialRepository, MemorialMapper memorialMapper) {
        this.memorialRepository = memorialRepository;
        this.memorialMapper = memorialMapper;
    }

    @Override
    public MemorialDTO save(MemorialDTO memorialDTO) {
        log.debug("Request to save Memorial : {}", memorialDTO);
        Memorial memorial = memorialMapper.toEntity(memorialDTO);
        memorial = memorialRepository.save(memorial);
        return memorialMapper.toDto(memorial);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemorialDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Memorials");
        return memorialRepository.findAll(pageable)
            .map(memorialMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<MemorialDTO> findOne(Long id) {
        log.debug("Request to get Memorial : {}", id);
        return memorialRepository.findById(id)
            .map(memorialMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Memorial : {}", id);
        memorialRepository.deleteById(id);
    }
}
