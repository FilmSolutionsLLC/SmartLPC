package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.WorkOrderAbcHdd;
import com.fps.repository.WorkOrderAbcHddRepository;
import com.fps.elastics.search.WorkOrderAbcHddSearchRepository;
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
 * REST controller for managing WorkOrderAbcHdd.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WorkOrderAbcHddResource {

    private final Logger log = LoggerFactory.getLogger(WorkOrderAbcHddResource.class);

    @Inject
    private WorkOrderAbcHddRepository workOrderAbcHddRepository;

    @Inject
    private WorkOrderAbcHddSearchRepository workOrderAbcHddSearchRepository;

    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    final static private String MASTER ="master";
    final static private String SLAVE ="slave";
    /**
     * POST  /work-order-abc-hdds : Create a new workOrderAbcHdd.
     *
     * @param workOrderAbcHdd the workOrderAbcHdd to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workOrderAbcHdd, or with status 400 (Bad Request) if the workOrderAbcHdd has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/work-order-abc-hdds",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkOrderAbcHdd> createWorkOrderAbcHdd(@RequestBody WorkOrderAbcHdd workOrderAbcHdd) throws URISyntaxException {
        log.debug("REST request to save WorkOrderAbcHdd : {}", workOrderAbcHdd);
        if (workOrderAbcHdd.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("workOrderAbcHdd", "idexists", "A new workOrderAbcHdd cannot already have an ID")).body(null);
        }
        currentTenantIdentifierResolver.setTenant(MASTER);
        WorkOrderAbcHdd result = workOrderAbcHddRepository.save(workOrderAbcHdd);
        workOrderAbcHddSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/work-order-abc-hdds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("workOrderAbcHdd", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /work-order-abc-hdds : Updates an existing workOrderAbcHdd.
     *
     * @param workOrderAbcHdd the workOrderAbcHdd to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workOrderAbcHdd,
     * or with status 400 (Bad Request) if the workOrderAbcHdd is not valid,
     * or with status 500 (Internal Server Error) if the workOrderAbcHdd couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/work-order-abc-hdds",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkOrderAbcHdd> updateWorkOrderAbcHdd(@RequestBody WorkOrderAbcHdd workOrderAbcHdd) throws URISyntaxException {
        log.debug("REST request to update WorkOrderAbcHdd : {}", workOrderAbcHdd);
        if (workOrderAbcHdd.getId() == null) {
            return createWorkOrderAbcHdd(workOrderAbcHdd);
        }
        currentTenantIdentifierResolver.setTenant(MASTER);
        WorkOrderAbcHdd result = workOrderAbcHddRepository.save(workOrderAbcHdd);
        workOrderAbcHddSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("workOrderAbcHdd", workOrderAbcHdd.getId().toString()))
            .body(result);
    }

    /**
     * GET  /work-order-abc-hdds : get all the workOrderAbcHdds.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of workOrderAbcHdds in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/work-order-abc-hdds",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<WorkOrderAbcHdd>> getAllWorkOrderAbcHdds(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of WorkOrderAbcHdds");
        currentTenantIdentifierResolver.setTenant(SLAVE);
        Page<WorkOrderAbcHdd> page = workOrderAbcHddRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/work-order-abc-hdds");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /work-order-abc-hdds/:id : get the "id" workOrderAbcHdd.
     *
     * @param id the id of the workOrderAbcHdd to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workOrderAbcHdd, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/work-order-abc-hdds/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkOrderAbcHdd> getWorkOrderAbcHdd(@PathVariable Long id) {
        log.debug("REST request to get WorkOrderAbcHdd : {}", id);
        currentTenantIdentifierResolver.setTenant(SLAVE);
        WorkOrderAbcHdd workOrderAbcHdd = workOrderAbcHddRepository.findOne(id);
        return Optional.ofNullable(workOrderAbcHdd)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /work-order-abc-hdds/:id : delete the "id" workOrderAbcHdd.
     *
     * @param id the id of the workOrderAbcHdd to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/work-order-abc-hdds/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWorkOrderAbcHdd(@PathVariable Long id) {
        log.debug("REST request to delete WorkOrderAbcHdd : {}", id);
        currentTenantIdentifierResolver.setTenant(MASTER);
        workOrderAbcHddRepository.delete(id);
        workOrderAbcHddSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("workOrderAbcHdd", id.toString())).build();
    }

    /**
     * SEARCH  /_search/work-order-abc-hdds?query=:query : search for the workOrderAbcHdd corresponding
     * to the query.
     *
     * @param query the query of the workOrderAbcHdd search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/work-order-abc-hdds",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<WorkOrderAbcHdd>> searchWorkOrderAbcHdds(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of WorkOrderAbcHdds for query {}", query);
        Page<WorkOrderAbcHdd> page = workOrderAbcHddSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/work-order-abc-hdds");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
