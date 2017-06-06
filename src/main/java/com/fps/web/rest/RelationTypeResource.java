package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.RelationType;
import com.fps.repository.RelationTypeRepository;
import com.fps.elastics.search.RelationTypeSearchRepository;
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

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing RelationType.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RelationTypeResource {

    private final Logger log = LoggerFactory.getLogger(RelationTypeResource.class);

    @Inject
    private RelationTypeRepository relationTypeRepository;

    @Inject
    private RelationTypeSearchRepository relationTypeSearchRepository;

    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    final static private String MASTER = "master";
    final static private String SLAVE = "slave";


    /**
     * POST  /relation-types : Create a new relationType.
     *
     * @param relationType the relationType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new relationType, or with status 400 (Bad Request) if the relationType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/relation-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RelationType> createRelationType(@RequestBody RelationType relationType) throws URISyntaxException {
        log.debug("REST request to save RelationType : {}", relationType);
        if (relationType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("relationType", "idexists", "A new relationType cannot already have an ID")).body(null);
        }
        currentTenantIdentifierResolver.setTenant(MASTER);
        RelationType result = relationTypeRepository.save(relationType);
        relationTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/relation-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("relationType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /relation-types : Updates an existing relationType.
     *
     * @param relationType the relationType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated relationType,
     * or with status 400 (Bad Request) if the relationType is not valid,
     * or with status 500 (Internal Server Error) if the relationType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/relation-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RelationType> updateRelationType(@RequestBody RelationType relationType) throws URISyntaxException {
        log.debug("REST request to update RelationType : {}", relationType);
        if (relationType.getId() == null) {
            return createRelationType(relationType);
        }
        currentTenantIdentifierResolver.setTenant(MASTER);
        RelationType result = relationTypeRepository.save(relationType);
        relationTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("relationType", relationType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /relation-types : get all the relationTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of relationTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/relation-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<RelationType>> getAllRelationTypes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of RelationTypes");
        currentTenantIdentifierResolver.setTenant(SLAVE);
        Page<RelationType> page = relationTypeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/relation-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /relation-types/:id : get the "id" relationType.
     *
     * @param id the id of the relationType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the relationType, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/relation-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RelationType> getRelationType(@PathVariable Long id) {
        log.debug("REST request to get RelationType : {}", id);
        currentTenantIdentifierResolver.setTenant(SLAVE);
        RelationType relationType = relationTypeRepository.findOne(id);
        return Optional.ofNullable(relationType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /relation-types/:id : delete the "id" relationType.
     *
     * @param id the id of the relationType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/relation-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRelationType(@PathVariable Long id) {
        log.debug("REST request to delete RelationType : {}", id);
        currentTenantIdentifierResolver.setTenant(MASTER);
        relationTypeRepository.delete(id);
        relationTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("relationType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/relation-types?query=:query : search for the relationType corresponding
     * to the query.
     *
     * @param query the query of the relationType search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/relation-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<RelationType>> searchRelationTypes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of RelationTypes for query {}", query);
        Page<RelationType> page = relationTypeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/relation-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
