package edu.sharif.math.yaadmaan.service.mapper;


import org.mapstruct.*;

import edu.sharif.math.yaadmaan.domain.*;
import edu.sharif.math.yaadmaan.service.dto.TopicRatingDTO;

/**
 * Mapper for the entity {@link TopicRating} and its DTO {@link TopicRatingDTO}.
 */
@Mapper(componentModel = "spring", uses = {TopicMapper.class, UserPerDepartmentMapper.class})
public interface TopicRatingMapper extends EntityMapper<TopicRatingDTO, TopicRating> {

    @Mapping(source = "rating.id", target = "ratingId")
    @Mapping(source = "user.id", target = "userId")
    TopicRatingDTO toDto(TopicRating topicRating);

    @Mapping(source = "ratingId", target = "rating")
    @Mapping(source = "userId", target = "user")
    TopicRating toEntity(TopicRatingDTO topicRatingDTO);

    default TopicRating fromId(Long id) {
        if (id == null) {
            return null;
        }
        TopicRating topicRating = new TopicRating();
        topicRating.setId(id);
        return topicRating;
    }
}
