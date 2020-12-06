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
 * A Topic.
 */
@Entity
@Table(name = "topic")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Topic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @OneToMany(mappedBy = "topic")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<TopicRating> ratings = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "topics", allowSetters = true)
    private Department department;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "topic_voters",
               joinColumns = @JoinColumn(name = "topic_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "voters_id", referencedColumnName = "id"))
    private Set<UserPerDepartment> voters = new HashSet<>();

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

    public Topic title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<TopicRating> getRatings() {
        return ratings;
    }

    public Topic ratings(Set<TopicRating> topicRatings) {
        this.ratings = topicRatings;
        return this;
    }

    public Topic addRatings(TopicRating topicRating) {
        this.ratings.add(topicRating);
        topicRating.setTopic(this);
        return this;
    }

    public Topic removeRatings(TopicRating topicRating) {
        this.ratings.remove(topicRating);
        topicRating.setTopic(null);
        return this;
    }

    public void setRatings(Set<TopicRating> topicRatings) {
        this.ratings = topicRatings;
    }

    public Department getDepartment() {
        return department;
    }

    public Topic department(Department department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<UserPerDepartment> getVoters() {
        return voters;
    }

    public Topic voters(Set<UserPerDepartment> userPerDepartments) {
        this.voters = userPerDepartments;
        return this;
    }

    public Topic addVoters(UserPerDepartment userPerDepartment) {
        this.voters.add(userPerDepartment);
        userPerDepartment.getTopicsVoteds().add(this);
        return this;
    }

    public Topic removeVoters(UserPerDepartment userPerDepartment) {
        this.voters.remove(userPerDepartment);
        userPerDepartment.getTopicsVoteds().remove(this);
        return this;
    }

    public void setVoters(Set<UserPerDepartment> userPerDepartments) {
        this.voters = userPerDepartments;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Topic)) {
            return false;
        }
        return id != null && id.equals(((Topic) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Topic{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
