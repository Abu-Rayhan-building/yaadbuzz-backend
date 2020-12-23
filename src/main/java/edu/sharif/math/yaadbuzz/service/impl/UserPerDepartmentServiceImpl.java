package edu.sharif.math.yaadbuzz.service.impl;

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
import edu.sharif.math.yaadbuzz.service.dto.helpers.MyUserPerDepartmentStatsDTO;
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
		    if (tr.getTopicId().equals(t.getId())) {
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
		    if (mem.getRecipientId().equals(upd.getId())) {
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
	return this.getCurrentUserInDep(depid).getId();
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

    @Autowired
    public void setDepartmentService(
	    final DepartmentService departmentService) {
	this.departmentService = departmentService;
    }
}
