package edu.sharif.math.yaadbuzz.service.mapper;


import edu.sharif.math.yaadbuzz.domain.*;
import edu.sharif.math.yaadbuzz.service.dto.CharateristicsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Charateristics} and its DTO {@link CharateristicsDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserPerDepartmentMapper.class})
public interface CharateristicsMapper extends EntityMapper<CharateristicsDTO, Charateristics> {

    @Mapping(source = "userPerDepartment.id", target = "userPerDepartmentId")
    CharateristicsDTO toDto(Charateristics charateristics);

    @Mapping(source = "userPerDepartmentId", target = "userPerDepartment")
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
