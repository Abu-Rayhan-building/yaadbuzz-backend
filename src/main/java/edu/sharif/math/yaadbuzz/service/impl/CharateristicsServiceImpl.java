package edu.sharif.math.yaadbuzz.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadbuzz.domain.Charateristics;
import edu.sharif.math.yaadbuzz.repository.CharateristicsRepository;
import edu.sharif.math.yaadbuzz.service.CharateristicsService;
import edu.sharif.math.yaadbuzz.service.DepartmentService;
import edu.sharif.math.yaadbuzz.service.UserPerDepartmentService;
import edu.sharif.math.yaadbuzz.service.UserService;
import edu.sharif.math.yaadbuzz.service.dto.CharateristicsDTO;
import edu.sharif.math.yaadbuzz.service.dto.helpers.CharateristicsVoteDTO;
import edu.sharif.math.yaadbuzz.service.mapper.CharateristicsMapper;

/**
 * Service Implementation for managing {@link Charateristics}.
 */
@Service
@Transactional
public class CharateristicsServiceImpl implements CharateristicsService {

    private final Logger log = LoggerFactory
	    .getLogger(CharateristicsServiceImpl.class);

    private final CharateristicsRepository charateristicsRepository;

    private final CharateristicsMapper charateristicsMapper;

    private final DepartmentService departmentService;

    private final UserPerDepartmentService userPerDepartmentService;

    public CharateristicsServiceImpl(
	    final CharateristicsRepository charateristicsRepository,
	    final CharateristicsMapper charateristicsMapper,
	    final DepartmentService departmentService,
	    final UserService userService,
	    final UserPerDepartmentService userPerDepartmentService) {
	this.charateristicsRepository = charateristicsRepository;
	this.charateristicsMapper = charateristicsMapper;
	this.departmentService = departmentService;
	this.userPerDepartmentService = userPerDepartmentService;
    }

    @Override
    public CharateristicsDTO assign(
	    final CharateristicsVoteDTO charateristicsDTO) {
	final Charateristics c = this
		.findOne(charateristicsDTO.getUserPerDepartmentId(),
			charateristicsDTO.getTitle())
		.orElseGet(() -> null);
	CharateristicsDTO characteristic;
	if (c == null) {
	    characteristic = new CharateristicsDTO();
	    characteristic.setRepetation(1);
	    characteristic.setTitle(charateristicsDTO.getTitle());
	    characteristic.setUserPerDepartmentId(
		    charateristicsDTO.getUserPerDepartmentId());
	} else {
	    characteristic = this.charateristicsMapper.toDto(c);
	    characteristic.setRepetation(characteristic.getRepetation() + 1);

	}
	characteristic = this.save(characteristic);
	return characteristic;
    }

    @Override
    public boolean currentuserHasCreateAccess(final Long depId) {
	return false;
    }

    @Override
    public boolean currentuserHasGetAccess(final Long id) {
	return false;
    }

    @Override
    public boolean currentuserHasUpdateAccess(final Long id) {
	return false;
    }

    @Override
    public boolean currentuserHasVoteAccess(final Long depId) {
	return this.departmentService.currentuserHasGetAccess(depId);
    }

    @Override
    public void delete(final Long id) {
	this.log.debug("Request to delete Charateristics : {}", id);
	this.charateristicsRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CharateristicsDTO> findAll(final Pageable pageable) {
	this.log.debug("Request to get all Charateristics");
	return this.charateristicsRepository.findAll(pageable)
		.map(this.charateristicsMapper::toDto);
    }

    @Override
    public Page<CharateristicsDTO> findMineInDep(final Long depId,
	    final Pageable pageable) {
	final var updId = this.userPerDepartmentService
		.getCurrentUserInDep(depId).getId();
	return this.findUsersCharactersInDep(updId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CharateristicsDTO> findOne(final Long id) {
	this.log.debug("Request to get Charateristics : {}", id);
	return this.charateristicsRepository.findById(id)
		.map(this.charateristicsMapper::toDto);
    }

    private Optional<Charateristics> findOne(final Long userPerDepartmentId,
	    final String title) {
	return this.charateristicsRepository.findOne(userPerDepartmentId,
		title);
    }

    @Override
    public Page<CharateristicsDTO> findUsersCharactersInDep(final Long updId,
	    final Pageable pageable) {
	return this.charateristicsRepository
		.findUsersCharactersInDep(updId, pageable)
		.map(this.charateristicsMapper::toDto);
    }

    @Override
    public CharateristicsDTO save(final CharateristicsDTO charateristicsDTO) {
	this.log.debug("Request to save Charateristics : {}",
		charateristicsDTO);
	Charateristics charateristics = this.charateristicsMapper
		.toEntity(charateristicsDTO);
	charateristics = this.charateristicsRepository.save(charateristics);
	return this.charateristicsMapper.toDto(charateristics);
    }
}
