package edu.sharif.math.yaadbuzz.service.impl;

import edu.sharif.math.yaadbuzz.domain.Topic;
import edu.sharif.math.yaadbuzz.repository.TopicRepository;
import edu.sharif.math.yaadbuzz.service.TopicService;
import edu.sharif.math.yaadbuzz.service.dto.TopicDTO;
import edu.sharif.math.yaadbuzz.service.mapper.TopicMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadbuzz.domain.Topic;
import edu.sharif.math.yaadbuzz.repository.TopicRepository;
import edu.sharif.math.yaadbuzz.service.DepartmentService;
import edu.sharif.math.yaadbuzz.service.TopicService;
import edu.sharif.math.yaadbuzz.service.TopicVoteService;
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
    private final TopicVoteService topicVoteService;

    private final UserPerDepartmentService userPerDepartmentService;

    public TopicServiceImpl(final TopicRepository topicRepository,
	    final TopicMapper topicMapper,
	    final DepartmentService departmentService,
	    UserPerDepartmentService userPerDepartmentService,
	    TopicVoteService topicVoteService) {
	this.topicRepository = topicRepository;
	this.topicMapper = topicMapper;
	this.departmentService = departmentService;
	this.topicVoteService = topicVoteService;
	this.userPerDepartmentService = userPerDepartmentService;
    }

    @Override
    public boolean currentuserHasCreateAccess(final Long departmentId) {
	return this.departmentService.currentuserHasGetAccess(departmentId);
    }

    @Override
    public boolean currentuserHasGetAccess(final Long id) {
	return this.departmentService.currentuserHasGetAccess(
		this.findOne(id).get().getDepartment().getId());
    }

    @Override
    public boolean currentuserHasUpdateAccess(final Long id) {
	return false;
    }

    @Override
    public boolean currentuserHasVoteAccess(final Long id) {
	return this.departmentService.currentuserHasGetAccess(
		this.findOne(id).get().getDepartment().getId());
    }

    @Override
    public TopicDTO save(TopicDTO topicDTO) {
	log.debug("Request to save Topic : {}", topicDTO);
	Topic topic = topicMapper.toEntity(topicDTO);
	topic = topicRepository.save(topic);
	return topicMapper.toDto(topic);
    }

    @Override
    public Optional<TopicDTO> partialUpdate(TopicDTO topicDTO) {
	log.debug("Request to partially update Topic : {}", topicDTO);

	return topicRepository.findById(topicDTO.getId()).map(existingTopic -> {
	    if (topicDTO.getTitle() != null) {
		existingTopic.setTitle(topicDTO.getTitle());
	    }

	    return existingTopic;
	}).map(topicRepository::save).map(topicMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TopicDTO> findAll(Pageable pageable) {
	log.debug("Request to get all Topics");
	return topicRepository.findAll(pageable).map(topicMapper::toDto);
    }

    public Page<TopicDTO> findAllWithEagerRelationships(Pageable pageable) {
	return topicRepository.findAllWithEagerRelationships(pageable)
		.map(topicMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TopicDTO> findOne(Long id) {
	log.debug("Request to get Topic : {}", id);
	return topicRepository.findOneWithEagerRelationships(id)
		.map(topicMapper::toDto);
    }

    @Override
    public void delete(Long id) {
	log.debug("Request to delete Topic : {}", id);
	topicRepository.deleteById(id);
    }

    @Override
    public TopicDTO vote(Long depId, TopicVoteUDTO topicVoteUDTO) {
	var upd = this.userPerDepartmentService.getCurrentUserInDep(depId);
	var topic = this.findOne(topicVoteUDTO.getTopicId()).get();
	topic.getVoters().add(upd);
	this.save(topic);
	for (Long ballot : topicVoteUDTO.getBallots()) {
	    this.topicVoteService.addBallot(topic.getId(), ballot);
	}
	return topic;

    }
}
