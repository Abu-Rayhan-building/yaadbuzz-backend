package edu.sharif.math.yaadmaan.web.rest.notCrud;

import org.springframework.data.domain.Sort;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

import org.hibernate.service.spi.InjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.sharif.math.yaadmaan.config.Constants;
import edu.sharif.math.yaadmaan.domain.Department;
import edu.sharif.math.yaadmaan.domain.User;
import edu.sharif.math.yaadmaan.repository.UserRepository;
import edu.sharif.math.yaadmaan.security.AuthoritiesConstants;
import edu.sharif.math.yaadmaan.security.SecurityUtils;
import edu.sharif.math.yaadmaan.service.DepartmentService;
import edu.sharif.math.yaadmaan.service.MailService;
import edu.sharif.math.yaadmaan.service.UserService;
import edu.sharif.math.yaadmaan.service.dto.DepartmentDTO;
import edu.sharif.math.yaadmaan.service.dto.MemoryDTO;
import edu.sharif.math.yaadmaan.service.dto.UserDTO;
import edu.sharif.math.yaadmaan.service.dto.UserPerDepartmentDTO;
import edu.sharif.math.yaadmaan.web.rest.errors.BadRequestAlertException;
import edu.sharif.math.yaadmaan.web.rest.errors.EmailAlreadyUsedException;
import edu.sharif.math.yaadmaan.web.rest.errors.LoginAlreadyUsedException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

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
public class UserPerDepartmentNotCrudResource {
    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections
	    .unmodifiableList(Arrays.asList("id", "login", "firstName",
		    "lastName", "email", "activated", "langKey"));

    private final Logger log = LoggerFactory
	    .getLogger(UserPerDepartmentNotCrudResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final UserRepository userRepository;

    private final MailService mailService;

    public UserPerDepartmentNotCrudResource(DepartmentService departmentService,
	    UserService userService, UserRepository userRepository,
	    MailService mailService) {
	this.userService = userService;
	this.userRepository = userRepository;
	this.mailService = mailService;
	this.departmentService = departmentService;
    }

    private final DepartmentService departmentService;

    @GetMapping("/department/{id}/users")
    public ResponseEntity<List<UserPerDepartmentDTO>> getUsersInDep(  final Pageable pageable,
	    @PathVariable Long id) {
	
	Page<UserPerDepartmentDTO> page;
	page = this.departmentService.getDepartmentUsers(id, pageable);
	final HttpHeaders headers = PaginationUtil
		.generatePaginationHttpHeaders(
			ServletUriComponentsBuilder.fromCurrentRequest(), page);
	return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


}
