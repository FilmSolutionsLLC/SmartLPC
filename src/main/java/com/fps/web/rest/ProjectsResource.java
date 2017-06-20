package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.*;
import com.fps.repository.*;
import com.fps.service.ProjectsService;
import com.fps.web.rest.dto.ProjectsDTO;
import com.fps.web.rest.dto.ProjectsViewDTO;
import com.fps.web.rest.dto.TalentInfoDTO;
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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * REST controller for managing Projects.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProjectsResource {

    private final Logger log = LoggerFactory.getLogger(ProjectsResource.class);

    @Inject
    private ProjectsService projectsService;
    @Inject
    private UserRepository userRepository;

    @Inject
    private ProjectRolesRepository projectRolesRepository;

    @Inject
    private ProjectPurchaseOrdersRepository projectPurchaseOrdersRepository;

    @Inject
    private ProjectLabTasksRepository projectLabTasksRepository;

    @Inject
    private ContactPrivilegesRepository contactPrivilegesRepository;

    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    final static private String MASTER = "master";
    final static private String SLAVE = "slave";


    /**
     * POST  /projects : Create a new projects.
     *
     * @param projectsDTO the projects to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projects, or with status 400 (Bad Request) if the projects has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/projects",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Projects> createProjects(@RequestBody ProjectsDTO projectsDTO) throws URISyntaxException {


        if (projectsDTO.getProjects().getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("projects", "idexists", "A new projects cannot already have an ID")).body(null);
        }

        Projects result = projectsService.save(projectsDTO);

        return ResponseEntity.created(new URI("/api/projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("projects", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /projects : Updates an existing projects.
     *
     * @param projectsDTO the projects to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projects,
     * or with status 400 (Bad Request) if the projects is not valid,
     * or with status 500 (Internal Server Error) if the projects couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/projects",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<Projects> updateProjects(@RequestBody ProjectsDTO projectsDTO) throws URISyntaxException {

        Projects result = projectsService.update(projectsDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("projects", result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /projects : get all the projects.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of projects in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/projects",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ProjectsDTO>> getAllProjects(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Projects");
        Page<Projects> page = projectsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/projects");
        currentTenantIdentifierResolver.setTenant(SLAVE);

        List<ProjectsDTO> projectsDTOs = new ArrayList<ProjectsDTO>();
        for (Projects projects : page.getContent()) {

            /*final List<ProjectRoles> projectRoles = projectRolesRepository.findByProject(projects);
            final Set<ProjectRoles> projectRolesSet = new HashSet<>(projectRoles);*/

            final Set<ProjectRoles> projectRoles = projectRolesRepository.findByProjectAndRelationshipType(projects, "Main Contact", "Unit Publicist");
            ProjectsDTO projectsDTO = new ProjectsDTO();
            projectsDTO.setProjectRoles(projectRoles);
            projectsDTO.setProjects(projects);
            projectsDTOs.add(projectsDTO);
        }

        return new ResponseEntity<>(projectsDTOs, headers, HttpStatus.OK);
    }

    /**
     * GET  /projects/:id : get the "id" projects.
     *
     * @param id the id of the projects to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projects, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/projects/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectsDTO> getProjects(@PathVariable Long id) {
        log.debug("REST request to get Projects : {}", id);

        final Projects projects = projectsService.findOne(id);

        //final List<ProjectRoles> projectRoleses = projectRolesRepository.findByProject(projects);
        final List<ProjectPurchaseOrders> projectPurchaseOrderses = projectPurchaseOrdersRepository.findByProject(projects);

        final List<ProjectLabTasks> projectLabTaskses = projectLabTasksRepository.findByProject(projects);
        final List<ContactPrivileges> contactPrivilegeses = contactPrivilegesRepository.findByProjectAndInternal(projects, false);

        //final Set<ProjectRoles> projectRolesSet = new HashSet<>(projectRoleses);
        final Set<ProjectPurchaseOrders> projectPurchaseOrdersSet = new HashSet<>(projectPurchaseOrderses);
        final Set<ProjectLabTasks> projectLabTasksSet = new HashSet<>(projectLabTaskses);
        final Set<ContactPrivileges> contactPrivilegesSet = new HashSet<>(contactPrivilegeses);

        Contacts contacts = new Contacts();
        contacts.setId(Long.valueOf(5181));
        Set<ProjectRoles> projectRolesSet = projectRolesRepository.findByProjectAndContact(projects, contacts);

        final ProjectsDTO projectsDTO = new ProjectsDTO(projects, projectLabTasksSet, projectPurchaseOrdersSet, projectRolesSet, contactPrivilegesSet);


        log.info(projectsDTO.toString());
        return Optional.ofNullable(projectsDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /projects/:id : delete the "id" projects.
     *
     * @param id the id of the projects to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/projects/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProjects(@PathVariable Long id) {
        log.debug("REST request to delete Projects : {}", id);
        projectsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("projects", id.toString())).build();
    }

    /**
     * SEARCH  /_search/projects?query=:query : search for the projects corresponding
     * to the query.
     *
     * @param query the query of the projects search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/projects",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ProjectsDTO>> searchProjects(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Projects for query {}", query);
        Page<Projects> page = projectsService.search(query, pageable);


        List<ProjectsDTO> projectsDTOs = new ArrayList<ProjectsDTO>();
        for (Projects projects : page.getContent()) {

            final Set<ProjectRoles> projectRoles = projectRolesRepository.findByProjectAndRelationshipType(projects, "Main Contact", "Unit Publicist");
            ProjectsDTO projectsDTO = new ProjectsDTO();
            projectsDTO.setProjectRoles(projectRoles);
            projectsDTO.setProjects(projects);
            projectsDTOs.add(projectsDTO);
        }

        log.info("total DTO size : " + projectsDTOs.size());

        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/projects");
        return new ResponseEntity<>(projectsDTOs, headers, HttpStatus.OK);
    }

    /*

        @RequestMapping(value = "/_search/projects/filter",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public ResponseEntity<List<Projects>> searchProjectsbyFilter(@RequestParam String query)
            throws URISyntaxException {
            log.debug("REST request to search for a page of Projects for query {}", query);
            Page<Projects> page =
            HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/projects");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        }
    */
    @RequestMapping(value = "/idname/projects",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Objects[]> getAllProjectsIDName() throws URISyntaxException {
        log.debug("REST request to get all projects");
        List<Objects[]> projects = projectsService.findProjects();
        return projects;
    }

    @RequestMapping(value = "/pro",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ProjectsViewDTO>> getAllProjectsNew(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get all projects");
        Page<Objects[]> page = projectsService.getAllProjects(pageable);
        // List<ProjectsViewDTO> projectsViewDTOs = page.getContent();
        // columns names same as pojo class
        List<ProjectsViewDTO> projectsViewDTOs = new ArrayList<>();
        for (Object[] objects : page.getContent()) {

            ProjectsViewDTO projectsViewDTO = new ProjectsViewDTO();
            projectsViewDTO.setId(Long.valueOf(String.valueOf(objects[0])));
            projectsViewDTO.setProjectName(String.valueOf(objects[1]));
            projectsViewDTO.setProjectStatus(String.valueOf(objects[2]));
            projectsViewDTO.setMainContactName(String.valueOf(objects[3]));
            projectsViewDTO.setMainContactOffice(String.valueOf(objects[4]));
            projectsViewDTO.setMainContactEmail(String.valueOf(objects[5]));
            projectsViewDTO.setUnitPublicistName(String.valueOf(objects[6]));
            projectsViewDTO.setUnitPublicistMobile(String.valueOf(objects[7]));
            projectsViewDTO.setUnitPublicistOffice(String.valueOf(objects[8]));
            projectsViewDTO.setUnitPublicistEmail(String.valueOf(objects[9]));

            log.info("===================================================");
            projectsViewDTOs.add(projectsViewDTO);
            log.info(projectsViewDTO.toString());
        }


        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/pro");
        return new ResponseEntity<>(projectsViewDTOs, headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/logos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> addLogos(@RequestBody byte[] logo) throws URISyntaxException, IOException {

        final Integer fileCreated = projectsService.saveLogo(logo);
        if (fileCreated == 1) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/count",
        method = RequestMethod.POST, produces = "text/plain")
    @Timed
    public String getGBCount(@RequestBody Projects projects) throws URISyntaxException, IOException {

        final String gbCount = projectsService.getGbCount(projects);

/*        return Optional.ofNullable(gbCount)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));*/

        return gbCount;
    }


    @RequestMapping(value = "/talents",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TalentInfoDTO>> talentInfoss(@RequestParam Long id, @RequestParam String type) {


        log.info("ID : " + id);
        log.info("Type : " + type);


        final List<TalentInfoDTO> talentInfo = projectsService.talentInfos(id, type);


        return Optional.ofNullable(talentInfo)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @RequestMapping(value = "/remove",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void removeTalentInfo(@RequestParam Long id, @RequestParam String type) {


        log.info("Remove with ID : " + id);
        log.info("Type : " + type);
        projectsService.removeTalentInfo(id, type);

    }

    @RequestMapping(value = "/update/album",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void updateAlbum(@RequestBody TalentInfoDTO album) {


        log.info("Update Album  : " + album.getId());

        projectsService.updateAlbum(album);

    }


    @RequestMapping(value = "/insert/album",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void insertAlbum(@RequestBody TalentInfoDTO album) {


        log.info("Insert Album for Project  : " + album.getId());

        projectsService.insertAlbum(album);

    }

}
