package edu.sharif.math.yaadbuzz.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import edu.sharif.math.yaadbuzz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PictureDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PictureDTO.class);
        PictureDTO pictureDTO1 = new PictureDTO();
        pictureDTO1.setId(1L);
        PictureDTO pictureDTO2 = new PictureDTO();
        assertThat(pictureDTO1).isNotEqualTo(pictureDTO2);
        pictureDTO2.setId(pictureDTO1.getId());
        assertThat(pictureDTO1).isEqualTo(pictureDTO2);
        pictureDTO2.setId(2L);
        assertThat(pictureDTO1).isNotEqualTo(pictureDTO2);
        pictureDTO1.setId(null);
        assertThat(pictureDTO1).isNotEqualTo(pictureDTO2);
    }
}
