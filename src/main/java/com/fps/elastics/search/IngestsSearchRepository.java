package com.fps.elastics.search;

import com.fps.domain.Ingests;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by macbookpro on 2/2/17.
 */
public interface IngestsSearchRepository extends ElasticsearchRepository<Ingests, Long> {

}
