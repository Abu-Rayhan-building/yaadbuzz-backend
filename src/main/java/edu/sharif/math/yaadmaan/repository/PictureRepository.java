package edu.sharif.math.yaadmaan.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import edu.sharif.math.yaadmaan.domain.Picture;

/**
 * Spring Data  repository for the Picture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {
}
