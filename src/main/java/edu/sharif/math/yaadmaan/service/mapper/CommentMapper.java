package edu.sharif.math.yaadmaan.service.mapper;


import edu.sharif.math.yaadmaan.domain.*;
import edu.sharif.math.yaadmaan.service.dto.CommentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comment} and its DTO {@link CommentDTO}.
 */
@Mapper(componentModel = "spring", uses = {PictureMapper.class, UserPerDepartmentMapper.class, MemoryMapper.class})
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {

    @Mapping(source = "writer.id", target = "writerId")
    @Mapping(source = "memory.id", target = "memoryId")
    CommentDTO toDto(Comment comment);

    @Mapping(source = "writerId", target = "writer")
    @Mapping(target = "removePictures", ignore = true)
    @Mapping(source = "memoryId", target = "memory")
    Comment toEntity(CommentDTO commentDTO);

    default Comment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setId(id);
        return comment;
    }
}
