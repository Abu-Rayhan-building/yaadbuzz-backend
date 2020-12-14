package edu.sharif.math.yaadbuzz.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.Memory} entity.
 */
public class MemoryDTO implements Serializable {
    
    private Long id;

    private String title;

    private Boolean isPrivate;

    private Long textId;

    private Long writerId;
    private Set<UserPerDepartmentDTO> tageds = new HashSet<>();

    private Long departmentId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Long getTextId() {
        return textId;
    }

    public void setTextId(Long commentId) {
        this.textId = commentId;
    }

    public Long getWriterId() {
        return writerId;
    }

    public void setWriterId(Long userPerDepartmentId) {
        this.writerId = userPerDepartmentId;
    }

    public Set<UserPerDepartmentDTO> getTageds() {
        return tageds;
    }

    public void setTageds(Set<UserPerDepartmentDTO> userPerDepartments) {
        this.tageds = userPerDepartments;
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
        if (!(o instanceof MemoryDTO)) {
            return false;
        }

        return id != null && id.equals(((MemoryDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemoryDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", isPrivate='" + isIsPrivate() + "'" +
            ", textId=" + getTextId() +
            ", writerId=" + getWriterId() +
            ", tageds='" + getTageds() + "'" +
            ", departmentId=" + getDepartmentId() +
            "}";
    }
}
