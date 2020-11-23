package edu.sharif.math.yaadmaan.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.sharif.math.yaadmaan.service.mapper.TopicMapper;
import edu.sharif.math.yaadmaan.service.mapper.TopicMapperImpl;

import static org.assertj.core.api.Assertions.assertThat;

public class TopicMapperTest {

    private TopicMapper topicMapper;

    @BeforeEach
    public void setUp() {
        topicMapper = new TopicMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(topicMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(topicMapper.fromId(null)).isNull();
    }
}
