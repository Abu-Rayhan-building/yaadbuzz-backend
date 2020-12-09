package edu.sharif.math.yaadbuzz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Department.
 */
@Entity
@Table(name = "department")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "department")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<UserPerDepartment> userPerDepartments = new HashSet<>();

    @OneToMany(mappedBy = "department")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Memory> memories = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "departments", allowSetters = true)
    private Picture avatar;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "departments", allowSetters = true)
    private User owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Department name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public Department password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserPerDepartment> getUserPerDepartments() {
        return userPerDepartments;
    }

    public Department userPerDepartments(Set<UserPerDepartment> userPerDepartments) {
        this.userPerDepartments = userPerDepartments;
        return this;
    }

    public Department addUserPerDepartment(UserPerDepartment userPerDepartment) {
        this.userPerDepartments.add(userPerDepartment);
        userPerDepartment.setDepartment(this);
        return this;
    }

    public Department removeUserPerDepartment(UserPerDepartment userPerDepartment) {
        this.userPerDepartments.remove(userPerDepartment);
        userPerDepartment.setDepartment(null);
        return this;
    }

    public void setUserPerDepartments(Set<UserPerDepartment> userPerDepartments) {
        this.userPerDepartments = userPerDepartments;
    }

    public Set<Memory> getMemories() {
        return memories;
    }

    public Department memories(Set<Memory> memories) {
        this.memories = memories;
        return this;
    }

    public Department addMemory(Memory memory) {
        this.memories.add(memory);
        memory.setDepartment(this);
        return this;
    }

    public Department removeMemory(Memory memory) {
        this.memories.remove(memory);
        memory.setDepartment(null);
        return this;
    }

    public void setMemories(Set<Memory> memories) {
        this.memories = memories;
    }

    public Picture getAvatar() {
        return avatar;
    }

    public Department avatar(Picture picture) {
        this.avatar = picture;
        return this;
    }

    public void setAvatar(Picture picture) {
        this.avatar = picture;
    }

    public User getOwner() {
        return owner;
    }

    public Department owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Department)) {
            return false;
        }
        return id != null && id.equals(((Department) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Department{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
