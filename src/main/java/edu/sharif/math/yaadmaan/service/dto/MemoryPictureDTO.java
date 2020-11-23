package edu.sharif.math.yaadmaan.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link edu.sharif.math.yaadmaan.domain.MemoryPicture} entity.
 */
public class MemoryPictureDTO implements Serializable {
    
    private Long id;

    
    @Lob
    private byte[] image;

    private String imageContentType;

    private Long memoryId;
    
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
        if (!(o instanceof MemoryPictureDTO)) {
            return false;
        }

        return id != null && id.equals(((MemoryPictureDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemoryPictureDTO{" +
            "id=" + getId() +
            ", image='" + getImage() + "'" +
            ", memoryId=" + getMemoryId() +
            "}";
    }
}
