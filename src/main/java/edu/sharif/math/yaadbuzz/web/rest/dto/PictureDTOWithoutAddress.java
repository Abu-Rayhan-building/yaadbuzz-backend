package edu.sharif.math.yaadbuzz.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.Picture} entity.
 */
public class PictureDTOWithoutAddress implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Long id;

    @Override
    public boolean equals(final Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof PictureDTOWithoutAddress)) {
	    return false;
	}

	final PictureDTOWithoutAddress pictureDTO = (PictureDTOWithoutAddress) o;
	if (this.id == null) {
	    return false;
	}
	return Objects.equals(this.id, pictureDTO.id);
    }

    public Long getId() {
	return this.id;
    }

    @Override
    public int hashCode() {
	return Objects.hash(this.id);
    }

    public void setId(final Long id) {
	this.id = id;
    }

    // prettier-ignore
    @Override
    public String toString() {
	return "PictureDTO{" + "id=" + this.getId() + "'" + "}";
    }
}
