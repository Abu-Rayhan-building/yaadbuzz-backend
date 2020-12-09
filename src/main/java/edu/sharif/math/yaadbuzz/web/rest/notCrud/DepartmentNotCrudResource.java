package edu.sharif.math.yaadbuzz.web.rest.notCrud;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.sharif.math.yaadbuzz.domain.User;
import edu.sharif.math.yaadbuzz.repository.DepartmentRepository;
import edu.sharif.math.yaadbuzz.service.DepartmentService;
import edu.sharif.math.yaadbuzz.service.MailService;
import edu.sharif.math.yaadbuzz.service.UserPerDepartmentService;
import edu.sharif.math.yaadbuzz.service.UserService;
import edu.sharif.math.yaadbuzz.service.dto.DepartmentDTO;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;
import edu.sharif.math.yaadbuzz.service.dto.helpers.MyUserPerDepartmentStatsDTO;
import edu.sharif.math.yaadbuzz.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link User} entity, and needs to fetch its
 * collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship
 * between User and Authority, and send everything to the client side: there
 * would be no View Model and DTO, a lot less code, and an outer-join which
 * would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities,
 * because people will quite often do relationships with the user, and we don't
 * want them to get the authorities all the time for nothing (for performance
 * reasons). This is the #1 goal: we should not impact our users' application
 * because of this use-case.</li>
 * <li>Not having an outer join causes n+1 requests to the database. This is not
 * a real issue as we have by default a second-level cache. This means on the
 * first HTTP call we do the n+1 requests, but then all authorities come from
 * the cache, so in fact it's much better than doing an outer join (which will
 * get lots of data from the database, for each HTTP call).</li>
 * <li>As this manages users, for security reasons, we'd rather have a DTO
 * layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this
 * case.
 */
@RestController
@RequestMapping("/api")
public class DepartmentNotCrudResource {
    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections
	    .unmodifiableList(Arrays.asList("id", "login", "firstName",
		    "lastName", "email", "activated", "langKey"));

    private final Logger log = LoggerFactory
	    .getLogger(DepartmentNotCrudResource.class);

    private static final String ENTITY_NAME = "department";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final DepartmentRepository departmentRepository;

    private final DepartmentService departmentService;

    private final UserPerDepartmentService userPerDepartmentService;

    public DepartmentNotCrudResource(final DepartmentService departmentService,
	    final UserService userService, final MailService mailService,
	    UserPerDepartmentService userPerDepartmentService,
	    DepartmentRepository departmentRepository) {
	this.userService = userService;
	this.departmentRepository = departmentRepository;
	this.departmentService = departmentService;
	this.userPerDepartmentService = userPerDepartmentService;
    }

