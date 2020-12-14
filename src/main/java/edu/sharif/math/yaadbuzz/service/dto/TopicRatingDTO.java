package edu.sharif.math.yaadbuzz.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.TopicRating} entity.
 */
public class TopicRatingDTO implements Serializable {
    
    private Long id;

    private Integer repetitions;

    private Long topicId;

    private Long userId;
    
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

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userPerDepartmentId) {
        this.userId = userPerDepartmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TopicRatingDTO)) {
            return false;
        }

        return id != null && id.equals(((TopicRatingDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TopicRatingDTO{" +
            "id=" + getId() +
            ", repetitions=" + getRepetitions() +
            ", topicId=" + getTopicId() +
            ", userId=" + getUserId() +
            "}";
    }
}
