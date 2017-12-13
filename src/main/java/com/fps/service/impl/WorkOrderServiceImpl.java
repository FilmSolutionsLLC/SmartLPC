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
import com.fps.web.rest.dto.WorkOrderProcessingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private ProjectsRepository projectRepository;

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
    private JdbcTemplate jdbcTemplate;
    @Inject
    private ReportService reportService;


    final static private String MASTER = "master";
    final static private String SLAVE = "slave";

    /**
     * Save a workOrder.
     *
     * @param workOrderDTO the entity to save
     * @return the persisted entity
     */

    @Transactional
    public WorkOrder save(WorkOrderDTO workOrderDTO) {
        WorkOrder workOrder = workOrderDTO.getWorkOrder();
        log.debug("Request to save WorkOrder : {}", workOrder);
        currentTenantIdentifierResolver.setTenant(MASTER);
        if (workOrder.getProject() == null) {
            Projects project = projectRepository.findOne((long) 0);
            workOrder.setProject(project);
        }
        WorkOrder result = workOrderRepository.save(workOrder);
        //Set<WorkOrdersAdminRelation> workOrdersAdminRelations = workOrde
        workOrderSearchRepository.save(result);

        log.info("Saved WorkOrder : " + result.getId());
        if (workOrder.isIsAbc()) {
            Set<WorkOrderAbcFile> workOrderAbcFiles = new HashSet<>();
            Set<WorkOrderAbcHdd> workOrderAbcHdds = new HashSet<>();

            log.info("Saving WorkOrderAbcFile ");
            for (WorkOrderAbcFile workOrderAbcFile : workOrderDTO.getWorkOrderAbcFiles()) {
                workOrderAbcFile.setWorkOrder(result);
                workOrderAbcFiles.add(workOrderAbcFile);
                log.info(workOrderAbcFile.toString());
            }
            if (workOrderAbcFiles.size() > 0) {
                workOrderAbcFileRepository.save(workOrderAbcFiles);
            }

            log.info("Saving WorkOrderAbcHdd ");
            for (WorkOrderAbcHdd workOrderAbcHdd : workOrderDTO.getWorkOrderAbcHdds()) {
                workOrderAbcHdd.setWorkOrder(result);
                log.info(workOrderAbcHdd.toString());
                workOrderAbcHdds.add(workOrderAbcHdd);
            }
            if (workOrderAbcHdds.size() > 0) {
                workOrderAbcHddRepository.save(workOrderAbcHdds);
            }
        }

        Set<WorkOrdersAdminRelation> workOrdersAdminRelations = new HashSet<>();
        log.info("Saving WorkOrdersAdminRelation ");
        for (WorkOrdersAdminRelation workOrdersAdminRelation : workOrderDTO.getWorkOrdersAdminRelations()) {
            if (workOrdersAdminRelation.getAdmin_user() != null) {
                log.info("Saving workOrdersAdminRelation :"+workOrdersAdminRelation.getAdmin_user().getFullName()) ;

                workOrdersAdminRelation.setWorkOrder(result);
                workOrdersAdminRelations.add(workOrdersAdminRelation);
                log.info(workOrdersAdminRelation.toString());
            }
        }
        if (workOrdersAdminRelations.size() > 0) {
            log.info("Saving all workOrdersAdminRelations now");
            workOrdersAdminRelationRepository.save(workOrdersAdminRelations);
        }


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


        log.info("Request to get WorkOrder : {}", id);
        currentTenantIdentifierResolver.setTenant(SLAVE);
        WorkOrder workOrder = workOrderRepository.findOne(id);
        final List<WorkOrdersAdminRelation> workOrdersAdminRelations = workOrdersAdminRelationRepository.findByWorkOrder(workOrder);
        final List<WorkOrderAbcFile> workOrderAbcFiles = workOrderAbcFileRepository.findByWorkOrder(workOrder);
        final List<WorkOrderAbcHdd>  workOrderAbcHdds = workOrderAbcHddRepository.findByWorkOrder(workOrder);

        log.info("Total workOrdersAdminRelations : "+workOrdersAdminRelations.size());
        log.info("Total workOrderAbcFiles : "+workOrderAbcFiles.size());
        log.info("Total workOrderAbcHdds : "+workOrderAbcHdds.size());

        final Set<WorkOrdersAdminRelation> workOrdersAdminRelationSet = new HashSet<>(workOrdersAdminRelations);
        final Set<WorkOrderAbcFile> workOrderAbcFileSet = new HashSet<>(workOrderAbcFiles);
        final Set<WorkOrderAbcHdd> workOrderAbcHddSet = new HashSet<>(workOrderAbcHdds);
        final WorkOrderDTO workOrderDTO = new WorkOrderDTO(workOrder, workOrderAbcFileSet, workOrderAbcHddSet, workOrdersAdminRelationSet);

        return workOrderDTO;
    }


    /**
     * Delete the workOrder by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkOrder : {}", id);
        currentTenantIdentifierResolver.setTenant(MASTER);
        WorkOrder workOrder  = new WorkOrder();
        workOrder.setId(id);
        workOrdersAdminRelationRepository.deleteByWorkOrder(workOrder);
        workOrderAbcFileRepository.deleteByWorkOrder(workOrder);
        workOrderAbcHddRepository.deleteByWorkOrder(workOrder);
        String sql = "delete from work_order where id ="+id;
        jdbcTemplate.execute(sql);
        //workOrderRepository.delete(id);

        //workOrderSearchRepository.delete(id);
    }

    /**
     * Search for the workOrder corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WorkOrder> search(String query, Pageable pageable) {
        log.info("Request to search for a page of WorkOrders for query {}", query);

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

    public List<WorkOrderListDTO> findOpenWorkOrdersGrouped(String reportType) {

        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        Map<String, List<WorkOrderListDTO>> workOrdersMap = new HashMap<String, List<WorkOrderListDTO>>();
        final String sql = "select query from reports where name = '" + reportType + "'";
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
                /*
				 * if (flag == false) {
				 *
				 * GroupWorkOrderDTO groupWorkOrder = new GroupWorkOrderDTO();
				 * groupWorkOrder.setOwner(owner);
				 * groupWorkOrder.setWorkOrderListDTOs(finalList); // add
				 * workorder to returning list
				 * groupWorkOrders.add(groupWorkOrder);
				 *
				 * log.info(groupWorkOrder.toString()); // add to map
				 * map.put(owner, finalList);
				 *
				 * //clear workorder list for next owner finalList.clear();
				 * log.info(
				 * "=================================================================================="
				 * );
				 *
				 * flag = true; }
				 *
				 * // add current owner owner = (String)
				 * workOrder.get("Project Name");
				 */
                WorkOrderListDTO workOrderListDTO = new WorkOrderListDTO();
                workOrderListDTO.setProject_name((String) workOrder.get("Project Name"));
                workOrderListDTO.setPo((String) workOrder.get("Id"));
                workOrderListDTO.setColor((String) workOrder.get("color"));
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
            workOrderListDTO.setId(((BigInteger) workOrder.get("Id")).longValue());
            workOrderListDTO.setProject_name((String) workOrder.get("Project Name"));
            workOrderListDTO.setWorkOrderId(((BigInteger) workOrder.get("Work Order ID")).longValue());
            workOrderListDTO.setPo((String) workOrder.get("PO#"));
            workOrderListDTO.setDate((String) workOrder.get("Date"));
            workOrderListDTO.setType((String) workOrder.get("Type"));
            workOrderListDTO.setWorkDesc((String) workOrder.get("Work Description"));
            workOrderListDTO.setStatus((String) workOrder.get("Status"));
            workOrderListDTO.setTime(Float.parseFloat((String) workOrder.get("Time (Hours)")));
            workOrderListDTO.setAssignedTo((String) workOrder.get("Assigned To"));
            workOrderListDTO.setColor((String) workOrder.get("color"));
            // get only audit,invoice
            if ((reportType.equalsIgnoreCase(Constants.TO_AUDIT))
                || (reportType.equalsIgnoreCase(Constants.TO_INVOICE))) {
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

    @Override
    public List<WorkOrderListDTO> searchByVarious(String type, String query) {
        List<WorkOrderListDTO> workOrderListDTOs = new ArrayList<>();
        try {
            String searchSQL = null;

            switch (type) {
                case "project":
                    searchSQL = "select \n"
                        + "\tw.id as Id,p.name as 'Project Name',w.po_record as 'PO#',w.request_date as 'Date',l.text_value as 'Type',\n"
                        + "\tw.request_description as 'Work Description',s.text_value as 'Status',w.duration_of_service as 'Time(Hours)',\n"
                        + "\ti.text_value as 'Invoiced',au.full_name as 'Assigned To' \n" + "\tfrom work_order w \n"
                        + "\t\tinner join projects p on w.project_id = p.id \n"
                        + "\t\tinner join lookups l on l.id = w.project_type \n"
                        + "\t\tinner join lookups s on s.id = w.status\n"
                        + "\t\tinner join lookups i on i.id = w.invoiced\n"
                        + "\t\tinner join admin_user au on au.id = w.assigned_to_user_id\n" + "\twhere p.name like \"%"
                        + query + "%\" order by w.id desc";
                    break;

                case "id":
                    searchSQL = "select \n"
                        + "\tw.id as Id,p.name as 'Project Name',w.po_record as 'PO#',w.request_date as 'Date',l.text_value as 'Type',\n"
                        + "\tw.request_description as 'Work Description',s.text_value as 'Status',w.duration_of_service as 'Time(Hours)',\n"
                        + "\ti.text_value as 'Invoiced',au.full_name as 'Assigned To' \n" + "\tfrom work_order w \n"
                        + "\t\tinner join projects p on w.project_id = p.id \n"
                        + "\t\tinner join lookups l on l.id = w.project_type \n"
                        + "\t\tinner join lookups s on s.id = w.status\n"
                        + "\t\tinner join lookups i on i.id = w.invoiced\n"
                        + "\t\tinner join admin_user au on au.id = w.assigned_to_user_id\n" + "\twhere w.id  = "
                        + query + " order by w.id desc";
                    break;

                case "po":
                    searchSQL = "select \n"
                        + "\tw.id as Id,p.name as 'Project Name',w.po_record as 'PO#',w.request_date as 'Date',l.text_value as 'Type',\n"
                        + "\tw.request_description as 'Work Description',s.text_value as 'Status',w.duration_of_service as 'Time(Hours)',\n"
                        + "\ti.text_value as 'Invoiced',au.full_name as 'Assigned To' \n" + "\tfrom work_order w \n"
                        + "\t\tinner join projects p on w.project_id = p.id \n"
                        + "\t\tinner join lookups l on l.id = w.project_type \n"
                        + "\t\tinner join lookups s on s.id = w.status\n"
                        + "\t\tinner join lookups i on i.id = w.invoiced\n"
                        + "\t\tinner join admin_user au on au.id = w.assigned_to_user_id\n" + "\twhere w.po_record like \"%"
                        + query + "%\" order by w.id desc";
                    break;

                //TODO: select modal box for contact and get ID from contacts and get requestor work orders

                case "requester":
                    searchSQL = "select \n"
                        + "\tw.id as Id,p.name as 'Project Name',w.po_record as 'PO#',w.request_date as 'Date',l.text_value as 'Type',\n"
                        + "\tw.request_description as 'Work Description',s.text_value as 'Status',w.duration_of_service as 'Time(Hours)',\n"
                        + "\ti.text_value as 'Invoiced',au.full_name as 'Assigned To' \n" + "\tfrom work_order w \n"
                        + "\t\tinner join projects p on w.project_id = p.id \n"
                        + "\t\tinner join lookups l on l.id = w.project_type \n"
                        + "\t\tinner join lookups s on s.id = w.status\n"
                        + "\t\tinner join lookups i on i.id = w.invoiced\n"
                        + "\t\tinner join admin_user au on au.id = w.assigned_to_user_id\n" + "\twhere p.name like \"%"
                        + query + "%\" order by w.id desc";
                    break;

                case "desc":
                    searchSQL = "select \n"
                        + "\tw.id as Id,p.name as 'Project Name',w.po_record as 'PO#',w.request_date as 'Date',l.text_value as 'Type',\n"
                        + "\tw.request_description as 'Work Description',s.text_value as 'Status',w.duration_of_service as 'Time(Hours)',\n"
                        + "\ti.text_value as 'Invoiced',au.full_name as 'Assigned To' \n" + "\tfrom work_order w \n"
                        + "\t\tinner join projects p on w.project_id = p.id \n"
                        + "\t\tinner join lookups l on l.id = w.project_type \n"
                        + "\t\tinner join lookups s on s.id = w.status\n"
                        + "\t\tinner join lookups i on i.id = w.invoiced\n"
                        + "\t\tinner join admin_user au on au.id = w.assigned_to_user_id\n" + "\twhere w.request_description like \"%"
                        + query + "%\" order by w.id desc";
                    break;

                case "invoice":
                    searchSQL = "select \n"
                        + "\tw.id as Id,p.name as 'Project Name',w.po_record as 'PO#',w.request_date as 'Date',l.text_value as 'Type',\n"
                        + "\tw.request_description as 'Work Description',s.text_value as 'Status',w.duration_of_service as 'Time(Hours)',\n"
                        + "\ti.text_value as 'Invoiced',au.full_name as 'Assigned To' \n" + "\tfrom work_order w \n"
                        + "\t\tinner join projects p on w.project_id = p.id \n"
                        + "\t\tinner join lookups l on l.id = w.project_type \n"
                        + "\t\tinner join lookups s on s.id = w.status\n"
                        + "\t\tinner join lookups i on i.id = w.invoiced\n"
                        + "\t\tinner join admin_user au on au.id = w.assigned_to_user_id\n" + "\twhere i.text_value like \"%"
                        + query + "%\" order by w.id desc";
                    break;
                default:
                    searchSQL = "Nothing found";
                    break;
            }
            log.info("Report Query : " + searchSQL);
            final List<Map<String, Object>> workOrders = jdbcTemplate.queryForList(searchSQL);

            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

            for (Map workOrder : workOrders) {
                WorkOrderListDTO workOrderListDTO = new WorkOrderListDTO();
                workOrderListDTO.setId(((BigInteger) workOrder.get("Id")).longValue());
                workOrderListDTO.setProject_name((String) workOrder.get("Project Name"));
                workOrderListDTO.setPo((String) workOrder.get("PO#"));
                if (workOrder.get("Date") != null) {
                    workOrderListDTO.setDate(df.format(workOrder.get("Date")));
                } else {
                    workOrderListDTO.setDate("");
                }
                workOrderListDTO.setType((String) workOrder.get("Type"));

                workOrderListDTO.setWorkDesc((String) workOrder.get("Work Description"));
                workOrderListDTO.setStatus((String) workOrder.get("Status"));
                workOrderListDTO.setTime(Float.parseFloat((String) workOrder.get("Time(Hours)")));
                workOrderListDTO.setAssignedTo((String) workOrder.get("Assigned To"));

                workOrderListDTO.setAuditedBy((String) workOrder.get("Audited By"));
                workOrderListDTO.setInvoiced((String) workOrder.get("Invoiced"));

                workOrderListDTOs.add(workOrderListDTO);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return workOrderListDTOs;
    }

    public List<WorkOrderProcessingDTO> findWorkOrderProcessingDtos(String order,String field) {
        List<WorkOrderProcessingDTO> workOrderProcessingDTOList = new ArrayList<>();
        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        String sql = "select query from reports where name = 'processing_log'";
        log.info(sql);
        String query = jdbcTemplate.queryForObject(sql, new Object[]{}, String.class);
        log.info("Report Query : " + query);

        List<Map<String, Object>> workOrders = jdbcTemplate.queryForList(query.concat(" ").concat(field).concat(" ").concat(order));
        for (Map workOrder : workOrders) {
            WorkOrderProcessingDTO workOrderProcessingDTO = new WorkOrderProcessingDTO();
            workOrderProcessingDTO.setId(((BigInteger) workOrder.get("Id")).longValue());
            workOrderProcessingDTO.setProjectName((String) workOrder.get("Project Name"));
            workOrderProcessingDTO.setWorkOrderId(((BigInteger) workOrder.get("Work Order ID")).longValue());

            workOrderProcessingDTO.setDateRecieved((Date) workOrder.get("Date Recieved"));
            workOrderProcessingDTO.setHddID((String) workOrder.get("HDD ID"));
            workOrderProcessingDTO.setProofShipped((Date) workOrder.get("Proofs Shipped"));
            workOrderProcessingDTO.setDriveShipped((Date) workOrder.get("Drive Shipped"));
            workOrderProcessingDTO.setImageRange((String) workOrder.get("Image Range"));
            workOrderProcessingDTO.setImageQty((String) workOrder.get("Image Qty."));
            workOrderProcessingDTO.setPage1((String) workOrder.get("Pages 1"));
            workOrderProcessingDTO.setPage2((String) workOrder.get("Pages 2"));
            workOrderProcessingDTO.setNotes((String) workOrder.get("Notes"));
            workOrderProcessingDTO.setLocation((String) workOrder.get("Location"));
            workOrderProcessingDTO.setIsPko((String) workOrder.get("PKO"));
            workOrderProcessingDTO.setStatus((String) workOrder.get("Status"));
            workOrderProcessingDTO.setAudited((String) workOrder.get("Audited"));
            workOrderProcessingDTO.setAssignedTo((String) workOrder.get("Assigned To"));
            workOrderProcessingDTO.setProcessedBy((String) workOrder.get("Processed By"));
            workOrderProcessingDTO.setIngestedBy((String) workOrder.get("Ingested By"));
            workOrderProcessingDTO.setCompletion((Date) workOrder.get("Completion Date"));
            workOrderProcessingDTO.setColor((String) workOrder.get("Color"));


            workOrderProcessingDTOList.add(workOrderProcessingDTO);

        }
        return workOrderProcessingDTOList;
    }


}

