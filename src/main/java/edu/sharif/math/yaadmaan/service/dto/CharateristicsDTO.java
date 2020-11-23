package edu.sharif.math.yaadmaan.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link edu.sharif.math.yaadmaan.domain.Charateristics} entity.
 */
public class CharateristicsDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String title;


    private Long departmentId;
    
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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
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
            ", departmentId=" + getDepartmentId() +
            "}";
    }
}
