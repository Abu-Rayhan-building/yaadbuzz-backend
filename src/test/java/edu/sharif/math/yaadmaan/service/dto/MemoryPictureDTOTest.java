package edu.sharif.math.yaadmaan.service.dto;

import org.junit.jupiter.api.Test;

import edu.sharif.math.yaadmaan.service.dto.MemoryPictureDTO;
import edu.sharif.math.yaadmaan.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryPictureDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemoryPictureDTO.class);
        MemoryPictureDTO memoryPictureDTO1 = new MemoryPictureDTO();
        memoryPictureDTO1.setId(1L);
        MemoryPictureDTO memoryPictureDTO2 = new MemoryPictureDTO();
        assertThat(memoryPictureDTO1).isNotEqualTo(memoryPictureDTO2);
        memoryPictureDTO2.setId(memoryPictureDTO1.getId());
        assertThat(memoryPictureDTO1).isEqualTo(memoryPictureDTO2);
        memoryPictureDTO2.setId(2L);
        assertThat(memoryPictureDTO1).isNotEqualTo(memoryPictureDTO2);
        memoryPictureDTO1.setId(null);
        assertThat(memoryPictureDTO1).isNotEqualTo(memoryPictureDTO2);
    }
}
