package edu.sharif.math.yaadbuzz.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.Picture} entity.
 */
public class PictureDTO implements Serializable {

    private Long id;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof PictureDTO)) {
	    return false;
	}

	PictureDTO pictureDTO = (PictureDTO) o;
	if (this.id == null) {
	    return false;
	}
	return Objects.equals(this.id, pictureDTO.id);
    }

    @Override
    public int hashCode() {
	return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
	return "PictureDTO{" + "id=" + getId() + "'" + "}";
    }
}
