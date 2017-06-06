package com.fps.web.rest;

import com.fps.SmartLpcApp;
import com.fps.domain.WorkOrdersAdminRelation;
import com.fps.repository.WorkOrdersAdminRelationRepository;
import com.fps.elastics.search.WorkOrdersAdminRelationSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the WorkOrdersAdminRelationResource REST controller.
 *
 * @see WorkOrdersAdminRelationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartLpcApp.class)
@WebAppConfiguration
@IntegrationTest
public class WorkOrdersAdminRelationResourceIntTest {


    @Inject
    private WorkOrdersAdminRelationRepository workOrdersAdminRelationRepository;

    @Inject
    private WorkOrdersAdminRelationSearchRepository workOrdersAdminRelationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWorkOrdersAdminRelationMockMvc;

    private WorkOrdersAdminRelation workOrdersAdminRelation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkOrdersAdminRelationResource workOrdersAdminRelationResource = new WorkOrdersAdminRelationResource();
        ReflectionTestUtils.setField(workOrdersAdminRelationResource, "workOrdersAdminRelationSearchRepository", workOrdersAdminRelationSearchRepository);
        ReflectionTestUtils.setField(workOrdersAdminRelationResource, "workOrdersAdminRelationRepository", workOrdersAdminRelationRepository);
        this.restWorkOrdersAdminRelationMockMvc = MockMvcBuilders.standaloneSetup(workOrdersAdminRelationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        workOrdersAdminRelationSearchRepository.deleteAll();
        workOrdersAdminRelation = new WorkOrdersAdminRelation();
    }

    @Test
    @Transactional
    public void createWorkOrdersAdminRelation() throws Exception {
        int databaseSizeBeforeCreate = workOrdersAdminRelationRepository.findAll().size();

        // Create the WorkOrdersAdminRelation

        restWorkOrdersAdminRelationMockMvc.perform(post("/api/work-orders-admin-relations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workOrdersAdminRelation)))
                .andExpect(status().isCreated());

        // Validate the WorkOrdersAdminRelation in the database
        List<WorkOrdersAdminRelation> workOrdersAdminRelations = workOrdersAdminRelationRepository.findAll();
        assertThat(workOrdersAdminRelations).hasSize(databaseSizeBeforeCreate + 1);
        WorkOrdersAdminRelation testWorkOrdersAdminRelation = workOrdersAdminRelations.get(workOrdersAdminRelations.size() - 1);

        // Validate the WorkOrdersAdminRelation in ElasticSearch
        WorkOrdersAdminRelation workOrdersAdminRelationEs = workOrdersAdminRelationSearchRepository.findOne(testWorkOrdersAdminRelation.getId());
        assertThat(workOrdersAdminRelationEs).isEqualToComparingFieldByField(testWorkOrdersAdminRelation);
    }

    @Test
    @Transactional
    public void getAllWorkOrdersAdminRelations() throws Exception {
        // Initialize the database
        workOrdersAdminRelationRepository.saveAndFlush(workOrdersAdminRelation);

        // Get all the workOrdersAdminRelations
        restWorkOrdersAdminRelationMockMvc.perform(get("/api/work-orders-admin-relations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(workOrdersAdminRelation.getId().intValue())));
    }

    @Test
    @Transactional
    public void getWorkOrdersAdminRelation() throws Exception {
        // Initialize the database
        workOrdersAdminRelationRepository.saveAndFlush(workOrdersAdminRelation);

        // Get the workOrdersAdminRelation
        restWorkOrdersAdminRelationMockMvc.perform(get("/api/work-orders-admin-relations/{id}", workOrdersAdminRelation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(workOrdersAdminRelation.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkOrdersAdminRelation() throws Exception {
        // Get the workOrdersAdminRelation
        restWorkOrdersAdminRelationMockMvc.perform(get("/api/work-orders-admin-relations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkOrdersAdminRelation() throws Exception {
        // Initialize the database
        workOrdersAdminRelationRepository.saveAndFlush(workOrdersAdminRelation);
        workOrdersAdminRelationSearchRepository.save(workOrdersAdminRelation);
        int databaseSizeBeforeUpdate = workOrdersAdminRelationRepository.findAll().size();

        // Update the workOrdersAdminRelation
        WorkOrdersAdminRelation updatedWorkOrdersAdminRelation = new WorkOrdersAdminRelation();
        updatedWorkOrdersAdminRelation.setId(workOrdersAdminRelation.getId());

        restWorkOrdersAdminRelationMockMvc.perform(put("/api/work-orders-admin-relations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedWorkOrdersAdminRelation)))
                .andExpect(status().isOk());

        // Validate the WorkOrdersAdminRelation in the database
        List<WorkOrdersAdminRelation> workOrdersAdminRelations = workOrdersAdminRelationRepository.findAll();
        assertThat(workOrdersAdminRelations).hasSize(databaseSizeBeforeUpdate);
        WorkOrdersAdminRelation testWorkOrdersAdminRelation = workOrdersAdminRelations.get(workOrdersAdminRelations.size() - 1);

        // Validate the WorkOrdersAdminRelation in ElasticSearch
        WorkOrdersAdminRelation workOrdersAdminRelationEs = workOrdersAdminRelationSearchRepository.findOne(testWorkOrdersAdminRelation.getId());
        assertThat(workOrdersAdminRelationEs).isEqualToComparingFieldByField(testWorkOrdersAdminRelation);
    }

    @Test
    @Transactional
    public void deleteWorkOrdersAdminRelation() throws Exception {
        // Initialize the database
        workOrdersAdminRelationRepository.saveAndFlush(workOrdersAdminRelation);
        workOrdersAdminRelationSearchRepository.save(workOrdersAdminRelation);
        int databaseSizeBeforeDelete = workOrdersAdminRelationRepository.findAll().size();

        // Get the workOrdersAdminRelation
        restWorkOrdersAdminRelationMockMvc.perform(delete("/api/work-orders-admin-relations/{id}", workOrdersAdminRelation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean workOrdersAdminRelationExistsInEs = workOrdersAdminRelationSearchRepository.exists(workOrdersAdminRelation.getId());
        assertThat(workOrdersAdminRelationExistsInEs).isFalse();

        // Validate the database is empty
        List<WorkOrdersAdminRelation> workOrdersAdminRelations = workOrdersAdminRelationRepository.findAll();
        assertThat(workOrdersAdminRelations).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWorkOrdersAdminRelation() throws Exception {
        // Initialize the database
        workOrdersAdminRelationRepository.saveAndFlush(workOrdersAdminRelation);
        workOrdersAdminRelationSearchRepository.save(workOrdersAdminRelation);

        // Search the workOrdersAdminRelation
        restWorkOrdersAdminRelationMockMvc.perform(get("/api/_search/work-orders-admin-relations?query=id:" + workOrdersAdminRelation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workOrdersAdminRelation.getId().intValue())));
    }
}
