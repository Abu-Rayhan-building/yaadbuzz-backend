package edu.sharif.math.yaadbuzz.web.rest;

import edu.sharif.math.yaadbuzz.service.UserPerDepartmentService;
import edu.sharif.math.yaadbuzz.web.rest.errors.BadRequestAlertException;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentCriteria;
import edu.sharif.math.yaadbuzz.service.UserPerDepartmentQueryService;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.sharif.math.yaadbuzz.security.AuthoritiesConstants;
import edu.sharif.math.yaadbuzz.service.UserPerDepartmentService;
import edu.sharif.math.yaadbuzz.service.dto.UserPerDepartmentDTO;
import edu.sharif.math.yaadbuzz.web.rest.errors.BadRequestAlertException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link edu.sharif.math.yaadbuzz.domain.UserPerDepartment}.
 */
@RestController
@RequestMapping("/api")
@RolesAllowed(AuthoritiesConstants.ADMIN)
public class UserPerDepartmentResource {

    private final Logger log = LoggerFactory.getLogger(UserPerDepartmentResource.class);

    private static final String ENTITY_NAME = "userPerDepartment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserPerDepartmentService userPerDepartmentService;

    private final UserPerDepartmentQueryService userPerDepartmentQueryService;

    public UserPerDepartmentResource(UserPerDepartmentService userPerDepartmentService, UserPerDepartmentQueryService userPerDepartmentQueryService) {
        this.userPerDepartmentService = userPerDepartmentService;
        this.userPerDepartmentQueryService = userPerDepartmentQueryService;
    }

    /**
     * {@code POST  /user-per-departments} : Create a new userPerDepartment.
     *
     * @param userPerDepartmentDTO the userPerDepartmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userPerDepartmentDTO, or with status {@code 400 (Bad Request)} if the userPerDepartment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-per-departments")
    public ResponseEntity<UserPerDepartmentDTO> createUserPerDepartment(@Valid @RequestBody UserPerDepartmentDTO userPerDepartmentDTO) throws URISyntaxException {
        log.debug("REST request to save UserPerDepartment : {}", userPerDepartmentDTO);
        if (userPerDepartmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new userPerDepartment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserPerDepartmentDTO result = userPerDepartmentService.save(userPerDepartmentDTO);
        return ResponseEntity.created(new URI("/api/user-per-departments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-per-departments} : Updates an existing userPerDepartment.
     *
     * @param userPerDepartmentDTO the userPerDepartmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userPerDepartmentDTO,
     * or with status {@code 400 (Bad Request)} if the userPerDepartmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userPerDepartmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-per-departments")
    public ResponseEntity<UserPerDepartmentDTO> updateUserPerDepartment(@Valid @RequestBody UserPerDepartmentDTO userPerDepartmentDTO) throws URISyntaxException {
        log.debug("REST request to update UserPerDepartment : {}", userPerDepartmentDTO);
        if (userPerDepartmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserPerDepartmentDTO result = userPerDepartmentService.save(userPerDepartmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userPerDepartmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-per-departments} : get all the userPerDepartments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userPerDepartments in body.
     */
    @GetMapping("/user-per-departments")
    public ResponseEntity<List<UserPerDepartmentDTO>> getAllUserPerDepartments(UserPerDepartmentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get UserPerDepartments by criteria: {}", criteria);
        Page<UserPerDepartmentDTO> page = userPerDepartmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-per-departments/count} : count all the userPerDepartments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/user-per-departments/count")
    public ResponseEntity<Long> countUserPerDepartments(UserPerDepartmentCriteria criteria) {
        log.debug("REST request to count UserPerDepartments by criteria: {}", criteria);
        return ResponseEntity.ok().body(userPerDepartmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-per-departments/:id} : get the "id" userPerDepartment.
     *
     * @param id the id of the userPerDepartmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userPerDepartmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-per-departments/{id}")
    public ResponseEntity<UserPerDepartmentDTO> getUserPerDepartment(@PathVariable Long id) {
        log.debug("REST request to get UserPerDepartment : {}", id);
        Optional<UserPerDepartmentDTO> userPerDepartmentDTO = userPerDepartmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userPerDepartmentDTO);
    }

    /**
     * {@code DELETE  /user-per-departments/:id} : delete the "id" userPerDepartment.
     *
     * @param id the id of the userPerDepartmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-per-departments/{id}")
    public ResponseEntity<Void> deleteUserPerDepartment(@PathVariable Long id) {
        log.debug("REST request to delete UserPerDepartment : {}", id);
        userPerDepartmentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
