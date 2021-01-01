package edu.sharif.math.yaadbuzz.service;

import edu.sharif.math.yaadbuzz.domain.*; // for static metamodels
import edu.sharif.math.yaadbuzz.domain.Memory;
import edu.sharif.math.yaadbuzz.repository.MemoryRepository;
import edu.sharif.math.yaadbuzz.service.dto.MemoryCriteria;
import edu.sharif.math.yaadbuzz.service.dto.MemoryDTO;
import edu.sharif.math.yaadbuzz.service.mapper.MemoryMapper;
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
 * Service for executing complex queries for {@link Memory} entities in the
 * database. The main input is a {@link MemoryCriteria} which gets converted to
 * {@link Specification}, in a way that all the filters must apply. It returns a
 * {@link List} of {@link MemoryDTO} or a {@link Page} of {@link MemoryDTO}
 * which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MemoryQueryService extends QueryService<Memory> {

    private final Logger log = LoggerFactory
	    .getLogger(MemoryQueryService.class);

    private final MemoryRepository memoryRepository;

    private final MemoryMapper memoryMapper;

    public MemoryQueryService(MemoryRepository memoryRepository,
	    MemoryMapper memoryMapper) {
	this.memoryRepository = memoryRepository;
	this.memoryMapper = memoryMapper;
    }

    /**
     * Return a {@link List} of {@link MemoryDTO} which matches the criteria
     * from the database.
     * 
     * @param criteria The object which holds all the filters, which the
     *                 entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MemoryDTO> findByCriteria(MemoryCriteria criteria) {
	log.debug("find by criteria : {}", criteria);
	final Specification<Memory> specification = createSpecification(
		criteria);
	return memoryMapper.toDto(memoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MemoryDTO} which matches the criteria
     * from the database.
     * 
     * @param criteria The object which holds all the filters, which the
     *                 entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MemoryDTO> findByCriteria(MemoryCriteria criteria,
	    Pageable page) {
	log.debug("find by criteria : {}, page: {}", criteria, page);
	final Specification<Memory> specification = createSpecification(
		criteria);
	return memoryRepository.findAll(specification, page)
		.map(memoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * 
     * @param criteria The object which holds all the filters, which the
     *                 entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MemoryCriteria criteria) {
	log.debug("count by criteria : {}", criteria);
	final Specification<Memory> specification = createSpecification(
		criteria);
	return memoryRepository.count(specification);
    }

    /**
     * Function to convert {@link MemoryCriteria} to a {@link Specification}
     * 
     * @param criteria The object which holds all the filters, which the
     *                 entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Memory> createSpecification(
	    MemoryCriteria criteria) {
	Specification<Memory> specification = Specification.where(null);
	if (criteria != null) {
	    if (criteria.getId() != null) {
		specification = specification.and(
			buildRangeSpecification(criteria.getId(), Memory_.id));
	    }
	    if (criteria.getTitle() != null) {
		specification = specification.and(buildStringSpecification(
			criteria.getTitle(), Memory_.title));
	    }
	    if (criteria.getIsPrivate() != null) {
		specification = specification.and(buildSpecification(
			criteria.getIsPrivate(), Memory_.isPrivate));
	    }

	    if (criteria.getBaseCommentId() != null) {
		specification = specification.and(buildSpecification(
			criteria.getBaseCommentId(),
			root -> root.join(Memory_.baseComment, JoinType.LEFT)
				.get(Comment_.id)));
	    }
	    if (criteria.getWriterId() != null) {
		specification = specification
			.and(buildSpecification(criteria.getWriterId(),
				root -> root.join(Memory_.writer, JoinType.LEFT)
					.get(UserPerDepartment_.id)));
	    }
	    if (criteria.getTagedId() != null) {
		specification = specification
			.and(buildSpecification(criteria.getTagedId(),
				root -> root.join(Memory_.tageds, JoinType.LEFT)
					.get(UserPerDepartment_.id)));
	    }
	    if (criteria.getDepartmentId() != null) {
		specification = specification
			.and(buildSpecification(criteria.getDepartmentId(),
				root -> root
					.join(Memory_.department, JoinType.LEFT)
					.get(Department_.id)));
	    }
	}
	return specification;
    }
}
