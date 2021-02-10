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
 * Criteria class for the {@link edu.sharif.math.yaadbuzz.domain.Department}
 * entity. This class is used in
 * {@link edu.sharif.math.yaadbuzz.web.rest.DepartmentResource} to receive all
 * the possible filtering options from the Http GET request parameters. For
 * example the following could be a valid request:
 * {@code /departments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific
 * {@link Filter} class are used, we need to use fix type specific filters.
 */
public class DepartmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter password;

    private LongFilter userPerDepartmentId;

    private LongFilter memoryId;

    private LongFilter avatarId;

    private LongFilter ownerId;

    public DepartmentCriteria() {}

    public DepartmentCriteria(DepartmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.password = other.password == null ? null : other.password.copy();
        this.userPerDepartmentId = other.userPerDepartmentId == null ? null : other.userPerDepartmentId.copy();
        this.memoryId = other.memoryId == null ? null : other.memoryId.copy();
        this.avatarId = other.avatarId == null ? null : other.avatarId.copy();
        this.ownerId = other.ownerId == null ? null : other.ownerId.copy();
    }

    @Override
    public DepartmentCriteria copy() {
        return new DepartmentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getPassword() {
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }

    public LongFilter getUserPerDepartmentId() {
        return userPerDepartmentId;
    }

    public void setUserPerDepartmentId(LongFilter userPerDepartmentId) {
        this.userPerDepartmentId = userPerDepartmentId;
    }

    public LongFilter getMemoryId() {
        return memoryId;
    }

    public void setMemoryId(LongFilter memoryId) {
        this.memoryId = memoryId;
    }

    public LongFilter getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(LongFilter avatarId) {
        this.avatarId = avatarId;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DepartmentCriteria that = (DepartmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(password, that.password) &&
            Objects.equals(userPerDepartmentId, that.userPerDepartmentId) &&
            Objects.equals(memoryId, that.memoryId) &&
            Objects.equals(avatarId, that.avatarId) &&
            Objects.equals(ownerId, that.ownerId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, userPerDepartmentId, memoryId, avatarId, ownerId);
    }

    // prettier-ignore
    @Override
    public String toString() {
	return "DepartmentCriteria{" + (id != null ? "id=" + id + ", " : "")
		+ (name != null ? "name=" + name + ", " : "")
		+ (password != null ? "password=" + password + ", " : "")
		+ (userPerDepartmentId != null
			? "userPerDepartmentId=" + userPerDepartmentId + ", "
			: "")
		+ (memoryId != null ? "memoryId=" + memoryId + ", " : "")
		+ (avatarId != null ? "avatarId=" + avatarId + ", " : "")
		+ (ownerId != null ? "ownerId=" + ownerId + ", " : "") + "}";
    }
}
