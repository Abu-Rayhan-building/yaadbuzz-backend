package edu.sharif.math.yaadbuzz.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TopicVoteMapperTest {

    private TopicVoteMapper topicVoteMapper;

    @BeforeEach
    public void setUp() {
        topicVoteMapper = new TopicVoteMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(topicVoteMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(topicVoteMapper.fromId(null)).isNull();
    }
}
