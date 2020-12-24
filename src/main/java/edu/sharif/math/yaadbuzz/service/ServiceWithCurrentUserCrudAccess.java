package edu.sharif.math.yaadbuzz.service;

import edu.sharif.math.yaadbuzz.service.dto.DepartmentDTO;

public interface ServiceWithCurrentUserCrudAccess {

    boolean currentuserHasUpdateAccess(Long id);

    boolean currentuserHasGetAccess(Long id);
    

    default boolean currentuserHasDeleteAccess(Long id) {
	return currentuserHasUpdateAccess(id);
    }


}
