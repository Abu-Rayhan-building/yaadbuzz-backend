package edu.sharif.math.yaadmaan.config;

import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.http.CacheControl;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import io.github.jhipster.config.JHipsterDefaults;
import io.github.jhipster.config.JHipsterProperties;

public class StaticResourcesWebConfigurerTest {
    public static final int MAX_AGE_TEST = 5;
    public StaticResourcesWebConfiguration staticResourcesWebConfiguration;
    private ResourceHandlerRegistry resourceHandlerRegistry;
    private MockServletContext servletContext;
    private WebApplicationContext applicationContext;
    private JHipsterProperties props;

    @BeforeEach
    void setUp() {
	this.servletContext = Mockito.spy(new MockServletContext());
	this.applicationContext = Mockito.mock(WebApplicationContext.class);
	this.resourceHandlerRegistry = Mockito.spy(new ResourceHandlerRegistry(
		this.applicationContext, this.servletContext));
	this.props = new JHipsterProperties();
	this.staticResourcesWebConfiguration = Mockito
		.spy(new StaticResourcesWebConfiguration(this.props));
    }

    @Test
    public void shoudCreateCacheControlBasedOnJhipsterDefaultProperties() {
	final CacheControl cacheExpected = CacheControl
		.maxAge(JHipsterDefaults.Http.Cache.timeToLiveInDays,
			TimeUnit.DAYS)
		.cachePublic();
	Assertions
		.assertThat(
			this.staticResourcesWebConfiguration.getCacheControl())
		.extracting(CacheControl::getHeaderValue)
		.isEqualTo(cacheExpected.getHeaderValue());
    }

    @Test
    public void shoudCreateCacheControlWithSpecificConfigurationInProperties() {
	this.props.getHttp().getCache().setTimeToLiveInDays(
		StaticResourcesWebConfigurerTest.MAX_AGE_TEST);
	final CacheControl cacheExpected = CacheControl
		.maxAge(StaticResourcesWebConfigurerTest.MAX_AGE_TEST,
			TimeUnit.DAYS)
		.cachePublic();
	Assertions
		.assertThat(
			this.staticResourcesWebConfiguration.getCacheControl())
		.extracting(CacheControl::getHeaderValue)
		.isEqualTo(cacheExpected.getHeaderValue());
    }

    @Test
    public void shouldAppendResourceHandlerAndInitiliazeIt() {

	this.staticResourcesWebConfiguration
		.addResourceHandlers(this.resourceHandlerRegistry);

	Mockito.verify(this.resourceHandlerRegistry, Mockito.times(1))
		.addResourceHandler(
			StaticResourcesWebConfiguration.RESOURCE_PATHS);
	Mockito.verify(this.staticResourcesWebConfiguration, Mockito.times(1))
		.initializeResourceHandler(ArgumentMatchers
			.any(ResourceHandlerRegistration.class));
	for (final String testingPath : StaticResourcesWebConfiguration.RESOURCE_PATHS) {
	    Assertions.assertThat(this.resourceHandlerRegistry
		    .hasMappingForPattern(testingPath)).isTrue();
	}
    }

    @Test
    public void shouldInitializeResourceHandlerWithCacheControlAndLocations() {
	final CacheControl ccExpected = CacheControl.maxAge(5, TimeUnit.DAYS)
		.cachePublic();
	Mockito.when(this.staticResourcesWebConfiguration.getCacheControl())
		.thenReturn(ccExpected);
	final ResourceHandlerRegistration resourceHandlerRegistration = Mockito
		.spy(new ResourceHandlerRegistration(
			StaticResourcesWebConfiguration.RESOURCE_PATHS));

	this.staticResourcesWebConfiguration
		.initializeResourceHandler(resourceHandlerRegistration);

	Mockito.verify(this.staticResourcesWebConfiguration, Mockito.times(1))
		.getCacheControl();
	Mockito.verify(resourceHandlerRegistration, Mockito.times(1))
		.setCacheControl(ccExpected);
	Mockito.verify(resourceHandlerRegistration, Mockito.times(1))
		.addResourceLocations(
			StaticResourcesWebConfiguration.RESOURCE_LOCATIONS);
    }
}
