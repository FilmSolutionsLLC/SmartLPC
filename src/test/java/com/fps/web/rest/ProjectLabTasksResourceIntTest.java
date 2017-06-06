package com.fps.web.rest;

import com.fps.SmartLpcApp;
import com.fps.domain.ProjectLabTasks;
import com.fps.repository.ProjectLabTasksRepository;
import com.fps.service.ProjectLabTasksService;
import com.fps.elastics.search.ProjectLabTasksSearchRepository;

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
 * Test class for the ProjectLabTasksResource REST controller.
 *
 * @see ProjectLabTasksResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartLpcApp.class)
@WebAppConfiguration
@IntegrationTest
public class ProjectLabTasksResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATED_DATE);

    private static final ZonedDateTime DEFAULT_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_UPDATED_DATE_STR = dateTimeFormatter.format(DEFAULT_UPDATED_DATE);

    private static final Integer DEFAULT_QB_RID = 1;
    private static final Integer UPDATED_QB_RID = 2;

    @Inject
    private ProjectLabTasksRepository projectLabTasksRepository;

    @Inject
    private ProjectLabTasksService projectLabTasksService;

    @Inject
    private ProjectLabTasksSearchRepository projectLabTasksSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProjectLabTasksMockMvc;

    private ProjectLabTasks projectLabTasks;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectLabTasksResource projectLabTasksResource = new ProjectLabTasksResource();
        ReflectionTestUtils.setField(projectLabTasksResource, "projectLabTasksService", projectLabTasksService);
        this.restProjectLabTasksMockMvc = MockMvcBuilders.standaloneSetup(projectLabTasksResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        projectLabTasksSearchRepository.deleteAll();
        projectLabTasks = new ProjectLabTasks();
        projectLabTasks.setCreatedDate(DEFAULT_CREATED_DATE);
        projectLabTasks.setUpdatedDate(DEFAULT_UPDATED_DATE);
        projectLabTasks.setQb_rid(DEFAULT_QB_RID);
    }

    @Test
    @Transactional
    public void createProjectLabTasks() throws Exception {
        int databaseSizeBeforeCreate = projectLabTasksRepository.findAll().size();

        // Create the ProjectLabTasks

        restProjectLabTasksMockMvc.perform(post("/api/project-lab-tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectLabTasks)))
                .andExpect(status().isCreated());

        // Validate the ProjectLabTasks in the database
        List<ProjectLabTasks> projectLabTasks = projectLabTasksRepository.findAll();
        assertThat(projectLabTasks).hasSize(databaseSizeBeforeCreate + 1);
        ProjectLabTasks testProjectLabTasks = projectLabTasks.get(projectLabTasks.size() - 1);
        assertThat(testProjectLabTasks.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProjectLabTasks.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testProjectLabTasks.getQb_rid()).isEqualTo(DEFAULT_QB_RID);

        // Validate the ProjectLabTasks in ElasticSearch
        ProjectLabTasks projectLabTasksEs = projectLabTasksSearchRepository.findOne(testProjectLabTasks.getId());
        assertThat(projectLabTasksEs).isEqualToComparingFieldByField(testProjectLabTasks);
    }

    @Test
    @Transactional
    public void getAllProjectLabTasks() throws Exception {
        // Initialize the database
        projectLabTasksRepository.saveAndFlush(projectLabTasks);

        // Get all the projectLabTasks
        restProjectLabTasksMockMvc.perform(get("/api/project-lab-tasks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(projectLabTasks.getId().intValue())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].qb_rid").value(hasItem(DEFAULT_QB_RID)));
    }

    @Test
    @Transactional
    public void getProjectLabTasks() throws Exception {
        // Initialize the database
        projectLabTasksRepository.saveAndFlush(projectLabTasks);

        // Get the projectLabTasks
        restProjectLabTasksMockMvc.perform(get("/api/project-lab-tasks/{id}", projectLabTasks.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(projectLabTasks.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE_STR))
            .andExpect(jsonPath("$.qb_rid").value(DEFAULT_QB_RID));
    }

    @Test
    @Transactional
    public void getNonExistingProjectLabTasks() throws Exception {
        // Get the projectLabTasks
        restProjectLabTasksMockMvc.perform(get("/api/project-lab-tasks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectLabTasks() throws Exception {
        // Initialize the database
        projectLabTasksService.save(projectLabTasks);

        int databaseSizeBeforeUpdate = projectLabTasksRepository.findAll().size();

        // Update the projectLabTasks
        ProjectLabTasks updatedProjectLabTasks = new ProjectLabTasks();
        updatedProjectLabTasks.setId(projectLabTasks.getId());
        updatedProjectLabTasks.setCreatedDate(UPDATED_CREATED_DATE);
        updatedProjectLabTasks.setUpdatedDate(UPDATED_UPDATED_DATE);
        updatedProjectLabTasks.setQb_rid(UPDATED_QB_RID);

        restProjectLabTasksMockMvc.perform(put("/api/project-lab-tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProjectLabTasks)))
                .andExpect(status().isOk());

        // Validate the ProjectLabTasks in the database
        List<ProjectLabTasks> projectLabTasks = projectLabTasksRepository.findAll();
        assertThat(projectLabTasks).hasSize(databaseSizeBeforeUpdate);
        ProjectLabTasks testProjectLabTasks = projectLabTasks.get(projectLabTasks.size() - 1);
        assertThat(testProjectLabTasks.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProjectLabTasks.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testProjectLabTasks.getQb_rid()).isEqualTo(UPDATED_QB_RID);

        // Validate the ProjectLabTasks in ElasticSearch
        ProjectLabTasks projectLabTasksEs = projectLabTasksSearchRepository.findOne(testProjectLabTasks.getId());
        assertThat(projectLabTasksEs).isEqualToComparingFieldByField(testProjectLabTasks);
    }

    @Test
    @Transactional
    public void deleteProjectLabTasks() throws Exception {
        // Initialize the database
        projectLabTasksService.save(projectLabTasks);

        int databaseSizeBeforeDelete = projectLabTasksRepository.findAll().size();

        // Get the projectLabTasks
        restProjectLabTasksMockMvc.perform(delete("/api/project-lab-tasks/{id}", projectLabTasks.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean projectLabTasksExistsInEs = projectLabTasksSearchRepository.exists(projectLabTasks.getId());
        assertThat(projectLabTasksExistsInEs).isFalse();

        // Validate the database is empty
        List<ProjectLabTasks> projectLabTasks = projectLabTasksRepository.findAll();
        assertThat(projectLabTasks).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProjectLabTasks() throws Exception {
        // Initialize the database
        projectLabTasksService.save(projectLabTasks);

        // Search the projectLabTasks
        restProjectLabTasksMockMvc.perform(get("/api/_search/project-lab-tasks?query=id:" + projectLabTasks.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectLabTasks.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE_STR)))
            .andExpect(jsonPath("$.[*].qb_rid").value(hasItem(DEFAULT_QB_RID)));
    }
}
