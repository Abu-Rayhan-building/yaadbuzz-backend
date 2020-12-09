package edu.sharif.math.yaadbuzz.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import edu.sharif.math.yaadbuzz.web.rest.TestUtil;

public class MemorialDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
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
