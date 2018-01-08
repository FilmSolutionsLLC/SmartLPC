package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.*;
import com.fps.elastics.search.ProjectRolesSearchRepository;
import com.fps.repository.*;
import com.fps.service.ProjectsService;
import com.fps.web.rest.dto.ProjectsDTO;
import com.fps.web.rest.dto.ProjectsViewDTO;
import com.fps.web.rest.dto.TalentInfoDTO;
import com.fps.web.rest.util.HeaderUtil;
import com.fps.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private ProjectRolesSearchRepository projectRolesSearchRepository;

	@Inject
	private ProjectPurchaseOrdersRepository projectPurchaseOrdersRepository;

	@Inject
	private ProjectLabTasksRepository projectLabTasksRepository;

	@Inject
	private ContactPrivilegesRepository contactPrivilegesRepository;

	@Inject
	private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

	@Inject
    private ContactsRepository contactsRepository;

	@Inject
    private ProjectsRepository projectsRepository;

	@Inject
    private AlbumPermissionsRepository albumPermissionsRepository;

	@Inject
    private JdbcTemplate jdbcTemplate;

	final static private String MASTER = "master";
	final static private String SLAVE = "slave";

	/**
	 * POST /projects : Create a new projects.
	 *
	 * @param projectsDTO
	 *            the projects to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new projects, or with status 400 (Bad Request) if the projects
	 *         has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/projects", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Projects> createProjects(@RequestBody ProjectsDTO projectsDTO) throws URISyntaxException {

		if (projectsDTO.getProjects().getId() != null) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert("projects", "idexists", "A new projects cannot already have an ID"))
					.body(null);
		}

            Projects result = projectsService.save(projectsDTO);

		return ResponseEntity.created(new URI("/api/projects/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("projects", result.getId().toString())).body(result);
	}

	/**
	 * PUT /projects : Updates an existing projects.
	 *
	 * @param projectsDTO
	 *            the projects to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         projects, or with status 400 (Bad Request) if the projects is not
	 *         valid, or with status 500 (Internal Server Error) if the projects
	 *         couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/projects", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	@Transactional
	public ResponseEntity<Projects> updateProjects(@RequestBody ProjectsDTO projectsDTO) throws URISyntaxException {

		Projects result = projectsService.update(projectsDTO);

		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("projects", result.getId().toString()))
				.body(result);
	}

	/**
	 * GET /projects : get all the projects.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of projects
	 *         in body
	 * @throws URISyntaxException
	 *             if there is an error to generate the pagination HTTP headers
	 */
	@RequestMapping(value = "/projects", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<ProjectsDTO>> getAllProjects(Pageable pageable) throws URISyntaxException {
		log.info("REST request to get a page of Projects");
		Page<Projects> page = projectsService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/projects");
		currentTenantIdentifierResolver.setTenant(SLAVE);

		List<ProjectsDTO> projectsDTOs = new ArrayList<ProjectsDTO>();
		for (Projects projects : page.getContent()) {

			/*
			 * final List<ProjectRoles> projectRoles =
			 * projectRolesRepository.findByProject(projects); final
			 * Set<ProjectRoles> projectRolesSet = new HashSet<>(projectRoles);
			 */

			final Set<ProjectRoles> projectRoles = projectRolesRepository.findByProjectAndRelationshipType(projects,
					"Main Contact", "Unit Publicist");
			ProjectsDTO projectsDTO = new ProjectsDTO();
			projectsDTO.setProjectRoles(projectRoles);
			projectsDTO.setProjects(projects);
			projectsDTOs.add(projectsDTO);
		}

		return new ResponseEntity<>(projectsDTOs, headers, HttpStatus.OK);
	}

	/**
	 * GET /projects/:id : get the "id" projects.
	 *
	 * @param id
	 *            the id of the projects to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         projects, or with status 404 (Not Found)
	 */
	@RequestMapping(value = "/projects/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<ProjectsDTO> getProjects(@PathVariable Long id) {
		log.info("REST request to get Projects : {}", id);

        long startTime = System.nanoTime();
		final Projects projects = projectsService.findOne(id);

		// final List<ProjectRoles> projectRoleses =
		// projectRolesRepository.findByProject(projects);
		final List<ProjectPurchaseOrders> projectPurchaseOrderses = projectPurchaseOrdersRepository
				.findByProject(projects);

		final List<ProjectLabTasks> projectLabTaskses = projectLabTasksRepository.findByProject(projects);
		/*final List<ContactPrivileges> contactPrivilegeses = contactPrivilegesRepository
				.findByProjectAndInternal(projects, false);*/
        final List<ContactPrivileges> contactPrivilegeses = contactPrivilegesRepository
            .findByProject(projects);

		// final Set<ProjectRoles> projectRolesSet = new
		// HashSet<>(projectRoleses);
		final Set<ProjectPurchaseOrders> projectPurchaseOrdersSet = new HashSet<>(projectPurchaseOrderses);
		final Set<ProjectLabTasks> projectLabTasksSet = new HashSet<>(projectLabTaskses);
		final Set<ContactPrivileges> contactPrivilegesSet = new HashSet<>(contactPrivilegeses);

		Contact contact = new Contact();
		contact.setId(Long.valueOf(5181));
		Set<ProjectRoles> projectRolesSet = projectRolesRepository.findByProjectAndContact(projects, contact);

		final ProjectsDTO projectsDTO = new ProjectsDTO(projects, projectLabTasksSet, projectPurchaseOrdersSet,
				projectRolesSet, contactPrivilegesSet);

		log.info(projectsDTO.toString());
        long elapsedTimeNs = System.nanoTime() - startTime;
		return Optional.ofNullable(projectsDTO).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /projects/:id : delete the "id" projects.
	 *
	 * @param id
	 *            the id of the projects to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@RequestMapping(value = "/projects/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deleteProjects(@PathVariable Long id) {
        long startTime = System.nanoTime();
		log.debug("REST request to delete Projects : {}", id);
		projectsService.delete(id);
        long elapsedTimeNs = System.nanoTime() - startTime;
        double seconds = (double)elapsedTimeNs / 1000000000.0;

        log.info("Project Deleted in :"+seconds+" seconds");
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("projects", id.toString())).build();
	}

	/**
	 * SEARCH /_search/projects?query=:query : search for the projects
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the projects search
	 * @return the result of the search
	 */
	@RequestMapping(value = "/_search/projects", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<ProjectsDTO>> searchProjects(@RequestParam String query, Pageable pageable)
			throws URISyntaxException {
		log.debug("REST request to search for a page of Projects for query {}", query);
		Page<Projects> page = projectsService.search(query, pageable);

		List<ProjectsDTO> projectsDTOs = new ArrayList<ProjectsDTO>();
		for (Projects projects : page.getContent()) {

			final Set<ProjectRoles> projectRoles = projectRolesRepository.findByProjectAndRelationshipType(projects,
					"Main Contact", "Unit Publicist");
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
	 *
	 * @RequestMapping(value = "/_search/projects/filter", method =
	 * RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	 *
	 * @Timed public ResponseEntity<List<Projects>>
	 * searchProjectsbyFilter(@RequestParam String query) throws
	 * URISyntaxException {
	 * log.debug("REST request to search for a page of Projects for query {}",
	 * query); Page<Projects> page = HttpHeaders headers =
	 * PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
	 * "/api/_search/projects"); return new ResponseEntity<>(page.getContent(),
	 * headers, HttpStatus.OK); }
	 */

    /**
     * Get All Projects in id,name form for Select Dropdown
     * @return
     * @throws URISyntaxException
     */
	@RequestMapping(value = "/idname/projects", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Objects[]> getAllProjectsIDName() throws URISyntaxException {
		log.info("REST request to get all projects");
		List<Objects[]> projects = projectsService.findProjects();
		return projects;
	}

    /**
     * DATA in project view table form with pagination
     * @param pageable
     * @return
     * @throws URISyntaxException
     */
	@RequestMapping(value = "/pro", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<ProjectsViewDTO>> getAllProjectsNew(Pageable pageable) throws URISyntaxException {
		log.info("REST request to get all projects");
        Page<Objects[]> page =null;
        List<ProjectsViewDTO> projectsViewDTOs = new ArrayList<>();
        try {
             page = projectsService.getAllProjects(pageable);

            // columns names same as pojo class

            for (Object[] objects : page.getContent()) {

                ProjectsViewDTO projectsViewDTO = new ProjectsViewDTO();
                projectsViewDTO.setId(Long.valueOf(String.valueOf(objects[0])));
                projectsViewDTO.setProjectName(String.valueOf(objects[1]));
                projectsViewDTO.setProjectStatus(String.valueOf(objects[2]));

                if (objects[3] != null) {
                    projectsViewDTO.setMainContactID(Long.valueOf(String.valueOf(objects[3])));
                }
                projectsViewDTO.setMainContactName(String.valueOf(objects[4]));
                projectsViewDTO.setMainContactOffice(String.valueOf(objects[5]));
                projectsViewDTO.setMainContactEmail(String.valueOf(objects[6]));

                if (objects[7] != null) {
                    projectsViewDTO.setUnitPublicistID(Long.valueOf(String.valueOf(objects[7])));
                }
                projectsViewDTO.setUnitPublicistName(String.valueOf(objects[8]));
                projectsViewDTO.setUnitPublicistMobile(String.valueOf(objects[9]));
                projectsViewDTO.setUnitPublicistOffice(String.valueOf(objects[10]));
                projectsViewDTO.setUnitPublicistEmail(String.valueOf(objects[11]));

                projectsViewDTOs.add(projectsViewDTO);

            }

        }catch (Exception e){
		    e.printStackTrace();
        }
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/pro");
		return new ResponseEntity<>(projectsViewDTOs, headers, HttpStatus.OK);
	}

    /**
     * Upload Logo to server
     * @param logo
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
	@RequestMapping(value = "/logos", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<?> addLogos(@RequestBody byte[] logo) throws URISyntaxException, IOException {

		final Integer fileCreated = projectsService.saveLogo(logo);
		if (fileCreated == 1) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

    /**
     * GB count of project on server /alfrescoTitle1/alfrescoTitle2
     * @param projects
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
	@RequestMapping(value = "/count", method = RequestMethod.POST, produces = "text/plain")
	@Timed
	public String getGBCount(@RequestBody Projects projects) throws URISyntaxException, IOException {

		final String gbCount = projectsService.getGbCount(projects);
		return gbCount;
	}

    /**
     * Get All Talents related to Project
     * @param id
     * @param type
     * @return
     */
	@RequestMapping(value = "/talents", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<TalentInfoDTO>> talentInfoss(@RequestParam Long id, @RequestParam String type) {

		log.info("ID : " + id);
		log.info("Type : " + type);

		final List<TalentInfoDTO> talentInfo = projectsService.talentInfos(id, type);

		return Optional.ofNullable(talentInfo).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

	}

    /**
     * Remove Talent
     * @param id
     * @param type
     */
	@RequestMapping(value = "/remove", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public void removeTalentInfo(@RequestParam Long id, @RequestParam String type) {

		log.info("Remove with ID : " + id);
		log.info("Type : " + type);
		projectsService.removeTalentInfo(id, type);

	}

    /**
     * Update album
     * @param album
     */
	@RequestMapping(value = "/update/album", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public void updateAlbum(@RequestBody TalentInfoDTO album) {

		log.info("Update Album  : " + album.getId());

		projectsService.updateAlbum(album);

	}

    /**
     * Insert new album
     * @param album
     */
	@RequestMapping(value = "/insert/album", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public void insertAlbum(@RequestBody TalentInfoDTO album) {

		log.info("Insert Album for Project  : " + album.getId());

		projectsService.insertAlbum(album);

	}

    /**
     * TODO: Create touch file or call delete script
     * Delete Project
     * @param password
     * @return
     */
	@RequestMapping(value = "/project/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public Integer deleteProjectPassword(@RequestBody String password) {
		if (password.equals("13qe!#QE")) {
			return 1;
		} else {
			return 0;
		}
	}

    /**
     * TODO: Create touch file or call delete script
     * Rename project
     * @param id
     * @param alfrescoTitle1
     * @param alfrescoTitle2
     */
	@RequestMapping(value = "/rename", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public void renameProject(@RequestParam Long id, @RequestParam String alfrescoTitle1,
			@RequestParam String alfrescoTitle2) {

		log.info("Rename with ID : " + id);

		projectsService.rename(id, alfrescoTitle1, alfrescoTitle2);

	}


    /**
     * Get just the Project and not the ProjectDTO
     * @param id
     * @return
     */
    @RequestMapping(value = "/justproject/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Projects>  justProject(@PathVariable Long id) {

        log.info("Get Just Project with ID : " + id);
        Projects projects = projectsRepository.findOne(id);

        return Optional.ofNullable(projects).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * Save Album Permissions for Exec
     * @param albumPermissionsList
     * @return
     */
    @RequestMapping(value = "/album/permissions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void albumPermissions(@RequestBody List<AlbumPermissions> albumPermissionsList) {


        albumPermissionsRepository.save(albumPermissionsList);
        log.info("Saved album permissions");
    }

    /**
     * Disable Projects by setting disable flag to true
     * @param id
     * @param action
     */
    @RequestMapping(value = "/disableproject", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void  disableProject(@RequestParam("id") Long id,@RequestParam("action") String action) {
        if(action.equalsIgnoreCase("disable")) {
            log.info("Disable Project with ID : " + id);
            String sql = "Update projects set disabled = 1 where id = " + id;
            jdbcTemplate.update(sql);
            log.info("Disable success");
        }else{
            log.info("Enable Project with ID : " + id);
            String sql = "Update projects set disabled = 0 where id = " + id;
            jdbcTemplate.update(sql);
            log.info("Enable success");
        }
    }


    /**
     * Get All Projects in id,name form for Select Dropdown
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/template/projects", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Objects[]> getAllTemplateProjects() throws URISyntaxException {
        log.info("REST request to get all template projects");
        List<Objects[]> projects = projectsRepository.findTemplateProjects();
        return projects;
    }



    /**
     * Get All Projects in id,name form for Select Dropdown
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/prev/next/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Map<String,Long> getPreviousNextID(@PathVariable Long id) throws URISyntaxException {
        log.info("Get Previous and Next Project ID");
        String next = "select id from projects where id = (select min(id) from projects where id > "+id+")";
        String prev = "select id from projects where id = (select max(id) from projects where id < "+id+")";
        //Long nextID = jdbcTemplate.queryForObject(next,new Object[] {id},Long.class);
        //Long prevID = jdbcTemplate.queryForObject(prev,new Object[] {id},Long.class);
        Long nextID = jdbcTemplate.query(next, new ResultSetExtractor<Long>() {
            @Override
            public Long extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                return resultSet.next() ? resultSet.getLong("id") : null;
            }
        });

        Long prevID = jdbcTemplate.query(prev, new ResultSetExtractor<Long>() {
            @Override
            public Long extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                return resultSet.next() ? resultSet.getLong("id") : null;
            }
        });
        Map<String,Long> projects = new HashMap<>();
        projects.put("next",nextID);
        projects.put("prev",prevID);

        return projects;
    }


    /**
     * Get All Projects in id,name form for Select Dropdown
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/prev/next/talent/{id}/{projectID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Map<String,Long> getPreviousNextTalentID(@PathVariable Long id,@PathVariable Long projectID) throws URISyntaxException {
        log.info("Get Previous and Next Talent ID");
        String next = "select id from project_roles where id = (select min(id) from project_roles where id > "+id+" and project_id = "+projectID+" and relationship_type = 'PKO_Tag')";
        String prev = "select id from project_roles where id = (select max(id) from project_roles where id < "+id+" and project_id = "+projectID+" and relationship_type = 'PKO_Tag')";

        //Long nextID = jdbcTemplate.queryForObject(next,new Object[] {id,projectID},Long.class);
        Long nextID = jdbcTemplate.query(next, new ResultSetExtractor<Long>() {
            @Override
            public Long extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                return resultSet.next() ? resultSet.getLong("id") : null;
            }
        });


        //Long prevID = jdbcTemplate.queryForObject(prev,new Object[] {id,projectID},Long.class);
        Long prevID = jdbcTemplate.query(prev, new ResultSetExtractor<Long>() {
            @Override
            public Long extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                return resultSet.next() ? resultSet.getLong("id") : null;
            }
        });
        Map<String,Long> projects = new HashMap<>();
        projects.put("next",nextID);
        projects.put("prev",prevID);

        return projects;
    }







}
