package edu.sharif.math.yaadmaan.web.rest;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.sharif.math.yaadmaan.config.Constants;
import edu.sharif.math.yaadmaan.domain.User;
import edu.sharif.math.yaadmaan.repository.UserRepository;
import edu.sharif.math.yaadmaan.security.AuthoritiesConstants;
import edu.sharif.math.yaadmaan.service.MailService;
import edu.sharif.math.yaadmaan.service.UserService;
import edu.sharif.math.yaadmaan.service.dto.UserDTO;
import edu.sharif.math.yaadmaan.web.rest.errors.BadRequestAlertException;
import edu.sharif.math.yaadmaan.web.rest.errors.EmailAlreadyUsedException;
import edu.sharif.math.yaadmaan.web.rest.errors.LoginAlreadyUsedException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
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
public class UserResource {
    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections
	    .unmodifiableList(Arrays.asList("id", "login", "firstName",
		    "lastName", "email", "activated", "langKey"));

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final UserRepository userRepository;

    private final MailService mailService;

    public UserResource(final UserService userService,
	    final UserRepository userRepository,
	    final MailService mailService) {
	this.userService = userService;
	this.userRepository = userRepository;
	this.mailService = mailService;
    }

    /**
     * {@code POST  /users} : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends
     * an mail with an activation link. The user needs to be activated on
     * creation.
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and
     *         with body the new user, or with status {@code 400 (Bad Request)}
     *         if the login or email is already in use.
     * @throws URISyntaxException       if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login
     *                                  or email is already in use.
     */
    @PostMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<User> createUser(
	    @Valid @RequestBody final UserDTO userDTO)
	    throws URISyntaxException {
	this.log.debug("REST request to save User : {}", userDTO);

	if (userDTO.getId() != null) {
	    throw new BadRequestAlertException(
		    "A new user cannot already have an ID", "userManagement",
		    "idexists");
	    // Lowercase the user login before comparing with database
	} else if (this.userRepository
		.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
	    throw new LoginAlreadyUsedException();
	} else if (this.userRepository
		.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
	    throw new EmailAlreadyUsedException();
	} else {
	    final User newUser = this.userService.createUser(userDTO);
	    this.mailService.sendCreationEmail(newUser);
	    return ResponseEntity
		    .created(new URI("/api/users/" + newUser.getLogin()))
		    .headers(HeaderUtil.createAlert(this.applicationName,
			    "userManagement.created", newUser.getLogin()))
		    .body(newUser);
	}
    }

    /**
     * {@code DELETE /users/:login} : delete the "login" User.
     *
     * @param login the login of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteUser(@PathVariable final String login) {
	this.log.debug("REST request to delete User: {}", login);
	this.userService.deleteUser(login);
	return ResponseEntity.noContent()
		.headers(HeaderUtil.createAlert(this.applicationName,
			"userManagement.deleted", login))
		.build();
    }

    /**
     * {@code GET /users} : get all users.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body all users.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(final Pageable pageable) {
	if (!this.onlyContainsAllowedProperties(pageable)) {
	    return ResponseEntity.badRequest().build();
	}

	final Page<UserDTO> page = this.userService
		.getAllManagedUsers(pageable);
	final HttpHeaders headers = PaginationUtil
		.generatePaginationHttpHeaders(
			ServletUriComponentsBuilder.fromCurrentRequest(), page);
	return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * Gets a list of all roles.
     *
     * @return a string list of all roles.
     */
    @GetMapping("/users/authorities")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<String> getAuthorities() {
	return this.userService.getAuthorities();
    }

    /**
     * {@code GET /users/:login} : get the "login" user.
     *
     * @param login the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the "login" user, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<UserDTO> getUser(@PathVariable final String login) {
	this.log.debug("REST request to get User : {}", login);
	return ResponseUtil.wrapOrNotFound(this.userService
		.getUserWithAuthoritiesByLogin(login).map(UserDTO::new));
    }

    private boolean onlyContainsAllowedProperties(final Pageable pageable) {
	return pageable.getSort().stream().map(Sort.Order::getProperty)
		.allMatch(UserResource.ALLOWED_ORDERED_PROPERTIES::contains);
    }

    /**
     * {@code PUT /users} : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the updated user.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email
     *                                   is already in use.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login
     *                                   is already in use.
     */
    @PutMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserDTO> updateUser(
	    @Valid @RequestBody final UserDTO userDTO) {
	this.log.debug("REST request to update User : {}", userDTO);
	Optional<User> existingUser = this.userRepository
		.findOneByEmailIgnoreCase(userDTO.getEmail());
	if (existingUser.isPresent()
		&& !existingUser.get().getId().equals(userDTO.getId())) {
	    throw new EmailAlreadyUsedException();
	}
	existingUser = this.userRepository
		.findOneByLogin(userDTO.getLogin().toLowerCase());
	if (existingUser.isPresent()
		&& !existingUser.get().getId().equals(userDTO.getId())) {
	    throw new LoginAlreadyUsedException();
	}
	final Optional<UserDTO> updatedUser = this.userService
		.updateUser(userDTO);

	return ResponseUtil.wrapOrNotFound(updatedUser,
		HeaderUtil.createAlert(this.applicationName,
			"userManagement.updated", userDTO.getLogin()));
    }
}
