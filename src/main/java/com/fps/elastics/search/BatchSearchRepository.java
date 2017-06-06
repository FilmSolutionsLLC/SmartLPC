package com.fps.elastics.search;

import com.fps.domain.Batch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Batch entity.
 */
public interface BatchSearchRepository extends ElasticsearchRepository<Batch, Long> {
}
