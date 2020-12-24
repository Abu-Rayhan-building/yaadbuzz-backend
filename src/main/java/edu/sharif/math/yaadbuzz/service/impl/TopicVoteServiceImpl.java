package edu.sharif.math.yaadbuzz.service.impl;

import edu.sharif.math.yaadbuzz.domain.TopicVote;
import edu.sharif.math.yaadbuzz.repository.TopicVoteRepository;
import edu.sharif.math.yaadbuzz.service.DepartmentService;
import edu.sharif.math.yaadbuzz.service.TopicVoteService;
import edu.sharif.math.yaadbuzz.service.UserPerDepartmentService;
import edu.sharif.math.yaadbuzz.service.dto.TopicDTO;
import edu.sharif.math.yaadbuzz.service.dto.TopicVoteDTO;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;
import edu.sharif.math.yaadbuzz.service.mapper.TopicVoteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TopicVote}.
 */
@Service
@Transactional
public class TopicVoteServiceImpl implements TopicVoteService {

    private final Logger log = LoggerFactory
	    .getLogger(TopicVoteServiceImpl.class);

    private final TopicVoteRepository topicVoteRepository;

    private final TopicVoteMapper topicVoteMapper;

    public TopicVoteServiceImpl(TopicVoteRepository topicVoteRepository,
	    TopicVoteMapper topicVoteMapper,
	    UserPerDepartmentService userPerDepartmentService,
	    DepartmentService departmentService) {
	this.topicVoteRepository = topicVoteRepository;
	this.topicVoteMapper = topicVoteMapper;
	this.departmentService = departmentService;
	this.userPerDepartmentService = userPerDepartmentService;
    }

    @Override
    public TopicVoteDTO save(TopicVoteDTO topicVoteDTO) {
	log.debug("Request to save TopicVote : {}", topicVoteDTO);
	TopicVote topicVote = topicVoteMapper.toEntity(topicVoteDTO);
	topicVote = topicVoteRepository.save(topicVote);
	return topicVoteMapper.toDto(topicVote);
    }

    @Override
    public Optional<TopicVoteDTO> partialUpdate(TopicVoteDTO topicVoteDTO) {
	log.debug("Request to partially update TopicVote : {}", topicVoteDTO);

	return topicVoteRepository.findById(topicVoteDTO.getId())
		.map(existingTopicVote -> {
		    if (topicVoteDTO.getRepetitions() != null) {
			existingTopicVote
				.setRepetitions(topicVoteDTO.getRepetitions());
		    }

		    return existingTopicVote;
		}).map(topicVoteRepository::save).map(topicVoteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TopicVoteDTO> findAll(Pageable pageable) {
	log.debug("Request to get all TopicVotes");
	return topicVoteRepository.findAll(pageable)
		.map(topicVoteMapper::toDto);
    }

    private final DepartmentService departmentService;

    private final UserPerDepartmentService userPerDepartmentService;

    @Override
    public boolean currentuserHasGetAccess(final Long id) {
	return this.departmentService
		.currentuserHasGetAccess(this.userPerDepartmentService
			.findOne(this.findOne(id).get().getUser().getId()).get()
			.getDepartment().getId());
    }

    @Override
    public boolean currentuserHasUpdateAccess(final Long id) {
	return false;
    }

    @Override
    public TopicVoteDTO addBallot(Long topicId, Long ballotForUPD) {
	TopicVoteDTO old = this.findOne(topicId, ballotForUPD)
		.orElseGet(() -> null);
	if (old == null) {
	    old = new TopicVoteDTO();
	    {
		var topic = new TopicDTO();
		topic.setId(topicId);
		old.setTopic(topic);
	    }
	    {
		var upd = new UserPerDepartmentDTO();
		upd.setId(ballotForUPD);
		old.setUser(upd);
	    }
	    old.setRepetitions(0);
	}
	old.setRepetitions(old.getRepetitions() + 1);
	return this.save(old);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TopicVoteDTO> findOne(Long id) {
	log.debug("Request to get TopicVote : {}", id);
	return topicVoteRepository.findById(id).map(topicVoteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
	log.debug("Request to delete TopicVote : {}", id);
	topicVoteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TopicVoteDTO> findOne(Long topicId, Long ballotForUPD) {
	return this.topicVoteRepository.findOne(topicId, ballotForUPD)
		.map(this.topicVoteMapper::toDto);
    }
}
