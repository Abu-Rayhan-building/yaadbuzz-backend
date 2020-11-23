package edu.sharif.math.yaadmaan.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.sharif.math.yaadmaan.service.mapper.CharateristicsMapper;
import edu.sharif.math.yaadmaan.service.mapper.CharateristicsMapperImpl;

import static org.assertj.core.api.Assertions.assertThat;

public class CharateristicsMapperTest {

    private CharateristicsMapper charateristicsMapper;

    @BeforeEach
    public void setUp() {
        charateristicsMapper = new CharateristicsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(charateristicsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(charateristicsMapper.fromId(null)).isNull();
    }
}
