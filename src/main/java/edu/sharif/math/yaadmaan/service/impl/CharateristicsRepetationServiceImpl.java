package edu.sharif.math.yaadmaan.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadmaan.domain.CharateristicsRepetation;
import edu.sharif.math.yaadmaan.repository.CharateristicsRepetationRepository;
import edu.sharif.math.yaadmaan.security.AuthoritiesConstants;
import edu.sharif.math.yaadmaan.security.SecurityUtils;
import edu.sharif.math.yaadmaan.service.CharateristicsRepetationService;
import edu.sharif.math.yaadmaan.service.dto.CharateristicsRepetationDTO;
import edu.sharif.math.yaadmaan.service.mapper.CharateristicsRepetationMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CharateristicsRepetation}.
 */
@Service
@Transactional
public class CharateristicsRepetationServiceImpl
	implements CharateristicsRepetationService {

    private final Logger log = LoggerFactory
	    .getLogger(CharateristicsRepetationServiceImpl.class);

    private final CharateristicsRepetationRepository charateristicsRepetationRepository;

    private final CharateristicsRepetationMapper charateristicsRepetationMapper;

    public CharateristicsRepetationServiceImpl(
	    CharateristicsRepetationRepository charateristicsRepetationRepository,
	    CharateristicsRepetationMapper charateristicsRepetationMapper) {
	this.charateristicsRepetationRepository = charateristicsRepetationRepository;
	this.charateristicsRepetationMapper = charateristicsRepetationMapper;
    }

    @Override
    public CharateristicsRepetationDTO save(
	    CharateristicsRepetationDTO charateristicsRepetationDTO) {
	log.debug("Request to save CharateristicsRepetation : {}",
		charateristicsRepetationDTO);
	CharateristicsRepetation charateristicsRepetation = charateristicsRepetationMapper
		.toEntity(charateristicsRepetationDTO);
	charateristicsRepetation = charateristicsRepetationRepository
		.save(charateristicsRepetation);
	return charateristicsRepetationMapper.toDto(charateristicsRepetation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CharateristicsRepetationDTO> findAll() {
	log.debug("Request to get all CharateristicsRepetations");
	return charateristicsRepetationRepository.findAll().stream()
		.map(charateristicsRepetationMapper::toDto)
		.collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CharateristicsRepetationDTO> findOne(Long id) {
	log.debug("Request to get CharateristicsRepetation : {}", id);
	return charateristicsRepetationRepository.findById(id)
		.map(charateristicsRepetationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
	log.debug("Request to delete CharateristicsRepetation : {}", id);
	charateristicsRepetationRepository.deleteById(id);
    }
}
