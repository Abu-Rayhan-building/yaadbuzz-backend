package edu.sharif.math.yaadbuzz.service.impl;

import edu.sharif.math.yaadbuzz.domain.UserPerDepartment;
import edu.sharif.math.yaadbuzz.repository.UserPerDepartmentRepository;
import edu.sharif.math.yaadbuzz.service.UserPerDepartmentService;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;
import edu.sharif.math.yaadbuzz.service.mapper.UserPerDepartmentMapper;
import edu.sharif.math.yaadbuzz.web.rest.dto.MyUserPerDepartmentStatsDTO;

import java.util.HashSet;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadbuzz.domain.UserPerDepartment;
import edu.sharif.math.yaadbuzz.repository.UserPerDepartmentRepository;
import edu.sharif.math.yaadbuzz.service.DepartmentService;
import edu.sharif.math.yaadbuzz.service.MemorialQueryService;
import edu.sharif.math.yaadbuzz.service.TopicQueryService;
import edu.sharif.math.yaadbuzz.service.TopicVoteQueryService;
import edu.sharif.math.yaadbuzz.service.UserPerDepartmentService;
import edu.sharif.math.yaadbuzz.service.UserService;
import edu.sharif.math.yaadbuzz.service.dto.MemorialCriteria;
import edu.sharif.math.yaadbuzz.service.dto.TopicCriteria;
import edu.sharif.math.yaadbuzz.service.dto.TopicVoteCriteria;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;
import edu.sharif.math.yaadbuzz.service.mapper.UserPerDepartmentMapper;
import tech.jhipster.service.filter.LongFilter;

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

    private final UserPerDepartmentMapper userPerDepartmentMapper;

    private final UserService userService;

    private final TopicQueryService topicQueryService;

    private final TopicVoteQueryService topicVoteQueryService;

    private final MemorialQueryService memorialQueryService;

    public UserPerDepartmentServiceImpl(
	    final UserPerDepartmentRepository userPerDepartmentRepository,
	    final UserPerDepartmentMapper userPerDepartmentMapper,
	    final UserService userService,
	    final TopicVoteQueryService topicVoteQueryService,
	    final TopicQueryService topicQueryService,
	    final MemorialQueryService memorialQueryService) {
	this.userPerDepartmentRepository = userPerDepartmentRepository;
	this.userPerDepartmentMapper = userPerDepartmentMapper;
	this.userService = userService;
	this.topicQueryService = topicQueryService;
	this.topicVoteQueryService = topicVoteQueryService;
	this.memorialQueryService = memorialQueryService;
    }

    @Override
    public boolean currentuserHasGetAccess(final Long id) {
	return this.departmentService.currentuserHasGetAccess(
		this.findOne(id).get().getDepartment().getId());
    }

    @Override
    public boolean currentuserHasUpdateAccess(final Long id) {
	return this.findOne(id).get().getRealUser().getId()
		.equals(this.userService.getCurrentUserId());
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
    public Optional<UserPerDepartmentDTO> partialUpdate(
	    UserPerDepartmentDTO userPerDepartmentDTO) {
	log.debug("Request to partially update UserPerDepartment : {}",
		userPerDepartmentDTO);

	return userPerDepartmentRepository
		.findById(userPerDepartmentDTO.getId())
		.map(existingUserPerDepartment -> {
		    if (userPerDepartmentDTO.getNickname() != null) {
			existingUserPerDepartment
				.setNickname(userPerDepartmentDTO.getNickname());
		    }

		    if (userPerDepartmentDTO.getBio() != null) {
			existingUserPerDepartment
				.setBio(userPerDepartmentDTO.getBio());
		    }

		    return existingUserPerDepartment;
		}).map(userPerDepartmentRepository::save)
		.map(userPerDepartmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserPerDepartmentDTO> findAll(Pageable pageable) {
	log.debug("Request to get all UserPerDepartments");
	return userPerDepartmentRepository.findAll(pageable)
		.map(userPerDepartmentMapper::toDto);
    }

    @Override
    public UserPerDepartmentDTO getCurrentUserInDep(final Long depid) {
	return this.userPerDepartmentMapper.toDto(
		this.userPerDepartmentRepository.getCurrentUserInDep(depid));

    }

    // fuck
    @Override
    public MyUserPerDepartmentStatsDTO getCurrentUserStatsInDep(
	    final Long depid) {
	final var res = new MyUserPerDepartmentStatsDTO();
	final var currentUPDId = this.userPerDepartmentRepository
		.getCurrentUserInDep(depid).getId();
	{
	    final var topicsNotVotedYet = new HashSet<Long>();
	    final TopicCriteria depCriteria = new TopicCriteria();
	    final var longFilter = new LongFilter();
	    longFilter.setEquals(depid);
	    depCriteria.setDepartmentId(longFilter);
	    final var topics = this.topicQueryService
		    .findByCriteria(depCriteria);
	    final var userIdFilter = new LongFilter();
	    userIdFilter.setEquals(currentUPDId);
	    final TopicVoteCriteria userCriteria = new TopicVoteCriteria();
	    userCriteria.setUserId(userIdFilter);
	    final var voted = this.topicVoteQueryService
		    .findByCriteria(userCriteria);

	    topics.stream().forEach(t -> {
		final boolean[] flag = new boolean[1];
		voted.forEach(tr -> {
		    if (tr.getTopic().equals(t)) {
			flag[0] = true;
		    }
		});
		if (flag[0] == false) {
		    topicsNotVotedYet.add(t.getId());
		}
	    });

	    res.setTopicsNotVotedYet(topicsNotVotedYet);
	}

	{
	    final MemorialCriteria mc = new MemorialCriteria();
	    final var writerIdFilter = new LongFilter();
	    writerIdFilter.setEquals(currentUPDId);
	    mc.setWriterId(writerIdFilter);
	    final var memorials = this.memorialQueryService.findByCriteria(mc);
	    final var allUsers = this.departmentService
		    .getAllDepartmentUsers(depid);
	    final var userPerDepartmentNotWritedMemoryFor = new HashSet<Long>();
	    allUsers.forEach(upd -> {
		final boolean[] flag = new boolean[1];
		memorials.forEach(mem -> {
		    if (mem.getRecipient().equals(upd)) {
			flag[0] = true;
		    }
		});
		if (flag[0] == false) {
		    userPerDepartmentNotWritedMemoryFor.add(upd.getId());
		}
	    });

	    res.setUserPerDepartmentNotWritedMemoryFor(
		    userPerDepartmentNotWritedMemoryFor);
	}

	return res;
    }

    @Override
    public Long getCurrentUserUserPerDepeartmentIdInDep(final Long depid) {
	return this.getCurrentUserUserPerDepeartmentInDep(depid).getId();
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

    @Autowired
    public void setDepartmentService(
	    final DepartmentService departmentService) {
	this.departmentService = departmentService;
    }

    @Override
    public UserPerDepartmentDTO getCurrentUserUserPerDepeartmentInDep(
	    Long depId) {
	return this.getCurrentUserInDep(depId);
    }
}
