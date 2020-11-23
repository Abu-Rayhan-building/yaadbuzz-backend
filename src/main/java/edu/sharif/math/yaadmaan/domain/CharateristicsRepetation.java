package edu.sharif.math.yaadmaan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A CharateristicsRepetation.
 */
@Entity
@Table(name = "charateristics_repetation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CharateristicsRepetation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "repetation")
    private Integer repetation;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "charateristicsRepetations", allowSetters = true)
    private UserPerDepartment user;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "charateristicsRepetations", allowSetters = true)
    private Charateristics charactristic;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRepetation() {
        return repetation;
    }

    public CharateristicsRepetation repetation(Integer repetation) {
        this.repetation = repetation;
        return this;
    }

    public void setRepetation(Integer repetation) {
        this.repetation = repetation;
    }

    public UserPerDepartment getUser() {
        return user;
    }

    public CharateristicsRepetation user(UserPerDepartment userPerDepartment) {
        this.user = userPerDepartment;
        return this;
    }

    public void setUser(UserPerDepartment userPerDepartment) {
        this.user = userPerDepartment;
    }

    public Charateristics getCharactristic() {
        return charactristic;
    }

    public CharateristicsRepetation charactristic(Charateristics charateristics) {
        this.charactristic = charateristics;
        return this;
    }

    public void setCharactristic(Charateristics charateristics) {
        this.charactristic = charateristics;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CharateristicsRepetation)) {
            return false;
        }
        return id != null && id.equals(((CharateristicsRepetation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CharateristicsRepetation{" +
            "id=" + getId() +
            ", repetation=" + getRepetation() +
            "}";
    }
}
