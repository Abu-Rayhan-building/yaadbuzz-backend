package edu.sharif.math.yaadbuzz.service.dto;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link edu.sharif.math.yaadbuzz.domain.TopicVote} entity. This class is used
 * in {@link edu.sharif.math.yaadbuzz.web.rest.TopicVoteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /topic-votes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TopicVoteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter repetitions;

    private LongFilter topicId;

    private LongFilter userId;

    public TopicVoteCriteria() {
    }

    public TopicVoteCriteria(TopicVoteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.repetitions = other.repetitions == null ? null : other.repetitions.copy();
        this.topicId = other.topicId == null ? null : other.topicId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public TopicVoteCriteria copy() {
        return new TopicVoteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(IntegerFilter repetitions) {
        this.repetitions = repetitions;
    }

    public LongFilter getTopicId() {
        return topicId;
    }

    public void setTopicId(LongFilter topicId) {
        this.topicId = topicId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TopicVoteCriteria that = (TopicVoteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(repetitions, that.repetitions) &&
            Objects.equals(topicId, that.topicId) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        repetitions,
        topicId,
        userId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TopicVoteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (repetitions != null ? "repetitions=" + repetitions + ", " : "") +
                (topicId != null ? "topicId=" + topicId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
