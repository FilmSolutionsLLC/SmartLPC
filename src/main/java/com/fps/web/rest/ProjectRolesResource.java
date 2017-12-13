package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.Contact;
import com.fps.domain.ProjectRoles;
import com.fps.domain.User;
import com.fps.elastics.search.ProjectRolesSearchRepository;
import com.fps.repository.ContactsRepository;
import com.fps.repository.ProjectRolesRepository;
import com.fps.repository.UserRepository;
import com.fps.security.SecurityUtils;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing ProjectRoles.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProjectRolesResource {

    private final Logger log = LoggerFactory.getLogger(ProjectRolesResource.class);

    @Inject
    private ProjectRolesRepository projectRolesRepository;

    @Inject
    private ProjectRolesSearchRepository projectRolesSearchRepository;

    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    @Inject
    private ContactsRepository contactsRepository;


    @Inject
    private JdbcTemplate jdbcTemplate;

    @Inject
    private UserRepository userRepository;

    final static private String MASTER = "master";
    final static private String SLAVE = "slave";

    /**
     * POST  /project-roles : Create a new projectRoles.
     *
     * @param projectRoles the projectRoles to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectRoles, or with status 400 (Bad Request) if the projectRoles has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-roles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectRoles> createProjectRoles(@RequestBody ProjectRoles projectRoles) throws URISyntaxException {
        log.debug("REST request to save ProjectRoles : {}", projectRoles);
        if (projectRoles.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("projectRoles", "idexists", "A new projectRoles cannot already have an ID")).body(null);
        }
        currentTenantIdentifierResolver.setTenant(MASTER);
        if (projectRoles.getContact() == null) {
            Contact contact = new Contact();
            contact.setId((long)5181);
            projectRoles.setContact(contact);
            projectRoles.setRelationship_type("PKO_Tag");
            projectRoles.setDisabled(true);
        }
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        projectRoles.setCreatedByAdminUser(user);
        projectRoles.setCreatedDate(ZonedDateTime.now());

        log.info("Saving This Project Roles : " + projectRoles.toString());
        ProjectRoles result = projectRolesRepository.save(projectRoles);
        projectRolesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/project-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("projectRoles", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-roles : Updates an existing projectRoles.
     *
     * @param projectRoles the projectRoles to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectRoles,
     * or with status 400 (Bad Request) if the projectRoles is not valid,
     * or with status 500 (Internal Server Error) if the projectRoles couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-roles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectRoles> updateProjectRoles(@RequestBody ProjectRoles projectRoles) throws URISyntaxException {
        log.debug("REST request to update ProjectRoles : {}", projectRoles);
        if (projectRoles.getId() == null) {
            return createProjectRoles(projectRoles);
        }
        final User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        projectRoles.setUpdatedByAdminUser(user);
        projectRoles.setUpdatedDate(ZonedDateTime.now());

        currentTenantIdentifierResolver.setTenant(MASTER);
        ProjectRoles result = projectRolesRepository.save(projectRoles);
        projectRolesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("projectRoles", projectRoles.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-roles : get all the projectRoles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of projectRoles in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/project-roles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ProjectRoles>> getAllProjectRoles(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ProjectRoles");
        currentTenantIdentifierResolver.setTenant(SLAVE);
        Page<ProjectRoles> page = projectRolesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/project-roles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /project-roles/:id : get the "id" projectRoles.
     *
     * @param id the id of the projectRoles to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectRoles, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/project-roles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectRoles> getProjectRoles(@PathVariable Long id) {
        log.debug("REST request to get ProjectRoles : {}", id);
        currentTenantIdentifierResolver.setTenant(SLAVE);
        ProjectRoles projectRoles = projectRolesRepository.findOne(id);
        return Optional.ofNullable(projectRoles)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /project-roles/:id : delete the "id" projectRoles.
     *
     * @param id the id of the projectRoles to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    // TODO: Delete from user_image table where project_roles id = xxx
    @RequestMapping(value = "/project-roles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProjectRoles(@PathVariable Long id) {
        log.debug("REST request to delete ProjectRoles : {}", id);
        currentTenantIdentifierResolver.setTenant(MASTER);
        projectRolesRepository.delete(id);
        projectRolesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("projectRoles", id.toString())).build();
    }

    /**
     * SEARCH  /_search/project-roles?query=:query : search for the projectRoles corresponding
     * to the query.
     *
     * @param query the query of the projectRoles search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/project-roles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ProjectRoles>> searchProjectRoles(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ProjectRoles for query {}", query);
        Page<ProjectRoles> page = projectRolesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/project-roles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/hotkeys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Map> getHotKeys(@PathVariable Long id) throws URISyntaxException {
    	System.out.println("GET HOTKEYS");

        final String sql = "select hotkey_value from project_roles  where relationship_type='PKO_Tag' and project_id=" + id;

        System.out.println(sql);
        final List<Map> hotkeys = (List) jdbcTemplate.queryForList(sql);

        return hotkeys;
    }


}
