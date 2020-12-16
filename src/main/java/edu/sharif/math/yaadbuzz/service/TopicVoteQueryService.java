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

import edu.sharif.math.yaadbuzz.domain.TopicVote;
import edu.sharif.math.yaadbuzz.domain.*; // for static metamodels
import edu.sharif.math.yaadbuzz.repository.TopicVoteRepository;
import edu.sharif.math.yaadbuzz.service.dto.TopicVoteCriteria;
import edu.sharif.math.yaadbuzz.service.dto.TopicVoteDTO;
import edu.sharif.math.yaadbuzz.service.mapper.TopicVoteMapper;

/**
 * Service for executing complex queries for {@link TopicVote} entities in the database.
 * The main input is a {@link TopicVoteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TopicVoteDTO} or a {@link Page} of {@link TopicVoteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TopicVoteQueryService extends QueryService<TopicVote> {

    private final Logger log = LoggerFactory.getLogger(TopicVoteQueryService.class);

    private final TopicVoteRepository topicVoteRepository;

    private final TopicVoteMapper topicVoteMapper;

    public TopicVoteQueryService(TopicVoteRepository topicVoteRepository, TopicVoteMapper topicVoteMapper) {
        this.topicVoteRepository = topicVoteRepository;
        this.topicVoteMapper = topicVoteMapper;
    }

    /**
     * Return a {@link List} of {@link TopicVoteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TopicVoteDTO> findByCriteria(TopicVoteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TopicVote> specification = createSpecification(criteria);
        return topicVoteMapper.toDto(topicVoteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TopicVoteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TopicVoteDTO> findByCriteria(TopicVoteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TopicVote> specification = createSpecification(criteria);
        return topicVoteRepository.findAll(specification, page)
            .map(topicVoteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TopicVoteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TopicVote> specification = createSpecification(criteria);
        return topicVoteRepository.count(specification);
    }

    /**
     * Function to convert {@link TopicVoteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TopicVote> createSpecification(TopicVoteCriteria criteria) {
        Specification<TopicVote> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TopicVote_.id));
            }
            if (criteria.getRepetitions() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRepetitions(), TopicVote_.repetitions));
            }
            if (criteria.getTopicId() != null) {
                specification = specification.and(buildSpecification(criteria.getTopicId(),
                    root -> root.join(TopicVote_.topic, JoinType.LEFT).get(Topic_.id)));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(TopicVote_.user, JoinType.LEFT).get(UserPerDepartment_.id)));
            }
        }
        return specification;
    }
}
