package edu.sharif.math.yaadbuzz.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import edu.sharif.math.yaadbuzz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CharateristicsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CharateristicsDTO.class);
        CharateristicsDTO charateristicsDTO1 = new CharateristicsDTO();
        charateristicsDTO1.setId(1L);
        CharateristicsDTO charateristicsDTO2 = new CharateristicsDTO();
        assertThat(charateristicsDTO1).isNotEqualTo(charateristicsDTO2);
        charateristicsDTO2.setId(charateristicsDTO1.getId());
        assertThat(charateristicsDTO1).isEqualTo(charateristicsDTO2);
        charateristicsDTO2.setId(2L);
        assertThat(charateristicsDTO1).isNotEqualTo(charateristicsDTO2);
        charateristicsDTO1.setId(null);
        assertThat(charateristicsDTO1).isNotEqualTo(charateristicsDTO2);
    }
}
