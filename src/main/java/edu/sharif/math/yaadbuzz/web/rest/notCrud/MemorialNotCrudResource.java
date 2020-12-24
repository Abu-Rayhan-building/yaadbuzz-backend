package edu.sharif.math.yaadbuzz.web.rest.notCrud;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.sharif.math.yaadbuzz.service.MemorialQueryService;
import edu.sharif.math.yaadbuzz.service.MemorialService;
import edu.sharif.math.yaadbuzz.service.UserPerDepartmentService;
import edu.sharif.math.yaadbuzz.service.UserService;
import edu.sharif.math.yaadbuzz.service.dto.MemorialCriteria;
import edu.sharif.math.yaadbuzz.service.dto.MemorialDTO;
import edu.sharif.math.yaadbuzz.service.dto.helpers.MemorialUDTO;
import edu.sharif.math.yaadbuzz.web.rest.errors.BadRequestAlertException;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link edu.sharif.math.yaadbuzz.domain.Memorial}.
 */
@RestController
@RequestMapping("/api/department/{depId}")
public class MemorialNotCrudResource {

    private static final String ENTITY_NAME = "memorial";

    private final Logger log = LoggerFactory
	    .getLogger(MemorialNotCrudResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MemorialService memorialService;

    private final MemorialQueryService memorialQueryService;

    private final UserService userService;

    private final UserPerDepartmentService userPerDepartmentService;

    public MemorialNotCrudResource(final MemorialService memorialService,
	    final MemorialQueryService memorialQueryService,
	    final UserService userService,
	    final UserPerDepartmentService userPerDepartmentService) {
	this.memorialService = memorialService;
	this.memorialQueryService = memorialQueryService;
	this.userService = userService;
	this.userPerDepartmentService = userPerDepartmentService;
    }

    /**
     * {@code POST  /memorials} : Create a new memorial.
     *
     * @param memorialDTO the memorialDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and
     *         with body the new memorialDTO, or with status
     *         {@code 400 (Bad Request)} if the memorial has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/memorial/assign/")
    public ResponseEntity<MemorialDTO> createMemorial(
	    @PathVariable final Long depId,
	    @Valid @RequestBody final MemorialUDTO memorialUDTO)
	    throws URISyntaxException {
	this.log.debug("REST request to save Memorial : {}", memorialUDTO);

	final MemorialDTO result = this.memorialService.create(depId,
		memorialUDTO);
	return ResponseEntity
		.created(new URI("/api/memorials/" + result.getId()))
		.headers(HeaderUtil.createEntityCreationAlert(
			this.applicationName, true,
			MemorialNotCrudResource.ENTITY_NAME,
			result.getId().toString()))
		.body(result);
    }

    /**
     * {@code GET  /memorials/:id} : get the "id" memorial.
     *
     * @param id the id of the memorialDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the memorialDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/memorials/{id}")
    public ResponseEntity<MemorialDTO> getMemorial(
	    @PathVariable final Long id) {
	this.log.debug("REST request to get Memorial : {}", id);
	if (!this.memorialService.currentuserHasGetAccess(id)) {
	    throw new AccessDeniedException("cant get memorial");
	}
	final Optional<MemorialDTO> memorialDTO = this.memorialService
		.findOne(id);
	return ResponseUtil.wrapOrNotFound(memorialDTO);
    }

    /**
     * {@code GET  /memorials} : get all the memorials.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
     *         list of memorials in body.
     */
    @GetMapping("/memorial")
    public ResponseEntity<List<MemorialDTO>> getMemorialsIWrote(
	    @PathVariable final Long depId, final MemorialCriteria criteria,
	    final Pageable pageable) {
	this.log.debug("REST request to get Memorials by criteria: {}",
		criteria);
	final var writerFilter = new LongFilter();
	writerFilter.setEquals(this.userPerDepartmentService
		.getCurrentUserInDep(depId).getId());
	criteria.setWriterId(writerFilter);
	final Page<MemorialDTO> page = this.memorialQueryService
		.findByCriteria(criteria, pageable);
	final HttpHeaders headers = PaginationUtil
		.generatePaginationHttpHeaders(
			ServletUriComponentsBuilder.fromCurrentRequest(), page);
	return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
