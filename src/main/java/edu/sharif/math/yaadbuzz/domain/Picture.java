package edu.sharif.math.yaadbuzz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Picture.
 */
@Entity
@Table(name = "picture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "address", nullable = false)
    private String address;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Picture id(Long id) {
	this.id = id;
	return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters
    // and setters here

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof Picture)) {
	    return false;
	}
	return id != null && id.equals(((Picture) o).id);
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
	return "Picture{" + "id=" + getId() + ", address='" + getAddress() + "'"
		+ "}";
    }
}
