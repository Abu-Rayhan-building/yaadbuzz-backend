package edu.sharif.math.yaadmaan.web.rest;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.sharif.math.yaadmaan.security.jwt.JWTFilter;
import edu.sharif.math.yaadmaan.security.jwt.TokenProvider;
import edu.sharif.math.yaadmaan.web.rest.vm.LoginVM;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

	private String idToken;

	JWTToken(final String idToken) {
	    this.idToken = idToken;
	}

	@JsonProperty("id_token")
	String getIdToken() {
	    return this.idToken;
	}

	void setIdToken(final String idToken) {
	    this.idToken = idToken;
	}
    }

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserJWTController(final TokenProvider tokenProvider,
	    final AuthenticationManagerBuilder authenticationManagerBuilder) {
	this.tokenProvider = tokenProvider;
	this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(
	    @Valid @RequestBody final LoginVM loginVM) {

	final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
		loginVM.getUsername(), loginVM.getPassword());

	final Authentication authentication = this.authenticationManagerBuilder
		.getObject().authenticate(authenticationToken);
	SecurityContextHolder.getContext().setAuthentication(authentication);
	final boolean rememberMe = loginVM.isRememberMe() == null ? false
		: loginVM.isRememberMe();
	final String jwt = this.tokenProvider.createToken(authentication,
		rememberMe);
	final HttpHeaders httpHeaders = new HttpHeaders();
	httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
	return new ResponseEntity<>(new JWTToken(jwt), httpHeaders,
		HttpStatus.OK);
    }
}
