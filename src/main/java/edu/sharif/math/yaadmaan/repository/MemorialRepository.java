package edu.sharif.math.yaadmaan.repository;

import edu.sharif.math.yaadmaan.domain.Memorial;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Memorial entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemorialRepository extends JpaRepository<Memorial, Long>, JpaSpecificationExecutor<Memorial> {
}
