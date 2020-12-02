package edu.sharif.math.yaadmaan.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import edu.sharif.math.yaadmaan.web.rest.TestUtil;

public class MemoryDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
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
