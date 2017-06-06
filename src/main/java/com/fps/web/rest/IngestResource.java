package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.Ingest;
import com.fps.domain.User;
import com.fps.repository.UserRepository;
import com.fps.security.SecurityUtils;
import com.fps.service.IngestService;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Ingest.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class IngestResource {

    private final Logger log = LoggerFactory.getLogger(IngestResource.class);

    @Inject
    private IngestService ingestService;


    @Inject
    private UserRepository userRepository;

    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    final static private String MASTER = "master";
    final static private String SLAVE = "slave";

    /**
     * POST  /ingests : Create a new ingest.
     *
     * @param ingest the ingest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ingest, or with status 400 (Bad Request) if the ingest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ingests",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ingest> createIngest(@RequestBody Ingest ingest) throws URISyntaxException {
        log.debug("REST request to save Ingest : {}", ingest);
        if (ingest.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ingest", "idexists", "A new ingest cannot already have an ID")).body(null);
        }
        try {

            currentTenantIdentifierResolver.setTenant(SLAVE);
            User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

            ingest.setUser(user);
            ingest.setIngestStartTime(ZonedDateTime.now());
            ingest.setCompleted(false);
            final String projectFolder = "mnt/chas001";
            final String getFileCount = "find /" + projectFolder + "/ -type f | wc -l";
            String fileCount = null;

            try {
                Process p = Runtime.getRuntime().exec(getFileCount);
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    fileCount = line;
                    ingest.setTotalImages(Integer.valueOf(fileCount));
                    log.debug("total images to be ingested : " + fileCount);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // alex script to get number of images in source and set to object
            // find foldername -type f | wc -l

            // this gets number of files in folder

            //
            PrintWriter writer = new PrintWriter("/mnt/ingest/ingest.txt", "UTF-8");
            writer.println("Ingest");
            writer.println("Admin Owner : " + ingest.getUser().getFirstName());
            writer.println("Server Name : " + ingest.getServer().getIpAddress());
            writer.println("Task Name   : " + ingest.getAction().getTextValue());
            writer.println("Start Time  : " + ZonedDateTime.now());

            writer.close();
        } catch (IOException e) {
            log.error(e.toString());
        }
        Ingest result = ingestService.save(ingest);
        return ResponseEntity.created(new URI("/api/ingests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ingest", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ingests : Updates an existing ingest.
     *
     * @param ingest the ingest to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ingest,
     * or with status 400 (Bad Request) if the ingest is not valid,
     * or with status 500 (Internal Server Error) if the ingest couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ingests",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ingest> updateIngest(@RequestBody Ingest ingest) throws URISyntaxException {
        log.debug("REST request to update Ingest : {}", ingest);
        if (ingest.getId() == null) {
            return createIngest(ingest);
        }

        Ingest result = ingestService.save(ingest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ingest", ingest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ingests : get all the ingests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ingests in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/ingests",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Ingest>> getAllIngests(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Ingests");
        Page<Ingest> page = ingestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ingests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ingests/:id : get the "id" ingest.
     *
     * @param id the id of the ingest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ingest, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/ingests/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ingest> getIngest(@PathVariable Long id) {
        log.debug("REST request to get Ingest : {}", id);
        Ingest ingest = ingestService.findOne(id);
        return Optional.ofNullable(ingest)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ingests/:id : delete the "id" ingest.
     *
     * @param id the id of the ingest to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/ingests/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteIngest(@PathVariable Long id) {
        log.debug("REST request to delete Ingest : {}", id);
        ingestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ingest", id.toString())).build();
    }

    /**
     * SEARCH  /_search/ingests?query=:query : search for the ingest corresponding
     * to the query.
     *
     * @param query the query of the ingest search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/ingests",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Ingest>> searchIngests(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Ingests for query {}", query);
        Page<Ingest> page = ingestService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ingests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
