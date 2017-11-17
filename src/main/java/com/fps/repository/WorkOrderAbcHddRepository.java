package com.fps.repository;

import com.fps.domain.WorkOrder;
import com.fps.domain.WorkOrderAbcHdd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the WorkOrderAbcHdd entity.
 */
public interface WorkOrderAbcHddRepository extends JpaRepository<WorkOrderAbcHdd, Long> {
    List<WorkOrderAbcHdd> findByWorkOrder(WorkOrder workOrder);


    @Transactional
    void deleteByWorkOrder(WorkOrder workOrder);
}
