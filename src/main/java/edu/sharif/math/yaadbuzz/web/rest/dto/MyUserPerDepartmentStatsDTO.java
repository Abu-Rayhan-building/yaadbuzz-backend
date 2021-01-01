package edu.sharif.math.yaadbuzz.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.UserPerDepartment}
 * entity.
 */
public class MyUserPerDepartmentStatsDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Set<Long> topicsNotVotedYet = new HashSet<>();

    private Set<Long> userPerDepartmentNotWritedMemoryFor = new HashSet<>();

    public Set<Long> getTopicsNotVotedYet() {
	return this.topicsNotVotedYet;
    }

    public Set<Long> getUserPerDepartmentNotWritedMemoryFor() {
	return this.userPerDepartmentNotWritedMemoryFor;
    }

    public void setTopicsNotVotedYet(final Set<Long> topicsNotVotedYet) {
	this.topicsNotVotedYet = topicsNotVotedYet;
    }

    public void setUserPerDepartmentNotWritedMemoryFor(
	    final Set<Long> userPerDepartmentNotWritedMemoryFor) {
	this.userPerDepartmentNotWritedMemoryFor = userPerDepartmentNotWritedMemoryFor;
    }
}
