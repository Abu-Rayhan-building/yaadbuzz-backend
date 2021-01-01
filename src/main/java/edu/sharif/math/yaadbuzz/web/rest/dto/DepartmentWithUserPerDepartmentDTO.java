package edu.sharif.math.yaadbuzz.web.rest.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import edu.sharif.math.yaadbuzz.service.dto.DepartmentDTO;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;

public class DepartmentWithUserPerDepartmentDTO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private DepartmentDTO departmentDTO;
    private UserPerDepartmentDTO userPerDepartmentDTO;
    public DepartmentDTO getDepartmentDTO() {
        return departmentDTO;
    }
    public void setDepartmentDTO(DepartmentDTO departmentDTO) {
        this.departmentDTO = departmentDTO;
    }
    public UserPerDepartmentDTO getUserPerDepartmentDTO() {
        return userPerDepartmentDTO;
    }
    public void setUserPerDepartmentDTO(UserPerDepartmentDTO userPerDepartmentDTO) {
        this.userPerDepartmentDTO = userPerDepartmentDTO;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }


}

