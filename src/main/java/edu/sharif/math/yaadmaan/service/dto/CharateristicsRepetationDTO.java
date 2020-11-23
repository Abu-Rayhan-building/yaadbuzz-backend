package edu.sharif.math.yaadmaan.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link edu.sharif.math.yaadmaan.domain.CharateristicsRepetation} entity.
 */
public class CharateristicsRepetationDTO implements Serializable {
    
    private Long id;

    private Integer repetation;


    private Long userId;

    private Long charactristicId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRepetation() {
        return repetation;
    }

    public void setRepetation(Integer repetation) {
        this.repetation = repetation;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userPerDepartmentId) {
        this.userId = userPerDepartmentId;
    }

    public Long getCharactristicId() {
        return charactristicId;
    }

    public void setCharactristicId(Long charateristicsId) {
        this.charactristicId = charateristicsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CharateristicsRepetationDTO)) {
            return false;
        }

        return id != null && id.equals(((CharateristicsRepetationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CharateristicsRepetationDTO{" +
            "id=" + getId() +
            ", repetation=" + getRepetation() +
            ", userId=" + getUserId() +
            ", charactristicId=" + getCharactristicId() +
            "}";
    }
}
