package com.fps.web.rest;

import com.fps.SmartLpcApp;
import com.fps.domain.WorkOrderAbcFile;
import com.fps.repository.WorkOrderAbcFileRepository;
import com.fps.elastics.search.WorkOrderAbcFileSearchRepository;

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
 * Test class for the WorkOrderAbcFileResource REST controller.
 *
 * @see WorkOrderAbcFileResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartLpcApp.class)
@WebAppConfiguration
@IntegrationTest
public class WorkOrderAbcFileResourceIntTest {


    private static final Integer DEFAULT_FILE_COUNT = 1;
    private static final Integer UPDATED_FILE_COUNT = 2;
    private static final String DEFAULT_FILE_SIZE = "AAAAA";
    private static final String UPDATED_FILE_SIZE = "BBBBB";

    @Inject
    private WorkOrderAbcFileRepository workOrderAbcFileRepository;

    @Inject
    private WorkOrderAbcFileSearchRepository workOrderAbcFileSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWorkOrderAbcFileMockMvc;

    private WorkOrderAbcFile workOrderAbcFile;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkOrderAbcFileResource workOrderAbcFileResource = new WorkOrderAbcFileResource();
        ReflectionTestUtils.setField(workOrderAbcFileResource, "workOrderAbcFileSearchRepository", workOrderAbcFileSearchRepository);
        ReflectionTestUtils.setField(workOrderAbcFileResource, "workOrderAbcFileRepository", workOrderAbcFileRepository);
        this.restWorkOrderAbcFileMockMvc = MockMvcBuilders.standaloneSetup(workOrderAbcFileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        workOrderAbcFileSearchRepository.deleteAll();
        workOrderAbcFile = new WorkOrderAbcFile();
        workOrderAbcFile.setFile_count(DEFAULT_FILE_COUNT);
        workOrderAbcFile.setFile_size(DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    public void createWorkOrderAbcFile() throws Exception {
        int databaseSizeBeforeCreate = workOrderAbcFileRepository.findAll().size();

        // Create the WorkOrderAbcFile

        restWorkOrderAbcFileMockMvc.perform(post("/api/work-order-abc-files")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workOrderAbcFile)))
                .andExpect(status().isCreated());

        // Validate the WorkOrderAbcFile in the database
        List<WorkOrderAbcFile> workOrderAbcFiles = workOrderAbcFileRepository.findAll();
        assertThat(workOrderAbcFiles).hasSize(databaseSizeBeforeCreate + 1);
        WorkOrderAbcFile testWorkOrderAbcFile = workOrderAbcFiles.get(workOrderAbcFiles.size() - 1);
        assertThat(testWorkOrderAbcFile.getFile_count()).isEqualTo(DEFAULT_FILE_COUNT);
        assertThat(testWorkOrderAbcFile.getFile_size()).isEqualTo(DEFAULT_FILE_SIZE);

        // Validate the WorkOrderAbcFile in ElasticSearch
        WorkOrderAbcFile workOrderAbcFileEs = workOrderAbcFileSearchRepository.findOne(testWorkOrderAbcFile.getId());
        assertThat(workOrderAbcFileEs).isEqualToComparingFieldByField(testWorkOrderAbcFile);
    }

