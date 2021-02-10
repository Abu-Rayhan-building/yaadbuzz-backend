package edu.sharif.math.yaadbuzz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
    @JsonIgnoreProperties(
        value = { "topicAssigneds", "avatar", "realUser", "department", "topicsVoteds", "tagedInMemoeries" },
        allowSetters = true
    )
    private UserPerDepartment userPerDepartment;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Charateristics id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Charateristics title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRepetation() {
        return this.repetation;
    }

    public Charateristics repetation(Integer repetation) {
        this.repetation = repetation;
        return this;
    }

    public void setRepetation(Integer repetation) {
        this.repetation = repetation;
    }

    public UserPerDepartment getUserPerDepartment() {
        return this.userPerDepartment;
    }

    public Charateristics userPerDepartment(UserPerDepartment userPerDepartment) {
        this.setUserPerDepartment(userPerDepartment);
        return this;
    }

    public void setUserPerDepartment(UserPerDepartment userPerDepartment) {
        this.userPerDepartment = userPerDepartment;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters
    // and setters here

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
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
	return "Charateristics{" + "id=" + getId() + ", title='" + getTitle()
		+ "'" + ", repetation=" + getRepetation() + "}";
    }
}
