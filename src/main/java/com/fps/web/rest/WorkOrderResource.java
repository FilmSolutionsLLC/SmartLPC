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
		log.debug("REST request to save WorkOrder : {}", workOrderDTO);
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
		WorkOrder result = workOrderService.save(workOrder);

		if (workOrder.isIsAbc()) {
			Set<WorkOrderAbcFile> workOrderAbcFiles = new HashSet<>();
			Set<WorkOrderAbcHdd> workOrderAbcHdds = new HashSet<>();

			log.info("Saving WorkOrderAbcFile ");
			for (WorkOrderAbcFile workOrderAbcFile : workOrderDTO.getWorkOrderAbcFiles()) {
				workOrderAbcFile.setWorkOrder(workOrder);
				workOrderAbcFiles.add(workOrderAbcFile);
				log.info(workOrderAbcFile.toString());
			}
			if (workOrderAbcFiles.size() > 0) {
				workOrderAbcFileRepository.save(workOrderAbcFiles);
			}

			log.info("Saving WorkOrderAbcHdd ");
			for (WorkOrderAbcHdd workOrderAbcHdd : workOrderDTO.getWorkOrderAbcHdds()) {
				workOrderAbcHdd.setWorkOrder(workOrder);
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

				workOrdersAdminRelation.setWorkOrder(workOrder);
				workOrdersAdminRelations.add(workOrdersAdminRelation);
				log.info(workOrdersAdminRelation.toString());
			}
		}
		if (workOrdersAdminRelations.size() > 0) {
			workOrdersAdminRelationRepository.save(workOrdersAdminRelations);
		}

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
		log.debug("REST request to update WorkOrder : {}", workOrderDTO);
		WorkOrder workOrder = workOrderDTO.getWorkOrder();
		if (workOrder.getId() == null) {
			return createWorkOrder(workOrderDTO);
		}
		currentTenantIdentifierResolver.setTenant(SLAVE);
		User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

		workOrder.setUpdatedBy(user);
		workOrder.setUpdatedDate(ZonedDateTime.now());

		WorkOrder result = workOrderService.save(workOrder);
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
		log.debug("RES</div> T request to get a page of WorkOrders");
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
		log.debug("REST request to get WorkOrder : {}", id);
		final WorkOrderDTO workOrderDTO = workOrderService.findOne(id);
		log.info("workOrderDTO ====> " + workOrderDTO.toString());
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
		log.debug("REST request to delete WorkOrder : {}", id);
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
		log.debug("REST request to search for a page of WorkOrders for query {}", query);
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
		log.debug("REST request to search for a page of WorkOrders for query {}", query + " and filer :" + filter);
		List<WorkOrder> workOrders = new ArrayList<>();
		if (filter.equals("project")) {

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
	public List<WorkOrderListDTO> workOrderReports(@RequestParam String reportType) throws URISyntaxException {
		log.info("Request for work orders : " + reportType);
		final List<WorkOrderListDTO> workOrderListDTOList = workOrderService.findWorkOrdersByReport(reportType);
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
		log.info("Request for work orders :  OPENED and GROUPED");

		final List<WorkOrderListDTO> workOrderListDTOList = workOrderService.findOpenWorkOrdersGrouped();
		log.info("Total Open Grouped Work Orders : " + workOrderListDTOList.size());
		log.info(" =  = = = = = = = = = == = = = = = = == = = = = == = = = == = = = ==  ");
		for (WorkOrderListDTO groupWorkOrder : workOrderListDTOList) {
			log.info(groupWorkOrder.toString());
			log.info(" =  = = = = = = = = = == = = = = = = == = = = = == = = = == = = = ==  ");
		}
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
	public ResponseEntity<List<WorkOrder>> processingWorkOrders(Pageable pageable) throws URISyntaxException {
		log.debug("RES</div> T request to get a page of WorkOrders");
		final Page<WorkOrder> page = workOrderRepository.findByProcessing_pko_flag(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/processing/work-orders");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}
