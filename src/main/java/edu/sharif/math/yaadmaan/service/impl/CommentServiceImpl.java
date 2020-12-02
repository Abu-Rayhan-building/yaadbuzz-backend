package edu.sharif.math.yaadmaan.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadmaan.domain.Comment;
import edu.sharif.math.yaadmaan.repository.CommentRepository;
import edu.sharif.math.yaadmaan.service.CommentService;
import edu.sharif.math.yaadmaan.service.DepartmentService;
import edu.sharif.math.yaadmaan.service.MemoryService;
import edu.sharif.math.yaadmaan.service.UserService;
import edu.sharif.math.yaadmaan.service.dto.CommentDTO;
import edu.sharif.math.yaadmaan.service.mapper.CommentMapper;

/**
 * Service Implementation for managing {@link Comment}.
 */
@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final Logger log = LoggerFactory
	    .getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;
    private final MemoryService memoryService;

    private final UserService userService;

    private final CommentMapper commentMapper;

    public CommentServiceImpl(final CommentRepository commentRepository,
	    final CommentMapper commentMapper,
	    final DepartmentService departmentService,
	    final MemoryService memoryService, final UserService userService) {
	this.commentRepository = commentRepository;
	this.memoryService = memoryService;
	this.userService = userService;
	this.commentMapper = commentMapper;
    }

    @Override
    public boolean currentuserHasCreateAccess(final Long memid) {
	return this.memoryService.currentuserHasGetAccess(memid);
    }

    @Override
    public boolean currentuserHasGetAccess(final Long id) {
	final var comment = this.commentRepository.getOne(id);
	final var mem = comment.getMemory();
	return this.memoryService.currentuserHasGetAccess(mem.getId());
    }

    @Override
    public boolean currentuserHasUpdateAccess(final Long id) {
	final var comment = this.commentRepository.getOne(id);

	final var currentUserId = this.userService.getCurrentUserId();
	return comment.getWriter().getId().equals(currentUserId);
    }

    @Override
    public void delete(final Long id) {
	this.log.debug("Request to delete Comment : {}", id);
	this.commentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentDTO> findAll(final Pageable pageable) {
	this.log.debug("Request to get all Comments");
	return this.commentRepository.findAll(pageable)
		.map(this.commentMapper::toDto);
    }

    @Override
    public Page<CommentDTO> findAllForMemory(final Long memid,
	    final Pageable pageable) {
	this.log.debug("Request to get all Comments for post");
	return this.commentRepository.findAllForMemory(memid, pageable)
		.map(this.commentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentDTO> findOne(final Long id) {
	this.log.debug("Request to get Comment : {}", id);
	return this.commentRepository.findById(id)
		.map(this.commentMapper::toDto);
    }

    @Override
    public CommentDTO save(final CommentDTO commentDTO) {
	this.log.debug("Request to save Comment : {}", commentDTO);
	if (commentDTO.getId() != null) {
	    final var com = this.findOne(commentDTO.getId());
	    if (!com.get().getMemoryId().equals(commentDTO.getMemoryId())) {
		throw new RuntimeException("bad req in CommentDTO save");
	    }
	}
	Comment comment = this.commentMapper.toEntity(commentDTO);
	comment = this.commentRepository.save(comment);
	return this.commentMapper.toDto(comment);
    }
}
