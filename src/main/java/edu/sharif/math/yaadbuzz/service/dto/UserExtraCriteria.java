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
 * Criteria class for the {@link edu.sharif.math.yaadbuzz.domain.UserExtra} entity. This class is used
 * in {@link edu.sharif.math.yaadbuzz.web.rest.UserExtraResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-extras?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserExtraCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter phone;

    private LongFilter userId;

    private LongFilter defaultUserPerDepartmentId;

    public UserExtraCriteria() {}

    public UserExtraCriteria(UserExtraCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.defaultUserPerDepartmentId = other.defaultUserPerDepartmentId == null ? null : other.defaultUserPerDepartmentId.copy();
    }

    @Override
    public UserExtraCriteria copy() {
        return new UserExtraCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getDefaultUserPerDepartmentId() {
        return defaultUserPerDepartmentId;
    }

    public void setDefaultUserPerDepartmentId(LongFilter defaultUserPerDepartmentId) {
        this.defaultUserPerDepartmentId = defaultUserPerDepartmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserExtraCriteria that = (UserExtraCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(defaultUserPerDepartmentId, that.defaultUserPerDepartmentId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phone, userId, defaultUserPerDepartmentId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserExtraCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (defaultUserPerDepartmentId != null ? "defaultUserPerDepartmentId=" + defaultUserPerDepartmentId + ", " : "") +
            "}";
    }
}
