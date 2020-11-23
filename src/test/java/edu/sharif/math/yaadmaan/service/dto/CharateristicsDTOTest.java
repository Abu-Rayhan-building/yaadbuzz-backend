package edu.sharif.math.yaadmaan.service.dto;

import org.junit.jupiter.api.Test;

import edu.sharif.math.yaadmaan.service.dto.CharateristicsDTO;
import edu.sharif.math.yaadmaan.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class CharateristicsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
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
