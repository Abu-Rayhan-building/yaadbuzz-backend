package edu.sharif.math.yaadbuzz.service.impl;

import edu.sharif.math.yaadbuzz.domain.Department;
import edu.sharif.math.yaadbuzz.repository.DepartmentRepository;
import edu.sharif.math.yaadbuzz.service.DepartmentService;
import edu.sharif.math.yaadbuzz.service.UserExtraService;
import edu.sharif.math.yaadbuzz.service.dto.DepartmentDTO;
import edu.sharif.math.yaadbuzz.service.mapper.DepartmentMapper;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadbuzz.domain.Department;
import edu.sharif.math.yaadbuzz.domain.UserPerDepartment;
import edu.sharif.math.yaadbuzz.repository.DepartmentRepository;
import edu.sharif.math.yaadbuzz.repository.UserPerDepartmentRepository;
import edu.sharif.math.yaadbuzz.service.DepartmentService;
import edu.sharif.math.yaadbuzz.service.UserPerDepartmentService;
import edu.sharif.math.yaadbuzz.service.UserService;
import edu.sharif.math.yaadbuzz.service.dto.DepartmentDTO;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;
import edu.sharif.math.yaadbuzz.service.mapper.DepartmentMapper;
import edu.sharif.math.yaadbuzz.service.mapper.UserPerDepartmentMapper;
import edu.sharif.math.yaadbuzz.web.rest.dto.MyUserPerDepartmentStatsDTO;

/**
 * Service Implementation for managing {@link Department}.
 */
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final Logger log = LoggerFactory
	    .getLogger(DepartmentServiceImpl.class);

    private final UserPerDepartmentRepository userPerDepartmentRepository;

    private UserPerDepartmentService userPerDepartmentService;

    private final DepartmentRepository departmentRepository;

    private final DepartmentMapper departmentMapper;

    private final UserPerDepartmentMapper userPerDepartmentMapper;

    private final UserService userService;

    @Autowired
    private UserExtraService userExtraService;

    public DepartmentServiceImpl(
	    final DepartmentRepository departmentRepository,
	    final DepartmentMapper departmentMapper,
	    final UserPerDepartmentRepository userPerDepartmentRepository,
	    final UserPerDepartmentMapper userPerDepartmentMapper,
	    final UserService userService) {
	this.userPerDepartmentRepository = userPerDepartmentRepository;
	this.departmentRepository = departmentRepository;
	this.departmentMapper = departmentMapper;
	this.userPerDepartmentMapper = userPerDepartmentMapper;
	this.userService = userService;
    }

    @Autowired
    public void setUserPerDepartmentService(
	    UserPerDepartmentService userPerDepartmentService) {
	this.userPerDepartmentService = userPerDepartmentService;
    }

    @Override
    public boolean currentuserHasGetAccess(final Long id) {
	final var currentUserId = this.userService.getCurrentUserId();
	final var dep = this.departmentRepository.findById(id).get();
	return dep.getUserPerDepartments().parallelStream().anyMatch(upd -> {

	    return upd.getRealUser().getId().equals(currentUserId);
	});
    }

    @Override
    public boolean currentuserHasUpdateAccess(final Long id) {
	final var currentUserId = this.userService.getCurrentUserId();
	return this.departmentRepository.findById(id).get().getOwner().getId()
		.equals(currentUserId);
    }

    @Override
    public DepartmentDTO save(DepartmentDTO departmentDTO) {
	log.debug("Request to save Department : {}", departmentDTO);
	Department department = departmentMapper.toEntity(departmentDTO);
	department = departmentRepository.save(department);
	return departmentMapper.toDto(department);
    }

    @Override
    public Optional<DepartmentDTO> partialUpdate(DepartmentDTO departmentDTO) {
	log.debug("Request to partially update Department : {}", departmentDTO);

	return departmentRepository.findById(departmentDTO.getId())
		.map(existingDepartment -> {
		    if (departmentDTO.getName() != null) {
			existingDepartment.setName(departmentDTO.getName());
		    }

		    if (departmentDTO.getPassword() != null) {
			existingDepartment
				.setPassword(departmentDTO.getPassword());
		    }

		    return existingDepartment;
		}).map(departmentRepository::save).map(departmentMapper::toDto);
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
    public Page<UserPerDepartmentDTO> getDepartmentUsers(final Long id,
	    final Pageable pageable) {
	Page<UserPerDepartmentDTO> res = this.userPerDepartmentRepository
		.findByDepatment(id, pageable)
		.map(this.userPerDepartmentMapper::toDto);
	// todo maybe more effecient ways
	res.map(upd -> overwriteFromDefault(upd));
	return res;
    }

    UserPerDepartmentDTO overwriteFromDefault(UserPerDepartmentDTO upd) {
	if (upd.getAvatar() != null && upd.getBio() != null
		&& upd.getNickname() != null)
	    return upd;
	var ux = userExtraService.findOne(upd.getRealUser().getId()).get()
		.getDefaultUserPerDepartment();
	if (upd.getAvatar() == null) {
	    upd.setAvatar(ux.getAvatar());
	}
	if (upd.getBio() == null) {
	    upd.setBio(ux.getBio());
	}
	if (upd.getNickname() == null) {
	    upd.setNickname(ux.getNickname());
	}
	return upd;
    }

    @Override
    public List<DepartmentDTO> getMyDeps() {
	return this.userPerDepartmentRepository.findByRealUserIsCurrentUser()
		.stream().map(UserPerDepartment::getDepartment)
		.map(this.departmentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public DepartmentDTO join(final Long departmentId, final String password,
	    UserPerDepartmentDTO u) {
	if (!this.findOne(departmentId).isPresent()) {
	    throw new EntityNotFoundException("department not found");
	}
	final var dep = this.findOne(departmentId).get();
	if (!dep.getPassword().equals(password)) {
	    throw new InvalidParameterException("department password is wrong");
	}
	// fuck
	u = overwriteFromDefault(u);
	this.userPerDepartmentService.save(u);
	return this.findOne(departmentId).get();
    }

    @Override
    public void delete(Long id) {
	log.debug("Request to delete Department : {}", id);
	departmentRepository.deleteById(id);
    }

    @Override
    public MyUserPerDepartmentStatsDTO getMyStatsInDep(Long depId) {
	return userPerDepartmentService.getCurrentUserStatsInDep(depId);

    }

    @Override
    public List<UserPerDepartmentDTO> getAllDepartmentUsers(Long depid) {
	return this.userPerDepartmentRepository.findByDepatment(depid).stream()
		.map(this.userPerDepartmentMapper::toDto)
		.collect(Collectors.toList());
    }
}
