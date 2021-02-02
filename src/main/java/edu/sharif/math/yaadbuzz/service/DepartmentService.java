package edu.sharif.math.yaadbuzz.service;

import edu.sharif.math.yaadbuzz.domain.Department;
import edu.sharif.math.yaadbuzz.domain.UserPerDepartment;
import edu.sharif.math.yaadbuzz.repository.DepartmentRepository;
import edu.sharif.math.yaadbuzz.repository.PictureRepository;
import edu.sharif.math.yaadbuzz.repository.UserPerDepartmentRepository;
import edu.sharif.math.yaadbuzz.service.*;
import edu.sharif.math.yaadbuzz.service.dto.DepartmentDTO;
import edu.sharif.math.yaadbuzz.service.dto.PictureDTO;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;
import edu.sharif.math.yaadbuzz.service.mapper.DepartmentMapper;
import edu.sharif.math.yaadbuzz.service.mapper.PictureMapper;
import edu.sharif.math.yaadbuzz.service.mapper.UserPerDepartmentMapper;
import edu.sharif.math.yaadbuzz.web.rest.dto.MyUserPerDepartmentStatsDTO;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Department}.
 */
@Service
@Transactional
public class DepartmentService implements ServiceWithCurrentUserCrudAccess {

    private final Logger log = LoggerFactory.getLogger(DepartmentService.class);

    @Autowired
    private UserPerDepartmentRepository userPerDepartmentRepository;

    private UserPerDepartmentService userPerDepartmentService;

    private final DepartmentRepository departmentRepository;

    private final DepartmentMapper departmentMapper;

    @Autowired
    private UserPerDepartmentMapper userPerDepartmentMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private PictureMapper pictureMapper;

    @Autowired
    private UserExtraService userExtraService;

    public DepartmentService(final DepartmentRepository departmentRepository, final DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public boolean currentuserHasGetAccess(final Long id) {
        final var currentUserId = this.userService.getCurrentUserId();
        final var dep = this.departmentRepository.findById(id).get();
        return dep.getUserPerDepartments().parallelStream().anyMatch(upd -> upd.getRealUser().getId().equals(currentUserId));
    }

    @Override
    public boolean currentuserHasUpdateAccess(final Long id) {
        final var currentUserId = this.userService.getCurrentUserId();
        return this.departmentRepository.findById(id).get().getOwner().getId().equals(currentUserId);
    }

    public void delete(final Long id) {
        this.log.debug("Request to delete Department : {}", id);
        this.departmentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<DepartmentDTO> findAll(final Pageable pageable) {
        this.log.debug("Request to get all Departments");
        return this.departmentRepository.findAll(pageable).map(this.departmentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<DepartmentDTO> findOne(final Long id) {
        this.log.debug("Request to get Department : {}", id);
        return this.departmentRepository.findById(id).map(this.departmentMapper::toDto);
    }

    public List<UserPerDepartmentDTO> getAllDepartmentUsers(final Long depid) {
        return this.userPerDepartmentRepository.findByDepatment(depid)
            .stream()
            .map(this.userPerDepartmentMapper::toDto)
            .collect(Collectors.toList());
    }

    public Optional<PictureDTO> getDepartmentPicture(final Long depId) {
        var pid = this.departmentService.findOne(depId).get().getAvatar();
        if (pid != null) {
            final var picture = this.pictureRepository.getOne(this.departmentService.findOne(depId).get().getAvatar().getId());
            return Optional.of(this.pictureMapper.toDto(picture));
        }
        return Optional.empty();
    }

    public List<DepartmentDTO> getMyDeps() {
        return this.userPerDepartmentRepository.findByRealUserIsCurrentUser()
            .stream()
            .map(UserPerDepartment::getDepartment)
            .map(this.departmentMapper::toDto)
            .collect(Collectors.toList());
    }

    public MyUserPerDepartmentStatsDTO getMyStatsInDep(final Long depId) {
        return this.userPerDepartmentService.getCurrentUserStatsInDep(depId);
    }

    public DepartmentDTO join(final Long departmentId, final String password, UserPerDepartmentDTO u) {
        if (!this.findOne(departmentId).isPresent()) {
            throw new EntityNotFoundException("department not found");
        }
        final var dep = this.findOne(departmentId).get();
        if (!dep.getPassword().equals(password)) {
            throw new InvalidParameterException("department password is wrong");
        }
        this.userPerDepartmentService.save(u);
        this.userPerDepartmentService.updateDefaultUPDAfterJoin(u);
        return this.findOne(departmentId).get();
    }

    public Optional<DepartmentDTO> partialUpdate(final DepartmentDTO departmentDTO) {
        this.log.debug("Request to partially update Department : {}", departmentDTO);

        return this.departmentRepository.findById(departmentDTO.getId())
            .map(
                existingDepartment -> {
                    if (departmentDTO.getName() != null) {
                        existingDepartment.setName(departmentDTO.getName());
                    }

                    if (departmentDTO.getPassword() != null) {
                        existingDepartment.setPassword(departmentDTO.getPassword());
                    }

                    return existingDepartment;
                }
            )
            .map(this.departmentRepository::save)
            .map(this.departmentMapper::toDto);
    }

    public DepartmentDTO save(final DepartmentDTO departmentDTO) {
        this.log.debug("Request to save Department : {}", departmentDTO);
        Department department = this.departmentMapper.toEntity(departmentDTO);
        department = this.departmentRepository.save(department);
        return this.departmentMapper.toDto(department);
    }

    @Autowired
    public void setUserPerDepartmentService(final UserPerDepartmentService userPerDepartmentService) {
        this.userPerDepartmentService = userPerDepartmentService;
    }

    private final Long defaultDepId = Long.valueOf(1);

    public Long getDefaultDepId() {
        return this.defaultDepId;
    }
}
