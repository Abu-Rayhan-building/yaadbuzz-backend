package edu.sharif.math.yaadmaan.web.rest;

import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.sharif.math.yaadmaan.domain.User;
import edu.sharif.math.yaadmaan.repository.UserRepository;
import edu.sharif.math.yaadmaan.security.AuthoritiesConstants;
import edu.sharif.math.yaadmaan.security.SecurityUtils;
import edu.sharif.math.yaadmaan.service.MailService;
import edu.sharif.math.yaadmaan.service.UserService;
import edu.sharif.math.yaadmaan.service.dto.PasswordChangeDTO;
import edu.sharif.math.yaadmaan.service.dto.UserDTO;
import edu.sharif.math.yaadmaan.web.rest.errors.EmailAlreadyUsedException;
import edu.sharif.math.yaadmaan.web.rest.errors.InvalidPasswordException;
import edu.sharif.math.yaadmaan.web.rest.errors.LoginAlreadyUsedException;
import edu.sharif.math.yaadmaan.web.rest.vm.KeyAndPasswordVM;
import edu.sharif.math.yaadmaan.web.rest.vm.ManagedUserVM;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private AccountResourceException(final String message) {
	    super(message);
	}
    }

    private static boolean checkPasswordLength(final String password) {
	return !StringUtils.isEmpty(password)
		&& password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH
		&& password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    public AccountResource(final UserRepository userRepository,
	    final UserService userService, final MailService mailService) {

	this.userRepository = userRepository;
	this.userService = userService;
	this.mailService = mailService;
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user
     *                          couldn't be activated.
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") final String key) {
	final Optional<User> user = this.userService.activateRegistration(key);
	if (!user.isPresent()) {
	    throw new AccountResourceException(
		    "No user was found for this activation key");
	}
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's
     * password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new
     *                                  password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(
	    @RequestBody final PasswordChangeDTO passwordChangeDto) {
	if (!AccountResource
		.checkPasswordLength(passwordChangeDto.getNewPassword())) {
	    throw new InvalidPasswordException();
	}
	this.userService.changePassword(passwordChangeDto.getCurrentPassword(),
		passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the
     * password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the
     *                                  password is incorrect.
     * @throws RuntimeException         {@code 500 (Internal Server Error)} if
     *                                  the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    public void finishPasswordReset(
	    @RequestBody final KeyAndPasswordVM keyAndPassword) {
	if (!AccountResource
		.checkPasswordLength(keyAndPassword.getNewPassword())) {
	    throw new InvalidPasswordException();
	}
	final Optional<User> user = this.userService.completePasswordReset(
		keyAndPassword.getNewPassword(), keyAndPassword.getKey());

	if (!user.isPresent()) {
	    throw new AccountResourceException(
		    "No user was found for this reset key");
	}
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user
     *                          couldn't be returned.
     */
    @GetMapping("/account")
    public UserDTO getAccount() {
	return this.userService.getUserWithAuthorities().map(UserDTO::new)
		.orElseThrow(() -> new AccountResourceException(
			"User could not be found"));
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and
     * return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @PermitAll
    @GetMapping("/authenticate")
    public String isAuthenticated(final HttpServletRequest request) {
	this.log.debug(
		"REST request to check if the current user is authenticated");
	return request.getRemoteUser();
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException  {@code 400 (Bad Request)} if the
     *                                   password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email
     *                                   is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login
     *                                   is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(
	    @Valid @RequestBody final ManagedUserVM managedUserVM) {
	if (!AccountResource.checkPasswordLength(managedUserVM.getPassword())) {
	    throw new InvalidPasswordException();
	}
	final User user = this.userService.registerUser(managedUserVM,
		managedUserVM.getPassword());
	this.mailService.sendActivationEmail(user);
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the
     * password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody final String mail) {
	final Optional<User> user = this.userService.requestPasswordReset(mail);
	if (user.isPresent()) {
	    this.mailService.sendPasswordResetMail(user.get());
	} else {
	    // Pretend the request has been successful to prevent checking which
	    // emails really exist
	    // but log that an invalid attempt has been made
	    this.log.warn("Password reset requested for non existing mail");
	}
    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email
     *                                   is already used.
     * @throws RuntimeException          {@code 500 (Internal Server Error)} if
     *                                   the user login wasn't found.
     */
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody final UserDTO userDTO) {
	final String userLogin = SecurityUtils.getCurrentUserLogin()
		.orElseThrow(() -> new AccountResourceException(
			"Current user login not found"));
	final Optional<User> existingUser = this.userRepository
		.findOneByEmailIgnoreCase(userDTO.getEmail());
	if (existingUser.isPresent()
		&& !existingUser.get().getLogin().equalsIgnoreCase(userLogin)) {
	    throw new EmailAlreadyUsedException();
	}
	final Optional<User> user = this.userRepository
		.findOneByLogin(userLogin);
	if (!user.isPresent()) {
	    throw new AccountResourceException("User could not be found");
	}
	this.userService.updateUser(userDTO.getFirstName(),
		userDTO.getLastName(), userDTO.getEmail(), userDTO.getLangKey(),
		userDTO.getImageUrl());
    }
}
