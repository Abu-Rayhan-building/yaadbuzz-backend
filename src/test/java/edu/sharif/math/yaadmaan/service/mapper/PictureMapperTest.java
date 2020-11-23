package edu.sharif.math.yaadmaan.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.sharif.math.yaadmaan.service.mapper.PictureMapper;
import edu.sharif.math.yaadmaan.service.mapper.PictureMapperImpl;

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
