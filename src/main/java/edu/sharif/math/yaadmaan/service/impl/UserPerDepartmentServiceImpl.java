package edu.sharif.math.yaadmaan.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadmaan.domain.UserPerDepartment;
import edu.sharif.math.yaadmaan.repository.UserPerDepartmentRepository;
import edu.sharif.math.yaadmaan.service.UserPerDepartmentService;
import edu.sharif.math.yaadmaan.service.dto.UserPerDepartmentDTO;
import edu.sharif.math.yaadmaan.service.mapper.UserPerDepartmentMapper;

import java.util.Optional;

/**
 * Service Implementation for managing {@link UserPerDepartment}.
 */
@Service
@Transactional
public class UserPerDepartmentServiceImpl implements UserPerDepartmentService {

    private final Logger log = LoggerFactory
	    .getLogger(UserPerDepartmentServiceImpl.class);

    private final UserPerDepartmentRepository userPerDepartmentRepository;

    private final UserPerDepartmentMapper userPerDepartmentMapper;

    public UserPerDepartmentServiceImpl(
	    UserPerDepartmentRepository userPerDepartmentRepository,
	    UserPerDepartmentMapper userPerDepartmentMapper) {
	this.userPerDepartmentRepository = userPerDepartmentRepository;
	this.userPerDepartmentMapper = userPerDepartmentMapper;
    }

    @Override
    public UserPerDepartmentDTO save(
	    UserPerDepartmentDTO userPerDepartmentDTO) {
	log.debug("Request to save UserPerDepartment : {}",
		userPerDepartmentDTO);
	UserPerDepartment userPerDepartment = userPerDepartmentMapper
		.toEntity(userPerDepartmentDTO);
	userPerDepartment = userPerDepartmentRepository.save(userPerDepartment);
	return userPerDepartmentMapper.toDto(userPerDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserPerDepartmentDTO> findAll(Pageable pageable) {
	log.debug("Request to get all UserPerDepartments");
	return userPerDepartmentRepository.findAll(pageable)
		.map(userPerDepartmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserPerDepartmentDTO> findOne(Long id) {
	log.debug("Request to get UserPerDepartment : {}", id);
	return userPerDepartmentRepository.findById(id)
		.map(userPerDepartmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
	log.debug("Request to delete UserPerDepartment : {}", id);
	userPerDepartmentRepository.deleteById(id);
    }

    @Override
    public UserPerDepartmentDTO getCurrentUserInDep(Long depid) {
	return userPerDepartmentMapper
		.toDto(userPerDepartmentRepository.getCurrentUserInDep(depid));

    }
}
