package com.fps.web.rest;

import com.fps.SmartLpcApp;
import com.fps.domain.Departments;
import com.fps.repository.DepartmentsRepository;
import com.fps.elastics.search.DepartmentsSearchRepository;

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
 * Test class for the DepartmentsResource REST controller.
 *
 * @see DepartmentsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartLpcApp.class)
@WebAppConfiguration
@IntegrationTest
public class DepartmentsResourceIntTest {

    private static final String DEFAULT_DEPARTMENT_NAME = "AAAAA";
    private static final String UPDATED_DEPARTMENT_NAME = "BBBBB";

    private static final Integer DEFAULT_COMPANY_ID = 1;
    private static final Integer UPDATED_COMPANY_ID = 2;

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_LOGO = "AAAAA";
    private static final String UPDATED_LOGO = "BBBBB";
    private static final String DEFAULT_URL_OVERRIDE = "AAAAA";
    private static final String UPDATED_URL_OVERRIDE = "BBBBB";

    private static final Boolean DEFAULT_SELF_PROJECT = false;
    private static final Boolean UPDATED_SELF_PROJECT = true;

    @Inject
    private DepartmentsRepository departmentsRepository;

    @Inject
    private DepartmentsSearchRepository departmentsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDepartmentsMockMvc;

    private Departments departments;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DepartmentsResource departmentsResource = new DepartmentsResource();
        ReflectionTestUtils.setField(departmentsResource, "departmentsSearchRepository", departmentsSearchRepository);
        ReflectionTestUtils.setField(departmentsResource, "departmentsRepository", departmentsRepository);
        this.restDepartmentsMockMvc = MockMvcBuilders.standaloneSetup(departmentsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        departmentsSearchRepository.deleteAll();
        departments = new Departments();
        departments.setDepartmentName(DEFAULT_DEPARTMENT_NAME);
        departments.setCompanyId(DEFAULT_COMPANY_ID);
        departments.setCreatedDate(DEFAULT_CREATED_DATE);
        departments.setUpdatedDate(DEFAULT_UPDATED_DATE);
        departments.setLogo(DEFAULT_LOGO);
        departments.setUrlOverride(DEFAULT_URL_OVERRIDE);
        departments.setSelfProject(DEFAULT_SELF_PROJECT);
    }

    @Test
    @Transactional
    public void createDepartments() throws Exception {
        int databaseSizeBeforeCreate = departmentsRepository.findAll().size();

        // Create the Departments

        restDepartmentsMockMvc.perform(post("/api/departments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(departments)))
                .andExpect(status().isCreated());

        // Validate the Departments in the database
        List<Departments> departments = departmentsRepository.findAll();
        assertThat(departments).hasSize(databaseSizeBeforeCreate + 1);
        Departments testDepartments = departments.get(departments.size() - 1);
        assertThat(testDepartments.getDepartmentName()).isEqualTo(DEFAULT_DEPARTMENT_NAME);
        assertThat(testDepartments.getCompanyId()).isEqualTo(DEFAULT_COMPANY_ID);
        assertThat(testDepartments.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDepartments.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testDepartments.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testDepartments.getUrlOverride()).isEqualTo(DEFAULT_URL_OVERRIDE);
        assertThat(testDepartments.isSelfProject()).isEqualTo(DEFAULT_SELF_PROJECT);

        // Validate the Departments in ElasticSearch
        Departments departmentsEs = departmentsSearchRepository.findOne(testDepartments.getId());
        assertThat(departmentsEs).isEqualToComparingFieldByField(testDepartments);
    }

    @Test
    @Transactional
    public void getAllDepartments() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get all the departments
        restDepartmentsMockMvc.perform(get("/api/departments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(departments.getId().intValue())))
                .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME.toString())))
                .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID)))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
                .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())))
                .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO.toString())))
                .andExpect(jsonPath("$.[*].urlOverride").value(hasItem(DEFAULT_URL_OVERRIDE.toString())))
                .andExpect(jsonPath("$.[*].selfProject").value(hasItem(DEFAULT_SELF_PROJECT.booleanValue())));
    }

    @Test
    @Transactional
    public void getDepartments() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get the departments
        restDepartmentsMockMvc.perform(get("/api/departments/{id}", departments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(departments.getId().intValue()))
            .andExpect(jsonPath("$.departmentName").value(DEFAULT_DEPARTMENT_NAME.toString()))
            .andExpect(jsonPath("$.companyId").value(DEFAULT_COMPANY_ID))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE.toString()))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO.toString()))
            .andExpect(jsonPath("$.urlOverride").value(DEFAULT_URL_OVERRIDE.toString()))
            .andExpect(jsonPath("$.selfProject").value(DEFAULT_SELF_PROJECT.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDepartments() throws Exception {
        // Get the departments
        restDepartmentsMockMvc.perform(get("/api/departments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDepartments() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);
        departmentsSearchRepository.save(departments);
        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();

        // Update the departments
        Departments updatedDepartments = new Departments();
        updatedDepartments.setId(departments.getId());
        updatedDepartments.setDepartmentName(UPDATED_DEPARTMENT_NAME);
        updatedDepartments.setCompanyId(UPDATED_COMPANY_ID);
        updatedDepartments.setCreatedDate(UPDATED_CREATED_DATE);
        updatedDepartments.setUpdatedDate(UPDATED_UPDATED_DATE);
        updatedDepartments.setLogo(UPDATED_LOGO);
        updatedDepartments.setUrlOverride(UPDATED_URL_OVERRIDE);
        updatedDepartments.setSelfProject(UPDATED_SELF_PROJECT);

        restDepartmentsMockMvc.perform(put("/api/departments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDepartments)))
                .andExpect(status().isOk());

        // Validate the Departments in the database
        List<Departments> departments = departmentsRepository.findAll();
        assertThat(departments).hasSize(databaseSizeBeforeUpdate);
        Departments testDepartments = departments.get(departments.size() - 1);
        assertThat(testDepartments.getDepartmentName()).isEqualTo(UPDATED_DEPARTMENT_NAME);
        assertThat(testDepartments.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testDepartments.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDepartments.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testDepartments.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testDepartments.getUrlOverride()).isEqualTo(UPDATED_URL_OVERRIDE);
        assertThat(testDepartments.isSelfProject()).isEqualTo(UPDATED_SELF_PROJECT);

        // Validate the Departments in ElasticSearch
        Departments departmentsEs = departmentsSearchRepository.findOne(testDepartments.getId());
        assertThat(departmentsEs).isEqualToComparingFieldByField(testDepartments);
    }

    @Test
    @Transactional
    public void deleteDepartments() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);
        departmentsSearchRepository.save(departments);
        int databaseSizeBeforeDelete = departmentsRepository.findAll().size();

        // Get the departments
        restDepartmentsMockMvc.perform(delete("/api/departments/{id}", departments.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean departmentsExistsInEs = departmentsSearchRepository.exists(departments.getId());
        assertThat(departmentsExistsInEs).isFalse();

        // Validate the database is empty
        List<Departments> departments = departmentsRepository.findAll();
        assertThat(departments).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDepartments() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);
        departmentsSearchRepository.save(departments);

        // Search the departments
        restDepartmentsMockMvc.perform(get("/api/_search/departments?query=id:" + departments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departments.getId().intValue())))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO.toString())))
            .andExpect(jsonPath("$.[*].urlOverride").value(hasItem(DEFAULT_URL_OVERRIDE.toString())))
            .andExpect(jsonPath("$.[*].selfProject").value(hasItem(DEFAULT_SELF_PROJECT.booleanValue())));
    }
}
