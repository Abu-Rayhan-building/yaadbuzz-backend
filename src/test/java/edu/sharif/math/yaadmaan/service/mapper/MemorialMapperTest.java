package edu.sharif.math.yaadmaan.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MemorialMapperTest {

    private MemorialMapper memorialMapper;

    @BeforeEach
    public void setUp() {
        memorialMapper = new MemorialMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(memorialMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(memorialMapper.fromId(null)).isNull();
    }
}
