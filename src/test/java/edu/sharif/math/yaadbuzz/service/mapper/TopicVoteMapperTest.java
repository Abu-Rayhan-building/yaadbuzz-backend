package edu.sharif.math.yaadbuzz.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TopicVoteMapperTest {

    private TopicVoteMapper topicVoteMapper;

    @BeforeEach
    public void setUp() {
        topicVoteMapper = new TopicVoteMapperImpl();
    }
}
