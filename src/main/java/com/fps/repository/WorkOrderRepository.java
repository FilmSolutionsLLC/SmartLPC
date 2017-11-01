package com.fps.repository;

import com.fps.domain.Lookups;
import com.fps.domain.User;
import com.fps.domain.WorkOrder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the WorkOrder entity.
 */
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {

    @Query("select workOrder from WorkOrder workOrder where workOrder.assignedToUser.login = ?#{principal.username}")
    List<WorkOrder> findByAssignedToUserIsCurrentUser();


    @Query("select workOrder from WorkOrder workOrder where workOrder.assignedToUser = :user and workOrder.status.id != 101")
    List<WorkOrder> findByAssignedToUser(@Param("user") User user);

    @Query("select workOrder from WorkOrder workOrder where workOrder.createdBy.login = ?#{principal.username}")
    List<WorkOrder> findByCreatedByIsCurrentUser();

    @Query("select workOrder from WorkOrder workOrder where workOrder.updatedBy.login = ?#{principal.username}")
    List<WorkOrder> findByUpdatedByIsCurrentUser();


    // MY OPEN WORK ORDERS
    @Query("select workOrder from WorkOrder workOrder where workOrder.auditedBy.login = ?#{principal.username}")
    List<WorkOrder> findByAuditedByIsCurrentUser();


    //OPEN WORK ORDERS
    @Query("select workOrder from WorkOrder workOrder where workOrder.status.id != 101")
    Page<WorkOrder> findByStatus(Pageable pageable);


    //GROUPED OPEN WORK ORDER
    @Query("select workOrder from WorkOrder workOrder where workOrder.status.id != 101")
    Page<WorkOrder> findByStatusGrouped(Pageable pageable);

    //TO AUDIT
    @Query("select workOrder from WorkOrder workOrder where workOrder.status.id = 101 and workOrder.auditedFlag = 0")
    List<WorkOrder> findByStatusAndAudited();


    // TO INVOICE
    @Query("select workOrder from WorkOrder workOrder where workOrder.status.id = 101 and workOrder.auditedFlag = 0 and workOrder.invoiced = 0")
    List<WorkOrder> findByStatusAndAuditedAndInvoiced();


    //PRCESSING LOG
    //@EntityGraph(attributePaths="workOrdersAdminRelations")
    @Query("select workOrder from WorkOrder workOrder where workOrder.processing_pko_flag.id = 219 ORDER BY workOrder.id DESC")
    List<WorkOrder> findByProcessing_pko_flag();

}
