package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.Contacts;
import com.fps.domain.Ingests;
import com.fps.domain.Projects;
import com.fps.domain.WorkOrder;
import com.fps.elastics.search.ContactsSearchRepository;
import com.fps.elastics.search.IngestsSearchRepository;
import com.fps.elastics.search.ProjectsSearchRepository;
import com.fps.elastics.search.WorkOrderSearchRepository;
import com.fps.repository.ContactsRepository;
import com.fps.repository.IngestsRepository;
import com.fps.repository.ProjectsRepository;
import com.fps.repository.WorkOrderRepository;
import com.sun.net.httpserver.Authenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by macbookpro on 1/30/17.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ElasticSearchUpdater {

    private final Logger log = LoggerFactory.getLogger(ElasticSearchUpdater.class);


    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    final static private String MASTER = "master";
    final static private String SLAVE = "slave";

    @Inject
    private ContactsRepository contactsRepository;

    @Inject
    private ContactsSearchRepository contactsSearchRepository;

    @Inject
    private ProjectsRepository projectsRepository;

    @Inject
    private ProjectsSearchRepository projectsSearchRepository;

    @Inject
    private WorkOrderRepository workOrderRepository;

    @Inject
    private WorkOrderSearchRepository workOrderSearchRepository;

    @Inject
    private IngestsRepository ingestsRepository;

    @Inject
    private IngestsSearchRepository ingestsSearchRepository;


    /**
     * GET  /updates/contacts : update elasticsearch for contacts from Database.
     *
     * @return the ResponseEntity with status 200 (OK)
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/updates/contacts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> updateContacts() throws URISyntaxException {

        currentTenantIdentifierResolver.setTenant(SLAVE);
        final List<Contacts> contactsList = contactsRepository.findAll();
        try {
            contactsSearchRepository.save(contactsList);
            return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Error>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * GET  /updates/contacts : update elasticsearch for projects from Database.
     *
     * @return the ResponseEntity with status 200 (OK)
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/updates/projects",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> updateProjects() throws URISyntaxException {

        currentTenantIdentifierResolver.setTenant(SLAVE);
        final List<Projects> projectsList = projectsRepository.findAll();
        try {
            projectsSearchRepository.save(projectsList);
            return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Error>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * GET  /updates/contacts : update elasticsearch for workorder from Database.
     *
     * @return the ResponseEntity with status 200 (OK)
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/updates/workorder",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> updateWorkOrders() throws URISyntaxException {

        currentTenantIdentifierResolver.setTenant(SLAVE);
        final List<WorkOrder> workOrderList = workOrderRepository.findAll();
        try {
            workOrderSearchRepository.save(workOrderList);
            return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Error>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * GET  /updates/ingest : update elasticsearch for ingests from Database.
     *
     * @return the ResponseEntity with status 200 (OK)
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/updates/ingests",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> updateIngests() throws URISyntaxException {

        currentTenantIdentifierResolver.setTenant(SLAVE);
        final List<Ingests> ingestsList = ingestsRepository.findAll();
        try {
            ingestsSearchRepository.save(ingestsList);
            return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Error>(HttpStatus.BAD_REQUEST);
        }
    }
}
