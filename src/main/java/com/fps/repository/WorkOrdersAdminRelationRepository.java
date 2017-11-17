package com.fps.repository;

import com.fps.domain.WorkOrder;
import com.fps.domain.WorkOrdersAdminRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the WorkOrdersAdminRelation entity.
 */
public interface WorkOrdersAdminRelationRepository extends JpaRepository<WorkOrdersAdminRelation, Long> {

    @Query("select workOrdersAdminRelation from WorkOrdersAdminRelation workOrdersAdminRelation where workOrdersAdminRelation.admin_user.login = ?#{principal.username}")
    List<WorkOrdersAdminRelation> findByAdmin_userIsCurrentUser();

    List<WorkOrdersAdminRelation> findByWorkOrder(WorkOrder workOrder);

    @Transactional
    void deleteByWorkOrder(WorkOrder workOrder);
}
