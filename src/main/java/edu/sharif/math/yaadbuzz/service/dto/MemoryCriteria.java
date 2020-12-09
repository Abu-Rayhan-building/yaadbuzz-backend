package edu.sharif.math.yaadbuzz.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link edu.sharif.math.yaadbuzz.domain.Memory} entity. This class is used
 * in {@link edu.sharif.math.yaadbuzz.web.rest.MemoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /memories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MemoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private BooleanFilter isPrivate;

    private LongFilter commentsId;

    private LongFilter textId;

    private LongFilter writerId;

    private LongFilter tagedId;

    private LongFilter departmentId;

    public MemoryCriteria() {
    }

    public MemoryCriteria(MemoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.isPrivate = other.isPrivate == null ? null : other.isPrivate.copy();
        this.commentsId = other.commentsId == null ? null : other.commentsId.copy();
        this.textId = other.textId == null ? null : other.textId.copy();
        this.writerId = other.writerId == null ? null : other.writerId.copy();
        this.tagedId = other.tagedId == null ? null : other.tagedId.copy();
        this.departmentId = other.departmentId == null ? null : other.departmentId.copy();
    }

    @Override
    public MemoryCriteria copy() {
        return new MemoryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public BooleanFilter getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(BooleanFilter isPrivate) {
        this.isPrivate = isPrivate;
    }

    public LongFilter getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(LongFilter commentsId) {
        this.commentsId = commentsId;
    }

    public LongFilter getTextId() {
        return textId;
    }

    public void setTextId(LongFilter textId) {
        this.textId = textId;
    }

    public LongFilter getWriterId() {
        return writerId;
    }

    public void setWriterId(LongFilter writerId) {
        this.writerId = writerId;
    }

    public LongFilter getTagedId() {
        return tagedId;
    }

    public void setTagedId(LongFilter tagedId) {
        this.tagedId = tagedId;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MemoryCriteria that = (MemoryCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(isPrivate, that.isPrivate) &&
            Objects.equals(commentsId, that.commentsId) &&
            Objects.equals(textId, that.textId) &&
            Objects.equals(writerId, that.writerId) &&
            Objects.equals(tagedId, that.tagedId) &&
            Objects.equals(departmentId, that.departmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        isPrivate,
        commentsId,
        textId,
        writerId,
        tagedId,
        departmentId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemoryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (isPrivate != null ? "isPrivate=" + isPrivate + ", " : "") +
                (commentsId != null ? "commentsId=" + commentsId + ", " : "") +
                (textId != null ? "textId=" + textId + ", " : "") +
                (writerId != null ? "writerId=" + writerId + ", " : "") +
                (tagedId != null ? "tagedId=" + tagedId + ", " : "") +
                (departmentId != null ? "departmentId=" + departmentId + ", " : "") +
            "}";
    }

}