    /**
     * {@code PUT  /departments} : Updates an existing department.
     *
     * @param departmentDTO the departmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the updated departmentDTO, or with status
     *         {@code 400 (Bad Request)} if the departmentDTO is not valid, or
     *         with status {@code 500 (Internal Server Error)} if the
     *         departmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/department/{id}/edit")
    public ResponseEntity<DepartmentDTO> updateDepartment(
	    @Valid @RequestBody DepartmentDTO departmentDTO)
	    throws URISyntaxException {
	log.debug("REST request to update Department : {}", departmentDTO);
	if (departmentDTO.getId() == null) {
	    throw new BadRequestAlertException("Invalid id", ENTITY_NAME,
		    "idnull");
	}

	var oid = departmentRepository.getOne(departmentDTO.getId()).getOwner()
		.getId();
	if (!userService.getCurrentUserId().equals(oid))
	    throw new AccessDeniedException("dd");

	DepartmentDTO result = departmentService.save(departmentDTO);
	return ResponseEntity.ok()
		.headers(HeaderUtil.createEntityUpdateAlert(applicationName,
			true, ENTITY_NAME, departmentDTO.getId().toString()))
		.body(result);
    }

    @GetMapping("/department/me")
    public ResponseEntity<List<DepartmentDTO>> getMyDeps(
	    final Pageable pageable) {
	final var d = this.departmentService.getMyDeps();
	return new ResponseEntity<>(d, HttpStatus.OK);
    }
    
    @GetMapping("department/{departmentId}/my-stats")
    public ResponseEntity<MyUserPerDepartmentStatsDTO> getMyUPDWithStats(
	    @PathVariable final Long departmentId
	    ) {
	final var d = this.departmentService.getMyStatsInDep(departmentId);
	return new ResponseEntity<>(d, HttpStatus.OK);
    }
    @PostMapping("department/{departmentId}/join")
    public ResponseEntity<DepartmentDTO> join(
	    @PathVariable final Long departmentId,
	    @RequestBody final String password,
	    @RequestBody final UserPerDepartmentDTO userPerDepartmentDTO) {
	userPerDepartmentDTO.setDepartmentId(departmentId);
	final Optional<User> isUser = this.userService.getUserWithAuthorities();
	userPerDepartmentDTO.setRealUserId(isUser.get().getId());
	final DepartmentDTO departmentDTO = this.departmentService
		.join(departmentId, password, userPerDepartmentDTO);
	return ResponseUtil.wrapOrNotFound(Optional.of(departmentDTO));
    }

    /**
     * {@code POST  /departments} : Create a new department.
     *
     * @param departmentDTO the departmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and
     *         with body the new departmentDTO, or with status
     *         {@code 400 (Bad Request)} if the department has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/department/create")
    public ResponseEntity<DepartmentWithUserPerDepartmentDTO> createDepartment(
	    @Valid @RequestBody DepartmentWithUserPerDepartmentDTO departmentWithUserPerDepartmentDTO)
	    throws URISyntaxException {
	log.debug("REST request to save Department : {}",
		departmentWithUserPerDepartmentDTO);
	if (departmentWithUserPerDepartmentDTO.getDepartmentDTO()
		.getId() != null
		|| departmentWithUserPerDepartmentDTO.getUserPerDepartmentDTO()
			.getId() != null
		|| departmentWithUserPerDepartmentDTO.getUserPerDepartmentDTO()
			.getDepartmentId() != null) {
	    throw new BadRequestAlertException(
		    "A new department or upd or udp's dep's id cannot already have an ID",
		    ENTITY_NAME, "idexists");
	}
	DepartmentDTO res = departmentService
		.save(departmentWithUserPerDepartmentDTO.getDepartmentDTO());
	departmentWithUserPerDepartmentDTO.getUserPerDepartmentDTO()
		.setDepartmentId(res.getId());
	var res2 = userPerDepartmentService.save(
		departmentWithUserPerDepartmentDTO.getUserPerDepartmentDTO());

	var result = new DepartmentWithUserPerDepartmentDTO();
	result.setDepartmentDTO(res);
	result.setUserPerDepartmentDTO(res2);
	return ResponseEntity
		.created(new URI("/api/departments/"
			+ result.getDepartmentDTO().getId()))
		.headers(HeaderUtil.createEntityCreationAlert(applicationName,
			true, ENTITY_NAME,
			result.getDepartmentDTO().getId().toString()))
		.body(result);
    }

}

class DepartmentWithUserPerDepartmentDTO implements Serializable {
    DepartmentDTO departmentDTO;
    UserPerDepartmentDTO userPerDepartmentDTO;

    public DepartmentDTO getDepartmentDTO() {
	return departmentDTO;
    }

    public void setDepartmentDTO(DepartmentDTO departmentDTO) {
	this.departmentDTO = departmentDTO;
    }

    public UserPerDepartmentDTO getUserPerDepartmentDTO() {
	return userPerDepartmentDTO;
    }

    public void setUserPerDepartmentDTO(
	    UserPerDepartmentDTO userPerDepartmentDTO) {
	this.userPerDepartmentDTO = userPerDepartmentDTO;
    }

}
