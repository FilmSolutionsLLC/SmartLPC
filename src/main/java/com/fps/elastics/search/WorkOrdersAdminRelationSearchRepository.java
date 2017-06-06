package com.fps.elastics.search;

import com.fps.domain.WorkOrdersAdminRelation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the WorkOrdersAdminRelation entity.
 */
public interface WorkOrdersAdminRelationSearchRepository extends ElasticsearchRepository<WorkOrdersAdminRelation, Long> {
}
