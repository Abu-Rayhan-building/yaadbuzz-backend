package edu.sharif.math.yaadmaan.service.dto;

import org.junit.jupiter.api.Test;

import edu.sharif.math.yaadmaan.service.dto.CommentDTO;
import edu.sharif.math.yaadmaan.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentDTO.class);
        CommentDTO commentDTO1 = new CommentDTO();
        commentDTO1.setId(1L);
        CommentDTO commentDTO2 = new CommentDTO();
        assertThat(commentDTO1).isNotEqualTo(commentDTO2);
        commentDTO2.setId(commentDTO1.getId());
        assertThat(commentDTO1).isEqualTo(commentDTO2);
        commentDTO2.setId(2L);
        assertThat(commentDTO1).isNotEqualTo(commentDTO2);
        commentDTO1.setId(null);
        assertThat(commentDTO1).isNotEqualTo(commentDTO2);
    }
}
