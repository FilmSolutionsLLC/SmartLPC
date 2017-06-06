package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.ContactRelationships;
import com.fps.repository.ContactRelationshipsRepository;
import com.fps.elastics.search.ContactRelationshipsSearchRepository;
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
 * REST controller for managing ContactRelationships.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ContactRelationshipsResource {

    private final Logger log = LoggerFactory.getLogger(ContactRelationshipsResource.class);

    @Inject
    private ContactRelationshipsRepository contactRelationshipsRepository;

    @Inject
    private ContactRelationshipsSearchRepository contactRelationshipsSearchRepository;

    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    final static private String MASTER ="master";
    final static private String SLAVE ="slave";

    /**
     * POST  /contact-relationships : Create a new contactRelationships.
     *
     * @param contactRelationships the contactRelationships to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contactRelationships, or with status 400 (Bad Request) if the contactRelationships has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/contact-relationships",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContactRelationships> createContactRelationships(@RequestBody ContactRelationships contactRelationships) throws URISyntaxException {
        log.debug("REST request to save ContactRelationships : {}", contactRelationships);
        if (contactRelationships.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contactRelationships", "idexists", "A new contactRelationships cannot already have an ID")).body(null);
        }
        currentTenantIdentifierResolver.setTenant(MASTER);
        ContactRelationships result = contactRelationshipsRepository.save(contactRelationships);
        contactRelationshipsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/contact-relationships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("contactRelationships", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contact-relationships : Updates an existing contactRelationships.
     *
     * @param contactRelationships the contactRelationships to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contactRelationships,
     * or with status 400 (Bad Request) if the contactRelationships is not valid,
     * or with status 500 (Internal Server Error) if the contactRelationships couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/contact-relationships",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContactRelationships> updateContactRelationships(@RequestBody ContactRelationships contactRelationships) throws URISyntaxException {
        log.debug("REST request to update ContactRelationships : {}", contactRelationships);
        if (contactRelationships.getId() == null) {
            return createContactRelationships(contactRelationships);
        }
        currentTenantIdentifierResolver.setTenant(MASTER);
        ContactRelationships result = contactRelationshipsRepository.save(contactRelationships);
        contactRelationshipsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("contactRelationships", contactRelationships.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contact-relationships : get all the contactRelationships.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contactRelationships in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/contact-relationships",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ContactRelationships>> getAllContactRelationships(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ContactRelationships");
        currentTenantIdentifierResolver.setTenant(SLAVE);
        Page<ContactRelationships> page = contactRelationshipsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contact-relationships");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contact-relationships/:id : get the "id" contactRelationships.
     *
     * @param id the id of the contactRelationships to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contactRelationships, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/contact-relationships/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContactRelationships> getContactRelationships(@PathVariable Long id) {
        log.debug("REST request to get ContactRelationships : {}", id);
        currentTenantIdentifierResolver.setTenant(SLAVE);
        ContactRelationships contactRelationships = contactRelationshipsRepository.findOne(id);
        return Optional.ofNullable(contactRelationships)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /contact-relationships/:id : delete the "id" contactRelationships.
     *
     * @param id the id of the contactRelationships to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/contact-relationships/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteContactRelationships(@PathVariable Long id) {
        log.debug("REST request to delete ContactRelationships : {}", id);
        currentTenantIdentifierResolver.setTenant(MASTER);
        contactRelationshipsRepository.delete(id);
        contactRelationshipsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("contactRelationships", id.toString())).build();
    }

    /**
     * SEARCH  /_search/contact-relationships?query=:query : search for the contactRelationships corresponding
     * to the query.
     *
     * @param query the query of the contactRelationships search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/contact-relationships",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ContactRelationships>> searchContactRelationships(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ContactRelationships for query {}", query);
        Page<ContactRelationships> page = contactRelationshipsSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/contact-relationships");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
