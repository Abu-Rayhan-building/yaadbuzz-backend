package edu.sharif.math.yaadbuzz.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import edu.sharif.math.yaadbuzz.service.dto.CommentDTO;
import edu.sharif.math.yaadbuzz.service.dto.MemoryDTO;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.Memory} entity.
 */
public class MemoryUDTO implements Serializable, UserInputDTO<MemoryDTO> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String title;

    private Boolean isPrivate;

    private CommentCreateUDTO baseComment;

    private Set<UserPerDepartmentDTO> tageds = new HashSet<>();

    @Override
    public MemoryDTO build() {
	final var res = new MemoryDTO();
	res.setIsPrivate(this.isPrivate);
	res.setTageds(this.tageds);
	{
	    var com = this.getBaseComment().build();
	    res.setBaseComment(com);
	}
	res.setTitle(this.title);
	return res;
    }

    public Set<UserPerDepartmentDTO> getTageds() {
	return this.tageds;
    }

    public CommentCreateUDTO getBaseComment() {
	return this.baseComment;
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

    public void setIsPrivate(final Boolean isPrivate) {
	this.isPrivate = isPrivate;
    }

    public void setTageds(final Set<UserPerDepartmentDTO> userPerDepartments) {
	this.tageds = userPerDepartments;
    }

    public void setBaseComment(final CommentCreateUDTO comment) {
	this.baseComment = comment;
    }

    public void setTitle(final String title) {
	this.title = title;
    }

    // prettier-ignore
    @Override
    public String toString() {
	return "MemoryDTO{" + ", title='" + this.getTitle() + "'"
		+ ", isPrivate='" + this.isIsPrivate() + "'" + ", textId="
		+ this.getBaseComment() + ", tageds='" + this.getTageds() + "'"
		+ "}";
    }
}
