package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.ProjectPurchaseOrders;
import com.fps.domain.Projects;
import com.fps.elastics.search.ProjectPurchaseOrdersSearchRepository;
import com.fps.repository.ProjectPurchaseOrdersRepository;
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
import java.util.Set;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing ProjectPurchaseOrders.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProjectPurchaseOrdersResource {

    private final Logger log = LoggerFactory.getLogger(ProjectPurchaseOrdersResource.class);

    @Inject
    private ProjectPurchaseOrdersRepository projectPurchaseOrdersRepository;

    @Inject
    private ProjectPurchaseOrdersSearchRepository projectPurchaseOrdersSearchRepository;


    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    final static private String MASTER = "master";
    final static private String SLAVE = "slave";

    /**
     * POST  /project-purchase-orders : Create a new projectPurchaseOrders.
     *
     * @param projectPurchaseOrders the projectPurchaseOrders to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectPurchaseOrders, or with status 400 (Bad Request) if the projectPurchaseOrders has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-purchase-orders",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectPurchaseOrders> createProjectPurchaseOrders(@RequestBody ProjectPurchaseOrders projectPurchaseOrders) throws URISyntaxException {
        log.debug("REST request to save ProjectPurchaseOrders : {}", projectPurchaseOrders);
        if (projectPurchaseOrders.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("projectPurchaseOrders", "idexists", "A new projectPurchaseOrders cannot already have an ID")).body(null);
        }
        currentTenantIdentifierResolver.setTenant(MASTER);
        ProjectPurchaseOrders result = projectPurchaseOrdersRepository.save(projectPurchaseOrders);
        projectPurchaseOrdersSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/project-purchase-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("projectPurchaseOrders", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-purchase-orders : Updates an existing projectPurchaseOrders.
     *
     * @param projectPurchaseOrders the projectPurchaseOrders to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectPurchaseOrders,
     * or with status 400 (Bad Request) if the projectPurchaseOrders is not valid,
     * or with status 500 (Internal Server Error) if the projectPurchaseOrders couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-purchase-orders",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectPurchaseOrders> updateProjectPurchaseOrders(@RequestBody ProjectPurchaseOrders projectPurchaseOrders) throws URISyntaxException {
        log.debug("REST request to update ProjectPurchaseOrders : {}", projectPurchaseOrders);
        if (projectPurchaseOrders.getId() == null) {
            return createProjectPurchaseOrders(projectPurchaseOrders);
        }
        currentTenantIdentifierResolver.setTenant(MASTER);
        ProjectPurchaseOrders result = projectPurchaseOrdersRepository.save(projectPurchaseOrders);
        projectPurchaseOrdersSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("projectPurchaseOrders", projectPurchaseOrders.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-purchase-orders : get all the projectPurchaseOrders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of projectPurchaseOrders in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/project-purchase-orders",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ProjectPurchaseOrders>> getAllProjectPurchaseOrders(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ProjectPurchaseOrders");
        currentTenantIdentifierResolver.setTenant(SLAVE);
        Page<ProjectPurchaseOrders> page = projectPurchaseOrdersRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/project-purchase-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /project-purchase-orders/:id : get the "id" projectPurchaseOrders.
     *
     * @param id the id of the projectPurchaseOrders to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectPurchaseOrders, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/project-purchase-orders/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectPurchaseOrders> getProjectPurchaseOrders(@PathVariable Long id) {
        log.debug("REST request to get ProjectPurchaseOrders : {}", id);
        currentTenantIdentifierResolver.setTenant(SLAVE);
        ProjectPurchaseOrders projectPurchaseOrders = projectPurchaseOrdersRepository.findOne(id);
        return Optional.ofNullable(projectPurchaseOrders)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /project-purchase-orders/:id : delete the "id" projectPurchaseOrders.
     *
     * @param id the id of the projectPurchaseOrders to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/project-purchase-orders/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProjectPurchaseOrders(@PathVariable Long id) {
        log.debug("REST request to delete ProjectPurchaseOrders : {}", id);
        currentTenantIdentifierResolver.setTenant(MASTER);
        projectPurchaseOrdersRepository.delete(id);
        projectPurchaseOrdersSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("projectPurchaseOrders", id.toString())).build();
    }

    /**
     * SEARCH  /_search/project-purchase-orders?query=:query : search for the projectPurchaseOrders corresponding
     * to the query.
     *
     * @param query the query of the projectPurchaseOrders search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/project-purchase-orders",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ProjectPurchaseOrders>> searchProjectPurchaseOrders(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ProjectPurchaseOrders for query {}", query);
        Page<ProjectPurchaseOrders> page = projectPurchaseOrdersSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/project-purchase-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * Get Project Purchase Order by Project id
     *
     * @param id
     * @return projectPurchaseOrders
     */
    @RequestMapping(value = "/project-purchase-orders/projects/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Set<ProjectPurchaseOrders> getProjectPurchaseOrdersByProject(@PathVariable Long id) {
        log.debug("REST request to get ProjectPurchaseOrders : {}", id);
        currentTenantIdentifierResolver.setTenant(SLAVE);
        Projects project = new Projects();
        project.setId(id);
        Set<ProjectPurchaseOrders> projectPurchaseOrders = projectPurchaseOrdersRepository.findByProjectID(project);
        return projectPurchaseOrders;
    }


}
