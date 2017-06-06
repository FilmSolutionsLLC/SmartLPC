package com.fps.elastics.search;

import com.fps.domain.ProjectPurchaseOrders;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ProjectPurchaseOrders entity.
 */
public interface ProjectPurchaseOrdersSearchRepository extends ElasticsearchRepository<ProjectPurchaseOrders, Long> {
}
