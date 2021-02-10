package edu.sharif.math.yaadbuzz.service;

import edu.sharif.math.yaadbuzz.domain.*; // for static metamodels
import edu.sharif.math.yaadbuzz.domain.Department;
import edu.sharif.math.yaadbuzz.repository.DepartmentRepository;
import edu.sharif.math.yaadbuzz.service.dto.DepartmentCriteria;
import edu.sharif.math.yaadbuzz.service.dto.DepartmentDTO;
import edu.sharif.math.yaadbuzz.service.mapper.DepartmentMapper;
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
 * Service for executing complex queries for {@link Department} entities in the
 * database. The main input is a {@link DepartmentCriteria} which gets converted
 * to {@link Specification}, in a way that all the filters must apply. It
 * returns a {@link List} of {@link DepartmentDTO} or a {@link Page} of
 * {@link DepartmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepartmentQueryService extends QueryService<Department> {

    private final Logger log = LoggerFactory.getLogger(DepartmentQueryService.class);

    private final DepartmentRepository departmentRepository;

    private final DepartmentMapper departmentMapper;

    public DepartmentQueryService(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    /**
     * Return a {@link List} of {@link DepartmentDTO} which matches the criteria
     * from the database.
     *
     * @param criteria The object which holds all the filters, which the
     *                 entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DepartmentDTO> findByCriteria(DepartmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Department> specification = createSpecification(criteria);
        return departmentMapper.toDto(departmentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DepartmentDTO} which matches the criteria
     * from the database.
     *
     * @param criteria The object which holds all the filters, which the
     *                 entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DepartmentDTO> findByCriteria(DepartmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Department> specification = createSpecification(criteria);
        return departmentRepository.findAll(specification, page).map(departmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the
     *                 entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepartmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Department> specification = createSpecification(criteria);
        return departmentRepository.count(specification);
    }

    /**
     * Function to convert {@link DepartmentCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the
     *                 entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Department> createSpecification(DepartmentCriteria criteria) {
        Specification<Department> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Department_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Department_.name));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), Department_.password));
            }
            if (criteria.getUserPerDepartmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUserPerDepartmentId(),
                            root -> root.join(Department_.userPerDepartments, JoinType.LEFT).get(UserPerDepartment_.id)
                        )
                    );
            }
            if (criteria.getMemoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMemoryId(), root -> root.join(Department_.memories, JoinType.LEFT).get(Memory_.id))
                    );
            }
            if (criteria.getAvatarId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAvatarId(), root -> root.join(Department_.avatar, JoinType.LEFT).get(Picture_.id))
                    );
            }
            if (criteria.getOwnerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOwnerId(), root -> root.join(Department_.owner, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
