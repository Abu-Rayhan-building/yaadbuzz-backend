package edu.sharif.math.yaadmaan.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.sharif.math.yaadmaan.service.mapper.CharateristicsRepetationMapper;
import edu.sharif.math.yaadmaan.service.mapper.CharateristicsRepetationMapperImpl;

import static org.assertj.core.api.Assertions.assertThat;

public class CharateristicsRepetationMapperTest {

    private CharateristicsRepetationMapper charateristicsRepetationMapper;

    @BeforeEach
    public void setUp() {
        charateristicsRepetationMapper = new CharateristicsRepetationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(charateristicsRepetationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(charateristicsRepetationMapper.fromId(null)).isNull();
    }
}
