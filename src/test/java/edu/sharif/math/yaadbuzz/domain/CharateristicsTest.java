package edu.sharif.math.yaadbuzz.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import edu.sharif.math.yaadbuzz.web.rest.TestUtil;

public class CharateristicsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Charateristics.class);
        Charateristics charateristics1 = new Charateristics();
        charateristics1.setId(1L);
        Charateristics charateristics2 = new Charateristics();
        charateristics2.setId(charateristics1.getId());
        assertThat(charateristics1).isEqualTo(charateristics2);
        charateristics2.setId(2L);
        assertThat(charateristics1).isNotEqualTo(charateristics2);
        charateristics1.setId(null);
        assertThat(charateristics1).isNotEqualTo(charateristics2);
    }
}
