package edu.sharif.math.yaadbuzz.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.UserPerDepartment} entity.
 */
public class UserPerDepartmentDTO implements Serializable {

    private Long id;

    private String nickname;

    private String bio;

    private PictureDTO avatar;

    private UserDTO realUser;

    private DepartmentDTO department;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public PictureDTO getAvatar() {
        return avatar;
    }

    public void setAvatar(PictureDTO avatar) {
        this.avatar = avatar;
    }

    public UserDTO getRealUser() {
        return realUser;
    }

    public void setRealUser(UserDTO realUser) {
        this.realUser = realUser;
    }

    public DepartmentDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDTO department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserPerDepartmentDTO)) {
            return false;
        }

        UserPerDepartmentDTO userPerDepartmentDTO = (UserPerDepartmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userPerDepartmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPerDepartmentDTO{" +
            "id=" + getId() +
            ", nickname='" + getNickname() + "'" +
            ", bio='" + getBio() + "'" +
            ", avatar=" + getAvatar() +
            ", realUser=" + getRealUser() +
            ", department=" + getDepartment() +
            "}";
    }
}
