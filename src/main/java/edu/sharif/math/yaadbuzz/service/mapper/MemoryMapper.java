package edu.sharif.math.yaadbuzz.service.mapper;

import edu.sharif.math.yaadbuzz.domain.*;
import edu.sharif.math.yaadbuzz.service.dto.MemoryDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Memory} and its DTO {@link MemoryDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommentMapper.class, UserPerDepartmentMapper.class, DepartmentMapper.class })
public interface MemoryMapper extends EntityMapper<MemoryDTO, Memory> {
    @Mapping(target = "baseComment", source = "baseComment", qualifiedByName = "id")
    @Mapping(target = "writer", source = "writer", qualifiedByName = "id")
    @Mapping(target = "tageds", source = "tageds", qualifiedByName = "idSet")
    @Mapping(target = "department", source = "department", qualifiedByName = "id")
    MemoryDTO toDto(Memory memory);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MemoryDTO toDtoId(Memory memory);

    @Mapping(target = "removeTaged", ignore = true)
    Memory toEntity(MemoryDTO memoryDTO);
}
