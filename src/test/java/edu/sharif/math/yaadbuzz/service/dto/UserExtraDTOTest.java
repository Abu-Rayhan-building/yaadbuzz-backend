package edu.sharif.math.yaadbuzz.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import edu.sharif.math.yaadbuzz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserExtraDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserExtraDTO.class);
        UserExtraDTO userExtraDTO1 = new UserExtraDTO();
        userExtraDTO1.setId(1L);
        UserExtraDTO userExtraDTO2 = new UserExtraDTO();
        assertThat(userExtraDTO1).isNotEqualTo(userExtraDTO2);
        userExtraDTO2.setId(userExtraDTO1.getId());
        assertThat(userExtraDTO1).isEqualTo(userExtraDTO2);
        userExtraDTO2.setId(2L);
        assertThat(userExtraDTO1).isNotEqualTo(userExtraDTO2);
        userExtraDTO1.setId(null);
        assertThat(userExtraDTO1).isNotEqualTo(userExtraDTO2);
    }
}
