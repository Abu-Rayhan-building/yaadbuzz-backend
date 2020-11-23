package edu.sharif.math.yaadmaan.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import edu.sharif.math.yaadmaan.domain.CharateristicsRepetation;

/**
 * Spring Data  repository for the CharateristicsRepetation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CharateristicsRepetationRepository extends JpaRepository<CharateristicsRepetation, Long> {
}
