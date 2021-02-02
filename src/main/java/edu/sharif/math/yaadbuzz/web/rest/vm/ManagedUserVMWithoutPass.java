package edu.sharif.math.yaadbuzz.web.rest.vm;

import edu.sharif.math.yaadbuzz.domain.UserExtra;
import edu.sharif.math.yaadbuzz.domain.UserPerDepartment;
import edu.sharif.math.yaadbuzz.service.dto.AdminUserDTO;
import edu.sharif.math.yaadbuzz.service.dto.UserExtraDTO;
import edu.sharif.math.yaadbuzz.web.rest.dto.UserPerDepartmentUDTO;
import javax.validation.constraints.Size;

/**
 * View Model extending the AdminUserDTO, which is meant to be used in the user
 * management UI.
 */
public class ManagedUserVMWithoutPass extends AdminUserDTO {

    private String phone;

    private UserPerDepartmentUDTO defaultUserPerDepartment;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserPerDepartmentUDTO getDefaultUserPerDepartment() {
        return defaultUserPerDepartment;
    }

    public void setDefaultUserPerDepartment(UserPerDepartmentUDTO defaultUserPerDepartment) {
        this.defaultUserPerDepartment = defaultUserPerDepartment;
    }

    public ManagedUserVMWithoutPass() {
        // Empty constructor needed for Jackson.
    }

    // prettier-ignore
    @Override
    public String toString() {
	return "ManagedUserVM{" + super.toString() + "} ";
    }
}
