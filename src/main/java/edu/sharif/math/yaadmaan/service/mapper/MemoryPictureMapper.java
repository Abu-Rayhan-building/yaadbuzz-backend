package edu.sharif.math.yaadmaan.service.mapper;


import org.mapstruct.*;

import edu.sharif.math.yaadmaan.domain.*;
import edu.sharif.math.yaadmaan.service.dto.MemoryPictureDTO;

/**
 * Mapper for the entity {@link MemoryPicture} and its DTO {@link MemoryPictureDTO}.
 */
@Mapper(componentModel = "spring", uses = {MemoryMapper.class})
public interface MemoryPictureMapper extends EntityMapper<MemoryPictureDTO, MemoryPicture> {

    @Mapping(source = "memory.id", target = "memoryId")
    MemoryPictureDTO toDto(MemoryPicture memoryPicture);

    @Mapping(source = "memoryId", target = "memory")
    MemoryPicture toEntity(MemoryPictureDTO memoryPictureDTO);

    default MemoryPicture fromId(Long id) {
        if (id == null) {
            return null;
        }
        MemoryPicture memoryPicture = new MemoryPicture();
        memoryPicture.setId(id);
        return memoryPicture;
    }
}
