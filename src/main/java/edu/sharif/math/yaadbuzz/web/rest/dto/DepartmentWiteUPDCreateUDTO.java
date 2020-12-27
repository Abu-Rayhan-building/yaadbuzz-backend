package edu.sharif.math.yaadbuzz.web.rest.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class DepartmentWiteUPDCreateUDTO implements Serializable,
	UserInputDTO<DepartmentWithUserPerDepartmentDTO> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private DepartmentCreateUDTO departmentCreateUDTO;

    public DepartmentCreateUDTO getDepartmentCreateUDTO() {
	return departmentCreateUDTO;
    }

    public void setDepartmentCreateUDTO(
	    DepartmentCreateUDTO departmentCreateUDTO) {
	this.departmentCreateUDTO = departmentCreateUDTO;
    }

    public UserPerDepartmentUDTO getUserPerDepartmentUDTO() {
	return userPerDepartmentUDTO;
    }

    public void setUserPerDepartmentUDTO(
	    UserPerDepartmentUDTO userPerDepartmentUDTO) {
	this.userPerDepartmentUDTO = userPerDepartmentUDTO;
    }

    private UserPerDepartmentUDTO userPerDepartmentUDTO;

    @Override
    public DepartmentWithUserPerDepartmentDTO build() {
	var res = new DepartmentWithUserPerDepartmentDTO();
	res.setDepartmentDTO(this.getDepartmentCreateUDTO().build());
	res.setUserPerDepartmentDTO(this.getUserPerDepartmentUDTO().build());
	return res;
    }

}
