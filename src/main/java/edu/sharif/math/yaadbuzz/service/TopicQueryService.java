package edu.sharif.math.yaadbuzz.service;

import edu.sharif.math.yaadbuzz.domain.*; // for static metamodels
import edu.sharif.math.yaadbuzz.domain.Topic;
import edu.sharif.math.yaadbuzz.repository.TopicRepository;
import edu.sharif.math.yaadbuzz.service.dto.TopicCriteria;
import edu.sharif.math.yaadbuzz.service.dto.TopicDTO;
import edu.sharif.math.yaadbuzz.service.mapper.TopicMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Topic} entities in the database.
 * The main input is a {@link TopicCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TopicDTO} or a {@link Page} of {@link TopicDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TopicQueryService extends QueryService<Topic> {

    private final Logger log = LoggerFactory.getLogger(TopicQueryService.class);

    private final TopicRepository topicRepository;

    private final TopicMapper topicMapper;

    public TopicQueryService(TopicRepository topicRepository, TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
    }

    /**
     * Return a {@link List} of {@link TopicDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TopicDTO> findByCriteria(TopicCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Topic> specification = createSpecification(criteria);
        return topicMapper.toDto(topicRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TopicDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TopicDTO> findByCriteria(TopicCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Topic> specification = createSpecification(criteria);
        return topicRepository.findAll(specification, page).map(topicMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TopicCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Topic> specification = createSpecification(criteria);
        return topicRepository.count(specification);
    }

    /**
     * Function to convert {@link TopicCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Topic> createSpecification(TopicCriteria criteria) {
        Specification<Topic> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Topic_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Topic_.title));
            }
            if (criteria.getVotesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVotesId(), root -> root.join(Topic_.votes, JoinType.LEFT).get(TopicVote_.id))
                    );
            }
            if (criteria.getDepartmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepartmentId(),
                            root -> root.join(Topic_.department, JoinType.LEFT).get(Department_.id)
                        )
                    );
            }
            if (criteria.getVotersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getVotersId(),
                            root -> root.join(Topic_.voters, JoinType.LEFT).get(UserPerDepartment_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
