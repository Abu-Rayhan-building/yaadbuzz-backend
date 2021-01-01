package edu.sharif.math.yaadbuzz.web.rest.dto;

import javax.validation.constraints.*;

import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.Topic} entity.
 */
public class TopicVoteUDTO implements Serializable {
    
    private Long topicId;

    private Set<Long> ballots = new HashSet<>();

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Set<Long> getBallots() {
        return ballots;
    }

    public void setBallots(Set<Long> ballots) {
        this.ballots = ballots;
    }
    
    
}
