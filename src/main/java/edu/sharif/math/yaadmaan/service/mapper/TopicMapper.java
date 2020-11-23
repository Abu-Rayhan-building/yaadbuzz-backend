package edu.sharif.math.yaadmaan.service.mapper;


import org.mapstruct.*;

import edu.sharif.math.yaadmaan.domain.*;
import edu.sharif.math.yaadmaan.service.dto.TopicDTO;

/**
 * Mapper for the entity {@link Topic} and its DTO {@link TopicDTO}.
 */
@Mapper(componentModel = "spring", uses = {DepartmentMapper.class, UserPerDepartmentMapper.class})
public interface TopicMapper extends EntityMapper<TopicDTO, Topic> {

    @Mapping(source = "department.id", target = "departmentId")
    TopicDTO toDto(Topic topic);

    @Mapping(target = "ratings", ignore = true)
    @Mapping(target = "removeRatings", ignore = true)
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
