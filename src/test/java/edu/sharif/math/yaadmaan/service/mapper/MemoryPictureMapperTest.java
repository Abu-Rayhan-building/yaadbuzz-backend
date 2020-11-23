package edu.sharif.math.yaadmaan.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.sharif.math.yaadmaan.service.mapper.MemoryPictureMapper;
import edu.sharif.math.yaadmaan.service.mapper.MemoryPictureMapperImpl;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryPictureMapperTest {

    private MemoryPictureMapper memoryPictureMapper;

    @BeforeEach
    public void setUp() {
        memoryPictureMapper = new MemoryPictureMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(memoryPictureMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(memoryPictureMapper.fromId(null)).isNull();
    }
}
