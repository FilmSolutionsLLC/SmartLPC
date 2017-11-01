package com.fps.service;

import com.fps.domain.User;
import com.fps.domain.WorkOrder;
import com.fps.web.rest.dto.WorkOrderDTO;
import com.fps.web.rest.dto.WorkOrderListDTO;
import com.fps.web.rest.dto.WorkOrderProcessingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing WorkOrder.
 */
public interface WorkOrderService {

    /**
     * Save a workOrder.
     *
     * @param workOrderDTO the entity to save
     * @return the persisted entitys
     */
    WorkOrder save(WorkOrderDTO workOrderDTO);

    /**
     * Get all the workOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<WorkOrder> findAll(Pageable pageable);

    /**
     * Get the "id" workOrder.
     *
     * @param id the id of the entity
     * @return the entity
     */
    WorkOrderDTO findOne(Long id);

    /**
     * Delete the "id" workOrder.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the workOrder corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    Page<WorkOrder> search(String query, Pageable pageable);


    List<WorkOrderListDTO> findOpenWorkOrders();

    List<WorkOrderListDTO> findOpenWorkOrdersGrouped(String reportType);

    List<WorkOrderListDTO> findToAudit();

    List<WorkOrderListDTO> findWorkOrdersByReport(String reportType);

    List<WorkOrder> findMyOpenWorkOrders(User user);

    List<WorkOrderListDTO> searchByVarious(String type,String query);

    List<WorkOrderProcessingDTO> findWorkOrderProcessingDtos();
}


