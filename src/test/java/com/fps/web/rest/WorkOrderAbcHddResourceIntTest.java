package com.fps.web.rest;

import com.fps.SmartLpcApp;
import com.fps.domain.WorkOrderAbcHdd;
import com.fps.repository.WorkOrderAbcHddRepository;
import com.fps.elastics.search.WorkOrderAbcHddSearchRepository;

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
 * Test class for the WorkOrderAbcHddResource REST controller.
 *
 * @see WorkOrderAbcHddResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartLpcApp.class)
@WebAppConfiguration
@IntegrationTest
public class WorkOrderAbcHddResourceIntTest {

    private static final String DEFAULT_SIZE = "AAAAA";
    private static final String UPDATED_SIZE = "BBBBB";
    private static final String DEFAULT_DRIVE_NUMBER = "AAAAA";
    private static final String UPDATED_DRIVE_NUMBER = "BBBBB";

    @Inject
    private WorkOrderAbcHddRepository workOrderAbcHddRepository;

    @Inject
    private WorkOrderAbcHddSearchRepository workOrderAbcHddSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWorkOrderAbcHddMockMvc;

    private WorkOrderAbcHdd workOrderAbcHdd;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkOrderAbcHddResource workOrderAbcHddResource = new WorkOrderAbcHddResource();
        ReflectionTestUtils.setField(workOrderAbcHddResource, "workOrderAbcHddSearchRepository", workOrderAbcHddSearchRepository);
        ReflectionTestUtils.setField(workOrderAbcHddResource, "workOrderAbcHddRepository", workOrderAbcHddRepository);
        this.restWorkOrderAbcHddMockMvc = MockMvcBuilders.standaloneSetup(workOrderAbcHddResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        workOrderAbcHddSearchRepository.deleteAll();
        workOrderAbcHdd = new WorkOrderAbcHdd();
        workOrderAbcHdd.setSize(DEFAULT_SIZE);
        workOrderAbcHdd.setDrive_number(DEFAULT_DRIVE_NUMBER);
    }