    @Test
    @Transactional
    public void getAllWorkOrderAbcFiles() throws Exception {
        // Initialize the database
        workOrderAbcFileRepository.saveAndFlush(workOrderAbcFile);

        // Get all the workOrderAbcFiles
        restWorkOrderAbcFileMockMvc.perform(get("/api/work-order-abc-files?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(workOrderAbcFile.getId().intValue())))
                .andExpect(jsonPath("$.[*].file_count").value(hasItem(DEFAULT_FILE_COUNT)))
                .andExpect(jsonPath("$.[*].file_size").value(hasItem(DEFAULT_FILE_SIZE.toString())));
    }

    @Test
    @Transactional
    public void getWorkOrderAbcFile() throws Exception {
        // Initialize the database
        workOrderAbcFileRepository.saveAndFlush(workOrderAbcFile);

        // Get the workOrderAbcFile
        restWorkOrderAbcFileMockMvc.perform(get("/api/work-order-abc-files/{id}", workOrderAbcFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(workOrderAbcFile.getId().intValue()))
            .andExpect(jsonPath("$.file_count").value(DEFAULT_FILE_COUNT))
            .andExpect(jsonPath("$.file_size").value(DEFAULT_FILE_SIZE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkOrderAbcFile() throws Exception {
        // Get the workOrderAbcFile
        restWorkOrderAbcFileMockMvc.perform(get("/api/work-order-abc-files/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkOrderAbcFile() throws Exception {
        // Initialize the database
        workOrderAbcFileRepository.saveAndFlush(workOrderAbcFile);
        workOrderAbcFileSearchRepository.save(workOrderAbcFile);
        int databaseSizeBeforeUpdate = workOrderAbcFileRepository.findAll().size();

        // Update the workOrderAbcFile
        WorkOrderAbcFile updatedWorkOrderAbcFile = new WorkOrderAbcFile();
        updatedWorkOrderAbcFile.setId(workOrderAbcFile.getId());
        updatedWorkOrderAbcFile.setFile_count(UPDATED_FILE_COUNT);
        updatedWorkOrderAbcFile.setFile_size(UPDATED_FILE_SIZE);

        restWorkOrderAbcFileMockMvc.perform(put("/api/work-order-abc-files")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedWorkOrderAbcFile)))
                .andExpect(status().isOk());

        // Validate the WorkOrderAbcFile in the database
        List<WorkOrderAbcFile> workOrderAbcFiles = workOrderAbcFileRepository.findAll();
        assertThat(workOrderAbcFiles).hasSize(databaseSizeBeforeUpdate);
        WorkOrderAbcFile testWorkOrderAbcFile = workOrderAbcFiles.get(workOrderAbcFiles.size() - 1);
        assertThat(testWorkOrderAbcFile.getFile_count()).isEqualTo(UPDATED_FILE_COUNT);
        assertThat(testWorkOrderAbcFile.getFile_size()).isEqualTo(UPDATED_FILE_SIZE);

        // Validate the WorkOrderAbcFile in ElasticSearch
        WorkOrderAbcFile workOrderAbcFileEs = workOrderAbcFileSearchRepository.findOne(testWorkOrderAbcFile.getId());
        assertThat(workOrderAbcFileEs).isEqualToComparingFieldByField(testWorkOrderAbcFile);
    }

    @Test
    @Transactional
    public void deleteWorkOrderAbcFile() throws Exception {
        // Initialize the database
        workOrderAbcFileRepository.saveAndFlush(workOrderAbcFile);
        workOrderAbcFileSearchRepository.save(workOrderAbcFile);
        int databaseSizeBeforeDelete = workOrderAbcFileRepository.findAll().size();

        // Get the workOrderAbcFile
        restWorkOrderAbcFileMockMvc.perform(delete("/api/work-order-abc-files/{id}", workOrderAbcFile.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean workOrderAbcFileExistsInEs = workOrderAbcFileSearchRepository.exists(workOrderAbcFile.getId());
        assertThat(workOrderAbcFileExistsInEs).isFalse();

        // Validate the database is empty
        List<WorkOrderAbcFile> workOrderAbcFiles = workOrderAbcFileRepository.findAll();
        assertThat(workOrderAbcFiles).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWorkOrderAbcFile() throws Exception {
        // Initialize the database
        workOrderAbcFileRepository.saveAndFlush(workOrderAbcFile);
        workOrderAbcFileSearchRepository.save(workOrderAbcFile);

        // Search the workOrderAbcFile
        restWorkOrderAbcFileMockMvc.perform(get("/api/_search/work-order-abc-files?query=id:" + workOrderAbcFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workOrderAbcFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].file_count").value(hasItem(DEFAULT_FILE_COUNT)))
            .andExpect(jsonPath("$.[*].file_size").value(hasItem(DEFAULT_FILE_SIZE.toString())));
    }
}
