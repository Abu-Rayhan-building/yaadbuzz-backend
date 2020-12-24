package edu.sharif.math.yaadbuzz.service.mapper;

import edu.sharif.math.yaadbuzz.domain.*;
import edu.sharif.math.yaadbuzz.service.dto.TopicVoteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TopicVote} and its DTO {@link TopicVoteDTO}.
 */
@Mapper(componentModel = "spring", uses = { TopicMapper.class, UserPerDepartmentMapper.class })
public interface TopicVoteMapper extends EntityMapper<TopicVoteDTO, TopicVote> {
    @Mapping(target = "topic", source = "topic", qualifiedByName = "id")
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    TopicVoteDTO toDto(TopicVote topicVote);
}
