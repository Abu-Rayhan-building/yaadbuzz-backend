package edu.sharif.math.yaadmaan.domain;

import org.junit.jupiter.api.Test;

import edu.sharif.math.yaadmaan.domain.MemoryPicture;
import edu.sharif.math.yaadmaan.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryPictureTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemoryPicture.class);
        MemoryPicture memoryPicture1 = new MemoryPicture();
        memoryPicture1.setId(1L);
        MemoryPicture memoryPicture2 = new MemoryPicture();
        memoryPicture2.setId(memoryPicture1.getId());
        assertThat(memoryPicture1).isEqualTo(memoryPicture2);
        memoryPicture2.setId(2L);
        assertThat(memoryPicture1).isNotEqualTo(memoryPicture2);
        memoryPicture1.setId(null);
        assertThat(memoryPicture1).isNotEqualTo(memoryPicture2);
    }
}
