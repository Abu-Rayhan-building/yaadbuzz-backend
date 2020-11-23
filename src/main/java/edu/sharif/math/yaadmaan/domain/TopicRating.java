package edu.sharif.math.yaadmaan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A TopicRating.
 */
@Entity
@Table(name = "topic_rating")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TopicRating implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "repetitions")
    private Integer repetitions;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "ratings", allowSetters = true)
    private Topic rating;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "topicAssigneds", allowSetters = true)
    private UserPerDepartment user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public TopicRating repetitions(Integer repetitions) {
        this.repetitions = repetitions;
        return this;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public Topic getRating() {
        return rating;
    }

    public TopicRating rating(Topic topic) {
        this.rating = topic;
        return this;
    }

    public void setRating(Topic topic) {
        this.rating = topic;
    }

    public UserPerDepartment getUser() {
        return user;
    }

    public TopicRating user(UserPerDepartment userPerDepartment) {
        this.user = userPerDepartment;
        return this;
    }

    public void setUser(UserPerDepartment userPerDepartment) {
        this.user = userPerDepartment;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TopicRating)) {
            return false;
        }
        return id != null && id.equals(((TopicRating) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TopicRating{" +
            "id=" + getId() +
            ", repetitions=" + getRepetitions() +
            "}";
    }
}
