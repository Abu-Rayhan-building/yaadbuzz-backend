package edu.sharif.math.yaadmaan.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadmaan.domain.UserPerDepartment;
import edu.sharif.math.yaadmaan.repository.UserPerDepartmentRepository;
import edu.sharif.math.yaadmaan.service.DepartmentService;
import edu.sharif.math.yaadmaan.service.UserPerDepartmentService;
import edu.sharif.math.yaadmaan.service.UserService;
import edu.sharif.math.yaadmaan.service.dto.UserPerDepartmentDTO;
import edu.sharif.math.yaadmaan.service.mapper.UserPerDepartmentMapper;

/**
 * Service Implementation for managing {@link UserPerDepartment}.
 */
@Service
@Transactional
public class UserPerDepartmentServiceImpl implements UserPerDepartmentService {

    private final Logger log = LoggerFactory
	    .getLogger(UserPerDepartmentServiceImpl.class);

    private final UserPerDepartmentRepository userPerDepartmentRepository;

    private DepartmentService departmentService;
    
    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    private final UserPerDepartmentMapper userPerDepartmentMapper;

    private final UserService userService;

    public UserPerDepartmentServiceImpl(
	    final UserPerDepartmentRepository userPerDepartmentRepository,
	    final UserPerDepartmentMapper userPerDepartmentMapper,
	    final UserService userService) {
	this.userPerDepartmentRepository = userPerDepartmentRepository;
	this.userPerDepartmentMapper = userPerDepartmentMapper;
	this.userService = userService;
    }

    @Override
    public boolean currentuserHasGetAccess(final Long id) {
	return this.departmentService.currentuserHasGetAccess(
		this.findOne(id).get().getDepartmentId());
    }

    @Override
    public boolean currentuserHasUpdateAccess(final Long id) {
	return this.findOne(id).get().getRealUserId()
		.equals(this.userService.getCurrentUserId());
    }

    @Override
    public void delete(final Long id) {
	this.log.debug("Request to delete UserPerDepartment : {}", id);
	this.userPerDepartmentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserPerDepartmentDTO> findAll(final Pageable pageable) {
	this.log.debug("Request to get all UserPerDepartments");
	return this.userPerDepartmentRepository.findAll(pageable)
		.map(this.userPerDepartmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserPerDepartmentDTO> findOne(final Long id) {
	this.log.debug("Request to get UserPerDepartment : {}", id);
	return this.userPerDepartmentRepository.findById(id)
		.map(this.userPerDepartmentMapper::toDto);
    }

    @Override
    public UserPerDepartmentDTO getCurrentUserInDep(final Long depid) {
	return this.userPerDepartmentMapper.toDto(
		this.userPerDepartmentRepository.getCurrentUserInDep(depid));

    }

    @Override
    public UserPerDepartmentDTO save(
	    final UserPerDepartmentDTO userPerDepartmentDTO) {
	this.log.debug("Request to save UserPerDepartment : {}",
		userPerDepartmentDTO);
	UserPerDepartment userPerDepartment = this.userPerDepartmentMapper
		.toEntity(userPerDepartmentDTO);
	userPerDepartment = this.userPerDepartmentRepository
		.save(userPerDepartment);
	return this.userPerDepartmentMapper.toDto(userPerDepartment);
    }
}
