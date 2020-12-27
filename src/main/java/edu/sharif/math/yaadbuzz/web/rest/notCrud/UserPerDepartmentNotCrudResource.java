package edu.sharif.math.yaadbuzz.web.rest.notCrud;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.sharif.math.yaadbuzz.domain.User;
import edu.sharif.math.yaadbuzz.repository.UserRepository;
import edu.sharif.math.yaadbuzz.service.DepartmentService;
import edu.sharif.math.yaadbuzz.service.MailService;
import edu.sharif.math.yaadbuzz.service.UserPerDepartmentService;
import edu.sharif.math.yaadbuzz.service.UserService;
import edu.sharif.math.yaadbuzz.service.dto.DepartmentDTO;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;
import edu.sharif.math.yaadbuzz.web.rest.dto.UserPerDepartmentUDTO;
import edu.sharif.math.yaadbuzz.web.rest.dto.UserPerDepartmentWithIdUDTO;
import edu.sharif.math.yaadbuzz.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;

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
@RequestMapping("/api/department/{depId}")
public class UserPerDepartmentNotCrudResource {
    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections
	    .unmodifiableList(Arrays.asList("id", "login", "firstName",
		    "lastName", "email", "activated", "langKey"));

    private static final String ENTITY_NAME = "userPerDepartment";

    private final Logger log = LoggerFactory
	    .getLogger(UserPerDepartmentNotCrudResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartmentService departmentService;

    private final UserPerDepartmentService userPerDepartmentService;

    public UserPerDepartmentNotCrudResource(
	    final DepartmentService departmentService,
	    final UserService userService, final UserRepository userRepository,
	    final MailService mailService,
	    final UserPerDepartmentService userPerDepartmentService) {
	this.departmentService = departmentService;
	this.userPerDepartmentService = userPerDepartmentService;
    }

    @GetMapping("/user-per-department")
    public ResponseEntity<List<UserPerDepartmentDTO>> getUsersInDep(
	    final Pageable pageable, @PathVariable final Long depId) {

	Page<UserPerDepartmentDTO> page;
	page = this.departmentService.getDepartmentUsers(depId, pageable);
	final HttpHeaders headers = PaginationUtil
		.generatePaginationHttpHeaders(
			ServletUriComponentsBuilder.fromCurrentRequest(), page);
	return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code PUT  /user-per-departments} : Updates an existing
     * userPerDepartment.
     *
     * @param userPerDepartmentDTO the userPerDepartmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the updated userPerDepartmentDTO, or with status
     *         {@code 400 (Bad Request)} if the userPerDepartmentDTO is not
     *         valid, or with status {@code 500 (Internal Server Error)} if the
     *         userPerDepartmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-per-department")
    public ResponseEntity<UserPerDepartmentDTO> updateUserPerDepartment(
	    @Valid @RequestBody final UserPerDepartmentWithIdUDTO departmentWithIdUDTO)
	    throws URISyntaxException {
	this.log.debug("REST request to update UserPerDepartment : {}",
		departmentWithIdUDTO);
	if (departmentWithIdUDTO.getId() == null) {
	    throw new BadRequestAlertException("Invalid id",
		    UserPerDepartmentNotCrudResource.ENTITY_NAME, "idnull");
	}
	if (!this.userPerDepartmentService
		.currentuserHasUpdateAccess(departmentWithIdUDTO.getId())) {
	    throw new AccessDeniedException("cant update upd");
	}
	var old = this.userPerDepartmentService.findOne(departmentWithIdUDTO.getId()).get();
	var newUPD = departmentWithIdUDTO.build();
	newUPD.setDepartment(old.getDepartment());
	newUPD.setRealUser(old.getRealUser());
	
	final UserPerDepartmentDTO result = this.userPerDepartmentService
		.save(newUPD);
	return ResponseEntity.ok()
		.headers(HeaderUtil.createEntityUpdateAlert(
			this.applicationName, true,
			UserPerDepartmentNotCrudResource.ENTITY_NAME,
			newUPD.getId().toString()))
		.body(result);
    }
    


}
