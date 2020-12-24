package edu.sharif.math.yaadbuzz.service;

import edu.sharif.math.yaadbuzz.domain.*; // for static metamodels
import edu.sharif.math.yaadbuzz.domain.Charateristics;
import edu.sharif.math.yaadbuzz.repository.CharateristicsRepository;
import edu.sharif.math.yaadbuzz.service.dto.CharateristicsCriteria;
import edu.sharif.math.yaadbuzz.service.dto.CharateristicsDTO;
import edu.sharif.math.yaadbuzz.service.mapper.CharateristicsMapper;
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
 * Service for executing complex queries for {@link Charateristics} entities in the database.
 * The main input is a {@link CharateristicsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CharateristicsDTO} or a {@link Page} of {@link CharateristicsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CharateristicsQueryService extends QueryService<Charateristics> {

    private final Logger log = LoggerFactory.getLogger(CharateristicsQueryService.class);

    private final CharateristicsRepository charateristicsRepository;

    private final CharateristicsMapper charateristicsMapper;

    public CharateristicsQueryService(CharateristicsRepository charateristicsRepository, CharateristicsMapper charateristicsMapper) {
        this.charateristicsRepository = charateristicsRepository;
        this.charateristicsMapper = charateristicsMapper;
    }

    /**
     * Return a {@link List} of {@link CharateristicsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CharateristicsDTO> findByCriteria(CharateristicsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Charateristics> specification = createSpecification(criteria);
        return charateristicsMapper.toDto(charateristicsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CharateristicsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CharateristicsDTO> findByCriteria(CharateristicsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Charateristics> specification = createSpecification(criteria);
        return charateristicsRepository.findAll(specification, page).map(charateristicsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CharateristicsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Charateristics> specification = createSpecification(criteria);
        return charateristicsRepository.count(specification);
    }

    /**
     * Function to convert {@link CharateristicsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Charateristics> createSpecification(CharateristicsCriteria criteria) {
        Specification<Charateristics> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Charateristics_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Charateristics_.title));
            }
            if (criteria.getRepetation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRepetation(), Charateristics_.repetation));
            }
            if (criteria.getUserPerDepartmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUserPerDepartmentId(),
                            root -> root.join(Charateristics_.userPerDepartment, JoinType.LEFT).get(UserPerDepartment_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
