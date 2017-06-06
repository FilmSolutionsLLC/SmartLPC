package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.Departments;
import com.fps.repository.DepartmentsRepository;
import com.fps.elastics.search.DepartmentsSearchRepository;
import com.fps.web.rest.util.HeaderUtil;
import com.fps.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Departments.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DepartmentsResource {

    private final Logger log = LoggerFactory.getLogger(DepartmentsResource.class);

    @Inject
    private DepartmentsRepository departmentsRepository;

    @Inject
    private DepartmentsSearchRepository departmentsSearchRepository;

    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    final static private String MASTER ="master";
    final static private String SLAVE ="slave";


    /**
     * POST  /departments : Create a new departments.
     *
     * @param departments the departments to create
     * @return the ResponseEntity with status 201 (Created) and with body the new departments, or with status 400 (Bad Request) if the departments has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/departments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Departments> createDepartments(@RequestBody Departments departments) throws URISyntaxException {
        log.debug("REST request to save Departments : {}", departments);
        if (departments.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("departments", "idexists", "A new departments cannot already have an ID")).body(null);
        }
        currentTenantIdentifierResolver.setTenant(MASTER);
        Departments result = departmentsRepository.save(departments);
        departmentsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/departments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("departments", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /departments : Updates an existing departments.
     *
     * @param departments the departments to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated departments,
     * or with status 400 (Bad Request) if the departments is not valid,
     * or with status 500 (Internal Server Error) if the departments couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/departments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Departments> updateDepartments(@RequestBody Departments departments) throws URISyntaxException {
        log.debug("REST request to update Departments : {}", departments);
        if (departments.getId() == null) {
            return createDepartments(departments);
        }
        currentTenantIdentifierResolver.setTenant(MASTER);
        Departments result = departmentsRepository.save(departments);
        departmentsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("departments", departments.getId().toString()))
            .body(result);
    }

    /**
     * GET  /departments : get all the departments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of departments in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/departments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Departments>> getAllDepartments(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Departments");
        currentTenantIdentifierResolver.setTenant(SLAVE);
        Page<Departments> page = departmentsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/departments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /departments/:id : get the "id" departments.
     *
     * @param id the id of the departments to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the departments, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/departments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Departments> getDepartments(@PathVariable Long id) {
        log.debug("REST request to get Departments : {}", id);
        currentTenantIdentifierResolver.setTenant(SLAVE);
        Departments departments = departmentsRepository.findOne(id);
        return Optional.ofNullable(departments)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /departments/:id : delete the "id" departments.
     *
     * @param id the id of the departments to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/departments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDepartments(@PathVariable Long id) {
        log.debug("REST request to delete Departments : {}", id);
        currentTenantIdentifierResolver.setTenant(MASTER);
        departmentsRepository.delete(id);
        departmentsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("departments", id.toString())).build();
    }

    /**
     * SEARCH  /_search/departments?query=:query : search for the departments corresponding
     * to the query.
     *
     * @param query the query of the departments search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/departments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Departments>> searchDepartments(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Departments for query {}", query);
        Page<Departments> page = departmentsSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/departments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