    @Test
    @Transactional
    public void createWorkOrderAbcHdd() throws Exception {
        int databaseSizeBeforeCreate = workOrderAbcHddRepository.findAll().size();

        // Create the WorkOrderAbcHdd

        restWorkOrderAbcHddMockMvc.perform(post("/api/work-order-abc-hdds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workOrderAbcHdd)))
                .andExpect(status().isCreated());

        // Validate the WorkOrderAbcHdd in the database
        List<WorkOrderAbcHdd> workOrderAbcHdds = workOrderAbcHddRepository.findAll();
        assertThat(workOrderAbcHdds).hasSize(databaseSizeBeforeCreate + 1);
        WorkOrderAbcHdd testWorkOrderAbcHdd = workOrderAbcHdds.get(workOrderAbcHdds.size() - 1);
        assertThat(testWorkOrderAbcHdd.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testWorkOrderAbcHdd.getDrive_number()).isEqualTo(DEFAULT_DRIVE_NUMBER);

        // Validate the WorkOrderAbcHdd in ElasticSearch
        WorkOrderAbcHdd workOrderAbcHddEs = workOrderAbcHddSearchRepository.findOne(testWorkOrderAbcHdd.getId());
        assertThat(workOrderAbcHddEs).isEqualToComparingFieldByField(testWorkOrderAbcHdd);
    }

    @Test
    @Transactional
    public void getAllWorkOrderAbcHdds() throws Exception {
        // Initialize the database
        workOrderAbcHddRepository.saveAndFlush(workOrderAbcHdd);

        // Get all the workOrderAbcHdds
        restWorkOrderAbcHddMockMvc.perform(get("/api/work-order-abc-hdds?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(workOrderAbcHdd.getId().intValue())))
                .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.toString())))
                .andExpect(jsonPath("$.[*].drive_number").value(hasItem(DEFAULT_DRIVE_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getWorkOrderAbcHdd() throws Exception {
        // Initialize the database
        workOrderAbcHddRepository.saveAndFlush(workOrderAbcHdd);

        // Get the workOrderAbcHdd
        restWorkOrderAbcHddMockMvc.perform(get("/api/work-order-abc-hdds/{id}", workOrderAbcHdd.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(workOrderAbcHdd.getId().intValue()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.toString()))
            .andExpect(jsonPath("$.drive_number").value(DEFAULT_DRIVE_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkOrderAbcHdd() throws Exception {
        // Get the workOrderAbcHdd
        restWorkOrderAbcHddMockMvc.perform(get("/api/work-order-abc-hdds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkOrderAbcHdd() throws Exception {
        // Initialize the database
        workOrderAbcHddRepository.saveAndFlush(workOrderAbcHdd);
        workOrderAbcHddSearchRepository.save(workOrderAbcHdd);
        int databaseSizeBeforeUpdate = workOrderAbcHddRepository.findAll().size();

        // Update the workOrderAbcHdd
        WorkOrderAbcHdd updatedWorkOrderAbcHdd = new WorkOrderAbcHdd();
        updatedWorkOrderAbcHdd.setId(workOrderAbcHdd.getId());
        updatedWorkOrderAbcHdd.setSize(UPDATED_SIZE);
        updatedWorkOrderAbcHdd.setDrive_number(UPDATED_DRIVE_NUMBER);

        restWorkOrderAbcHddMockMvc.perform(put("/api/work-order-abc-hdds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedWorkOrderAbcHdd)))
                .andExpect(status().isOk());

        // Validate the WorkOrderAbcHdd in the database
        List<WorkOrderAbcHdd> workOrderAbcHdds = workOrderAbcHddRepository.findAll();
        assertThat(workOrderAbcHdds).hasSize(databaseSizeBeforeUpdate);
        WorkOrderAbcHdd testWorkOrderAbcHdd = workOrderAbcHdds.get(workOrderAbcHdds.size() - 1);
        assertThat(testWorkOrderAbcHdd.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testWorkOrderAbcHdd.getDrive_number()).isEqualTo(UPDATED_DRIVE_NUMBER);

        // Validate the WorkOrderAbcHdd in ElasticSearch
        WorkOrderAbcHdd workOrderAbcHddEs = workOrderAbcHddSearchRepository.findOne(testWorkOrderAbcHdd.getId());
        assertThat(workOrderAbcHddEs).isEqualToComparingFieldByField(testWorkOrderAbcHdd);
    }

    @Test
    @Transactional
    public void deleteWorkOrderAbcHdd() throws Exception {
        // Initialize the database
        workOrderAbcHddRepository.saveAndFlush(workOrderAbcHdd);
        workOrderAbcHddSearchRepository.save(workOrderAbcHdd);
        int databaseSizeBeforeDelete = workOrderAbcHddRepository.findAll().size();

        // Get the workOrderAbcHdd
        restWorkOrderAbcHddMockMvc.perform(delete("/api/work-order-abc-hdds/{id}", workOrderAbcHdd.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean workOrderAbcHddExistsInEs = workOrderAbcHddSearchRepository.exists(workOrderAbcHdd.getId());
        assertThat(workOrderAbcHddExistsInEs).isFalse();

        // Validate the database is empty
        List<WorkOrderAbcHdd> workOrderAbcHdds = workOrderAbcHddRepository.findAll();
        assertThat(workOrderAbcHdds).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWorkOrderAbcHdd() throws Exception {
        // Initialize the database
        workOrderAbcHddRepository.saveAndFlush(workOrderAbcHdd);
        workOrderAbcHddSearchRepository.save(workOrderAbcHdd);

        // Search the workOrderAbcHdd
        restWorkOrderAbcHddMockMvc.perform(get("/api/_search/work-order-abc-hdds?query=id:" + workOrderAbcHdd.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workOrderAbcHdd.getId().intValue())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.toString())))
            .andExpect(jsonPath("$.[*].drive_number").value(hasItem(DEFAULT_DRIVE_NUMBER.toString())));
    }
}
