package edu.sharif.math.yaadbuzz.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.Topic} entity.
 */
public class TopicDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String title;

    private Long departmentId;
    
    private Set<UserPerDepartmentDTO> voters = new HashSet<>();
    
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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Set<UserPerDepartmentDTO> getVoters() {
        return voters;
    }

    public void setVoters(Set<UserPerDepartmentDTO> userPerDepartments) {
        this.voters = userPerDepartments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TopicDTO)) {
            return false;
        }

        return id != null && id.equals(((TopicDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TopicDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", departmentId=" + getDepartmentId() +
            ", voters='" + getVoters() + "'" +
            "}";
    }
}
