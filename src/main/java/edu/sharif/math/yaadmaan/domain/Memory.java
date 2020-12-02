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

    @Column(name = "is_annonymos")
    private Boolean isAnnonymos;

    @OneToMany(mappedBy = "memory")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "memories", allowSetters = true)
    private Comment text;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "memories", allowSetters = true)
    private UserPerDepartment writer;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "memory_taged",
               joinColumns = @JoinColumn(name = "memory_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "taged_id", referencedColumnName = "id"))
    private Set<UserPerDepartment> tageds = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "memories", allowSetters = true)
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

    public Memory title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isIsPrivate() {
        return isPrivate;
    }

    public Memory isPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
        return this;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Boolean isIsAnnonymos() {
        return isAnnonymos;
    }

    public Memory isAnnonymos(Boolean isAnnonymos) {
        this.isAnnonymos = isAnnonymos;
        return this;
    }

    public void setIsAnnonymos(Boolean isAnnonymos) {
        this.isAnnonymos = isAnnonymos;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public Memory comments(Set<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public Memory addComments(Comment comment) {
        this.comments.add(comment);
        comment.setMemory(this);
        return this;
    }

    public Memory removeComments(Comment comment) {
        this.comments.remove(comment);
        comment.setMemory(null);
        return this;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Comment getText() {
        return text;
    }

    public Memory text(Comment comment) {
        this.text = comment;
        return this;
    }

    public void setText(Comment comment) {
        this.text = comment;
    }

    public UserPerDepartment getWriter() {
        return writer;
    }

    public Memory writer(UserPerDepartment userPerDepartment) {
        this.writer = userPerDepartment;
        return this;
    }

    public void setWriter(UserPerDepartment userPerDepartment) {
        this.writer = userPerDepartment;
    }

    public Set<UserPerDepartment> getTageds() {
        return tageds;
    }

    public Memory tageds(Set<UserPerDepartment> userPerDepartments) {
        this.tageds = userPerDepartments;
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
        return department;
    }

    public Memory department(Department department) {
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
        if (!(o instanceof Memory)) {
            return false;
        }
        return id != null && id.equals(((Memory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Memory{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", isPrivate='" + isIsPrivate() + "'" +
            ", isAnnonymos='" + isIsAnnonymos() + "'" +
            "}";
    }
}
