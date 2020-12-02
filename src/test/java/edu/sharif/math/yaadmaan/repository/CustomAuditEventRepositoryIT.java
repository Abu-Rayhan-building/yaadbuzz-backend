package edu.sharif.math.yaadmaan.repository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadmaan.YaadmaanApp;
import edu.sharif.math.yaadmaan.config.Constants;
import edu.sharif.math.yaadmaan.config.audit.AuditEventConverter;
import edu.sharif.math.yaadmaan.domain.PersistentAuditEvent;

/**
 * Integration tests for {@link CustomAuditEventRepository}.
 */
@SpringBootTest(classes = YaadmaanApp.class)
@Transactional
public class CustomAuditEventRepositoryIT {

    @Autowired
    private PersistenceAuditEventRepository persistenceAuditEventRepository;

    @Autowired
    private AuditEventConverter auditEventConverter;

    private CustomAuditEventRepository customAuditEventRepository;

    @Test
    public void addAuditEvent() {
	final Map<String, Object> data = new HashMap<>();
	data.put("test-key", "test-value");
	final AuditEvent event = new AuditEvent("test-user", "test-type", data);
	this.customAuditEventRepository.add(event);
	final List<PersistentAuditEvent> persistentAuditEvents = this.persistenceAuditEventRepository
		.findAll();
	Assertions.assertThat(persistentAuditEvents).hasSize(1);
	final PersistentAuditEvent persistentAuditEvent = persistentAuditEvents
		.get(0);
	Assertions.assertThat(persistentAuditEvent.getPrincipal())
		.isEqualTo(event.getPrincipal());
	Assertions.assertThat(persistentAuditEvent.getAuditEventType())
		.isEqualTo(event.getType());
	Assertions.assertThat(persistentAuditEvent.getData())
		.containsKey("test-key");
	Assertions.assertThat(persistentAuditEvent.getData().get("test-key"))
		.isEqualTo("test-value");
	Assertions
		.assertThat(persistentAuditEvent.getAuditEventDate()
			.truncatedTo(ChronoUnit.MILLIS))
		.isEqualTo(event.getTimestamp().truncatedTo(ChronoUnit.MILLIS));
    }

    @Test
    public void addAuditEventTruncateLargeData() {
	final Map<String, Object> data = new HashMap<>();
	final StringBuilder largeData = new StringBuilder();
	for (int i = 0; i < CustomAuditEventRepository.EVENT_DATA_COLUMN_MAX_LENGTH
		+ 10; i++) {
	    largeData.append("a");
	}
	data.put("test-key", largeData);
	final AuditEvent event = new AuditEvent("test-user", "test-type", data);
	this.customAuditEventRepository.add(event);
	final List<PersistentAuditEvent> persistentAuditEvents = this.persistenceAuditEventRepository
		.findAll();
	Assertions.assertThat(persistentAuditEvents).hasSize(1);
	final PersistentAuditEvent persistentAuditEvent = persistentAuditEvents
		.get(0);
	Assertions.assertThat(persistentAuditEvent.getPrincipal())
		.isEqualTo(event.getPrincipal());
	Assertions.assertThat(persistentAuditEvent.getAuditEventType())
		.isEqualTo(event.getType());
	Assertions.assertThat(persistentAuditEvent.getData())
		.containsKey("test-key");
	final String actualData = persistentAuditEvent.getData()
		.get("test-key");
	Assertions.assertThat(actualData.length()).isEqualTo(
		CustomAuditEventRepository.EVENT_DATA_COLUMN_MAX_LENGTH);
	Assertions.assertThat(actualData).isSubstringOf(largeData);
	Assertions
		.assertThat(persistentAuditEvent.getAuditEventDate()
			.truncatedTo(ChronoUnit.MILLIS))
		.isEqualTo(event.getTimestamp().truncatedTo(ChronoUnit.MILLIS));
    }

