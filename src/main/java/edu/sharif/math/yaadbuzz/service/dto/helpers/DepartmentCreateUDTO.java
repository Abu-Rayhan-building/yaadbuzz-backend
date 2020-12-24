package edu.sharif.math.yaadbuzz.service.dto.helpers;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import edu.sharif.math.yaadbuzz.service.dto.DepartmentDTO;
import edu.sharif.math.yaadbuzz.service.dto.PictureDTO;

public class DepartmentCreateUDTO
	implements Serializable, UserInputDTO<DepartmentDTO> {
    @NotNull
    private String name;

    @NotNull
    private String password;

    private Long avatarId;

    public Long getAvatarId() {
	return this.avatarId;
    }

    public String getName() {
	return this.name;
    }

    public String getPassword() {
	return this.password;
    }

    public void setAvatarId(final Long avatarId) {
	this.avatarId = avatarId;
    }

    public void setName(final String name) {
	this.name = name;
    }

    public void setPassword(final String password) {
	this.password = password;
    }

    @Override
    public DepartmentDTO build() {
	DepartmentDTO res = new DepartmentDTO();
	{
	    var avatar = new PictureDTO();
	    avatar.setId(this.getAvatarId());
	    res.setAvatar(avatar);
	}
	res.setName(this.getName());
	res.setPassword(this.getPassword());
	return res;
    }
}