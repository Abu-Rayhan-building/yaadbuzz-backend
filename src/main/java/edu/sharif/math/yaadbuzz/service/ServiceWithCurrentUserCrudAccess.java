package edu.sharif.math.yaadbuzz.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.sharif.math.yaadbuzz.service.dto.CommentDTO;
import edu.sharif.math.yaadbuzz.service.dto.DepartmentDTO;

public interface ServiceWithCurrentUserCrudAccess {

    boolean currentuserHasUpdateAccess(Long id);

    boolean currentuserHasGetAccess(Long id);

    default boolean currentuserHasDeleteAccess(Long id) {
	return currentuserHasUpdateAccess(id);
    }

}
