package edu.sharif.math.yaadmaan.domain;

import org.junit.jupiter.api.Test;

import edu.sharif.math.yaadmaan.domain.CharateristicsRepetation;
import edu.sharif.math.yaadmaan.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class CharateristicsRepetationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CharateristicsRepetation.class);
        CharateristicsRepetation charateristicsRepetation1 = new CharateristicsRepetation();
        charateristicsRepetation1.setId(1L);
        CharateristicsRepetation charateristicsRepetation2 = new CharateristicsRepetation();
        charateristicsRepetation2.setId(charateristicsRepetation1.getId());
        assertThat(charateristicsRepetation1).isEqualTo(charateristicsRepetation2);
        charateristicsRepetation2.setId(2L);
        assertThat(charateristicsRepetation1).isNotEqualTo(charateristicsRepetation2);
        charateristicsRepetation1.setId(null);
        assertThat(charateristicsRepetation1).isNotEqualTo(charateristicsRepetation2);
    }
}
