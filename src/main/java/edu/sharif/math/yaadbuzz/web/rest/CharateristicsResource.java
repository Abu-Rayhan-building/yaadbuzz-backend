package edu.sharif.math.yaadbuzz.web.rest;

import edu.sharif.math.yaadbuzz.security.AuthoritiesConstants;
import edu.sharif.math.yaadbuzz.service.CharateristicsQueryService;
import edu.sharif.math.yaadbuzz.service.CharateristicsService;
import edu.sharif.math.yaadbuzz.service.dto.CharateristicsCriteria;
import edu.sharif.math.yaadbuzz.service.dto.CharateristicsDTO;
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
 * REST controller for managing
 * {@link edu.sharif.math.yaadbuzz.domain.Charateristics}.
 */
@RestController
@RequestMapping("/api")
@RolesAllowed(AuthoritiesConstants.ADMIN)
public class CharateristicsResource {

    private final Logger log = LoggerFactory.getLogger(CharateristicsResource.class);

    private static final String ENTITY_NAME = "charateristics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CharateristicsService charateristicsService;

    private final CharateristicsQueryService charateristicsQueryService;

    public CharateristicsResource(CharateristicsService charateristicsService, CharateristicsQueryService charateristicsQueryService) {
        this.charateristicsService = charateristicsService;
        this.charateristicsQueryService = charateristicsQueryService;
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
    @PostMapping("/charateristics")
    public ResponseEntity<CharateristicsDTO> createCharateristics(@Valid @RequestBody CharateristicsDTO charateristicsDTO)
        throws URISyntaxException {
        log.debug("REST request to save Charateristics : {}", charateristicsDTO);
        if (charateristicsDTO.getId() != null) {
            throw new BadRequestAlertException("A new charateristics cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CharateristicsDTO result = charateristicsService.save(charateristicsDTO);
        return ResponseEntity
            .created(new URI("/api/charateristics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /charateristics} : Updates an existing charateristics.
     *
     * @param charateristicsDTO the charateristicsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the updated charateristicsDTO, or with status
     *         {@code 400 (Bad Request)} if the charateristicsDTO is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         charateristicsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/charateristics")
    public ResponseEntity<CharateristicsDTO> updateCharateristics(@Valid @RequestBody CharateristicsDTO charateristicsDTO)
        throws URISyntaxException {
        log.debug("REST request to update Charateristics : {}", charateristicsDTO);
        if (charateristicsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CharateristicsDTO result = charateristicsService.save(charateristicsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, charateristicsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /charateristics} : Updates given fields of an existing
     * charateristics.
     *
     * @param charateristicsDTO the charateristicsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the updated charateristicsDTO, or with status
     *         {@code 400 (Bad Request)} if the charateristicsDTO is not valid,
     *         or with status {@code 404 (Not Found)} if the charateristicsDTO
     *         is not found, or with status {@code 500 (Internal Server Error)}
     *         if the charateristicsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/charateristics", consumes = "application/merge-patch+json")
    public ResponseEntity<CharateristicsDTO> partialUpdateCharateristics(@NotNull @RequestBody CharateristicsDTO charateristicsDTO)
        throws URISyntaxException {
        log.debug("REST request to update Charateristics partially : {}", charateristicsDTO);
        if (charateristicsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<CharateristicsDTO> result = charateristicsService.partialUpdate(charateristicsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, charateristicsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /charateristics} : get all the charateristics.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
     *         list of charateristics in body.
     */
    @GetMapping("/charateristics")
    public ResponseEntity<List<CharateristicsDTO>> getAllCharateristics(CharateristicsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Charateristics by criteria: {}", criteria);
        Page<CharateristicsDTO> page = charateristicsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /charateristics/count} : count all the charateristics.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
     *         count in body.
     */
    @GetMapping("/charateristics/count")
    public ResponseEntity<Long> countCharateristics(CharateristicsCriteria criteria) {
        log.debug("REST request to count Charateristics by criteria: {}", criteria);
        return ResponseEntity.ok().body(charateristicsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /charateristics/:id} : get the "id" charateristics.
     *
     * @param id the id of the charateristicsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *         body the charateristicsDTO, or with status
     *         {@code 404 (Not Found)}.
     */
    @GetMapping("/charateristics/{id}")
    public ResponseEntity<CharateristicsDTO> getCharateristics(@PathVariable Long id) {
        log.debug("REST request to get Charateristics : {}", id);
        Optional<CharateristicsDTO> charateristicsDTO = charateristicsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(charateristicsDTO);
    }

    /**
     * {@code DELETE  /charateristics/:id} : delete the "id" charateristics.
     *
     * @param id the id of the charateristicsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/charateristics/{id}")
    public ResponseEntity<Void> deleteCharateristics(@PathVariable Long id) {
        log.debug("REST request to delete Charateristics : {}", id);
        charateristicsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
