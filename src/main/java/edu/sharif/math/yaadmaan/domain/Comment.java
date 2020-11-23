package edu.sharif.math.yaadmaan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Comment.
 */
@Entity
@Table(name = "comment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "comments", allowSetters = true)
    private UserPerDepartment writer;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "comments", allowSetters = true)
    private Memory memory;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public Comment text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserPerDepartment getWriter() {
        return writer;
    }

    public Comment writer(UserPerDepartment userPerDepartment) {
        this.writer = userPerDepartment;
        return this;
    }

    public void setWriter(UserPerDepartment userPerDepartment) {
        this.writer = userPerDepartment;
    }

    public Memory getMemory() {
        return memory;
    }

    public Comment memory(Memory memory) {
        this.memory = memory;
        return this;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comment)) {
            return false;
        }
        return id != null && id.equals(((Comment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Comment{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            "}";
    }
}
