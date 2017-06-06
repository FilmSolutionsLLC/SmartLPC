package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.Captions;
import com.fps.repository.CaptionsRepository;
import com.fps.elastics.search.CaptionsSearchRepository;
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
 * REST controller for managing Captions.
 */
@RestController
@RequestMapping("/api")
public class CaptionsResource {

    private final Logger log = LoggerFactory.getLogger(CaptionsResource.class);

    @Inject
    private CaptionsRepository captionsRepository;

    @Inject
    private CaptionsSearchRepository captionsSearchRepository;

    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    final static private String MASTER ="master";
    final static private String SLAVE ="slave";

    /**
     * POST  /captions : Create a new captions.
     *
     * @param captions the captions to create
     * @return the ResponseEntity with status 201 (Created) and with body the new captions, or with status 400 (Bad Request) if the captions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/captions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Captions> createCaptions(@RequestBody Captions captions) throws URISyntaxException {
        log.debug("REST request to save Captions : {}", captions);
        if (captions.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("captions", "idexists", "A new captions cannot already have an ID")).body(null);
        }
        currentTenantIdentifierResolver.setTenant(MASTER);

        Captions result = captionsRepository.save(captions);
        captionsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/captions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("captions", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /captions : Updates an existing captions.
     *
     * @param captions the captions to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated captions,
     * or with status 400 (Bad Request) if the captions is not valid,
     * or with status 500 (Internal Server Error) if the captions couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/captions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Captions> updateCaptions(@RequestBody Captions captions) throws URISyntaxException {
        log.debug("REST request to update Captions : {}", captions);
        if (captions.getId() == null) {
            return createCaptions(captions);
        }
        currentTenantIdentifierResolver.setTenant(MASTER);

        Captions result = captionsRepository.save(captions);
        captionsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("captions", captions.getId().toString()))
            .body(result);
    }

    /**
     * GET  /captions : get all the captions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of captions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/captions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Captions>> getAllCaptions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Captions");
        currentTenantIdentifierResolver.setTenant(SLAVE);

        Page<Captions> page = captionsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/captions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /captions/:id : get the "id" captions.
     *
     * @param id the id of the captions to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the captions, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/captions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Captions> getCaptions(@PathVariable Long id) {
        log.debug("REST request to get Captions : {}", id);
        currentTenantIdentifierResolver.setTenant(SLAVE);

        Captions captions = captionsRepository.findOne(id);
        return Optional.ofNullable(captions)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /captions/:id : delete the "id" captions.
     *
     * @param id the id of the captions to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/captions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCaptions(@PathVariable Long id) {
        log.debug("REST request to delete Captions : {}", id);
        currentTenantIdentifierResolver.setTenant(MASTER);

        captionsRepository.delete(id);
        captionsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("captions", id.toString())).build();
    }

    /**
     * SEARCH  /_search/captions?query=:query : search for the captions corresponding
     * to the query.
     *
     * @param query the query of the captions search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/captions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Captions>> searchCaptions(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Captions for query {}", query);
        Page<Captions> page = captionsSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/captions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
