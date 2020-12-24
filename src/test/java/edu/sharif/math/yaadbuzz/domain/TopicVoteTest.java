package edu.sharif.math.yaadbuzz.domain;

import static org.assertj.core.api.Assertions.assertThat;

import edu.sharif.math.yaadbuzz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TopicVoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TopicVote.class);
        TopicVote topicVote1 = new TopicVote();
        topicVote1.setId(1L);
        TopicVote topicVote2 = new TopicVote();
        topicVote2.setId(topicVote1.getId());
        assertThat(topicVote1).isEqualTo(topicVote2);
        topicVote2.setId(2L);
        assertThat(topicVote1).isNotEqualTo(topicVote2);
        topicVote1.setId(null);
        assertThat(topicVote1).isNotEqualTo(topicVote2);
    }
}
