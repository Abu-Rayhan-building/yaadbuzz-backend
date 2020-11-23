package edu.sharif.math.yaadmaan.domain;

import org.junit.jupiter.api.Test;

import edu.sharif.math.yaadmaan.domain.Topic;
import edu.sharif.math.yaadmaan.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class TopicTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Topic.class);
        Topic topic1 = new Topic();
        topic1.setId(1L);
        Topic topic2 = new Topic();
        topic2.setId(topic1.getId());
        assertThat(topic1).isEqualTo(topic2);
        topic2.setId(2L);
        assertThat(topic1).isNotEqualTo(topic2);
        topic1.setId(null);
        assertThat(topic1).isNotEqualTo(topic2);
    }
}
