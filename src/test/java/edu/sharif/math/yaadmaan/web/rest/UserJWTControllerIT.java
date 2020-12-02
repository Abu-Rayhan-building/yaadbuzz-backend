package edu.sharif.math.yaadmaan.web.rest;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadmaan.YaadmaanApp;
import edu.sharif.math.yaadmaan.domain.User;
import edu.sharif.math.yaadmaan.repository.UserRepository;
import edu.sharif.math.yaadmaan.web.rest.vm.LoginVM;

/**
 * Integration tests for the {@link UserJWTController} REST controller.
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = YaadmaanApp.class)
public class UserJWTControllerIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    public void testAuthorize() throws Exception {
	final User user = new User();
	user.setLogin("user-jwt-controller");
	user.setEmail("user-jwt-controller@example.com");
	user.setActivated(true);
	user.setPassword(this.passwordEncoder.encode("test"));

	this.userRepository.saveAndFlush(user);

	final LoginVM login = new LoginVM();
	login.setUsername("user-jwt-controller");
	login.setPassword("test");
	this.mockMvc
		.perform(MockMvcRequestBuilders.post("/api/authenticate")
			.contentType(MediaType.APPLICATION_JSON)
			.content(TestUtil.convertObjectToJsonBytes(login)))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(
			MockMvcResultMatchers.jsonPath("$.id_token").isString())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id_token")
			.isNotEmpty())
		.andExpect(MockMvcResultMatchers.header().string(
			"Authorization", Matchers.not(Matchers.nullValue())))
		.andExpect(MockMvcResultMatchers.header().string(
			"Authorization",
			Matchers.not(Matchers.is(Matchers.emptyString()))));
    }

    @Test
    public void testAuthorizeFails() throws Exception {
	final LoginVM login = new LoginVM();
	login.setUsername("wrong-user");
	login.setPassword("wrong password");
	this.mockMvc
		.perform(MockMvcRequestBuilders.post("/api/authenticate")
			.contentType(MediaType.APPLICATION_JSON)
			.content(TestUtil.convertObjectToJsonBytes(login)))
		.andExpect(MockMvcResultMatchers.status().isUnauthorized())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id_token")
			.doesNotExist())
		.andExpect(MockMvcResultMatchers.header()
			.doesNotExist("Authorization"));
    }

    @Test
    @Transactional
    public void testAuthorizeWithRememberMe() throws Exception {
	final User user = new User();
	user.setLogin("user-jwt-controller-remember-me");
	user.setEmail("user-jwt-controller-remember-me@example.com");
	user.setActivated(true);
	user.setPassword(this.passwordEncoder.encode("test"));

	this.userRepository.saveAndFlush(user);

	final LoginVM login = new LoginVM();
	login.setUsername("user-jwt-controller-remember-me");
	login.setPassword("test");
	login.setRememberMe(true);
	this.mockMvc
		.perform(MockMvcRequestBuilders.post("/api/authenticate")
			.contentType(MediaType.APPLICATION_JSON)
			.content(TestUtil.convertObjectToJsonBytes(login)))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(
			MockMvcResultMatchers.jsonPath("$.id_token").isString())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id_token")
			.isNotEmpty())
		.andExpect(MockMvcResultMatchers.header().string(
			"Authorization", Matchers.not(Matchers.nullValue())))
		.andExpect(MockMvcResultMatchers.header().string(
			"Authorization",
			Matchers.not(Matchers.is(Matchers.emptyString()))));
    }
}
