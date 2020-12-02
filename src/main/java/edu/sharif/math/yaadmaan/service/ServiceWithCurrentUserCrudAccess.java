package edu.sharif.math.yaadmaan.service;

public interface ServiceWithCurrentUserCrudAccess {

    boolean currentuserHasUpdateAccess(Long id);

    boolean currentuserHasGetAccess(Long id);
    

    default boolean currentuserHasDeleteAccess(Long id) {
	return currentuserHasUpdateAccess(id);
    }



}
