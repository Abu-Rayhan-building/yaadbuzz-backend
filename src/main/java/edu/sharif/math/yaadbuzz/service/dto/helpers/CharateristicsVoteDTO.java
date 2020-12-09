package edu.sharif.math.yaadbuzz.service.dto.helpers;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class CharateristicsVoteDTO implements Serializable {
    
    @NotNull
    private String title;


    private Long userPerDepartmentId;
    


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Long getUserPerDepartmentId() {
        return userPerDepartmentId;
    }

    public void setUserPerDepartmentId(Long userPerDepartmentId) {
        this.userPerDepartmentId = userPerDepartmentId;
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CharateristicsDTO{" +
            ", title='" + getTitle() + "'" +
            ", userPerDepartmentId=" + getUserPerDepartmentId() +
            "}";
    }
}