package edu.sharif.math.yaadbuzz.service;

import edu.sharif.math.yaadbuzz.domain.*; // for static metamodels
import edu.sharif.math.yaadbuzz.domain.UserExtra;
import edu.sharif.math.yaadbuzz.repository.UserExtraRepository;
import edu.sharif.math.yaadbuzz.service.dto.UserExtraCriteria;
import edu.sharif.math.yaadbuzz.service.dto.UserExtraDTO;
import edu.sharif.math.yaadbuzz.service.mapper.UserExtraMapper;
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
 * Service for executing complex queries for {@link UserExtra} entities in the
 * database. The main input is a {@link UserExtraCriteria} which gets converted
 * to {@link Specification}, in a way that all the filters must apply. It
 * returns a {@link List} of {@link UserExtraDTO} or a {@link Page} of
 * {@link UserExtraDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserExtraQueryService extends QueryService<UserExtra> {

    private final Logger log = LoggerFactory
	    .getLogger(UserExtraQueryService.class);

    private final UserExtraRepository userExtraRepository;

    private final UserExtraMapper userExtraMapper;

    public UserExtraQueryService(UserExtraRepository userExtraRepository,
	    UserExtraMapper userExtraMapper) {
	this.userExtraRepository = userExtraRepository;
	this.userExtraMapper = userExtraMapper;
    }

    /**
     * Return a {@link List} of {@link UserExtraDTO} which matches the criteria
     * from the database.
     * 
     * @param criteria The object which holds all the filters, which the
     *                 entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserExtraDTO> findByCriteria(UserExtraCriteria criteria) {
	log.debug("find by criteria : {}", criteria);
	final Specification<UserExtra> specification = createSpecification(
		criteria);
	return userExtraMapper
		.toDto(userExtraRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserExtraDTO} which matches the criteria
     * from the database.
     * 
     * @param criteria The object which holds all the filters, which the
     *                 entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserExtraDTO> findByCriteria(UserExtraCriteria criteria,
	    Pageable page) {
	log.debug("find by criteria : {}, page: {}", criteria, page);
	final Specification<UserExtra> specification = createSpecification(
		criteria);
	return userExtraRepository.findAll(specification, page)
		.map(userExtraMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * 
     * @param criteria The object which holds all the filters, which the
     *                 entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserExtraCriteria criteria) {
	log.debug("count by criteria : {}", criteria);
	final Specification<UserExtra> specification = createSpecification(
		criteria);
	return userExtraRepository.count(specification);
    }

    /**
     * Function to convert {@link UserExtraCriteria} to a {@link Specification}
     * 
     * @param criteria The object which holds all the filters, which the
     *                 entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserExtra> createSpecification(
	    UserExtraCriteria criteria) {
	Specification<UserExtra> specification = Specification.where(null);
	if (criteria != null) {
	    if (criteria.getId() != null) {
		specification = specification.and(buildRangeSpecification(
			criteria.getId(), UserExtra_.id));
	    }
	    if (criteria.getPhone() != null) {
		specification = specification.and(buildStringSpecification(
			criteria.getPhone(), UserExtra_.phone));
	    }
	    if (criteria.getUserId() != null) {
		specification = specification
			.and(buildSpecification(criteria.getUserId(),
				root -> root
					.join(UserExtra_.user, JoinType.LEFT)
					.get(User_.id)));
	    }
	}
	return specification;
    }
}
