package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.WorkOrdersAdminRelation;
import com.fps.repository.WorkOrdersAdminRelationRepository;
import com.fps.elastics.search.WorkOrdersAdminRelationSearchRepository;
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
 * REST controller for managing WorkOrdersAdminRelation.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WorkOrdersAdminRelationResource {

    private final Logger log = LoggerFactory.getLogger(WorkOrdersAdminRelationResource.class);

    @Inject
    private WorkOrdersAdminRelationRepository workOrdersAdminRelationRepository;

    @Inject
    private WorkOrdersAdminRelationSearchRepository workOrdersAdminRelationSearchRepository;

    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    final static private String MASTER ="master";
    final static private String SLAVE ="slave";
    /**
     * POST  /work-orders-admin-relations : Create a new workOrdersAdminRelation.
     *
     * @param workOrdersAdminRelation the workOrdersAdminRelation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workOrdersAdminRelation, or with status 400 (Bad Request) if the workOrdersAdminRelation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/work-orders-admin-relations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkOrdersAdminRelation> createWorkOrdersAdminRelation(@RequestBody WorkOrdersAdminRelation workOrdersAdminRelation) throws URISyntaxException {
        log.debug("REST request to save WorkOrdersAdminRelation : {}", workOrdersAdminRelation);
        if (workOrdersAdminRelation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("workOrdersAdminRelation", "idexists", "A new workOrdersAdminRelation cannot already have an ID")).body(null);
        }
        currentTenantIdentifierResolver.setTenant(MASTER);
        WorkOrdersAdminRelation result = workOrdersAdminRelationRepository.save(workOrdersAdminRelation);
        workOrdersAdminRelationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/work-orders-admin-relations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("workOrdersAdminRelation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /work-orders-admin-relations : Updates an existing workOrdersAdminRelation.
     *
     * @param workOrdersAdminRelation the workOrdersAdminRelation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workOrdersAdminRelation,
     * or with status 400 (Bad Request) if the workOrdersAdminRelation is not valid,
     * or with status 500 (Internal Server Error) if the workOrdersAdminRelation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/work-orders-admin-relations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkOrdersAdminRelation> updateWorkOrdersAdminRelation(@RequestBody WorkOrdersAdminRelation workOrdersAdminRelation) throws URISyntaxException {
        log.debug("REST request to update WorkOrdersAdminRelation : {}", workOrdersAdminRelation);
        if (workOrdersAdminRelation.getId() == null) {
            return createWorkOrdersAdminRelation(workOrdersAdminRelation);
        }
        currentTenantIdentifierResolver.setTenant(MASTER);
        WorkOrdersAdminRelation result = workOrdersAdminRelationRepository.save(workOrdersAdminRelation);

        workOrdersAdminRelationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("workOrdersAdminRelation", workOrdersAdminRelation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /work-orders-admin-relations : get all the workOrdersAdminRelations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of workOrdersAdminRelations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/work-orders-admin-relations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<WorkOrdersAdminRelation>> getAllWorkOrdersAdminRelations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of WorkOrdersAdminRelations");
        currentTenantIdentifierResolver.setTenant(SLAVE);
        Page<WorkOrdersAdminRelation> page = workOrdersAdminRelationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/work-orders-admin-relations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /work-orders-admin-relations/:id : get the "id" workOrdersAdminRelation.
     *
     * @param id the id of the workOrdersAdminRelation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workOrdersAdminRelation, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/work-orders-admin-relations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkOrdersAdminRelation> getWorkOrdersAdminRelation(@PathVariable Long id) {
        log.debug("REST request to get WorkOrdersAdminRelation : {}", id);
        currentTenantIdentifierResolver.setTenant(SLAVE);
        WorkOrdersAdminRelation workOrdersAdminRelation = workOrdersAdminRelationRepository.findOne(id);
        return Optional.ofNullable(workOrdersAdminRelation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /work-orders-admin-relations/:id : delete the "id" workOrdersAdminRelation.
     *
     * @param id the id of the workOrdersAdminRelation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/work-orders-admin-relations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWorkOrdersAdminRelation(@PathVariable Long id) {
        log.debug("REST request to delete WorkOrdersAdminRelation : {}", id);
        currentTenantIdentifierResolver.setTenant(MASTER);
        workOrdersAdminRelationRepository.delete(id);
        workOrdersAdminRelationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("workOrdersAdminRelation", id.toString())).build();
    }

    /**
     * SEARCH  /_search/work-orders-admin-relations?query=:query : search for the workOrdersAdminRelation corresponding
     * to the query.
     *
     * @param query the query of the workOrdersAdminRelation search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/work-orders-admin-relations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<WorkOrdersAdminRelation>> searchWorkOrdersAdminRelations(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of WorkOrdersAdminRelations for query {}", query);
        Page<WorkOrdersAdminRelation> page = workOrdersAdminRelationSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/work-orders-admin-relations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
