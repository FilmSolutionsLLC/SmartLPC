package com.fps.web.rest;

import com.fps.SmartLpcApp;
import com.fps.domain.ProjectPurchaseOrders;
import com.fps.repository.ProjectPurchaseOrdersRepository;
import com.fps.elastics.search.ProjectPurchaseOrdersSearchRepository;

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
 * Test class for the ProjectPurchaseOrdersResource REST controller.
 *
 * @see ProjectPurchaseOrdersResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartLpcApp.class)
@WebAppConfiguration
@IntegrationTest
public class ProjectPurchaseOrdersResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_PO_NUMBER = "AAAAA";
    private static final String UPDATED_PO_NUMBER = "BBBBB";
    private static final String DEFAULT_PO_NOTES = "AAAAA";
    private static final String UPDATED_PO_NOTES = "BBBBB";

    private static final Integer DEFAULT_QB_RID = 1;
    private static final Integer UPDATED_QB_RID = 2;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATED_DATE);

    private static final ZonedDateTime DEFAULT_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_UPDATED_DATE_STR = dateTimeFormatter.format(DEFAULT_UPDATED_DATE);

    @Inject
    private ProjectPurchaseOrdersRepository projectPurchaseOrdersRepository;

    @Inject
    private ProjectPurchaseOrdersSearchRepository projectPurchaseOrdersSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProjectPurchaseOrdersMockMvc;

    private ProjectPurchaseOrders projectPurchaseOrders;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectPurchaseOrdersResource projectPurchaseOrdersResource = new ProjectPurchaseOrdersResource();
        ReflectionTestUtils.setField(projectPurchaseOrdersResource, "projectPurchaseOrdersSearchRepository", projectPurchaseOrdersSearchRepository);
        ReflectionTestUtils.setField(projectPurchaseOrdersResource, "projectPurchaseOrdersRepository", projectPurchaseOrdersRepository);
        this.restProjectPurchaseOrdersMockMvc = MockMvcBuilders.standaloneSetup(projectPurchaseOrdersResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        projectPurchaseOrdersSearchRepository.deleteAll();
        projectPurchaseOrders = new ProjectPurchaseOrders();
        projectPurchaseOrders.setPo_number(DEFAULT_PO_NUMBER);
        projectPurchaseOrders.setPo_notes(DEFAULT_PO_NOTES);
        projectPurchaseOrders.setQb_rid(DEFAULT_QB_RID);
        projectPurchaseOrders.setCreated_date(DEFAULT_CREATED_DATE);
        projectPurchaseOrders.setUpdated_date(DEFAULT_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createProjectPurchaseOrders() throws Exception {
        int databaseSizeBeforeCreate = projectPurchaseOrdersRepository.findAll().size();

        // Create the ProjectPurchaseOrders

        restProjectPurchaseOrdersMockMvc.perform(post("/api/project-purchase-orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectPurchaseOrders)))
                .andExpect(status().isCreated());

        // Validate the ProjectPurchaseOrders in the database
        List<ProjectPurchaseOrders> projectPurchaseOrders = projectPurchaseOrdersRepository.findAll();
        assertThat(projectPurchaseOrders).hasSize(databaseSizeBeforeCreate + 1);
        ProjectPurchaseOrders testProjectPurchaseOrders = projectPurchaseOrders.get(projectPurchaseOrders.size() - 1);
        assertThat(testProjectPurchaseOrders.getPo_number()).isEqualTo(DEFAULT_PO_NUMBER);
        assertThat(testProjectPurchaseOrders.getPo_notes()).isEqualTo(DEFAULT_PO_NOTES);
        assertThat(testProjectPurchaseOrders.getQb_rid()).isEqualTo(DEFAULT_QB_RID);
        assertThat(testProjectPurchaseOrders.getCreated_date()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProjectPurchaseOrders.getUpdated_date()).isEqualTo(DEFAULT_UPDATED_DATE);

        // Validate the ProjectPurchaseOrders in ElasticSearch
        ProjectPurchaseOrders projectPurchaseOrdersEs = projectPurchaseOrdersSearchRepository.findOne(testProjectPurchaseOrders.getId());
        assertThat(projectPurchaseOrdersEs).isEqualToComparingFieldByField(testProjectPurchaseOrders);
    }

    @Test
    @Transactional
    public void getAllProjectPurchaseOrders() throws Exception {
        // Initialize the database
        projectPurchaseOrdersRepository.saveAndFlush(projectPurchaseOrders);

        // Get all the projectPurchaseOrders
        restProjectPurchaseOrdersMockMvc.perform(get("/api/project-purchase-orders?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(projectPurchaseOrders.getId().intValue())))
                .andExpect(jsonPath("$.[*].po_number").value(hasItem(DEFAULT_PO_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].po_notes").value(hasItem(DEFAULT_PO_NOTES.toString())))
                .andExpect(jsonPath("$.[*].qb_rid").value(hasItem(DEFAULT_QB_RID)))
                .andExpect(jsonPath("$.[*].created_date").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].updated_date").value(hasItem(DEFAULT_UPDATED_DATE_STR)));
    }

    @Test
    @Transactional
    public void getProjectPurchaseOrders() throws Exception {
        // Initialize the database
        projectPurchaseOrdersRepository.saveAndFlush(projectPurchaseOrders);

        // Get the projectPurchaseOrders
        restProjectPurchaseOrdersMockMvc.perform(get("/api/project-purchase-orders/{id}", projectPurchaseOrders.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(projectPurchaseOrders.getId().intValue()))
            .andExpect(jsonPath("$.po_number").value(DEFAULT_PO_NUMBER.toString()))
            .andExpect(jsonPath("$.po_notes").value(DEFAULT_PO_NOTES.toString()))
            .andExpect(jsonPath("$.qb_rid").value(DEFAULT_QB_RID))
            .andExpect(jsonPath("$.created_date").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.updated_date").value(DEFAULT_UPDATED_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingProjectPurchaseOrders() throws Exception {
        // Get the projectPurchaseOrders
        restProjectPurchaseOrdersMockMvc.perform(get("/api/project-purchase-orders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectPurchaseOrders() throws Exception {
        // Initialize the database
        projectPurchaseOrdersRepository.saveAndFlush(projectPurchaseOrders);
        projectPurchaseOrdersSearchRepository.save(projectPurchaseOrders);
        int databaseSizeBeforeUpdate = projectPurchaseOrdersRepository.findAll().size();

        // Update the projectPurchaseOrders
        ProjectPurchaseOrders updatedProjectPurchaseOrders = new ProjectPurchaseOrders();
        updatedProjectPurchaseOrders.setId(projectPurchaseOrders.getId());
        updatedProjectPurchaseOrders.setPo_number(UPDATED_PO_NUMBER);
        updatedProjectPurchaseOrders.setPo_notes(UPDATED_PO_NOTES);
        updatedProjectPurchaseOrders.setQb_rid(UPDATED_QB_RID);
        updatedProjectPurchaseOrders.setCreated_date(UPDATED_CREATED_DATE);
        updatedProjectPurchaseOrders.setUpdated_date(UPDATED_UPDATED_DATE);

        restProjectPurchaseOrdersMockMvc.perform(put("/api/project-purchase-orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProjectPurchaseOrders)))
                .andExpect(status().isOk());

        // Validate the ProjectPurchaseOrders in the database
        List<ProjectPurchaseOrders> projectPurchaseOrders = projectPurchaseOrdersRepository.findAll();
        assertThat(projectPurchaseOrders).hasSize(databaseSizeBeforeUpdate);
        ProjectPurchaseOrders testProjectPurchaseOrders = projectPurchaseOrders.get(projectPurchaseOrders.size() - 1);
        assertThat(testProjectPurchaseOrders.getPo_number()).isEqualTo(UPDATED_PO_NUMBER);
        assertThat(testProjectPurchaseOrders.getPo_notes()).isEqualTo(UPDATED_PO_NOTES);
        assertThat(testProjectPurchaseOrders.getQb_rid()).isEqualTo(UPDATED_QB_RID);
        assertThat(testProjectPurchaseOrders.getCreated_date()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProjectPurchaseOrders.getUpdated_date()).isEqualTo(UPDATED_UPDATED_DATE);

        // Validate the ProjectPurchaseOrders in ElasticSearch
        ProjectPurchaseOrders projectPurchaseOrdersEs = projectPurchaseOrdersSearchRepository.findOne(testProjectPurchaseOrders.getId());
        assertThat(projectPurchaseOrdersEs).isEqualToComparingFieldByField(testProjectPurchaseOrders);
    }

    @Test
    @Transactional
    public void deleteProjectPurchaseOrders() throws Exception {
        // Initialize the database
        projectPurchaseOrdersRepository.saveAndFlush(projectPurchaseOrders);
        projectPurchaseOrdersSearchRepository.save(projectPurchaseOrders);
        int databaseSizeBeforeDelete = projectPurchaseOrdersRepository.findAll().size();

        // Get the projectPurchaseOrders
        restProjectPurchaseOrdersMockMvc.perform(delete("/api/project-purchase-orders/{id}", projectPurchaseOrders.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean projectPurchaseOrdersExistsInEs = projectPurchaseOrdersSearchRepository.exists(projectPurchaseOrders.getId());
        assertThat(projectPurchaseOrdersExistsInEs).isFalse();

        // Validate the database is empty
        List<ProjectPurchaseOrders> projectPurchaseOrders = projectPurchaseOrdersRepository.findAll();
        assertThat(projectPurchaseOrders).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProjectPurchaseOrders() throws Exception {
        // Initialize the database
        projectPurchaseOrdersRepository.saveAndFlush(projectPurchaseOrders);
        projectPurchaseOrdersSearchRepository.save(projectPurchaseOrders);

        // Search the projectPurchaseOrders
        restProjectPurchaseOrdersMockMvc.perform(get("/api/_search/project-purchase-orders?query=id:" + projectPurchaseOrders.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectPurchaseOrders.getId().intValue())))
            .andExpect(jsonPath("$.[*].po_number").value(hasItem(DEFAULT_PO_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].po_notes").value(hasItem(DEFAULT_PO_NOTES.toString())))
            .andExpect(jsonPath("$.[*].qb_rid").value(hasItem(DEFAULT_QB_RID)))
            .andExpect(jsonPath("$.[*].created_date").value(hasItem(DEFAULT_CREATED_DATE_STR)))
            .andExpect(jsonPath("$.[*].updated_date").value(hasItem(DEFAULT_UPDATED_DATE_STR)));
    }
}
