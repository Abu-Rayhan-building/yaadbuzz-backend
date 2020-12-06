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
 * Criteria class for the {@link edu.sharif.math.yaadmaan.domain.Topic} entity. This class is used
 * in {@link edu.sharif.math.yaadmaan.web.rest.TopicResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /topics?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TopicCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private LongFilter ratingsId;

    private LongFilter departmentId;

    private LongFilter votersId;

    public TopicCriteria() {
    }

    public TopicCriteria(TopicCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.ratingsId = other.ratingsId == null ? null : other.ratingsId.copy();
        this.departmentId = other.departmentId == null ? null : other.departmentId.copy();
        this.votersId = other.votersId == null ? null : other.votersId.copy();
    }

    @Override
    public TopicCriteria copy() {
        return new TopicCriteria(this);
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

    public LongFilter getRatingsId() {
        return ratingsId;
    }

    public void setRatingsId(LongFilter ratingsId) {
        this.ratingsId = ratingsId;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
    }

    public LongFilter getVotersId() {
        return votersId;
    }

    public void setVotersId(LongFilter votersId) {
        this.votersId = votersId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TopicCriteria that = (TopicCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(ratingsId, that.ratingsId) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(votersId, that.votersId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        ratingsId,
        departmentId,
        votersId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TopicCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (ratingsId != null ? "ratingsId=" + ratingsId + ", " : "") +
                (departmentId != null ? "departmentId=" + departmentId + ", " : "") +
                (votersId != null ? "votersId=" + votersId + ", " : "") +
            "}";
    }

}
