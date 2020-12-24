package edu.sharif.math.yaadbuzz.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.Memorial} entity.
 */
public class MemorialDTO implements Serializable {

    private Long id;

    private CommentDTO anonymousComment;

    private CommentDTO notAnonymousComment;

    private UserPerDepartmentDTO writer;

    private UserPerDepartmentDTO recipient;

    private DepartmentDTO department;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CommentDTO getAnonymousComment() {
        return anonymousComment;
    }

    public void setAnonymousComment(CommentDTO anonymousComment) {
        this.anonymousComment = anonymousComment;
    }

    public CommentDTO getNotAnonymousComment() {
        return notAnonymousComment;
    }

    public void setNotAnonymousComment(CommentDTO notAnonymousComment) {
        this.notAnonymousComment = notAnonymousComment;
    }

    public UserPerDepartmentDTO getWriter() {
        return writer;
    }

    public void setWriter(UserPerDepartmentDTO writer) {
        this.writer = writer;
    }

    public UserPerDepartmentDTO getRecipient() {
        return recipient;
    }

    public void setRecipient(UserPerDepartmentDTO recipient) {
        this.recipient = recipient;
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
        if (!(o instanceof MemorialDTO)) {
            return false;
        }

        MemorialDTO memorialDTO = (MemorialDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, memorialDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemorialDTO{" +
            "id=" + getId() +
            ", anonymousComment=" + getAnonymousComment() +
            ", notAnonymousComment=" + getNotAnonymousComment() +
            ", writer=" + getWriter() +
            ", recipient=" + getRecipient() +
            ", department=" + getDepartment() +
            "}";
    }
}
