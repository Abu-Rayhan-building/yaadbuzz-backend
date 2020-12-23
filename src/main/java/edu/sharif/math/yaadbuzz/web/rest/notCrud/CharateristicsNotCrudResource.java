package edu.sharif.math.yaadbuzz.web.rest.notCrud;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.sharif.math.yaadbuzz.service.CharateristicsService;
import edu.sharif.math.yaadbuzz.service.DepartmentService;
import edu.sharif.math.yaadbuzz.service.dto.CharateristicsDTO;
import edu.sharif.math.yaadbuzz.service.dto.helpers.CharateristicsVoteDTO;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing
 * {@link edu.sharif.math.yaadbuzz.domain.Charateristics}.
 */
@RestController
@RequestMapping("/api/department/{depId}")
public class CharateristicsNotCrudResource {

    private static final String ENTITY_NAME = "charateristics";

    private final Logger log = LoggerFactory
	    .getLogger(CharateristicsNotCrudResource.class);

    private final DepartmentService departmentService;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CharateristicsService charateristicsService;

    public CharateristicsNotCrudResource(
	    final CharateristicsService charateristicsService,
	    final DepartmentService departmentService) {
	this.departmentService = departmentService;
	this.charateristicsService = charateristicsService;
    }

    /**
     * {@code POST  /charateristics} : Create a new charateristics.
     *
     * @param charateristicsDTO the charateristicsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and
     *         with body the new charateristicsDTO, or with status
     *         {@code 400 (Bad Request)} if the charateristics has already an
     *         ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/charateristic/assign")
    public ResponseEntity<CharateristicsDTO> createCharateristics(
	    @PathVariable final Long depId,
	    @Valid @RequestBody final CharateristicsVoteDTO charateristicsDTO)
	    throws URISyntaxException {
	this.log.debug("REST request to save Charateristics : {}",
		charateristicsDTO);
	if (!this.charateristicsService.currentuserHasVoteAccess(depId)) {
	    throw new AccessDeniedException("create characteristics denied");
	}

	final CharateristicsDTO result = this.charateristicsService
		.assign(charateristicsDTO);
	return ResponseEntity
		.created(new URI("/api/charateristics/" + result.getId()))
		.headers(HeaderUtil.createEntityCreationAlert(
			this.applicationName, true,
			CharateristicsNotCrudResource.ENTITY_NAME,
			result.getId().toString()))
		.body(result);
    }

    /**
     * {@code GET  /charateristics} : get all the charateristics.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
     *         list of charateristics in body.
     */
    @GetMapping("/charateristic/me")
    public Page<CharateristicsDTO> getAllCharateristics(
	    @PathVariable final Long depId, final Pageable pageable) {

	this.log.debug("REST request to get all Charateristics");
	if (!this.departmentService.currentuserHasGetAccess(depId)) {
	    throw new AccessDeniedException("caracteristics denied");
	}
	return this.charateristicsService.findMineInDep(depId, pageable);
    }

}
