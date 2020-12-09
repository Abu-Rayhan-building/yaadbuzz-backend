package edu.sharif.math.yaadbuzz.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import edu.sharif.math.yaadbuzz.web.rest.TestUtil;

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
