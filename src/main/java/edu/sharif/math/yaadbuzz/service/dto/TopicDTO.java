package edu.sharif.math.yaadbuzz.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.Topic} entity.
 */
public class TopicDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private DepartmentDTO department;

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

    public DepartmentDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDTO department) {
        this.department = department;
    }

    public Set<UserPerDepartmentDTO> getVoters() {
        return voters;
    }

    public void setVoters(Set<UserPerDepartmentDTO> voters) {
        this.voters = voters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TopicDTO)) {
            return false;
        }

        TopicDTO topicDTO = (TopicDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, topicDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
	return "TopicDTO{" + "id=" + getId() + ", title='" + getTitle() + "'"
		+ ", department=" + getDepartment() + ", voters=" + getVoters()
		+ "}";
    }
}
