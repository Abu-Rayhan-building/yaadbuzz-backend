package edu.sharif.math.yaadmaan.service.mapper;


import org.mapstruct.*;

import edu.sharif.math.yaadmaan.domain.*;
import edu.sharif.math.yaadmaan.service.dto.CharateristicsRepetationDTO;

/**
 * Mapper for the entity {@link CharateristicsRepetation} and its DTO {@link CharateristicsRepetationDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserPerDepartmentMapper.class, CharateristicsMapper.class})
public interface CharateristicsRepetationMapper extends EntityMapper<CharateristicsRepetationDTO, CharateristicsRepetation> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "charactristic.id", target = "charactristicId")
    CharateristicsRepetationDTO toDto(CharateristicsRepetation charateristicsRepetation);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "charactristicId", target = "charactristic")
    CharateristicsRepetation toEntity(CharateristicsRepetationDTO charateristicsRepetationDTO);

    default CharateristicsRepetation fromId(Long id) {
        if (id == null) {
            return null;
        }
        CharateristicsRepetation charateristicsRepetation = new CharateristicsRepetation();
        charateristicsRepetation.setId(id);
        return charateristicsRepetation;
    }
}
