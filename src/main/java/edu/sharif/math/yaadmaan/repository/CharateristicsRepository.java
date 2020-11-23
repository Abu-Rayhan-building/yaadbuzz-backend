package edu.sharif.math.yaadmaan.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import edu.sharif.math.yaadmaan.domain.Charateristics;

/**
 * Spring Data  repository for the Charateristics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CharateristicsRepository extends JpaRepository<Charateristics, Long> {
}
