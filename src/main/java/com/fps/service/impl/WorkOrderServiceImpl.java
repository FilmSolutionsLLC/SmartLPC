package com.fps.service.impl;

import com.fps.config.Constants;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.*;
import com.fps.elastics.search.WorkOrderSearchRepository;
import com.fps.repository.*;
import com.fps.service.ReportService;
import com.fps.service.WorkOrderService;
import com.fps.web.rest.dto.GroupWorkOrderDTO;
import com.fps.web.rest.dto.WorkOrderDTO;
import com.fps.web.rest.dto.WorkOrderListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing WorkOrder.
 */
@Service
@Transactional
public class WorkOrderServiceImpl implements WorkOrderService {

    private final Logger log = LoggerFactory.getLogger(WorkOrderServiceImpl.class);

    @Inject
    private WorkOrderRepository workOrderRepository;

    @Inject
    private WorkOrderSearchRepository workOrderSearchRepository;


    @Inject
    private WorkOrderAbcFileRepository workOrderAbcFileRepository;

    @Inject
    private WorkOrderAbcHddRepository workOrderAbcHddRepository;

    @Inject
    private WorkOrdersAdminRelationRepository workOrdersAdminRelationRepository;


    @Inject
    private UserRepository userRepository;


    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    @Inject
    private ReportService reportService;

    @Inject
    private JdbcTemplate jdbcTemplate;

    final static private String MASTER = "master";
    final static private String SLAVE = "slave";

