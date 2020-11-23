package edu.sharif.math.yaadmaan.service.mapper;


import org.mapstruct.*;

import edu.sharif.math.yaadmaan.domain.*;
import edu.sharif.math.yaadmaan.service.dto.CharateristicsDTO;

/**
 * Mapper for the entity {@link Charateristics} and its DTO {@link CharateristicsDTO}.
 */
@Mapper(componentModel = "spring", uses = {DepartmentMapper.class})
public interface CharateristicsMapper extends EntityMapper<CharateristicsDTO, Charateristics> {

    @Mapping(source = "department.id", target = "departmentId")
    CharateristicsDTO toDto(Charateristics charateristics);

    @Mapping(target = "charateristicsRepetations", ignore = true)
    @Mapping(target = "removeCharateristicsRepetation", ignore = true)
    @Mapping(source = "departmentId", target = "department")
    Charateristics toEntity(CharateristicsDTO charateristicsDTO);

    default Charateristics fromId(Long id) {
        if (id == null) {
            return null;
        }
        Charateristics charateristics = new Charateristics();
        charateristics.setId(id);
        return charateristics;
    }
}
