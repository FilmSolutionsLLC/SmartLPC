package com.fps.web.rest;

import com.fps.SmartLpcApp;
import com.fps.domain.WorkOrder;
import com.fps.repository.WorkOrderRepository;
import com.fps.service.WorkOrderService;
import com.fps.elastics.search.WorkOrderSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the WorkOrderResource REST controller.
 *
 * @see WorkOrderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartLpcApp.class)
@WebAppConfiguration
@IntegrationTest
public class WorkOrderResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Boolean DEFAULT_IS_PRINT = false;
    private static final Boolean UPDATED_IS_PRINT = true;

    private static final Boolean DEFAULT_IS_PROOF = false;
    private static final Boolean UPDATED_IS_PROOF = true;

    private static final Boolean DEFAULT_IS_ABC = false;
    private static final Boolean UPDATED_IS_ABC = true;

    private static final Boolean DEFAULT_IS_TRACKING = false;
    private static final Boolean UPDATED_IS_TRACKING = true;

    private static final LocalDate DEFAULT_REQUEST_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REQUEST_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_REQUEST_DESCRIPTION = "AAAAA";
    private static final String UPDATED_REQUEST_DESCRIPTION = "BBBBB";

    private static final Integer DEFAULT_AUDITED_FLAG = 1;
    private static final Integer UPDATED_AUDITED_FLAG = 2;
    private static final String DEFAULT_PO_RECORD = "AAAAA";
    private static final String UPDATED_PO_RECORD = "BBBBB";

    private static final Integer DEFAULT_INVOICED = 1;
    private static final Integer UPDATED_INVOICED = 2;
    private static final String DEFAULT_INVOICE_NUMBER = "AAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBB";

    private static final Boolean DEFAULT_IS_ALT_CREDIT = false;
    private static final Boolean UPDATED_IS_ALT_CREDIT = true;
    private static final String DEFAULT_OVERWRITE = "AAAAA";
    private static final String UPDATED_OVERWRITE = "BBBBB";
    private static final String DEFAULT_PRINT_SET = "AAAAA";
    private static final String UPDATED_PRINT_SET = "BBBBB";
    private static final String DEFAULT_PRINT_QUANTITY = "AAAAA";
    private static final String UPDATED_PRINT_QUANTITY = "BBBBB";
    private static final String DEFAULT_PRINT_DAYS_FROM = "AAAAA";
    private static final String UPDATED_PRINT_DAYS_FROM = "BBBBB";
    private static final String DEFAULT_PRINT_DAYS_TO = "AAAAA";
    private static final String UPDATED_PRINT_DAYS_TO = "BBBBB";
    private static final String DEFAULT_PRINT_PAGES_FROM = "AAAAA";
    private static final String UPDATED_PRINT_PAGES_FROM = "BBBBB";
    private static final String DEFAULT_PRINT_PAGES_TO = "AAAAA";
    private static final String UPDATED_PRINT_PAGES_TO = "BBBBB";

    private static final LocalDate DEFAULT_REMINDER_DATE_1 = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REMINDER_DATE_1 = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_REMINDER_MSG_1 = "AAAAA";
    private static final String UPDATED_REMINDER_MSG_1 = "BBBBB";

    private static final LocalDate DEFAULT_REMINDER_DATE_2 = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REMINDER_DATE_2 = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_REMINDER_MSG_2 = "AAAAA";
    private static final String UPDATED_REMINDER_MSG_2 = "BBBBB";

    private static final LocalDate DEFAULT_REMINDER_DATE_3 = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REMINDER_DATE_3 = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_REMINDER_MSG_3 = "AAAAA";
    private static final String UPDATED_REMINDER_MSG_3 = "BBBBB";

    private static final LocalDate DEFAULT_PROCESSING_DATE_RECIEVED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PROCESSING_DATE_RECIEVED = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_PROCESSING_HD_DID = "AAAAA";
    private static final String UPDATED_PROCESSING_HD_DID = "BBBBB";

    private static final LocalDate DEFAULT_PROCESSING_DATE_SHIPPED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PROCESSING_DATE_SHIPPED = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_PROCESSING_NOTE = "AAAAA";
    private static final String UPDATED_PROCESSING_NOTE = "BBBBB";
    private static final String DEFAULT_PROCESSING_LOCATION = "AAAAA";
    private static final String UPDATED_PROCESSING_LOCATION = "BBBBB";
    private static final String DEFAULT_PROCESSING_IMAGE_RANGE = "AAAAA";
    private static final String UPDATED_PROCESSING_IMAGE_RANGE = "BBBBB";
    private static final String DEFAULT_PROCESSING_IMAGE_QTY = "AAAAA";
    private static final String UPDATED_PROCESSING_IMAGE_QTY = "BBBBB";

    private static final LocalDate DEFAULT_DUE_TO_CLIENT_REMINDER = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_TO_CLIENT_REMINDER = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DUE_TO_MOUNTER_REMINDER = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_TO_MOUNTER_REMINDER = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_RECIEVED_FROM_MOUNTER_REMINDER = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECIEVED_FROM_MOUNTER_REMINDER = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_ABC_INSTRUCTION = "AAAAA";
    private static final String UPDATED_ABC_INSTRUCTION = "BBBBB";

    private static final Integer DEFAULT_ABC_RAW_DVD = 1;
    private static final Integer UPDATED_ABC_RAW_DVD = 2;

    private static final Integer DEFAULT_ABC_TALENT_COUNT = 1;
    private static final Integer UPDATED_ABC_TALENT_COUNT = 2;
    private static final String DEFAULT_KICK_BACK = "AAAAA";
    private static final String UPDATED_KICK_BACK = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATED_DATE);

    private static final ZonedDateTime DEFAULT_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_UPDATED_DATE_STR = dateTimeFormatter.format(DEFAULT_UPDATED_DATE);

    private static final LocalDate DEFAULT_COMPLETION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_COMPLETION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_DURATION_OF_SERVICE = "AAAAA";
    private static final String UPDATED_DURATION_OF_SERVICE = "BBBBB";

    private static final LocalDate DEFAULT_PROCESSING_PROOF_SHIPPED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PROCESSING_PROOF_SHIPPED = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private WorkOrderRepository workOrderRepository;

    @Inject
    private WorkOrderService workOrderService;

    @Inject
    private WorkOrderSearchRepository workOrderSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWorkOrderMockMvc;

    private WorkOrder workOrder;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkOrderResource workOrderResource = new WorkOrderResource();
        ReflectionTestUtils.setField(workOrderResource, "workOrderService", workOrderService);
        this.restWorkOrderMockMvc = MockMvcBuilders.standaloneSetup(workOrderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        workOrderSearchRepository.deleteAll();
        workOrder = new WorkOrder();
        workOrder.setIsPrint(DEFAULT_IS_PRINT);
        workOrder.setIsProof(DEFAULT_IS_PROOF);
        workOrder.setIsAbc(DEFAULT_IS_ABC);
        workOrder.setIsTracking(DEFAULT_IS_TRACKING);
        workOrder.setRequestDate(DEFAULT_REQUEST_DATE);
        workOrder.setRequestDescription(DEFAULT_REQUEST_DESCRIPTION);
        workOrder.setAuditedFlag(DEFAULT_AUDITED_FLAG);
        workOrder.setPoRecord(DEFAULT_PO_RECORD);
        workOrder.setInvoiced(DEFAULT_INVOICED);
        workOrder.setInvoiceNumber(DEFAULT_INVOICE_NUMBER);
        workOrder.setIsAltCredit(DEFAULT_IS_ALT_CREDIT);
        workOrder.setOverwrite(DEFAULT_OVERWRITE);
        workOrder.setPrintSet(DEFAULT_PRINT_SET);
        workOrder.setPrintQuantity(DEFAULT_PRINT_QUANTITY);
        workOrder.setPrintDaysFrom(DEFAULT_PRINT_DAYS_FROM);
        workOrder.setPrintDaysTo(DEFAULT_PRINT_DAYS_TO);
        workOrder.setPrintPagesFrom(DEFAULT_PRINT_PAGES_FROM);
        workOrder.setPrintPagesTo(DEFAULT_PRINT_PAGES_TO);
        workOrder.setReminderDate1(DEFAULT_REMINDER_DATE_1);
        workOrder.setReminderMsg1(DEFAULT_REMINDER_MSG_1);
        workOrder.setReminderDate2(DEFAULT_REMINDER_DATE_2);
        workOrder.setReminderMsg2(DEFAULT_REMINDER_MSG_2);
        workOrder.setReminderDate3(DEFAULT_REMINDER_DATE_3);
        workOrder.setReminderMsg3(DEFAULT_REMINDER_MSG_3);
        workOrder.setProcessingDateRecieved(DEFAULT_PROCESSING_DATE_RECIEVED);
        workOrder.setProcessingHDDid(DEFAULT_PROCESSING_HD_DID);
        workOrder.setProcessingDateShipped(DEFAULT_PROCESSING_DATE_SHIPPED);
        workOrder.setProcessingNote(DEFAULT_PROCESSING_NOTE);
        workOrder.setProcessingLocation(DEFAULT_PROCESSING_LOCATION);
        workOrder.setProcessingImageRange(DEFAULT_PROCESSING_IMAGE_RANGE);
        workOrder.setProcessingImageQty(DEFAULT_PROCESSING_IMAGE_QTY);
        workOrder.setDueToClientReminder(DEFAULT_DUE_TO_CLIENT_REMINDER);
        workOrder.setDueToMounterReminder(DEFAULT_DUE_TO_MOUNTER_REMINDER);
        workOrder.setRecievedFromMounterReminder(DEFAULT_RECIEVED_FROM_MOUNTER_REMINDER);
        workOrder.setAbcInstruction(DEFAULT_ABC_INSTRUCTION);
        workOrder.setAbcRawDvd(DEFAULT_ABC_RAW_DVD);
        workOrder.setAbcTalentCount(DEFAULT_ABC_TALENT_COUNT);
        workOrder.setKickBack(DEFAULT_KICK_BACK);
        workOrder.setCreatedDate(DEFAULT_CREATED_DATE);
        workOrder.setUpdatedDate(DEFAULT_UPDATED_DATE);
        workOrder.setCompletionDate(DEFAULT_COMPLETION_DATE);
        workOrder.setDurationOfService(DEFAULT_DURATION_OF_SERVICE);
        workOrder.setProcessingProofShipped(DEFAULT_PROCESSING_PROOF_SHIPPED);
    }

    @Test
    @Transactional
    public void createWorkOrder() throws Exception {
        int databaseSizeBeforeCreate = workOrderRepository.findAll().size();

        // Create the WorkOrder

        restWorkOrderMockMvc.perform(post("/api/work-orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workOrder)))
                .andExpect(status().isCreated());

        // Validate the WorkOrder in the database
        List<WorkOrder> workOrders = workOrderRepository.findAll();
        assertThat(workOrders).hasSize(databaseSizeBeforeCreate + 1);
        WorkOrder testWorkOrder = workOrders.get(workOrders.size() - 1);
        assertThat(testWorkOrder.isIsPrint()).isEqualTo(DEFAULT_IS_PRINT);
        assertThat(testWorkOrder.isIsProof()).isEqualTo(DEFAULT_IS_PROOF);
        assertThat(testWorkOrder.isIsAbc()).isEqualTo(DEFAULT_IS_ABC);
        assertThat(testWorkOrder.isIsTracking()).isEqualTo(DEFAULT_IS_TRACKING);
        assertThat(testWorkOrder.getRequestDate()).isEqualTo(DEFAULT_REQUEST_DATE);
        assertThat(testWorkOrder.getRequestDescription()).isEqualTo(DEFAULT_REQUEST_DESCRIPTION);
        assertThat(testWorkOrder.getAuditedFlag()).isEqualTo(DEFAULT_AUDITED_FLAG);
        assertThat(testWorkOrder.getPoRecord()).isEqualTo(DEFAULT_PO_RECORD);
        assertThat(testWorkOrder.getInvoiced()).isEqualTo(DEFAULT_INVOICED);
        assertThat(testWorkOrder.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testWorkOrder.isIsAltCredit()).isEqualTo(DEFAULT_IS_ALT_CREDIT);
        assertThat(testWorkOrder.getOverwrite()).isEqualTo(DEFAULT_OVERWRITE);
        assertThat(testWorkOrder.getPrintSet()).isEqualTo(DEFAULT_PRINT_SET);
        assertThat(testWorkOrder.getPrintQuantity()).isEqualTo(DEFAULT_PRINT_QUANTITY);
        assertThat(testWorkOrder.getPrintDaysFrom()).isEqualTo(DEFAULT_PRINT_DAYS_FROM);
        assertThat(testWorkOrder.getPrintDaysTo()).isEqualTo(DEFAULT_PRINT_DAYS_TO);
        assertThat(testWorkOrder.getPrintPagesFrom()).isEqualTo(DEFAULT_PRINT_PAGES_FROM);
        assertThat(testWorkOrder.getPrintPagesTo()).isEqualTo(DEFAULT_PRINT_PAGES_TO);
        assertThat(testWorkOrder.getReminderDate1()).isEqualTo(DEFAULT_REMINDER_DATE_1);
        assertThat(testWorkOrder.getReminderMsg1()).isEqualTo(DEFAULT_REMINDER_MSG_1);
        assertThat(testWorkOrder.getReminderDate2()).isEqualTo(DEFAULT_REMINDER_DATE_2);
        assertThat(testWorkOrder.getReminderMsg2()).isEqualTo(DEFAULT_REMINDER_MSG_2);
        assertThat(testWorkOrder.getReminderDate3()).isEqualTo(DEFAULT_REMINDER_DATE_3);
        assertThat(testWorkOrder.getReminderMsg3()).isEqualTo(DEFAULT_REMINDER_MSG_3);
        assertThat(testWorkOrder.getProcessingDateRecieved()).isEqualTo(DEFAULT_PROCESSING_DATE_RECIEVED);
        assertThat(testWorkOrder.getProcessingHDDid()).isEqualTo(DEFAULT_PROCESSING_HD_DID);
        assertThat(testWorkOrder.getProcessingDateShipped()).isEqualTo(DEFAULT_PROCESSING_DATE_SHIPPED);
        assertThat(testWorkOrder.getProcessingNote()).isEqualTo(DEFAULT_PROCESSING_NOTE);
        assertThat(testWorkOrder.getProcessingLocation()).isEqualTo(DEFAULT_PROCESSING_LOCATION);
        assertThat(testWorkOrder.getProcessingImageRange()).isEqualTo(DEFAULT_PROCESSING_IMAGE_RANGE);
        assertThat(testWorkOrder.getProcessingImageQty()).isEqualTo(DEFAULT_PROCESSING_IMAGE_QTY);
        assertThat(testWorkOrder.getDueToClientReminder()).isEqualTo(DEFAULT_DUE_TO_CLIENT_REMINDER);
        assertThat(testWorkOrder.getDueToMounterReminder()).isEqualTo(DEFAULT_DUE_TO_MOUNTER_REMINDER);
        assertThat(testWorkOrder.getRecievedFromMounterReminder()).isEqualTo(DEFAULT_RECIEVED_FROM_MOUNTER_REMINDER);
        assertThat(testWorkOrder.getAbcInstruction()).isEqualTo(DEFAULT_ABC_INSTRUCTION);
        assertThat(testWorkOrder.getAbcRawDvd()).isEqualTo(DEFAULT_ABC_RAW_DVD);
        assertThat(testWorkOrder.getAbcTalentCount()).isEqualTo(DEFAULT_ABC_TALENT_COUNT);
        assertThat(testWorkOrder.getKickBack()).isEqualTo(DEFAULT_KICK_BACK);
        assertThat(testWorkOrder.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testWorkOrder.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testWorkOrder.getCompletionDate()).isEqualTo(DEFAULT_COMPLETION_DATE);
        assertThat(testWorkOrder.getDurationOfService()).isEqualTo(DEFAULT_DURATION_OF_SERVICE);
        assertThat(testWorkOrder.getProcessingProofShipped()).isEqualTo(DEFAULT_PROCESSING_PROOF_SHIPPED);

        // Validate the WorkOrder in ElasticSearch
        WorkOrder workOrderEs = workOrderSearchRepository.findOne(testWorkOrder.getId());
        assertThat(workOrderEs).isEqualToComparingFieldByField(testWorkOrder);
    }

    @Test
    @Transactional
    public void getAllWorkOrders() throws Exception {
        // Initialize the database
        workOrderRepository.saveAndFlush(workOrder);

        // Get all the workOrders
        restWorkOrderMockMvc.perform(get("/api/work-orders?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(workOrder.getId().intValue())))
                .andExpect(jsonPath("$.[*].isPrint").value(hasItem(DEFAULT_IS_PRINT.booleanValue())))
                .andExpect(jsonPath("$.[*].isProof").value(hasItem(DEFAULT_IS_PROOF.booleanValue())))
                .andExpect(jsonPath("$.[*].isAbc").value(hasItem(DEFAULT_IS_ABC.booleanValue())))
                .andExpect(jsonPath("$.[*].isTracking").value(hasItem(DEFAULT_IS_TRACKING.booleanValue())))
                .andExpect(jsonPath("$.[*].requestDate").value(hasItem(DEFAULT_REQUEST_DATE.toString())))
                .andExpect(jsonPath("$.[*].requestDescription").value(hasItem(DEFAULT_REQUEST_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].auditedFlag").value(hasItem(DEFAULT_AUDITED_FLAG)))
                .andExpect(jsonPath("$.[*].poRecord").value(hasItem(DEFAULT_PO_RECORD.toString())))
                .andExpect(jsonPath("$.[*].invoiced").value(hasItem(DEFAULT_INVOICED)))
                .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].isAltCredit").value(hasItem(DEFAULT_IS_ALT_CREDIT.booleanValue())))
                .andExpect(jsonPath("$.[*].overwrite").value(hasItem(DEFAULT_OVERWRITE.toString())))
                .andExpect(jsonPath("$.[*].printSet").value(hasItem(DEFAULT_PRINT_SET.toString())))
                .andExpect(jsonPath("$.[*].printQuantity").value(hasItem(DEFAULT_PRINT_QUANTITY.toString())))
                .andExpect(jsonPath("$.[*].printDaysFrom").value(hasItem(DEFAULT_PRINT_DAYS_FROM.toString())))
                .andExpect(jsonPath("$.[*].printDaysTo").value(hasItem(DEFAULT_PRINT_DAYS_TO.toString())))
                .andExpect(jsonPath("$.[*].printPagesFrom").value(hasItem(DEFAULT_PRINT_PAGES_FROM.toString())))
                .andExpect(jsonPath("$.[*].printPagesTo").value(hasItem(DEFAULT_PRINT_PAGES_TO.toString())))
                .andExpect(jsonPath("$.[*].reminderDate1").value(hasItem(DEFAULT_REMINDER_DATE_1.toString())))
                .andExpect(jsonPath("$.[*].reminderMsg1").value(hasItem(DEFAULT_REMINDER_MSG_1.toString())))
                .andExpect(jsonPath("$.[*].reminderDate2").value(hasItem(DEFAULT_REMINDER_DATE_2.toString())))
                .andExpect(jsonPath("$.[*].reminderMsg2").value(hasItem(DEFAULT_REMINDER_MSG_2.toString())))
                .andExpect(jsonPath("$.[*].reminderDate3").value(hasItem(DEFAULT_REMINDER_DATE_3.toString())))
                .andExpect(jsonPath("$.[*].reminderMsg3").value(hasItem(DEFAULT_REMINDER_MSG_3.toString())))
                .andExpect(jsonPath("$.[*].processingDateRecieved").value(hasItem(DEFAULT_PROCESSING_DATE_RECIEVED.toString())))
                .andExpect(jsonPath("$.[*].processingHDDid").value(hasItem(DEFAULT_PROCESSING_HD_DID.toString())))
                .andExpect(jsonPath("$.[*].processingDateShipped").value(hasItem(DEFAULT_PROCESSING_DATE_SHIPPED.toString())))
                .andExpect(jsonPath("$.[*].processingNote").value(hasItem(DEFAULT_PROCESSING_NOTE.toString())))
                .andExpect(jsonPath("$.[*].processingLocation").value(hasItem(DEFAULT_PROCESSING_LOCATION.toString())))
                .andExpect(jsonPath("$.[*].processingImageRange").value(hasItem(DEFAULT_PROCESSING_IMAGE_RANGE.toString())))
                .andExpect(jsonPath("$.[*].processingImageQty").value(hasItem(DEFAULT_PROCESSING_IMAGE_QTY.toString())))
                .andExpect(jsonPath("$.[*].dueToClientReminder").value(hasItem(DEFAULT_DUE_TO_CLIENT_REMINDER.toString())))
                .andExpect(jsonPath("$.[*].dueToMounterReminder").value(hasItem(DEFAULT_DUE_TO_MOUNTER_REMINDER.toString())))
                .andExpect(jsonPath("$.[*].recievedFromMounterReminder").value(hasItem(DEFAULT_RECIEVED_FROM_MOUNTER_REMINDER.toString())))
                .andExpect(jsonPath("$.[*].abcInstruction").value(hasItem(DEFAULT_ABC_INSTRUCTION.toString())))
                .andExpect(jsonPath("$.[*].abcRawDvd").value(hasItem(DEFAULT_ABC_RAW_DVD)))
                .andExpect(jsonPath("$.[*].abcTalentCount").value(hasItem(DEFAULT_ABC_TALENT_COUNT)))
                .andExpect(jsonPath("$.[*].kickBack").value(hasItem(DEFAULT_KICK_BACK.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].completionDate").value(hasItem(DEFAULT_COMPLETION_DATE.toString())))
                .andExpect(jsonPath("$.[*].durationOfService").value(hasItem(DEFAULT_DURATION_OF_SERVICE.toString())))
                .andExpect(jsonPath("$.[*].processingProofShipped").value(hasItem(DEFAULT_PROCESSING_PROOF_SHIPPED.toString())));
    }

    @Test
    @Transactional
    public void getWorkOrder() throws Exception {
        // Initialize the database
        workOrderRepository.saveAndFlush(workOrder);

        // Get the workOrder
        restWorkOrderMockMvc.perform(get("/api/work-orders/{id}", workOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(workOrder.getId().intValue()))
            .andExpect(jsonPath("$.isPrint").value(DEFAULT_IS_PRINT.booleanValue()))
            .andExpect(jsonPath("$.isProof").value(DEFAULT_IS_PROOF.booleanValue()))
            .andExpect(jsonPath("$.isAbc").value(DEFAULT_IS_ABC.booleanValue()))
            .andExpect(jsonPath("$.isTracking").value(DEFAULT_IS_TRACKING.booleanValue()))
            .andExpect(jsonPath("$.requestDate").value(DEFAULT_REQUEST_DATE.toString()))
            .andExpect(jsonPath("$.requestDescription").value(DEFAULT_REQUEST_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.auditedFlag").value(DEFAULT_AUDITED_FLAG))
            .andExpect(jsonPath("$.poRecord").value(DEFAULT_PO_RECORD.toString()))
            .andExpect(jsonPath("$.invoiced").value(DEFAULT_INVOICED))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER.toString()))
            .andExpect(jsonPath("$.isAltCredit").value(DEFAULT_IS_ALT_CREDIT.booleanValue()))
            .andExpect(jsonPath("$.overwrite").value(DEFAULT_OVERWRITE.toString()))
            .andExpect(jsonPath("$.printSet").value(DEFAULT_PRINT_SET.toString()))
            .andExpect(jsonPath("$.printQuantity").value(DEFAULT_PRINT_QUANTITY.toString()))
            .andExpect(jsonPath("$.printDaysFrom").value(DEFAULT_PRINT_DAYS_FROM.toString()))
            .andExpect(jsonPath("$.printDaysTo").value(DEFAULT_PRINT_DAYS_TO.toString()))
            .andExpect(jsonPath("$.printPagesFrom").value(DEFAULT_PRINT_PAGES_FROM.toString()))
            .andExpect(jsonPath("$.printPagesTo").value(DEFAULT_PRINT_PAGES_TO.toString()))
            .andExpect(jsonPath("$.reminderDate1").value(DEFAULT_REMINDER_DATE_1.toString()))
            .andExpect(jsonPath("$.reminderMsg1").value(DEFAULT_REMINDER_MSG_1.toString()))
            .andExpect(jsonPath("$.reminderDate2").value(DEFAULT_REMINDER_DATE_2.toString()))
            .andExpect(jsonPath("$.reminderMsg2").value(DEFAULT_REMINDER_MSG_2.toString()))
            .andExpect(jsonPath("$.reminderDate3").value(DEFAULT_REMINDER_DATE_3.toString()))
            .andExpect(jsonPath("$.reminderMsg3").value(DEFAULT_REMINDER_MSG_3.toString()))
            .andExpect(jsonPath("$.processingDateRecieved").value(DEFAULT_PROCESSING_DATE_RECIEVED.toString()))
            .andExpect(jsonPath("$.processingHDDid").value(DEFAULT_PROCESSING_HD_DID.toString()))
            .andExpect(jsonPath("$.processingDateShipped").value(DEFAULT_PROCESSING_DATE_SHIPPED.toString()))
            .andExpect(jsonPath("$.processingNote").value(DEFAULT_PROCESSING_NOTE.toString()))
            .andExpect(jsonPath("$.processingLocation").value(DEFAULT_PROCESSING_LOCATION.toString()))
            .andExpect(jsonPath("$.processingImageRange").value(DEFAULT_PROCESSING_IMAGE_RANGE.toString()))
            .andExpect(jsonPath("$.processingImageQty").value(DEFAULT_PROCESSING_IMAGE_QTY.toString()))
            .andExpect(jsonPath("$.dueToClientReminder").value(DEFAULT_DUE_TO_CLIENT_REMINDER.toString()))
            .andExpect(jsonPath("$.dueToMounterReminder").value(DEFAULT_DUE_TO_MOUNTER_REMINDER.toString()))
            .andExpect(jsonPath("$.recievedFromMounterReminder").value(DEFAULT_RECIEVED_FROM_MOUNTER_REMINDER.toString()))
            .andExpect(jsonPath("$.abcInstruction").value(DEFAULT_ABC_INSTRUCTION.toString()))
            .andExpect(jsonPath("$.abcRawDvd").value(DEFAULT_ABC_RAW_DVD))
            .andExpect(jsonPath("$.abcTalentCount").value(DEFAULT_ABC_TALENT_COUNT))
            .andExpect(jsonPath("$.kickBack").value(DEFAULT_KICK_BACK.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE_STR))
            .andExpect(jsonPath("$.completionDate").value(DEFAULT_COMPLETION_DATE.toString()))
            .andExpect(jsonPath("$.durationOfService").value(DEFAULT_DURATION_OF_SERVICE.toString()))
            .andExpect(jsonPath("$.processingProofShipped").value(DEFAULT_PROCESSING_PROOF_SHIPPED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkOrder() throws Exception {
        // Get the workOrder
        restWorkOrderMockMvc.perform(get("/api/work-orders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkOrder() throws Exception {
        // Initialize the database
        workOrderService.save(workOrder);

        int databaseSizeBeforeUpdate = workOrderRepository.findAll().size();

        // Update the workOrder
        WorkOrder updatedWorkOrder = new WorkOrder();
        updatedWorkOrder.setId(workOrder.getId());
        updatedWorkOrder.setIsPrint(UPDATED_IS_PRINT);
        updatedWorkOrder.setIsProof(UPDATED_IS_PROOF);
        updatedWorkOrder.setIsAbc(UPDATED_IS_ABC);
        updatedWorkOrder.setIsTracking(UPDATED_IS_TRACKING);
        updatedWorkOrder.setRequestDate(UPDATED_REQUEST_DATE);
        updatedWorkOrder.setRequestDescription(UPDATED_REQUEST_DESCRIPTION);
        updatedWorkOrder.setAuditedFlag(UPDATED_AUDITED_FLAG);
        updatedWorkOrder.setPoRecord(UPDATED_PO_RECORD);
        updatedWorkOrder.setInvoiced(UPDATED_INVOICED);
        updatedWorkOrder.setInvoiceNumber(UPDATED_INVOICE_NUMBER);
        updatedWorkOrder.setIsAltCredit(UPDATED_IS_ALT_CREDIT);
        updatedWorkOrder.setOverwrite(UPDATED_OVERWRITE);
        updatedWorkOrder.setPrintSet(UPDATED_PRINT_SET);
        updatedWorkOrder.setPrintQuantity(UPDATED_PRINT_QUANTITY);
        updatedWorkOrder.setPrintDaysFrom(UPDATED_PRINT_DAYS_FROM);
        updatedWorkOrder.setPrintDaysTo(UPDATED_PRINT_DAYS_TO);
        updatedWorkOrder.setPrintPagesFrom(UPDATED_PRINT_PAGES_FROM);
        updatedWorkOrder.setPrintPagesTo(UPDATED_PRINT_PAGES_TO);
        updatedWorkOrder.setReminderDate1(UPDATED_REMINDER_DATE_1);
        updatedWorkOrder.setReminderMsg1(UPDATED_REMINDER_MSG_1);
        updatedWorkOrder.setReminderDate2(UPDATED_REMINDER_DATE_2);
        updatedWorkOrder.setReminderMsg2(UPDATED_REMINDER_MSG_2);
        updatedWorkOrder.setReminderDate3(UPDATED_REMINDER_DATE_3);
        updatedWorkOrder.setReminderMsg3(UPDATED_REMINDER_MSG_3);
        updatedWorkOrder.setProcessingDateRecieved(UPDATED_PROCESSING_DATE_RECIEVED);
        updatedWorkOrder.setProcessingHDDid(UPDATED_PROCESSING_HD_DID);
        updatedWorkOrder.setProcessingDateShipped(UPDATED_PROCESSING_DATE_SHIPPED);
        updatedWorkOrder.setProcessingNote(UPDATED_PROCESSING_NOTE);
        updatedWorkOrder.setProcessingLocation(UPDATED_PROCESSING_LOCATION);
        updatedWorkOrder.setProcessingImageRange(UPDATED_PROCESSING_IMAGE_RANGE);
        updatedWorkOrder.setProcessingImageQty(UPDATED_PROCESSING_IMAGE_QTY);
        updatedWorkOrder.setDueToClientReminder(UPDATED_DUE_TO_CLIENT_REMINDER);
        updatedWorkOrder.setDueToMounterReminder(UPDATED_DUE_TO_MOUNTER_REMINDER);
        updatedWorkOrder.setRecievedFromMounterReminder(UPDATED_RECIEVED_FROM_MOUNTER_REMINDER);
        updatedWorkOrder.setAbcInstruction(UPDATED_ABC_INSTRUCTION);
        updatedWorkOrder.setAbcRawDvd(UPDATED_ABC_RAW_DVD);
        updatedWorkOrder.setAbcTalentCount(UPDATED_ABC_TALENT_COUNT);
        updatedWorkOrder.setKickBack(UPDATED_KICK_BACK);
        updatedWorkOrder.setCreatedDate(UPDATED_CREATED_DATE);
        updatedWorkOrder.setUpdatedDate(UPDATED_UPDATED_DATE);
        updatedWorkOrder.setCompletionDate(UPDATED_COMPLETION_DATE);
        updatedWorkOrder.setDurationOfService(UPDATED_DURATION_OF_SERVICE);
        updatedWorkOrder.setProcessingProofShipped(UPDATED_PROCESSING_PROOF_SHIPPED);

        restWorkOrderMockMvc.perform(put("/api/work-orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedWorkOrder)))
                .andExpect(status().isOk());

        // Validate the WorkOrder in the database
        List<WorkOrder> workOrders = workOrderRepository.findAll();
        assertThat(workOrders).hasSize(databaseSizeBeforeUpdate);
        WorkOrder testWorkOrder = workOrders.get(workOrders.size() - 1);
        assertThat(testWorkOrder.isIsPrint()).isEqualTo(UPDATED_IS_PRINT);
        assertThat(testWorkOrder.isIsProof()).isEqualTo(UPDATED_IS_PROOF);
        assertThat(testWorkOrder.isIsAbc()).isEqualTo(UPDATED_IS_ABC);
        assertThat(testWorkOrder.isIsTracking()).isEqualTo(UPDATED_IS_TRACKING);
        assertThat(testWorkOrder.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
        assertThat(testWorkOrder.getRequestDescription()).isEqualTo(UPDATED_REQUEST_DESCRIPTION);
        assertThat(testWorkOrder.getAuditedFlag()).isEqualTo(UPDATED_AUDITED_FLAG);
        assertThat(testWorkOrder.getPoRecord()).isEqualTo(UPDATED_PO_RECORD);
        assertThat(testWorkOrder.getInvoiced()).isEqualTo(UPDATED_INVOICED);
        assertThat(testWorkOrder.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testWorkOrder.isIsAltCredit()).isEqualTo(UPDATED_IS_ALT_CREDIT);
        assertThat(testWorkOrder.getOverwrite()).isEqualTo(UPDATED_OVERWRITE);
        assertThat(testWorkOrder.getPrintSet()).isEqualTo(UPDATED_PRINT_SET);
        assertThat(testWorkOrder.getPrintQuantity()).isEqualTo(UPDATED_PRINT_QUANTITY);
        assertThat(testWorkOrder.getPrintDaysFrom()).isEqualTo(UPDATED_PRINT_DAYS_FROM);
        assertThat(testWorkOrder.getPrintDaysTo()).isEqualTo(UPDATED_PRINT_DAYS_TO);
        assertThat(testWorkOrder.getPrintPagesFrom()).isEqualTo(UPDATED_PRINT_PAGES_FROM);
        assertThat(testWorkOrder.getPrintPagesTo()).isEqualTo(UPDATED_PRINT_PAGES_TO);
        assertThat(testWorkOrder.getReminderDate1()).isEqualTo(UPDATED_REMINDER_DATE_1);
        assertThat(testWorkOrder.getReminderMsg1()).isEqualTo(UPDATED_REMINDER_MSG_1);
        assertThat(testWorkOrder.getReminderDate2()).isEqualTo(UPDATED_REMINDER_DATE_2);
        assertThat(testWorkOrder.getReminderMsg2()).isEqualTo(UPDATED_REMINDER_MSG_2);
        assertThat(testWorkOrder.getReminderDate3()).isEqualTo(UPDATED_REMINDER_DATE_3);
        assertThat(testWorkOrder.getReminderMsg3()).isEqualTo(UPDATED_REMINDER_MSG_3);
        assertThat(testWorkOrder.getProcessingDateRecieved()).isEqualTo(UPDATED_PROCESSING_DATE_RECIEVED);
        assertThat(testWorkOrder.getProcessingHDDid()).isEqualTo(UPDATED_PROCESSING_HD_DID);
        assertThat(testWorkOrder.getProcessingDateShipped()).isEqualTo(UPDATED_PROCESSING_DATE_SHIPPED);
        assertThat(testWorkOrder.getProcessingNote()).isEqualTo(UPDATED_PROCESSING_NOTE);
        assertThat(testWorkOrder.getProcessingLocation()).isEqualTo(UPDATED_PROCESSING_LOCATION);
        assertThat(testWorkOrder.getProcessingImageRange()).isEqualTo(UPDATED_PROCESSING_IMAGE_RANGE);
        assertThat(testWorkOrder.getProcessingImageQty()).isEqualTo(UPDATED_PROCESSING_IMAGE_QTY);
        assertThat(testWorkOrder.getDueToClientReminder()).isEqualTo(UPDATED_DUE_TO_CLIENT_REMINDER);
        assertThat(testWorkOrder.getDueToMounterReminder()).isEqualTo(UPDATED_DUE_TO_MOUNTER_REMINDER);
        assertThat(testWorkOrder.getRecievedFromMounterReminder()).isEqualTo(UPDATED_RECIEVED_FROM_MOUNTER_REMINDER);
        assertThat(testWorkOrder.getAbcInstruction()).isEqualTo(UPDATED_ABC_INSTRUCTION);
        assertThat(testWorkOrder.getAbcRawDvd()).isEqualTo(UPDATED_ABC_RAW_DVD);
        assertThat(testWorkOrder.getAbcTalentCount()).isEqualTo(UPDATED_ABC_TALENT_COUNT);
        assertThat(testWorkOrder.getKickBack()).isEqualTo(UPDATED_KICK_BACK);
        assertThat(testWorkOrder.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testWorkOrder.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testWorkOrder.getCompletionDate()).isEqualTo(UPDATED_COMPLETION_DATE);
        assertThat(testWorkOrder.getDurationOfService()).isEqualTo(UPDATED_DURATION_OF_SERVICE);
        assertThat(testWorkOrder.getProcessingProofShipped()).isEqualTo(UPDATED_PROCESSING_PROOF_SHIPPED);

        // Validate the WorkOrder in ElasticSearch
        WorkOrder workOrderEs = workOrderSearchRepository.findOne(testWorkOrder.getId());
        assertThat(workOrderEs).isEqualToComparingFieldByField(testWorkOrder);
    }

    @Test
    @Transactional
    public void deleteWorkOrder() throws Exception {
        // Initialize the database
        workOrderService.save(workOrder);

        int databaseSizeBeforeDelete = workOrderRepository.findAll().size();

        // Get the workOrder
        restWorkOrderMockMvc.perform(delete("/api/work-orders/{id}", workOrder.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean workOrderExistsInEs = workOrderSearchRepository.exists(workOrder.getId());
        assertThat(workOrderExistsInEs).isFalse();

        // Validate the database is empty
        List<WorkOrder> workOrders = workOrderRepository.findAll();
        assertThat(workOrders).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWorkOrder() throws Exception {
        // Initialize the database
        workOrderService.save(workOrder);

        // Search the workOrder
        restWorkOrderMockMvc.perform(get("/api/_search/work-orders?query=id:" + workOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].isPrint").value(hasItem(DEFAULT_IS_PRINT.booleanValue())))
            .andExpect(jsonPath("$.[*].isProof").value(hasItem(DEFAULT_IS_PROOF.booleanValue())))
            .andExpect(jsonPath("$.[*].isAbc").value(hasItem(DEFAULT_IS_ABC.booleanValue())))
            .andExpect(jsonPath("$.[*].isTracking").value(hasItem(DEFAULT_IS_TRACKING.booleanValue())))
            .andExpect(jsonPath("$.[*].requestDate").value(hasItem(DEFAULT_REQUEST_DATE.toString())))
            .andExpect(jsonPath("$.[*].requestDescription").value(hasItem(DEFAULT_REQUEST_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].auditedFlag").value(hasItem(DEFAULT_AUDITED_FLAG)))
            .andExpect(jsonPath("$.[*].poRecord").value(hasItem(DEFAULT_PO_RECORD.toString())))
            .andExpect(jsonPath("$.[*].invoiced").value(hasItem(DEFAULT_INVOICED)))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].isAltCredit").value(hasItem(DEFAULT_IS_ALT_CREDIT.booleanValue())))
            .andExpect(jsonPath("$.[*].overwrite").value(hasItem(DEFAULT_OVERWRITE.toString())))
            .andExpect(jsonPath("$.[*].printSet").value(hasItem(DEFAULT_PRINT_SET.toString())))
            .andExpect(jsonPath("$.[*].printQuantity").value(hasItem(DEFAULT_PRINT_QUANTITY.toString())))
            .andExpect(jsonPath("$.[*].printDaysFrom").value(hasItem(DEFAULT_PRINT_DAYS_FROM.toString())))
            .andExpect(jsonPath("$.[*].printDaysTo").value(hasItem(DEFAULT_PRINT_DAYS_TO.toString())))
            .andExpect(jsonPath("$.[*].printPagesFrom").value(hasItem(DEFAULT_PRINT_PAGES_FROM.toString())))
            .andExpect(jsonPath("$.[*].printPagesTo").value(hasItem(DEFAULT_PRINT_PAGES_TO.toString())))
            .andExpect(jsonPath("$.[*].reminderDate1").value(hasItem(DEFAULT_REMINDER_DATE_1.toString())))
            .andExpect(jsonPath("$.[*].reminderMsg1").value(hasItem(DEFAULT_REMINDER_MSG_1.toString())))
            .andExpect(jsonPath("$.[*].reminderDate2").value(hasItem(DEFAULT_REMINDER_DATE_2.toString())))
            .andExpect(jsonPath("$.[*].reminderMsg2").value(hasItem(DEFAULT_REMINDER_MSG_2.toString())))
            .andExpect(jsonPath("$.[*].reminderDate3").value(hasItem(DEFAULT_REMINDER_DATE_3.toString())))
            .andExpect(jsonPath("$.[*].reminderMsg3").value(hasItem(DEFAULT_REMINDER_MSG_3.toString())))
            .andExpect(jsonPath("$.[*].processingDateRecieved").value(hasItem(DEFAULT_PROCESSING_DATE_RECIEVED.toString())))
            .andExpect(jsonPath("$.[*].processingHDDid").value(hasItem(DEFAULT_PROCESSING_HD_DID.toString())))
            .andExpect(jsonPath("$.[*].processingDateShipped").value(hasItem(DEFAULT_PROCESSING_DATE_SHIPPED.toString())))
            .andExpect(jsonPath("$.[*].processingNote").value(hasItem(DEFAULT_PROCESSING_NOTE.toString())))
            .andExpect(jsonPath("$.[*].processingLocation").value(hasItem(DEFAULT_PROCESSING_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].processingImageRange").value(hasItem(DEFAULT_PROCESSING_IMAGE_RANGE.toString())))
            .andExpect(jsonPath("$.[*].processingImageQty").value(hasItem(DEFAULT_PROCESSING_IMAGE_QTY.toString())))
            .andExpect(jsonPath("$.[*].dueToClientReminder").value(hasItem(DEFAULT_DUE_TO_CLIENT_REMINDER.toString())))
            .andExpect(jsonPath("$.[*].dueToMounterReminder").value(hasItem(DEFAULT_DUE_TO_MOUNTER_REMINDER.toString())))
            .andExpect(jsonPath("$.[*].recievedFromMounterReminder").value(hasItem(DEFAULT_RECIEVED_FROM_MOUNTER_REMINDER.toString())))
            .andExpect(jsonPath("$.[*].abcInstruction").value(hasItem(DEFAULT_ABC_INSTRUCTION.toString())))
            .andExpect(jsonPath("$.[*].abcRawDvd").value(hasItem(DEFAULT_ABC_RAW_DVD)))
            .andExpect(jsonPath("$.[*].abcTalentCount").value(hasItem(DEFAULT_ABC_TALENT_COUNT)))
            .andExpect(jsonPath("$.[*].kickBack").value(hasItem(DEFAULT_KICK_BACK.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE_STR)))
            .andExpect(jsonPath("$.[*].completionDate").value(hasItem(DEFAULT_COMPLETION_DATE.toString())))
            .andExpect(jsonPath("$.[*].durationOfService").value(hasItem(DEFAULT_DURATION_OF_SERVICE.toString())))
            .andExpect(jsonPath("$.[*].processingProofShipped").value(hasItem(DEFAULT_PROCESSING_PROOF_SHIPPED.toString())));
    }
}
