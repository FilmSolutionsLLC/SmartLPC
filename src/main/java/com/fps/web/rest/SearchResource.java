package com.fps.web.rest;

/**
 * Created by macbookpro on 12/14/16.
 */

import com.codahale.metrics.annotation.Timed;
import com.fps.domain.Contacts;
import com.fps.domain.Projects;
import com.fps.elastics.search.WorkOrderSearchRepository;
import com.fps.service.ContactsService;
import com.fps.service.ProjectsService;
import com.fps.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for managing Search.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SearchResource {
    private final Logger log = LoggerFactory.getLogger(SearchResource.class);

    @Inject
    private ContactsService contactsService;

    @Inject
    private ProjectsService projectsService;


    @Inject
    private WorkOrderSearchRepository workOrderSearchRepository;




    /**
     * SEARCH  /_search/all?query=:query : search for the all projects and contacts corresponding
     * to the query.
     *
     * @param query the query of the all search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/all",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> searchAll(@RequestParam String query) throws URISyntaxException {

        log.debug("REST request to search for a page of Projects for query {}", query);
        Iterable<Projects> projectses = projectsService.searchForList(query);

        Iterable<Contacts> contactses = contactsService.searchForList(query);

        Map<String, Iterable<?>> searchResponse = new HashMap<String, Iterable<?>>();

        searchResponse.put("contacts", contactses);
        searchResponse.put("projects", projectses);


        return new ResponseEntity<>(searchResponse, HttpStatus.OK);

    }

}
