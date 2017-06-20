package com.fps.service.impl;

import com.fps.config.Constants;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.*;
import com.fps.elastics.search.ContactPrivilegesSearchRepository;
import com.fps.elastics.search.ProjectPurchaseOrdersSearchRepository;
import com.fps.elastics.search.ProjectsSearchRepository;
import com.fps.repository.*;
import com.fps.security.SecurityUtils;
import com.fps.service.ProjectsService;
import com.fps.web.rest.dto.ProjectsDTO;
import com.fps.web.rest.dto.TalentInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Projects.
 */
@Service
@Transactional
public class ProjectsServiceImpl implements ProjectsService {

    private final Logger log = LoggerFactory.getLogger(ProjectsServiceImpl.class);

    @Inject
    private ProjectsRepository projectsRepository;

    @Inject
    private ProjectLabTasksRepository projectLabTasksRepository;

    @Inject
    private ProjectRolesRepository projectRolesRepository;

    @Inject
    private ProjectPurchaseOrdersRepository projectPurchaseOrdersRepository;

    @Inject
    private ContactPrivilegesRepository contactPrivilegesRepository;

    @Inject
    private ProjectsSearchRepository projectsSearchRepository;


    @Inject
    private ProjectPurchaseOrdersSearchRepository projectPurchaseOrdersSearchRepository;

    @Inject
    private ContactPrivilegesSearchRepository contactPrivilegesSearchRepository;

    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    @Inject
    private LookupsRepository lookupsRepository;

    @Inject
    private ContactsRepository contactsRepository;

    @Inject
    private UserRepository userRepository;


    @Inject
    private JdbcTemplate jdbcTemplate;


    @Value("${project.rename.path}")
    private String REMOTE_PROJECT_RENAME;

    @Value("${project.logo.path}")
    private String LOGO_LOCATION;

    /**
     * Save a projects.
     *
     * @param projectsDTO the entity to save
     * @return the persisted entity
     */
    public Projects save(ProjectsDTO projectsDTO) {
        log.debug("Request to save Projects : {}", projectsDTO);

        Projects projects = projectsDTO.getProjects();
        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        final User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        projects.setCreatedDate(LocalDate.now());
        projects.setCreatedByAdminUser(user);


        currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);

        // save project in database
        Projects result = projectsRepository.save(projects);
        projectsSearchRepository.save(result);

        Set<ProjectRoles> projectRoleses = new HashSet<>();
        Set<ProjectPurchaseOrders> projectPurchaseOrderses = new HashSet<>();
        Set<ProjectLabTasks> projectLabTaskses = new HashSet<>();
        Set<ContactPrivileges> contactPrivilegeses = new HashSet<>();

        // save purchase orders in database
        if (projects.isRunOfShowFlag()) {
            log.info("PROJECT PURCHASE ORDERS");

            for (ProjectPurchaseOrders projectPurchaseOrders : projectsDTO.getProjectPurchaseOrderses()) {

                projectPurchaseOrders.setProject(projects);
                projectPurchaseOrders.setCreated_by_admin_user(user);
                projectPurchaseOrders.setCreated_date(ZonedDateTime.now());
                projectPurchaseOrderses.add(projectPurchaseOrders);
                log.info(projectPurchaseOrders.toString());

            }
            projectPurchaseOrdersRepository.save(projectPurchaseOrderses);


        }
        // save labs in database
        if (projects.isLabFlag()) {
            log.info("PROJECT LAB TASKS");

            for (ProjectLabTasks projectLabTasks : projectsDTO.getProjectLabTaskses()) {
                projectLabTasks.setProject(projects);
                projectLabTasks.setCreated_by_admin_user(user);
                projectLabTasks.setCreatedDate(ZonedDateTime.now());
                log.info(projectLabTasks.toString());
                projectLabTaskses.add(projectLabTasks);
            }
            projectLabTasksRepository.save(projectLabTaskses);
        }

        // save tags( projects roles ) in database
        int index = 0;
        log.info("PROJECT ROLES");
        for (ProjectRoles projectRoles : projectsDTO.getProjectRoles()) {
            projectRoles.setProject(projects);
            projectRoles.setCreatedDate(LocalDate.now());
            projectRoles.setCreatedByAdminUser(user);

            // addd hotkeys to tags
            if (projectRoles.getRelationship_type().equals(Constants.PKO_TAG)) {
                projectRoles.setHotkeyValue(Constants.HOTKEYS[index]);
                index++;
            }
            projectRoleses.add(projectRoles);

            log.info(projectRoles.toString());
        }
        // add default tags in PKO ROLES like Sensitive,Misc,Unknown,Crew,Background,Ensemble,Equipement
        final Contacts fixedContact = contactsRepository.findOne(Long.valueOf(5181));

