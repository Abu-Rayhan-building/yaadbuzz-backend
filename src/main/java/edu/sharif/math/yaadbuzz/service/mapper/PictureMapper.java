package edu.sharif.math.yaadbuzz.service.mapper;


import edu.sharif.math.yaadbuzz.domain.*;
import edu.sharif.math.yaadbuzz.service.dto.PictureDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Picture} and its DTO {@link PictureDTO}.
 */
@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface PictureMapper extends EntityMapper<PictureDTO, Picture> {

    PictureDTO toDto(Picture picture);

    Picture toEntity(PictureDTO pictureDTO);

    default Picture fromId(Long id) {
        if (id == null) {
            return null;
        }
        Picture picture = new Picture();
        picture.setId(id);
        return picture;
    }
}