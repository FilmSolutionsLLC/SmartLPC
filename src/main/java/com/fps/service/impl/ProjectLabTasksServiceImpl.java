package com.fps.service.impl;

import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.ProjectLabTasks;
import com.fps.repository.ProjectLabTasksRepository;
import com.fps.elastics.search.ProjectLabTasksSearchRepository;
import com.fps.service.ProjectLabTasksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing ProjectLabTasks.
 */
@Service
@Transactional
public class ProjectLabTasksServiceImpl implements ProjectLabTasksService{

    private final Logger log = LoggerFactory.getLogger(ProjectLabTasksServiceImpl.class);

    @Inject
    private ProjectLabTasksRepository projectLabTasksRepository;

    @Inject
    private ProjectLabTasksSearchRepository projectLabTasksSearchRepository;

    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    final static private String MASTER ="master";
    final static private String SLAVE ="slave";


    /**
     * Save a projectLabTasks.
     *
     * @param projectLabTasks the entity to save
     * @return the persisted entity
     */
    public ProjectLabTasks save(ProjectLabTasks projectLabTasks) {
        log.debug("Request to save ProjectLabTasks : {}", projectLabTasks);
        currentTenantIdentifierResolver.setTenant(MASTER);
        ProjectLabTasks result = projectLabTasksRepository.save(projectLabTasks);
        projectLabTasksSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the projectLabTasks.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProjectLabTasks> findAll(Pageable pageable) {
        log.debug("Request to get all ProjectLabTasks");
        currentTenantIdentifierResolver.setTenant(SLAVE);
        Page<ProjectLabTasks> result = projectLabTasksRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one projectLabTasks by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ProjectLabTasks findOne(Long id) {
        log.debug("Request to get ProjectLabTasks : {}", id);
        currentTenantIdentifierResolver.setTenant(SLAVE);
        ProjectLabTasks projectLabTasks = projectLabTasksRepository.findOne(id);
        return projectLabTasks;
    }

    /**
     *  Delete the  projectLabTasks by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectLabTasks : {}", id);
        currentTenantIdentifierResolver.setTenant(MASTER);
        projectLabTasksRepository.delete(id);
        projectLabTasksSearchRepository.delete(id);
    }

    /**
     * Search for the projectLabTasks corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProjectLabTasks> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProjectLabTasks for query {}", query);
        return projectLabTasksSearchRepository.search(queryStringQuery(query), pageable);
    }
}
