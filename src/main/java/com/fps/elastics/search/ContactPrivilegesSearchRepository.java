package com.fps.elastics.search;

import com.fps.domain.ContactPrivileges;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ContactPrivileges entity.
 */
public interface ContactPrivilegesSearchRepository extends ElasticsearchRepository<ContactPrivileges, Long> {
}
