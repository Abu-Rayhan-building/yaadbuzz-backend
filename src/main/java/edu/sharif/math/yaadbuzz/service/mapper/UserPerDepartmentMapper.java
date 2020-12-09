package edu.sharif.math.yaadbuzz.service.mapper;


import edu.sharif.math.yaadbuzz.domain.*;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserPerDepartment} and its DTO {@link UserPerDepartmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {PictureMapper.class, UserMapper.class, DepartmentMapper.class})
public interface UserPerDepartmentMapper extends EntityMapper<UserPerDepartmentDTO, UserPerDepartment> {

    @Mapping(source = "avatar.id", target = "avatarId")
    @Mapping(source = "realUser.id", target = "realUserId")
    @Mapping(source = "department.id", target = "departmentId")
    UserPerDepartmentDTO toDto(UserPerDepartment userPerDepartment);

    @Mapping(target = "topicAssigneds", ignore = true)
    @Mapping(target = "removeTopicAssigneds", ignore = true)
    @Mapping(source = "avatarId", target = "avatar")
    @Mapping(source = "realUserId", target = "realUser")
    @Mapping(source = "departmentId", target = "department")
    @Mapping(target = "topicsVoteds", ignore = true)
    @Mapping(target = "removeTopicsVoted", ignore = true)
    @Mapping(target = "tagedInMemoeries", ignore = true)
    @Mapping(target = "removeTagedInMemoeries", ignore = true)
    UserPerDepartment toEntity(UserPerDepartmentDTO userPerDepartmentDTO);

    default UserPerDepartment fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserPerDepartment userPerDepartment = new UserPerDepartment();
        userPerDepartment.setId(id);
        return userPerDepartment;
    }
}
