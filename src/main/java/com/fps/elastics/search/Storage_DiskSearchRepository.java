package com.fps.elastics.search;

import com.fps.domain.Storage_Disk;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Storage_Disk entity.
 */
public interface Storage_DiskSearchRepository extends ElasticsearchRepository<Storage_Disk, Long> {
}
