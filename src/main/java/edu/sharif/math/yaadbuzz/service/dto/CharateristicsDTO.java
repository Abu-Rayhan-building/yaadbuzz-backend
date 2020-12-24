package edu.sharif.math.yaadbuzz.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.Charateristics} entity.
 */
public class CharateristicsDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private Integer repetation;

    private UserPerDepartmentDTO userPerDepartment;

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

    public UserPerDepartmentDTO getUserPerDepartment() {
        return userPerDepartment;
    }

    public void setUserPerDepartment(UserPerDepartmentDTO userPerDepartment) {
        this.userPerDepartment = userPerDepartment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CharateristicsDTO)) {
            return false;
        }

        CharateristicsDTO charateristicsDTO = (CharateristicsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, charateristicsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CharateristicsDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", repetation=" + getRepetation() +
            ", userPerDepartment=" + getUserPerDepartment() +
            "}";
    }
}
