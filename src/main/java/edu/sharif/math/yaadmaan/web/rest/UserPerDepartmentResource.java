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
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.sharif.math.yaadmaan.security.AuthoritiesConstants;
import edu.sharif.math.yaadmaan.service.UserPerDepartmentService;
import edu.sharif.math.yaadmaan.service.dto.UserPerDepartmentDTO;
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
 * REST controller for managing {@link edu.sharif.math.yaadmaan.domain.UserPerDepartment}.
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

    public UserPerDepartmentResource(UserPerDepartmentService userPerDepartmentService) {
        this.userPerDepartmentService = userPerDepartmentService;
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
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userPerDepartments in body.
     */
    @GetMapping("/user-per-departments")
    public ResponseEntity<List<UserPerDepartmentDTO>> getAllUserPerDepartments(Pageable pageable) {
        log.debug("REST request to get a page of UserPerDepartments");
        Page<UserPerDepartmentDTO> page = userPerDepartmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
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
