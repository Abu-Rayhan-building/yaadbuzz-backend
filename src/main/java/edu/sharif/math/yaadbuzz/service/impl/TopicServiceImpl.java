package edu.sharif.math.yaadbuzz.service.impl;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadbuzz.domain.Topic;
import edu.sharif.math.yaadbuzz.repository.TopicRepository;
import edu.sharif.math.yaadbuzz.service.DepartmentService;
import edu.sharif.math.yaadbuzz.service.TopicRatingService;
import edu.sharif.math.yaadbuzz.service.TopicService;
import edu.sharif.math.yaadbuzz.service.UserPerDepartmentService;
import edu.sharif.math.yaadbuzz.service.dto.TopicDTO;
import edu.sharif.math.yaadbuzz.service.dto.helpers.TopicVoteUDTO;
import edu.sharif.math.yaadbuzz.service.mapper.TopicMapper;

/**
 * Service Implementation for managing {@link Topic}.
 */
@Service
@Transactional
public class TopicServiceImpl implements TopicService {

    private final Logger log = LoggerFactory.getLogger(TopicServiceImpl.class);

    private final TopicRepository topicRepository;

    private final TopicMapper topicMapper;

    private final DepartmentService departmentService;
    private final TopicRatingService topicRatingService;

    private final UserPerDepartmentService userPerDepartmentService;

    public TopicServiceImpl(final TopicRepository topicRepository,
	    final TopicMapper topicMapper,
	    final DepartmentService departmentService,
	    UserPerDepartmentService userPerDepartmentService,
	    TopicRatingService topicRatingService) {
	this.topicRepository = topicRepository;
	this.topicMapper = topicMapper;
	this.departmentService = departmentService;
	this.topicRatingService = topicRatingService;
	this.userPerDepartmentService = userPerDepartmentService;
    }

    @Override
    public boolean currentuserHasCreateAccess(final Long departmentId) {
	return this.departmentService.currentuserHasGetAccess(departmentId);
    }

    @Override
    public boolean currentuserHasGetAccess(final Long id) {
	return this.departmentService.currentuserHasGetAccess(
		this.findOne(id).get().getDepartmentId());
    }

    @Override
    public boolean currentuserHasUpdateAccess(final Long id) {
	return false;
    }

    @Override
    public boolean currentuserHasVoteAccess(final Long id) {
	return this.departmentService.currentuserHasGetAccess(
		this.findOne(id).get().getDepartmentId());
    }

    @Override
    public void delete(final Long id) {
	this.log.debug("Request to delete Topic : {}", id);
	this.topicRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TopicDTO> findAll(final Pageable pageable) {
	this.log.debug("Request to get all Topics");
	return this.topicRepository.findAll(pageable)
		.map(this.topicMapper::toDto);
    }

    @Override
    public Page<TopicDTO> findAllWithEagerRelationships(
	    final Pageable pageable) {
	return this.topicRepository.findAllWithEagerRelationships(pageable)
		.map(this.topicMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TopicDTO> findOne(final Long id) {
	this.log.debug("Request to get Topic : {}", id);
	return this.topicRepository.findOneWithEagerRelationships(id)
		.map(this.topicMapper::toDto);
    }

    @Override
    public TopicDTO save(final TopicDTO topicDTO) {
	this.log.debug("Request to save Topic : {}", topicDTO);
	Topic topic = this.topicMapper.toEntity(topicDTO);
	topic = this.topicRepository.save(topic);
	return this.topicMapper.toDto(topic);
    }

    @Override
    public TopicDTO vote(Long depId, @Valid TopicVoteUDTO topicVoteUDTO) {
	var upd = this.userPerDepartmentService.getCurrentUserInDep(depId);
	var topic = this.findOne(topicVoteUDTO.getTopicId()).get();
	topic.getVoters().add(upd);
	this.save(topic);
	for (Long ballot : topicVoteUDTO.getBallots()) {
	    this.topicRatingService.addBallot(topic.getId(), ballot);
	}
	return topic;
    }
}
