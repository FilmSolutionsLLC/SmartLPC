package com.fps.elastics.search;

import com.fps.domain.IngestServer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by macbookpro on 6/5/17.
 */
public interface IngestServerSearchRepository extends ElasticsearchRepository<IngestServer, Long> {
}
