package edu.sharif.math.yaadbuzz.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.Charateristics} entity.
 */
public class CharateristicsDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String title;

    private Integer repetation;

    private Long userPerDepartmentId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRepetation() {
        return repetation;
    }

    public void setRepetation(Integer repetation) {
        this.repetation = repetation;
    }

    public Long getUserPerDepartmentId() {
        return userPerDepartmentId;
    }

    public void setUserPerDepartmentId(Long userPerDepartmentId) {
        this.userPerDepartmentId = userPerDepartmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CharateristicsDTO)) {
            return false;
        }

        return id != null && id.equals(((CharateristicsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CharateristicsDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", repetation=" + getRepetation() +
            ", userPerDepartmentId=" + getUserPerDepartmentId() +
            "}";
    }
}
