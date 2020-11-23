package edu.sharif.math.yaadmaan.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadmaan.domain.TopicRating;
import edu.sharif.math.yaadmaan.repository.TopicRatingRepository;
import edu.sharif.math.yaadmaan.service.TopicRatingService;
import edu.sharif.math.yaadmaan.service.dto.TopicRatingDTO;
import edu.sharif.math.yaadmaan.service.mapper.TopicRatingMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link TopicRating}.
 */
@Service
@Transactional
public class TopicRatingServiceImpl implements TopicRatingService {

    private final Logger log = LoggerFactory.getLogger(TopicRatingServiceImpl.class);

    private final TopicRatingRepository topicRatingRepository;

    private final TopicRatingMapper topicRatingMapper;

    public TopicRatingServiceImpl(TopicRatingRepository topicRatingRepository, TopicRatingMapper topicRatingMapper) {
        this.topicRatingRepository = topicRatingRepository;
        this.topicRatingMapper = topicRatingMapper;
    }

    @Override
    public TopicRatingDTO save(TopicRatingDTO topicRatingDTO) {
        log.debug("Request to save TopicRating : {}", topicRatingDTO);
        TopicRating topicRating = topicRatingMapper.toEntity(topicRatingDTO);
        topicRating = topicRatingRepository.save(topicRating);
        return topicRatingMapper.toDto(topicRating);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopicRatingDTO> findAll() {
        log.debug("Request to get all TopicRatings");
        return topicRatingRepository.findAll().stream()
            .map(topicRatingMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<TopicRatingDTO> findOne(Long id) {
        log.debug("Request to get TopicRating : {}", id);
        return topicRatingRepository.findById(id)
            .map(topicRatingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TopicRating : {}", id);
        topicRatingRepository.deleteById(id);
    }
}
