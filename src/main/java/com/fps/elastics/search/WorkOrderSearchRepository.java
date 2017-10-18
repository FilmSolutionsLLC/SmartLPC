package com.fps.elastics.search;

import com.fps.domain.Contacts;
import com.fps.domain.Projects;
import com.fps.domain.WorkOrder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Spring Data ElasticSearch repository for the WorkOrder entity.
 */
public interface WorkOrderSearchRepository extends ElasticsearchRepository<WorkOrder, Long> {

    List<WorkOrder> findByProject(String project);

    List<WorkOrder> findByPoRecord(String poRecord);

    List<WorkOrder> findByRequestor(Contacts requestor);

    List<WorkOrder> findByRequestDescription(String requestDescription);

    List<WorkOrder> findByInvoiced(String invoice);


}
