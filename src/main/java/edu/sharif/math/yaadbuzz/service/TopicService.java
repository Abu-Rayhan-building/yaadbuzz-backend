package edu.sharif.math.yaadbuzz.service;

import edu.sharif.math.yaadbuzz.domain.Topic;
import edu.sharif.math.yaadbuzz.domain.Topic;
import edu.sharif.math.yaadbuzz.repository.TopicRepository;
import edu.sharif.math.yaadbuzz.repository.TopicRepository;
import edu.sharif.math.yaadbuzz.service.*;
import edu.sharif.math.yaadbuzz.service.TopicService;
import edu.sharif.math.yaadbuzz.service.dto.TopicDTO;
import edu.sharif.math.yaadbuzz.service.dto.TopicDTO;
import edu.sharif.math.yaadbuzz.service.mapper.TopicMapper;
import edu.sharif.math.yaadbuzz.service.mapper.TopicMapper;
import edu.sharif.math.yaadbuzz.web.rest.dto.TopicVoteUDTO;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Topic}.
 */
@Service
@Transactional
public class TopicService implements ServiceWithCurrentUserCrudAccess {

    private final Logger log = LoggerFactory.getLogger(TopicService.class);

    private final TopicRepository topicRepository;

    private final TopicMapper topicMapper;

    private final DepartmentService departmentService;
    private final TopicVoteService topicVoteService;

    private final UserPerDepartmentService userPerDepartmentService;

    public TopicService(
        final TopicRepository topicRepository,
        final TopicMapper topicMapper,
        final DepartmentService departmentService,
        UserPerDepartmentService userPerDepartmentService,
        TopicVoteService topicVoteService
    ) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
        this.departmentService = departmentService;
        this.topicVoteService = topicVoteService;
        this.userPerDepartmentService = userPerDepartmentService;
    }

    public boolean currentuserHasCreateAccess(final Long departmentId) {
        return this.departmentService.currentuserHasGetAccess(departmentId);
    }

    @Override
    public boolean currentuserHasGetAccess(final Long id) {
        return this.departmentService.currentuserHasGetAccess(this.findOne(id).get().getDepartment().getId());
    }

    @Override
    public boolean currentuserHasUpdateAccess(final Long id) {
        return false;
    }

    public boolean currentuserHasVoteAccess(final Long id) {
        return this.departmentService.currentuserHasGetAccess(this.findOne(id).get().getDepartment().getId());
    }

    public TopicDTO save(TopicDTO topicDTO) {
        log.debug("Request to save Topic : {}", topicDTO);
        Topic topic = topicMapper.toEntity(topicDTO);
        topic = topicRepository.save(topic);
        return topicMapper.toDto(topic);
    }

    public Optional<TopicDTO> partialUpdate(TopicDTO topicDTO) {
        log.debug("Request to partially update Topic : {}", topicDTO);

        return topicRepository
            .findById(topicDTO.getId())
            .map(
                existingTopic -> {
                    if (topicDTO.getTitle() != null) {
                        existingTopic.setTitle(topicDTO.getTitle());
                    }

                    return existingTopic;
                }
            )
            .map(topicRepository::save)
            .map(topicMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<TopicDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Topics");
        return topicRepository.findAll(pageable).map(topicMapper::toDto);
    }

    public Page<TopicDTO> findAllWithEagerRelationships(Pageable pageable) {
        return topicRepository.findAllWithEagerRelationships(pageable).map(topicMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<TopicDTO> findOne(Long id) {
        log.debug("Request to get Topic : {}", id);
        return topicRepository.findOneWithEagerRelationships(id).map(topicMapper::toDto);
    }

    public void delete(Long id) {
        log.debug("Request to delete Topic : {}", id);
        topicRepository.deleteById(id);
    }

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
