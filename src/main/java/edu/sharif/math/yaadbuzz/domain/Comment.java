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

    @OneToMany()
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "comment_picture", joinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "picture_id", referencedColumnName = "id"))
    // fuck
    @JsonIgnoreProperties(value = { "comment" }, allowSetters = true)
    private Set<Picture> pictures = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "topicAssigneds", "avatar", "realUser", "department", "topicsVoteds", "tagedInMemoeries" },
        allowSetters = true
    )
    private UserPerDepartment writer;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "comments", "text", "writer", "tageds", "department" }, allowSetters = true)
    private Memory memory;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Comment id(Long id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return this.text;
    }

    public Comment text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<Picture> getPictures() {
        return this.pictures;
    }

    public Comment pictures(Set<Picture> pictures) {
        this.setPictures(pictures);
        return this;
    }

    public Comment addPictures(Picture picture) {
        this.pictures.add(picture);
        return this;
    }

    public Comment removePictures(Picture picture) {
        this.pictures.remove(picture);
        return this;
    }

    public void setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
    }

    public UserPerDepartment getWriter() {
        return this.writer;
    }

    public Comment writer(UserPerDepartment userPerDepartment) {
        this.setWriter(userPerDepartment);
        return this;
    }

    public void setWriter(UserPerDepartment userPerDepartment) {
        this.writer = userPerDepartment;
    }

    public Memory getMemory() {
        return this.memory;
    }

    public Comment memory(Memory memory) {
        this.setMemory(memory);
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
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
