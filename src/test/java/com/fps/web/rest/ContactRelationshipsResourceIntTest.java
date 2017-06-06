package com.fps.web.rest;

import com.fps.SmartLpcApp;
import com.fps.domain.ContactRelationships;
import com.fps.repository.ContactRelationshipsRepository;
import com.fps.elastics.search.ContactRelationshipsSearchRepository;

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
 * Test class for the ContactRelationshipsResource REST controller.
 *
 * @see ContactRelationshipsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartLpcApp.class)
@WebAppConfiguration
@IntegrationTest
public class ContactRelationshipsResourceIntTest {


    private static final Boolean DEFAULT_IS_PRIMARY_CONTACT = false;
    private static final Boolean UPDATED_IS_PRIMARY_CONTACT = true;

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private ContactRelationshipsRepository contactRelationshipsRepository;

    @Inject
    private ContactRelationshipsSearchRepository contactRelationshipsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restContactRelationshipsMockMvc;

    private ContactRelationships contactRelationships;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContactRelationshipsResource contactRelationshipsResource = new ContactRelationshipsResource();
        ReflectionTestUtils.setField(contactRelationshipsResource, "contactRelationshipsSearchRepository", contactRelationshipsSearchRepository);
        ReflectionTestUtils.setField(contactRelationshipsResource, "contactRelationshipsRepository", contactRelationshipsRepository);
        this.restContactRelationshipsMockMvc = MockMvcBuilders.standaloneSetup(contactRelationshipsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        contactRelationshipsSearchRepository.deleteAll();
        contactRelationships = new ContactRelationships();
        contactRelationships.setIsPrimaryContact(DEFAULT_IS_PRIMARY_CONTACT);
        contactRelationships.setCreatedDate(DEFAULT_CREATED_DATE);
        contactRelationships.setUpdatedDate(DEFAULT_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createContactRelationships() throws Exception {
        int databaseSizeBeforeCreate = contactRelationshipsRepository.findAll().size();

        // Create the ContactRelationships

        restContactRelationshipsMockMvc.perform(post("/api/contact-relationships")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contactRelationships)))
                .andExpect(status().isCreated());

        // Validate the ContactRelationships in the database
        List<ContactRelationships> contactRelationships = contactRelationshipsRepository.findAll();
        assertThat(contactRelationships).hasSize(databaseSizeBeforeCreate + 1);
        ContactRelationships testContactRelationships = contactRelationships.get(contactRelationships.size() - 1);
        assertThat(testContactRelationships.isIsPrimaryContact()).isEqualTo(DEFAULT_IS_PRIMARY_CONTACT);
        assertThat(testContactRelationships.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testContactRelationships.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);

        // Validate the ContactRelationships in ElasticSearch
        ContactRelationships contactRelationshipsEs = contactRelationshipsSearchRepository.findOne(testContactRelationships.getId());
        assertThat(contactRelationshipsEs).isEqualToComparingFieldByField(testContactRelationships);
    }

    @Test
    @Transactional
    public void getAllContactRelationships() throws Exception {
        // Initialize the database
        contactRelationshipsRepository.saveAndFlush(contactRelationships);

        // Get all the contactRelationships
        restContactRelationshipsMockMvc.perform(get("/api/contact-relationships?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(contactRelationships.getId().intValue())))
                .andExpect(jsonPath("$.[*].isPrimaryContact").value(hasItem(DEFAULT_IS_PRIMARY_CONTACT.booleanValue())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
                .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())));
    }

    @Test
    @Transactional
    public void getContactRelationships() throws Exception {
        // Initialize the database
        contactRelationshipsRepository.saveAndFlush(contactRelationships);

        // Get the contactRelationships
        restContactRelationshipsMockMvc.perform(get("/api/contact-relationships/{id}", contactRelationships.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(contactRelationships.getId().intValue()))
            .andExpect(jsonPath("$.isPrimaryContact").value(DEFAULT_IS_PRIMARY_CONTACT.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingContactRelationships() throws Exception {
        // Get the contactRelationships
        restContactRelationshipsMockMvc.perform(get("/api/contact-relationships/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContactRelationships() throws Exception {
        // Initialize the database
        contactRelationshipsRepository.saveAndFlush(contactRelationships);
        contactRelationshipsSearchRepository.save(contactRelationships);
        int databaseSizeBeforeUpdate = contactRelationshipsRepository.findAll().size();

        // Update the contactRelationships
        ContactRelationships updatedContactRelationships = new ContactRelationships();
        updatedContactRelationships.setId(contactRelationships.getId());
        updatedContactRelationships.setIsPrimaryContact(UPDATED_IS_PRIMARY_CONTACT);
        updatedContactRelationships.setCreatedDate(UPDATED_CREATED_DATE);
        updatedContactRelationships.setUpdatedDate(UPDATED_UPDATED_DATE);

        restContactRelationshipsMockMvc.perform(put("/api/contact-relationships")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedContactRelationships)))
                .andExpect(status().isOk());

        // Validate the ContactRelationships in the database
        List<ContactRelationships> contactRelationships = contactRelationshipsRepository.findAll();
        assertThat(contactRelationships).hasSize(databaseSizeBeforeUpdate);
        ContactRelationships testContactRelationships = contactRelationships.get(contactRelationships.size() - 1);
        assertThat(testContactRelationships.isIsPrimaryContact()).isEqualTo(UPDATED_IS_PRIMARY_CONTACT);
        assertThat(testContactRelationships.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testContactRelationships.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);

        // Validate the ContactRelationships in ElasticSearch
        ContactRelationships contactRelationshipsEs = contactRelationshipsSearchRepository.findOne(testContactRelationships.getId());
        assertThat(contactRelationshipsEs).isEqualToComparingFieldByField(testContactRelationships);
    }

    @Test
    @Transactional
    public void deleteContactRelationships() throws Exception {
        // Initialize the database
        contactRelationshipsRepository.saveAndFlush(contactRelationships);
        contactRelationshipsSearchRepository.save(contactRelationships);
        int databaseSizeBeforeDelete = contactRelationshipsRepository.findAll().size();

        // Get the contactRelationships
        restContactRelationshipsMockMvc.perform(delete("/api/contact-relationships/{id}", contactRelationships.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean contactRelationshipsExistsInEs = contactRelationshipsSearchRepository.exists(contactRelationships.getId());
        assertThat(contactRelationshipsExistsInEs).isFalse();

        // Validate the database is empty
        List<ContactRelationships> contactRelationships = contactRelationshipsRepository.findAll();
        assertThat(contactRelationships).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchContactRelationships() throws Exception {
        // Initialize the database
        contactRelationshipsRepository.saveAndFlush(contactRelationships);
        contactRelationshipsSearchRepository.save(contactRelationships);

        // Search the contactRelationships
        restContactRelationshipsMockMvc.perform(get("/api/_search/contact-relationships?query=id:" + contactRelationships.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactRelationships.getId().intValue())))
            .andExpect(jsonPath("$.[*].isPrimaryContact").value(hasItem(DEFAULT_IS_PRIMARY_CONTACT.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())));
    }
}