        final ProjectRoles sensitive = new ProjectRoles(projects, Constants.PKO_TAG, Float.valueOf("0.00"), Float.valueOf("0.00"), null, null, true, null, null, 0, null, null, Constants.SENSITIVE, "0", null, Float.valueOf("0.00"), LocalDate.now(), null, null, fixedContact, user, null);
        final ProjectRoles misc = new ProjectRoles(projects, Constants.PKO_TAG, Float.valueOf("0.00"), Float.valueOf("0.00"), null, null, true, null, null, 0, null, null, Constants.MISC_UNKOWN, "1", null, Float.valueOf("0.00"), LocalDate.now(), null, null, fixedContact, user, null);
        final ProjectRoles background = new ProjectRoles(projects, Constants.PKO_TAG, Float.valueOf("0.00"), Float.valueOf("0.00"), null, null, true, null, null, 0, null, null, Constants.BACKGROUND, "2", null, Float.valueOf("0.00"), LocalDate.now(), null, null, fixedContact, user, null);
        final ProjectRoles crew = new ProjectRoles(projects, Constants.PKO_TAG, Float.valueOf("0.00"), Float.valueOf("0.00"), null, null, true, null, null, 0, null, null, Constants.CREW, "3", null, Float.valueOf("0.00"), LocalDate.now(), null, null, fixedContact, user, null);
        final ProjectRoles ensemble = new ProjectRoles(projects, Constants.PKO_TAG, Float.valueOf("0.00"), Float.valueOf("0.00"), null, null, true, null, null, 0, null, null, Constants.ENSEMBLE, "4", null, Float.valueOf("0.00"), LocalDate.now(), null, null, fixedContact, user, null);
        final ProjectRoles equipment = new ProjectRoles(projects, Constants.PKO_TAG, Float.valueOf("0.00"), Float.valueOf("0.00"), null, null, true, null, null, 0, null, null, Constants.EQUIPMENT, "5", null, Float.valueOf("0.00"), LocalDate.now(), null, null, fixedContact, user, null);

        projectRoleses.add(sensitive);
        projectRoleses.add(misc);
        projectRoleses.add(background);
        projectRoleses.add(crew);
        projectRoleses.add(ensemble);
        projectRoleses.add(equipment);

        projectRolesRepository.save(projectRoleses);
        log.info("PROJECT EXECS");
        for (ContactPrivileges contactPrivileges : projectsDTO.getContactPrivileges()) {
            contactPrivileges.setCreatedByAdminUser(user);
            contactPrivileges.setProject(projects);
            contactPrivileges.setCreatedDate(ZonedDateTime.now());
            contactPrivilegeses.add(contactPrivileges);
        }
        contactPrivilegesRepository.save(contactPrivilegeses);

