package edu.sharif.math.yaadbuzz.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.Memorial} entity.
 */
public class MemorialDTO implements Serializable {
    
    private Long id;


    private Long anonymousCommentId;

    private Long notAnonymousCommentId;

    private Long writerId;

    private Long recipientId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAnonymousCommentId() {
        return anonymousCommentId;
    }

    public void setAnonymousCommentId(Long commentId) {
        this.anonymousCommentId = commentId;
    }

    public Long getNotAnonymousCommentId() {
        return notAnonymousCommentId;
    }

    public void setNotAnonymousCommentId(Long commentId) {
        this.notAnonymousCommentId = commentId;
    }

    public Long getWriterId() {
        return writerId;
    }

    public void setWriterId(Long userPerDepartmentId) {
        this.writerId = userPerDepartmentId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long userPerDepartmentId) {
        this.recipientId = userPerDepartmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemorialDTO)) {
            return false;
        }

        return id != null && id.equals(((MemorialDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemorialDTO{" +
            "id=" + getId() +
            ", anonymousCommentId=" + getAnonymousCommentId() +
            ", notAnonymousCommentId=" + getNotAnonymousCommentId() +
            ", writerId=" + getWriterId() +
            ", recipientId=" + getRecipientId() +
            "}";
    }
}
