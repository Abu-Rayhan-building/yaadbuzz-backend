package edu.sharif.math.yaadbuzz.service.dto.helpers;

import java.io.Serializable;

import edu.sharif.math.yaadbuzz.service.dto.MemorialDTO;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.Memorial} entity.
 */
public class MemorialUDTO implements Serializable, UserInputDTO<MemorialDTO> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private CommentCreateUDTO anonymousComment;

    private CommentCreateUDTO notAnonymousComment;

    private Long recipientId;

    @Override
    public MemorialDTO build() {
	final MemorialDTO res = new MemorialDTO();
	res.setRecipientId(this.recipientId);
	return res;
    }

    public CommentCreateUDTO getAnonymousComment() {
	return this.anonymousComment;
    }

    public CommentCreateUDTO getNotAnonymousComment() {
	return this.notAnonymousComment;
    }

    public Long getRecipientId() {
	return this.recipientId;
    }

    @Override
    public int hashCode() {
	return 31;
    }

    public void setAnonymousComment(final CommentCreateUDTO anonymousComment) {
	this.anonymousComment = anonymousComment;
    }

    public void setNotAnonymousComment(
	    final CommentCreateUDTO notAnonymousComment) {
	this.notAnonymousComment = notAnonymousComment;
    }

    public void setRecipientId(final Long userPerDepartmentId) {
	this.recipientId = userPerDepartmentId;
    }

    // prettier-ignore
    @Override
    public String toString() {
	return "MemorialDTO{" + ", anonymousCommentId=" + this.getRecipientId()
		+ "}";
    }

}
