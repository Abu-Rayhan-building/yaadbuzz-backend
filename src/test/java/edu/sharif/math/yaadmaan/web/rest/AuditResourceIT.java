package edu.sharif.math.yaadmaan.web.rest;

import java.time.Instant;

import org.assertj.core.api.AssertionsForClassTypes;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadmaan.YaadmaanApp;
import edu.sharif.math.yaadmaan.domain.PersistentAuditEvent;
import edu.sharif.math.yaadmaan.repository.PersistenceAuditEventRepository;
import edu.sharif.math.yaadmaan.security.AuthoritiesConstants;

/**
 * Integration tests for the {@link AuditResource} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@SpringBootTest(classes = YaadmaanApp.class)
@Transactional
public class AuditResourceIT {

    private static final String SAMPLE_PRINCIPAL = "SAMPLE_PRINCIPAL";
    private static final String SAMPLE_TYPE = "SAMPLE_TYPE";
    private static final Instant SAMPLE_TIMESTAMP = Instant
	    .parse("2015-08-04T10:11:30Z");
    private static final long SECONDS_PER_DAY = 60 * 60 * 24;

    @Autowired
    private PersistenceAuditEventRepository auditEventRepository;

    private PersistentAuditEvent auditEvent;

    @Autowired
    private MockMvc restAuditMockMvc;

    @Test
    public void getAllAudits() throws Exception {
	// Initialize the database
	this.auditEventRepository.save(this.auditEvent);

	// Get all the audits
	this.restAuditMockMvc
		.perform(MockMvcRequestBuilders.get("/management/audits"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content()
			.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].principal")
			.value(Matchers
				.hasItem(AuditResourceIT.SAMPLE_PRINCIPAL)));
    }

    @Test
    public void getAudit() throws Exception {
	// Initialize the database
	this.auditEventRepository.save(this.auditEvent);

	// Get the audit
	this.restAuditMockMvc
		.perform(MockMvcRequestBuilders.get("/management/audits/{id}",
			this.auditEvent.getId()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content()
			.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(MockMvcResultMatchers.jsonPath("$.principal")
			.value(AuditResourceIT.SAMPLE_PRINCIPAL));
    }

    @Test
    public void getAuditsByDate() throws Exception {
	// Initialize the database
	this.auditEventRepository.save(this.auditEvent);

	// Generate dates for selecting audits by date, making sure the period
	// will contain the audit
	final String fromDate = AuditResourceIT.SAMPLE_TIMESTAMP
		.minusSeconds(AuditResourceIT.SECONDS_PER_DAY).toString()
		.substring(0, 10);
	final String toDate = AuditResourceIT.SAMPLE_TIMESTAMP
		.plusSeconds(AuditResourceIT.SECONDS_PER_DAY).toString()
		.substring(0, 10);

	// Get the audit
	this.restAuditMockMvc
		.perform(MockMvcRequestBuilders
			.get("/management/audits?fromDate=" + fromDate
				+ "&toDate=" + toDate))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content()
			.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(MockMvcResultMatchers.jsonPath("$.[*].principal")
			.value(Matchers
				.hasItem(AuditResourceIT.SAMPLE_PRINCIPAL)));
    }

    @Test
    public void getNonExistingAudit() throws Exception {
	// Get the audit
	this.restAuditMockMvc
		.perform(MockMvcRequestBuilders.get("/management/audits/{id}",
			Long.MAX_VALUE))
		.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getNonExistingAuditsByDate() throws Exception {
	// Initialize the database
	this.auditEventRepository.save(this.auditEvent);

	// Generate dates for selecting audits by date, making sure the period
	// will not contain the sample audit
	final String fromDate = AuditResourceIT.SAMPLE_TIMESTAMP
		.minusSeconds(2 * AuditResourceIT.SECONDS_PER_DAY).toString()
		.substring(0, 10);
	final String toDate = AuditResourceIT.SAMPLE_TIMESTAMP
		.minusSeconds(AuditResourceIT.SECONDS_PER_DAY).toString()
		.substring(0, 10);

	// Query audits but expect no results
	this.restAuditMockMvc
		.perform(MockMvcRequestBuilders
			.get("/management/audits?fromDate=" + fromDate
				+ "&toDate=" + toDate))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content()
			.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(MockMvcResultMatchers.header()
			.string("X-Total-Count", "0"));
    }

    @BeforeEach
    public void initTest() {
	this.auditEventRepository.deleteAll();
	this.auditEvent = new PersistentAuditEvent();
	this.auditEvent.setAuditEventType(AuditResourceIT.SAMPLE_TYPE);
	this.auditEvent.setPrincipal(AuditResourceIT.SAMPLE_PRINCIPAL);
	this.auditEvent.setAuditEventDate(AuditResourceIT.SAMPLE_TIMESTAMP);
    }

    @Test
    public void testPersistentAuditEventEquals() throws Exception {
	TestUtil.equalsVerifier(PersistentAuditEvent.class);
	final PersistentAuditEvent auditEvent1 = new PersistentAuditEvent();
	auditEvent1.setId(1L);
	final PersistentAuditEvent auditEvent2 = new PersistentAuditEvent();
	auditEvent2.setId(auditEvent1.getId());
	AssertionsForClassTypes.assertThat(auditEvent1).isEqualTo(auditEvent2);
	auditEvent2.setId(2L);
	AssertionsForClassTypes.assertThat(auditEvent1)
		.isNotEqualTo(auditEvent2);
	auditEvent1.setId(null);
	AssertionsForClassTypes.assertThat(auditEvent1)
		.isNotEqualTo(auditEvent2);
    }
}
