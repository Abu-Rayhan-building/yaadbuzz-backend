package edu.sharif.math.yaadbuzz.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import edu.sharif.math.yaadbuzz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserPerDepartmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserPerDepartmentDTO.class);
        UserPerDepartmentDTO userPerDepartmentDTO1 = new UserPerDepartmentDTO();
        userPerDepartmentDTO1.setId(1L);
        UserPerDepartmentDTO userPerDepartmentDTO2 = new UserPerDepartmentDTO();
        assertThat(userPerDepartmentDTO1).isNotEqualTo(userPerDepartmentDTO2);
        userPerDepartmentDTO2.setId(userPerDepartmentDTO1.getId());
        assertThat(userPerDepartmentDTO1).isEqualTo(userPerDepartmentDTO2);
        userPerDepartmentDTO2.setId(2L);
        assertThat(userPerDepartmentDTO1).isNotEqualTo(userPerDepartmentDTO2);
        userPerDepartmentDTO1.setId(null);
        assertThat(userPerDepartmentDTO1).isNotEqualTo(userPerDepartmentDTO2);
    }
}
