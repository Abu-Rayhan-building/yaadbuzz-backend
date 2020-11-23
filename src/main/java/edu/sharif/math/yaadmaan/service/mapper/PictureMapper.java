package edu.sharif.math.yaadmaan.service.mapper;


import org.mapstruct.*;

import edu.sharif.math.yaadmaan.domain.*;
import edu.sharif.math.yaadmaan.service.dto.PictureDTO;

/**
 * Mapper for the entity {@link Picture} and its DTO {@link PictureDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PictureMapper extends EntityMapper<PictureDTO, Picture> {



    default Picture fromId(Long id) {
        if (id == null) {
            return null;
        }
        Picture picture = new Picture();
        picture.setId(id);
        return picture;
    }
}
