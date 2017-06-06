package com.fps.web.rest;

import com.fps.SmartLpcApp;
import com.fps.domain.Contacts;
import com.fps.repository.ContactsRepository;
import com.fps.service.ContactsService;
import com.fps.elastics.search.ContactsSearchRepository;

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
 * Test class for the ContactsResource REST controller.
 *
 * @see ContactsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartLpcApp.class)
@WebAppConfiguration
@IntegrationTest
public class ContactsResourceIntTest {

    private static final String DEFAULT_USERNAME = "AAAAA";
    private static final String UPDATED_USERNAME = "BBBBB";
    private static final String DEFAULT_PASSWORD = "AAAAA";
    private static final String UPDATED_PASSWORD = "BBBBB";
    private static final String DEFAULT_FULL_NAME = "AAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBB";
    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_EMAIL_2 = "AAAAA";
    private static final String UPDATED_EMAIL_2 = "BBBBB";
    private static final String DEFAULT_PHONE_OFFICE = "AAAAA";
    private static final String UPDATED_PHONE_OFFICE = "BBBBB";
    private static final String DEFAULT_PHONE_ALTERNATE = "AAAAA";
    private static final String UPDATED_PHONE_ALTERNATE = "BBBBB";
    private static final String DEFAULT_PHONE_MOBILE = "AAAAA";
    private static final String UPDATED_PHONE_MOBILE = "BBBBB";
    private static final String DEFAULT_PHONE_FAX = "AAAAA";
    private static final String UPDATED_PHONE_FAX = "BBBBB";
    private static final String DEFAULT_STREET_ADDRESS = "AAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBB";
    private static final String DEFAULT_STREET_ADDRESS_2 = "AAAAA";
    private static final String UPDATED_STREET_ADDRESS_2 = "BBBBB";
    private static final String DEFAULT_STREET_ADDRESS_3 = "AAAAA";
    private static final String UPDATED_STREET_ADDRESS_3 = "BBBBB";
    private static final String DEFAULT_CITY = "AAAAA";
    private static final String UPDATED_CITY = "BBBBB";
    private static final String DEFAULT_STATE = "AAAAA";
    private static final String UPDATED_STATE = "BBBBB";
    private static final String DEFAULT_ZIPCODE = "AAAAA";
    private static final String UPDATED_ZIPCODE = "BBBBB";
    private static final String DEFAULT_COUNTRY = "AAAAA";
    private static final String UPDATED_COUNTRY = "BBBBB";
    private static final String DEFAULT_WEBSITE = "AAAAA";
    private static final String UPDATED_WEBSITE = "BBBBB";
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";
    private static final String DEFAULT_SOURCE_ID = "AAAAA";
    private static final String UPDATED_SOURCE_ID = "BBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_GLOBAL_RESTART_COLUMNS = 1;
    private static final Integer UPDATED_GLOBAL_RESTART_COLUMNS = 2;

    private static final Integer DEFAULT_GLOBAL_RESTART_IMAGES_PER_PAGE = 1;
    private static final Integer UPDATED_GLOBAL_RESTART_IMAGES_PER_PAGE = 2;
    private static final String DEFAULT_GLOBAL_RESTART_IMAGE_SIZE = "AAAAA";
    private static final String UPDATED_GLOBAL_RESTART_IMAGE_SIZE = "BBBBB";

    private static final Long DEFAULT_GLOBAL_RESTART_TIME = 1L;
    private static final Long UPDATED_GLOBAL_RESTART_TIME = 2L;

    private static final Boolean DEFAULT_DASHBOARD = false;
    private static final Boolean UPDATED_DASHBOARD = true;

    private static final Boolean DEFAULT_INTERNAL_ACCESS_ONLY = false;
    private static final Boolean UPDATED_INTERNAL_ACCESS_ONLY = true;

    private static final Integer DEFAULT_ADHOC_EXPIRES_IN = 1;
    private static final Integer UPDATED_ADHOC_EXPIRES_IN = 2;

    private static final Integer DEFAULT_ADHOC_LIMIT_VIEWS = 1;
    private static final Integer UPDATED_ADHOC_LIMIT_VIEWS = 2;

