package com.fps.web.rest;

import com.fps.SmartLpcApp;
import com.fps.domain.Projects;
import com.fps.repository.ProjectsRepository;
import com.fps.service.ProjectsService;
import com.fps.elastics.search.ProjectsSearchRepository;

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
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ProjectsResource REST controller.
 *
 * @see ProjectsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartLpcApp.class)
@WebAppConfiguration
@IntegrationTest
public class ProjectsResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_FULL_NAME = "AAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBB";

    private static final Boolean DEFAULT_RUN_OF_SHOW_FLAG = false;
    private static final Boolean UPDATED_RUN_OF_SHOW_FLAG = true;

    private static final Boolean DEFAULT_TEMPLATE = false;
    private static final Boolean UPDATED_TEMPLATE = true;

    private static final Boolean DEFAULT_LAB_FLAG = false;
    private static final Boolean UPDATED_LAB_FLAG = true;
    private static final String DEFAULT_ALFRESCO_TITLE_1 = "AAAAA";
    private static final String UPDATED_ALFRESCO_TITLE_1 = "BBBBB";
    private static final String DEFAULT_ALFRESCO_TITLE_2 = "AAAAA";
    private static final String UPDATED_ALFRESCO_TITLE_2 = "BBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_ACTORS_WITH_RIGHTS = 1L;
    private static final Long UPDATED_ACTORS_WITH_RIGHTS = 2L;

    private static final Long DEFAULT_DAYS_SHOOTING = 1L;
    private static final Long UPDATED_DAYS_SHOOTING = 2L;

    private static final Long DEFAULT_WEEKS_SHOOTING = 1L;
    private static final Long UPDATED_WEEKS_SHOOTING = 2L;
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";

    private static final Boolean DEFAULT_SENSITIVE_VIEWING = false;
    private static final Boolean UPDATED_SENSITIVE_VIEWING = true;
    private static final String DEFAULT_PRODUCTION_COMPANY_NOTES = "AAAAA";
    private static final String UPDATED_PRODUCTION_COMPANY_NOTES = "BBBBB";
    private static final String DEFAULT_PRODUCTION_COMPANY_SHIPPING_NUMBER = "AAAAA";
    private static final String UPDATED_PRODUCTION_COMPANY_SHIPPING_NUMBER = "BBBBB";
    private static final String DEFAULT_PROCESSING_DELIVERIES = "AAAAA";
    private static final String UPDATED_PROCESSING_DELIVERIES = "BBBBB";
    private static final String DEFAULT_PROCESSING_SPECIAL_INSTRUCTIONS = "AAAAA";
    private static final String UPDATED_PROCESSING_SPECIAL_INSTRUCTIONS = "BBBBB";
    private static final String DEFAULT_PROCESSING_WATERMARK = "AAAAA";
    private static final String UPDATED_PROCESSING_WATERMARK = "BBBBB";
    private static final String DEFAULT_PROCESSING_COPYRIGHT = "AAAAA";
    private static final String UPDATED_PROCESSING_COPYRIGHT = "BBBBB";
    private static final String DEFAULT_LAB_PROOF_NOTES = "AAAAA";
    private static final String UPDATED_LAB_PROOF_NOTES = "BBBBB";
    private static final String DEFAULT_LAB_LAST_PROOF_IMAGE_NUMBER = "AAAAA";
    private static final String UPDATED_LAB_LAST_PROOF_IMAGE_NUMBER = "BBBBB";
    private static final String DEFAULT_LAB_LAST_PROOF_PAGE_NUMBER = "AAAAA";
    private static final String UPDATED_LAB_LAST_PROOF_PAGE_NUMBER = "BBBBB";
    private static final String DEFAULT_LAB_IMAGE_NUMBER_SCHEMA = "AAAAA";
    private static final String UPDATED_LAB_IMAGE_NUMBER_SCHEMA = "BBBBB";
    private static final String DEFAULT_LAB_FOLDER_BATCH_SCHEMA = "AAAAA";
    private static final String UPDATED_LAB_FOLDER_BATCH_SCHEMA = "BBBBB";
    private static final String DEFAULT_PHOTO_LAB_INFO = "AAAAA";
    private static final String UPDATED_PHOTO_LAB_INFO = "BBBBB";
    private static final String DEFAULT_PROJECT_UNIT_PHOTO_NOTES = "AAAAA";
    private static final String UPDATED_PROJECT_UNIT_PHOTO_NOTES = "BBBBB";
    private static final String DEFAULT_PROJECT_INFO_NOTES = "AAAAA";
    private static final String UPDATED_PROJECT_INFO_NOTES = "BBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_LEGACY_DIRECTOR = "AAAAA";
    private static final String UPDATED_LEGACY_DIRECTOR = "BBBBB";
    private static final String DEFAULT_LEGACY_EXECUTIVE_PRODUCER = "AAAAA";
    private static final String UPDATED_LEGACY_EXECUTIVE_PRODUCER = "BBBBB";
    private static final String DEFAULT_LEGACY_EXECUTIVE_PRODUCER_2 = "AAAAA";
    private static final String UPDATED_LEGACY_EXECUTIVE_PRODUCER_2 = "BBBBB";
    private static final String DEFAULT_LEGACY_EXECUTIVE_PRODUCER_3 = "AAAAA";
    private static final String UPDATED_LEGACY_EXECUTIVE_PRODUCER_3 = "BBBBB";
    private static final String DEFAULT_LEGACY_EXECUTIVE_PRODUCER_4 = "AAAAA";
    private static final String UPDATED_LEGACY_EXECUTIVE_PRODUCER_4 = "BBBBB";
    private static final String DEFAULT_LEGACY_PRODUCER = "AAAAA";
    private static final String UPDATED_LEGACY_PRODUCER = "BBBBB";
    private static final String DEFAULT_LEGACY_PRODUCER_2 = "AAAAA";
    private static final String UPDATED_LEGACY_PRODUCER_2 = "BBBBB";
    private static final String DEFAULT_LEGACY_PRODUCER_3 = "AAAAA";
    private static final String UPDATED_LEGACY_PRODUCER_3 = "BBBBB";
    private static final String DEFAULT_LEGACY_PRODUCER_4 = "AAAAA";
    private static final String UPDATED_LEGACY_PRODUCER_4 = "BBBBB";
    private static final String DEFAULT_LEGACY_ADDITIONAL_TALENT = "AAAAA";
    private static final String UPDATED_LEGACY_ADDITIONAL_TALENT = "BBBBB";

    private static final Boolean DEFAULT_THEME_ID = false;
    private static final Boolean UPDATED_THEME_ID = true;
    private static final String DEFAULT_SPT_PHOTO_SUBTYPE = "AAAAA";
    private static final String UPDATED_SPT_PHOTO_SUBTYPE = "BBBBB";
    private static final String DEFAULT_PHOTO_CREDIT = "AAAAA";
    private static final String UPDATED_PHOTO_CREDIT = "BBBBB";

    private static final LocalDate DEFAULT_SHOOT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SHOOT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_SHOOT_DATE_OVERRIDE = false;
    private static final Boolean UPDATED_SHOOT_DATE_OVERRIDE = true;

    private static final Boolean DEFAULT_UNIT_PHOTOGRAPHER_OVERRIDE = false;
    private static final Boolean UPDATED_UNIT_PHOTOGRAPHER_OVERRIDE = true;

    private static final Boolean DEFAULT_USE_SETUP = false;
    private static final Boolean UPDATED_USE_SETUP = true;

    private static final Boolean DEFAULT_USE_EXIF = false;
    private static final Boolean UPDATED_USE_EXIF = true;

    private static final LocalDate DEFAULT_TAG_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TAG_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_TAGREPORT_INDEX = 1;
    private static final Integer UPDATED_TAGREPORT_INDEX = 2;
    private static final String DEFAULT_LOGIN_MESSAGE = "AAAAA";
    private static final String UPDATED_LOGIN_MESSAGE = "BBBBB";

    private static final Boolean DEFAULT_LOGIN_MESSAGE_ACTIVE = false;
    private static final Boolean UPDATED_LOGIN_MESSAGE_ACTIVE = true;

    private static final Boolean DEFAULT_TOP_LEVEL_ALBUMS = false;
    private static final Boolean UPDATED_TOP_LEVEL_ALBUMS = true;

    private static final Boolean DEFAULT_ENABLE_TERTIARY = false;
    private static final Boolean UPDATED_ENABLE_TERTIARY = true;

    private static final Long DEFAULT_INVOICE_CREATED = 1L;
    private static final Long UPDATED_INVOICE_CREATED = 2L;
    private static final String DEFAULT_PRICE = "AAAAA";
    private static final String UPDATED_PRICE = "BBBBB";
    private static final String DEFAULT_FOX_TITLE = "AAAAA";
    private static final String UPDATED_FOX_TITLE = "BBBBB";

    private static final Boolean DEFAULT_IS_ASSET = false;
    private static final Boolean UPDATED_IS_ASSET = true;

    private static final Integer DEFAULT_FULL_REJECTION = 1;
    private static final Integer UPDATED_FULL_REJECTION = 2;

    private static final Boolean DEFAULT_DISABLED = false;
    private static final Boolean UPDATED_DISABLED = true;

    private static final LocalDate DEFAULT_REMINDER_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REMINDER_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_PHOTO_CREDIT_OVERRIDE = false;
    private static final Boolean UPDATED_PHOTO_CREDIT_OVERRIDE = true;

    @Inject
    private ProjectsRepository projectsRepository;

    @Inject
    private ProjectsService projectsService;

    @Inject
    private ProjectsSearchRepository projectsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProjectsMockMvc;

    private Projects projects;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectsResource projectsResource = new ProjectsResource();
        ReflectionTestUtils.setField(projectsResource, "projectsService", projectsService);
        this.restProjectsMockMvc = MockMvcBuilders.standaloneSetup(projectsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        projectsSearchRepository.deleteAll();
        projects = new Projects();
        projects.setName(DEFAULT_NAME);
        projects.setFullName(DEFAULT_FULL_NAME);
        projects.setRunOfShowFlag(DEFAULT_RUN_OF_SHOW_FLAG);
        projects.setTemplate(DEFAULT_TEMPLATE);
        projects.setLabFlag(DEFAULT_LAB_FLAG);
        projects.setAlfrescoTitle1(DEFAULT_ALFRESCO_TITLE_1);
        projects.setAlfrescoTitle2(DEFAULT_ALFRESCO_TITLE_2);
        projects.setStartDate(DEFAULT_START_DATE);
        projects.setEndDate(DEFAULT_END_DATE);
        projects.setActorsWithRights(DEFAULT_ACTORS_WITH_RIGHTS);
        projects.setDaysShooting(DEFAULT_DAYS_SHOOTING);
        projects.setWeeksShooting(DEFAULT_WEEKS_SHOOTING);
        projects.setNotes(DEFAULT_NOTES);
        projects.setSensitiveViewing(DEFAULT_SENSITIVE_VIEWING);
        projects.setProductionCompanyNotes(DEFAULT_PRODUCTION_COMPANY_NOTES);
        projects.setProductionCompanyShippingNumber(DEFAULT_PRODUCTION_COMPANY_SHIPPING_NUMBER);
        projects.setProcessingDeliveries(DEFAULT_PROCESSING_DELIVERIES);
        projects.setProcessingSpecialInstructions(DEFAULT_PROCESSING_SPECIAL_INSTRUCTIONS);
        projects.setProcessingWatermark(DEFAULT_PROCESSING_WATERMARK);
        projects.setProcessingCopyright(DEFAULT_PROCESSING_COPYRIGHT);
        projects.setLabProofNotes(DEFAULT_LAB_PROOF_NOTES);
        projects.setLabLastProofImageNumber(DEFAULT_LAB_LAST_PROOF_IMAGE_NUMBER);
        projects.setLabLastProofPageNumber(DEFAULT_LAB_LAST_PROOF_PAGE_NUMBER);
        projects.setLabImageNumberSchema(DEFAULT_LAB_IMAGE_NUMBER_SCHEMA);
        projects.setLabFolderBatchSchema(DEFAULT_LAB_FOLDER_BATCH_SCHEMA);
        projects.setPhotoLabInfo(DEFAULT_PHOTO_LAB_INFO);
        projects.setProjectUnitPhotoNotes(DEFAULT_PROJECT_UNIT_PHOTO_NOTES);
        projects.setProjectInfoNotes(DEFAULT_PROJECT_INFO_NOTES);
        projects.setCreatedDate(DEFAULT_CREATED_DATE);
        projects.setUpdatedDate(DEFAULT_UPDATED_DATE);
        projects.setLegacyDirector(DEFAULT_LEGACY_DIRECTOR);
        projects.setLegacyExecutiveProducer(DEFAULT_LEGACY_EXECUTIVE_PRODUCER);
        projects.setLegacyExecutiveProducer2(DEFAULT_LEGACY_EXECUTIVE_PRODUCER_2);
        projects.setLegacyExecutiveProducer3(DEFAULT_LEGACY_EXECUTIVE_PRODUCER_3);
        projects.setLegacyExecutiveProducer4(DEFAULT_LEGACY_EXECUTIVE_PRODUCER_4);
        projects.setLegacyProducer(DEFAULT_LEGACY_PRODUCER);
        projects.setLegacyProducer2(DEFAULT_LEGACY_PRODUCER_2);
        projects.setLegacyProducer3(DEFAULT_LEGACY_PRODUCER_3);
        projects.setLegacyProducer4(DEFAULT_LEGACY_PRODUCER_4);
        projects.setLegacyAdditionalTalent(DEFAULT_LEGACY_ADDITIONAL_TALENT);
        projects.setThemeId(DEFAULT_THEME_ID);
        projects.setSptPhotoSubtype(DEFAULT_SPT_PHOTO_SUBTYPE);
        projects.setPhotoCredit(DEFAULT_PHOTO_CREDIT);
        projects.setShootDate(DEFAULT_SHOOT_DATE);
        projects.setShootDateOverride(DEFAULT_SHOOT_DATE_OVERRIDE);
        projects.setUnitPhotographerOverride(DEFAULT_UNIT_PHOTOGRAPHER_OVERRIDE);
        projects.setUseSetup(DEFAULT_USE_SETUP);
        projects.setUseExif(DEFAULT_USE_EXIF);
        projects.setTagDate(DEFAULT_TAG_DATE);
        projects.setTagreportIndex(DEFAULT_TAGREPORT_INDEX);
        projects.setLoginMessage(DEFAULT_LOGIN_MESSAGE);
        projects.setLoginMessageActive(DEFAULT_LOGIN_MESSAGE_ACTIVE);
        projects.setTopLevelAlbums(DEFAULT_TOP_LEVEL_ALBUMS);
        projects.setEnableTertiary(DEFAULT_ENABLE_TERTIARY);
        projects.setInvoiceCreated(DEFAULT_INVOICE_CREATED);
        projects.setPrice(DEFAULT_PRICE);
        projects.setFoxTitle(DEFAULT_FOX_TITLE);
        projects.setIsAsset(DEFAULT_IS_ASSET);
        projects.setFullRejection(DEFAULT_FULL_REJECTION);
        projects.setDisabled(DEFAULT_DISABLED);
        projects.setReminderDate(DEFAULT_REMINDER_DATE);
        projects.setPhotoCreditOverride(DEFAULT_PHOTO_CREDIT_OVERRIDE);
    }

    @Test
    @Transactional
    public void createProjects() throws Exception {
        int databaseSizeBeforeCreate = projectsRepository.findAll().size();

        // Create the Projects

        restProjectsMockMvc.perform(post("/api/projects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projects)))
                .andExpect(status().isCreated());

        // Validate the Projects in the database
        List<Projects> projects = projectsRepository.findAll();
        assertThat(projects).hasSize(databaseSizeBeforeCreate + 1);
        Projects testProjects = projects.get(projects.size() - 1);
        assertThat(testProjects.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProjects.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testProjects.isRunOfShowFlag()).isEqualTo(DEFAULT_RUN_OF_SHOW_FLAG);
        assertThat(testProjects.isTemplate()).isEqualTo(DEFAULT_TEMPLATE);
        assertThat(testProjects.isLabFlag()).isEqualTo(DEFAULT_LAB_FLAG);
        assertThat(testProjects.getAlfrescoTitle1()).isEqualTo(DEFAULT_ALFRESCO_TITLE_1);
        assertThat(testProjects.getAlfrescoTitle2()).isEqualTo(DEFAULT_ALFRESCO_TITLE_2);
        assertThat(testProjects.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testProjects.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testProjects.getActorsWithRights()).isEqualTo(DEFAULT_ACTORS_WITH_RIGHTS);
        assertThat(testProjects.getDaysShooting()).isEqualTo(DEFAULT_DAYS_SHOOTING);
        assertThat(testProjects.getWeeksShooting()).isEqualTo(DEFAULT_WEEKS_SHOOTING);
        assertThat(testProjects.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testProjects.isSensitiveViewing()).isEqualTo(DEFAULT_SENSITIVE_VIEWING);
        assertThat(testProjects.getProductionCompanyNotes()).isEqualTo(DEFAULT_PRODUCTION_COMPANY_NOTES);
        assertThat(testProjects.getProductionCompanyShippingNumber()).isEqualTo(DEFAULT_PRODUCTION_COMPANY_SHIPPING_NUMBER);
        assertThat(testProjects.getProcessingDeliveries()).isEqualTo(DEFAULT_PROCESSING_DELIVERIES);
        assertThat(testProjects.getProcessingSpecialInstructions()).isEqualTo(DEFAULT_PROCESSING_SPECIAL_INSTRUCTIONS);
        assertThat(testProjects.getProcessingWatermark()).isEqualTo(DEFAULT_PROCESSING_WATERMARK);
        assertThat(testProjects.getProcessingCopyright()).isEqualTo(DEFAULT_PROCESSING_COPYRIGHT);
        assertThat(testProjects.getLabProofNotes()).isEqualTo(DEFAULT_LAB_PROOF_NOTES);
        assertThat(testProjects.getLabLastProofImageNumber()).isEqualTo(DEFAULT_LAB_LAST_PROOF_IMAGE_NUMBER);
        assertThat(testProjects.getLabLastProofPageNumber()).isEqualTo(DEFAULT_LAB_LAST_PROOF_PAGE_NUMBER);
        assertThat(testProjects.getLabImageNumberSchema()).isEqualTo(DEFAULT_LAB_IMAGE_NUMBER_SCHEMA);
        assertThat(testProjects.getLabFolderBatchSchema()).isEqualTo(DEFAULT_LAB_FOLDER_BATCH_SCHEMA);
        assertThat(testProjects.getPhotoLabInfo()).isEqualTo(DEFAULT_PHOTO_LAB_INFO);
        assertThat(testProjects.getProjectUnitPhotoNotes()).isEqualTo(DEFAULT_PROJECT_UNIT_PHOTO_NOTES);
        assertThat(testProjects.getProjectInfoNotes()).isEqualTo(DEFAULT_PROJECT_INFO_NOTES);
        assertThat(testProjects.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProjects.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testProjects.getLegacyDirector()).isEqualTo(DEFAULT_LEGACY_DIRECTOR);
        assertThat(testProjects.getLegacyExecutiveProducer()).isEqualTo(DEFAULT_LEGACY_EXECUTIVE_PRODUCER);
        assertThat(testProjects.getLegacyExecutiveProducer2()).isEqualTo(DEFAULT_LEGACY_EXECUTIVE_PRODUCER_2);
        assertThat(testProjects.getLegacyExecutiveProducer3()).isEqualTo(DEFAULT_LEGACY_EXECUTIVE_PRODUCER_3);
        assertThat(testProjects.getLegacyExecutiveProducer4()).isEqualTo(DEFAULT_LEGACY_EXECUTIVE_PRODUCER_4);
        assertThat(testProjects.getLegacyProducer()).isEqualTo(DEFAULT_LEGACY_PRODUCER);
        assertThat(testProjects.getLegacyProducer2()).isEqualTo(DEFAULT_LEGACY_PRODUCER_2);
        assertThat(testProjects.getLegacyProducer3()).isEqualTo(DEFAULT_LEGACY_PRODUCER_3);
        assertThat(testProjects.getLegacyProducer4()).isEqualTo(DEFAULT_LEGACY_PRODUCER_4);
        assertThat(testProjects.getLegacyAdditionalTalent()).isEqualTo(DEFAULT_LEGACY_ADDITIONAL_TALENT);
        assertThat(testProjects.isThemeId()).isEqualTo(DEFAULT_THEME_ID);
        assertThat(testProjects.getSptPhotoSubtype()).isEqualTo(DEFAULT_SPT_PHOTO_SUBTYPE);
        assertThat(testProjects.getPhotoCredit()).isEqualTo(DEFAULT_PHOTO_CREDIT);
        assertThat(testProjects.getShootDate()).isEqualTo(DEFAULT_SHOOT_DATE);
        assertThat(testProjects.isShootDateOverride()).isEqualTo(DEFAULT_SHOOT_DATE_OVERRIDE);
        assertThat(testProjects.isUnitPhotographerOverride()).isEqualTo(DEFAULT_UNIT_PHOTOGRAPHER_OVERRIDE);
        assertThat(testProjects.isUseSetup()).isEqualTo(DEFAULT_USE_SETUP);
        assertThat(testProjects.isUseExif()).isEqualTo(DEFAULT_USE_EXIF);
        assertThat(testProjects.getTagDate()).isEqualTo(DEFAULT_TAG_DATE);
        assertThat(testProjects.getTagreportIndex()).isEqualTo(DEFAULT_TAGREPORT_INDEX);
        assertThat(testProjects.getLoginMessage()).isEqualTo(DEFAULT_LOGIN_MESSAGE);
        assertThat(testProjects.isLoginMessageActive()).isEqualTo(DEFAULT_LOGIN_MESSAGE_ACTIVE);
        assertThat(testProjects.isTopLevelAlbums()).isEqualTo(DEFAULT_TOP_LEVEL_ALBUMS);
        assertThat(testProjects.isEnableTertiary()).isEqualTo(DEFAULT_ENABLE_TERTIARY);
        assertThat(testProjects.getInvoiceCreated()).isEqualTo(DEFAULT_INVOICE_CREATED);
        assertThat(testProjects.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProjects.getFoxTitle()).isEqualTo(DEFAULT_FOX_TITLE);
        assertThat(testProjects.isIsAsset()).isEqualTo(DEFAULT_IS_ASSET);
        assertThat(testProjects.getFullRejection()).isEqualTo(DEFAULT_FULL_REJECTION);
        assertThat(testProjects.isDisabled()).isEqualTo(DEFAULT_DISABLED);
        assertThat(testProjects.getReminderDate()).isEqualTo(DEFAULT_REMINDER_DATE);
        assertThat(testProjects.isPhotoCreditOverride()).isEqualTo(DEFAULT_PHOTO_CREDIT_OVERRIDE);

        // Validate the Projects in ElasticSearch
        Projects projectsEs = projectsSearchRepository.findOne(testProjects.getId());
        assertThat(projectsEs).isEqualToComparingFieldByField(testProjects);
    }

    @Test
    @Transactional
    public void getAllProjects() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projects
        restProjectsMockMvc.perform(get("/api/projects?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(projects.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME.toString())))
                .andExpect(jsonPath("$.[*].runOfShowFlag").value(hasItem(DEFAULT_RUN_OF_SHOW_FLAG.booleanValue())))
                .andExpect(jsonPath("$.[*].template").value(hasItem(DEFAULT_TEMPLATE.booleanValue())))
                .andExpect(jsonPath("$.[*].labFlag").value(hasItem(DEFAULT_LAB_FLAG.booleanValue())))
                .andExpect(jsonPath("$.[*].alfrescoTitle1").value(hasItem(DEFAULT_ALFRESCO_TITLE_1.toString())))
                .andExpect(jsonPath("$.[*].alfrescoTitle2").value(hasItem(DEFAULT_ALFRESCO_TITLE_2.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
                .andExpect(jsonPath("$.[*].actorsWithRights").value(hasItem(DEFAULT_ACTORS_WITH_RIGHTS.intValue())))
                .andExpect(jsonPath("$.[*].daysShooting").value(hasItem(DEFAULT_DAYS_SHOOTING.intValue())))
                .andExpect(jsonPath("$.[*].weeksShooting").value(hasItem(DEFAULT_WEEKS_SHOOTING.intValue())))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
                .andExpect(jsonPath("$.[*].sensitiveViewing").value(hasItem(DEFAULT_SENSITIVE_VIEWING.booleanValue())))
                .andExpect(jsonPath("$.[*].productionCompanyNotes").value(hasItem(DEFAULT_PRODUCTION_COMPANY_NOTES.toString())))
                .andExpect(jsonPath("$.[*].productionCompanyShippingNumber").value(hasItem(DEFAULT_PRODUCTION_COMPANY_SHIPPING_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].processingDeliveries").value(hasItem(DEFAULT_PROCESSING_DELIVERIES.toString())))
                .andExpect(jsonPath("$.[*].processingSpecialInstructions").value(hasItem(DEFAULT_PROCESSING_SPECIAL_INSTRUCTIONS.toString())))
                .andExpect(jsonPath("$.[*].processingWatermark").value(hasItem(DEFAULT_PROCESSING_WATERMARK.toString())))
                .andExpect(jsonPath("$.[*].processingCopyright").value(hasItem(DEFAULT_PROCESSING_COPYRIGHT.toString())))
                .andExpect(jsonPath("$.[*].labProofNotes").value(hasItem(DEFAULT_LAB_PROOF_NOTES.toString())))
                .andExpect(jsonPath("$.[*].labLastProofImageNumber").value(hasItem(DEFAULT_LAB_LAST_PROOF_IMAGE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].labLastProofPageNumber").value(hasItem(DEFAULT_LAB_LAST_PROOF_PAGE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].labImageNumberSchema").value(hasItem(DEFAULT_LAB_IMAGE_NUMBER_SCHEMA.toString())))
                .andExpect(jsonPath("$.[*].labFolderBatchSchema").value(hasItem(DEFAULT_LAB_FOLDER_BATCH_SCHEMA.toString())))
                .andExpect(jsonPath("$.[*].photoLabInfo").value(hasItem(DEFAULT_PHOTO_LAB_INFO.toString())))
                .andExpect(jsonPath("$.[*].projectUnitPhotoNotes").value(hasItem(DEFAULT_PROJECT_UNIT_PHOTO_NOTES.toString())))
                .andExpect(jsonPath("$.[*].projectInfoNotes").value(hasItem(DEFAULT_PROJECT_INFO_NOTES.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
                .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())))
                .andExpect(jsonPath("$.[*].legacyDirector").value(hasItem(DEFAULT_LEGACY_DIRECTOR.toString())))
                .andExpect(jsonPath("$.[*].legacyExecutiveProducer").value(hasItem(DEFAULT_LEGACY_EXECUTIVE_PRODUCER.toString())))
                .andExpect(jsonPath("$.[*].legacyExecutiveProducer2").value(hasItem(DEFAULT_LEGACY_EXECUTIVE_PRODUCER_2.toString())))
                .andExpect(jsonPath("$.[*].legacyExecutiveProducer3").value(hasItem(DEFAULT_LEGACY_EXECUTIVE_PRODUCER_3.toString())))
                .andExpect(jsonPath("$.[*].legacyExecutiveProducer4").value(hasItem(DEFAULT_LEGACY_EXECUTIVE_PRODUCER_4.toString())))
                .andExpect(jsonPath("$.[*].legacyProducer").value(hasItem(DEFAULT_LEGACY_PRODUCER.toString())))
                .andExpect(jsonPath("$.[*].legacyProducer2").value(hasItem(DEFAULT_LEGACY_PRODUCER_2.toString())))
                .andExpect(jsonPath("$.[*].legacyProducer3").value(hasItem(DEFAULT_LEGACY_PRODUCER_3.toString())))
                .andExpect(jsonPath("$.[*].legacyProducer4").value(hasItem(DEFAULT_LEGACY_PRODUCER_4.toString())))
                .andExpect(jsonPath("$.[*].legacyAdditionalTalent").value(hasItem(DEFAULT_LEGACY_ADDITIONAL_TALENT.toString())))
                .andExpect(jsonPath("$.[*].themeId").value(hasItem(DEFAULT_THEME_ID.booleanValue())))
                .andExpect(jsonPath("$.[*].sptPhotoSubtype").value(hasItem(DEFAULT_SPT_PHOTO_SUBTYPE.toString())))
                .andExpect(jsonPath("$.[*].photoCredit").value(hasItem(DEFAULT_PHOTO_CREDIT.toString())))
                .andExpect(jsonPath("$.[*].shootDate").value(hasItem(DEFAULT_SHOOT_DATE.toString())))
                .andExpect(jsonPath("$.[*].shootDateOverride").value(hasItem(DEFAULT_SHOOT_DATE_OVERRIDE.booleanValue())))
                .andExpect(jsonPath("$.[*].unitPhotographerOverride").value(hasItem(DEFAULT_UNIT_PHOTOGRAPHER_OVERRIDE.booleanValue())))
                .andExpect(jsonPath("$.[*].useSetup").value(hasItem(DEFAULT_USE_SETUP.booleanValue())))
                .andExpect(jsonPath("$.[*].useExif").value(hasItem(DEFAULT_USE_EXIF.booleanValue())))
                .andExpect(jsonPath("$.[*].tagDate").value(hasItem(DEFAULT_TAG_DATE.toString())))
                .andExpect(jsonPath("$.[*].tagreportIndex").value(hasItem(DEFAULT_TAGREPORT_INDEX)))
                .andExpect(jsonPath("$.[*].loginMessage").value(hasItem(DEFAULT_LOGIN_MESSAGE.toString())))
                .andExpect(jsonPath("$.[*].loginMessageActive").value(hasItem(DEFAULT_LOGIN_MESSAGE_ACTIVE.booleanValue())))
                .andExpect(jsonPath("$.[*].topLevelAlbums").value(hasItem(DEFAULT_TOP_LEVEL_ALBUMS.booleanValue())))
                .andExpect(jsonPath("$.[*].enableTertiary").value(hasItem(DEFAULT_ENABLE_TERTIARY.booleanValue())))
                .andExpect(jsonPath("$.[*].invoiceCreated").value(hasItem(DEFAULT_INVOICE_CREATED.intValue())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.toString())))
                .andExpect(jsonPath("$.[*].foxTitle").value(hasItem(DEFAULT_FOX_TITLE.toString())))
                .andExpect(jsonPath("$.[*].isAsset").value(hasItem(DEFAULT_IS_ASSET.booleanValue())))
                .andExpect(jsonPath("$.[*].fullRejection").value(hasItem(DEFAULT_FULL_REJECTION)))
                .andExpect(jsonPath("$.[*].disabled").value(hasItem(DEFAULT_DISABLED.booleanValue())))
                .andExpect(jsonPath("$.[*].reminderDate").value(hasItem(DEFAULT_REMINDER_DATE.toString())))
                .andExpect(jsonPath("$.[*].photoCreditOverride").value(hasItem(DEFAULT_PHOTO_CREDIT_OVERRIDE.booleanValue())));
    }

    @Test
    @Transactional
    public void getProjects() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get the projects
        restProjectsMockMvc.perform(get("/api/projects/{id}", projects.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(projects.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME.toString()))
            .andExpect(jsonPath("$.runOfShowFlag").value(DEFAULT_RUN_OF_SHOW_FLAG.booleanValue()))
            .andExpect(jsonPath("$.template").value(DEFAULT_TEMPLATE.booleanValue()))
            .andExpect(jsonPath("$.labFlag").value(DEFAULT_LAB_FLAG.booleanValue()))
            .andExpect(jsonPath("$.alfrescoTitle1").value(DEFAULT_ALFRESCO_TITLE_1.toString()))
            .andExpect(jsonPath("$.alfrescoTitle2").value(DEFAULT_ALFRESCO_TITLE_2.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.actorsWithRights").value(DEFAULT_ACTORS_WITH_RIGHTS.intValue()))
            .andExpect(jsonPath("$.daysShooting").value(DEFAULT_DAYS_SHOOTING.intValue()))
            .andExpect(jsonPath("$.weeksShooting").value(DEFAULT_WEEKS_SHOOTING.intValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.sensitiveViewing").value(DEFAULT_SENSITIVE_VIEWING.booleanValue()))
            .andExpect(jsonPath("$.productionCompanyNotes").value(DEFAULT_PRODUCTION_COMPANY_NOTES.toString()))
            .andExpect(jsonPath("$.productionCompanyShippingNumber").value(DEFAULT_PRODUCTION_COMPANY_SHIPPING_NUMBER.toString()))
            .andExpect(jsonPath("$.processingDeliveries").value(DEFAULT_PROCESSING_DELIVERIES.toString()))
            .andExpect(jsonPath("$.processingSpecialInstructions").value(DEFAULT_PROCESSING_SPECIAL_INSTRUCTIONS.toString()))
            .andExpect(jsonPath("$.processingWatermark").value(DEFAULT_PROCESSING_WATERMARK.toString()))
            .andExpect(jsonPath("$.processingCopyright").value(DEFAULT_PROCESSING_COPYRIGHT.toString()))
            .andExpect(jsonPath("$.labProofNotes").value(DEFAULT_LAB_PROOF_NOTES.toString()))
            .andExpect(jsonPath("$.labLastProofImageNumber").value(DEFAULT_LAB_LAST_PROOF_IMAGE_NUMBER.toString()))
            .andExpect(jsonPath("$.labLastProofPageNumber").value(DEFAULT_LAB_LAST_PROOF_PAGE_NUMBER.toString()))
            .andExpect(jsonPath("$.labImageNumberSchema").value(DEFAULT_LAB_IMAGE_NUMBER_SCHEMA.toString()))
            .andExpect(jsonPath("$.labFolderBatchSchema").value(DEFAULT_LAB_FOLDER_BATCH_SCHEMA.toString()))
            .andExpect(jsonPath("$.photoLabInfo").value(DEFAULT_PHOTO_LAB_INFO.toString()))
            .andExpect(jsonPath("$.projectUnitPhotoNotes").value(DEFAULT_PROJECT_UNIT_PHOTO_NOTES.toString()))
            .andExpect(jsonPath("$.projectInfoNotes").value(DEFAULT_PROJECT_INFO_NOTES.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE.toString()))
            .andExpect(jsonPath("$.legacyDirector").value(DEFAULT_LEGACY_DIRECTOR.toString()))
            .andExpect(jsonPath("$.legacyExecutiveProducer").value(DEFAULT_LEGACY_EXECUTIVE_PRODUCER.toString()))
            .andExpect(jsonPath("$.legacyExecutiveProducer2").value(DEFAULT_LEGACY_EXECUTIVE_PRODUCER_2.toString()))
            .andExpect(jsonPath("$.legacyExecutiveProducer3").value(DEFAULT_LEGACY_EXECUTIVE_PRODUCER_3.toString()))
            .andExpect(jsonPath("$.legacyExecutiveProducer4").value(DEFAULT_LEGACY_EXECUTIVE_PRODUCER_4.toString()))
            .andExpect(jsonPath("$.legacyProducer").value(DEFAULT_LEGACY_PRODUCER.toString()))
            .andExpect(jsonPath("$.legacyProducer2").value(DEFAULT_LEGACY_PRODUCER_2.toString()))
            .andExpect(jsonPath("$.legacyProducer3").value(DEFAULT_LEGACY_PRODUCER_3.toString()))
            .andExpect(jsonPath("$.legacyProducer4").value(DEFAULT_LEGACY_PRODUCER_4.toString()))
            .andExpect(jsonPath("$.legacyAdditionalTalent").value(DEFAULT_LEGACY_ADDITIONAL_TALENT.toString()))
            .andExpect(jsonPath("$.themeId").value(DEFAULT_THEME_ID.booleanValue()))
            .andExpect(jsonPath("$.sptPhotoSubtype").value(DEFAULT_SPT_PHOTO_SUBTYPE.toString()))
            .andExpect(jsonPath("$.photoCredit").value(DEFAULT_PHOTO_CREDIT.toString()))
            .andExpect(jsonPath("$.shootDate").value(DEFAULT_SHOOT_DATE.toString()))
            .andExpect(jsonPath("$.shootDateOverride").value(DEFAULT_SHOOT_DATE_OVERRIDE.booleanValue()))
            .andExpect(jsonPath("$.unitPhotographerOverride").value(DEFAULT_UNIT_PHOTOGRAPHER_OVERRIDE.booleanValue()))
            .andExpect(jsonPath("$.useSetup").value(DEFAULT_USE_SETUP.booleanValue()))
            .andExpect(jsonPath("$.useExif").value(DEFAULT_USE_EXIF.booleanValue()))
            .andExpect(jsonPath("$.tagDate").value(DEFAULT_TAG_DATE.toString()))
            .andExpect(jsonPath("$.tagreportIndex").value(DEFAULT_TAGREPORT_INDEX))
            .andExpect(jsonPath("$.loginMessage").value(DEFAULT_LOGIN_MESSAGE.toString()))
            .andExpect(jsonPath("$.loginMessageActive").value(DEFAULT_LOGIN_MESSAGE_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.topLevelAlbums").value(DEFAULT_TOP_LEVEL_ALBUMS.booleanValue()))
            .andExpect(jsonPath("$.enableTertiary").value(DEFAULT_ENABLE_TERTIARY.booleanValue()))
            .andExpect(jsonPath("$.invoiceCreated").value(DEFAULT_INVOICE_CREATED.intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.toString()))
            .andExpect(jsonPath("$.foxTitle").value(DEFAULT_FOX_TITLE.toString()))
            .andExpect(jsonPath("$.isAsset").value(DEFAULT_IS_ASSET.booleanValue()))
            .andExpect(jsonPath("$.fullRejection").value(DEFAULT_FULL_REJECTION))
            .andExpect(jsonPath("$.disabled").value(DEFAULT_DISABLED.booleanValue()))
            .andExpect(jsonPath("$.reminderDate").value(DEFAULT_REMINDER_DATE.toString()))
            .andExpect(jsonPath("$.photoCreditOverride").value(DEFAULT_PHOTO_CREDIT_OVERRIDE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProjects() throws Exception {
        // Get the projects
        restProjectsMockMvc.perform(get("/api/projects/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjects() throws Exception {
        // Initialize the database
        projectsService.save(projects);

        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();

        // Update the projects
        Projects updatedProjects = new Projects();
        updatedProjects.setId(projects.getId());
        updatedProjects.setName(UPDATED_NAME);
        updatedProjects.setFullName(UPDATED_FULL_NAME);
        updatedProjects.setRunOfShowFlag(UPDATED_RUN_OF_SHOW_FLAG);
        updatedProjects.setTemplate(UPDATED_TEMPLATE);
        updatedProjects.setLabFlag(UPDATED_LAB_FLAG);
        updatedProjects.setAlfrescoTitle1(UPDATED_ALFRESCO_TITLE_1);
        updatedProjects.setAlfrescoTitle2(UPDATED_ALFRESCO_TITLE_2);
        updatedProjects.setStartDate(UPDATED_START_DATE);
        updatedProjects.setEndDate(UPDATED_END_DATE);
        updatedProjects.setActorsWithRights(UPDATED_ACTORS_WITH_RIGHTS);
        updatedProjects.setDaysShooting(UPDATED_DAYS_SHOOTING);
        updatedProjects.setWeeksShooting(UPDATED_WEEKS_SHOOTING);
        updatedProjects.setNotes(UPDATED_NOTES);
        updatedProjects.setSensitiveViewing(UPDATED_SENSITIVE_VIEWING);
        updatedProjects.setProductionCompanyNotes(UPDATED_PRODUCTION_COMPANY_NOTES);
        updatedProjects.setProductionCompanyShippingNumber(UPDATED_PRODUCTION_COMPANY_SHIPPING_NUMBER);
        updatedProjects.setProcessingDeliveries(UPDATED_PROCESSING_DELIVERIES);
        updatedProjects.setProcessingSpecialInstructions(UPDATED_PROCESSING_SPECIAL_INSTRUCTIONS);
        updatedProjects.setProcessingWatermark(UPDATED_PROCESSING_WATERMARK);
        updatedProjects.setProcessingCopyright(UPDATED_PROCESSING_COPYRIGHT);
        updatedProjects.setLabProofNotes(UPDATED_LAB_PROOF_NOTES);
        updatedProjects.setLabLastProofImageNumber(UPDATED_LAB_LAST_PROOF_IMAGE_NUMBER);
        updatedProjects.setLabLastProofPageNumber(UPDATED_LAB_LAST_PROOF_PAGE_NUMBER);
        updatedProjects.setLabImageNumberSchema(UPDATED_LAB_IMAGE_NUMBER_SCHEMA);
        updatedProjects.setLabFolderBatchSchema(UPDATED_LAB_FOLDER_BATCH_SCHEMA);
        updatedProjects.setPhotoLabInfo(UPDATED_PHOTO_LAB_INFO);
        updatedProjects.setProjectUnitPhotoNotes(UPDATED_PROJECT_UNIT_PHOTO_NOTES);
        updatedProjects.setProjectInfoNotes(UPDATED_PROJECT_INFO_NOTES);
        updatedProjects.setCreatedDate(UPDATED_CREATED_DATE);
        updatedProjects.setUpdatedDate(UPDATED_UPDATED_DATE);
        updatedProjects.setLegacyDirector(UPDATED_LEGACY_DIRECTOR);
        updatedProjects.setLegacyExecutiveProducer(UPDATED_LEGACY_EXECUTIVE_PRODUCER);
        updatedProjects.setLegacyExecutiveProducer2(UPDATED_LEGACY_EXECUTIVE_PRODUCER_2);
        updatedProjects.setLegacyExecutiveProducer3(UPDATED_LEGACY_EXECUTIVE_PRODUCER_3);
        updatedProjects.setLegacyExecutiveProducer4(UPDATED_LEGACY_EXECUTIVE_PRODUCER_4);
        updatedProjects.setLegacyProducer(UPDATED_LEGACY_PRODUCER);
        updatedProjects.setLegacyProducer2(UPDATED_LEGACY_PRODUCER_2);
        updatedProjects.setLegacyProducer3(UPDATED_LEGACY_PRODUCER_3);
        updatedProjects.setLegacyProducer4(UPDATED_LEGACY_PRODUCER_4);
        updatedProjects.setLegacyAdditionalTalent(UPDATED_LEGACY_ADDITIONAL_TALENT);
        updatedProjects.setThemeId(UPDATED_THEME_ID);
        updatedProjects.setSptPhotoSubtype(UPDATED_SPT_PHOTO_SUBTYPE);
        updatedProjects.setPhotoCredit(UPDATED_PHOTO_CREDIT);
        updatedProjects.setShootDate(UPDATED_SHOOT_DATE);
        updatedProjects.setShootDateOverride(UPDATED_SHOOT_DATE_OVERRIDE);
        updatedProjects.setUnitPhotographerOverride(UPDATED_UNIT_PHOTOGRAPHER_OVERRIDE);
        updatedProjects.setUseSetup(UPDATED_USE_SETUP);
        updatedProjects.setUseExif(UPDATED_USE_EXIF);
        updatedProjects.setTagDate(UPDATED_TAG_DATE);
        updatedProjects.setTagreportIndex(UPDATED_TAGREPORT_INDEX);
        updatedProjects.setLoginMessage(UPDATED_LOGIN_MESSAGE);
        updatedProjects.setLoginMessageActive(UPDATED_LOGIN_MESSAGE_ACTIVE);
        updatedProjects.setTopLevelAlbums(UPDATED_TOP_LEVEL_ALBUMS);
        updatedProjects.setEnableTertiary(UPDATED_ENABLE_TERTIARY);
        updatedProjects.setInvoiceCreated(UPDATED_INVOICE_CREATED);
        updatedProjects.setPrice(UPDATED_PRICE);
        updatedProjects.setFoxTitle(UPDATED_FOX_TITLE);
        updatedProjects.setIsAsset(UPDATED_IS_ASSET);
        updatedProjects.setFullRejection(UPDATED_FULL_REJECTION);
        updatedProjects.setDisabled(UPDATED_DISABLED);
        updatedProjects.setReminderDate(UPDATED_REMINDER_DATE);
        updatedProjects.setPhotoCreditOverride(UPDATED_PHOTO_CREDIT_OVERRIDE);

        restProjectsMockMvc.perform(put("/api/projects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProjects)))
                .andExpect(status().isOk());

        // Validate the Projects in the database
        List<Projects> projects = projectsRepository.findAll();
        assertThat(projects).hasSize(databaseSizeBeforeUpdate);
        Projects testProjects = projects.get(projects.size() - 1);
        assertThat(testProjects.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProjects.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testProjects.isRunOfShowFlag()).isEqualTo(UPDATED_RUN_OF_SHOW_FLAG);
        assertThat(testProjects.isTemplate()).isEqualTo(UPDATED_TEMPLATE);
        assertThat(testProjects.isLabFlag()).isEqualTo(UPDATED_LAB_FLAG);
        assertThat(testProjects.getAlfrescoTitle1()).isEqualTo(UPDATED_ALFRESCO_TITLE_1);
        assertThat(testProjects.getAlfrescoTitle2()).isEqualTo(UPDATED_ALFRESCO_TITLE_2);
        assertThat(testProjects.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testProjects.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testProjects.getActorsWithRights()).isEqualTo(UPDATED_ACTORS_WITH_RIGHTS);
        assertThat(testProjects.getDaysShooting()).isEqualTo(UPDATED_DAYS_SHOOTING);
        assertThat(testProjects.getWeeksShooting()).isEqualTo(UPDATED_WEEKS_SHOOTING);
        assertThat(testProjects.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testProjects.isSensitiveViewing()).isEqualTo(UPDATED_SENSITIVE_VIEWING);
        assertThat(testProjects.getProductionCompanyNotes()).isEqualTo(UPDATED_PRODUCTION_COMPANY_NOTES);
        assertThat(testProjects.getProductionCompanyShippingNumber()).isEqualTo(UPDATED_PRODUCTION_COMPANY_SHIPPING_NUMBER);
        assertThat(testProjects.getProcessingDeliveries()).isEqualTo(UPDATED_PROCESSING_DELIVERIES);
        assertThat(testProjects.getProcessingSpecialInstructions()).isEqualTo(UPDATED_PROCESSING_SPECIAL_INSTRUCTIONS);
        assertThat(testProjects.getProcessingWatermark()).isEqualTo(UPDATED_PROCESSING_WATERMARK);
        assertThat(testProjects.getProcessingCopyright()).isEqualTo(UPDATED_PROCESSING_COPYRIGHT);
        assertThat(testProjects.getLabProofNotes()).isEqualTo(UPDATED_LAB_PROOF_NOTES);
        assertThat(testProjects.getLabLastProofImageNumber()).isEqualTo(UPDATED_LAB_LAST_PROOF_IMAGE_NUMBER);
        assertThat(testProjects.getLabLastProofPageNumber()).isEqualTo(UPDATED_LAB_LAST_PROOF_PAGE_NUMBER);
        assertThat(testProjects.getLabImageNumberSchema()).isEqualTo(UPDATED_LAB_IMAGE_NUMBER_SCHEMA);
        assertThat(testProjects.getLabFolderBatchSchema()).isEqualTo(UPDATED_LAB_FOLDER_BATCH_SCHEMA);
        assertThat(testProjects.getPhotoLabInfo()).isEqualTo(UPDATED_PHOTO_LAB_INFO);
        assertThat(testProjects.getProjectUnitPhotoNotes()).isEqualTo(UPDATED_PROJECT_UNIT_PHOTO_NOTES);
        assertThat(testProjects.getProjectInfoNotes()).isEqualTo(UPDATED_PROJECT_INFO_NOTES);
        assertThat(testProjects.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProjects.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testProjects.getLegacyDirector()).isEqualTo(UPDATED_LEGACY_DIRECTOR);
        assertThat(testProjects.getLegacyExecutiveProducer()).isEqualTo(UPDATED_LEGACY_EXECUTIVE_PRODUCER);
        assertThat(testProjects.getLegacyExecutiveProducer2()).isEqualTo(UPDATED_LEGACY_EXECUTIVE_PRODUCER_2);
        assertThat(testProjects.getLegacyExecutiveProducer3()).isEqualTo(UPDATED_LEGACY_EXECUTIVE_PRODUCER_3);
        assertThat(testProjects.getLegacyExecutiveProducer4()).isEqualTo(UPDATED_LEGACY_EXECUTIVE_PRODUCER_4);
        assertThat(testProjects.getLegacyProducer()).isEqualTo(UPDATED_LEGACY_PRODUCER);
        assertThat(testProjects.getLegacyProducer2()).isEqualTo(UPDATED_LEGACY_PRODUCER_2);
        assertThat(testProjects.getLegacyProducer3()).isEqualTo(UPDATED_LEGACY_PRODUCER_3);
        assertThat(testProjects.getLegacyProducer4()).isEqualTo(UPDATED_LEGACY_PRODUCER_4);
        assertThat(testProjects.getLegacyAdditionalTalent()).isEqualTo(UPDATED_LEGACY_ADDITIONAL_TALENT);
        assertThat(testProjects.isThemeId()).isEqualTo(UPDATED_THEME_ID);
        assertThat(testProjects.getSptPhotoSubtype()).isEqualTo(UPDATED_SPT_PHOTO_SUBTYPE);
        assertThat(testProjects.getPhotoCredit()).isEqualTo(UPDATED_PHOTO_CREDIT);
        assertThat(testProjects.getShootDate()).isEqualTo(UPDATED_SHOOT_DATE);
        assertThat(testProjects.isShootDateOverride()).isEqualTo(UPDATED_SHOOT_DATE_OVERRIDE);
        assertThat(testProjects.isUnitPhotographerOverride()).isEqualTo(UPDATED_UNIT_PHOTOGRAPHER_OVERRIDE);
        assertThat(testProjects.isUseSetup()).isEqualTo(UPDATED_USE_SETUP);
        assertThat(testProjects.isUseExif()).isEqualTo(UPDATED_USE_EXIF);
        assertThat(testProjects.getTagDate()).isEqualTo(UPDATED_TAG_DATE);
        assertThat(testProjects.getTagreportIndex()).isEqualTo(UPDATED_TAGREPORT_INDEX);
        assertThat(testProjects.getLoginMessage()).isEqualTo(UPDATED_LOGIN_MESSAGE);
        assertThat(testProjects.isLoginMessageActive()).isEqualTo(UPDATED_LOGIN_MESSAGE_ACTIVE);
        assertThat(testProjects.isTopLevelAlbums()).isEqualTo(UPDATED_TOP_LEVEL_ALBUMS);
        assertThat(testProjects.isEnableTertiary()).isEqualTo(UPDATED_ENABLE_TERTIARY);
        assertThat(testProjects.getInvoiceCreated()).isEqualTo(UPDATED_INVOICE_CREATED);
        assertThat(testProjects.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProjects.getFoxTitle()).isEqualTo(UPDATED_FOX_TITLE);
        assertThat(testProjects.isIsAsset()).isEqualTo(UPDATED_IS_ASSET);
        assertThat(testProjects.getFullRejection()).isEqualTo(UPDATED_FULL_REJECTION);
        assertThat(testProjects.isDisabled()).isEqualTo(UPDATED_DISABLED);
        assertThat(testProjects.getReminderDate()).isEqualTo(UPDATED_REMINDER_DATE);
        assertThat(testProjects.isPhotoCreditOverride()).isEqualTo(UPDATED_PHOTO_CREDIT_OVERRIDE);

        // Validate the Projects in ElasticSearch
        Projects projectsEs = projectsSearchRepository.findOne(testProjects.getId());
        assertThat(projectsEs).isEqualToComparingFieldByField(testProjects);
    }

    @Test
    @Transactional
    public void deleteProjects() throws Exception {
        // Initialize the database
        projectsService.save(projects);

        int databaseSizeBeforeDelete = projectsRepository.findAll().size();

        // Get the projects
        restProjectsMockMvc.perform(delete("/api/projects/{id}", projects.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean projectsExistsInEs = projectsSearchRepository.exists(projects.getId());
        assertThat(projectsExistsInEs).isFalse();

        // Validate the database is empty
        List<Projects> projects = projectsRepository.findAll();
        assertThat(projects).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProjects() throws Exception {
        // Initialize the database
        projectsService.save(projects);

        // Search the projects
        restProjectsMockMvc.perform(get("/api/_search/projects?query=id:" + projects.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projects.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME.toString())))
            .andExpect(jsonPath("$.[*].runOfShowFlag").value(hasItem(DEFAULT_RUN_OF_SHOW_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].template").value(hasItem(DEFAULT_TEMPLATE.booleanValue())))
            .andExpect(jsonPath("$.[*].labFlag").value(hasItem(DEFAULT_LAB_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].alfrescoTitle1").value(hasItem(DEFAULT_ALFRESCO_TITLE_1.toString())))
            .andExpect(jsonPath("$.[*].alfrescoTitle2").value(hasItem(DEFAULT_ALFRESCO_TITLE_2.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].actorsWithRights").value(hasItem(DEFAULT_ACTORS_WITH_RIGHTS.intValue())))
            .andExpect(jsonPath("$.[*].daysShooting").value(hasItem(DEFAULT_DAYS_SHOOTING.intValue())))
            .andExpect(jsonPath("$.[*].weeksShooting").value(hasItem(DEFAULT_WEEKS_SHOOTING.intValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].sensitiveViewing").value(hasItem(DEFAULT_SENSITIVE_VIEWING.booleanValue())))
            .andExpect(jsonPath("$.[*].productionCompanyNotes").value(hasItem(DEFAULT_PRODUCTION_COMPANY_NOTES.toString())))
            .andExpect(jsonPath("$.[*].productionCompanyShippingNumber").value(hasItem(DEFAULT_PRODUCTION_COMPANY_SHIPPING_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].processingDeliveries").value(hasItem(DEFAULT_PROCESSING_DELIVERIES.toString())))
            .andExpect(jsonPath("$.[*].processingSpecialInstructions").value(hasItem(DEFAULT_PROCESSING_SPECIAL_INSTRUCTIONS.toString())))
            .andExpect(jsonPath("$.[*].processingWatermark").value(hasItem(DEFAULT_PROCESSING_WATERMARK.toString())))
            .andExpect(jsonPath("$.[*].processingCopyright").value(hasItem(DEFAULT_PROCESSING_COPYRIGHT.toString())))
            .andExpect(jsonPath("$.[*].labProofNotes").value(hasItem(DEFAULT_LAB_PROOF_NOTES.toString())))
            .andExpect(jsonPath("$.[*].labLastProofImageNumber").value(hasItem(DEFAULT_LAB_LAST_PROOF_IMAGE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].labLastProofPageNumber").value(hasItem(DEFAULT_LAB_LAST_PROOF_PAGE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].labImageNumberSchema").value(hasItem(DEFAULT_LAB_IMAGE_NUMBER_SCHEMA.toString())))
            .andExpect(jsonPath("$.[*].labFolderBatchSchema").value(hasItem(DEFAULT_LAB_FOLDER_BATCH_SCHEMA.toString())))
            .andExpect(jsonPath("$.[*].photoLabInfo").value(hasItem(DEFAULT_PHOTO_LAB_INFO.toString())))
            .andExpect(jsonPath("$.[*].projectUnitPhotoNotes").value(hasItem(DEFAULT_PROJECT_UNIT_PHOTO_NOTES.toString())))
            .andExpect(jsonPath("$.[*].projectInfoNotes").value(hasItem(DEFAULT_PROJECT_INFO_NOTES.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].legacyDirector").value(hasItem(DEFAULT_LEGACY_DIRECTOR.toString())))
            .andExpect(jsonPath("$.[*].legacyExecutiveProducer").value(hasItem(DEFAULT_LEGACY_EXECUTIVE_PRODUCER.toString())))
            .andExpect(jsonPath("$.[*].legacyExecutiveProducer2").value(hasItem(DEFAULT_LEGACY_EXECUTIVE_PRODUCER_2.toString())))
            .andExpect(jsonPath("$.[*].legacyExecutiveProducer3").value(hasItem(DEFAULT_LEGACY_EXECUTIVE_PRODUCER_3.toString())))
            .andExpect(jsonPath("$.[*].legacyExecutiveProducer4").value(hasItem(DEFAULT_LEGACY_EXECUTIVE_PRODUCER_4.toString())))
            .andExpect(jsonPath("$.[*].legacyProducer").value(hasItem(DEFAULT_LEGACY_PRODUCER.toString())))
            .andExpect(jsonPath("$.[*].legacyProducer2").value(hasItem(DEFAULT_LEGACY_PRODUCER_2.toString())))
            .andExpect(jsonPath("$.[*].legacyProducer3").value(hasItem(DEFAULT_LEGACY_PRODUCER_3.toString())))
            .andExpect(jsonPath("$.[*].legacyProducer4").value(hasItem(DEFAULT_LEGACY_PRODUCER_4.toString())))
            .andExpect(jsonPath("$.[*].legacyAdditionalTalent").value(hasItem(DEFAULT_LEGACY_ADDITIONAL_TALENT.toString())))
            .andExpect(jsonPath("$.[*].themeId").value(hasItem(DEFAULT_THEME_ID.booleanValue())))
            .andExpect(jsonPath("$.[*].sptPhotoSubtype").value(hasItem(DEFAULT_SPT_PHOTO_SUBTYPE.toString())))
            .andExpect(jsonPath("$.[*].photoCredit").value(hasItem(DEFAULT_PHOTO_CREDIT.toString())))
            .andExpect(jsonPath("$.[*].shootDate").value(hasItem(DEFAULT_SHOOT_DATE.toString())))
            .andExpect(jsonPath("$.[*].shootDateOverride").value(hasItem(DEFAULT_SHOOT_DATE_OVERRIDE.booleanValue())))
            .andExpect(jsonPath("$.[*].unitPhotographerOverride").value(hasItem(DEFAULT_UNIT_PHOTOGRAPHER_OVERRIDE.booleanValue())))
            .andExpect(jsonPath("$.[*].useSetup").value(hasItem(DEFAULT_USE_SETUP.booleanValue())))
            .andExpect(jsonPath("$.[*].useExif").value(hasItem(DEFAULT_USE_EXIF.booleanValue())))
            .andExpect(jsonPath("$.[*].tagDate").value(hasItem(DEFAULT_TAG_DATE.toString())))
            .andExpect(jsonPath("$.[*].tagreportIndex").value(hasItem(DEFAULT_TAGREPORT_INDEX)))
            .andExpect(jsonPath("$.[*].loginMessage").value(hasItem(DEFAULT_LOGIN_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].loginMessageActive").value(hasItem(DEFAULT_LOGIN_MESSAGE_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].topLevelAlbums").value(hasItem(DEFAULT_TOP_LEVEL_ALBUMS.booleanValue())))
            .andExpect(jsonPath("$.[*].enableTertiary").value(hasItem(DEFAULT_ENABLE_TERTIARY.booleanValue())))
            .andExpect(jsonPath("$.[*].invoiceCreated").value(hasItem(DEFAULT_INVOICE_CREATED.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.toString())))
            .andExpect(jsonPath("$.[*].foxTitle").value(hasItem(DEFAULT_FOX_TITLE.toString())))
            .andExpect(jsonPath("$.[*].isAsset").value(hasItem(DEFAULT_IS_ASSET.booleanValue())))
            .andExpect(jsonPath("$.[*].fullRejection").value(hasItem(DEFAULT_FULL_REJECTION)))
            .andExpect(jsonPath("$.[*].disabled").value(hasItem(DEFAULT_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].reminderDate").value(hasItem(DEFAULT_REMINDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].photoCreditOverride").value(hasItem(DEFAULT_PHOTO_CREDIT_OVERRIDE.booleanValue())));
    }
}
