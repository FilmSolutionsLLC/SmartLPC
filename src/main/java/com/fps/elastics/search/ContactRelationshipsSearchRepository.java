package com.fps.elastics.search;

import com.fps.domain.ContactRelationships;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ContactRelationships entity.
 */
public interface ContactRelationshipsSearchRepository extends ElasticsearchRepository<ContactRelationships, Long> {

}
