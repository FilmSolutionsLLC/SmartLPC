package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.Batch;
import com.fps.repository.BatchRepository;
import com.fps.elastics.search.BatchSearchRepository;
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
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Batch.
 */
@RestController
@RequestMapping("/api")
public class BatchResource {

    private final Logger log = LoggerFactory.getLogger(BatchResource.class);

    @Inject
    private BatchRepository batchRepository;

    @Inject
    private BatchSearchRepository batchSearchRepository;

    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    final static private String MASTER ="master";
    final static private String SLAVE ="slave";

    /**
     * POST  /batches : Create a new batch.
     *
     * @param batch the batch to create
     * @return the ResponseEntity with status 201 (Created) and with body the new batch, or with status 400 (Bad Request) if the batch has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/batches",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Batch> createBatch(@RequestBody Batch batch) throws URISyntaxException {
        log.debug("REST request to save Batch : {}", batch);
        if (batch.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("batch", "idexists", "A new batch cannot already have an ID")).body(null);
        }
        currentTenantIdentifierResolver.setTenant(MASTER);
        Batch result = batchRepository.save(batch);
        batchSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/batches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("batch", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /batches : Updates an existing batch.
     *
     * @param batch the batch to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated batch,
     * or with status 400 (Bad Request) if the batch is not valid,
     * or with status 500 (Internal Server Error) if the batch couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/batches",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Batch> updateBatch(@RequestBody Batch batch) throws URISyntaxException {
        log.debug("REST request to update Batch : {}", batch);
        if (batch.getId() == null) {
            return createBatch(batch);
        }
        currentTenantIdentifierResolver.setTenant(MASTER);
        Batch result = batchRepository.save(batch);
        batchSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("batch", batch.getId().toString()))
            .body(result);
    }

    /**
     * GET  /batches : get all the batches.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of batches in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/batches",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Batch>> getAllBatches(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Batches");
        currentTenantIdentifierResolver.setTenant(SLAVE);
        Page<Batch> page = batchRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/batches");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /batches/:id : get the "id" batch.
     *
     * @param id the id of the batch to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the batch, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/batches/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Batch> getBatch(@PathVariable Long id) {
        log.debug("REST request to get Batch : {}", id);
        currentTenantIdentifierResolver.setTenant(SLAVE);
        Batch batch = batchRepository.findOne(id);
        return Optional.ofNullable(batch)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /batches/:id : delete the "id" batch.
     *
     * @param id the id of the batch to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/batches/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBatch(@PathVariable Long id) {
        log.debug("REST request to delete Batch : {}", id);
        currentTenantIdentifierResolver.setTenant(MASTER);
        batchRepository.delete(id);
        batchSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("batch", id.toString())).build();
    }

    /**
     * SEARCH  /_search/batches?query=:query : search for the batch corresponding
     * to the query.
     *
     * @param query the query of the batch search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/batches",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Batch>> searchBatches(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Batches for query {}", query);
        Page<Batch> page = batchSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/batches");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
