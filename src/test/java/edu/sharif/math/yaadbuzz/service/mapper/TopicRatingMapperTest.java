package edu.sharif.math.yaadbuzz.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TopicRatingMapperTest {

    private TopicRatingMapper topicRatingMapper;

    @BeforeEach
    public void setUp() {
        topicRatingMapper = new TopicRatingMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(topicRatingMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(topicRatingMapper.fromId(null)).isNull();
    }
}
