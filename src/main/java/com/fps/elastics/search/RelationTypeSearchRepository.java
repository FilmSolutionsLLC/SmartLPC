package com.fps.elastics.search;

import com.fps.domain.RelationType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the RelationType entity.
 */
public interface RelationTypeSearchRepository extends ElasticsearchRepository<RelationType, Long> {
}
