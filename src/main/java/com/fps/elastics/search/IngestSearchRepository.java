package com.fps.elastics.search;

import com.fps.domain.Ingest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Ingest entity.
 */
public interface IngestSearchRepository extends ElasticsearchRepository<Ingest, Long> {
}
