package edu.sharif.math.yaadmaan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Charateristics.
 */
@Entity
@Table(name = "charateristics")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Charateristics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "repetation")
    private Integer repetation;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "charateristics", allowSetters = true)
    private UserPerDepartment userPerDepartment;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Charateristics title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRepetation() {
        return repetation;
    }

    public Charateristics repetation(Integer repetation) {
        this.repetation = repetation;
        return this;
    }

    public void setRepetation(Integer repetation) {
        this.repetation = repetation;
    }

    public UserPerDepartment getUserPerDepartment() {
        return userPerDepartment;
    }

    public Charateristics userPerDepartment(UserPerDepartment userPerDepartment) {
        this.userPerDepartment = userPerDepartment;
        return this;
    }

    public void setUserPerDepartment(UserPerDepartment userPerDepartment) {
        this.userPerDepartment = userPerDepartment;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Charateristics)) {
            return false;
        }
        return id != null && id.equals(((Charateristics) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Charateristics{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", repetation=" + getRepetation() +
            "}";
    }
}
