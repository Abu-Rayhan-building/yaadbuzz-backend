package edu.sharif.math.yaadbuzz.web.rest;

import edu.sharif.math.yaadbuzz.service.MemorialService;
import edu.sharif.math.yaadbuzz.web.rest.errors.BadRequestAlertException;
import edu.sharif.math.yaadbuzz.service.dto.MemorialDTO;
import edu.sharif.math.yaadbuzz.service.dto.MemorialCriteria;
import edu.sharif.math.yaadbuzz.security.AuthoritiesConstants;
import edu.sharif.math.yaadbuzz.service.MemorialQueryService;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link edu.sharif.math.yaadbuzz.domain.Memorial}.
 */
@RestController
@RequestMapping("/api")
@RolesAllowed(AuthoritiesConstants.ADMIN)
public class MemorialResource {

    private final Logger log = LoggerFactory.getLogger(MemorialResource.class);

    private static final String ENTITY_NAME = "memorial";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MemorialService memorialService;

    private final MemorialQueryService memorialQueryService;

    public MemorialResource(MemorialService memorialService, MemorialQueryService memorialQueryService) {
        this.memorialService = memorialService;
        this.memorialQueryService = memorialQueryService;
    }

    /**
     * {@code POST  /memorials} : Create a new memorial.
     *
     * @param memorialDTO the memorialDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new memorialDTO, or with status {@code 400 (Bad Request)} if the memorial has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/memorials")
    public ResponseEntity<MemorialDTO> createMemorial(@Valid @RequestBody MemorialDTO memorialDTO) throws URISyntaxException {
        log.debug("REST request to save Memorial : {}", memorialDTO);
        if (memorialDTO.getId() != null) {
            throw new BadRequestAlertException("A new memorial cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MemorialDTO result = memorialService.save(memorialDTO);
        return ResponseEntity.created(new URI("/api/memorials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /memorials} : Updates an existing memorial.
     *
     * @param memorialDTO the memorialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memorialDTO,
     * or with status {@code 400 (Bad Request)} if the memorialDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the memorialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/memorials")
    public ResponseEntity<MemorialDTO> updateMemorial(@Valid @RequestBody MemorialDTO memorialDTO) throws URISyntaxException {
        log.debug("REST request to update Memorial : {}", memorialDTO);
        if (memorialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MemorialDTO result = memorialService.save(memorialDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memorialDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /memorials} : get all the memorials.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of memorials in body.
     */
    @GetMapping("/memorials")
    public ResponseEntity<List<MemorialDTO>> getAllMemorials(MemorialCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Memorials by criteria: {}", criteria);
        Page<MemorialDTO> page = memorialQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /memorials/count} : count all the memorials.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/memorials/count")
    public ResponseEntity<Long> countMemorials(MemorialCriteria criteria) {
        log.debug("REST request to count Memorials by criteria: {}", criteria);
        return ResponseEntity.ok().body(memorialQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /memorials/:id} : get the "id" memorial.
     *
     * @param id the id of the memorialDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the memorialDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/memorials/{id}")
    public ResponseEntity<MemorialDTO> getMemorial(@PathVariable Long id) {
        log.debug("REST request to get Memorial : {}", id);
        Optional<MemorialDTO> memorialDTO = memorialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(memorialDTO);
    }

    /**
     * {@code DELETE  /memorials/:id} : delete the "id" memorial.
     *
     * @param id the id of the memorialDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/memorials/{id}")
    public ResponseEntity<Void> deleteMemorial(@PathVariable Long id) {
        log.debug("REST request to delete Memorial : {}", id);
        memorialService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
