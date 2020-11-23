package edu.sharif.math.yaadmaan.domain;

import org.junit.jupiter.api.Test;

import edu.sharif.math.yaadmaan.domain.Comment;
import edu.sharif.math.yaadmaan.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Comment.class);
        Comment comment1 = new Comment();
        comment1.setId(1L);
        Comment comment2 = new Comment();
        comment2.setId(comment1.getId());
        assertThat(comment1).isEqualTo(comment2);
        comment2.setId(2L);
        assertThat(comment1).isNotEqualTo(comment2);
        comment1.setId(null);
        assertThat(comment1).isNotEqualTo(comment2);
    }
}
