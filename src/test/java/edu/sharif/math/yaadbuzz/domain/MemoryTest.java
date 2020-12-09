package edu.sharif.math.yaadbuzz.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import edu.sharif.math.yaadbuzz.web.rest.TestUtil;

public class MemoryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Memory.class);
        Memory memory1 = new Memory();
        memory1.setId(1L);
        Memory memory2 = new Memory();
        memory2.setId(memory1.getId());
        assertThat(memory1).isEqualTo(memory2);
        memory2.setId(2L);
        assertThat(memory1).isNotEqualTo(memory2);
        memory1.setId(null);
        assertThat(memory1).isNotEqualTo(memory2);
    }
}
