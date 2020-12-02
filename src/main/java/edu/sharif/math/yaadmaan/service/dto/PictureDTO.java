package edu.sharif.math.yaadmaan.service.dto;

import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link edu.sharif.math.yaadmaan.domain.Picture} entity.
 */
public class PictureDTO implements Serializable {
    
    private Long id;

    
    @Lob
    private byte[] image;

    private String imageContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PictureDTO)) {
            return false;
        }

        return id != null && id.equals(((PictureDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PictureDTO{" +
            "id=" + getId() +
            ", image='" + getImage() + "'" +
            "}";
    }
}
