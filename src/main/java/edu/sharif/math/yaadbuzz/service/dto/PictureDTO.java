package edu.sharif.math.yaadbuzz.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.Picture} entity.
 */
public class PictureDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Long id;

    private String address;

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
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