    private static final Integer DEFAULT_ADHOC_DOWNLOAD = 1;
    private static final Integer UPDATED_ADHOC_DOWNLOAD = 2;
    private static final String DEFAULT_ADHOC_WATERMARK_TEXT = "AAAAA";
    private static final String UPDATED_ADHOC_WATERMARK_TEXT = "BBBBB";
    private static final String DEFAULT_LOGIN_IP = "AAAAA";
    private static final String UPDATED_LOGIN_IP = "BBBBB";

    private static final Integer DEFAULT_LOGIN_ATTEMPT = 1;
    private static final Integer UPDATED_LOGIN_ATTEMPT = 2;

    private static final Boolean DEFAULT_ATTEMPT_BASED_LOGIN = false;
    private static final Boolean UPDATED_ATTEMPT_BASED_LOGIN = true;

    private static final Boolean DEFAULT_IP_BASED_LOGIN = false;
    private static final Boolean UPDATED_IP_BASED_LOGIN = true;

    @Inject
    private ContactsRepository contactsRepository;

    @Inject
    private ContactsService contactsService;

    @Inject
    private ContactsSearchRepository contactsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restContactsMockMvc;

    private Contacts contacts;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContactsResource contactsResource = new ContactsResource();
        ReflectionTestUtils.setField(contactsResource, "contactsService", contactsService);
        this.restContactsMockMvc = MockMvcBuilders.standaloneSetup(contactsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        contactsSearchRepository.deleteAll();
        contacts = new Contacts();
        contacts.setUsername(DEFAULT_USERNAME);
        contacts.setPassword(DEFAULT_PASSWORD);
        contacts.setFullName(DEFAULT_FULL_NAME);
        contacts.setTitle(DEFAULT_TITLE);
        contacts.setEmail(DEFAULT_EMAIL);
        contacts.setEmail2(DEFAULT_EMAIL_2);
        contacts.setPhoneOffice(DEFAULT_PHONE_OFFICE);
        contacts.setPhoneAlternate(DEFAULT_PHONE_ALTERNATE);
        contacts.setPhoneMobile(DEFAULT_PHONE_MOBILE);
        contacts.setPhoneFax(DEFAULT_PHONE_FAX);
        contacts.setStreetAddress(DEFAULT_STREET_ADDRESS);
        contacts.setStreetAddress2(DEFAULT_STREET_ADDRESS_2);
        contacts.setStreetAddress3(DEFAULT_STREET_ADDRESS_3);
        contacts.setCity(DEFAULT_CITY);
        contacts.setState(DEFAULT_STATE);
        contacts.setZipcode(DEFAULT_ZIPCODE);
        contacts.setCountry(DEFAULT_COUNTRY);
        contacts.setWebsite(DEFAULT_WEBSITE);
        contacts.setNotes(DEFAULT_NOTES);
        contacts.setSourceId(DEFAULT_SOURCE_ID);
        contacts.setCreatedDate(DEFAULT_CREATED_DATE);
        contacts.setUpdatedDate(DEFAULT_UPDATED_DATE);
        contacts.setGlobalRestartColumns(DEFAULT_GLOBAL_RESTART_COLUMNS);
        contacts.setGlobalRestartImagesPerPage(DEFAULT_GLOBAL_RESTART_IMAGES_PER_PAGE);
        contacts.setGlobalRestartImageSize(DEFAULT_GLOBAL_RESTART_IMAGE_SIZE);
        contacts.setGlobalRestartTime(DEFAULT_GLOBAL_RESTART_TIME);
        contacts.setDashboard(DEFAULT_DASHBOARD);
        contacts.setInternalAccessOnly(DEFAULT_INTERNAL_ACCESS_ONLY);
        contacts.setAdhocExpiresIn(DEFAULT_ADHOC_EXPIRES_IN);
        contacts.setAdhocLimitViews(DEFAULT_ADHOC_LIMIT_VIEWS);
        contacts.setAdhocDownload(DEFAULT_ADHOC_DOWNLOAD);
        contacts.setAdhocWatermarkText(DEFAULT_ADHOC_WATERMARK_TEXT);
        contacts.setLoginIp(DEFAULT_LOGIN_IP);
        contacts.setLoginAttempt(DEFAULT_LOGIN_ATTEMPT);
        contacts.setAttemptBasedLogin(DEFAULT_ATTEMPT_BASED_LOGIN);
        contacts.setIpBasedLogin(DEFAULT_IP_BASED_LOGIN);
    }

