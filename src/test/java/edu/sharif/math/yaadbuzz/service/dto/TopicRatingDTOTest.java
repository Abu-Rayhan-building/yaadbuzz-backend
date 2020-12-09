package edu.sharif.math.yaadbuzz.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import edu.sharif.math.yaadbuzz.web.rest.TestUtil;

public class TopicRatingDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TopicRatingDTO.class);
        TopicRatingDTO topicRatingDTO1 = new TopicRatingDTO();
        topicRatingDTO1.setId(1L);
        TopicRatingDTO topicRatingDTO2 = new TopicRatingDTO();
        assertThat(topicRatingDTO1).isNotEqualTo(topicRatingDTO2);
        topicRatingDTO2.setId(topicRatingDTO1.getId());
        assertThat(topicRatingDTO1).isEqualTo(topicRatingDTO2);
        topicRatingDTO2.setId(2L);
        assertThat(topicRatingDTO1).isNotEqualTo(topicRatingDTO2);
        topicRatingDTO1.setId(null);
        assertThat(topicRatingDTO1).isNotEqualTo(topicRatingDTO2);
    }
}
