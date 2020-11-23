package edu.sharif.math.yaadmaan.service.dto;

import org.junit.jupiter.api.Test;

import edu.sharif.math.yaadmaan.service.dto.TopicDTO;
import edu.sharif.math.yaadmaan.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class TopicDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TopicDTO.class);
        TopicDTO topicDTO1 = new TopicDTO();
        topicDTO1.setId(1L);
        TopicDTO topicDTO2 = new TopicDTO();
        assertThat(topicDTO1).isNotEqualTo(topicDTO2);
        topicDTO2.setId(topicDTO1.getId());
        assertThat(topicDTO1).isEqualTo(topicDTO2);
        topicDTO2.setId(2L);
        assertThat(topicDTO1).isNotEqualTo(topicDTO2);
        topicDTO1.setId(null);
        assertThat(topicDTO1).isNotEqualTo(topicDTO2);
    }
}
