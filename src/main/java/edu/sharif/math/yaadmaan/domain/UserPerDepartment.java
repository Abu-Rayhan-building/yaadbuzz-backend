package edu.sharif.math.yaadmaan.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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

    
    @Column(name = "nic_name", unique = true)
    private String nicName;

    @Column(name = "bio")
    private String bio;

    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<TopicRating> topicAssigneds = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<CharateristicsRepetation> charateristicsRepetations = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "userPerDepartments", allowSetters = true)
    private Picture avatar;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "userPerDepartments", allowSetters = true)
    private User realUser;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "userPerDepartments", allowSetters = true)
    private Department department;

    @ManyToMany(mappedBy = "voters")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Topic> topicsVoteds = new HashSet<>();

    @ManyToMany(mappedBy = "tageds")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Memory> tagedInMemoeries = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNicName() {
        return nicName;
    }

    public UserPerDepartment nicName(String nicName) {
        this.nicName = nicName;
        return this;
    }

    public void setNicName(String nicName) {
        this.nicName = nicName;
    }

    public String getBio() {
        return bio;
    }

    public UserPerDepartment bio(String bio) {
        this.bio = bio;
        return this;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Set<TopicRating> getTopicAssigneds() {
        return topicAssigneds;
    }

    public UserPerDepartment topicAssigneds(Set<TopicRating> topicRatings) {
        this.topicAssigneds = topicRatings;
        return this;
    }

    public UserPerDepartment addTopicAssigneds(TopicRating topicRating) {
        this.topicAssigneds.add(topicRating);
        topicRating.setUser(this);
        return this;
    }

    public UserPerDepartment removeTopicAssigneds(TopicRating topicRating) {
        this.topicAssigneds.remove(topicRating);
        topicRating.setUser(null);
        return this;
    }

    public void setTopicAssigneds(Set<TopicRating> topicRatings) {
        this.topicAssigneds = topicRatings;
    }

    public Set<CharateristicsRepetation> getCharateristicsRepetations() {
        return charateristicsRepetations;
    }

    public UserPerDepartment charateristicsRepetations(Set<CharateristicsRepetation> charateristicsRepetations) {
        this.charateristicsRepetations = charateristicsRepetations;
        return this;
    }

    public UserPerDepartment addCharateristicsRepetations(CharateristicsRepetation charateristicsRepetation) {
        this.charateristicsRepetations.add(charateristicsRepetation);
        charateristicsRepetation.setUser(this);
        return this;
    }

    public UserPerDepartment removeCharateristicsRepetations(CharateristicsRepetation charateristicsRepetation) {
        this.charateristicsRepetations.remove(charateristicsRepetation);
        charateristicsRepetation.setUser(null);
        return this;
    }

    public void setCharateristicsRepetations(Set<CharateristicsRepetation> charateristicsRepetations) {
        this.charateristicsRepetations = charateristicsRepetations;
    }

    public Picture getAvatar() {
        return avatar;
    }

    public UserPerDepartment avatar(Picture picture) {
        this.avatar = picture;
        return this;
    }

    public void setAvatar(Picture picture) {
        this.avatar = picture;
    }

    public User getRealUser() {
        return realUser;
    }

    public UserPerDepartment realUser(User user) {
        this.realUser = user;
        return this;
    }

    public void setRealUser(User user) {
        this.realUser = user;
    }

    public Department getDepartment() {
        return department;
    }

    public UserPerDepartment department(Department department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Topic> getTopicsVoteds() {
        return topicsVoteds;
    }

    public UserPerDepartment topicsVoteds(Set<Topic> topics) {
        this.topicsVoteds = topics;
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
        this.topicsVoteds = topics;
    }

    public Set<Memory> getTagedInMemoeries() {
        return tagedInMemoeries;
    }

    public UserPerDepartment tagedInMemoeries(Set<Memory> memories) {
        this.tagedInMemoeries = memories;
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
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPerDepartment{" +
            "id=" + getId() +
            ", nicName='" + getNicName() + "'" +
            ", bio='" + getBio() + "'" +
            "}";
    }
}
