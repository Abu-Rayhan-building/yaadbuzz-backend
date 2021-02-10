package edu.sharif.math.yaadbuzz.web.rest.dto;

import edu.sharif.math.yaadbuzz.service.dto.CommentDTO;
import edu.sharif.math.yaadbuzz.service.dto.PictureDTO;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link edu.sharif.math.yaadbuzz.domain.Comment} entity.
 */
public class CommentCreateUDTO implements Serializable, UserInputDTO<CommentDTO> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @NotNull
    private String text;

    private Set<PictureDTO> pictures = new HashSet<>();

    public Set<PictureDTO> getPictures() {
        return this.pictures;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public int hashCode() {
        return 31;
    }

    public void setPictures(final Set<PictureDTO> pictures) {
        this.pictures = pictures;
    }

    public void setText(final String text) {
        this.text = text;
    }

    // prettier-ignore
    @Override
    public String toString() {
	return "CommentDTO{" + ", text='" + this.getText() + "'" + ", pictures="
		+ this.getPictures() + "}";
    }

    @Override
    public CommentDTO build() {
        CommentDTO res = new CommentDTO();
        res.setPictures(this.getPictures());
        res.setText(this.getText());
        return res;
    }
}
