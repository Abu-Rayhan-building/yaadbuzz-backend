package edu.sharif.math.yaadmaan.service.dto;

import org.junit.jupiter.api.Test;

import edu.sharif.math.yaadmaan.service.dto.CharateristicsRepetationDTO;
import edu.sharif.math.yaadmaan.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class CharateristicsRepetationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CharateristicsRepetationDTO.class);
        CharateristicsRepetationDTO charateristicsRepetationDTO1 = new CharateristicsRepetationDTO();
        charateristicsRepetationDTO1.setId(1L);
        CharateristicsRepetationDTO charateristicsRepetationDTO2 = new CharateristicsRepetationDTO();
        assertThat(charateristicsRepetationDTO1).isNotEqualTo(charateristicsRepetationDTO2);
        charateristicsRepetationDTO2.setId(charateristicsRepetationDTO1.getId());
        assertThat(charateristicsRepetationDTO1).isEqualTo(charateristicsRepetationDTO2);
        charateristicsRepetationDTO2.setId(2L);
        assertThat(charateristicsRepetationDTO1).isNotEqualTo(charateristicsRepetationDTO2);
        charateristicsRepetationDTO1.setId(null);
        assertThat(charateristicsRepetationDTO1).isNotEqualTo(charateristicsRepetationDTO2);
    }
}
