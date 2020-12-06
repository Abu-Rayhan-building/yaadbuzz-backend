package edu.sharif.math.yaadmaan.service.dto;

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
 * Criteria class for the {@link edu.sharif.math.yaadmaan.domain.UserPerDepartment} entity. This class is used
 * in {@link edu.sharif.math.yaadmaan.web.rest.UserPerDepartmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-per-departments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserPerDepartmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nicName;

    private StringFilter bio;

    private LongFilter topicAssignedsId;

    private LongFilter avatarId;

    private LongFilter realUserId;

    private LongFilter departmentId;

    private LongFilter topicsVotedId;

    private LongFilter tagedInMemoeriesId;

    public UserPerDepartmentCriteria() {
    }

    public UserPerDepartmentCriteria(UserPerDepartmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nicName = other.nicName == null ? null : other.nicName.copy();
        this.bio = other.bio == null ? null : other.bio.copy();
        this.topicAssignedsId = other.topicAssignedsId == null ? null : other.topicAssignedsId.copy();
        this.avatarId = other.avatarId == null ? null : other.avatarId.copy();
        this.realUserId = other.realUserId == null ? null : other.realUserId.copy();
        this.departmentId = other.departmentId == null ? null : other.departmentId.copy();
        this.topicsVotedId = other.topicsVotedId == null ? null : other.topicsVotedId.copy();
        this.tagedInMemoeriesId = other.tagedInMemoeriesId == null ? null : other.tagedInMemoeriesId.copy();
    }

    @Override
    public UserPerDepartmentCriteria copy() {
        return new UserPerDepartmentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNicName() {
        return nicName;
    }

    public void setNicName(StringFilter nicName) {
        this.nicName = nicName;
    }

    public StringFilter getBio() {
        return bio;
    }

    public void setBio(StringFilter bio) {
        this.bio = bio;
    }

    public LongFilter getTopicAssignedsId() {
        return topicAssignedsId;
    }

    public void setTopicAssignedsId(LongFilter topicAssignedsId) {
        this.topicAssignedsId = topicAssignedsId;
    }

    public LongFilter getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(LongFilter avatarId) {
        this.avatarId = avatarId;
    }

    public LongFilter getRealUserId() {
        return realUserId;
    }

    public void setRealUserId(LongFilter realUserId) {
        this.realUserId = realUserId;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
    }

    public LongFilter getTopicsVotedId() {
        return topicsVotedId;
    }

    public void setTopicsVotedId(LongFilter topicsVotedId) {
        this.topicsVotedId = topicsVotedId;
    }

    public LongFilter getTagedInMemoeriesId() {
        return tagedInMemoeriesId;
    }

    public void setTagedInMemoeriesId(LongFilter tagedInMemoeriesId) {
        this.tagedInMemoeriesId = tagedInMemoeriesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserPerDepartmentCriteria that = (UserPerDepartmentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nicName, that.nicName) &&
            Objects.equals(bio, that.bio) &&
            Objects.equals(topicAssignedsId, that.topicAssignedsId) &&
            Objects.equals(avatarId, that.avatarId) &&
            Objects.equals(realUserId, that.realUserId) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(topicsVotedId, that.topicsVotedId) &&
            Objects.equals(tagedInMemoeriesId, that.tagedInMemoeriesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nicName,
        bio,
        topicAssignedsId,
        avatarId,
        realUserId,
        departmentId,
        topicsVotedId,
        tagedInMemoeriesId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPerDepartmentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nicName != null ? "nicName=" + nicName + ", " : "") +
                (bio != null ? "bio=" + bio + ", " : "") +
                (topicAssignedsId != null ? "topicAssignedsId=" + topicAssignedsId + ", " : "") +
                (avatarId != null ? "avatarId=" + avatarId + ", " : "") +
                (realUserId != null ? "realUserId=" + realUserId + ", " : "") +
                (departmentId != null ? "departmentId=" + departmentId + ", " : "") +
                (topicsVotedId != null ? "topicsVotedId=" + topicsVotedId + ", " : "") +
                (tagedInMemoeriesId != null ? "tagedInMemoeriesId=" + tagedInMemoeriesId + ", " : "") +
            "}";
    }

}
