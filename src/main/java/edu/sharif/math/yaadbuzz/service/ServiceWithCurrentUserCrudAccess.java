package edu.sharif.math.yaadbuzz.service;


public interface ServiceWithCurrentUserCrudAccess {

    boolean currentuserHasUpdateAccess(Long id);

    boolean currentuserHasGetAccess(Long id);
    

    default boolean currentuserHasDeleteAccess(Long id) {
	return currentuserHasUpdateAccess(id);
    }


}
