package edu.sharif.math.yaadmaan.service.mapper;


import org.mapstruct.*;

import edu.sharif.math.yaadmaan.domain.*;
import edu.sharif.math.yaadmaan.service.dto.CommentDTO;

/**
 * Mapper for the entity {@link Comment} and its DTO {@link CommentDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserPerDepartmentMapper.class, MemoryMapper.class})
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {

    @Mapping(source = "writer.id", target = "writerId")
    @Mapping(source = "memory.id", target = "memoryId")
    CommentDTO toDto(Comment comment);

    @Mapping(source = "writerId", target = "writer")
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
