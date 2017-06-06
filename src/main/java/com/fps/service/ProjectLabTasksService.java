package com.fps.service;

import com.fps.domain.ProjectLabTasks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing ProjectLabTasks.
 */
public interface ProjectLabTasksService {

    /**
     * Save a projectLabTasks.
     * 
     * @param projectLabTasks the entity to save
     * @return the persisted entity
     */
    ProjectLabTasks save(ProjectLabTasks projectLabTasks);

    /**
     *  Get all the projectLabTasks.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ProjectLabTasks> findAll(Pageable pageable);

    /**
     *  Get the "id" projectLabTasks.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    ProjectLabTasks findOne(Long id);

    /**
     *  Delete the "id" projectLabTasks.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the projectLabTasks corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<ProjectLabTasks> search(String query, Pageable pageable);
}
