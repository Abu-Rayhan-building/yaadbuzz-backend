package edu.sharif.math.yaadmaan.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link edu.sharif.math.yaadmaan.domain.Comment} entity.
 */
public class CommentDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String text;

    private Long writerId;

    private Set<PictureDTO> pictures = new HashSet<>();

    private Long memoryId;

    @Override
    public boolean equals(final Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof CommentDTO)) {
	    return false;
	}

	return this.id != null && this.id.equals(((CommentDTO) o).id);
    }

    public Long getId() {
	return this.id;
    }

    public Long getMemoryId() {
	return this.memoryId;
    }

    public Set<PictureDTO> getPictures() {
	return this.pictures;
    }

    public String getText() {
	return this.text;
    }

    public Long getWriterId() {
	return this.writerId;
    }

    @Override
    public int hashCode() {
	return 31;
    }

    public void setId(final Long id) {
	this.id = id;
    }

    public void setMemoryId(final Long memoryId) {
	this.memoryId = memoryId;
    }

    public void setPictures(final Set<PictureDTO> pictures) {
	this.pictures = pictures;
    }

    public void setText(final String text) {
	this.text = text;
    }

    public void setWriterId(final Long userPerDepartmentId) {
	this.writerId = userPerDepartmentId;
    }

    // prettier-ignore
    @Override
    public String toString() {
	return "CommentDTO{" + "id=" + this.getId() + ", text='"
		+ this.getText() + "'" + ", writerId=" + this.getWriterId()
		+ ", memoryId=" + this.getMemoryId() + ", pictures="
		+ this.getPictures() + "}";
    }
}
