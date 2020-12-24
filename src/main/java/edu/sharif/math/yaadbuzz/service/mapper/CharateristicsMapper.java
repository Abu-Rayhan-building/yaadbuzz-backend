package edu.sharif.math.yaadbuzz.service.mapper;

import edu.sharif.math.yaadbuzz.domain.*;
import edu.sharif.math.yaadbuzz.service.dto.CharateristicsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Charateristics} and its DTO {@link CharateristicsDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserPerDepartmentMapper.class })
public interface CharateristicsMapper extends EntityMapper<CharateristicsDTO, Charateristics> {
    @Mapping(target = "userPerDepartment", source = "userPerDepartment", qualifiedByName = "id")
    CharateristicsDTO toDto(Charateristics charateristics);
}
