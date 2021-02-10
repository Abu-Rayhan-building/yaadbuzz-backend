package edu.sharif.math.yaadbuzz.service.mapper;

import edu.sharif.math.yaadbuzz.domain.*;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserPerDepartment} and its DTO
 * {@link UserPerDepartmentDTO}.
 */
@Mapper(componentModel = "spring", uses = { PictureMapper.class, UserMapper.class, DepartmentMapper.class })
public interface UserPerDepartmentMapper extends EntityMapper<UserPerDepartmentDTO, UserPerDepartment> {
    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "id")
    @Mapping(target = "realUser", source = "realUser", qualifiedByName = "id")
    @Mapping(target = "department", source = "department", qualifiedByName = "id")
    UserPerDepartmentDTO toDto(UserPerDepartment userPerDepartment);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserPerDepartmentDTO toDtoId(UserPerDepartment userPerDepartment);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<UserPerDepartmentDTO> toDtoIdSet(Set<UserPerDepartment> userPerDepartment);
}
