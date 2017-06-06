package com.fps.elastics.search;

import com.fps.domain.Projects;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Spring Data ElasticSearch repository for the Projects entity.
 */
public interface ProjectsSearchRepository extends ElasticsearchRepository<Projects, Long> {
    List<Projects> findByDepartment(String department);

    List<Projects> findByOwner(String name);

    List<Projects>  findByName(String name);

}
