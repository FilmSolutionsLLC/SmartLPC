package com.fps.elastics.search;

import com.fps.domain.WorkOrderAbcFile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the WorkOrderAbcFile entity.
 */
public interface WorkOrderAbcFileSearchRepository extends ElasticsearchRepository<WorkOrderAbcFile, Long> {
}
