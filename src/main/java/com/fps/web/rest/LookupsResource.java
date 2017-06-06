package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.domain.Contacts;
import com.fps.domain.Lookups;
import com.fps.repository.ContactsRepository;
import com.fps.service.LookupsService;
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

/**
 * REST controller for managing Lookups.
 */
@RestController
@RequestMapping("/api")
public class LookupsResource {

    private final Logger log = LoggerFactory.getLogger(LookupsResource.class);

    @Inject
    private LookupsService lookupsService;

    @Inject
    private ContactsRepository contactsRepository;

    /**
     * POST  /lookups : Create a new lookups.
     *
     * @param lookups the lookups to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lookups, or with status 400 (Bad Request) if the lookups has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lookups",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lookups> createLookups(@RequestBody Lookups lookups) throws URISyntaxException {
        log.debug("REST request to save Lookups : {}", lookups);
        if (lookups.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lookups", "idexists", "A new lookups cannot already have an ID")).body(null);
        }
        Lookups result = lookupsService.save(lookups);
        return ResponseEntity.created(new URI("/api/lookups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lookups", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lookups : Updates an existing lookups.
     *
     * @param lookups the lookups to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lookups,
     * or with status 400 (Bad Request) if the lookups is not valid,
     * or with status 500 (Internal Server Error) if the lookups couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lookups",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lookups> updateLookups(@RequestBody Lookups lookups) throws URISyntaxException {
        log.debug("REST request to update Lookups : {}", lookups);
        if (lookups.getId() == null) {
            return createLookups(lookups);
        }
        Lookups result = lookupsService.save(lookups);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lookups", lookups.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lookups : get all the lookups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of lookups in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/lookups",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Lookups>> getAllLookups(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Lookups");
        Page<Lookups> page = lookupsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lookups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lookups/:id : get the "id" lookups.
     *
     * @param id the id of the lookups to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lookups, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lookups/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lookups> getLookups(@PathVariable Long id) {
        log.debug("REST request to get Lookups : {}", id);
        Lookups lookups = lookupsService.findOne(id);
        return Optional.ofNullable(lookups)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lookups/:id : delete the "id" lookups.
     *
     * @param id the id of the lookups to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lookups/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLookups(@PathVariable Long id) {
        log.debug("REST request to delete Lookups : {}", id);
        lookupsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lookups", id.toString())).build();
    }

    /**
     * SEARCH  /_search/lookups?query=:query : search for the lookups corresponding
     * to the query.
     *
     * @param query the query of the lookups search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/lookups",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Lookups>> searchLookups(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Lookups for query {}", query);
        Page<Lookups> page = lookupsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/lookups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    //------------------------------------------------------------------------------------------------------------------

    /**
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/lookups/contacts/type",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Lookups> getContactType()
        throws URISyntaxException {
        log.debug("REST request to get a page of Lookups - ContactTypes");
        final List<Lookups> contactType = lookupsService.getLookups("contacts", "type_id");
        return contactType;
    }

    @RequestMapping(value = "/lookups/projects/type",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Lookups> getProjectType()
        throws URISyntaxException {
        log.debug("REST request to get a page of Lookups - ProjectTypes");
        final List<Lookups> contactType = lookupsService.getLookups("projects", "type_id");
        return contactType;
    }


    @RequestMapping(value = "/lookups/projects/status",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Lookups> getProjectStatus()
        throws URISyntaxException {
        log.debug("REST request to get a page of Lookups - ProjectStatus");
        final List<Lookups> projectStatus = lookupsService.getLookups("projects", "status_id");
        return projectStatus;
    }

    @RequestMapping(value = "/lookups/projects/filetype",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Lookups> getProjectFileType()
        throws URISyntaxException {
        log.debug("REST request to get a page of Lookups - ProjectFileType");
        final List<Lookups> projectFileType = lookupsService.getLookups("projects", "processing_original_file_type_id");
        return projectFileType;
    }

    @RequestMapping(value = "/lookups/projects/labtask",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Lookups> getProjectlabtask()
        throws URISyntaxException {
        log.debug("REST request to get a page of Lookups - ProjectLabtask");
        final List<Lookups> projectLabtask = lookupsService.getLookups("project_lab_tasks", "task_name_id");
        return projectLabtask;
    }

    /**
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/lookups/projects/serverTasks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Lookups> getServertask()
        throws URISyntaxException {
        log.debug("REST request to get a page of Lookups - ProjectLabtask");
        final List<Lookups> serverTask = lookupsService.getLookups("servers", "task_name");
        return serverTask;
    }


    //============================
    //============================
    //============================


    /**
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/lookups/get/{tableName}/{value}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Lookups> getAlltasks(@PathVariable("tableName") String tableName, @PathVariable("value") String value)
        throws URISyntaxException {
        log.debug("REST request to get a page of Lookups - ProjectLabtask");
        final List<Lookups> serverTask = lookupsService.getLookups(tableName, value);
        return serverTask;
    }


    // testing for contacts modal box

    /**
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/contacts/get/getAll",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Contacts> getAllContacts()
        throws URISyntaxException {
        log.debug("REST request to get all Contacts");
        final List<Contacts> contactses = contactsRepository.findAll();
        return contactses;
    }


}
