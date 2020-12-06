package edu.sharif.math.yaadmaan.service;

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

import edu.sharif.math.yaadmaan.domain.UserPerDepartment;
import edu.sharif.math.yaadmaan.domain.*; // for static metamodels
import edu.sharif.math.yaadmaan.repository.UserPerDepartmentRepository;
import edu.sharif.math.yaadmaan.service.dto.UserPerDepartmentCriteria;
import edu.sharif.math.yaadmaan.service.dto.UserPerDepartmentDTO;
import edu.sharif.math.yaadmaan.service.mapper.UserPerDepartmentMapper;

/**
 * Service for executing complex queries for {@link UserPerDepartment} entities in the database.
 * The main input is a {@link UserPerDepartmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserPerDepartmentDTO} or a {@link Page} of {@link UserPerDepartmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserPerDepartmentQueryService extends QueryService<UserPerDepartment> {

    private final Logger log = LoggerFactory.getLogger(UserPerDepartmentQueryService.class);

    private final UserPerDepartmentRepository userPerDepartmentRepository;

    private final UserPerDepartmentMapper userPerDepartmentMapper;

    public UserPerDepartmentQueryService(UserPerDepartmentRepository userPerDepartmentRepository, UserPerDepartmentMapper userPerDepartmentMapper) {
        this.userPerDepartmentRepository = userPerDepartmentRepository;
        this.userPerDepartmentMapper = userPerDepartmentMapper;
    }

    /**
     * Return a {@link List} of {@link UserPerDepartmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserPerDepartmentDTO> findByCriteria(UserPerDepartmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserPerDepartment> specification = createSpecification(criteria);
        return userPerDepartmentMapper.toDto(userPerDepartmentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserPerDepartmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserPerDepartmentDTO> findByCriteria(UserPerDepartmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserPerDepartment> specification = createSpecification(criteria);
        return userPerDepartmentRepository.findAll(specification, page)
            .map(userPerDepartmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserPerDepartmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserPerDepartment> specification = createSpecification(criteria);
        return userPerDepartmentRepository.count(specification);
    }

    /**
     * Function to convert {@link UserPerDepartmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserPerDepartment> createSpecification(UserPerDepartmentCriteria criteria) {
        Specification<UserPerDepartment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserPerDepartment_.id));
            }
            if (criteria.getNicName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNicName(), UserPerDepartment_.nicName));
            }
            if (criteria.getBio() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBio(), UserPerDepartment_.bio));
            }
            if (criteria.getTopicAssignedsId() != null) {
                specification = specification.and(buildSpecification(criteria.getTopicAssignedsId(),
                    root -> root.join(UserPerDepartment_.topicAssigneds, JoinType.LEFT).get(TopicRating_.id)));
            }
            if (criteria.getAvatarId() != null) {
                specification = specification.and(buildSpecification(criteria.getAvatarId(),
                    root -> root.join(UserPerDepartment_.avatar, JoinType.LEFT).get(Picture_.id)));
            }
            if (criteria.getRealUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getRealUserId(),
                    root -> root.join(UserPerDepartment_.realUser, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getDepartmentId() != null) {
                specification = specification.and(buildSpecification(criteria.getDepartmentId(),
                    root -> root.join(UserPerDepartment_.department, JoinType.LEFT).get(Department_.id)));
            }
            if (criteria.getTopicsVotedId() != null) {
                specification = specification.and(buildSpecification(criteria.getTopicsVotedId(),
                    root -> root.join(UserPerDepartment_.topicsVoteds, JoinType.LEFT).get(Topic_.id)));
            }
            if (criteria.getTagedInMemoeriesId() != null) {
                specification = specification.and(buildSpecification(criteria.getTagedInMemoeriesId(),
                    root -> root.join(UserPerDepartment_.tagedInMemoeries, JoinType.LEFT).get(Memory_.id)));
            }
        }
        return specification;
    }
}
