package com.fps.elastics.search;

import com.fps.domain.ProjectRoles;
import com.fps.domain.Projects;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Spring Data ElasticSearch repository for the ProjectRoles entity.
 */
public interface ProjectRolesSearchRepository extends ElasticsearchRepository<ProjectRoles, Long> {

    List<ProjectRoles> findByProject(Projects projects);
}
