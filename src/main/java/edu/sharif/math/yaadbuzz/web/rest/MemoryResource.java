package edu.sharif.math.yaadbuzz.web.rest;

import edu.sharif.math.yaadbuzz.security.AuthoritiesConstants;
import edu.sharif.math.yaadbuzz.service.MemoryQueryService;
import edu.sharif.math.yaadbuzz.service.MemoryService;
import edu.sharif.math.yaadbuzz.service.dto.MemoryCriteria;
import edu.sharif.math.yaadbuzz.service.dto.MemoryDTO;
import edu.sharif.math.yaadbuzz.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link edu.sharif.math.yaadbuzz.domain.Memory}.
 */
@RestController
@RequestMapping("/api")
@RolesAllowed(AuthoritiesConstants.ADMIN)
public class MemoryResource {

    private final Logger log = LoggerFactory.getLogger(MemoryResource.class);

    private static final String ENTITY_NAME = "memory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MemoryService memoryService;

    private final MemoryQueryService memoryQueryService;

    public MemoryResource(MemoryService memoryService, MemoryQueryService memoryQueryService) {
        this.memoryService = memoryService;
        this.memoryQueryService = memoryQueryService;
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
    @PostMapping("/memories")
    public ResponseEntity<MemoryDTO> createMemory(@Valid @RequestBody MemoryDTO memoryDTO) throws URISyntaxException {
        log.debug("REST request to save Memory : {}", memoryDTO);
        if (memoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new memory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MemoryDTO result = memoryService.save(memoryDTO);
        return ResponseEntity
            .created(new URI("/api/memories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
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
    @PutMapping("/memories")
    public ResponseEntity<MemoryDTO> updateMemory(@Valid @RequestBody MemoryDTO memoryDTO) throws URISyntaxException {
        log.debug("REST request to update Memory : {}", memoryDTO);
        if (memoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MemoryDTO result = memoryService.save(memoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /memories} : Updates given fields of an existing memory.
     *
     * @param memoryDTO the memoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the updated memoryDTO, or with status
     *         {@code 400 (Bad Request)} if the memoryDTO is not valid, or with
     *         status {@code 404 (Not Found)} if the memoryDTO is not found, or
     *         with status {@code 500 (Internal Server Error)} if the memoryDTO
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/memories", consumes = "application/merge-patch+json")
    public ResponseEntity<MemoryDTO> partialUpdateMemory(@NotNull @RequestBody MemoryDTO memoryDTO) throws URISyntaxException {
        log.debug("REST request to update Memory partially : {}", memoryDTO);
        if (memoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<MemoryDTO> result = memoryService.partialUpdate(memoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /memories} : get all the memories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
     *         list of memories in body.
     */
    @GetMapping("/memories")
    public ResponseEntity<List<MemoryDTO>> getAllMemories(MemoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Memories by criteria: {}", criteria);
        Page<MemoryDTO> page = memoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /memories/count} : count all the memories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
     *         count in body.
     */
    @GetMapping("/memories/count")
    public ResponseEntity<Long> countMemories(MemoryCriteria criteria) {
        log.debug("REST request to count Memories by criteria: {}", criteria);
        return ResponseEntity.ok().body(memoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /memories/:id} : get the "id" memory.
     *
     * @param id the id of the memoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the memoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/memories/{id}")
    public ResponseEntity<MemoryDTO> getMemory(@PathVariable Long id) {
        log.debug("REST request to get Memory : {}", id);
        Optional<MemoryDTO> memoryDTO = memoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(memoryDTO);
    }

    /**
     * {@code DELETE  /memories/:id} : delete the "id" memory.
     *
     * @param id the id of the memoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/memories/{id}")
    public ResponseEntity<Void> deleteMemory(@PathVariable Long id) {
        log.debug("REST request to delete Memory : {}", id);
        memoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
