package com.fps.service;

import com.fps.domain.Projects;
import com.fps.web.rest.dto.ProjectsDTO;
import com.fps.web.rest.dto.TalentInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;

/**
 * Service Interface for managing Projects.
 */
public interface ProjectsService {

    /**
     * Save a projects.
     *
     * @param projectsDTO the entity to save
     * @return the persisted entity
     */
    Projects save(ProjectsDTO projectsDTO);


    /**
     * Update a projects.
     *
     * @param projectsDTO the entity to update
     * @return the persisted entity
     */
    Projects update(ProjectsDTO projectsDTO);


    /**
     * Get all the projects.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Projects> findAll(Pageable pageable);

    /**
     * Get the "id" projects.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Projects findOne(Long id);

    /**
     * Delete the "id" projects.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the projects corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    Page<Projects> search(String query, Pageable pageable);


    Iterable<Projects> searchForList(String query);

    List<Objects[]> findProjects();

    Page<Objects[]> getAllProjects(Pageable pageable);


    Integer saveLogo(byte[] logo);

    String getGbCount(Projects projects);


    List<TalentInfoDTO> talentInfos(Long id, String type);

    void removeTalentInfo(Long id, String type);

    void updateAlbum(TalentInfoDTO talentInfoDTO);

    void insertAlbum(TalentInfoDTO talentInfoDTO);
    
    void rename(Long id,String alfrescoTitle1,String alfrescoTitle2);
    
}
