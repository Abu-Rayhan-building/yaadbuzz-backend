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
 * Criteria class for the {@link edu.sharif.math.yaadbuzz.domain.Comment} entity. This class is used
 * in {@link edu.sharif.math.yaadbuzz.web.rest.CommentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /comments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter text;

    private LongFilter picturesId;

    private LongFilter writerId;

    private LongFilter memoryId;

    public CommentCriteria() {}

    public CommentCriteria(CommentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.text = other.text == null ? null : other.text.copy();
        this.picturesId = other.picturesId == null ? null : other.picturesId.copy();
        this.writerId = other.writerId == null ? null : other.writerId.copy();
        this.memoryId = other.memoryId == null ? null : other.memoryId.copy();
    }

    @Override
    public CommentCriteria copy() {
        return new CommentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getText() {
        return text;
    }

    public void setText(StringFilter text) {
        this.text = text;
    }

    public LongFilter getPicturesId() {
        return picturesId;
    }

    public void setPicturesId(LongFilter picturesId) {
        this.picturesId = picturesId;
    }

    public LongFilter getWriterId() {
        return writerId;
    }

    public void setWriterId(LongFilter writerId) {
        this.writerId = writerId;
    }

    public LongFilter getMemoryId() {
        return memoryId;
    }

    public void setMemoryId(LongFilter memoryId) {
        this.memoryId = memoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CommentCriteria that = (CommentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(text, that.text) &&
            Objects.equals(picturesId, that.picturesId) &&
            Objects.equals(writerId, that.writerId) &&
            Objects.equals(memoryId, that.memoryId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, picturesId, writerId, memoryId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (text != null ? "text=" + text + ", " : "") +
                (picturesId != null ? "picturesId=" + picturesId + ", " : "") +
                (writerId != null ? "writerId=" + writerId + ", " : "") +
                (memoryId != null ? "memoryId=" + memoryId + ", " : "") +
            "}";
    }
}
