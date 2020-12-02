package edu.sharif.math.yaadmaan.security.jwt;

import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import edu.sharif.math.yaadmaan.security.AuthoritiesConstants;
import io.github.jhipster.config.JHipsterProperties;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class JWTFilterTest {

    private TokenProvider tokenProvider;

    private JWTFilter jwtFilter;

    @BeforeEach
    public void setup() {
	final JHipsterProperties jHipsterProperties = new JHipsterProperties();
	this.tokenProvider = new TokenProvider(jHipsterProperties);
	ReflectionTestUtils.setField(this.tokenProvider, "key",
		Keys.hmacShaKeyFor(Decoders.BASE64.decode(
			"fd54a45s65fds737b9aafcb3412e07ed99b267f33413274720ddbb7f6c5e64e9f14075f2d7ed041592f0b7657baf8")));

	ReflectionTestUtils.setField(this.tokenProvider,
		"tokenValidityInMilliseconds", 60000);
	this.jwtFilter = new JWTFilter(this.tokenProvider);
	SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    public void testJWTFilter() throws Exception {
	final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
		"test-user", "test-password", Collections.singletonList(
			new SimpleGrantedAuthority(AuthoritiesConstants.USER)));
	final String jwt = this.tokenProvider.createToken(authentication,
		false);
	final MockHttpServletRequest request = new MockHttpServletRequest();
	request.addHeader(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
	request.setRequestURI("/api/test");
	final MockHttpServletResponse response = new MockHttpServletResponse();
	final MockFilterChain filterChain = new MockFilterChain();
	this.jwtFilter.doFilter(request, response, filterChain);
	Assertions.assertThat(response.getStatus())
		.isEqualTo(HttpStatus.OK.value());
	Assertions.assertThat(SecurityContextHolder.getContext()
		.getAuthentication().getName()).isEqualTo("test-user");
	Assertions
		.assertThat(SecurityContextHolder.getContext()
			.getAuthentication().getCredentials().toString())
		.isEqualTo(jwt);
    }

    @Test
    public void testJWTFilterInvalidToken() throws Exception {
	final String jwt = "wrong_jwt";
	final MockHttpServletRequest request = new MockHttpServletRequest();
	request.addHeader(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
	request.setRequestURI("/api/test");
	final MockHttpServletResponse response = new MockHttpServletResponse();
	final MockFilterChain filterChain = new MockFilterChain();
	this.jwtFilter.doFilter(request, response, filterChain);
	Assertions.assertThat(response.getStatus())
		.isEqualTo(HttpStatus.OK.value());
	Assertions
		.assertThat(
			SecurityContextHolder.getContext().getAuthentication())
		.isNull();
    }

    @Test
    public void testJWTFilterMissingAuthorization() throws Exception {
	final MockHttpServletRequest request = new MockHttpServletRequest();
	request.setRequestURI("/api/test");
	final MockHttpServletResponse response = new MockHttpServletResponse();
	final MockFilterChain filterChain = new MockFilterChain();
	this.jwtFilter.doFilter(request, response, filterChain);
	Assertions.assertThat(response.getStatus())
		.isEqualTo(HttpStatus.OK.value());
	Assertions
		.assertThat(
			SecurityContextHolder.getContext().getAuthentication())
		.isNull();
    }

    @Test
    public void testJWTFilterMissingToken() throws Exception {
	final MockHttpServletRequest request = new MockHttpServletRequest();
	request.addHeader(JWTFilter.AUTHORIZATION_HEADER, "Bearer ");
	request.setRequestURI("/api/test");
	final MockHttpServletResponse response = new MockHttpServletResponse();
	final MockFilterChain filterChain = new MockFilterChain();
	this.jwtFilter.doFilter(request, response, filterChain);
	Assertions.assertThat(response.getStatus())
		.isEqualTo(HttpStatus.OK.value());
	Assertions
		.assertThat(
			SecurityContextHolder.getContext().getAuthentication())
		.isNull();
    }

    @Test
    public void testJWTFilterWrongScheme() throws Exception {
	final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
		"test-user", "test-password", Collections.singletonList(
			new SimpleGrantedAuthority(AuthoritiesConstants.USER)));
	final String jwt = this.tokenProvider.createToken(authentication,
		false);
	final MockHttpServletRequest request = new MockHttpServletRequest();
	request.addHeader(JWTFilter.AUTHORIZATION_HEADER, "Basic " + jwt);
	request.setRequestURI("/api/test");
	final MockHttpServletResponse response = new MockHttpServletResponse();
	final MockFilterChain filterChain = new MockFilterChain();
	this.jwtFilter.doFilter(request, response, filterChain);
	Assertions.assertThat(response.getStatus())
		.isEqualTo(HttpStatus.OK.value());
	Assertions
		.assertThat(
			SecurityContextHolder.getContext().getAuthentication())
		.isNull();
    }

}
