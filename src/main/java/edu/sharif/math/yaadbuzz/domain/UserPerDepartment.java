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
 * A UserPerDepartment.
 */
@Entity
@Table(name = "user_per_department")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserPerDepartment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Column(name = "bio")
    private String bio;

    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "topic", "user" }, allowSetters = true)
    private Set<TopicVote> topicAssigneds = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "comment" }, allowSetters = true)
    private Picture avatar;

    @ManyToOne(optional = false)
    @NotNull
    private User realUser;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "userPerDepartments", "memories", "avatar", "owner" }, allowSetters = true)
    private Department department;

    @ManyToMany(mappedBy = "voters")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "votes", "department", "voters" }, allowSetters = true)
    private Set<Topic> topicsVoteds = new HashSet<>();

    @ManyToMany(mappedBy = "tageds")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "comments", "baseComment", "writer", "tageds", "department" }, allowSetters = true)
    private Set<Memory> tagedInMemoeries = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserPerDepartment id(Long id) {
        this.id = id;
        return this;
    }

    public String getNickname() {
        return this.nickname;
    }

    public UserPerDepartment nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBio() {
        return this.bio;
    }

    public UserPerDepartment bio(String bio) {
        this.bio = bio;
        return this;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Set<TopicVote> getTopicAssigneds() {
        return this.topicAssigneds;
    }

    public UserPerDepartment topicAssigneds(Set<TopicVote> topicVotes) {
        this.setTopicAssigneds(topicVotes);
        return this;
    }

    public UserPerDepartment addTopicAssigneds(TopicVote topicVote) {
        this.topicAssigneds.add(topicVote);
        topicVote.setUser(this);
        return this;
    }

    public UserPerDepartment removeTopicAssigneds(TopicVote topicVote) {
        this.topicAssigneds.remove(topicVote);
        topicVote.setUser(null);
        return this;
    }

    public void setTopicAssigneds(Set<TopicVote> topicVotes) {
        if (this.topicAssigneds != null) {
            this.topicAssigneds.forEach(i -> i.setUser(null));
        }
        if (topicVotes != null) {
            topicVotes.forEach(i -> i.setUser(this));
        }
        this.topicAssigneds = topicVotes;
    }

    public Picture getAvatar() {
        return this.avatar;
    }

    public UserPerDepartment avatar(Picture picture) {
        this.setAvatar(picture);
        return this;
    }

    public void setAvatar(Picture picture) {
        this.avatar = picture;
    }

    public User getRealUser() {
        return this.realUser;
    }

    public UserPerDepartment realUser(User user) {
        this.setRealUser(user);
        return this;
    }

    public void setRealUser(User user) {
        this.realUser = user;
    }

    public Department getDepartment() {
        return this.department;
    }

    public UserPerDepartment department(Department department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Topic> getTopicsVoteds() {
        return this.topicsVoteds;
    }

    public UserPerDepartment topicsVoteds(Set<Topic> topics) {
        this.setTopicsVoteds(topics);
        return this;
    }

    public UserPerDepartment addTopicsVoted(Topic topic) {
        this.topicsVoteds.add(topic);
        topic.getVoters().add(this);
        return this;
    }

    public UserPerDepartment removeTopicsVoted(Topic topic) {
        this.topicsVoteds.remove(topic);
        topic.getVoters().remove(this);
        return this;
    }

    public void setTopicsVoteds(Set<Topic> topics) {
        if (this.topicsVoteds != null) {
            this.topicsVoteds.forEach(i -> i.removeVoters(this));
        }
        if (topics != null) {
            topics.forEach(i -> i.addVoters(this));
        }
        this.topicsVoteds = topics;
    }

    public Set<Memory> getTagedInMemoeries() {
        return this.tagedInMemoeries;
    }

    public UserPerDepartment tagedInMemoeries(Set<Memory> memories) {
        this.setTagedInMemoeries(memories);
        return this;
    }

    public UserPerDepartment addTagedInMemoeries(Memory memory) {
        this.tagedInMemoeries.add(memory);
        memory.getTageds().add(this);
        return this;
    }

    public UserPerDepartment removeTagedInMemoeries(Memory memory) {
        this.tagedInMemoeries.remove(memory);
        memory.getTageds().remove(this);
        return this;
    }

    public void setTagedInMemoeries(Set<Memory> memories) {
        if (this.tagedInMemoeries != null) {
            this.tagedInMemoeries.forEach(i -> i.removeTaged(this));
        }
        if (memories != null) {
            memories.forEach(i -> i.addTaged(this));
        }
        this.tagedInMemoeries = memories;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserPerDepartment)) {
            return false;
        }
        return id != null && id.equals(((UserPerDepartment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPerDepartment{" +
            "id=" + getId() +
            ", nickname='" + getNickname() + "'" +
            ", bio='" + getBio() + "'" +
            "}";
    }
}
