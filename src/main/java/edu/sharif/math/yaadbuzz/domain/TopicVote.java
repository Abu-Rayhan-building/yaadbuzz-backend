package edu.sharif.math.yaadbuzz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TopicVote.
 */
@Entity
@Table(name = "topic_vote")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TopicVote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "repetitions")
    private Integer repetitions;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "votes", "department", "voters" }, allowSetters = true)
    private Topic topic;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "topicAssigneds", "avatar", "realUser", "department", "topicsVoteds", "tagedInMemoeries" },
        allowSetters = true
    )
    private UserPerDepartment user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TopicVote id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRepetitions() {
        return this.repetitions;
    }

    public TopicVote repetitions(Integer repetitions) {
        this.repetitions = repetitions;
        return this;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public Topic getTopic() {
        return this.topic;
    }

    public TopicVote topic(Topic topic) {
        this.setTopic(topic);
        return this;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public UserPerDepartment getUser() {
        return this.user;
    }

    public TopicVote user(UserPerDepartment userPerDepartment) {
        this.setUser(userPerDepartment);
        return this;
    }

    public void setUser(UserPerDepartment userPerDepartment) {
        this.user = userPerDepartment;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters
    // and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TopicVote)) {
            return false;
        }
        return id != null && id.equals(((TopicVote) o).id);
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
	return "TopicVote{" + "id=" + getId() + ", repetitions="
		+ getRepetitions() + "}";
    }
}
