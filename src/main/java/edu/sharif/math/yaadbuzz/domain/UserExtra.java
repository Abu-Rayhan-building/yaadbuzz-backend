package edu.sharif.math.yaadbuzz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserExtra.
 */
@Entity
@Table(name = "user_extra")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserExtra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "phone")
    private String phone;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @JsonIgnoreProperties(value = { "topicAssigneds", "avatar", "realUser",
	    "department", "topicsVoteds",
	    "tagedInMemoeries" }, allowSetters = true)

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public UserExtra id(Long id) {
	this.id = id;
	return this;
    }

    public String getPhone() {
	return this.phone;
    }

    public UserExtra phone(String phone) {
	this.phone = phone;
	return this;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }

    public User getUser() {
	return this.user;
    }

    public UserExtra user(User user) {
	this.setUser(user);
	return this;
    }

    public void setUser(User user) {
	this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters
    // and setters here

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof UserExtra)) {
	    return false;
	}
	return id != null && id.equals(((UserExtra) o).id);
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
	return "UserExtra{" + "id=" + getId() + ", phone='" + getPhone() + "'"
		+ "}";
    }
}
