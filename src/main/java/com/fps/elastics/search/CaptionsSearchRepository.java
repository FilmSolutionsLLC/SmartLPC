package com.fps.elastics.search;

import com.fps.domain.Captions;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Captions entity.
 */
public interface CaptionsSearchRepository extends ElasticsearchRepository<Captions, Long> {
	

}
