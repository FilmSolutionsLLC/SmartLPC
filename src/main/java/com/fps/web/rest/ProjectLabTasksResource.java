package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.domain.ProjectLabTasks;
import com.fps.domain.Projects;
import com.fps.repository.ProjectLabTasksRepository;
import com.fps.service.ProjectLabTasksService;
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

/**
 * REST controller for managing ProjectLabTasks.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProjectLabTasksResource {

    private final Logger log = LoggerFactory.getLogger(ProjectLabTasksResource.class);

    @Inject
    private ProjectLabTasksService projectLabTasksService;

    @Inject
    private ProjectLabTasksRepository projectLabTasksRepository;

    /**
     * POST  /project-lab-tasks : Create a new projectLabTasks.
     *
     * @param projectLabTasks the projectLabTasks to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectLabTasks, or with status 400 (Bad Request) if the projectLabTasks has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-lab-tasks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectLabTasks> createProjectLabTasks(@RequestBody ProjectLabTasks projectLabTasks) throws URISyntaxException {
        log.debug("REST request to save ProjectLabTasks : {}", projectLabTasks);
        if (projectLabTasks.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("projectLabTasks", "idexists", "A new projectLabTasks cannot already have an ID")).body(null);
        }
        ProjectLabTasks result = projectLabTasksService.save(projectLabTasks);
        return ResponseEntity.created(new URI("/api/project-lab-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("projectLabTasks", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-lab-tasks : Updates an existing projectLabTasks.
     *
     * @param projectLabTasks the projectLabTasks to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectLabTasks,
     * or with status 400 (Bad Request) if the projectLabTasks is not valid,
     * or with status 500 (Internal Server Error) if the projectLabTasks couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-lab-tasks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectLabTasks> updateProjectLabTasks(@RequestBody ProjectLabTasks projectLabTasks) throws URISyntaxException {
        log.debug("REST request to update ProjectLabTasks : {}", projectLabTasks);
        if (projectLabTasks.getId() == null) {
            return createProjectLabTasks(projectLabTasks);
        }
        ProjectLabTasks result = projectLabTasksService.save(projectLabTasks);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("projectLabTasks", projectLabTasks.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-lab-tasks : get all the projectLabTasks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of projectLabTasks in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/project-lab-tasks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ProjectLabTasks>> getAllProjectLabTasks(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ProjectLabTasks");
        Page<ProjectLabTasks> page = projectLabTasksService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/project-lab-tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /project-lab-tasks/:id : get the "id" projectLabTasks.
     *
     * @param id the id of the projectLabTasks to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectLabTasks, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/project-lab-tasks/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectLabTasks> getProjectLabTasks(@PathVariable Long id) {
        log.debug("REST request to get ProjectLabTasks : {}", id);
        ProjectLabTasks projectLabTasks = projectLabTasksService.findOne(id);
        return Optional.ofNullable(projectLabTasks)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /project-lab-tasks/:id : delete the "id" projectLabTasks.
     *
     * @param id the id of the projectLabTasks to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/project-lab-tasks/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProjectLabTasks(@PathVariable Long id) {
        log.debug("REST request to delete ProjectLabTasks : {}", id);
        projectLabTasksService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("projectLabTasks", id.toString())).build();
    }

    /**
     * SEARCH  /_search/project-lab-tasks?query=:query : search for the projectLabTasks corresponding
     * to the query.
     *
     * @param query the query of the projectLabTasks search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/project-lab-tasks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ProjectLabTasks>> searchProjectLabTasks(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ProjectLabTasks for query {}", query);
        Page<ProjectLabTasks> page = projectLabTasksService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/project-lab-tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/project-lab-tasks/projects/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProjectLabTasks> getProjectLabTasksByProject(@PathVariable Long id) {
        Projects projects = new Projects();
        projects.setId(id);
        List<ProjectLabTasks> projectLabTasks = projectLabTasksRepository.findByProjectID(projects);
        return projectLabTasks;
    }

}
