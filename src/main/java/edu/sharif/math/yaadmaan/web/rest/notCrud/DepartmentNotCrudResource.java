package edu.sharif.math.yaadmaan.web.rest.notCrud;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.sharif.math.yaadmaan.domain.User;
import edu.sharif.math.yaadmaan.repository.UserRepository;
import edu.sharif.math.yaadmaan.service.DepartmentService;
import edu.sharif.math.yaadmaan.service.MailService;
import edu.sharif.math.yaadmaan.service.UserService;
import edu.sharif.math.yaadmaan.service.dto.DepartmentDTO;
import edu.sharif.math.yaadmaan.service.dto.UserPerDepartmentDTO;
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

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final UserRepository userRepository;

    private final MailService mailService;

    private final DepartmentService departmentService;

    public DepartmentNotCrudResource(final DepartmentService departmentService,
	    final UserService userService, final UserRepository userRepository,
	    final MailService mailService) {
	this.userService = userService;
	this.userRepository = userRepository;
	this.mailService = mailService;
	this.departmentService = departmentService;
    }

    @GetMapping("/departments/me")
    public ResponseEntity<List<DepartmentDTO>> getMyDeps(
	    final Pageable pageable) {
	final var d = this.departmentService.getMyDeps();
	return new ResponseEntity<>(d, HttpStatus.OK);
    }

    @PostMapping("departments/{departmentId}/join")
    public ResponseEntity<DepartmentDTO> join(
	    @PathVariable final Long departmentId,
	    @RequestBody final String password,
	    @RequestBody final UserPerDepartmentDTO userPerDepartmentDTO) {
	userPerDepartmentDTO.setDepartmentId(departmentId);
	final Optional<User> isUser = this.userService.getUserWithAuthorities();
	userPerDepartmentDTO.setRealUserId(isUser.get().getId());
	final DepartmentDTO departmentDTO = this.departmentService
		.join(departmentId, password, userPerDepartmentDTO);
	// fuck2
	return ResponseUtil.wrapOrNotFound(Optional.of(departmentDTO));
    }

}
