package edu.sharif.math.yaadbuzz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

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

    @OneToOne
    @JoinColumn(unique = true)
    private Comment anonymousComment;

    @OneToOne
    @JoinColumn(unique = true)
    private Comment notAnonymousComment;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "memorials", allowSetters = true)
    private UserPerDepartment writer;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "memorials", allowSetters = true)
    private UserPerDepartment recipient;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Comment getAnonymousComment() {
        return anonymousComment;
    }

    public Memorial anonymousComment(Comment comment) {
        this.anonymousComment = comment;
        return this;
    }

    public void setAnonymousComment(Comment comment) {
        this.anonymousComment = comment;
    }

    public Comment getNotAnonymousComment() {
        return notAnonymousComment;
    }

    public Memorial notAnonymousComment(Comment comment) {
        this.notAnonymousComment = comment;
        return this;
    }

    public void setNotAnonymousComment(Comment comment) {
        this.notAnonymousComment = comment;
    }

    public UserPerDepartment getWriter() {
        return writer;
    }

    public Memorial writer(UserPerDepartment userPerDepartment) {
        this.writer = userPerDepartment;
        return this;
    }

    public void setWriter(UserPerDepartment userPerDepartment) {
        this.writer = userPerDepartment;
    }

    public UserPerDepartment getRecipient() {
        return recipient;
    }

    public Memorial recipient(UserPerDepartment userPerDepartment) {
        this.recipient = userPerDepartment;
        return this;
    }

    public void setRecipient(UserPerDepartment userPerDepartment) {
        this.recipient = userPerDepartment;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Memorial{" +
            "id=" + getId() +
            "}";
    }
}
