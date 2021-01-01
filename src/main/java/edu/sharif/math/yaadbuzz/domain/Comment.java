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
    private Set<Picture> pictures = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "topicAssigneds", "avatar", "realUser",
	    "department", "topicsVoteds",
	    "tagedInMemoeries" }, allowSetters = true)
    private UserPerDepartment writer;

    @ManyToOne(optional = true)
    @JsonIgnoreProperties(value = { "topicAssigneds", "avatar", "realUser",
	    "department", "topicsVoteds",
	    "tagedInMemoeries" }, allowSetters = true)
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pictures", "writer" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

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

    public Set<Comment> getComments() {
	return this.comments;
    }

    public Comment comments(Set<Comment> comments) {
	this.setComments(comments);
	return this;
    }

    public Comment addComments(Comment comment) {
	this.comments.add(comment);
	return this;
    }

    public Comment removeComments(Comment comment) {
	this.comments.remove(comment);
	return this;
    }

    public void setComments(Set<Comment> comments) {
//	if (this.comments != null) {
//	    this.comments.forEach(i -> i.setMemory(null));
//	}
//	if (comments != null) {
//	    comments.forEach(i -> i.setMemory(this));
//	}
	this.comments = comments;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters
    // and setters here

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
	// see
	// https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
	return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
	return "Comment{" + "id=" + getId() + ", text='" + getText() + "'"
		+ "}";
    }
}
