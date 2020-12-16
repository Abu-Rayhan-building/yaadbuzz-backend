package edu.sharif.math.yaadbuzz.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import edu.sharif.math.yaadbuzz.web.rest.TestUtil;

public class TopicVoteDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TopicVoteDTO.class);
        TopicVoteDTO topicVoteDTO1 = new TopicVoteDTO();
        topicVoteDTO1.setId(1L);
        TopicVoteDTO topicVoteDTO2 = new TopicVoteDTO();
        assertThat(topicVoteDTO1).isNotEqualTo(topicVoteDTO2);
        topicVoteDTO2.setId(topicVoteDTO1.getId());
        assertThat(topicVoteDTO1).isEqualTo(topicVoteDTO2);
        topicVoteDTO2.setId(2L);
        assertThat(topicVoteDTO1).isNotEqualTo(topicVoteDTO2);
        topicVoteDTO1.setId(null);
        assertThat(topicVoteDTO1).isNotEqualTo(topicVoteDTO2);
    }
}
