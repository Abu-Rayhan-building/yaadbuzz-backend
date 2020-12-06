package edu.sharif.math.yaadmaan.service.mapper;


import edu.sharif.math.yaadmaan.domain.*;
import edu.sharif.math.yaadmaan.service.dto.TopicRatingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TopicRating} and its DTO {@link TopicRatingDTO}.
 */
@Mapper(componentModel = "spring", uses = {TopicMapper.class, UserPerDepartmentMapper.class})
public interface TopicRatingMapper extends EntityMapper<TopicRatingDTO, TopicRating> {

    @Mapping(source = "topic.id", target = "topicId")
    @Mapping(source = "user.id", target = "userId")
    TopicRatingDTO toDto(TopicRating topicRating);

    @Mapping(source = "topicId", target = "topic")
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
