package edu.sharif.math.yaadbuzz.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import edu.sharif.math.yaadbuzz.web.rest.TestUtil;

public class MemorialTest {

    @Test
    public void equalsVerifier() throws Exception {
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
