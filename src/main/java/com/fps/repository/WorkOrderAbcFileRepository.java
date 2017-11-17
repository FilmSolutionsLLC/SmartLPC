package com.fps.repository;

import com.fps.domain.WorkOrder;
import com.fps.domain.WorkOrderAbcFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the WorkOrderAbcFile entity.
 */
public interface WorkOrderAbcFileRepository extends JpaRepository<WorkOrderAbcFile, Long> {

    List<WorkOrderAbcFile> findByWorkOrder(WorkOrder workOrder);

    @Transactional
    void deleteByWorkOrder(WorkOrder workOrder);

}
