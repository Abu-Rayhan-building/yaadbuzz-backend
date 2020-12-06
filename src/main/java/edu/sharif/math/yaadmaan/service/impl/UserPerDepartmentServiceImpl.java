package edu.sharif.math.yaadmaan.service.impl;

import java.util.HashSet;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadmaan.domain.UserPerDepartment;
import edu.sharif.math.yaadmaan.repository.TopicRepository;
import edu.sharif.math.yaadmaan.repository.UserPerDepartmentRepository;
import edu.sharif.math.yaadmaan.service.DepartmentService;
import edu.sharif.math.yaadmaan.service.MemorialQueryService;
import edu.sharif.math.yaadmaan.service.TopicQueryService;
import edu.sharif.math.yaadmaan.service.TopicRatingQueryService;
import edu.sharif.math.yaadmaan.service.TopicService;
import edu.sharif.math.yaadmaan.service.UserPerDepartmentService;
import edu.sharif.math.yaadmaan.service.UserService;
import edu.sharif.math.yaadmaan.service.dto.MemorialCriteria;
import edu.sharif.math.yaadmaan.service.dto.TopicCriteria;
import edu.sharif.math.yaadmaan.service.dto.TopicRatingCriteria;
import edu.sharif.math.yaadmaan.service.dto.UserPerDepartmentDTO;
import edu.sharif.math.yaadmaan.service.dto.helpers.MyUserPerDepartmentStatsDTO;
import edu.sharif.math.yaadmaan.service.mapper.UserPerDepartmentMapper;
import io.github.jhipster.service.filter.LongFilter;

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
	    final UserService userService,
	    TopicRatingQueryService topicRatingQueryService,
	    TopicQueryService topicQueryService,
	    MemorialQueryService memorialQueryService) {
	this.userPerDepartmentRepository = userPerDepartmentRepository;
	this.userPerDepartmentMapper = userPerDepartmentMapper;
	this.userService = userService;
	this.topicQueryService = topicQueryService;
	this.topicRatingQueryService = topicRatingQueryService;
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

    private final TopicQueryService topicQueryService;
    private final TopicRatingQueryService topicRatingQueryService;
    private final MemorialQueryService memorialQueryService;

    // fuck
    @Override
    public MyUserPerDepartmentStatsDTO getCurrentUserStatsInDep(
	    final Long depid) {
	var res = new MyUserPerDepartmentStatsDTO();
	var currentUserId = userPerDepartmentRepository
		.getCurrentUserInDep(depid).getId();

	{
	    var topicsNotVotedYet = new HashSet<Long>();
	    TopicCriteria depCriteria = new TopicCriteria();
	    var longFilter = new LongFilter();
	    longFilter.setEquals(depid);
	    depCriteria.setDepartmentId(longFilter);
	    var topics = topicQueryService.findByCriteria(depCriteria);
	    var userIdFilter = new LongFilter();
	    userIdFilter.setEquals(currentUserId);
	    TopicRatingCriteria userCriteria = new TopicRatingCriteria();
	    userCriteria.setUserId(userIdFilter);
	    var voted = topicRatingQueryService.findByCriteria(userCriteria);

	    topics.stream().forEach(t -> {
		boolean[] flag = new boolean[1];
		voted.forEach(tr -> {
		    if (tr.getTopicId().equals(t.getId()))
			flag[0] = true;
		});
		if (flag[0] == false) {
		    topicsNotVotedYet.add(t.getId());
		}
	    });

	    res.setTopicsNotVotedYet(topicsNotVotedYet);
	}

	{
	    MemorialCriteria mc = new MemorialCriteria();
	    var writerIdFilter = new LongFilter();
	    writerIdFilter.setEquals(currentUserId);
	    mc.setWriterId(writerIdFilter);
	    var memorials = memorialQueryService.findByCriteria(mc);
	    var allUsers = departmentService.getAllDepartmentUsers(depid);
	    var userPerDepartmentNotWritedMemoryFor = new HashSet<Long>();
	    allUsers.forEach(upd -> {
		boolean[] flag = new boolean[1];
		memorials.forEach(mem -> {
		    if (mem.getRecipientId().equals(upd.getId()))
			flag[0] = true;
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
