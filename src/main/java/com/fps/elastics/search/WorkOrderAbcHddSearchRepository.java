package com.fps.elastics.search;

import com.fps.domain.WorkOrderAbcHdd;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the WorkOrderAbcHdd entity.
 */
public interface WorkOrderAbcHddSearchRepository extends ElasticsearchRepository<WorkOrderAbcHdd, Long> {
}
