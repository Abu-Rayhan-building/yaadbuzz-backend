package edu.sharif.math.yaadbuzz.service;

import edu.sharif.math.yaadbuzz.domain.*; // for static metamodels
import edu.sharif.math.yaadbuzz.domain.Memorial;
import edu.sharif.math.yaadbuzz.repository.MemorialRepository;
import edu.sharif.math.yaadbuzz.service.dto.MemorialCriteria;
import edu.sharif.math.yaadbuzz.service.dto.MemorialDTO;
import edu.sharif.math.yaadbuzz.service.mapper.MemorialMapper;
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
 * Service for executing complex queries for {@link Memorial} entities in the database.
 * The main input is a {@link MemorialCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MemorialDTO} or a {@link Page} of {@link MemorialDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MemorialQueryService extends QueryService<Memorial> {

    private final Logger log = LoggerFactory.getLogger(MemorialQueryService.class);

    private final MemorialRepository memorialRepository;

    private final MemorialMapper memorialMapper;

    public MemorialQueryService(MemorialRepository memorialRepository, MemorialMapper memorialMapper) {
        this.memorialRepository = memorialRepository;
        this.memorialMapper = memorialMapper;
    }

    /**
     * Return a {@link List} of {@link MemorialDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MemorialDTO> findByCriteria(MemorialCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Memorial> specification = createSpecification(criteria);
        return memorialMapper.toDto(memorialRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MemorialDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MemorialDTO> findByCriteria(MemorialCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Memorial> specification = createSpecification(criteria);
        return memorialRepository.findAll(specification, page).map(memorialMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MemorialCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Memorial> specification = createSpecification(criteria);
        return memorialRepository.count(specification);
    }

    /**
     * Function to convert {@link MemorialCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Memorial> createSpecification(MemorialCriteria criteria) {
        Specification<Memorial> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Memorial_.id));
            }
            if (criteria.getAnonymousCommentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAnonymousCommentId(),
                            root -> root.join(Memorial_.anonymousComment, JoinType.LEFT).get(Comment_.id)
                        )
                    );
            }
            if (criteria.getNotAnonymousCommentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNotAnonymousCommentId(),
                            root -> root.join(Memorial_.notAnonymousComment, JoinType.LEFT).get(Comment_.id)
                        )
                    );
            }
            if (criteria.getWriterId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getWriterId(),
                            root -> root.join(Memorial_.writer, JoinType.LEFT).get(UserPerDepartment_.id)
                        )
                    );
            }
            if (criteria.getRecipientId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRecipientId(),
                            root -> root.join(Memorial_.recipient, JoinType.LEFT).get(UserPerDepartment_.id)
                        )
                    );
            }
            if (criteria.getDepartmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepartmentId(),
                            root -> root.join(Memorial_.department, JoinType.LEFT).get(Department_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
