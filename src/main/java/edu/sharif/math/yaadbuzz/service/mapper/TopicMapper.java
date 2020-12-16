package edu.sharif.math.yaadbuzz.service.mapper;


import org.mapstruct.*;

import edu.sharif.math.yaadbuzz.domain.*;
import edu.sharif.math.yaadbuzz.service.dto.TopicDTO;

/**
 * Mapper for the entity {@link Topic} and its DTO {@link TopicDTO}.
 */
@Mapper(componentModel = "spring", uses = {DepartmentMapper.class, UserPerDepartmentMapper.class})
public interface TopicMapper extends EntityMapper<TopicDTO, Topic> {

    @Mapping(source = "department.id", target = "departmentId")
    TopicDTO toDto(Topic topic);

    @Mapping(target = "votes", ignore = true)
    @Mapping(target = "removeVotes", ignore = true)
    @Mapping(source = "departmentId", target = "department")
    @Mapping(target = "removeVoters", ignore = true)
    Topic toEntity(TopicDTO topicDTO);

    default Topic fromId(Long id) {
        if (id == null) {
            return null;
        }
        Topic topic = new Topic();
        topic.setId(id);
        return topic;
    }
}
