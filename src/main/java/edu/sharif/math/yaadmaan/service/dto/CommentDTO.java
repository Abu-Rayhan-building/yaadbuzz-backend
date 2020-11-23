package edu.sharif.math.yaadmaan.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link edu.sharif.math.yaadmaan.domain.Comment} entity.
 */
public class CommentDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String text;


    private Long writerId;

    private Long memoryId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getWriterId() {
        return writerId;
    }

    public void setWriterId(Long userPerDepartmentId) {
        this.writerId = userPerDepartmentId;
    }

    public Long getMemoryId() {
        return memoryId;
    }

    public void setMemoryId(Long memoryId) {
        this.memoryId = memoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentDTO)) {
            return false;
        }

        return id != null && id.equals(((CommentDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentDTO{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", writerId=" + getWriterId() +
            ", memoryId=" + getMemoryId() +
            "}";
    }
}
