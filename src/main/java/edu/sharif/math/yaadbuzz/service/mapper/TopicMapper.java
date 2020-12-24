package edu.sharif.math.yaadbuzz.service.mapper;

import edu.sharif.math.yaadbuzz.domain.*;
import edu.sharif.math.yaadbuzz.service.dto.TopicDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Topic} and its DTO {@link TopicDTO}.
 */
@Mapper(componentModel = "spring", uses = { DepartmentMapper.class, UserPerDepartmentMapper.class })
public interface TopicMapper extends EntityMapper<TopicDTO, Topic> {
    @Mapping(target = "department", source = "department", qualifiedByName = "id")
    @Mapping(target = "voters", source = "voters", qualifiedByName = "idSet")
    TopicDTO toDto(Topic topic);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TopicDTO toDtoId(Topic topic);

    @Mapping(target = "removeVoters", ignore = true)
    Topic toEntity(TopicDTO topicDTO);
}
