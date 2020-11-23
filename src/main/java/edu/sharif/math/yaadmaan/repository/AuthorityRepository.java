package edu.sharif.math.yaadmaan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sharif.math.yaadmaan.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
