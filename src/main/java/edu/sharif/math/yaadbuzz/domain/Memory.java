package edu.sharif.math.yaadbuzz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Memory.
 */
@Entity
@Table(name = "memory")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Memory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "is_private")
    private Boolean isPrivate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "pictures", "writer",
	    "memory" }, allowSetters = true)
    private Comment baseComment;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "topicAssigneds", "avatar", "realUser",
	    "department", "topicsVoteds",
	    "tagedInMemoeries" }, allowSetters = true)
    private UserPerDepartment writer;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "rel_memory__taged", joinColumns = @JoinColumn(name = "memory_id"), inverseJoinColumns = @JoinColumn(name = "taged_id"))
    @JsonIgnoreProperties(value = { "topicAssigneds", "avatar", "realUser",
	    "department", "topicsVoteds",
	    "tagedInMemoeries" }, allowSetters = true)
    private Set<UserPerDepartment> tageds = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "userPerDepartments", "memories", "avatar",
	    "owner" }, allowSetters = true)
    private Department department;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Memory id(Long id) {
	this.id = id;
	return this;
    }

    public String getTitle() {
	return this.title;
    }

    public Memory title(String title) {
	this.title = title;
	return this;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Boolean getIsPrivate() {
	return this.isPrivate;
    }

    public Memory isPrivate(Boolean isPrivate) {
	this.isPrivate = isPrivate;
	return this;
    }

    public void setIsPrivate(Boolean isPrivate) {
	this.isPrivate = isPrivate;
    }

    public Comment getBaseComment() {
	return this.baseComment;
    }

    public Memory baseComment(Comment comment) {
	this.setBaseComment(comment);
	return this;
    }

    public void setBaseComment(Comment comment) {
	this.baseComment = comment;
    }

    public UserPerDepartment getWriter() {
	return this.writer;
    }

    public Memory writer(UserPerDepartment userPerDepartment) {
	this.setWriter(userPerDepartment);
	return this;
    }

    public void setWriter(UserPerDepartment userPerDepartment) {
	this.writer = userPerDepartment;
    }

    public Set<UserPerDepartment> getTageds() {
	return this.tageds;
    }

    public Memory tageds(Set<UserPerDepartment> userPerDepartments) {
	this.setTageds(userPerDepartments);
	return this;
    }

    public Memory addTaged(UserPerDepartment userPerDepartment) {
	this.tageds.add(userPerDepartment);
	userPerDepartment.getTagedInMemoeries().add(this);
	return this;
    }

    public Memory removeTaged(UserPerDepartment userPerDepartment) {
	this.tageds.remove(userPerDepartment);
	userPerDepartment.getTagedInMemoeries().remove(this);
	return this;
    }

    public void setTageds(Set<UserPerDepartment> userPerDepartments) {
	this.tageds = userPerDepartments;
    }

    public Department getDepartment() {
	return this.department;
    }

    public Memory department(Department department) {
	this.setDepartment(department);
	return this;
    }

    public void setDepartment(Department department) {
	this.department = department;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters
    // and setters here

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof Memory)) {
	    return false;
	}
	return id != null && id.equals(((Memory) o).id);
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
	return "Memory{" + "id=" + getId() + ", title='" + getTitle() + "'"
		+ ", isPrivate='" + getIsPrivate() + "'" + "}";
    }
}
