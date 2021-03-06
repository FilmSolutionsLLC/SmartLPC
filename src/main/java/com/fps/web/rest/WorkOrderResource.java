package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.*;
import com.fps.elastics.search.WorkOrderSearchRepository;
import com.fps.repository.*;
import com.fps.security.SecurityUtils;
import com.fps.service.ReportService;
import com.fps.service.WorkOrderService;
import com.fps.web.rest.dto.ProjectInfoReportsDTO;
import com.fps.web.rest.dto.WorkOrderDTO;
import com.fps.web.rest.dto.WorkOrderListDTO;
import com.fps.web.rest.dto.WorkOrderProcessingDTO;
import com.fps.web.rest.util.HeaderUtil;
import com.fps.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * REST controller for managing WorkOrder.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WorkOrderResource {

	private final Logger log = LoggerFactory.getLogger(WorkOrderResource.class);

	@Inject
	private WorkOrderService workOrderService;
	@Inject
	private UserRepository userRepository;

	@Inject
	private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

	final static private String MASTER = "master";
	final static private String SLAVE = "slave";

	@Inject
	private WorkOrderRepository workOrderRepository;

	@Inject
	private WorkOrderAbcFileRepository workOrderAbcFileRepository;

	@Inject
	private WorkOrderAbcHddRepository workOrderAbcHddRepository;

	@Inject
	private WorkOrdersAdminRelationRepository workOrdersAdminRelationRepository;

	@Inject
	private WorkOrderSearchRepository workOrderSearchRepository;

	@Inject
	private ReportService reportService;

	@Inject
	private JdbcTemplate jdbcTemplate;

	@Inject
	private LookupsRepository lookupsRepository;

	/**
	 * POST /work-orders : Create a new workOrder.
	 *
	 * @param workOrderDTO
	 *            the workOrder to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new workOrder, or with status 400 (Bad Request) if the workOrder
	 *         has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/work-orders", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<WorkOrder> createWorkOrder(@RequestBody WorkOrderDTO workOrderDTO) throws URISyntaxException {
		log.info("REST request to save WorkOrder : {}", workOrderDTO);
		WorkOrder workOrder = workOrderDTO.getWorkOrder();

		currentTenantIdentifierResolver.setTenant(SLAVE);
		User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

		workOrder.setCreatedBy(user);
		workOrder.setCreatedDate(ZonedDateTime.now());

		if (workOrder.getId() != null) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert("workOrder", "idexists", "A new workOrder cannot already have an ID"))
					.body(null);
		}
		currentTenantIdentifierResolver.setTenant(MASTER);
		WorkOrder result = workOrderService.save(workOrderDTO);

		return ResponseEntity.created(new URI("/api/work-orders/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("workOrder", result.getId().toString())).body(result);
	}

	/**
	 * PUT /work-orders : Updates an existing workOrder.
	 *
	 * @param workOrderDTO
	 *            the workOrder to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         workOrder, or with status 400 (Bad Request) if the workOrder is
	 *         not valid, or with status 500 (Internal Server Error) if the
	 *         workOrder couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/work-orders", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<WorkOrder> updateWorkOrder(@RequestBody WorkOrderDTO workOrderDTO) throws URISyntaxException {
		/*
		 * log.debug("REST request to update WorkOrder : {}", workOrderDTO);
		 * WorkOrder workOrder = workOrderDTO.getWorkOrder(); if
		 * (workOrder.getId() == null) { return createWorkOrder(workOrderDTO); }
		 * currentTenantIdentifierResolver.setTenant(SLAVE); User user =
		 * userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).
		 * get();
		 *
		 * workOrder.setUpdatedBy(user);
		 * workOrder.setUpdatedDate(ZonedDateTime.now());
		 *
		 * WorkOrder result = workOrderService.save(workOrder);
		 */
		log.info("REST request to update WorkOrder : {}", workOrderDTO);
		WorkOrder workOrder = workOrderDTO.getWorkOrder();
        currentTenantIdentifierResolver.setTenant(SLAVE);
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        workOrder.setUpdatedBy(user);
		workOrder.setUpdatedDate(ZonedDateTime.now());
		currentTenantIdentifierResolver.setTenant(MASTER);
		WorkOrder result = workOrderService.save(workOrderDTO);

		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert("workOrder", workOrder.getId().toString())).body(result);
	}

	/**
	 * GET /work-orders : get all the workOrders.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         workOrders in body
	 * @throws URISyntaxException
	 *             if there is an error to generate the pagination HTTP headers
	 */
	@RequestMapping(value = "/work-orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<WorkOrder>> getAllWorkOrders(Pageable pageable) throws URISyntaxException {
		Page<WorkOrder> page = workOrderService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/work-orders");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /work-orders/:id : get the "id" workOrder.
	 *
	 * @param id
	 *            the id of the workOrder to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         workOrder, or with status 404 (Not Found)
	 */
	@RequestMapping(value = "/work-orders/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<WorkOrderDTO> getWorkOrder(@PathVariable Long id) {
		log.info("REST request to get WorkOrder : {}", id);
		final WorkOrderDTO workOrderDTO = workOrderService.findOne(id);

		return Optional.ofNullable(workOrderDTO).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /work-orders/:id : delete the "id" workOrder.
	 *
	 * @param id
	 *            the id of the workOrder to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@RequestMapping(value = "/work-orders/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deleteWorkOrder(@PathVariable Long id) {
		log.info("REST request to delete WorkOrder : {}", id);
		workOrderService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("workOrder", id.toString())).build();
	}

	/**
	 * SEARCH /_search/work-orders?query=:query : search for the workOrder
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the workOrder search
	 * @return the result of the search
	 */
	@RequestMapping(value = "/_search/work-orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<WorkOrder>> searchWorkOrders(@RequestParam String query, Pageable pageable)
			throws URISyntaxException {
		log.info("REST request to search for a page of WorkOrders for query {}", query);
		Page<WorkOrder> page = workOrderService.search(query, pageable);
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
				"/api/_search/work-orders");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * SEARCH /_search/work-orders?query=:query : search for the workOrder
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the workOrder search
	 * @return the result of the search
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/_search/filters/work-orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<WorkOrder> searchWorkOrdersOnCriteria(@RequestParam String query, @RequestParam String filter)
			throws URISyntaxException {
		log.info("REST request to search for a page of WorkOrders for query {}", query + " and filer :" + filter);
		List<WorkOrder> workOrders = new ArrayList<>();
		if (filter.equals("project")) {
			workOrders = (List<WorkOrder>) workOrderSearchRepository.findByProject(query);
		} else if (filter.equals("id")) {
			workOrders = (List<WorkOrder>) workOrderSearchRepository.findOne(Long.valueOf(query));
		} else if (filter.equals("name")) {

		} else if (filter.equals("description")) {
			workOrders = (List<WorkOrder>) workOrderSearchRepository.findByRequestDescription(query);
		} else if (filter.equals("invoice")) {
			workOrders = (List<WorkOrder>) workOrderSearchRepository.findByInvoiced(query);
		}

		return workOrders;

	}

	/**
	 * OPEN WORK ORDERS
	 *
	 * @return list of open work orders
	 * @throws URISyntaxException
	 */
	@RequestMapping(value = "/reports/work-orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<WorkOrderListDTO> workOrderReports(@RequestParam String reportType,@RequestParam String sortType,@RequestParam String sortOrder) throws URISyntaxException {

		final List<WorkOrderListDTO> workOrderListDTOList = workOrderService.findWorkOrdersByReport(reportType,sortType,sortOrder);
		log.info("Total Open Work Orders : " + workOrderListDTOList.size());
		return workOrderListDTOList;
	}

	/**
	 * OPEN WORK ORDERS
	 *
	 * @return list of open work orders
	 * @throws URISyntaxException
	 */
	@RequestMapping(value = "/openworkorders/work-orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<WorkOrderListDTO> openWorkOrders() throws URISyntaxException {
		log.info("Request for work orders :  OPENED");

		final List<WorkOrderListDTO> workOrderListDTOList = workOrderService.findOpenWorkOrders();
		log.info("Total Open Work Orders : " + workOrderListDTOList.size());
		return workOrderListDTOList;
	}

	/**
	 * OPEN WORK ORDERS WHICH ARE GROUPED
	 *
	 * @return list of open work orders grouped
	 * @throws URISyntaxException
	 */
	@RequestMapping(value = "/grouped/work-orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<WorkOrderListDTO> openWorkOrdersGrouped() throws URISyntaxException {

		final List<WorkOrderListDTO> workOrderListDTOList = workOrderService.findOpenWorkOrdersGrouped("open_work_orders_grouped");
		log.info("Total Open Grouped Work Orders : " + workOrderListDTOList.size());

		return workOrderListDTOList;

	}

    @RequestMapping(value = "/included/work-orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WorkOrderListDTO> includedComp() throws URISyntaxException {

        final List<WorkOrderListDTO> workOrderListDTOList = workOrderService.findOpenWorkOrdersGrouped("included_comp");
        log.info("Total Open Grouped Work Orders : " + workOrderListDTOList.size());

        return workOrderListDTOList;

    }


    /**
	 * WORK ORDERS TO AUDIT
	 *
	 * @return list of work orders TO AUDIT
	 * @throws URISyntaxException
	 */
	@RequestMapping(value = "/audit/work-orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<WorkOrderListDTO> toAudit() throws URISyntaxException {
		log.info("Request for work orders :  TO AUDIT");

		final List<WorkOrderListDTO> workOrderListDTOList = workOrderService.findToAudit();
		log.info("Total To Audit Work Orders : " + workOrderListDTOList.size());
		return workOrderListDTOList;
	}

	/**
	 * WORK ORDERS TO INVOICE
	 *
	 * @return list of work orders TO INVOICE
	 * @throws URISyntaxException
	 */
	@RequestMapping(value = "/invoice/work-orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<WorkOrder> toInvoice() throws URISyntaxException {
		currentTenantIdentifierResolver.setTenant(SLAVE);
		final List<WorkOrder> workOrderList = workOrderRepository.findByStatusAndAuditedAndInvoiced();
		return workOrderList;
	}

	/**
	 * MY OPEN WORK ORDERS
	 *
	 * @return my open work orders
	 * @throws URISyntaxException
	 */
	@RequestMapping(value = "/my-open/work-orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<WorkOrder> myOpenWO() throws URISyntaxException {
		currentTenantIdentifierResolver.setTenant(SLAVE);

		User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

		// final List<WorkOrder> workOrderList =
		// workOrderRepository.findByAssignedToUserIsCurrentUser();
		final List<WorkOrder> workOrderList = workOrderService.findMyOpenWorkOrders(user);
		return workOrderList;
	}

	/**
	 * MY OPEN WORK ORDERS
	 *
	 * @throws URISyntaxException
	 */
	@RequestMapping(value = "/project-info/admin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<ProjectInfoReportsDTO> projectInfoAdmin() throws URISyntaxException {
		currentTenantIdentifierResolver.setTenant(SLAVE);
		final User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

		final String sqlQuery = "select * from admin_default_reports where admin_id=" + user.getId();
		System.out.println(sqlQuery);
		@SuppressWarnings({ "unchecked", "rawtypes" })

		final List<ProjectInfoReportsDTO> reportsDTOs = jdbcTemplate.query(sqlQuery,
				new BeanPropertyRowMapper(ProjectInfoReportsDTO.class));
		return reportsDTOs;
	}

	@RequestMapping(value = "/processing/work-orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<WorkOrderProcessingDTO> processingWorkOrders(@RequestParam String order,@RequestParam String field) throws URISyntaxException {
		log.info("REST request to get a page of WorkOrders");
		final List<WorkOrderProcessingDTO> workOrders = workOrderService.findWorkOrderProcessingDtos(order,field);
		return workOrders;
	}

	// test work order

	@RequestMapping(value = "/hhhh/www", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public WorkOrder hello() throws URISyntaxException {
		currentTenantIdentifierResolver.setTenant(SLAVE);

		WorkOrder wo = workOrderRepository.findOne((long) 9);
		return wo;
	}

    /**
     *
     * @param type
     * @param query
     * @return workorder list
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/search/work-orders/various", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WorkOrderListDTO> searchByVarious(@RequestParam String type,@RequestParam String query) throws URISyntaxException {

	    log.info("Search WorkOrder By : "+type );
	    log.info("Search KeyWord : "+query);
	    final List<WorkOrderListDTO> workOrderDTOS =  workOrderService.searchByVarious(type,query);
	    return workOrderDTOS;

    }

    /**
     * Get All Work Orders in id,name form for Select Dropdown
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/prev/next/work-orders/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Map<String,Long> getPreviousNextID(@PathVariable Long id) throws URISyntaxException {
        log.info("Get Previous and Next WorkOrders ID");
        String next = "select id from work_order where id = (select min(id) from work_order where id > ?)";
        String prev = "select id from work_order where id = (select max(id) from work_order where id < ?)";
        Long nextID = jdbcTemplate.queryForObject(next,new Object[] {id},Long.class);
        Long prevID = jdbcTemplate.queryForObject(prev,new Object[] {id},Long.class);

        Map<String,Long>    projects = new HashMap<>();
        projects.put("next",nextID);
        projects.put("prev",prevID);

        return projects;
    }
}
