package edu.sharif.math.yaadmaan.web.rest.notCrud;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.sharif.math.yaadmaan.domain.User;
import edu.sharif.math.yaadmaan.repository.UserRepository;
import edu.sharif.math.yaadmaan.service.DepartmentService;
import edu.sharif.math.yaadmaan.service.MailService;
import edu.sharif.math.yaadmaan.service.MemoryService;
import edu.sharif.math.yaadmaan.service.UserService;
import edu.sharif.math.yaadmaan.service.dto.MemoryDTO;
import edu.sharif.math.yaadmaan.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link User} entity, and needs to fetch its
 * collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship
 * between User and Authority, and send everything to the client side: there
 * would be no View Model and DTO, a lot less code, and an outer-join which
 * would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities,
 * because people will quite often do relationships with the user, and we don't
 * want them to get the authorities all the time for nothing (for performance
 * reasons). This is the #1 goal: we should not impact our users' application
 * because of this use-case.</li>
 * <li>Not having an outer join causes n+1 requests to the database. This is not
 * a real issue as we have by default a second-level cache. This means on the
 * first HTTP call we do the n+1 requests, but then all authorities come from
 * the cache, so in fact it's much better than doing an outer join (which will
 * get lots of data from the database, for each HTTP call).</li>
 * <li>As this manages users, for security reasons, we'd rather have a DTO
 * layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this
 * case.
 */
@RestController
@RequestMapping("/api/department/{depId}")
public class MemoryNotCrudResource {
    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections
	    .unmodifiableList(Arrays.asList("id", "login", "firstName",
		    "lastName", "email", "activated", "langKey"));

    private static final String ENTITY_NAME = "memory";

    private final Logger log = LoggerFactory
	    .getLogger(MemoryNotCrudResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MemoryService memoryService;

    public MemoryNotCrudResource(final DepartmentService departmentService,
	    final UserService userService, final UserRepository userRepository,
	    final MailService mailService, final MemoryService memoryService) {
	this.memoryService = memoryService;
    }

    /**
     * {@code POST  /memories} : Create a new memory.
     *
     * @param memoryDTO the memoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and
     *         with body the new memoryDTO, or with status
     *         {@code 400 (Bad Request)} if the memory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/memory")
    public ResponseEntity<MemoryDTO> createMemory(
	    @PathVariable final Long depId,
	    @Valid @RequestBody final MemoryDTO memoryDTO)
	    throws URISyntaxException {
	this.log.debug("REST request to save Memory : {}", memoryDTO);
	if (memoryDTO.getId() != null) {
	    throw new BadRequestAlertException(
		    "A new memory cannot already have an ID",
		    MemoryNotCrudResource.ENTITY_NAME, "idexists");
	}
	if (!this.memoryService.currentuserHasCreatAccess(depId)) {
	    throw new AccessDeniedException("cant create memory");
	}
	final MemoryDTO result = this.memoryService.save(memoryDTO);
	return ResponseEntity
		.created(new URI("/api/memories/" + result.getId()))
		.headers(HeaderUtil.createEntityCreationAlert(
			this.applicationName, true,
			MemoryNotCrudResource.ENTITY_NAME,
			result.getId().toString()))
		.body(result);
    }

    /**
     * {@code DELETE  /memories/:id} : delete the "id" memory.
     *
     * @param id the id of the memoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/memories/{id}")
    public ResponseEntity<Void> deleteMemory(@PathVariable final Long id) {
	this.log.debug("REST request to delete Memory : {}", id);
	if (!this.memoryService.currentuserHasDeleteAccess(id)) {
	    throw new AccessDeniedException("cant create memory");
	}
	this.memoryService.delete(id);
	return ResponseEntity.noContent()
		.headers(HeaderUtil.createEntityDeletionAlert(
			this.applicationName, true,
			MemoryNotCrudResource.ENTITY_NAME, id.toString()))
		.build();
    }

    @GetMapping("/memories")
    public ResponseEntity<List<MemoryDTO>> getMemoriesInDep(
	    final Pageable pageable, @PathVariable final Long depId) {
	final Page<MemoryDTO> page = this.memoryService
		.findAllInDepartment(depId, pageable);
	final HttpHeaders headers = PaginationUtil
		.generatePaginationHttpHeaders(
			ServletUriComponentsBuilder.fromCurrentRequest(), page);
	return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /memories/:id} : get the "id" memory.
     *
     * @param id the id of the memoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the memoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/memory/{id}")
    public ResponseEntity<MemoryDTO> getMemory(@PathVariable final Long id) {
	this.log.debug("REST request to get Memory : {}", id);
	if (!this.memoryService.currentuserHasGetAccess(id)) {
	    throw new AccessDeniedException("cant create memory");
	}
	final Optional<MemoryDTO> memoryDTO = this.memoryService.findOne(id);
	return ResponseUtil.wrapOrNotFound(memoryDTO);
    }

    @GetMapping("/memories/me")
    public ResponseEntity<List<MemoryDTO>> getMyMemoriesInDep(
	    @PathVariable final Long depId, final Pageable pageable) {
	final Page<MemoryDTO> page = this.memoryService
		.findAllWithCurrentUserTagedIn(depId, pageable);
	final HttpHeaders headers = PaginationUtil
		.generatePaginationHttpHeaders(
			ServletUriComponentsBuilder.fromCurrentRequest(), page);
	return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/memories/user/{userPerDepId}")
    public ResponseEntity<List<MemoryDTO>> getMyMemoriesOfUserInDep(
	    @PathVariable final Long depId,
	    @PathVariable final Long userPerDepId, final Pageable pageable) {
	final Page<MemoryDTO> page = this.memoryService
		.findAllWithCurrentUserTagedIn(depId, pageable);
	final HttpHeaders headers = PaginationUtil
		.generatePaginationHttpHeaders(
			ServletUriComponentsBuilder.fromCurrentRequest(), page);
	return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code PUT  /memories} : Updates an existing memory.
     *
     * @param memoryDTO the memoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the updated memoryDTO, or with status
     *         {@code 400 (Bad Request)} if the memoryDTO is not valid, or with
     *         status {@code 500 (Internal Server Error)} if the memoryDTO
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/memory")
    public ResponseEntity<MemoryDTO> updateMemory(
	    @Valid @RequestBody final MemoryDTO memoryDTO)
	    throws URISyntaxException {
	this.log.debug("REST request to update Memory : {}", memoryDTO);
	if (memoryDTO.getId() == null) {
	    throw new BadRequestAlertException("Invalid id",
		    MemoryNotCrudResource.ENTITY_NAME, "idnull");
	}
	if (!this.memoryService.currentuserHasUpdateAccess(memoryDTO.getId())) {
	    throw new AccessDeniedException("cant create memory");
	}
	final MemoryDTO result = this.memoryService.save(memoryDTO);
	return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(
		this.applicationName, true, MemoryNotCrudResource.ENTITY_NAME,
		memoryDTO.getId().toString())).body(result);
    }
}
