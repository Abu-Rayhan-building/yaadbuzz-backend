package edu.sharif.math.yaadbuzz.repository;

import edu.sharif.math.yaadbuzz.domain.Topic;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the Topic entity.
 */
@Repository
public interface TopicRepository extends JpaRepository<Topic, Long>, JpaSpecificationExecutor<Topic> {
    @Query(
        value = "select distinct topic from Topic topic left join fetch topic.voters",
        countQuery = "select count(distinct topic) from Topic topic"
    )
    Page<Topic> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct topic from Topic topic left join fetch topic.voters")
    List<Topic> findAllWithEagerRelationships();

    @Query("select topic from Topic topic left join fetch topic.voters where topic.id =:id")
    Optional<Topic> findOneWithEagerRelationships(@Param("id") Long id);
}
