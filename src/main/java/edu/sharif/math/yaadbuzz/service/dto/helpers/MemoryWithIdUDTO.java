package edu.sharif.math.yaadbuzz.service.dto.helpers;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import edu.sharif.math.yaadbuzz.service.dto.MemoryDTO;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.Memory} entity.
 */
public class MemoryWithIdUDTO implements Serializable, UserInputDTO<MemoryDTO> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private Boolean isPrivate;

    private Long textId;

    private Set<UserPerDepartmentDTO> tageds = new HashSet<>();

    @Override
    public MemoryDTO build() {
	final var res = new MemoryDTO();
	res.setIsPrivate(this.isPrivate);
	res.setTageds(this.tageds);
	res.setTextId(this.textId);
	res.setTitle(this.title);
	res.setId(this.id);
	return res;
    }

    @Override
    public boolean equals(final Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof MemoryDTO)) {
	    return false;
	}

	return this.id != null && this.id.equals(((MemoryWithIdUDTO) o).id);
    }

    public Long getId() {
	return this.id;
    }

    public Set<UserPerDepartmentDTO> getTageds() {
	return this.tageds;
    }

    public Long getTextId() {
	return this.textId;
    }

    public String getTitle() {
	return this.title;
    }

    @Override
    public int hashCode() {
	return 31;
    }

    public Boolean isIsPrivate() {
	return this.isPrivate;
    }

    public void setId(final Long id) {
	this.id = id;
    }

    public void setIsPrivate(final Boolean isPrivate) {
	this.isPrivate = isPrivate;
    }

    public void setTageds(final Set<UserPerDepartmentDTO> userPerDepartments) {
	this.tageds = userPerDepartments;
    }

    public void setTextId(final Long commentId) {
	this.textId = commentId;
    }

    public void setTitle(final String title) {
	this.title = title;
    }

    // prettier-ignore
    @Override
    public String toString() {
	return "MemoryDTO{" + "id=" + this.getId() + ", title='"
		+ this.getTitle() + "'" + ", isPrivate='" + this.isIsPrivate()
		+ "'" + ", textId=" + this.getTextId() + ", tageds='"
		+ this.getTageds() + "'" + "}";
    }
}