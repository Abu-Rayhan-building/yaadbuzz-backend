package edu.sharif.math.yaadbuzz.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import edu.sharif.math.yaadbuzz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MemoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemoryDTO.class);
        MemoryDTO memoryDTO1 = new MemoryDTO();
        memoryDTO1.setId(1L);
        MemoryDTO memoryDTO2 = new MemoryDTO();
        assertThat(memoryDTO1).isNotEqualTo(memoryDTO2);
        memoryDTO2.setId(memoryDTO1.getId());
        assertThat(memoryDTO1).isEqualTo(memoryDTO2);
        memoryDTO2.setId(2L);
        assertThat(memoryDTO1).isNotEqualTo(memoryDTO2);
        memoryDTO1.setId(null);
        assertThat(memoryDTO1).isNotEqualTo(memoryDTO2);
    }
}
