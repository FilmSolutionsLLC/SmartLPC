package com.fps.elastics.search;

import com.fps.domain.ProjectLabTasks;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ProjectLabTasks entity.
 */
public interface ProjectLabTasksSearchRepository extends ElasticsearchRepository<ProjectLabTasks, Long> {
}
