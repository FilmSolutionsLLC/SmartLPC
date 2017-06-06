package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.WorkOrderAbcFile;
import com.fps.repository.WorkOrderAbcFileRepository;
import com.fps.elastics.search.WorkOrderAbcFileSearchRepository;
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
 * REST controller for managing WorkOrderAbcFile.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WorkOrderAbcFileResource {

    private final Logger log = LoggerFactory.getLogger(WorkOrderAbcFileResource.class);

    @Inject
    private WorkOrderAbcFileRepository workOrderAbcFileRepository;

    @Inject
    private WorkOrderAbcFileSearchRepository workOrderAbcFileSearchRepository;

    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    final static private String MASTER ="master";
    final static private String SLAVE ="slave";
    /**
     * POST  /work-order-abc-files : Create a new workOrderAbcFile.
     *
     * @param workOrderAbcFile the workOrderAbcFile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workOrderAbcFile, or with status 400 (Bad Request) if the workOrderAbcFile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/work-order-abc-files",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkOrderAbcFile> createWorkOrderAbcFile(@RequestBody WorkOrderAbcFile workOrderAbcFile) throws URISyntaxException {
        log.debug("REST request to save WorkOrderAbcFile : {}", workOrderAbcFile);
        if (workOrderAbcFile.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("workOrderAbcFile", "idexists", "A new workOrderAbcFile cannot already have an ID")).body(null);
        }
        currentTenantIdentifierResolver.setTenant(MASTER);
        WorkOrderAbcFile result = workOrderAbcFileRepository.save(workOrderAbcFile);
        workOrderAbcFileSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/work-order-abc-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("workOrderAbcFile", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /work-order-abc-files : Updates an existing workOrderAbcFile.
     *
     * @param workOrderAbcFile the workOrderAbcFile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workOrderAbcFile,
     * or with status 400 (Bad Request) if the workOrderAbcFile is not valid,
     * or with status 500 (Internal Server Error) if the workOrderAbcFile couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/work-order-abc-files",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkOrderAbcFile> updateWorkOrderAbcFile(@RequestBody WorkOrderAbcFile workOrderAbcFile) throws URISyntaxException {
        log.debug("REST request to update WorkOrderAbcFile : {}", workOrderAbcFile);
        if (workOrderAbcFile.getId() == null) {
            return createWorkOrderAbcFile(workOrderAbcFile);
        }
        currentTenantIdentifierResolver.setTenant(MASTER);
        WorkOrderAbcFile result = workOrderAbcFileRepository.save(workOrderAbcFile);
        workOrderAbcFileSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("workOrderAbcFile", workOrderAbcFile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /work-order-abc-files : get all the workOrderAbcFiles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of workOrderAbcFiles in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/work-order-abc-files",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<WorkOrderAbcFile>> getAllWorkOrderAbcFiles(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of WorkOrderAbcFiles");
        currentTenantIdentifierResolver.setTenant(SLAVE);
        Page<WorkOrderAbcFile> page = workOrderAbcFileRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/work-order-abc-files");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /work-order-abc-files/:id : get the "id" workOrderAbcFile.
     *
     * @param id the id of the workOrderAbcFile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workOrderAbcFile, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/work-order-abc-files/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkOrderAbcFile> getWorkOrderAbcFile(@PathVariable Long id) {
        log.debug("REST request to get WorkOrderAbcFile : {}", id);
        currentTenantIdentifierResolver.setTenant(SLAVE);
        WorkOrderAbcFile workOrderAbcFile = workOrderAbcFileRepository.findOne(id);
        return Optional.ofNullable(workOrderAbcFile)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /work-order-abc-files/:id : delete the "id" workOrderAbcFile.
     *
     * @param id the id of the workOrderAbcFile to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/work-order-abc-files/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWorkOrderAbcFile(@PathVariable Long id) {
        log.debug("REST request to delete WorkOrderAbcFile : {}", id);
        currentTenantIdentifierResolver.setTenant(MASTER);
        workOrderAbcFileRepository.delete(id);
        workOrderAbcFileSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("workOrderAbcFile", id.toString())).build();
    }

    /**
     * SEARCH  /_search/work-order-abc-files?query=:query : search for the workOrderAbcFile corresponding
     * to the query.
     *
     * @param query the query of the workOrderAbcFile search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/work-order-abc-files",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<WorkOrderAbcFile>> searchWorkOrderAbcFiles(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of WorkOrderAbcFiles for query {}", query);
        Page<WorkOrderAbcFile> page = workOrderAbcFileSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/work-order-abc-files");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
