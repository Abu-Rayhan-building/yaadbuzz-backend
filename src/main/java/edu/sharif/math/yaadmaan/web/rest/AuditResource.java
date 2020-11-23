package edu.sharif.math.yaadmaan.web.rest;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.sharif.math.yaadmaan.service.AuditEventService;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for getting the {@link AuditEvent}s.
 */
@RestController
@RequestMapping("/management/audits")
public class AuditResource {

    private final AuditEventService auditEventService;

    public AuditResource(final AuditEventService auditEventService) {
	this.auditEventService = auditEventService;
    }

    /**
     * {@code GET  /audits/:id} : get an {@link AuditEvent} by id.
     *
     * @param id the id of the entity to get.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
     *         {@link AuditEvent} in body, or status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id:.+}")
    public ResponseEntity<AuditEvent> get(@PathVariable final Long id) {
	return ResponseUtil.wrapOrNotFound(this.auditEventService.find(id));
    }

    /**
     * {@code GET /audits} : get a page of {@link AuditEvent}s.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
     *         list of {@link AuditEvent}s in body.
     */
    @GetMapping
    public ResponseEntity<List<AuditEvent>> getAll(final Pageable pageable) {
	final Page<AuditEvent> page = this.auditEventService.findAll(pageable);
	final HttpHeaders headers = PaginationUtil
		.generatePaginationHttpHeaders(
			ServletUriComponentsBuilder.fromCurrentRequest(), page);
	return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET  /audits} : get a page of {@link AuditEvent} between the
     * {@code fromDate} and {@code toDate}.
     *
     * @param fromDate the start of the time period of {@link AuditEvent} to
     *                 get.
     * @param toDate   the end of the time period of {@link AuditEvent} to get.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
     *         list of {@link AuditEvent} in body.
     */
    @GetMapping(params = { "fromDate", "toDate" })
    public ResponseEntity<List<AuditEvent>> getByDates(
	    @RequestParam(value = "fromDate") final LocalDate fromDate,
	    @RequestParam(value = "toDate") final LocalDate toDate,
	    final Pageable pageable) {

	final Instant from = fromDate.atStartOfDay(ZoneId.systemDefault())
		.toInstant();
	final Instant to = toDate.atStartOfDay(ZoneId.systemDefault())
		.plusDays(1).toInstant();

	final Page<AuditEvent> page = this.auditEventService.findByDates(from,
		to, pageable);
	final HttpHeaders headers = PaginationUtil
		.generatePaginationHttpHeaders(
			ServletUriComponentsBuilder.fromCurrentRequest(), page);
	return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