    @Test
    public void addAuditEventWithAnonymousUser() {
	final Map<String, Object> data = new HashMap<>();
	data.put("test-key", "test-value");
	final AuditEvent event = new AuditEvent(Constants.ANONYMOUS_USER,
		"test-type", data);
	this.customAuditEventRepository.add(event);
	final List<PersistentAuditEvent> persistentAuditEvents = this.persistenceAuditEventRepository
		.findAll();
	Assertions.assertThat(persistentAuditEvents).hasSize(0);
    }

    @Test
    public void addAuditEventWithAuthorizationFailureType() {
	final Map<String, Object> data = new HashMap<>();
	data.put("test-key", "test-value");
	final AuditEvent event = new AuditEvent("test-user",
		"AUTHORIZATION_FAILURE", data);
	this.customAuditEventRepository.add(event);
	final List<PersistentAuditEvent> persistentAuditEvents = this.persistenceAuditEventRepository
		.findAll();
	Assertions.assertThat(persistentAuditEvents).hasSize(0);
    }

    @BeforeEach
    public void setup() {
	this.customAuditEventRepository = new CustomAuditEventRepository(
		this.persistenceAuditEventRepository, this.auditEventConverter);
	this.persistenceAuditEventRepository.deleteAll();
	final Instant oneHourAgo = Instant.now().minusSeconds(3600);

	final PersistentAuditEvent testUserEvent = new PersistentAuditEvent();
	testUserEvent.setPrincipal("test-user");
	testUserEvent.setAuditEventType("test-type");
	testUserEvent.setAuditEventDate(oneHourAgo);
	final Map<String, String> data = new HashMap<>();
	data.put("test-key", "test-value");
	testUserEvent.setData(data);

	final PersistentAuditEvent testOldUserEvent = new PersistentAuditEvent();
	testOldUserEvent.setPrincipal("test-user");
	testOldUserEvent.setAuditEventType("test-type");
	testOldUserEvent.setAuditEventDate(oneHourAgo.minusSeconds(10000));

	final PersistentAuditEvent testOtherUserEvent = new PersistentAuditEvent();
	testOtherUserEvent.setPrincipal("other-test-user");
	testOtherUserEvent.setAuditEventType("test-type");
	testOtherUserEvent.setAuditEventDate(oneHourAgo);
    }

    @Test
    public void testAddEventWithNullData() {
	final Map<String, Object> data = new HashMap<>();
	data.put("test-key", null);
	final AuditEvent event = new AuditEvent("test-user", "test-type", data);
	this.customAuditEventRepository.add(event);
	final List<PersistentAuditEvent> persistentAuditEvents = this.persistenceAuditEventRepository
		.findAll();
	Assertions.assertThat(persistentAuditEvents).hasSize(1);
	final PersistentAuditEvent persistentAuditEvent = persistentAuditEvents
		.get(0);
	Assertions.assertThat(persistentAuditEvent.getData().get("test-key"))
		.isEqualTo("null");
    }

    @Test
    public void testAddEventWithWebAuthenticationDetails() {
	final HttpSession session = new MockHttpSession(null,
		"test-session-id");
	final MockHttpServletRequest request = new MockHttpServletRequest();
	request.setSession(session);
	request.setRemoteAddr("1.2.3.4");
	final WebAuthenticationDetails details = new WebAuthenticationDetails(
		request);
	final Map<String, Object> data = new HashMap<>();
	data.put("test-key", details);
	final AuditEvent event = new AuditEvent("test-user", "test-type", data);
	this.customAuditEventRepository.add(event);
	final List<PersistentAuditEvent> persistentAuditEvents = this.persistenceAuditEventRepository
		.findAll();
	Assertions.assertThat(persistentAuditEvents).hasSize(1);
	final PersistentAuditEvent persistentAuditEvent = persistentAuditEvents
		.get(0);
	Assertions
		.assertThat(persistentAuditEvent.getData().get("remoteAddress"))
		.isEqualTo("1.2.3.4");
	Assertions.assertThat(persistentAuditEvent.getData().get("sessionId"))
		.isEqualTo("test-session-id");
    }

}
