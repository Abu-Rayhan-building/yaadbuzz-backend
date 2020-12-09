package edu.sharif.math.yaadbuzz.service.mapper;


import org.mapstruct.*;

import edu.sharif.math.yaadbuzz.domain.*;
import edu.sharif.math.yaadbuzz.service.dto.DepartmentDTO;

/**
 * Mapper for the entity {@link Department} and its DTO {@link DepartmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {PictureMapper.class, UserMapper.class})
public interface DepartmentMapper extends EntityMapper<DepartmentDTO, Department> {

    @Mapping(source = "avatar.id", target = "avatarId")
    @Mapping(source = "owner.id", target = "ownerId")
    DepartmentDTO toDto(Department department);

    @Mapping(target = "userPerDepartments", ignore = true)
    @Mapping(target = "removeUserPerDepartment", ignore = true)
    @Mapping(target = "memories", ignore = true)
    @Mapping(target = "removeMemory", ignore = true)
    @Mapping(source = "avatarId", target = "avatar")
    @Mapping(source = "ownerId", target = "owner")
    Department toEntity(DepartmentDTO departmentDTO);

    default Department fromId(Long id) {
        if (id == null) {
            return null;
        }
        Department department = new Department();
        department.setId(id);
        return department;
    }
}
