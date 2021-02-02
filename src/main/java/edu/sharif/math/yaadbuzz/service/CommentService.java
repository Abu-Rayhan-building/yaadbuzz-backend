package edu.sharif.math.yaadbuzz.service;

import edu.sharif.math.yaadbuzz.domain.Comment;
import edu.sharif.math.yaadbuzz.domain.Comment;
import edu.sharif.math.yaadbuzz.repository.CommentRepository;
import edu.sharif.math.yaadbuzz.repository.CommentRepository;
import edu.sharif.math.yaadbuzz.repository.PictureRepository;
import edu.sharif.math.yaadbuzz.service.DepartmentService;
import edu.sharif.math.yaadbuzz.service.MemoryService;
import edu.sharif.math.yaadbuzz.service.UserPerDepartmentService;
import edu.sharif.math.yaadbuzz.service.UserService;
import edu.sharif.math.yaadbuzz.service.dto.CommentDTO;
import edu.sharif.math.yaadbuzz.service.dto.CommentDTO;
import edu.sharif.math.yaadbuzz.service.dto.PictureDTO;
import edu.sharif.math.yaadbuzz.service.mapper.CommentMapper;
import edu.sharif.math.yaadbuzz.service.mapper.CommentMapper;
import edu.sharif.math.yaadbuzz.service.mapper.PictureMapper;
import edu.sharif.math.yaadbuzz.web.rest.dto.CommentWithIdUDTO;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Comment}.
 */
@Service
@Transactional
public class CommentService implements ServiceWithCurrentUserCrudAccess {

    private final Logger log = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemoryService memoryService;

    private final UserPerDepartmentService userPerDepartmentService;

    private final CommentMapper commentMapper;

    public CommentService(
        final CommentMapper commentMapper,
        final DepartmentService departmentService,
        final UserPerDepartmentService userPerDepartmentService
    ) {
        this.userPerDepartmentService = userPerDepartmentService;
        this.commentMapper = commentMapper;
    }

    public boolean currentuserHasCreateAccess(final Long memid) {
        return this.memoryService.currentuserHasGetAccess(memid);
    }

    public boolean currentuserHasGetAccess(Long memId, final Long id) {
        return this.memoryService.currentuserHasGetAccess(memId);
    }

    @Override
    public boolean currentuserHasGetAccess(Long id) {
        return false;
    }

    public boolean currentuserHasUpdateAccess(final Long id) {
        return false;
        //	final var comment = this.commentRepository.getOne(id);
        //
        //	final var currentUserId = this.userPerDepartmentService
        //		.getCurrentUserInDep(
        //			comment.getMemory().getDepartment().getId());
        //	return comment.getWriter().getId().equals(currentUserId);
    }

    public Optional<CommentDTO> partialUpdate(CommentDTO commentDTO) {
        log.debug("Request to partially update Comment : {}", commentDTO);

        return commentRepository
            .findById(commentDTO.getId())
            .map(
                existingComment -> {
                    if (commentDTO.getText() != null) {
                        existingComment.setText(commentDTO.getText());
                    }

                    return existingComment;
                }
            )
            .map(commentRepository::save)
            .map(commentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<CommentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Comments");
        return commentRepository.findAll(pageable).map(commentMapper::toDto);
    }

    public Page<CommentDTO> findAllForChildrenComments(final Long parentCommentId, final Pageable pageable) {
        this.log.debug("Request to get all Comments for post");

        return this.commentRepository.findAllForChildrenComments(parentCommentId, pageable).map(this.commentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<CommentDTO> findOne(Long id) {
        log.debug("Request to get Comment : {}", id);
        return commentRepository.findById(id).map(commentMapper::toDto);
    }

    public CommentDTO save(final CommentDTO commentDTO) {
        this.log.debug("Request to save Comment : {}", commentDTO);
        if (commentDTO.getId() != null) {
            final var com = this.findOne(commentDTO.getId());
            if (!com.get().getWriter().equals(commentDTO.getWriter())) {
                throw new RuntimeException("bad req in CommentDTO save");
            }
        }
        Comment comment = this.commentMapper.toEntity(commentDTO);
        comment = this.commentRepository.save(comment);
        return this.commentMapper.toDto(comment);
    }

    public CommentDTO save(final CommentWithIdUDTO commentUpdateReqDTO) {
        this.log.debug("Request to save Comment : {}", commentUpdateReqDTO);
        var com = this.findOne(commentUpdateReqDTO.getId()).get();
        com.setText(commentUpdateReqDTO.getText());
        com.setPictures(commentUpdateReqDTO.getPictures());
        Comment comment = this.commentMapper.toEntity(com);
        comment = this.commentRepository.save(comment);
        return this.commentMapper.toDto(comment);
    }

    public void delete(Long id) {
        log.debug("Request to delete Comment : {}", id);
        commentRepository.deleteById(id);
    }

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private PictureMapper pictureMapper;

    public PictureDTO getPicture(Long comId, Long picId) {
        final var com = this.findOne(comId);
        boolean flag = com
            .get()
            .getPictures()
            .parallelStream()
            .anyMatch(
                p -> {
                    if (p.getId().equals(picId)) return true;
                    return false;
                }
            );
        if (flag == false) throw new AccessDeniedException("");

        var picture = pictureRepository.getOne(picId);
        return pictureMapper.toDto(picture);
    }
}
