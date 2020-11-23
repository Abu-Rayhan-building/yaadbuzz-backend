package edu.sharif.math.yaadmaan.domain;

import org.junit.jupiter.api.Test;

import edu.sharif.math.yaadmaan.domain.Charateristics;
import edu.sharif.math.yaadmaan.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

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
