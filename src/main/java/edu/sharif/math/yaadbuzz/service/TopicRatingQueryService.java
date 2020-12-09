package edu.sharif.math.yaadbuzz.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import edu.sharif.math.yaadbuzz.domain.TopicRating;
import edu.sharif.math.yaadbuzz.domain.*; // for static metamodels
import edu.sharif.math.yaadbuzz.repository.TopicRatingRepository;
import edu.sharif.math.yaadbuzz.service.dto.TopicRatingCriteria;
import edu.sharif.math.yaadbuzz.service.dto.TopicRatingDTO;
import edu.sharif.math.yaadbuzz.service.mapper.TopicRatingMapper;

/**
 * Service for executing complex queries for {@link TopicRating} entities in the database.
 * The main input is a {@link TopicRatingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TopicRatingDTO} or a {@link Page} of {@link TopicRatingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TopicRatingQueryService extends QueryService<TopicRating> {

    private final Logger log = LoggerFactory.getLogger(TopicRatingQueryService.class);

    private final TopicRatingRepository topicRatingRepository;

    private final TopicRatingMapper topicRatingMapper;

    public TopicRatingQueryService(TopicRatingRepository topicRatingRepository, TopicRatingMapper topicRatingMapper) {
        this.topicRatingRepository = topicRatingRepository;
        this.topicRatingMapper = topicRatingMapper;
    }

    /**
     * Return a {@link List} of {@link TopicRatingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TopicRatingDTO> findByCriteria(TopicRatingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TopicRating> specification = createSpecification(criteria);
        return topicRatingMapper.toDto(topicRatingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TopicRatingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TopicRatingDTO> findByCriteria(TopicRatingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TopicRating> specification = createSpecification(criteria);
        return topicRatingRepository.findAll(specification, page)
            .map(topicRatingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TopicRatingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TopicRating> specification = createSpecification(criteria);
        return topicRatingRepository.count(specification);
    }

    /**
     * Function to convert {@link TopicRatingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TopicRating> createSpecification(TopicRatingCriteria criteria) {
        Specification<TopicRating> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TopicRating_.id));
            }
            if (criteria.getRepetitions() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRepetitions(), TopicRating_.repetitions));
            }
            if (criteria.getTopicId() != null) {
                specification = specification.and(buildSpecification(criteria.getTopicId(),
                    root -> root.join(TopicRating_.topic, JoinType.LEFT).get(Topic_.id)));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(TopicRating_.user, JoinType.LEFT).get(UserPerDepartment_.id)));
            }
        }
        return specification;
    }
}
