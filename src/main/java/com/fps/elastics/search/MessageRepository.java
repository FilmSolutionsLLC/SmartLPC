package com.fps.elastics.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.fps.domain.Lookups;
import com.fps.domain.Message;

public interface MessageRepository  extends ElasticsearchRepository<Message, Long> {

}
