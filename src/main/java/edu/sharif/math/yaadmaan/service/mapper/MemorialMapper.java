package edu.sharif.math.yaadmaan.service.mapper;


import edu.sharif.math.yaadmaan.domain.*;
import edu.sharif.math.yaadmaan.service.dto.MemorialDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Memorial} and its DTO {@link MemorialDTO}.
 */
@Mapper(componentModel = "spring", uses = {CommentMapper.class, UserPerDepartmentMapper.class})
public interface MemorialMapper extends EntityMapper<MemorialDTO, Memorial> {

    @Mapping(source = "anonymousComment.id", target = "anonymousCommentId")
    @Mapping(source = "notAnonymousComment.id", target = "notAnonymousCommentId")
    @Mapping(source = "writer.id", target = "writerId")
    @Mapping(source = "recipient.id", target = "recipientId")
    MemorialDTO toDto(Memorial memorial);

    @Mapping(source = "anonymousCommentId", target = "anonymousComment")
    @Mapping(source = "notAnonymousCommentId", target = "notAnonymousComment")
    @Mapping(source = "writerId", target = "writer")
    @Mapping(source = "recipientId", target = "recipient")
    Memorial toEntity(MemorialDTO memorialDTO);

    default Memorial fromId(Long id) {
        if (id == null) {
            return null;
        }
        Memorial memorial = new Memorial();
        memorial.setId(id);
        return memorial;
    }
}
