package edu.sharif.math.yaadmaan.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link edu.sharif.math.yaadmaan.domain.UserPerDepartment} entity.
 */
public class UserPerDepartmentDTO implements Serializable {
    
    private Long id;

    
    private String nicName;

    private String bio;


    private Long avatarId;

    private Long realUserId;

    private Long departmentId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNicName() {
        return nicName;
    }

    public void setNicName(String nicName) {
        this.nicName = nicName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long pictureId) {
        this.avatarId = pictureId;
    }

    public Long getRealUserId() {
        return realUserId;
    }

    public void setRealUserId(Long userId) {
        this.realUserId = userId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserPerDepartmentDTO)) {
            return false;
        }

        return id != null && id.equals(((UserPerDepartmentDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPerDepartmentDTO{" +
            "id=" + getId() +
            ", nicName='" + getNicName() + "'" +
            ", bio='" + getBio() + "'" +
            ", avatarId=" + getAvatarId() +
            ", realUserId=" + getRealUserId() +
            ", departmentId=" + getDepartmentId() +
            "}";
    }
}
