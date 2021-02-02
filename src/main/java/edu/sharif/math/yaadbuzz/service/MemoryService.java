package edu.sharif.math.yaadbuzz.service;

import edu.sharif.math.yaadbuzz.domain.Memory;
import edu.sharif.math.yaadbuzz.domain.Memory;
import edu.sharif.math.yaadbuzz.repository.MemoryRepository;
import edu.sharif.math.yaadbuzz.repository.MemoryRepository;
import edu.sharif.math.yaadbuzz.repository.PictureRepository;
import edu.sharif.math.yaadbuzz.repository.UserPerDepartmentRepository;
import edu.sharif.math.yaadbuzz.service.*;
import edu.sharif.math.yaadbuzz.service.MemoryService;
import edu.sharif.math.yaadbuzz.service.dto.CommentDTO;
import edu.sharif.math.yaadbuzz.service.dto.MemoryDTO;
import edu.sharif.math.yaadbuzz.service.dto.MemoryDTO;
import edu.sharif.math.yaadbuzz.service.dto.PictureDTO;
import edu.sharif.math.yaadbuzz.service.mapper.MemoryMapper;
import edu.sharif.math.yaadbuzz.service.mapper.MemoryMapper;
import edu.sharif.math.yaadbuzz.service.mapper.PictureMapper;
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
 * Service Implementation for managing {@link Memory}.
 */
@Service
@Transactional
public class MemoryService implements ServiceWithCurrentUserCrudAccess {

    private final Logger log = LoggerFactory.getLogger(MemoryService.class);

    @Autowired
    private MemoryRepository memoryRepository;

    private final UserPerDepartmentRepository userPerDepartmentRepository;

    private final UserPerDepartmentService userPerDepartmentService;

    private final DepartmentService departmentService;

    private final MemoryMapper memoryMapper;

    private final UserService userService;

    public MemoryService(
        final MemoryMapper memoryMapper,
        final UserPerDepartmentService userPerDepartmentService,
        final UserPerDepartmentRepository userPerDepartmentRepository,
        final UserService userService,
        final DepartmentService departmentService
    ) {
        this.userPerDepartmentRepository = userPerDepartmentRepository;
        this.userPerDepartmentService = userPerDepartmentService;
        this.departmentService = departmentService;
        this.memoryMapper = memoryMapper;
        this.userService = userService;
    }

    public boolean currentuserHasAccessToComments(final Long memid) {
        return this.currentuserHasGetAccess(memid);
    }

    public boolean currentuserHasCreatAccess(final Long depId) {
        return this.departmentService.currentuserHasGetAccess(depId);
    }

    @Override
    public boolean currentuserHasGetAccess(final Long id) {
        final var mem = this.memoryRepository.getOne(id);

        final var currentUserId = this.userService.getCurrentUserId();
        if (this.departmentService.currentuserHasGetAccess(mem.getDepartment().getId()) == false) {
            return false;
        }

        if (mem.getIsPrivate() == false) {
            return true;
        }
        final var flag =
            mem.getWriter().getId().equals(currentUserId) ||
            mem.getTageds().parallelStream().anyMatch(upd -> upd.getId().equals(currentUserId));
        return flag;
    }

    @Override
    public boolean currentuserHasUpdateAccess(final Long id) {
        final var mem = this.memoryRepository.findById(id);
        return mem.get().getWriter().getId().equals(this.userService.getCurrentUserId());
    }

    public MemoryDTO save(MemoryDTO memoryDTO) {
        log.debug("Request to save Memory : {}", memoryDTO);
        Memory memory = memoryMapper.toEntity(memoryDTO);
        memory = memoryRepository.save(memory);
        return memoryMapper.toDto(memory);
    }

    public Optional<MemoryDTO> partialUpdate(MemoryDTO memoryDTO) {
        log.debug("Request to partially update Memory : {}", memoryDTO);

        return memoryRepository
            .findById(memoryDTO.getId())
            .map(
                existingMemory -> {
                    if (memoryDTO.getTitle() != null) {
                        existingMemory.setTitle(memoryDTO.getTitle());
                    }

                    if (memoryDTO.getIsPrivate() != null) {
                        existingMemory.setIsPrivate(memoryDTO.getIsPrivate());
                    }

                    return existingMemory;
                }
            )
            .map(memoryRepository::save)
            .map(memoryMapper::toDto);
    }

    public Page<MemoryDTO> findAllInDepartment(final Long depid, final Pageable pageable) {
        final var t = this.userPerDepartmentService.getCurrentUserInDep(depid);
        final var cupd = this.userPerDepartmentRepository.findById(t.getId()).get();
        return this.memoryRepository.findAllWithDepartmentId(depid, cupd, pageable).map(this.memoryMapper::toDto);
    }

    public Page<MemoryDTO> findAllWithCurrentUserTagedIn(final Long depid, final Pageable pageable) {
        final var t = this.userPerDepartmentService.getCurrentUserInDep(depid);
        final var cupd = this.userPerDepartmentRepository.findById(t.getId()).get();

        return this.memoryRepository.findAllWithCurrentUserTagedIn(depid, cupd, pageable).map(this.memoryMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<MemoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Memories");
        return memoryRepository.findAll(pageable).map(memoryMapper::toDto);
    }

    public Page<MemoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return memoryRepository.findAllWithEagerRelationships(pageable).map(memoryMapper::toDto);
    }

    public Page<MemoryDTO> findAllWithUserTagedIn(final Long depid, final Long userInDepId, final Pageable pageable) {
        final var t = this.userPerDepartmentService.getCurrentUserInDep(depid);
        final var cupd = this.userPerDepartmentRepository.findById(t.getId()).get();

        final var upd = this.userPerDepartmentRepository.findById(depid).get();
        return this.memoryRepository.findAllWithUserTagedIn(depid, cupd, upd, pageable).map(this.memoryMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<MemoryDTO> findOne(Long id) {
        log.debug("Request to get Memory : {}", id);
        return memoryRepository.findOneWithEagerRelationships(id).map(memoryMapper::toDto);
    }

    public void delete(Long id) {
        log.debug("Request to delete Memory : {}", id);
        memoryRepository.deleteById(id);
    }

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private PictureMapper pictureMapper;

    @Autowired
    private CommentService commentService;

    public Page<CommentDTO> findAllForMemory(Long memId, Pageable pageable) {
        var commentId = this.findOne(memId).get().getBaseComment().getId();
        return this.commentService.findAllForChildrenComments(commentId, pageable);
    }
}
