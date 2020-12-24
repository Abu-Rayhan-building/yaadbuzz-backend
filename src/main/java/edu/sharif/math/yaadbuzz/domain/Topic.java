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
    @JsonIgnoreProperties(value = { "topic", "user" }, allowSetters = true)
    private Set<TopicVote> votes = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "userPerDepartments", "memories", "avatar", "owner" }, allowSetters = true)
    private Department department;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_topic__voters",
        joinColumns = @JoinColumn(name = "topic_id"),
        inverseJoinColumns = @JoinColumn(name = "voters_id")
    )
    @JsonIgnoreProperties(
        value = { "topicAssigneds", "avatar", "realUser", "department", "topicsVoteds", "tagedInMemoeries" },
        allowSetters = true
    )
    private Set<UserPerDepartment> voters = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Topic id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Topic title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<TopicVote> getVotes() {
        return this.votes;
    }

    public Topic votes(Set<TopicVote> topicVotes) {
        this.setVotes(topicVotes);
        return this;
    }

    public Topic addVotes(TopicVote topicVote) {
        this.votes.add(topicVote);
        topicVote.setTopic(this);
        return this;
    }

    public Topic removeVotes(TopicVote topicVote) {
        this.votes.remove(topicVote);
        topicVote.setTopic(null);
        return this;
    }

    public void setVotes(Set<TopicVote> topicVotes) {
        if (this.votes != null) {
            this.votes.forEach(i -> i.setTopic(null));
        }
        if (topicVotes != null) {
            topicVotes.forEach(i -> i.setTopic(this));
        }
        this.votes = topicVotes;
    }

    public Department getDepartment() {
        return this.department;
    }

    public Topic department(Department department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<UserPerDepartment> getVoters() {
        return this.voters;
    }

    public Topic voters(Set<UserPerDepartment> userPerDepartments) {
        this.setVoters(userPerDepartments);
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
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
