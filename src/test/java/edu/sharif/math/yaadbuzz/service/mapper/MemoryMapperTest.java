package edu.sharif.math.yaadbuzz.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MemoryMapperTest {

    private MemoryMapper memoryMapper;

    @BeforeEach
    public void setUp() {
        memoryMapper = new MemoryMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(memoryMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(memoryMapper.fromId(null)).isNull();
    }
}
