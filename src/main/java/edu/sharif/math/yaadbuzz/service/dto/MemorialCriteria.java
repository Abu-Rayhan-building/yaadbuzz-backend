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
 * Criteria class for the {@link edu.sharif.math.yaadbuzz.domain.Memorial}
 * entity. This class is used in
 * {@link edu.sharif.math.yaadbuzz.web.rest.MemorialResource} to receive all the
 * possible filtering options from the Http GET request parameters. For example
 * the following could be a valid request:
 * {@code /memorials?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific
 * {@link Filter} class are used, we need to use fix type specific filters.
 */
public class MemorialCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter anonymousCommentId;

    private LongFilter notAnonymousCommentId;

    private LongFilter writerId;

    private LongFilter recipientId;

    private LongFilter departmentId;

    public MemorialCriteria() {}

    public MemorialCriteria(MemorialCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.anonymousCommentId = other.anonymousCommentId == null ? null : other.anonymousCommentId.copy();
        this.notAnonymousCommentId = other.notAnonymousCommentId == null ? null : other.notAnonymousCommentId.copy();
        this.writerId = other.writerId == null ? null : other.writerId.copy();
        this.recipientId = other.recipientId == null ? null : other.recipientId.copy();
        this.departmentId = other.departmentId == null ? null : other.departmentId.copy();
    }

    @Override
    public MemorialCriteria copy() {
        return new MemorialCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getAnonymousCommentId() {
        return anonymousCommentId;
    }

    public void setAnonymousCommentId(LongFilter anonymousCommentId) {
        this.anonymousCommentId = anonymousCommentId;
    }

    public LongFilter getNotAnonymousCommentId() {
        return notAnonymousCommentId;
    }

    public void setNotAnonymousCommentId(LongFilter notAnonymousCommentId) {
        this.notAnonymousCommentId = notAnonymousCommentId;
    }

    public LongFilter getWriterId() {
        return writerId;
    }

    public void setWriterId(LongFilter writerId) {
        this.writerId = writerId;
    }

    public LongFilter getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(LongFilter recipientId) {
        this.recipientId = recipientId;
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
        final MemorialCriteria that = (MemorialCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(anonymousCommentId, that.anonymousCommentId) &&
            Objects.equals(notAnonymousCommentId, that.notAnonymousCommentId) &&
            Objects.equals(writerId, that.writerId) &&
            Objects.equals(recipientId, that.recipientId) &&
            Objects.equals(departmentId, that.departmentId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, anonymousCommentId, notAnonymousCommentId, writerId, recipientId, departmentId);
    }

    // prettier-ignore
    @Override
    public String toString() {
	return "MemorialCriteria{" + (id != null ? "id=" + id + ", " : "")
		+ (anonymousCommentId != null
			? "anonymousCommentId=" + anonymousCommentId + ", "
			: "")
		+ (notAnonymousCommentId != null
			? "notAnonymousCommentId=" + notAnonymousCommentId
				+ ", "
			: "")
		+ (writerId != null ? "writerId=" + writerId + ", " : "")
		+ (recipientId != null ? "recipientId=" + recipientId + ", "
			: "")
		+ (departmentId != null ? "departmentId=" + departmentId + ", "
			: "")
		+ "}";
    }
}
