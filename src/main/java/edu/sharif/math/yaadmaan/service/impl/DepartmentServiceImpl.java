package edu.sharif.math.yaadmaan.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadmaan.domain.Department;
import edu.sharif.math.yaadmaan.repository.DepartmentRepository;
import edu.sharif.math.yaadmaan.repository.UserPerDepartmentRepository;
import edu.sharif.math.yaadmaan.service.DepartmentService;
import edu.sharif.math.yaadmaan.service.UserPerDepartmentService;
import edu.sharif.math.yaadmaan.service.dto.DepartmentDTO;
import edu.sharif.math.yaadmaan.service.dto.UserPerDepartmentDTO;
import edu.sharif.math.yaadmaan.service.mapper.DepartmentMapper;
import edu.sharif.math.yaadmaan.service.mapper.UserPerDepartmentMapper;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

/**
 * Service Implementation for managing {@link Department}.
 */
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final Logger log = LoggerFactory
	    .getLogger(DepartmentServiceImpl.class);

    private final UserPerDepartmentRepository userPerDepartmentRepository;

    private final UserPerDepartmentService userPerDepartmentService;

    private final DepartmentRepository departmentRepository;

    private final DepartmentMapper departmentMapper;

    private final UserPerDepartmentMapper userPerDepartmentMapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository,
	    DepartmentMapper departmentMapper,
	    UserPerDepartmentRepository userPerDepartmentRepository,
	    UserPerDepartmentService userPerDepartmentService,
	    UserPerDepartmentMapper userPerDepartmentMapper) {
	this.userPerDepartmentRepository = userPerDepartmentRepository;
	this.userPerDepartmentService = userPerDepartmentService;
	this.departmentRepository = departmentRepository;
	this.departmentMapper = departmentMapper;
	this.userPerDepartmentMapper = userPerDepartmentMapper;
    }

    @Override
    public DepartmentDTO save(DepartmentDTO departmentDTO) {
	log.debug("Request to save Department : {}", departmentDTO);
	Department department = departmentMapper.toEntity(departmentDTO);
	department = departmentRepository.save(department);
	return departmentMapper.toDto(department);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentDTO> findAll(Pageable pageable) {
	log.debug("Request to get all Departments");
	return departmentRepository.findAll(pageable)
		.map(departmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepartmentDTO> findOne(Long id) {
	log.debug("Request to get Department : {}", id);
	return departmentRepository.findById(id).map(departmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
	log.debug("Request to delete Department : {}", id);
	departmentRepository.deleteById(id);
    }

    @Override
    public List<DepartmentDTO> getMyDeps() {
	return userPerDepartmentRepository.findByRealUserIsCurrentUser()
		.stream().map(u -> u.getDepartment())
		.map(departmentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public DepartmentDTO join(Long departmentId, String password,
	    UserPerDepartmentDTO u) {
	if (!this.findOne(departmentId).isPresent())
	    throw new EntityNotFoundException("department not found");
	var dep = this.findOne(departmentId).get();
	if (!dep.getPassword().equals(password))
	    throw new InvalidParameterException("department password is wrong");

	this.userPerDepartmentService.save(u);
	return this.findOne(departmentId).get();
    }

    @Override
    public Page<UserPerDepartmentDTO> getDepartmentUsers(Long id,
	    Pageable pageable) {
	return this.userPerDepartmentRepository.findByDepatment(id, pageable)
		.map(userPerDepartmentMapper::toDto);
    }

}
