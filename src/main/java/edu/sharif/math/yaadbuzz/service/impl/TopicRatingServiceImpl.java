package edu.sharif.math.yaadbuzz.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadbuzz.domain.TopicRating;
import edu.sharif.math.yaadbuzz.repository.TopicRatingRepository;
import edu.sharif.math.yaadbuzz.service.DepartmentService;
import edu.sharif.math.yaadbuzz.service.TopicRatingService;
import edu.sharif.math.yaadbuzz.service.UserPerDepartmentService;
import edu.sharif.math.yaadbuzz.service.dto.TopicRatingDTO;
import edu.sharif.math.yaadbuzz.service.mapper.TopicRatingMapper;

/**
 * Service Implementation for managing {@link TopicRating}.
 */
@Service
@Transactional
public class TopicRatingServiceImpl implements TopicRatingService {

    private final Logger log = LoggerFactory
	    .getLogger(TopicRatingServiceImpl.class);

    private final TopicRatingRepository topicRatingRepository;

    private final TopicRatingMapper topicRatingMapper;

    private final DepartmentService departmentService;

    private final UserPerDepartmentService userPerDepartmentService;

    public TopicRatingServiceImpl(
	    final TopicRatingRepository topicRatingRepository,
	    final TopicRatingMapper topicRatingMapper,
	    final UserPerDepartmentService userPerDepartmentService,
	    final DepartmentService departmentService) {
	this.topicRatingRepository = topicRatingRepository;
	this.topicRatingMapper = topicRatingMapper;
	this.departmentService = departmentService;
	this.userPerDepartmentService = userPerDepartmentService;
    }

    @Override
    public boolean currentuserHasGetAccess(final Long id) {
	return this.departmentService
		.currentuserHasGetAccess(this.userPerDepartmentService
			.findOne(this.findOne(id).get().getUserId()).get()
			.getDepartmentId());
    }

    @Override
    public boolean currentuserHasUpdateAccess(final Long id) {
	return false;
    }

    @Override
    public void delete(final Long id) {
	this.log.debug("Request to delete TopicRating : {}", id);
	this.topicRatingRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TopicRatingDTO> findAll(final Pageable pageable) {
	this.log.debug("Request to get all TopicRatings");
	return this.topicRatingRepository.findAll(pageable)
		.map(this.topicRatingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TopicRatingDTO> findOne(final Long id) {
	this.log.debug("Request to get TopicRating : {}", id);
	return this.topicRatingRepository.findById(id)
		.map(this.topicRatingMapper::toDto);
    }

    @Override
    public TopicRatingDTO save(final TopicRatingDTO topicRatingDTO) {
	this.log.debug("Request to save TopicRating : {}", topicRatingDTO);
	TopicRating topicRating = this.topicRatingMapper
		.toEntity(topicRatingDTO);
	topicRating = this.topicRatingRepository.save(topicRating);
	return this.topicRatingMapper.toDto(topicRating);
    }
}
