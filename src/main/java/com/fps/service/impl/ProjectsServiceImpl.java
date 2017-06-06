package com.fps.service.impl;

import com.fps.config.Constants;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.Projects;
import com.fps.elastics.search.ContactPrivilegesSearchRepository;
import com.fps.elastics.search.ProjectPurchaseOrdersSearchRepository;
import com.fps.elastics.search.ProjectsSearchRepository;
import com.fps.repository.*;
import com.fps.service.ProjectsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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

    final static private String MASTER = "master";
    final static private String SLAVE = "slave";

    @Value("${project.rename.path}")
    private String REMOTE_PROJECT_RENAME;

    @Value("${project.logo.path}")
    private String LOGO_LOCATION;

    /**
     * Save a projects.
     *
     * @param projects the entity to save
     * @return the persisted entity
     */
    public Projects save(Projects projects) {
        log.debug("Request to save Projects : {}", projects);
        currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);
        Projects result = projectsRepository.save(projects);
        projectsSearchRepository.save(result);
        return result;
    }


    /**
     * Save a projects.
     *
     * @param projects the entity to save
     * @return the persisted entity
     */
    public Projects update(Projects projects) {
        log.debug("Request to update Projects : {}", projects);
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

}
