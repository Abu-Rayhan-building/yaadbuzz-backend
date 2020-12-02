package edu.sharif.math.yaadmaan.security;

import java.util.Locale;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadmaan.YaadmaanApp;
import edu.sharif.math.yaadmaan.domain.User;
import edu.sharif.math.yaadmaan.repository.UserRepository;

/**
 * Integrations tests for {@link DomainUserDetailsService}.
 */
@SpringBootTest(classes = YaadmaanApp.class)
@Transactional
public class DomainUserDetailsServiceIT {

    private static final String USER_ONE_LOGIN = "test-user-one";
    private static final String USER_ONE_EMAIL = "test-user-one@localhost";
    private static final String USER_TWO_LOGIN = "test-user-two";
    private static final String USER_TWO_EMAIL = "test-user-two@localhost";
    private static final String USER_THREE_LOGIN = "test-user-three";
    private static final String USER_THREE_EMAIL = "test-user-three@localhost";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService domainUserDetailsService;

    @Test
    public void assertThatEmailIsPrioritizedOverLogin() {
	final UserDetails userDetails = this.domainUserDetailsService
		.loadUserByUsername(DomainUserDetailsServiceIT.USER_ONE_EMAIL);
	Assertions.assertThat(userDetails).isNotNull();
	Assertions.assertThat(userDetails.getUsername())
		.isEqualTo(DomainUserDetailsServiceIT.USER_ONE_LOGIN);
    }

    @Test
    public void assertThatUserCanBeFoundByEmail() {
	final UserDetails userDetails = this.domainUserDetailsService
		.loadUserByUsername(DomainUserDetailsServiceIT.USER_TWO_EMAIL);
	Assertions.assertThat(userDetails).isNotNull();
	Assertions.assertThat(userDetails.getUsername())
		.isEqualTo(DomainUserDetailsServiceIT.USER_TWO_LOGIN);
    }

    @Test
    public void assertThatUserCanBeFoundByEmailIgnoreCase() {
	final UserDetails userDetails = this.domainUserDetailsService
		.loadUserByUsername(DomainUserDetailsServiceIT.USER_TWO_EMAIL
			.toUpperCase(Locale.ENGLISH));
	Assertions.assertThat(userDetails).isNotNull();
	Assertions.assertThat(userDetails.getUsername())
		.isEqualTo(DomainUserDetailsServiceIT.USER_TWO_LOGIN);
    }

    @Test
    public void assertThatUserCanBeFoundByLogin() {
	final UserDetails userDetails = this.domainUserDetailsService
		.loadUserByUsername(DomainUserDetailsServiceIT.USER_ONE_LOGIN);
	Assertions.assertThat(userDetails).isNotNull();
	Assertions.assertThat(userDetails.getUsername())
		.isEqualTo(DomainUserDetailsServiceIT.USER_ONE_LOGIN);
    }

    @Test
    public void assertThatUserCanBeFoundByLoginIgnoreCase() {
	final UserDetails userDetails = this.domainUserDetailsService
		.loadUserByUsername(DomainUserDetailsServiceIT.USER_ONE_LOGIN
			.toUpperCase(Locale.ENGLISH));
	Assertions.assertThat(userDetails).isNotNull();
	Assertions.assertThat(userDetails.getUsername())
		.isEqualTo(DomainUserDetailsServiceIT.USER_ONE_LOGIN);
    }

    @Test
    public void assertThatUserNotActivatedExceptionIsThrownForNotActivatedUsers() {
	Assertions.assertThatExceptionOfType(UserNotActivatedException.class)
		.isThrownBy(
			() -> this.domainUserDetailsService.loadUserByUsername(
				DomainUserDetailsServiceIT.USER_THREE_LOGIN));
    }

    @BeforeEach
    public void init() {
	final User userOne = new User();
	userOne.setLogin(DomainUserDetailsServiceIT.USER_ONE_LOGIN);
	userOne.setPassword(RandomStringUtils.random(60));
	userOne.setActivated(true);
	userOne.setEmail(DomainUserDetailsServiceIT.USER_ONE_EMAIL);
	userOne.setFirstName("userOne");
	userOne.setLastName("doe");
	userOne.setLangKey("en");
	this.userRepository.save(userOne);

	final User userTwo = new User();
	userTwo.setLogin(DomainUserDetailsServiceIT.USER_TWO_LOGIN);
	userTwo.setPassword(RandomStringUtils.random(60));
	userTwo.setActivated(true);
	userTwo.setEmail(DomainUserDetailsServiceIT.USER_TWO_EMAIL);
	userTwo.setFirstName("userTwo");
	userTwo.setLastName("doe");
	userTwo.setLangKey("en");
	this.userRepository.save(userTwo);

	final User userThree = new User();
	userThree.setLogin(DomainUserDetailsServiceIT.USER_THREE_LOGIN);
	userThree.setPassword(RandomStringUtils.random(60));
	userThree.setActivated(false);
	userThree.setEmail(DomainUserDetailsServiceIT.USER_THREE_EMAIL);
	userThree.setFirstName("userThree");
	userThree.setLastName("doe");
	userThree.setLangKey("en");
	this.userRepository.save(userThree);
    }

}
