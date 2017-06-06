package com.fps.elastics.search;

import com.fps.domain.Lookups;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Lookups entity.
 */
public interface LookupsSearchRepository extends ElasticsearchRepository<Lookups, Long> {
}