    /**
     * Save a workOrder.
     *
     * @param workOrder the entity to save
     * @return the persisted entity
     */
    public WorkOrder save(WorkOrder workOrder) {
        log.debug("Request to save WorkOrder : {}", workOrder);
        currentTenantIdentifierResolver.setTenant(MASTER);
        WorkOrder result = workOrderRepository.save(workOrder);
        workOrderSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the workOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WorkOrder> findAll(Pageable pageable) {
        log.debug("Request to get all WorkOrders");
        currentTenantIdentifierResolver.setTenant(SLAVE);
        Page<WorkOrder> result = workOrderRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one workOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public WorkOrderDTO findOne(Long id) {
        log.debug("Request to get WorkOrder : {}", id);
        currentTenantIdentifierResolver.setTenant(SLAVE);
        WorkOrder workOrder = workOrderRepository.findOne(id);
        final List<WorkOrdersAdminRelation> workOrdersAdminRelations = workOrdersAdminRelationRepository.findByWorkOrder(workOrder);
        final List<WorkOrderAbcFile> workOrderAbcFiles = workOrderAbcFileRepository.findByWorkOrder(workOrder);
        final List<WorkOrderAbcHdd> workOrderAbcHdds = workOrderAbcHddRepository.findByWorkOrder(workOrder);

        final Set<WorkOrdersAdminRelation> workOrdersAdminRelationSet = new HashSet<>(workOrdersAdminRelations);
        final Set<WorkOrderAbcFile> workOrderAbcFileSet = new HashSet<>(workOrderAbcFiles);
        final Set<WorkOrderAbcHdd> workOrderAbcHddSet = new HashSet<>(workOrderAbcHdds);
        final WorkOrderDTO workOrderDTO = new WorkOrderDTO(workOrder, workOrderAbcFileSet, workOrderAbcHddSet, workOrdersAdminRelationSet);

        return workOrderDTO;
    }

    /**
     * Delete the  workOrder by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkOrder : {}", id);
        currentTenantIdentifierResolver.setTenant(MASTER);
        workOrderRepository.delete(id);
        workOrderSearchRepository.delete(id);
    }

    /**
     * Search for the workOrder corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WorkOrder> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WorkOrders for query {}", query);
        return workOrderSearchRepository.search(queryStringQuery(query), pageable);
    }


    public List<WorkOrderListDTO> findOpenWorkOrders() {

        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        String sql = "select query from reports where name = '" + Constants.OPEN_WORK_ORDERS + "'";
        String query = jdbcTemplate.queryForObject(sql, new Object[]{}, String.class);

        List<Map<String, Object>> workOrders = jdbcTemplate.queryForList(query);
        List<WorkOrderListDTO> workOrderListDTOs = new ArrayList<>();
        for (Map workOrder : workOrders) {
            WorkOrderListDTO workOrderListDTO = new WorkOrderListDTO();
            workOrderListDTO.setId((Long) workOrder.get("Id"));
            workOrderListDTO.setProject_name((String) workOrder.get("Project Name"));
            workOrderListDTO.setWorkOrderId((Long) workOrder.get("Work Order ID"));
            workOrderListDTO.setPo((String) workOrder.get("PO#"));
            workOrderListDTO.setDate((String) workOrder.get("Date"));
            workOrderListDTO.setType((String) workOrder.get("Type"));
            workOrderListDTO.setWorkDesc((String) workOrder.get("Work Description"));
            workOrderListDTO.setStatus((String) workOrder.get("Status"));
            workOrderListDTO.setTime((Float) workOrder.get("Time(Hours)"));
            workOrderListDTO.setAssignedTo((String) workOrder.get("Assigned To"));
            workOrderListDTO.setColor((String) workOrder.get("color"));
            workOrderListDTOs.add(workOrderListDTO);
        }

        return workOrderListDTOs;
    }


    public List<WorkOrderListDTO> findOpenWorkOrdersGrouped() {



        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        Map<String, List<WorkOrderListDTO>> workOrdersMap = new HashMap<String, List<WorkOrderListDTO>>();
        final String sql = "select query from reports where name = '" + Constants.OPEN_WORK_ORDERS_GROUPED + "'";
        log.info("Query : " + sql);

        final String query = jdbcTemplate.queryForObject(sql, new Object[]{}, String.class);

        final List<Map<String, Object>> workOrders = jdbcTemplate.queryForList(query);

        List<GroupWorkOrderDTO> groupWorkOrders = new ArrayList<GroupWorkOrderDTO>();
        List<WorkOrderListDTO> finalList = new ArrayList<>();

        String owner = null;
        boolean flag = true;

        Map<String, List<WorkOrderListDTO>> map = new HashMap<>();


        for (Map workOrder : workOrders) {
            // check for row if owner info specified

            String temp = (String) workOrder.get("Id");

            if (temp.equals("HIDE")) {

                // add owner and his list to map and clear list for next owner
              /*  if (flag == false) {

                    GroupWorkOrderDTO groupWorkOrder = new GroupWorkOrderDTO();
                    groupWorkOrder.setOwner(owner);
                    groupWorkOrder.setWorkOrderListDTOs(finalList);
                    // add workorder to returning list
                    groupWorkOrders.add(groupWorkOrder);

                    log.info(groupWorkOrder.toString());
                    // add to map
                    map.put(owner, finalList);

                    //clear workorder list for next owner
                    finalList.clear();
                    log.info("==================================================================================");

                    flag = true;
                }

                // add current owner
                owner = (String) workOrder.get("Project Name");*/
                WorkOrderListDTO workOrderListDTO = new WorkOrderListDTO();
                workOrderListDTO.setProject_name((String) workOrder.get("Project Name"));
                workOrderListDTO.setPo((String) workOrder.get("Id"));
                workOrderListDTO.setColor((String)workOrder.get("color"));
                finalList.add(workOrderListDTO);
            } else {
                WorkOrderListDTO workOrderListDTO = new WorkOrderListDTO();
                workOrderListDTO.setId(Long.valueOf((String) workOrder.get("Id")));
                workOrderListDTO.setProject_name((String) workOrder.get("Project Name"));
                workOrderListDTO.setWorkOrderId(Long.valueOf((String) workOrder.get("Work Order ID")));
                workOrderListDTO.setPo((String) workOrder.get("PO#"));
                workOrderListDTO.setDate((String) workOrder.get("Date"));
                workOrderListDTO.setType((String) workOrder.get("Type"));
                workOrderListDTO.setWorkDesc((String) workOrder.get("Work Description"));
                workOrderListDTO.setStatus((String) workOrder.get("Status"));
                workOrderListDTO.setTime((Float) workOrder.get("Time(Hours)"));
                workOrderListDTO.setAssignedTo((String) workOrder.get("Assigned To"));
                workOrderListDTO.setColor((String) workOrder.get("color"));


                // add workorders of associated owner
                finalList.add(workOrderListDTO);
                flag = false;
            }
        }
        return finalList;

    }


    public List<WorkOrderListDTO> findToAudit() {

        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        String sql = "select query from reports where name = '" + Constants.TO_AUDIT + "'";
        String query = jdbcTemplate.queryForObject(sql, new Object[]{}, String.class);

        List<Map<String, Object>> workOrders = jdbcTemplate.queryForList(query);
        List<WorkOrderListDTO> workOrderListDTOs = new ArrayList<>();
        for (Map workOrder : workOrders) {
            WorkOrderListDTO workOrderListDTO = new WorkOrderListDTO();
            workOrderListDTO.setId((Long) workOrder.get("Id"));
            workOrderListDTO.setProject_name((String) workOrder.get("Project Name"));
            workOrderListDTO.setWorkOrderId((Long) workOrder.get("Work Order ID"));
            workOrderListDTO.setPo((String) workOrder.get("PO#"));
            workOrderListDTO.setDate((String) workOrder.get("Date"));
            workOrderListDTO.setType((String) workOrder.get("Type"));

            workOrderListDTO.setWorkDesc((String) workOrder.get("Work Description"));
            workOrderListDTO.setStatus((String) workOrder.get("Status"));
            workOrderListDTO.setTime((Float) workOrder.get("Time(Hours)"));
            workOrderListDTO.setAssignedTo((String) workOrder.get("Assigned To"));
            workOrderListDTO.setColor((String) workOrder.get("color"));
            // get only
            workOrderListDTO.setPko((String) workOrder.get("PKO"));
            workOrderListDTO.setAudited((String) workOrder.get("Audited"));
            workOrderListDTO.setAuditedBy((String) workOrder.get("Audited By"));
            workOrderListDTO.setInvoiced((String) workOrder.get("Invoiced"));

            workOrderListDTOs.add(workOrderListDTO);

        }

        return workOrderListDTOs;
    }


    public List<WorkOrderListDTO> findWorkOrdersByReport(String reportType) {
        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        String sql = "select query from reports where name = '" + reportType + "'";
        log.info(sql);
        String query = jdbcTemplate.queryForObject(sql, new Object[]{}, String.class);
        log.info("Report Query : " + query);

        List<Map<String, Object>> workOrders = jdbcTemplate.queryForList(query);
        List<WorkOrderListDTO> workOrderListDTOs = new ArrayList<>();

        for (Map workOrder : workOrders) {
            WorkOrderListDTO workOrderListDTO = new WorkOrderListDTO();
            workOrderListDTO.setId((Long) workOrder.get("Id"));
            workOrderListDTO.setProject_name((String) workOrder.get("Project Name"));
            workOrderListDTO.setWorkOrderId((Long) workOrder.get("Work Order ID"));
            workOrderListDTO.setPo((String) workOrder.get("PO#"));
            workOrderListDTO.setDate((String) workOrder.get("Date"));
            workOrderListDTO.setType((String) workOrder.get("Type"));
            workOrderListDTO.setWorkDesc((String) workOrder.get("Work Description"));
            workOrderListDTO.setStatus((String) workOrder.get("Status"));
            workOrderListDTO.setTime((Float) workOrder.get("Time(Hours)"));
            workOrderListDTO.setAssignedTo((String) workOrder.get("Assigned To"));
            workOrderListDTO.setColor((String) workOrder.get("color"));
            // get only audit,invoice
            if ((reportType.equalsIgnoreCase(Constants.TO_AUDIT)) || (reportType.equalsIgnoreCase(Constants.TO_INVOICE))) {
                log.info("inside if");
                workOrderListDTO.setPko((String) workOrder.get("PKO"));
                workOrderListDTO.setAudited((String) workOrder.get("Audited"));
                workOrderListDTO.setAuditedBy((String) workOrder.get("Audited By"));
                workOrderListDTO.setInvoiced((String) workOrder.get("Invoiced"));
            }
            workOrderListDTOs.add(workOrderListDTO);
        }

        return workOrderListDTOs;
    }


    public List<WorkOrder> findMyOpenWorkOrders(User user) {


        List<WorkOrder> workOrders = workOrderRepository.findByAssignedToUser(user);
        return workOrders;
    }

}
