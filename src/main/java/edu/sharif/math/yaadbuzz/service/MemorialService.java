package edu.sharif.math.yaadbuzz.service;

import edu.sharif.math.yaadbuzz.domain.Memorial;
import edu.sharif.math.yaadbuzz.repository.MemorialRepository;
import edu.sharif.math.yaadbuzz.service.*;
import edu.sharif.math.yaadbuzz.service.dto.MemorialDTO;
import edu.sharif.math.yaadbuzz.service.mapper.MemorialMapper;
import edu.sharif.math.yaadbuzz.web.rest.dto.MemorialUDTO;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Memorial}.
 */
@Service
@Transactional
public class MemorialService implements ServiceWithCurrentUserCrudAccess {

    private final Logger log = LoggerFactory.getLogger(MemorialService.class);

    private final MemorialRepository memorialRepository;

    private final MemoryService memoryService;

    private final CommentService commentService;

    private final MemorialMapper memorialMapper;

    public MemorialService(
        MemorialRepository memorialRepository,
        MemorialMapper memorialMapper,
        UserPerDepartmentService userPerDepartmentService,
        DepartmentService departmentService,
        MemoryService memoryService,
        CommentService commentService
    ) {
        this.memorialRepository = memorialRepository;
        this.memoryService = memoryService;
        this.commentService = commentService;
        this.memorialMapper = memorialMapper;
        this.userPerDepartmentService = userPerDepartmentService;
        this.departmentService = departmentService;
    }

    public MemorialDTO save(MemorialDTO memorialDTO) {
        log.debug("Request to save Memorial : {}", memorialDTO);
        Memorial memorial = memorialMapper.toEntity(memorialDTO);
        memorial = memorialRepository.save(memorial);
        return memorialMapper.toDto(memorial);
    }

    public Optional<MemorialDTO> partialUpdate(MemorialDTO memorialDTO) {
        log.debug("Request to partially update Memorial : {}", memorialDTO);

        return memorialRepository
            .findById(memorialDTO.getId())
            .map(
                existingMemorial -> {
                    return existingMemorial;
                }
            )
            .map(memorialRepository::save)
            .map(memorialMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<MemorialDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Memorials");
        return memorialRepository.findAll(pageable).map(memorialMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<MemorialDTO> findOne(Long id) {
        log.debug("Request to get Memorial : {}", id);
        return memorialRepository.findById(id).map(memorialMapper::toDto);
    }

    public void delete(Long id) {
        log.debug("Request to delete Memorial : {}", id);
        memorialRepository.deleteById(id);
    }

    private final UserPerDepartmentService userPerDepartmentService;

    private final DepartmentService departmentService;

    @Override
    public boolean currentuserHasUpdateAccess(Long id) {
        final var memorial = this.memorialRepository.getOne(id);

        final var currentUserId = this.userPerDepartmentService.getCurrentUserInDep(memorial.getDepartment().getId());
        return memorial.getWriter().getId().equals(currentUserId);
    }

    @Override
    public boolean currentuserHasGetAccess(Long id) {
        final var memorial = this.memorialRepository.getOne(id);
        return this.departmentService.currentuserHasGetAccess(memorial.getDepartment().getId());
    }

    public MemorialDTO create(Long depId, MemorialUDTO memorialUDTO) {
        var memeorialDTO = memorialUDTO.build();
        if (memorialUDTO.getAnonymousComment() != null) {
            var an = this.commentService.save(memorialUDTO.getAnonymousComment().build());
            memeorialDTO.setAnonymousComment(an);
        }
        if (memorialUDTO.getNotAnonymousComment() != null) {
            var notAn = this.commentService.save(memorialUDTO.getNotAnonymousComment().build());
            memeorialDTO.setNotAnonymousComment(notAn);
        }

        memeorialDTO.setWriter(this.userPerDepartmentService.getCurrentUserUserPerDepeartmentInDep(depId));

        return this.save(memeorialDTO);
    }
}
