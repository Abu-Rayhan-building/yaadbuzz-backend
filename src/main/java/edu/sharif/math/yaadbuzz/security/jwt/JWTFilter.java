package edu.sharif.math.yaadbuzz.security.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Filters incoming requests and installs a Spring Security principal if a
 * header corresponding to a valid user is found.
 */
public class JWTFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final TokenProvider tokenProvider;

    public JWTFilter(TokenProvider tokenProvider) {
	this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
	    ServletResponse servletResponse, FilterChain filterChain)
	    throws IOException, ServletException {
	HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
	String jwt = resolveToken(httpServletRequest);
	if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
	    Authentication authentication = this.tokenProvider
		    .getAuthentication(jwt);
	    SecurityContextHolder.getContext()
		    .setAuthentication(authentication);
	}
	filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
	String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
	if (StringUtils.hasText(bearerToken)
		&& bearerToken.startsWith("Bearer ")) {
	    return bearerToken.substring(7);
	}
	try {
	    String token = "";

	    // get token from a Cookie
	    Cookie[] cookies = request.getCookies();

	    if (cookies == null || cookies.length < 1) {
		throw new AuthenticationServiceException("Invalid Token");
	    }

	    Cookie sessionCookie = null;
	    for (Cookie cookie : cookies) {
		if (("someSessionId").equals(cookie.getName())) {
		    sessionCookie = cookie;
		    break;
		}
	    }
	    if (sessionCookie == null
		    || StringUtils.isEmpty(sessionCookie.getValue())) {
		throw new AuthenticationServiceException("Invalid Token");
	    }

	    return sessionCookie.getValue();

	} catch (Exception e) {
	    // TODO: handle exception
	}
	return null;
    }

}
