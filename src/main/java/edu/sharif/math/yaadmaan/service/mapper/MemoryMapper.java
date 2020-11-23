package edu.sharif.math.yaadmaan.service.mapper;


import org.mapstruct.*;

import edu.sharif.math.yaadmaan.domain.*;
import edu.sharif.math.yaadmaan.service.dto.MemoryDTO;

/**
 * Mapper for the entity {@link Memory} and its DTO {@link MemoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {CommentMapper.class, UserPerDepartmentMapper.class, DepartmentMapper.class})
public interface MemoryMapper extends EntityMapper<MemoryDTO, Memory> {

    @Mapping(source = "text.id", target = "textId")
    @Mapping(source = "writer.id", target = "writerId")
    @Mapping(source = "department.id", target = "departmentId")
    MemoryDTO toDto(Memory memory);

    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "removeComments", ignore = true)
    @Mapping(target = "pictures", ignore = true)
    @Mapping(target = "removePictures", ignore = true)
    @Mapping(source = "textId", target = "text")
    @Mapping(source = "writerId", target = "writer")
    @Mapping(target = "removeTaged", ignore = true)
    @Mapping(source = "departmentId", target = "department")
    Memory toEntity(MemoryDTO memoryDTO);

    default Memory fromId(Long id) {
        if (id == null) {
            return null;
        }
        Memory memory = new Memory();
        memory.setId(id);
        return memory;
    }
}
