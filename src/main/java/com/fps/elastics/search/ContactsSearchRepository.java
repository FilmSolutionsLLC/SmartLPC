package com.fps.elastics.search;

import com.fps.domain.Contacts;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Contacts entity.
 */
public interface ContactsSearchRepository extends ElasticsearchRepository<Contacts, Long> {
}
