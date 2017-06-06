package com.fps.elastics.search;

import com.fps.domain.Storage_Servers;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Storage_Servers entity.
 */
public interface Storage_ServersSearchRepository extends ElasticsearchRepository<Storage_Servers, Long> {
}
