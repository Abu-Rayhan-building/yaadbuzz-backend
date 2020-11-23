package edu.sharif.math.yaadmaan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "charactristic")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<CharateristicsRepetation> charateristicsRepetations = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "charateristics", allowSetters = true)
    private Department department;

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

    public Set<CharateristicsRepetation> getCharateristicsRepetations() {
        return charateristicsRepetations;
    }

    public Charateristics charateristicsRepetations(Set<CharateristicsRepetation> charateristicsRepetations) {
        this.charateristicsRepetations = charateristicsRepetations;
        return this;
    }

    public Charateristics addCharateristicsRepetation(CharateristicsRepetation charateristicsRepetation) {
        this.charateristicsRepetations.add(charateristicsRepetation);
        charateristicsRepetation.setCharactristic(this);
        return this;
    }

    public Charateristics removeCharateristicsRepetation(CharateristicsRepetation charateristicsRepetation) {
        this.charateristicsRepetations.remove(charateristicsRepetation);
        charateristicsRepetation.setCharactristic(null);
        return this;
    }

    public void setCharateristicsRepetations(Set<CharateristicsRepetation> charateristicsRepetations) {
        this.charateristicsRepetations = charateristicsRepetations;
    }

    public Department getDepartment() {
        return department;
    }

    public Charateristics department(Department department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
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
            "}";
    }
}
