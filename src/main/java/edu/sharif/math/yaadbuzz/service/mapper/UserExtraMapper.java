package edu.sharif.math.yaadbuzz.service.mapper;

import edu.sharif.math.yaadbuzz.domain.*;
import edu.sharif.math.yaadbuzz.service.dto.UserExtraDTO;
import org.mapstruct.*;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link UserExtra} and its DTO {@link UserExtraDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class,
	UserPerDepartmentMapper.class })
public interface UserExtraMapper extends EntityMapper<UserExtraDTO, UserExtra> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    UserExtraDTO toDto(UserExtra userExtra);
}
