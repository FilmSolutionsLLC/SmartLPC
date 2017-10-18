package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.Storage_Servers;
import com.fps.repository.Storage_ServersRepository;
import com.fps.elastics.search.Storage_ServersSearchRepository;
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
 * REST controller for managing Storage_Servers.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class Storage_ServersResource {

    private final Logger log = LoggerFactory.getLogger(Storage_ServersResource.class);

    @Inject
    private Storage_ServersRepository storage_ServersRepository;

    @Inject
    private Storage_ServersSearchRepository storage_ServersSearchRepository;

    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    final static private String MASTER ="master";
    final static private String SLAVE ="slave";


    /**
     * POST  /storage-servers : Create a new storage_Servers.
     *
     * @param storage_Servers the storage_Servers to create
     * @return the ResponseEntity with status 201 (Created) and with body the new storage_Servers, or with status 400 (Bad Request) if the storage_Servers has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/storage-servers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Storage_Servers> createStorage_Servers(@RequestBody Storage_Servers storage_Servers) throws URISyntaxException {
        log.debug("REST request to save Storage_Servers : {}", storage_Servers);
        if (storage_Servers.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("storage_Servers", "idexists", "A new storage_Servers cannot already have an ID")).body(null);
        }
        currentTenantIdentifierResolver.setTenant(MASTER);
        Storage_Servers result = storage_ServersRepository.save(storage_Servers);
        storage_ServersSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/storage-servers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("storage_Servers", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /storage-servers : Updates an existing storage_Servers.
     *
     * @param storage_Servers the storage_Servers to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated storage_Servers,
     * or with status 400 (Bad Request) if the storage_Servers is not valid,
     * or with status 500 (Internal Server Error) if the storage_Servers couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/storage-servers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Storage_Servers> updateStorage_Servers(@RequestBody Storage_Servers storage_Servers) throws URISyntaxException {
        log.debug("REST request to update Storage_Servers : {}", storage_Servers);
        if (storage_Servers.getId() == null) {
            return createStorage_Servers(storage_Servers);
        }
        currentTenantIdentifierResolver.setTenant(MASTER);
        Storage_Servers result = storage_ServersRepository.save(storage_Servers);
        storage_ServersSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("storage_Servers", storage_Servers.getId().toString()))
            .body(result);
    }

    /**
     * GET  /storage-servers : get all the storage_Servers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of storage_Servers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/storage-servers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Storage_Servers>> getAllStorage_Servers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Storage_Servers");
        currentTenantIdentifierResolver.setTenant(SLAVE);
        Page<Storage_Servers> page = storage_ServersRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/storage-servers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /storage-servers/:id : get the "id" storage_Servers.
     *
     * @param id the id of the storage_Servers to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the storage_Servers, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/storage-servers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Storage_Servers> getStorage_Servers(@PathVariable Long id) {
        log.debug("REST request to get Storage_Servers : {}", id);
        currentTenantIdentifierResolver.setTenant(SLAVE);
        Storage_Servers storage_Servers = storage_ServersRepository.findOne(id);
        return Optional.ofNullable(storage_Servers)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /storage-servers/:id : delete the "id" storage_Servers.
     *
     * @param id the id of the storage_Servers to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/storage-servers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStorage_Servers(@PathVariable Long id) {
        log.debug("REST request to delete Storage_Servers : {}", id);
        currentTenantIdentifierResolver.setTenant(MASTER);

        storage_ServersRepository.delete(id);
        storage_ServersSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("storage_Servers", id.toString())).build();
    }

    /**
     * SEARCH  /_search/storage-servers?query=:query : search for the storage_Servers corresponding
     * to the query.
     *
     * @param query the query of the storage_Servers search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/storage-servers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Storage_Servers>> searchStorage_Servers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Storage_Servers for query {}", query);
        Page<Storage_Servers> page = storage_ServersSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/storage-servers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