    @Test
    @Transactional
    public void createContacts() throws Exception {
        int databaseSizeBeforeCreate = contactsRepository.findAll().size();

        // Create the Contacts

        restContactsMockMvc.perform(post("/api/contacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contacts)))
                .andExpect(status().isCreated());

        // Validate the Contacts in the database
        List<Contacts> contacts = contactsRepository.findAll();
        assertThat(contacts).hasSize(databaseSizeBeforeCreate + 1);
        Contacts testContacts = contacts.get(contacts.size() - 1);
        assertThat(testContacts.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testContacts.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testContacts.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testContacts.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testContacts.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testContacts.getEmail2()).isEqualTo(DEFAULT_EMAIL_2);
        assertThat(testContacts.getPhoneOffice()).isEqualTo(DEFAULT_PHONE_OFFICE);
        assertThat(testContacts.getPhoneAlternate()).isEqualTo(DEFAULT_PHONE_ALTERNATE);
        assertThat(testContacts.getPhoneMobile()).isEqualTo(DEFAULT_PHONE_MOBILE);
        assertThat(testContacts.getPhoneFax()).isEqualTo(DEFAULT_PHONE_FAX);
        assertThat(testContacts.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testContacts.getStreetAddress2()).isEqualTo(DEFAULT_STREET_ADDRESS_2);
        assertThat(testContacts.getStreetAddress3()).isEqualTo(DEFAULT_STREET_ADDRESS_3);
        assertThat(testContacts.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testContacts.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testContacts.getZipcode()).isEqualTo(DEFAULT_ZIPCODE);
        assertThat(testContacts.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testContacts.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testContacts.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testContacts.getSourceId()).isEqualTo(DEFAULT_SOURCE_ID);
        assertThat(testContacts.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testContacts.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testContacts.getGlobalRestartColumns()).isEqualTo(DEFAULT_GLOBAL_RESTART_COLUMNS);
        assertThat(testContacts.getGlobalRestartImagesPerPage()).isEqualTo(DEFAULT_GLOBAL_RESTART_IMAGES_PER_PAGE);
        assertThat(testContacts.getGlobalRestartImageSize()).isEqualTo(DEFAULT_GLOBAL_RESTART_IMAGE_SIZE);
        assertThat(testContacts.getGlobalRestartTime()).isEqualTo(DEFAULT_GLOBAL_RESTART_TIME);
        assertThat(testContacts.isDashboard()).isEqualTo(DEFAULT_DASHBOARD);
        assertThat(testContacts.isInternalAccessOnly()).isEqualTo(DEFAULT_INTERNAL_ACCESS_ONLY);
        assertThat(testContacts.getAdhocExpiresIn()).isEqualTo(DEFAULT_ADHOC_EXPIRES_IN);
        assertThat(testContacts.getAdhocLimitViews()).isEqualTo(DEFAULT_ADHOC_LIMIT_VIEWS);
        assertThat(testContacts.getAdhocDownload()).isEqualTo(DEFAULT_ADHOC_DOWNLOAD);
        assertThat(testContacts.getAdhocWatermarkText()).isEqualTo(DEFAULT_ADHOC_WATERMARK_TEXT);
        assertThat(testContacts.getLoginIp()).isEqualTo(DEFAULT_LOGIN_IP);
        assertThat(testContacts.getLoginAttempt()).isEqualTo(DEFAULT_LOGIN_ATTEMPT);
        assertThat(testContacts.isAttemptBasedLogin()).isEqualTo(DEFAULT_ATTEMPT_BASED_LOGIN);
        assertThat(testContacts.isIpBasedLogin()).isEqualTo(DEFAULT_IP_BASED_LOGIN);

        // Validate the Contacts in ElasticSearch
        Contacts contactsEs = contactsSearchRepository.findOne(testContacts.getId());
        assertThat(contactsEs).isEqualToComparingFieldByField(testContacts);
    }

    @Test
    @Transactional
    public void getAllContacts() throws Exception {
        // Initialize the database
        contactsRepository.saveAndFlush(contacts);

        // Get all the contacts
        restContactsMockMvc.perform(get("/api/contacts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(contacts.getId().intValue())))
                .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
                .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
                .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].email2").value(hasItem(DEFAULT_EMAIL_2.toString())))
                .andExpect(jsonPath("$.[*].phoneOffice").value(hasItem(DEFAULT_PHONE_OFFICE.toString())))
                .andExpect(jsonPath("$.[*].phoneAlternate").value(hasItem(DEFAULT_PHONE_ALTERNATE.toString())))
                .andExpect(jsonPath("$.[*].phoneMobile").value(hasItem(DEFAULT_PHONE_MOBILE.toString())))
                .andExpect(jsonPath("$.[*].phoneFax").value(hasItem(DEFAULT_PHONE_FAX.toString())))
                .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].streetAddress2").value(hasItem(DEFAULT_STREET_ADDRESS_2.toString())))
                .andExpect(jsonPath("$.[*].streetAddress3").value(hasItem(DEFAULT_STREET_ADDRESS_3.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
                .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE.toString())))
                .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
                .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
                .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
                .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())))
                .andExpect(jsonPath("$.[*].globalRestartColumns").value(hasItem(DEFAULT_GLOBAL_RESTART_COLUMNS)))
                .andExpect(jsonPath("$.[*].globalRestartImagesPerPage").value(hasItem(DEFAULT_GLOBAL_RESTART_IMAGES_PER_PAGE)))
                .andExpect(jsonPath("$.[*].globalRestartImageSize").value(hasItem(DEFAULT_GLOBAL_RESTART_IMAGE_SIZE.toString())))
                .andExpect(jsonPath("$.[*].globalRestartTime").value(hasItem(DEFAULT_GLOBAL_RESTART_TIME.intValue())))
                .andExpect(jsonPath("$.[*].dashboard").value(hasItem(DEFAULT_DASHBOARD.booleanValue())))
                .andExpect(jsonPath("$.[*].internalAccessOnly").value(hasItem(DEFAULT_INTERNAL_ACCESS_ONLY.booleanValue())))
                .andExpect(jsonPath("$.[*].adhocExpiresIn").value(hasItem(DEFAULT_ADHOC_EXPIRES_IN)))
                .andExpect(jsonPath("$.[*].adhocLimitViews").value(hasItem(DEFAULT_ADHOC_LIMIT_VIEWS)))
                .andExpect(jsonPath("$.[*].adhocDownload").value(hasItem(DEFAULT_ADHOC_DOWNLOAD)))
                .andExpect(jsonPath("$.[*].adhocWatermarkText").value(hasItem(DEFAULT_ADHOC_WATERMARK_TEXT.toString())))
                .andExpect(jsonPath("$.[*].loginIp").value(hasItem(DEFAULT_LOGIN_IP.toString())))
                .andExpect(jsonPath("$.[*].loginAttempt").value(hasItem(DEFAULT_LOGIN_ATTEMPT)))
                .andExpect(jsonPath("$.[*].attemptBasedLogin").value(hasItem(DEFAULT_ATTEMPT_BASED_LOGIN.booleanValue())))
                .andExpect(jsonPath("$.[*].ipBasedLogin").value(hasItem(DEFAULT_IP_BASED_LOGIN.booleanValue())));
    }

    @Test
    @Transactional
    public void getContacts() throws Exception {
        // Initialize the database
        contactsRepository.saveAndFlush(contacts);

        // Get the contacts
        restContactsMockMvc.perform(get("/api/contacts/{id}", contacts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(contacts.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.email2").value(DEFAULT_EMAIL_2.toString()))
            .andExpect(jsonPath("$.phoneOffice").value(DEFAULT_PHONE_OFFICE.toString()))
            .andExpect(jsonPath("$.phoneAlternate").value(DEFAULT_PHONE_ALTERNATE.toString()))
            .andExpect(jsonPath("$.phoneMobile").value(DEFAULT_PHONE_MOBILE.toString()))
            .andExpect(jsonPath("$.phoneFax").value(DEFAULT_PHONE_FAX.toString()))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS.toString()))
            .andExpect(jsonPath("$.streetAddress2").value(DEFAULT_STREET_ADDRESS_2.toString()))
            .andExpect(jsonPath("$.streetAddress3").value(DEFAULT_STREET_ADDRESS_3.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.zipcode").value(DEFAULT_ZIPCODE.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.sourceId").value(DEFAULT_SOURCE_ID.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE.toString()))
            .andExpect(jsonPath("$.globalRestartColumns").value(DEFAULT_GLOBAL_RESTART_COLUMNS))
            .andExpect(jsonPath("$.globalRestartImagesPerPage").value(DEFAULT_GLOBAL_RESTART_IMAGES_PER_PAGE))
            .andExpect(jsonPath("$.globalRestartImageSize").value(DEFAULT_GLOBAL_RESTART_IMAGE_SIZE.toString()))
            .andExpect(jsonPath("$.globalRestartTime").value(DEFAULT_GLOBAL_RESTART_TIME.intValue()))
            .andExpect(jsonPath("$.dashboard").value(DEFAULT_DASHBOARD.booleanValue()))
            .andExpect(jsonPath("$.internalAccessOnly").value(DEFAULT_INTERNAL_ACCESS_ONLY.booleanValue()))
            .andExpect(jsonPath("$.adhocExpiresIn").value(DEFAULT_ADHOC_EXPIRES_IN))
            .andExpect(jsonPath("$.adhocLimitViews").value(DEFAULT_ADHOC_LIMIT_VIEWS))
            .andExpect(jsonPath("$.adhocDownload").value(DEFAULT_ADHOC_DOWNLOAD))
            .andExpect(jsonPath("$.adhocWatermarkText").value(DEFAULT_ADHOC_WATERMARK_TEXT.toString()))
            .andExpect(jsonPath("$.loginIp").value(DEFAULT_LOGIN_IP.toString()))
            .andExpect(jsonPath("$.loginAttempt").value(DEFAULT_LOGIN_ATTEMPT))
            .andExpect(jsonPath("$.attemptBasedLogin").value(DEFAULT_ATTEMPT_BASED_LOGIN.booleanValue()))
            .andExpect(jsonPath("$.ipBasedLogin").value(DEFAULT_IP_BASED_LOGIN.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingContacts() throws Exception {
        // Get the contacts
        restContactsMockMvc.perform(get("/api/contacts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContacts() throws Exception {
        // Initialize the database
        contactsService.save(contacts);

        int databaseSizeBeforeUpdate = contactsRepository.findAll().size();

        // Update the contacts
        Contacts updatedContacts = new Contacts();
        updatedContacts.setId(contacts.getId());
        updatedContacts.setUsername(UPDATED_USERNAME);
        updatedContacts.setPassword(UPDATED_PASSWORD);
        updatedContacts.setFullName(UPDATED_FULL_NAME);
        updatedContacts.setTitle(UPDATED_TITLE);
        updatedContacts.setEmail(UPDATED_EMAIL);
        updatedContacts.setEmail2(UPDATED_EMAIL_2);
        updatedContacts.setPhoneOffice(UPDATED_PHONE_OFFICE);
        updatedContacts.setPhoneAlternate(UPDATED_PHONE_ALTERNATE);
        updatedContacts.setPhoneMobile(UPDATED_PHONE_MOBILE);
        updatedContacts.setPhoneFax(UPDATED_PHONE_FAX);
        updatedContacts.setStreetAddress(UPDATED_STREET_ADDRESS);
        updatedContacts.setStreetAddress2(UPDATED_STREET_ADDRESS_2);
        updatedContacts.setStreetAddress3(UPDATED_STREET_ADDRESS_3);
        updatedContacts.setCity(UPDATED_CITY);
        updatedContacts.setState(UPDATED_STATE);
        updatedContacts.setZipcode(UPDATED_ZIPCODE);
        updatedContacts.setCountry(UPDATED_COUNTRY);
        updatedContacts.setWebsite(UPDATED_WEBSITE);
        updatedContacts.setNotes(UPDATED_NOTES);
        updatedContacts.setSourceId(UPDATED_SOURCE_ID);
        updatedContacts.setCreatedDate(UPDATED_CREATED_DATE);
        updatedContacts.setUpdatedDate(UPDATED_UPDATED_DATE);
        updatedContacts.setGlobalRestartColumns(UPDATED_GLOBAL_RESTART_COLUMNS);
        updatedContacts.setGlobalRestartImagesPerPage(UPDATED_GLOBAL_RESTART_IMAGES_PER_PAGE);
        updatedContacts.setGlobalRestartImageSize(UPDATED_GLOBAL_RESTART_IMAGE_SIZE);
        updatedContacts.setGlobalRestartTime(UPDATED_GLOBAL_RESTART_TIME);
        updatedContacts.setDashboard(UPDATED_DASHBOARD);
        updatedContacts.setInternalAccessOnly(UPDATED_INTERNAL_ACCESS_ONLY);
        updatedContacts.setAdhocExpiresIn(UPDATED_ADHOC_EXPIRES_IN);
        updatedContacts.setAdhocLimitViews(UPDATED_ADHOC_LIMIT_VIEWS);
        updatedContacts.setAdhocDownload(UPDATED_ADHOC_DOWNLOAD);
        updatedContacts.setAdhocWatermarkText(UPDATED_ADHOC_WATERMARK_TEXT);
        updatedContacts.setLoginIp(UPDATED_LOGIN_IP);
        updatedContacts.setLoginAttempt(UPDATED_LOGIN_ATTEMPT);
        updatedContacts.setAttemptBasedLogin(UPDATED_ATTEMPT_BASED_LOGIN);
        updatedContacts.setIpBasedLogin(UPDATED_IP_BASED_LOGIN);

        restContactsMockMvc.perform(put("/api/contacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedContacts)))
                .andExpect(status().isOk());

        // Validate the Contacts in the database
        List<Contacts> contacts = contactsRepository.findAll();
        assertThat(contacts).hasSize(databaseSizeBeforeUpdate);
        Contacts testContacts = contacts.get(contacts.size() - 1);
        assertThat(testContacts.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testContacts.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testContacts.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testContacts.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContacts.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContacts.getEmail2()).isEqualTo(UPDATED_EMAIL_2);
        assertThat(testContacts.getPhoneOffice()).isEqualTo(UPDATED_PHONE_OFFICE);
        assertThat(testContacts.getPhoneAlternate()).isEqualTo(UPDATED_PHONE_ALTERNATE);
        assertThat(testContacts.getPhoneMobile()).isEqualTo(UPDATED_PHONE_MOBILE);
        assertThat(testContacts.getPhoneFax()).isEqualTo(UPDATED_PHONE_FAX);
        assertThat(testContacts.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testContacts.getStreetAddress2()).isEqualTo(UPDATED_STREET_ADDRESS_2);
        assertThat(testContacts.getStreetAddress3()).isEqualTo(UPDATED_STREET_ADDRESS_3);
        assertThat(testContacts.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testContacts.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testContacts.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
        assertThat(testContacts.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testContacts.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testContacts.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testContacts.getSourceId()).isEqualTo(UPDATED_SOURCE_ID);
        assertThat(testContacts.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testContacts.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testContacts.getGlobalRestartColumns()).isEqualTo(UPDATED_GLOBAL_RESTART_COLUMNS);
        assertThat(testContacts.getGlobalRestartImagesPerPage()).isEqualTo(UPDATED_GLOBAL_RESTART_IMAGES_PER_PAGE);
        assertThat(testContacts.getGlobalRestartImageSize()).isEqualTo(UPDATED_GLOBAL_RESTART_IMAGE_SIZE);
        assertThat(testContacts.getGlobalRestartTime()).isEqualTo(UPDATED_GLOBAL_RESTART_TIME);
        assertThat(testContacts.isDashboard()).isEqualTo(UPDATED_DASHBOARD);
        assertThat(testContacts.isInternalAccessOnly()).isEqualTo(UPDATED_INTERNAL_ACCESS_ONLY);
        assertThat(testContacts.getAdhocExpiresIn()).isEqualTo(UPDATED_ADHOC_EXPIRES_IN);
        assertThat(testContacts.getAdhocLimitViews()).isEqualTo(UPDATED_ADHOC_LIMIT_VIEWS);
        assertThat(testContacts.getAdhocDownload()).isEqualTo(UPDATED_ADHOC_DOWNLOAD);
        assertThat(testContacts.getAdhocWatermarkText()).isEqualTo(UPDATED_ADHOC_WATERMARK_TEXT);
        assertThat(testContacts.getLoginIp()).isEqualTo(UPDATED_LOGIN_IP);
        assertThat(testContacts.getLoginAttempt()).isEqualTo(UPDATED_LOGIN_ATTEMPT);
        assertThat(testContacts.isAttemptBasedLogin()).isEqualTo(UPDATED_ATTEMPT_BASED_LOGIN);
        assertThat(testContacts.isIpBasedLogin()).isEqualTo(UPDATED_IP_BASED_LOGIN);

        // Validate the Contacts in ElasticSearch
        Contacts contactsEs = contactsSearchRepository.findOne(testContacts.getId());
        assertThat(contactsEs).isEqualToComparingFieldByField(testContacts);
    }

    @Test
    @Transactional
    public void deleteContacts() throws Exception {
        // Initialize the database
        contactsService.save(contacts);

        int databaseSizeBeforeDelete = contactsRepository.findAll().size();

        // Get the contacts
        restContactsMockMvc.perform(delete("/api/contacts/{id}", contacts.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean contactsExistsInEs = contactsSearchRepository.exists(contacts.getId());
        assertThat(contactsExistsInEs).isFalse();

        // Validate the database is empty
        List<Contacts> contacts = contactsRepository.findAll();
        assertThat(contacts).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchContacts() throws Exception {
        // Initialize the database
        contactsService.save(contacts);

        // Search the contacts
        restContactsMockMvc.perform(get("/api/_search/contacts?query=id:" + contacts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contacts.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].email2").value(hasItem(DEFAULT_EMAIL_2.toString())))
            .andExpect(jsonPath("$.[*].phoneOffice").value(hasItem(DEFAULT_PHONE_OFFICE.toString())))
            .andExpect(jsonPath("$.[*].phoneAlternate").value(hasItem(DEFAULT_PHONE_ALTERNATE.toString())))
            .andExpect(jsonPath("$.[*].phoneMobile").value(hasItem(DEFAULT_PHONE_MOBILE.toString())))
            .andExpect(jsonPath("$.[*].phoneFax").value(hasItem(DEFAULT_PHONE_FAX.toString())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].streetAddress2").value(hasItem(DEFAULT_STREET_ADDRESS_2.toString())))
            .andExpect(jsonPath("$.[*].streetAddress3").value(hasItem(DEFAULT_STREET_ADDRESS_3.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].globalRestartColumns").value(hasItem(DEFAULT_GLOBAL_RESTART_COLUMNS)))
            .andExpect(jsonPath("$.[*].globalRestartImagesPerPage").value(hasItem(DEFAULT_GLOBAL_RESTART_IMAGES_PER_PAGE)))
            .andExpect(jsonPath("$.[*].globalRestartImageSize").value(hasItem(DEFAULT_GLOBAL_RESTART_IMAGE_SIZE.toString())))
            .andExpect(jsonPath("$.[*].globalRestartTime").value(hasItem(DEFAULT_GLOBAL_RESTART_TIME.intValue())))
            .andExpect(jsonPath("$.[*].dashboard").value(hasItem(DEFAULT_DASHBOARD.booleanValue())))
            .andExpect(jsonPath("$.[*].internalAccessOnly").value(hasItem(DEFAULT_INTERNAL_ACCESS_ONLY.booleanValue())))
            .andExpect(jsonPath("$.[*].adhocExpiresIn").value(hasItem(DEFAULT_ADHOC_EXPIRES_IN)))
            .andExpect(jsonPath("$.[*].adhocLimitViews").value(hasItem(DEFAULT_ADHOC_LIMIT_VIEWS)))
            .andExpect(jsonPath("$.[*].adhocDownload").value(hasItem(DEFAULT_ADHOC_DOWNLOAD)))
            .andExpect(jsonPath("$.[*].adhocWatermarkText").value(hasItem(DEFAULT_ADHOC_WATERMARK_TEXT.toString())))
            .andExpect(jsonPath("$.[*].loginIp").value(hasItem(DEFAULT_LOGIN_IP.toString())))
            .andExpect(jsonPath("$.[*].loginAttempt").value(hasItem(DEFAULT_LOGIN_ATTEMPT)))
            .andExpect(jsonPath("$.[*].attemptBasedLogin").value(hasItem(DEFAULT_ATTEMPT_BASED_LOGIN.booleanValue())))
            .andExpect(jsonPath("$.[*].ipBasedLogin").value(hasItem(DEFAULT_IP_BASED_LOGIN.booleanValue())));
    }
}
