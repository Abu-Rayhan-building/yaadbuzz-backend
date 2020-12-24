package edu.sharif.math.yaadbuzz.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.TopicVote} entity.
 */
public class TopicVoteDTO implements Serializable {

    private Long id;

    private Integer repetitions;

    private TopicDTO topic;

    private UserPerDepartmentDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public TopicDTO getTopic() {
        return topic;
    }

    public void setTopic(TopicDTO topic) {
        this.topic = topic;
    }

    public UserPerDepartmentDTO getUser() {
        return user;
    }

    public void setUser(UserPerDepartmentDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TopicVoteDTO)) {
            return false;
        }

        TopicVoteDTO topicVoteDTO = (TopicVoteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, topicVoteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TopicVoteDTO{" +
            "id=" + getId() +
            ", repetitions=" + getRepetitions() +
            ", topic=" + getTopic() +
            ", user=" + getUser() +
            "}";
    }
}
