package edu.sharif.math.yaadbuzz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Memorial.
 */
@Entity
@Table(name = "memorial")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Memorial implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @JsonIgnoreProperties(value = { "pictures", "writer", "memory" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Comment anonymousComment;

    @JsonIgnoreProperties(value = { "pictures", "writer", "memory" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Comment notAnonymousComment;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "topicAssigneds", "avatar", "realUser", "department", "topicsVoteds", "tagedInMemoeries" },
        allowSetters = true
    )
    private UserPerDepartment writer;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "topicAssigneds", "avatar", "realUser", "department", "topicsVoteds", "tagedInMemoeries" },
        allowSetters = true
    )
    private UserPerDepartment recipient;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "userPerDepartments", "memories", "avatar", "owner" }, allowSetters = true)
    private Department department;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Memorial id(Long id) {
        this.id = id;
        return this;
    }

    public Comment getAnonymousComment() {
        return this.anonymousComment;
    }

    public Memorial anonymousComment(Comment comment) {
        this.setAnonymousComment(comment);
        return this;
    }

    public void setAnonymousComment(Comment comment) {
        this.anonymousComment = comment;
    }

    public Comment getNotAnonymousComment() {
        return this.notAnonymousComment;
    }

    public Memorial notAnonymousComment(Comment comment) {
        this.setNotAnonymousComment(comment);
        return this;
    }

    public void setNotAnonymousComment(Comment comment) {
        this.notAnonymousComment = comment;
    }

    public UserPerDepartment getWriter() {
        return this.writer;
    }

    public Memorial writer(UserPerDepartment userPerDepartment) {
        this.setWriter(userPerDepartment);
        return this;
    }

    public void setWriter(UserPerDepartment userPerDepartment) {
        this.writer = userPerDepartment;
    }

    public UserPerDepartment getRecipient() {
        return this.recipient;
    }

    public Memorial recipient(UserPerDepartment userPerDepartment) {
        this.setRecipient(userPerDepartment);
        return this;
    }

    public void setRecipient(UserPerDepartment userPerDepartment) {
        this.recipient = userPerDepartment;
    }

    public Department getDepartment() {
        return this.department;
    }

    public Memorial department(Department department) {
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
        if (!(o instanceof Memorial)) {
            return false;
        }
        return id != null && id.equals(((Memorial) o).id);
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
	return "Memorial{" + "id=" + getId() + "}";
    }
}
