package edu.sharif.math.yaadmaan.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.sharif.math.yaadmaan.security.AuthoritiesConstants;
import edu.sharif.math.yaadmaan.service.MemoryService;
import edu.sharif.math.yaadmaan.service.dto.MemoryDTO;
import edu.sharif.math.yaadmaan.web.rest.errors.BadRequestAlertException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link edu.sharif.math.yaadmaan.domain.Memory}.
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

    public MemoryResource(MemoryService memoryService) {
        this.memoryService = memoryService;
    }

    /**
     * {@code POST  /memories} : Create a new memory.
     *
     * @param memoryDTO the memoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new memoryDTO, or with status {@code 400 (Bad Request)} if the memory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/memories")
    public ResponseEntity<MemoryDTO> createMemory(@Valid @RequestBody MemoryDTO memoryDTO) throws URISyntaxException {
        log.debug("REST request to save Memory : {}", memoryDTO);
        if (memoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new memory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MemoryDTO result = memoryService.save(memoryDTO);
        return ResponseEntity.created(new URI("/api/memories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /memories} : Updates an existing memory.
     *
     * @param memoryDTO the memoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memoryDTO,
     * or with status {@code 400 (Bad Request)} if the memoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the memoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/memories")
    public ResponseEntity<MemoryDTO> updateMemory(@Valid @RequestBody MemoryDTO memoryDTO) throws URISyntaxException {
        log.debug("REST request to update Memory : {}", memoryDTO);
        if (memoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MemoryDTO result = memoryService.save(memoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /memories} : get all the memories.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of memories in body.
     */
    @GetMapping("/memories")
    public ResponseEntity<List<MemoryDTO>> getAllMemories(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Memories");
        Page<MemoryDTO> page;
        if (eagerload) {
            page = memoryService.findAllWithEagerRelationships(pageable);
        } else {
            page = memoryService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /memories/:id} : get the "id" memory.
     *
     * @param id the id of the memoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the memoryDTO, or with status {@code 404 (Not Found)}.
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
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
