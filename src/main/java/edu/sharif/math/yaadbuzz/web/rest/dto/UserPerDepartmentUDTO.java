package edu.sharif.math.yaadbuzz.web.rest.dto;

import java.io.Serializable;

import edu.sharif.math.yaadbuzz.service.dto.PictureDTO;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.UserPerDepartment}
 * entity.
 */
public class UserPerDepartmentUDTO
	implements Serializable, UserInputDTO<UserPerDepartmentDTO> {

    public UserPerDepartmentDTO build() {
	var res = new UserPerDepartmentDTO();
	{
	    var ava = new PictureDTO();
	    ava.setId(getAvatarId());
	    res.setAvatar(ava);
	}
	res.setBio(this.getBio());
	res.setNickname(this.getnickname());
	return res;
    }

    private String nickname;

    private String bio;

    private Long avatarId;

    public String getnickname() {
	return nickname;
    }

    public void setnickname(String nickname) {
	this.nickname = nickname;
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
	return "UserPerDepartmentDTO{" + ", nickname='" + getnickname() + "'"
		+ ", bio='" + getBio() + "'" + ", avatarId=" + getAvatarId()
		+ "}";
    }
}
