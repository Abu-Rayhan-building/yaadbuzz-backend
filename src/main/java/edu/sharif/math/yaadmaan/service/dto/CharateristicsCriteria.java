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
 * Criteria class for the {@link edu.sharif.math.yaadmaan.domain.Charateristics} entity. This class is used
 * in {@link edu.sharif.math.yaadmaan.web.rest.CharateristicsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /charateristics?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CharateristicsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private IntegerFilter repetation;

    private LongFilter userPerDepartmentId;

    public CharateristicsCriteria() {
    }

    public CharateristicsCriteria(CharateristicsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.repetation = other.repetation == null ? null : other.repetation.copy();
        this.userPerDepartmentId = other.userPerDepartmentId == null ? null : other.userPerDepartmentId.copy();
    }

    @Override
    public CharateristicsCriteria copy() {
        return new CharateristicsCriteria(this);
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

    public IntegerFilter getRepetation() {
        return repetation;
    }

    public void setRepetation(IntegerFilter repetation) {
        this.repetation = repetation;
    }

    public LongFilter getUserPerDepartmentId() {
        return userPerDepartmentId;
    }

    public void setUserPerDepartmentId(LongFilter userPerDepartmentId) {
        this.userPerDepartmentId = userPerDepartmentId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CharateristicsCriteria that = (CharateristicsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(repetation, that.repetation) &&
            Objects.equals(userPerDepartmentId, that.userPerDepartmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        repetation,
        userPerDepartmentId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CharateristicsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (repetation != null ? "repetation=" + repetation + ", " : "") +
                (userPerDepartmentId != null ? "userPerDepartmentId=" + userPerDepartmentId + ", " : "") +
            "}";
    }

}
