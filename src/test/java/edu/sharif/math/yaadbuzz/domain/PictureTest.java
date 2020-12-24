package edu.sharif.math.yaadbuzz.domain;

import static org.assertj.core.api.Assertions.assertThat;

import edu.sharif.math.yaadbuzz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PictureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Picture.class);
        Picture picture1 = new Picture();
        picture1.setId(1L);
        Picture picture2 = new Picture();
        picture2.setId(picture1.getId());
        assertThat(picture1).isEqualTo(picture2);
        picture2.setId(2L);
        assertThat(picture1).isNotEqualTo(picture2);
        picture1.setId(null);
        assertThat(picture1).isNotEqualTo(picture2);
    }
}
