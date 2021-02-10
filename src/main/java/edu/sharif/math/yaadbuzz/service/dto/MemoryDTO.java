package edu.sharif.math.yaadbuzz.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.Memory} entity.
 */
public class MemoryDTO implements Serializable {

    private Long id;

    private String title;

    private Boolean isPrivate;

    private CommentDTO baseComment;

    private UserPerDepartmentDTO writer;

    private Set<UserPerDepartmentDTO> tageds = new HashSet<>();

    private DepartmentDTO department;

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

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public CommentDTO getBaseComment() {
        return baseComment;
    }

    public void setBaseComment(CommentDTO baseComment) {
        this.baseComment = baseComment;
    }

    public UserPerDepartmentDTO getWriter() {
        return writer;
    }

    public void setWriter(UserPerDepartmentDTO writer) {
        this.writer = writer;
    }

    public Set<UserPerDepartmentDTO> getTageds() {
        return tageds;
    }

    public void setTageds(Set<UserPerDepartmentDTO> tageds) {
        this.tageds = tageds;
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
        if (!(o instanceof MemoryDTO)) {
            return false;
        }

        MemoryDTO memoryDTO = (MemoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, memoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
	return "MemoryDTO{" + "id=" + getId() + ", title='" + getTitle() + "'"
		+ ", isPrivate='" + getIsPrivate() + "'" + ", baseComment="
		+ getBaseComment() + ", writer=" + getWriter() + ", tageds="
		+ getTageds() + ", department=" + getDepartment() + "}";
    }
}
