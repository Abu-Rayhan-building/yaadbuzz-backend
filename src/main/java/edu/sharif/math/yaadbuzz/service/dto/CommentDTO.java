package edu.sharif.math.yaadbuzz.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.*;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.Comment} entity.
 */
public class CommentDTO implements Serializable {

    private Long id;

    @NotNull
    private String text;

    private UserPerDepartmentDTO writer;

    private Set<PictureDTO> pictures = new HashSet<>();

    public Long getId() {
	return id;
    }

    public Set<PictureDTO> getPictures() {
	return this.pictures;
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

    public UserPerDepartmentDTO getWriter() {
	return writer;
    }

    public void setWriter(UserPerDepartmentDTO writer) {
	this.writer = writer;
    }

    public void setPictures(final Set<PictureDTO> pictures) {
	this.pictures = pictures;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof CommentDTO)) {
	    return false;
	}

	CommentDTO commentDTO = (CommentDTO) o;
	if (this.id == null) {
	    return false;
	}
	return Objects.equals(this.id, commentDTO.id);
    }

    @Override
    public int hashCode() {
	return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
	return "CommentDTO{" + "id=" + getId() + ", text='" + getText() + "'"
		+ ", writer=" + getWriter() + "}";
    }
}
