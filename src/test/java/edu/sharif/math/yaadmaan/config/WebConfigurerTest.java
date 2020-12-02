package edu.sharif.math.yaadmaan.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.assertj.core.api.Assertions;
import org.h2.server.web.WebServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import io.github.jhipster.config.JHipsterConstants;
import io.github.jhipster.config.JHipsterProperties;

/**
 * Unit tests for the {@link WebConfigurer} class.
 */
public class WebConfigurerTest {

    private WebConfigurer webConfigurer;

    private MockServletContext servletContext;

    private MockEnvironment env;

    private JHipsterProperties props;

    @BeforeEach
    public void setup() {
	this.servletContext = Mockito.spy(new MockServletContext());
	Mockito.doReturn(Mockito.mock(FilterRegistration.Dynamic.class))
		.when(this.servletContext)
		.addFilter(ArgumentMatchers.anyString(),
			ArgumentMatchers.any(Filter.class));
	Mockito.doReturn(Mockito.mock(ServletRegistration.Dynamic.class))
		.when(this.servletContext)
		.addServlet(ArgumentMatchers.anyString(),
			ArgumentMatchers.any(Servlet.class));

	this.env = new MockEnvironment();
	this.props = new JHipsterProperties();

	this.webConfigurer = new WebConfigurer(this.env, this.props);
    }

    @Test
    public void testCorsFilterDeactivated() throws Exception {
	this.props.getCors().setAllowedOrigins(null);

	final MockMvc mockMvc = MockMvcBuilders
		.standaloneSetup(new WebConfigurerTestController())
		.addFilters(this.webConfigurer.corsFilter()).build();

	mockMvc.perform(MockMvcRequestBuilders.get("/api/test-cors")
		.header(HttpHeaders.ORIGIN, "other.domain.com"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.header()
			.doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    @Test
    public void testCorsFilterDeactivated2() throws Exception {
	this.props.getCors().setAllowedOrigins(new ArrayList<>());

	final MockMvc mockMvc = MockMvcBuilders
		.standaloneSetup(new WebConfigurerTestController())
		.addFilters(this.webConfigurer.corsFilter()).build();

	mockMvc.perform(MockMvcRequestBuilders.get("/api/test-cors")
		.header(HttpHeaders.ORIGIN, "other.domain.com"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.header()
			.doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    @Test
    public void testCorsFilterOnApiPath() throws Exception {
	this.props.getCors().setAllowedOrigins(Collections.singletonList("*"));
	this.props.getCors().setAllowedMethods(
		Arrays.asList("GET", "POST", "PUT", "DELETE"));
	this.props.getCors().setAllowedHeaders(Collections.singletonList("*"));
	this.props.getCors().setMaxAge(1800L);
	this.props.getCors().setAllowCredentials(true);

	final MockMvc mockMvc = MockMvcBuilders
		.standaloneSetup(new WebConfigurerTestController())
		.addFilters(this.webConfigurer.corsFilter()).build();

	mockMvc.perform(MockMvcRequestBuilders.options("/api/test-cors")
		.header(HttpHeaders.ORIGIN, "other.domain.com")
		.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.header().string(
			HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
			"other.domain.com"))
		.andExpect(MockMvcResultMatchers.header()
			.string(HttpHeaders.VARY, "Origin"))
		.andExpect(MockMvcResultMatchers.header().string(
			HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
			"GET,POST,PUT,DELETE"))
		.andExpect(MockMvcResultMatchers.header().string(
			HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"))
		.andExpect(MockMvcResultMatchers.header()
			.string(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "1800"));

	mockMvc.perform(MockMvcRequestBuilders.get("/api/test-cors")
		.header(HttpHeaders.ORIGIN, "other.domain.com"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.header().string(
			HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
			"other.domain.com"));
    }

    @Test
    public void testCorsFilterOnOtherPath() throws Exception {
	this.props.getCors().setAllowedOrigins(Collections.singletonList("*"));
	this.props.getCors().setAllowedMethods(
		Arrays.asList("GET", "POST", "PUT", "DELETE"));
	this.props.getCors().setAllowedHeaders(Collections.singletonList("*"));
	this.props.getCors().setMaxAge(1800L);
	this.props.getCors().setAllowCredentials(true);

	final MockMvc mockMvc = MockMvcBuilders
		.standaloneSetup(new WebConfigurerTestController())
		.addFilters(this.webConfigurer.corsFilter()).build();

	mockMvc.perform(MockMvcRequestBuilders.get("/test/test-cors")
		.header(HttpHeaders.ORIGIN, "other.domain.com"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.header()
			.doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    @Test
    public void testCustomizeServletContainer() {
	this.env.setActiveProfiles(JHipsterConstants.SPRING_PROFILE_PRODUCTION);
	final UndertowServletWebServerFactory container = new UndertowServletWebServerFactory();
	this.webConfigurer.customize(container);
	Assertions.assertThat(container.getMimeMappings().get("abs"))
		.isEqualTo("audio/x-mpeg");
	Assertions.assertThat(container.getMimeMappings().get("html"))
		.isEqualTo("text/html;charset=utf-8");
	Assertions.assertThat(container.getMimeMappings().get("json"))
		.isEqualTo("text/html;charset=utf-8");
	if (container.getDocumentRoot() != null) {
	    Assertions.assertThat(container.getDocumentRoot())
		    .isEqualTo(new File("build/resources/main/static/"));
	}
    }

    @Test
    public void testStartUpDevServletContext() throws ServletException {
	this.env.setActiveProfiles(
		JHipsterConstants.SPRING_PROFILE_DEVELOPMENT);
	this.webConfigurer.onStartup(this.servletContext);

	Mockito.verify(this.servletContext).addServlet(
		ArgumentMatchers.eq("H2Console"),
		ArgumentMatchers.any(WebServlet.class));
    }

    @Test
    public void testStartUpProdServletContext() throws ServletException {
	this.env.setActiveProfiles(JHipsterConstants.SPRING_PROFILE_PRODUCTION);
	this.webConfigurer.onStartup(this.servletContext);

	Mockito.verify(this.servletContext, Mockito.never()).addServlet(
		ArgumentMatchers.eq("H2Console"),
		ArgumentMatchers.any(WebServlet.class));
    }
}
