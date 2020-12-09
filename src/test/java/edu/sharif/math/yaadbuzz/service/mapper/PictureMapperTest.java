package edu.sharif.math.yaadbuzz.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PictureMapperTest {

    private PictureMapper pictureMapper;

    @BeforeEach
    public void setUp() {
        pictureMapper = new PictureMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(pictureMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(pictureMapper.fromId(null)).isNull();
    }
}