        return result;
    }


    /**
     * Save a projects.
     *
     * @param projectsDTO the entity to save
     * @return the persisted entity
     */
    public Projects update(ProjectsDTO projectsDTO) {
        log.debug("Request to update Projects : {}", projectsDTO);

        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        final User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        Projects projects = projectsDTO.getProjects();
        projects.setUpdatedDate(LocalDate.now());
        projects.setUpdatedByAdminUser(user);
        log.debug("REST request to update Projects : {}", projects);
        currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);
        log.info("===========================");
        log.info("Size of all Related");

        log.info(" Lab Tasks : " + projectsDTO.getProjectLabTaskses().size());
        log.info(" Purchase Orders : " + projectsDTO.getProjectPurchaseOrderses().size());
        log.info(" Project Roles : " + projectsDTO.getProjectRoles().size());
        log.info(" Execs: " + projectsDTO.getContactPrivileges().size());


        // change project name on system
        Projects beforeUpdate = projectsRepository.getOne(projects.getId());
        if ((!beforeUpdate.getAlfrescoTitle1().equals(projects.getAlfrescoTitle1()) || (!beforeUpdate.getAlfrescoTitle2().equals(projects.getAlfrescoTitle2())))) {
            log.info("Project Change Name");
            try {
                File file = new File(REMOTE_PROJECT_RENAME + projects.getFullName() + ".ksh");
                PrintWriter out = new PrintWriter(file);
                out.println(REMOTE_PROJECT_RENAME
                    + " '" + projects.getImageLocation() + "'"
                    + " '" + beforeUpdate.getAlfrescoTitle1() + "'"
                    + " '" + beforeUpdate.getAlfrescoTitle2() + "'"
                    + " '" + projects.getAlfrescoTitle1() + "'"
                    + " '" + projects.getAlfrescoTitle2() + "'");
                out.close();
                log.info("Project Renamer File Created on : " + ZonedDateTime.now());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // change project name in database
        currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);
        Projects result = projectsRepository.save(projects);
        projectsSearchRepository.save(result);


        Set<ProjectRoles> projectRoleses = new HashSet<>();
        Set<ProjectPurchaseOrders> projectPurchaseOrderses = new HashSet<>();
        Set<ProjectLabTasks> projectLabTaskses = new HashSet<>();
        Set<ContactPrivileges> contactPrivilegeses = new HashSet<>();

        if (projects.isRunOfShowFlag()) {
            log.info("PROJECT PURCHASE ORDERS");

            for (ProjectPurchaseOrders projectPurchaseOrders : projectsDTO.getProjectPurchaseOrderses()) {

                projectPurchaseOrders.setProject(result);
                projectPurchaseOrders.setUpdated_by_admin_user(user);
                projectPurchaseOrders.setUpdated_date(ZonedDateTime.now());
                projectPurchaseOrderses.add(projectPurchaseOrders);
                log.info(projectPurchaseOrders.toString());

            }
            projectPurchaseOrdersRepository.save(projectPurchaseOrderses);


        }
        if (projects.isLabFlag()) {
            log.info("PROJECT LAB TASKS");
            if (projectsDTO.getProjectLabTaskses().size() > 0) {
                for (ProjectLabTasks projectLabTasks : projectsDTO.getProjectLabTaskses()) {
                    projectLabTasks.setProject(result);
                    projectLabTasks.setUpdated_by_admin_user(user);
                    projectLabTasks.setUpdatedDate(ZonedDateTime.now());
                    log.info(projectLabTasks.toString());
                    projectLabTaskses.add(projectLabTasks);
                }
                projectLabTasksRepository.save(projectLabTaskses);
            }
        }
        log.info("PROJECT ROLES");
        if (projectsDTO.getProjectRoles().size() > 0) {
            for (ProjectRoles projectRoles : projectsDTO.getProjectRoles()) {
                projectRoles.setProject(result);
                projectRoles.setUpdatedDate(LocalDate.now());
                projectRoles.setUpdatedByAdminUser(user);
                projectRoleses.add(projectRoles);
                log.info(projectRoles.toString());
            }
            projectRolesRepository.save(projectRoleses);
        }
        log.info("PROJECT EXECS");
        if (projectsDTO.getContactPrivileges().size() > 0) {
            for (ContactPrivileges contactPrivileges : projectsDTO.getContactPrivileges()) {
                contactPrivileges.setUpdatedByAdminUser(user);
                contactPrivileges.setProject(result);
                contactPrivileges.setUpdatedDate(ZonedDateTime.now());
                contactPrivilegeses.add(contactPrivileges);
            }
            contactPrivilegesRepository.save(contactPrivilegeses);
        }

        return result;
    }


    /**
     * Get all the projects.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Projects> findAll(Pageable pageable) {
        log.debug("Request to get all Projects");
        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        Page<Projects> result = projectsRepository.findAll(pageable);

        return result;
    }

    /**
     * Get one projects by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Projects findOne(Long id) {
        log.debug("Request to get Projects : {}", id);
        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        Projects projects = projectsRepository.findOne(id);
        return projects;
    }

    /**
     * Delete the  projects by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Projects : {}", id);
        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        Projects projects = projectsRepository.findOne(id);
        currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);
        projectLabTasksRepository.deleteByProject(projects);
        projectPurchaseOrdersRepository.deleteByProject(projects);
        projectRolesRepository.deleteByProject(projects);
        projectsRepository.delete(id);

        projectsSearchRepository.delete(id);
    }

    /**
     * Search for the projects corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Projects> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Projects for query {}", query);
        return projectsSearchRepository.search(queryStringQuery(query), pageable);
    }

    /**
     * Search for the projects corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Iterable<Projects> searchForList(String query) {
        log.debug("Request to search for a page of Projects for query {}", query);
        return projectsSearchRepository.search(queryStringQuery(query));
    }


    public List<Objects[]> findProjects() {
        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        return projectsRepository.findProjects();
    }


    public Page<Objects[]> getAllProjects(Pageable pageable) {
        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        return projectsRepository.getAllProjects(pageable);
    }


    public Integer saveLogo(byte[] logo) {
        Random rnd = new Random();
        String filmSolutions = "filmsolutions";
        char[] text = new char[10];
        try {
            for (int i = 0; i > 10; i++) {
                text[i] = filmSolutions.charAt(rnd.nextInt(filmSolutions.length()));
            }
            ByteArrayInputStream bufByteArrayInputStream = new ByteArrayInputStream(logo);
            BufferedImage bufferedImage = ImageIO.read(bufByteArrayInputStream);
            File file = new File(LOGO_LOCATION + text.toString() + ".jpg");
            ImageIO.write(bufferedImage, "jpg", file);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }


    public String getGbCount(Projects projects) {
        final Lookups username = lookupsRepository.findByConfigAndKey(Constants.CONFIG, Constants.USERNAME);

        final String drive = projects.getImageLocation().getName();
        final String ipAddress = projects.getImageLocation().getServer().getIpAddress();
        final String alfrescoTitle1 = projects.getAlfrescoTitle1();
        final String alfrescoTitle2 = projects.getAlfrescoTitle2();

        final String sshCommand = "ssh " + username.getTextValue() + "@" + ipAddress;
        final String command = sshCommand.concat(" du -sh /fspool/").concat(drive).concat("/Images/").concat(alfrescoTitle1).concat("/").concat(alfrescoTitle2).concat("/");


        ///fspool/brick8/Images/at1/at2 make du sh on this
        // not sure everything has fspool
        // sample command
        // du -sh /fspool/brick8/Images/Sweetheart_Proj/

        log.info("Storage GB Count Command : " + command);
        return command;
    }

    public List<TalentInfoDTO> talentInfos(Long id, String type) {
        List<TalentInfoDTO> talentInfo = new ArrayList<>();


        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);

        if (type.equals("tags")) {
            log.info("TAGS");
            final String sql = "select id,tag_name as type,`hotkey_value` as value from `project_roles`  where `relationship_type`='PKO_Tag' and project_id=" + id;
            log.info("SQL : " + sql);
            talentInfo = jdbcTemplate.query(sql, new BeanPropertyRowMapper<TalentInfoDTO>(TalentInfoDTO.class));
            log.info("Total talents from database : " + talentInfo.size());

        } else if (type.equals("privileges")) {
            log.info("PRIVILEGES");
            final String sql = "select cp.id,c.full_name as type,c.username as value from `contact_privileges` cp  inner join contacts c on cp.contact_id = c.id where project_id=" + id;
            log.info("SQL : " + sql);
            talentInfo = jdbcTemplate.query(sql, new BeanPropertyRowMapper<TalentInfoDTO>(TalentInfoDTO.class));
            log.info("Total privileges from database : " + talentInfo.size());
        } else if (type.equals("albums")) {
            log.info("ALBUMS");
            final String sql = "select id, album_name as type,album_description as value from albums where project_id=" + id;
            log.info("SQL : " + sql);
            talentInfo = jdbcTemplate.query(sql, new BeanPropertyRowMapper<TalentInfoDTO>(TalentInfoDTO.class));
            log.info("Total albums from database : " + talentInfo.size());
        }
        return talentInfo;
    }


    public void removeTalentInfo(Long id, String type) {
        currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);
        if (type.equals("tags")) {
            log.info("TAGS");
            final String sql = "delete from project_roles where id=" + id;
            log.info("SQL : " + sql);
            jdbcTemplate.execute(sql);
            log.info("Talent removed with ID : " + id);

        } else if (type.equals("privileges")) {
            log.info("PRIVILEGES");
            final String sql = "delete from contact_privileges where id=" + id;
            log.info("SQL : " + sql);
            jdbcTemplate.execute(sql);
            log.info("Contact Privilege removed with ID : " + id);
        } else if (type.equals("albums")) {
            log.info("ALBUMS");
            final String sql = "delete from albums where id=" + id;
            log.info("SQL : " + sql);
            jdbcTemplate.execute(sql);
            log.info("Album removed with ID : " + id);
        }
    }

    public void updateAlbum(TalentInfoDTO album) {

        currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);
        log.info("UPDATE ALBUM");
        final String sql = "update albums set album_name='" + album.getType() + "',album_description='" + album.getValue() + "', updated_time='" + new Timestamp(System.currentTimeMillis()) + "' where id=" + album.getId();
        log.info("SQL : " + sql);
        jdbcTemplate.execute(sql);
        log.info("Album removed with ID : " + album.getId());
    }

    public void insertAlbum(TalentInfoDTO album) {
        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        log.info(user.toString());

        currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);

        log.info("INSERT ALBUM");
        final String sql = "insert into albums(album_owner,project_id,album_name,album_permissions,album_description,album_type,created_by,created_time) values (?,?,?,?,?,?,?,?)";
        log.info("SQL : " + sql);
        jdbcTemplate.update(sql, new Object[]{user.getLogin(), album.getId(), album.getType(), user.getLogin().concat(";RW"), album.getValue(), 2, user.getLogin(), new Timestamp(System.currentTimeMillis())});
        log.info("Album inserted for project with  ID : ");
    }
}
