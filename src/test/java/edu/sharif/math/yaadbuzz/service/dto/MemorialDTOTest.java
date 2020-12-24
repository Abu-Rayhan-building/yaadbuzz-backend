package edu.sharif.math.yaadbuzz.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import edu.sharif.math.yaadbuzz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MemorialDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemorialDTO.class);
        MemorialDTO memorialDTO1 = new MemorialDTO();
        memorialDTO1.setId(1L);
        MemorialDTO memorialDTO2 = new MemorialDTO();
        assertThat(memorialDTO1).isNotEqualTo(memorialDTO2);
        memorialDTO2.setId(memorialDTO1.getId());
        assertThat(memorialDTO1).isEqualTo(memorialDTO2);
        memorialDTO2.setId(2L);
        assertThat(memorialDTO1).isNotEqualTo(memorialDTO2);
        memorialDTO1.setId(null);
        assertThat(memorialDTO1).isNotEqualTo(memorialDTO2);
    }
}
