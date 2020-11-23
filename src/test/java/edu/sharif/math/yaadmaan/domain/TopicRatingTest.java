package edu.sharif.math.yaadmaan.domain;

import org.junit.jupiter.api.Test;

import edu.sharif.math.yaadmaan.domain.TopicRating;
import edu.sharif.math.yaadmaan.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class TopicRatingTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TopicRating.class);
        TopicRating topicRating1 = new TopicRating();
        topicRating1.setId(1L);
        TopicRating topicRating2 = new TopicRating();
        topicRating2.setId(topicRating1.getId());
        assertThat(topicRating1).isEqualTo(topicRating2);
        topicRating2.setId(2L);
        assertThat(topicRating1).isNotEqualTo(topicRating2);
        topicRating1.setId(null);
        assertThat(topicRating1).isNotEqualTo(topicRating2);
    }
}
