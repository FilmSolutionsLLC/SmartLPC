package com.fps.elastics.search;

import com.fps.domain.Departments;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Departments entity.
 */
public interface DepartmentsSearchRepository extends ElasticsearchRepository<Departments, Long> {
}
