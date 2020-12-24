package edu.sharif.math.yaadbuzz.service.mapper;

import edu.sharif.math.yaadbuzz.domain.*;
import edu.sharif.math.yaadbuzz.service.dto.MemorialDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Memorial} and its DTO {@link MemorialDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommentMapper.class, UserPerDepartmentMapper.class, DepartmentMapper.class })
public interface MemorialMapper extends EntityMapper<MemorialDTO, Memorial> {
    @Mapping(target = "anonymousComment", source = "anonymousComment", qualifiedByName = "id")
    @Mapping(target = "notAnonymousComment", source = "notAnonymousComment", qualifiedByName = "id")
    @Mapping(target = "writer", source = "writer", qualifiedByName = "id")
    @Mapping(target = "recipient", source = "recipient", qualifiedByName = "id")
    @Mapping(target = "department", source = "department", qualifiedByName = "id")
    MemorialDTO toDto(Memorial memorial);
}
