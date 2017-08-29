package com.fps.elastics.search;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.fps.domain.Contacts;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
/**
 * Spring Data ElasticSearch repository for the Contacts entity.
 */
public interface ContactsSearchRepository extends ElasticsearchRepository<Contacts, Long> {
	 
	//Page<Contacts> findByFullNameContaining(queryStringQuery(String query),Pageable pageable);
}
