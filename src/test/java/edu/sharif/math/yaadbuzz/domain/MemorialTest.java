package edu.sharif.math.yaadbuzz.domain;

import static org.assertj.core.api.Assertions.assertThat;

import edu.sharif.math.yaadbuzz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MemorialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Memorial.class);
        Memorial memorial1 = new Memorial();
        memorial1.setId(1L);
        Memorial memorial2 = new Memorial();
        memorial2.setId(memorial1.getId());
        assertThat(memorial1).isEqualTo(memorial2);
        memorial2.setId(2L);
        assertThat(memorial1).isNotEqualTo(memorial2);
        memorial1.setId(null);
        assertThat(memorial1).isNotEqualTo(memorial2);
    }
}
