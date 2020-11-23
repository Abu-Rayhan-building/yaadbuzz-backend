package edu.sharif.math.yaadmaan.domain;

import org.junit.jupiter.api.Test;

import edu.sharif.math.yaadmaan.domain.UserPerDepartment;
import edu.sharif.math.yaadmaan.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class UserPerDepartmentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserPerDepartment.class);
        UserPerDepartment userPerDepartment1 = new UserPerDepartment();
        userPerDepartment1.setId(1L);
        UserPerDepartment userPerDepartment2 = new UserPerDepartment();
        userPerDepartment2.setId(userPerDepartment1.getId());
        assertThat(userPerDepartment1).isEqualTo(userPerDepartment2);
        userPerDepartment2.setId(2L);
        assertThat(userPerDepartment1).isNotEqualTo(userPerDepartment2);
        userPerDepartment1.setId(null);
        assertThat(userPerDepartment1).isNotEqualTo(userPerDepartment2);
    }
}
