package edu.sharif.math.yaadmaan.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link edu.sharif.math.yaadmaan.domain.Department} entity.
 */
public class DepartmentDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String password;


    private Long avatarId;

    private Long ownerId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long pictureId) {
        this.avatarId = pictureId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long userId) {
        this.ownerId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartmentDTO)) {
            return false;
        }

        return id != null && id.equals(((DepartmentDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartmentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", password='" + getPassword() + "'" +
            ", avatarId=" + getAvatarId() +
            ", ownerId=" + getOwnerId() +
            "}";
    }
}
