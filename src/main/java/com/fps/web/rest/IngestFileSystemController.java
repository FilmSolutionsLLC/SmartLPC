package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.config.IngestConfiguration;
import com.fps.domain.Ingests;
import com.fps.repository.LookupsRepository;
import com.fps.service.IngestFileSystemService;
import com.fps.web.rest.dto.IngestFileSystem;
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
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by macbookpro on 2/1/17.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class IngestFileSystemController {

    private final Logger log = LoggerFactory.getLogger(IngestFileSystemController.class);


    @Inject
    private IngestFileSystemService ingestFileSystemService;


    @Inject
    private IngestConfiguration ingestConfiguration;

    @Inject
    private LookupsRepository lookupsRepository;

    /**
     * @return
     */
    @RequestMapping(value = "/filesystem/ingest",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Set<IngestFileSystem>> currentIngests() {
        long startTime = System.currentTimeMillis();

        final Set<IngestFileSystem> getAllCurrentIngests = ingestFileSystemService.newIngests();

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("running time :" + totalTime);

        return new ResponseEntity<>(getAllCurrentIngests, HttpStatus.OK);
    }


    /**
     * @param ingestFileSystem
     * @return
     */
    @RequestMapping(value = "/filesystem/ingest",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> runIngest(@RequestBody IngestFileSystem ingestFileSystem) {

        log.info("request to start ingest : " + ingestFileSystem.toString());
        ingestFileSystemService.startIngest(ingestFileSystem);
        return ResponseEntity.ok().body(ingestFileSystem);
    }

    /**
     * @return
     */
    @RequestMapping(value = "/running/ingest",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Set<Ingests>> runningIngests() {
        final Set<Ingests> ingestses = ingestFileSystemService.getAllRunningIngests();
        return new ResponseEntity<>(ingestses, HttpStatus.OK);

    }


    /**
     * GET  /ingests/:id : get the "id" ingest.
     *
     * @param id the id of the ingest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ingest, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/filesystem/ingests/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ingests> getIngest(@PathVariable Long id) {
        log.debug("REST request to get Ingests : {}", id);
        Ingests ingests = ingestFileSystemService.getIngest(id);
        return Optional.ofNullable(ingests)
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
    @RequestMapping(value = "/filesystem/ingests/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteIngest(@PathVariable Long id) {
        log.debug("REST request to delete Ingest : {}", id);
        ingestFileSystemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ingests", id.toString())).build();
    }

    /**
     * KILL INGEST
     *
     * @param ingests
     * @return
     */
    @RequestMapping(value = "/filesystem/ingests",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> killIngest(@RequestBody Ingests ingests) {
        log.debug("REST request to update Ingest : {}", ingests);
        ingests.setStatus("KILLED");
        //KILLED BY ADMIN USER
        ingestFileSystemService.stopIngest(ingests);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Pause INGEST
     *
     * @param ingests
     * @return
     */
    @RequestMapping(value = "/filesystem/pause",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> pauseIngest(@RequestBody Ingests ingests) {
        log.debug("REST request to pause Ingest : {}", ingests);
        ingestFileSystemService.pause(ingests);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Resume INGEST
     *
     * @param ingests
     * @return
     */
    @RequestMapping(value = "/filesystem/resume",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> resumeIngest(@RequestBody Ingests ingests) {
        log.debug("REST request to resume Ingest : {}", ingests);
        ingestFileSystemService.resume(ingests);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET  /ingests : get all the ingests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ingests in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/ingestss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Ingests>> getAllIngests(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Ingests");
        Page<Ingests> page = ingestFileSystemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ingestss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * SEARCH  /_search/ingests?query=:query : search for the ingest corresponding
     * to the query.
     *
     * @param query the query of the ingest search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/ingestss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Ingests>> searchIngests(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Ingests for query {}", query);
        Page<Ingests> page = ingestFileSystemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ingestss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ingests/:id : get the "id" ingest.
     *
     * @param id the id of the ingest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ingest, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/filesystem/progress/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ingests> progressIngest(@PathVariable Long id) {
        log.debug("REST request to get Ingests : {}", id);
        Ingests ingests = ingestFileSystemService.progress(id);
        return Optional.ofNullable(ingests)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * Get all logos from central logo location
     *
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/logos/ingest",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<String> logos()
        throws URISyntaxException {
        List<String> logo = ingestFileSystemService.getLogos();

        return logo;
    }

    /**
     * Check for free servers
     *
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/filesystem/highpriority",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Boolean freeservers()
        throws URISyntaxException {
        final Boolean freeServer = ingestFileSystemService.idleservers();
        return freeServer;
    }

    
    

}
