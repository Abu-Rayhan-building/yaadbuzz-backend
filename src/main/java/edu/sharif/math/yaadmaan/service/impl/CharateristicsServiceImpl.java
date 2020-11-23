package edu.sharif.math.yaadmaan.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadmaan.domain.Charateristics;
import edu.sharif.math.yaadmaan.repository.CharateristicsRepository;
import edu.sharif.math.yaadmaan.service.CharateristicsService;
import edu.sharif.math.yaadmaan.service.dto.CharateristicsDTO;
import edu.sharif.math.yaadmaan.service.mapper.CharateristicsMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Charateristics}.
 */
@Service
@Transactional
public class CharateristicsServiceImpl implements CharateristicsService {

    private final Logger log = LoggerFactory.getLogger(CharateristicsServiceImpl.class);

    private final CharateristicsRepository charateristicsRepository;

    private final CharateristicsMapper charateristicsMapper;

    public CharateristicsServiceImpl(CharateristicsRepository charateristicsRepository, CharateristicsMapper charateristicsMapper) {
        this.charateristicsRepository = charateristicsRepository;
        this.charateristicsMapper = charateristicsMapper;
    }

    @Override
    public CharateristicsDTO save(CharateristicsDTO charateristicsDTO) {
        log.debug("Request to save Charateristics : {}", charateristicsDTO);
        Charateristics charateristics = charateristicsMapper.toEntity(charateristicsDTO);
        charateristics = charateristicsRepository.save(charateristics);
        return charateristicsMapper.toDto(charateristics);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CharateristicsDTO> findAll() {
        log.debug("Request to get all Charateristics");
        return charateristicsRepository.findAll().stream()
            .map(charateristicsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<CharateristicsDTO> findOne(Long id) {
        log.debug("Request to get Charateristics : {}", id);
        return charateristicsRepository.findById(id)
            .map(charateristicsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Charateristics : {}", id);
        charateristicsRepository.deleteById(id);
    }
}
