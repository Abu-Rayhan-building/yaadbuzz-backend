package edu.sharif.math.yaadmaan.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadmaan.domain.Topic;
import edu.sharif.math.yaadmaan.repository.TopicRepository;
import edu.sharif.math.yaadmaan.service.TopicService;
import edu.sharif.math.yaadmaan.service.dto.TopicDTO;
import edu.sharif.math.yaadmaan.service.mapper.TopicMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Topic}.
 */
@Service
@Transactional
public class TopicServiceImpl implements TopicService {

    private final Logger log = LoggerFactory.getLogger(TopicServiceImpl.class);

    private final TopicRepository topicRepository;

    private final TopicMapper topicMapper;

    public TopicServiceImpl(TopicRepository topicRepository, TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
    }

    @Override
    public TopicDTO save(TopicDTO topicDTO) {
        log.debug("Request to save Topic : {}", topicDTO);
        Topic topic = topicMapper.toEntity(topicDTO);
        topic = topicRepository.save(topic);
        return topicMapper.toDto(topic);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopicDTO> findAll() {
        log.debug("Request to get all Topics");
        return topicRepository.findAllWithEagerRelationships().stream()
            .map(topicMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    public Page<TopicDTO> findAllWithEagerRelationships(Pageable pageable) {
        return topicRepository.findAllWithEagerRelationships(pageable).map(topicMapper::toDto);
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
}
