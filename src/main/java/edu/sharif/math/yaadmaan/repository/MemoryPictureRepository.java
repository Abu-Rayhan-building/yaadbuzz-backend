package edu.sharif.math.yaadmaan.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import edu.sharif.math.yaadmaan.domain.MemoryPicture;

/**
 * Spring Data  repository for the MemoryPicture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemoryPictureRepository extends JpaRepository<MemoryPicture, Long> {
}
