package edu.sharif.math.yaadbuzz.service.mapper;


import edu.sharif.math.yaadbuzz.domain.*;
import edu.sharif.math.yaadbuzz.service.dto.TopicVoteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TopicVote} and its DTO {@link TopicVoteDTO}.
 */
@Mapper(componentModel = "spring", uses = {TopicMapper.class, UserPerDepartmentMapper.class})
public interface TopicVoteMapper extends EntityMapper<TopicVoteDTO, TopicVote> {

    @Mapping(source = "topic.id", target = "topicId")
    @Mapping(source = "user.id", target = "userId")
    TopicVoteDTO toDto(TopicVote topicVote);

    @Mapping(source = "topicId", target = "topic")
    @Mapping(source = "userId", target = "user")
    TopicVote toEntity(TopicVoteDTO topicVoteDTO);

    default TopicVote fromId(Long id) {
        if (id == null) {
            return null;
        }
        TopicVote topicVote = new TopicVote();
        topicVote.setId(id);
        return topicVote;
    }
}
