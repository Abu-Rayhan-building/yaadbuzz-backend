package edu.sharif.math.yaadbuzz.service.dto.helpers;

import java.io.Serializable;

import edu.sharif.math.yaadbuzz.service.dto.PictureDTO;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.UserPerDepartment}
 * entity.
 */
public class UserPerDepartmentWithIdUDTO
	implements Serializable, UserInputDTO<UserPerDepartmentDTO> {

    public UserPerDepartmentDTO build() {
	var res = new UserPerDepartmentDTO();
	{
	    var ava = new PictureDTO();
	    ava.setId(getAvatarId());
	    res.setAvatar(ava);
	}
	res.setBio(this.getBio());
	res.setNicName(this.getNicName());
	res.setId(id);
	return res;
    }

    private Long id;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    private String nicName;

    private String bio;

    private Long avatarId;

    public String getNicName() {
	return nicName;
    }

    public void setNicName(String nicName) {
	this.nicName = nicName;
    }

    public String getBio() {
	return bio;
    }

    public void setBio(String bio) {
	this.bio = bio;
    }

    public Long getAvatarId() {
	return avatarId;
    }

    public void setAvatarId(Long pictureId) {
	this.avatarId = pictureId;
    }

    @Override
    public int hashCode() {
	return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
	return "UserPerDepartmentDTO{" + ", nicName='" + getNicName() + "'"
		+ ", bio='" + getBio() + "'" + ", avatarId=" + getAvatarId()
		+ "}";
    }
}
