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

    private Long anonymousCommentId;

    private Long notAnonymousCommentId;

    private Long recipientId;

    @Override
    public MemorialDTO build() {
	final MemorialDTO res = new MemorialDTO();
	res.setAnonymousCommentId(this.getAnonymousCommentId());
	res.setNotAnonymousCommentId(this.notAnonymousCommentId);
	res.setRecipientId(this.recipientId);
	return res;
    }

    public Long getAnonymousCommentId() {
	return this.anonymousCommentId;
    }

    public Long getNotAnonymousCommentId() {
	return this.notAnonymousCommentId;
    }

    public Long getRecipientId() {
	return this.recipientId;
    }

    @Override
    public int hashCode() {
	return 31;
    }

    public void setAnonymousCommentId(final Long commentId) {
	this.anonymousCommentId = commentId;
    }

    public void setNotAnonymousCommentId(final Long commentId) {
	this.notAnonymousCommentId = commentId;
    }

    public void setRecipientId(final Long userPerDepartmentId) {
	this.recipientId = userPerDepartmentId;
    }

    // prettier-ignore
    @Override
    public String toString() {
	return "MemorialDTO{" + ", anonymousCommentId="
		+ this.getAnonymousCommentId() + ", notAnonymousCommentId="
		+ this.getNotAnonymousCommentId() + ", recipientId="
		+ this.getRecipientId() + "}";
    }
}
