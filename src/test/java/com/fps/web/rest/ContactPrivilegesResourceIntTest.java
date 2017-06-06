package com.fps.web.rest;

import com.fps.SmartLpcApp;
import com.fps.domain.ContactPrivileges;
import com.fps.repository.ContactPrivilegesRepository;
import com.fps.elastics.search.ContactPrivilegesSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ContactPrivilegesResource REST controller.
 *
 * @see ContactPrivilegesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartLpcApp.class)
@WebAppConfiguration
@IntegrationTest
public class ContactPrivilegesResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_AUTHOR = "AAAAA";
    private static final String UPDATED_AUTHOR = "BBBBB";

    private static final Boolean DEFAULT_EXEC = false;
    private static final Boolean UPDATED_EXEC = true;

    private static final Boolean DEFAULT_CAPTIONING = false;
    private static final Boolean UPDATED_CAPTIONING = true;

    private static final Integer DEFAULT_DOWNLOAD_TYPE = 1;
    private static final Integer UPDATED_DOWNLOAD_TYPE = 2;

    private static final Boolean DEFAULT_EMAIL = false;
    private static final Boolean UPDATED_EMAIL = true;

    private static final Boolean DEFAULT_PRINT = false;
    private static final Boolean UPDATED_PRINT = true;

    private static final Boolean DEFAULT_LOCK_APPROVE_RESTRICTION = false;
    private static final Boolean UPDATED_LOCK_APPROVE_RESTRICTION = true;

    private static final Boolean DEFAULT_PRIORITY_PIX = false;
    private static final Boolean UPDATED_PRIORITY_PIX = true;

    private static final Boolean DEFAULT_RELEASE_EXCLUDE = false;
    private static final Boolean UPDATED_RELEASE_EXCLUDE = true;

    private static final Boolean DEFAULT_VIEW_SENSITIVE = false;
    private static final Boolean UPDATED_VIEW_SENSITIVE = true;

    private static final Boolean DEFAULT_WATERMARK = false;
    private static final Boolean UPDATED_WATERMARK = true;

    private static final Double DEFAULT_WATERMARK_INNER_TRANSPARENCY = 1D;
    private static final Double UPDATED_WATERMARK_INNER_TRANSPARENCY = 2D;

    private static final Double DEFAULT_WATERMARK_OUTER_TRANSPARENCY = 1D;
    private static final Double UPDATED_WATERMARK_OUTER_TRANSPARENCY = 2D;

    private static final Boolean DEFAULT_INTERNAL = false;
    private static final Boolean UPDATED_INTERNAL = true;

    private static final Boolean DEFAULT_VENDOR = false;
    private static final Boolean UPDATED_VENDOR = true;
    private static final String DEFAULT_RESTART_ROLE = "AAAAA";
    private static final String UPDATED_RESTART_ROLE = "BBBBB";
    private static final String DEFAULT_RESTART_IMAGE = "AAAAA";
    private static final String UPDATED_RESTART_IMAGE = "BBBBB";

    private static final Long DEFAULT_RESTART_PAGE = 1L;
    private static final Long UPDATED_RESTART_PAGE = 2L;

    private static final ZonedDateTime DEFAULT_LAST_LOGIN_DT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_LOGIN_DT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_LOGIN_DT_STR = dateTimeFormatter.format(DEFAULT_LAST_LOGIN_DT);

    private static final ZonedDateTime DEFAULT_LAST_LOGOUT_DT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_LOGOUT_DT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_LOGOUT_DT_STR = dateTimeFormatter.format(DEFAULT_LAST_LOGOUT_DT);

    private static final Boolean DEFAULT_DISABLED = false;
    private static final Boolean UPDATED_DISABLED = true;

    private static final Boolean DEFAULT_WELCOME_MESSAGE = false;
    private static final Boolean UPDATED_WELCOME_MESSAGE = true;

    private static final Boolean DEFAULT_IS_ABC_VIEWER = false;
    private static final Boolean UPDATED_IS_ABC_VIEWER = true;

    private static final Boolean DEFAULT_TALENT_MANAGEMENT = false;
    private static final Boolean UPDATED_TALENT_MANAGEMENT = true;

    private static final Boolean DEFAULT_SIGNOFF_MANAGEMENT = false;
    private static final Boolean UPDATED_SIGNOFF_MANAGEMENT = true;

    private static final Boolean DEFAULT_DATGEDIT_MANAGEMENT = false;
    private static final Boolean UPDATED_DATGEDIT_MANAGEMENT = true;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATED_DATE);

    private static final ZonedDateTime DEFAULT_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_UPDATED_DATE_STR = dateTimeFormatter.format(DEFAULT_UPDATED_DATE);

    private static final ZonedDateTime DEFAULT_EXPIRE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_EXPIRE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_EXPIRE_DATE_STR = dateTimeFormatter.format(DEFAULT_EXPIRE_DATE);
    private static final String DEFAULT_RESTART_FILTER = "AAAAA";
    private static final String UPDATED_RESTART_FILTER = "BBBBB";

    private static final Integer DEFAULT_RESTART_COLUMNS = 1;
    private static final Integer UPDATED_RESTART_COLUMNS = 2;

    private static final Integer DEFAULT_RESTART_IMAGES_PER_PAGE = 1;
    private static final Integer UPDATED_RESTART_IMAGES_PER_PAGE = 2;
    private static final String DEFAULT_RESTART_IMAGE_SIZE = "AAAAA";
    private static final String UPDATED_RESTART_IMAGE_SIZE = "BBBBB";

    private static final Long DEFAULT_RESTART_TIME = 1L;
    private static final Long UPDATED_RESTART_TIME = 2L;

    private static final Boolean DEFAULT_SHOW_FINALIZATIONS = false;
    private static final Boolean UPDATED_SHOW_FINALIZATIONS = true;

    private static final Boolean DEFAULT_READ_ONLY = false;
    private static final Boolean UPDATED_READ_ONLY = true;

    private static final Boolean DEFAULT_HAS_VIDEO = false;
    private static final Boolean UPDATED_HAS_VIDEO = true;

    private static final Boolean DEFAULT_GLOBAL_ALBUM = false;
    private static final Boolean UPDATED_GLOBAL_ALBUM = true;

    private static final Boolean DEFAULT_SEES_UNTAGGED = false;
    private static final Boolean UPDATED_SEES_UNTAGGED = true;

    private static final Integer DEFAULT_LOGIN_COUNT = 1;
    private static final Integer UPDATED_LOGIN_COUNT = 2;

    private static final Integer DEFAULT_EXCLUSIVES = 0;
    private static final Integer UPDATED_EXCLUSIVES = 1;

    private static final Long DEFAULT_DEFAULT_ALBUM = 1L;
    private static final Long UPDATED_DEFAULT_ALBUM = 2L;

    private static final Boolean DEFAULT_CRITIQUE_IT = false;
    private static final Boolean UPDATED_CRITIQUE_IT = true;

    private static final Boolean DEFAULT_ADHOC_LINK = false;
    private static final Boolean UPDATED_ADHOC_LINK = true;

    private static final Boolean DEFAULT_RETOUCH = false;
    private static final Boolean UPDATED_RETOUCH = true;

    private static final Boolean DEFAULT_FILE_UPLOAD = false;
    private static final Boolean UPDATED_FILE_UPLOAD = true;

    private static final Boolean DEFAULT_DELETE_ASSETS = false;
    private static final Boolean UPDATED_DELETE_ASSETS = true;

    @Inject
    private ContactPrivilegesRepository contactPrivilegesRepository;

    @Inject
    private ContactPrivilegesSearchRepository contactPrivilegesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restContactPrivilegesMockMvc;

    private ContactPrivileges contactPrivileges;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContactPrivilegesResource contactPrivilegesResource = new ContactPrivilegesResource();
        ReflectionTestUtils.setField(contactPrivilegesResource, "contactPrivilegesSearchRepository", contactPrivilegesSearchRepository);
        ReflectionTestUtils.setField(contactPrivilegesResource, "contactPrivilegesRepository", contactPrivilegesRepository);
        this.restContactPrivilegesMockMvc = MockMvcBuilders.standaloneSetup(contactPrivilegesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        contactPrivilegesSearchRepository.deleteAll();
        contactPrivileges = new ContactPrivileges();
        contactPrivileges.setName(DEFAULT_NAME);
        contactPrivileges.setTitle(DEFAULT_TITLE);
        contactPrivileges.setDescription(DEFAULT_DESCRIPTION);
        contactPrivileges.setAuthor(DEFAULT_AUTHOR);
        contactPrivileges.setExec(DEFAULT_EXEC);
        contactPrivileges.setCaptioning(DEFAULT_CAPTIONING);
        contactPrivileges.setDownloadType(DEFAULT_DOWNLOAD_TYPE);
        contactPrivileges.setEmail(DEFAULT_EMAIL);
        contactPrivileges.setPrint(DEFAULT_PRINT);
        contactPrivileges.setLockApproveRestriction(DEFAULT_LOCK_APPROVE_RESTRICTION);
        contactPrivileges.setPriorityPix(DEFAULT_PRIORITY_PIX);
        contactPrivileges.setReleaseExclude(DEFAULT_RELEASE_EXCLUDE);
        contactPrivileges.setViewSensitive(DEFAULT_VIEW_SENSITIVE);
        contactPrivileges.setWatermark(DEFAULT_WATERMARK);
        contactPrivileges.setWatermarkInnerTransparency(DEFAULT_WATERMARK_INNER_TRANSPARENCY);
        contactPrivileges.setWatermarkOuterTransparency(DEFAULT_WATERMARK_OUTER_TRANSPARENCY);
        contactPrivileges.setInternal(DEFAULT_INTERNAL);
        contactPrivileges.setVendor(DEFAULT_VENDOR);
        contactPrivileges.setRestartRole(DEFAULT_RESTART_ROLE);
        contactPrivileges.setRestartImage(DEFAULT_RESTART_IMAGE);
        contactPrivileges.setRestartPage(DEFAULT_RESTART_PAGE);
        contactPrivileges.setLastLoginDt(DEFAULT_LAST_LOGIN_DT);
        contactPrivileges.setLastLogoutDt(DEFAULT_LAST_LOGOUT_DT);
        contactPrivileges.setDisabled(DEFAULT_DISABLED);
        contactPrivileges.setWelcomeMessage(DEFAULT_WELCOME_MESSAGE);
        contactPrivileges.setIsABCViewer(DEFAULT_IS_ABC_VIEWER);
        contactPrivileges.setTalentManagement(DEFAULT_TALENT_MANAGEMENT);
        contactPrivileges.setSignoffManagement(DEFAULT_SIGNOFF_MANAGEMENT);
        contactPrivileges.setDatgeditManagement(DEFAULT_DATGEDIT_MANAGEMENT);
        contactPrivileges.setCreatedDate(DEFAULT_CREATED_DATE);
        contactPrivileges.setUpdatedDate(DEFAULT_UPDATED_DATE);
        contactPrivileges.setExpireDate(DEFAULT_EXPIRE_DATE);
        contactPrivileges.setRestartFilter(DEFAULT_RESTART_FILTER);
        contactPrivileges.setRestartColumns(DEFAULT_RESTART_COLUMNS);
        contactPrivileges.setRestartImagesPerPage(DEFAULT_RESTART_IMAGES_PER_PAGE);
        contactPrivileges.setRestartImageSize(DEFAULT_RESTART_IMAGE_SIZE);
        contactPrivileges.setRestartTime(DEFAULT_RESTART_TIME);
        contactPrivileges.setShowFinalizations(DEFAULT_SHOW_FINALIZATIONS);
        contactPrivileges.setReadOnly(DEFAULT_READ_ONLY);
        contactPrivileges.setHasVideo(DEFAULT_HAS_VIDEO);
        contactPrivileges.setGlobalAlbum(DEFAULT_GLOBAL_ALBUM);
        contactPrivileges.setSeesUntagged(DEFAULT_SEES_UNTAGGED);
        contactPrivileges.setLoginCount(DEFAULT_LOGIN_COUNT);
        contactPrivileges.setExclusives(DEFAULT_EXCLUSIVES);
        contactPrivileges.setDefaultAlbum(DEFAULT_DEFAULT_ALBUM);
        contactPrivileges.setCritiqueIt(DEFAULT_CRITIQUE_IT);
        contactPrivileges.setAdhocLink(DEFAULT_ADHOC_LINK);
        contactPrivileges.setRetouch(DEFAULT_RETOUCH);
        contactPrivileges.setFileUpload(DEFAULT_FILE_UPLOAD);
        contactPrivileges.setDeleteAssets(DEFAULT_DELETE_ASSETS);
    }

    @Test
    @Transactional
    public void createContactPrivileges() throws Exception {
        int databaseSizeBeforeCreate = contactPrivilegesRepository.findAll().size();

        // Create the ContactPrivileges

        restContactPrivilegesMockMvc.perform(post("/api/contact-privileges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contactPrivileges)))
                .andExpect(status().isCreated());

        // Validate the ContactPrivileges in the database
        List<ContactPrivileges> contactPrivileges = contactPrivilegesRepository.findAll();
        assertThat(contactPrivileges).hasSize(databaseSizeBeforeCreate + 1);
        ContactPrivileges testContactPrivileges = contactPrivileges.get(contactPrivileges.size() - 1);
        assertThat(testContactPrivileges.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContactPrivileges.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testContactPrivileges.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testContactPrivileges.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testContactPrivileges.isExec()).isEqualTo(DEFAULT_EXEC);
        assertThat(testContactPrivileges.isCaptioning()).isEqualTo(DEFAULT_CAPTIONING);
        assertThat(testContactPrivileges.getDownloadType()).isEqualTo(DEFAULT_DOWNLOAD_TYPE);
        assertThat(testContactPrivileges.isEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testContactPrivileges.isPrint()).isEqualTo(DEFAULT_PRINT);
        assertThat(testContactPrivileges.isLockApproveRestriction()).isEqualTo(DEFAULT_LOCK_APPROVE_RESTRICTION);
        assertThat(testContactPrivileges.isPriorityPix()).isEqualTo(DEFAULT_PRIORITY_PIX);
        assertThat(testContactPrivileges.isReleaseExclude()).isEqualTo(DEFAULT_RELEASE_EXCLUDE);
        assertThat(testContactPrivileges.isViewSensitive()).isEqualTo(DEFAULT_VIEW_SENSITIVE);
        assertThat(testContactPrivileges.isWatermark()).isEqualTo(DEFAULT_WATERMARK);
        assertThat(testContactPrivileges.getWatermarkInnerTransparency()).isEqualTo(DEFAULT_WATERMARK_INNER_TRANSPARENCY);
        assertThat(testContactPrivileges.getWatermarkOuterTransparency()).isEqualTo(DEFAULT_WATERMARK_OUTER_TRANSPARENCY);
        assertThat(testContactPrivileges.isInternal()).isEqualTo(DEFAULT_INTERNAL);
        assertThat(testContactPrivileges.isVendor()).isEqualTo(DEFAULT_VENDOR);
        assertThat(testContactPrivileges.getRestartRole()).isEqualTo(DEFAULT_RESTART_ROLE);
        assertThat(testContactPrivileges.getRestartImage()).isEqualTo(DEFAULT_RESTART_IMAGE);
        assertThat(testContactPrivileges.getRestartPage()).isEqualTo(DEFAULT_RESTART_PAGE);
        assertThat(testContactPrivileges.getLastLoginDt()).isEqualTo(DEFAULT_LAST_LOGIN_DT);
        assertThat(testContactPrivileges.getLastLogoutDt()).isEqualTo(DEFAULT_LAST_LOGOUT_DT);
        assertThat(testContactPrivileges.isDisabled()).isEqualTo(DEFAULT_DISABLED);
        assertThat(testContactPrivileges.isWelcomeMessage()).isEqualTo(DEFAULT_WELCOME_MESSAGE);
        assertThat(testContactPrivileges.isIsABCViewer()).isEqualTo(DEFAULT_IS_ABC_VIEWER);
        assertThat(testContactPrivileges.isTalentManagement()).isEqualTo(DEFAULT_TALENT_MANAGEMENT);
        assertThat(testContactPrivileges.isSignoffManagement()).isEqualTo(DEFAULT_SIGNOFF_MANAGEMENT);
        assertThat(testContactPrivileges.isDatgeditManagement()).isEqualTo(DEFAULT_DATGEDIT_MANAGEMENT);
        assertThat(testContactPrivileges.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testContactPrivileges.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testContactPrivileges.getExpireDate()).isEqualTo(DEFAULT_EXPIRE_DATE);
        assertThat(testContactPrivileges.getRestartFilter()).isEqualTo(DEFAULT_RESTART_FILTER);
        assertThat(testContactPrivileges.getRestartColumns()).isEqualTo(DEFAULT_RESTART_COLUMNS);
        assertThat(testContactPrivileges.getRestartImagesPerPage()).isEqualTo(DEFAULT_RESTART_IMAGES_PER_PAGE);
        assertThat(testContactPrivileges.getRestartImageSize()).isEqualTo(DEFAULT_RESTART_IMAGE_SIZE);
        assertThat(testContactPrivileges.getRestartTime()).isEqualTo(DEFAULT_RESTART_TIME);
        assertThat(testContactPrivileges.isShowFinalizations()).isEqualTo(DEFAULT_SHOW_FINALIZATIONS);
        assertThat(testContactPrivileges.isReadOnly()).isEqualTo(DEFAULT_READ_ONLY);
        assertThat(testContactPrivileges.isHasVideo()).isEqualTo(DEFAULT_HAS_VIDEO);
        assertThat(testContactPrivileges.isGlobalAlbum()).isEqualTo(DEFAULT_GLOBAL_ALBUM);
        assertThat(testContactPrivileges.isSeesUntagged()).isEqualTo(DEFAULT_SEES_UNTAGGED);
        assertThat(testContactPrivileges.getLoginCount()).isEqualTo(DEFAULT_LOGIN_COUNT);
        assertThat(testContactPrivileges.getExclusives()).isEqualTo(DEFAULT_EXCLUSIVES);
        assertThat(testContactPrivileges.getDefaultAlbum()).isEqualTo(DEFAULT_DEFAULT_ALBUM);
        assertThat(testContactPrivileges.isCritiqueIt()).isEqualTo(DEFAULT_CRITIQUE_IT);
        assertThat(testContactPrivileges.isAdhocLink()).isEqualTo(DEFAULT_ADHOC_LINK);
        assertThat(testContactPrivileges.isRetouch()).isEqualTo(DEFAULT_RETOUCH);
        assertThat(testContactPrivileges.isFileUpload()).isEqualTo(DEFAULT_FILE_UPLOAD);
        assertThat(testContactPrivileges.isDeleteAssets()).isEqualTo(DEFAULT_DELETE_ASSETS);

        // Validate the ContactPrivileges in ElasticSearch
        ContactPrivileges contactPrivilegesEs = contactPrivilegesSearchRepository.findOne(testContactPrivileges.getId());
        assertThat(contactPrivilegesEs).isEqualToComparingFieldByField(testContactPrivileges);
    }

    @Test
    @Transactional
    public void getAllContactPrivileges() throws Exception {
        // Initialize the database
        contactPrivilegesRepository.saveAndFlush(contactPrivileges);

        // Get all the contactPrivileges
        restContactPrivilegesMockMvc.perform(get("/api/contact-privileges?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(contactPrivileges.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR.toString())))
                .andExpect(jsonPath("$.[*].exec").value(hasItem(DEFAULT_EXEC.booleanValue())))
                .andExpect(jsonPath("$.[*].captioning").value(hasItem(DEFAULT_CAPTIONING.booleanValue())))
                .andExpect(jsonPath("$.[*].downloadType").value(hasItem(DEFAULT_DOWNLOAD_TYPE)))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.booleanValue())))
                .andExpect(jsonPath("$.[*].print").value(hasItem(DEFAULT_PRINT.booleanValue())))
                .andExpect(jsonPath("$.[*].lockApproveRestriction").value(hasItem(DEFAULT_LOCK_APPROVE_RESTRICTION.booleanValue())))
                .andExpect(jsonPath("$.[*].priorityPix").value(hasItem(DEFAULT_PRIORITY_PIX.booleanValue())))
                .andExpect(jsonPath("$.[*].releaseExclude").value(hasItem(DEFAULT_RELEASE_EXCLUDE.booleanValue())))
                .andExpect(jsonPath("$.[*].viewSensitive").value(hasItem(DEFAULT_VIEW_SENSITIVE.booleanValue())))
                .andExpect(jsonPath("$.[*].watermark").value(hasItem(DEFAULT_WATERMARK.booleanValue())))
                .andExpect(jsonPath("$.[*].watermarkInnerTransparency").value(hasItem(DEFAULT_WATERMARK_INNER_TRANSPARENCY.doubleValue())))
                .andExpect(jsonPath("$.[*].watermarkOuterTransparency").value(hasItem(DEFAULT_WATERMARK_OUTER_TRANSPARENCY.doubleValue())))
                .andExpect(jsonPath("$.[*].internal").value(hasItem(DEFAULT_INTERNAL.booleanValue())))
                .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR.booleanValue())))
                .andExpect(jsonPath("$.[*].restartRole").value(hasItem(DEFAULT_RESTART_ROLE.toString())))
                .andExpect(jsonPath("$.[*].restartImage").value(hasItem(DEFAULT_RESTART_IMAGE.toString())))
                .andExpect(jsonPath("$.[*].restartPage").value(hasItem(DEFAULT_RESTART_PAGE.intValue())))
                .andExpect(jsonPath("$.[*].lastLoginDt").value(hasItem(DEFAULT_LAST_LOGIN_DT_STR)))
                .andExpect(jsonPath("$.[*].lastLogoutDt").value(hasItem(DEFAULT_LAST_LOGOUT_DT_STR)))
                .andExpect(jsonPath("$.[*].disabled").value(hasItem(DEFAULT_DISABLED.booleanValue())))
                .andExpect(jsonPath("$.[*].welcomeMessage").value(hasItem(DEFAULT_WELCOME_MESSAGE.booleanValue())))
                .andExpect(jsonPath("$.[*].isABCViewer").value(hasItem(DEFAULT_IS_ABC_VIEWER.booleanValue())))
                .andExpect(jsonPath("$.[*].talentManagement").value(hasItem(DEFAULT_TALENT_MANAGEMENT.booleanValue())))
                .andExpect(jsonPath("$.[*].signoffManagement").value(hasItem(DEFAULT_SIGNOFF_MANAGEMENT.booleanValue())))
                .andExpect(jsonPath("$.[*].datgeditManagement").value(hasItem(DEFAULT_DATGEDIT_MANAGEMENT.booleanValue())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].expireDate").value(hasItem(DEFAULT_EXPIRE_DATE_STR)))
                .andExpect(jsonPath("$.[*].restartFilter").value(hasItem(DEFAULT_RESTART_FILTER.toString())))
                .andExpect(jsonPath("$.[*].restartColumns").value(hasItem(DEFAULT_RESTART_COLUMNS)))
                .andExpect(jsonPath("$.[*].restartImagesPerPage").value(hasItem(DEFAULT_RESTART_IMAGES_PER_PAGE)))
                .andExpect(jsonPath("$.[*].restartImageSize").value(hasItem(DEFAULT_RESTART_IMAGE_SIZE.toString())))
                .andExpect(jsonPath("$.[*].restartTime").value(hasItem(DEFAULT_RESTART_TIME.intValue())))
                .andExpect(jsonPath("$.[*].showFinalizations").value(hasItem(DEFAULT_SHOW_FINALIZATIONS.booleanValue())))
                .andExpect(jsonPath("$.[*].readOnly").value(hasItem(DEFAULT_READ_ONLY.booleanValue())))
                .andExpect(jsonPath("$.[*].hasVideo").value(hasItem(DEFAULT_HAS_VIDEO.booleanValue())))
                .andExpect(jsonPath("$.[*].globalAlbum").value(hasItem(DEFAULT_GLOBAL_ALBUM.booleanValue())))
                .andExpect(jsonPath("$.[*].seesUntagged").value(hasItem(DEFAULT_SEES_UNTAGGED.booleanValue())))
                .andExpect(jsonPath("$.[*].loginCount").value(hasItem(DEFAULT_LOGIN_COUNT)))
                .andExpect(jsonPath("$.[*].exclusives").value(hasItem(DEFAULT_EXCLUSIVES)))
                .andExpect(jsonPath("$.[*].defaultAlbum").value(hasItem(DEFAULT_DEFAULT_ALBUM.intValue())))
                .andExpect(jsonPath("$.[*].critiqueIt").value(hasItem(DEFAULT_CRITIQUE_IT.booleanValue())))
                .andExpect(jsonPath("$.[*].adhocLink").value(hasItem(DEFAULT_ADHOC_LINK.booleanValue())))
                .andExpect(jsonPath("$.[*].retouch").value(hasItem(DEFAULT_RETOUCH.booleanValue())))
                .andExpect(jsonPath("$.[*].fileUpload").value(hasItem(DEFAULT_FILE_UPLOAD.booleanValue())))
                .andExpect(jsonPath("$.[*].deleteAssets").value(hasItem(DEFAULT_DELETE_ASSETS.booleanValue())));
    }

    @Test
    @Transactional
    public void getContactPrivileges() throws Exception {
        // Initialize the database
        contactPrivilegesRepository.saveAndFlush(contactPrivileges);

        // Get the contactPrivileges
        restContactPrivilegesMockMvc.perform(get("/api/contact-privileges/{id}", contactPrivileges.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(contactPrivileges.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR.toString()))
            .andExpect(jsonPath("$.exec").value(DEFAULT_EXEC.booleanValue()))
            .andExpect(jsonPath("$.captioning").value(DEFAULT_CAPTIONING.booleanValue()))
            .andExpect(jsonPath("$.downloadType").value(DEFAULT_DOWNLOAD_TYPE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.booleanValue()))
            .andExpect(jsonPath("$.print").value(DEFAULT_PRINT.booleanValue()))
            .andExpect(jsonPath("$.lockApproveRestriction").value(DEFAULT_LOCK_APPROVE_RESTRICTION.booleanValue()))
            .andExpect(jsonPath("$.priorityPix").value(DEFAULT_PRIORITY_PIX.booleanValue()))
            .andExpect(jsonPath("$.releaseExclude").value(DEFAULT_RELEASE_EXCLUDE.booleanValue()))
            .andExpect(jsonPath("$.viewSensitive").value(DEFAULT_VIEW_SENSITIVE.booleanValue()))
            .andExpect(jsonPath("$.watermark").value(DEFAULT_WATERMARK.booleanValue()))
            .andExpect(jsonPath("$.watermarkInnerTransparency").value(DEFAULT_WATERMARK_INNER_TRANSPARENCY.doubleValue()))
            .andExpect(jsonPath("$.watermarkOuterTransparency").value(DEFAULT_WATERMARK_OUTER_TRANSPARENCY.doubleValue()))
            .andExpect(jsonPath("$.internal").value(DEFAULT_INTERNAL.booleanValue()))
            .andExpect(jsonPath("$.vendor").value(DEFAULT_VENDOR.booleanValue()))
            .andExpect(jsonPath("$.restartRole").value(DEFAULT_RESTART_ROLE.toString()))
            .andExpect(jsonPath("$.restartImage").value(DEFAULT_RESTART_IMAGE.toString()))
            .andExpect(jsonPath("$.restartPage").value(DEFAULT_RESTART_PAGE.intValue()))
            .andExpect(jsonPath("$.lastLoginDt").value(DEFAULT_LAST_LOGIN_DT_STR))
            .andExpect(jsonPath("$.lastLogoutDt").value(DEFAULT_LAST_LOGOUT_DT_STR))
            .andExpect(jsonPath("$.disabled").value(DEFAULT_DISABLED.booleanValue()))
            .andExpect(jsonPath("$.welcomeMessage").value(DEFAULT_WELCOME_MESSAGE.booleanValue()))
            .andExpect(jsonPath("$.isABCViewer").value(DEFAULT_IS_ABC_VIEWER.booleanValue()))
            .andExpect(jsonPath("$.talentManagement").value(DEFAULT_TALENT_MANAGEMENT.booleanValue()))
            .andExpect(jsonPath("$.signoffManagement").value(DEFAULT_SIGNOFF_MANAGEMENT.booleanValue()))
            .andExpect(jsonPath("$.datgeditManagement").value(DEFAULT_DATGEDIT_MANAGEMENT.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE_STR))
            .andExpect(jsonPath("$.expireDate").value(DEFAULT_EXPIRE_DATE_STR))
            .andExpect(jsonPath("$.restartFilter").value(DEFAULT_RESTART_FILTER.toString()))
            .andExpect(jsonPath("$.restartColumns").value(DEFAULT_RESTART_COLUMNS))
            .andExpect(jsonPath("$.restartImagesPerPage").value(DEFAULT_RESTART_IMAGES_PER_PAGE))
            .andExpect(jsonPath("$.restartImageSize").value(DEFAULT_RESTART_IMAGE_SIZE.toString()))
            .andExpect(jsonPath("$.restartTime").value(DEFAULT_RESTART_TIME.intValue()))
            .andExpect(jsonPath("$.showFinalizations").value(DEFAULT_SHOW_FINALIZATIONS.booleanValue()))
            .andExpect(jsonPath("$.readOnly").value(DEFAULT_READ_ONLY.booleanValue()))
            .andExpect(jsonPath("$.hasVideo").value(DEFAULT_HAS_VIDEO.booleanValue()))
            .andExpect(jsonPath("$.globalAlbum").value(DEFAULT_GLOBAL_ALBUM.booleanValue()))
            .andExpect(jsonPath("$.seesUntagged").value(DEFAULT_SEES_UNTAGGED.booleanValue()))
            .andExpect(jsonPath("$.loginCount").value(DEFAULT_LOGIN_COUNT))
            .andExpect(jsonPath("$.exclusives").value(DEFAULT_EXCLUSIVES))
            .andExpect(jsonPath("$.defaultAlbum").value(DEFAULT_DEFAULT_ALBUM.intValue()))
            .andExpect(jsonPath("$.critiqueIt").value(DEFAULT_CRITIQUE_IT.booleanValue()))
            .andExpect(jsonPath("$.adhocLink").value(DEFAULT_ADHOC_LINK.booleanValue()))
            .andExpect(jsonPath("$.retouch").value(DEFAULT_RETOUCH.booleanValue()))
            .andExpect(jsonPath("$.fileUpload").value(DEFAULT_FILE_UPLOAD.booleanValue()))
            .andExpect(jsonPath("$.deleteAssets").value(DEFAULT_DELETE_ASSETS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingContactPrivileges() throws Exception {
        // Get the contactPrivileges
        restContactPrivilegesMockMvc.perform(get("/api/contact-privileges/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContactPrivileges() throws Exception {
        // Initialize the database
        contactPrivilegesRepository.saveAndFlush(contactPrivileges);
        contactPrivilegesSearchRepository.save(contactPrivileges);
        int databaseSizeBeforeUpdate = contactPrivilegesRepository.findAll().size();

        // Update the contactPrivileges
        ContactPrivileges updatedContactPrivileges = new ContactPrivileges();
        updatedContactPrivileges.setId(contactPrivileges.getId());
        updatedContactPrivileges.setName(UPDATED_NAME);
        updatedContactPrivileges.setTitle(UPDATED_TITLE);
        updatedContactPrivileges.setDescription(UPDATED_DESCRIPTION);
        updatedContactPrivileges.setAuthor(UPDATED_AUTHOR);
        updatedContactPrivileges.setExec(UPDATED_EXEC);
        updatedContactPrivileges.setCaptioning(UPDATED_CAPTIONING);
        updatedContactPrivileges.setDownloadType(UPDATED_DOWNLOAD_TYPE);
        updatedContactPrivileges.setEmail(UPDATED_EMAIL);
        updatedContactPrivileges.setPrint(UPDATED_PRINT);
        updatedContactPrivileges.setLockApproveRestriction(UPDATED_LOCK_APPROVE_RESTRICTION);
        updatedContactPrivileges.setPriorityPix(UPDATED_PRIORITY_PIX);
        updatedContactPrivileges.setReleaseExclude(UPDATED_RELEASE_EXCLUDE);
        updatedContactPrivileges.setViewSensitive(UPDATED_VIEW_SENSITIVE);
        updatedContactPrivileges.setWatermark(UPDATED_WATERMARK);
        updatedContactPrivileges.setWatermarkInnerTransparency(UPDATED_WATERMARK_INNER_TRANSPARENCY);
        updatedContactPrivileges.setWatermarkOuterTransparency(UPDATED_WATERMARK_OUTER_TRANSPARENCY);
        updatedContactPrivileges.setInternal(UPDATED_INTERNAL);
        updatedContactPrivileges.setVendor(UPDATED_VENDOR);
        updatedContactPrivileges.setRestartRole(UPDATED_RESTART_ROLE);
        updatedContactPrivileges.setRestartImage(UPDATED_RESTART_IMAGE);
        updatedContactPrivileges.setRestartPage(UPDATED_RESTART_PAGE);
        updatedContactPrivileges.setLastLoginDt(UPDATED_LAST_LOGIN_DT);
        updatedContactPrivileges.setLastLogoutDt(UPDATED_LAST_LOGOUT_DT);
        updatedContactPrivileges.setDisabled(UPDATED_DISABLED);
        updatedContactPrivileges.setWelcomeMessage(UPDATED_WELCOME_MESSAGE);
        updatedContactPrivileges.setIsABCViewer(UPDATED_IS_ABC_VIEWER);
        updatedContactPrivileges.setTalentManagement(UPDATED_TALENT_MANAGEMENT);
        updatedContactPrivileges.setSignoffManagement(UPDATED_SIGNOFF_MANAGEMENT);
        updatedContactPrivileges.setDatgeditManagement(UPDATED_DATGEDIT_MANAGEMENT);
        updatedContactPrivileges.setCreatedDate(UPDATED_CREATED_DATE);
        updatedContactPrivileges.setUpdatedDate(UPDATED_UPDATED_DATE);
        updatedContactPrivileges.setExpireDate(UPDATED_EXPIRE_DATE);
        updatedContactPrivileges.setRestartFilter(UPDATED_RESTART_FILTER);
        updatedContactPrivileges.setRestartColumns(UPDATED_RESTART_COLUMNS);
        updatedContactPrivileges.setRestartImagesPerPage(UPDATED_RESTART_IMAGES_PER_PAGE);
        updatedContactPrivileges.setRestartImageSize(UPDATED_RESTART_IMAGE_SIZE);
        updatedContactPrivileges.setRestartTime(UPDATED_RESTART_TIME);
        updatedContactPrivileges.setShowFinalizations(UPDATED_SHOW_FINALIZATIONS);
        updatedContactPrivileges.setReadOnly(UPDATED_READ_ONLY);
        updatedContactPrivileges.setHasVideo(UPDATED_HAS_VIDEO);
        updatedContactPrivileges.setGlobalAlbum(UPDATED_GLOBAL_ALBUM);
        updatedContactPrivileges.setSeesUntagged(UPDATED_SEES_UNTAGGED);
        updatedContactPrivileges.setLoginCount(UPDATED_LOGIN_COUNT);
        updatedContactPrivileges.setExclusives(UPDATED_EXCLUSIVES);
        updatedContactPrivileges.setDefaultAlbum(UPDATED_DEFAULT_ALBUM);
        updatedContactPrivileges.setCritiqueIt(UPDATED_CRITIQUE_IT);
        updatedContactPrivileges.setAdhocLink(UPDATED_ADHOC_LINK);
        updatedContactPrivileges.setRetouch(UPDATED_RETOUCH);
        updatedContactPrivileges.setFileUpload(UPDATED_FILE_UPLOAD);
        updatedContactPrivileges.setDeleteAssets(UPDATED_DELETE_ASSETS);

        restContactPrivilegesMockMvc.perform(put("/api/contact-privileges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedContactPrivileges)))
                .andExpect(status().isOk());

        // Validate the ContactPrivileges in the database
        List<ContactPrivileges> contactPrivileges = contactPrivilegesRepository.findAll();
        assertThat(contactPrivileges).hasSize(databaseSizeBeforeUpdate);
        ContactPrivileges testContactPrivileges = contactPrivileges.get(contactPrivileges.size() - 1);
        assertThat(testContactPrivileges.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContactPrivileges.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContactPrivileges.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testContactPrivileges.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testContactPrivileges.isExec()).isEqualTo(UPDATED_EXEC);
        assertThat(testContactPrivileges.isCaptioning()).isEqualTo(UPDATED_CAPTIONING);
        assertThat(testContactPrivileges.getDownloadType()).isEqualTo(UPDATED_DOWNLOAD_TYPE);
        assertThat(testContactPrivileges.isEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContactPrivileges.isPrint()).isEqualTo(UPDATED_PRINT);
        assertThat(testContactPrivileges.isLockApproveRestriction()).isEqualTo(UPDATED_LOCK_APPROVE_RESTRICTION);
        assertThat(testContactPrivileges.isPriorityPix()).isEqualTo(UPDATED_PRIORITY_PIX);
        assertThat(testContactPrivileges.isReleaseExclude()).isEqualTo(UPDATED_RELEASE_EXCLUDE);
        assertThat(testContactPrivileges.isViewSensitive()).isEqualTo(UPDATED_VIEW_SENSITIVE);
        assertThat(testContactPrivileges.isWatermark()).isEqualTo(UPDATED_WATERMARK);
        assertThat(testContactPrivileges.getWatermarkInnerTransparency()).isEqualTo(UPDATED_WATERMARK_INNER_TRANSPARENCY);
        assertThat(testContactPrivileges.getWatermarkOuterTransparency()).isEqualTo(UPDATED_WATERMARK_OUTER_TRANSPARENCY);
        assertThat(testContactPrivileges.isInternal()).isEqualTo(UPDATED_INTERNAL);
        assertThat(testContactPrivileges.isVendor()).isEqualTo(UPDATED_VENDOR);
        assertThat(testContactPrivileges.getRestartRole()).isEqualTo(UPDATED_RESTART_ROLE);
        assertThat(testContactPrivileges.getRestartImage()).isEqualTo(UPDATED_RESTART_IMAGE);
        assertThat(testContactPrivileges.getRestartPage()).isEqualTo(UPDATED_RESTART_PAGE);
        assertThat(testContactPrivileges.getLastLoginDt()).isEqualTo(UPDATED_LAST_LOGIN_DT);
        assertThat(testContactPrivileges.getLastLogoutDt()).isEqualTo(UPDATED_LAST_LOGOUT_DT);
        assertThat(testContactPrivileges.isDisabled()).isEqualTo(UPDATED_DISABLED);
        assertThat(testContactPrivileges.isWelcomeMessage()).isEqualTo(UPDATED_WELCOME_MESSAGE);
        assertThat(testContactPrivileges.isIsABCViewer()).isEqualTo(UPDATED_IS_ABC_VIEWER);
        assertThat(testContactPrivileges.isTalentManagement()).isEqualTo(UPDATED_TALENT_MANAGEMENT);
        assertThat(testContactPrivileges.isSignoffManagement()).isEqualTo(UPDATED_SIGNOFF_MANAGEMENT);
        assertThat(testContactPrivileges.isDatgeditManagement()).isEqualTo(UPDATED_DATGEDIT_MANAGEMENT);
        assertThat(testContactPrivileges.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testContactPrivileges.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testContactPrivileges.getExpireDate()).isEqualTo(UPDATED_EXPIRE_DATE);
        assertThat(testContactPrivileges.getRestartFilter()).isEqualTo(UPDATED_RESTART_FILTER);
        assertThat(testContactPrivileges.getRestartColumns()).isEqualTo(UPDATED_RESTART_COLUMNS);
        assertThat(testContactPrivileges.getRestartImagesPerPage()).isEqualTo(UPDATED_RESTART_IMAGES_PER_PAGE);
        assertThat(testContactPrivileges.getRestartImageSize()).isEqualTo(UPDATED_RESTART_IMAGE_SIZE);
        assertThat(testContactPrivileges.getRestartTime()).isEqualTo(UPDATED_RESTART_TIME);
        assertThat(testContactPrivileges.isShowFinalizations()).isEqualTo(UPDATED_SHOW_FINALIZATIONS);
        assertThat(testContactPrivileges.isReadOnly()).isEqualTo(UPDATED_READ_ONLY);
        assertThat(testContactPrivileges.isHasVideo()).isEqualTo(UPDATED_HAS_VIDEO);
        assertThat(testContactPrivileges.isGlobalAlbum()).isEqualTo(UPDATED_GLOBAL_ALBUM);
        assertThat(testContactPrivileges.isSeesUntagged()).isEqualTo(UPDATED_SEES_UNTAGGED);
        assertThat(testContactPrivileges.getLoginCount()).isEqualTo(UPDATED_LOGIN_COUNT);
        assertThat(testContactPrivileges.getExclusives()).isEqualTo(UPDATED_EXCLUSIVES);
        assertThat(testContactPrivileges.getDefaultAlbum()).isEqualTo(UPDATED_DEFAULT_ALBUM);
        assertThat(testContactPrivileges.isCritiqueIt()).isEqualTo(UPDATED_CRITIQUE_IT);
        assertThat(testContactPrivileges.isAdhocLink()).isEqualTo(UPDATED_ADHOC_LINK);
        assertThat(testContactPrivileges.isRetouch()).isEqualTo(UPDATED_RETOUCH);
        assertThat(testContactPrivileges.isFileUpload()).isEqualTo(UPDATED_FILE_UPLOAD);
        assertThat(testContactPrivileges.isDeleteAssets()).isEqualTo(UPDATED_DELETE_ASSETS);

        // Validate the ContactPrivileges in ElasticSearch
        ContactPrivileges contactPrivilegesEs = contactPrivilegesSearchRepository.findOne(testContactPrivileges.getId());
        assertThat(contactPrivilegesEs).isEqualToComparingFieldByField(testContactPrivileges);
    }

    @Test
    @Transactional
    public void deleteContactPrivileges() throws Exception {
        // Initialize the database
        contactPrivilegesRepository.saveAndFlush(contactPrivileges);
        contactPrivilegesSearchRepository.save(contactPrivileges);
        int databaseSizeBeforeDelete = contactPrivilegesRepository.findAll().size();

        // Get the contactPrivileges
        restContactPrivilegesMockMvc.perform(delete("/api/contact-privileges/{id}", contactPrivileges.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean contactPrivilegesExistsInEs = contactPrivilegesSearchRepository.exists(contactPrivileges.getId());
        assertThat(contactPrivilegesExistsInEs).isFalse();

        // Validate the database is empty
        List<ContactPrivileges> contactPrivileges = contactPrivilegesRepository.findAll();
        assertThat(contactPrivileges).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchContactPrivileges() throws Exception {
        // Initialize the database
        contactPrivilegesRepository.saveAndFlush(contactPrivileges);
        contactPrivilegesSearchRepository.save(contactPrivileges);

        // Search the contactPrivileges
        restContactPrivilegesMockMvc.perform(get("/api/_search/contact-privileges?query=id:" + contactPrivileges.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactPrivileges.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR.toString())))
            .andExpect(jsonPath("$.[*].exec").value(hasItem(DEFAULT_EXEC.booleanValue())))
            .andExpect(jsonPath("$.[*].captioning").value(hasItem(DEFAULT_CAPTIONING.booleanValue())))
            .andExpect(jsonPath("$.[*].downloadType").value(hasItem(DEFAULT_DOWNLOAD_TYPE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.booleanValue())))
            .andExpect(jsonPath("$.[*].print").value(hasItem(DEFAULT_PRINT.booleanValue())))
            .andExpect(jsonPath("$.[*].lockApproveRestriction").value(hasItem(DEFAULT_LOCK_APPROVE_RESTRICTION.booleanValue())))
            .andExpect(jsonPath("$.[*].priorityPix").value(hasItem(DEFAULT_PRIORITY_PIX.booleanValue())))
            .andExpect(jsonPath("$.[*].releaseExclude").value(hasItem(DEFAULT_RELEASE_EXCLUDE.booleanValue())))
            .andExpect(jsonPath("$.[*].viewSensitive").value(hasItem(DEFAULT_VIEW_SENSITIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].watermark").value(hasItem(DEFAULT_WATERMARK.booleanValue())))
            .andExpect(jsonPath("$.[*].watermarkInnerTransparency").value(hasItem(DEFAULT_WATERMARK_INNER_TRANSPARENCY.doubleValue())))
            .andExpect(jsonPath("$.[*].watermarkOuterTransparency").value(hasItem(DEFAULT_WATERMARK_OUTER_TRANSPARENCY.doubleValue())))
            .andExpect(jsonPath("$.[*].internal").value(hasItem(DEFAULT_INTERNAL.booleanValue())))
            .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR.booleanValue())))
            .andExpect(jsonPath("$.[*].restartRole").value(hasItem(DEFAULT_RESTART_ROLE.toString())))
            .andExpect(jsonPath("$.[*].restartImage").value(hasItem(DEFAULT_RESTART_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].restartPage").value(hasItem(DEFAULT_RESTART_PAGE.intValue())))
            .andExpect(jsonPath("$.[*].lastLoginDt").value(hasItem(DEFAULT_LAST_LOGIN_DT_STR)))
            .andExpect(jsonPath("$.[*].lastLogoutDt").value(hasItem(DEFAULT_LAST_LOGOUT_DT_STR)))
            .andExpect(jsonPath("$.[*].disabled").value(hasItem(DEFAULT_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].welcomeMessage").value(hasItem(DEFAULT_WELCOME_MESSAGE.booleanValue())))
            .andExpect(jsonPath("$.[*].isABCViewer").value(hasItem(DEFAULT_IS_ABC_VIEWER.booleanValue())))
            .andExpect(jsonPath("$.[*].talentManagement").value(hasItem(DEFAULT_TALENT_MANAGEMENT.booleanValue())))
            .andExpect(jsonPath("$.[*].signoffManagement").value(hasItem(DEFAULT_SIGNOFF_MANAGEMENT.booleanValue())))
            .andExpect(jsonPath("$.[*].datgeditManagement").value(hasItem(DEFAULT_DATGEDIT_MANAGEMENT.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE_STR)))
            .andExpect(jsonPath("$.[*].expireDate").value(hasItem(DEFAULT_EXPIRE_DATE_STR)))
            .andExpect(jsonPath("$.[*].restartFilter").value(hasItem(DEFAULT_RESTART_FILTER.toString())))
            .andExpect(jsonPath("$.[*].restartColumns").value(hasItem(DEFAULT_RESTART_COLUMNS)))
            .andExpect(jsonPath("$.[*].restartImagesPerPage").value(hasItem(DEFAULT_RESTART_IMAGES_PER_PAGE)))
            .andExpect(jsonPath("$.[*].restartImageSize").value(hasItem(DEFAULT_RESTART_IMAGE_SIZE.toString())))
            .andExpect(jsonPath("$.[*].restartTime").value(hasItem(DEFAULT_RESTART_TIME.intValue())))
            .andExpect(jsonPath("$.[*].showFinalizations").value(hasItem(DEFAULT_SHOW_FINALIZATIONS.booleanValue())))
            .andExpect(jsonPath("$.[*].readOnly").value(hasItem(DEFAULT_READ_ONLY.booleanValue())))
            .andExpect(jsonPath("$.[*].hasVideo").value(hasItem(DEFAULT_HAS_VIDEO.booleanValue())))
            .andExpect(jsonPath("$.[*].globalAlbum").value(hasItem(DEFAULT_GLOBAL_ALBUM.booleanValue())))
            .andExpect(jsonPath("$.[*].seesUntagged").value(hasItem(DEFAULT_SEES_UNTAGGED.booleanValue())))
            .andExpect(jsonPath("$.[*].loginCount").value(hasItem(DEFAULT_LOGIN_COUNT)))
            .andExpect(jsonPath("$.[*].exclusives").value(hasItem(DEFAULT_EXCLUSIVES)))
            .andExpect(jsonPath("$.[*].defaultAlbum").value(hasItem(DEFAULT_DEFAULT_ALBUM.intValue())))
            .andExpect(jsonPath("$.[*].critiqueIt").value(hasItem(DEFAULT_CRITIQUE_IT.booleanValue())))
            .andExpect(jsonPath("$.[*].adhocLink").value(hasItem(DEFAULT_ADHOC_LINK.booleanValue())))
            .andExpect(jsonPath("$.[*].retouch").value(hasItem(DEFAULT_RETOUCH.booleanValue())))
            .andExpect(jsonPath("$.[*].fileUpload").value(hasItem(DEFAULT_FILE_UPLOAD.booleanValue())))
            .andExpect(jsonPath("$.[*].deleteAssets").value(hasItem(DEFAULT_DELETE_ASSETS.booleanValue())));
    }
}
