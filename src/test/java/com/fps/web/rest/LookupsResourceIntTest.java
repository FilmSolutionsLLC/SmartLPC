package com.fps.web.rest;

import com.fps.SmartLpcApp;
import com.fps.domain.Lookups;
import com.fps.repository.LookupsRepository;
import com.fps.service.LookupsService;
import com.fps.elastics.search.LookupsSearchRepository;

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
 * Test class for the LookupsResource REST controller.
 *
 * @see LookupsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartLpcApp.class)
@WebAppConfiguration
@IntegrationTest
public class LookupsResourceIntTest {

    private static final String DEFAULT_TABLE_NAME = "AAAAA";
    private static final String UPDATED_TABLE_NAME = "BBBBB";
    private static final String DEFAULT_FIELD_NAME = "AAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBB";
    private static final String DEFAULT_TEXT_VALUE = "AAAAA";
    private static final String UPDATED_TEXT_VALUE = "BBBBB";

    @Inject
    private LookupsRepository lookupsRepository;

    @Inject
    private LookupsService lookupsService;

    @Inject
    private LookupsSearchRepository lookupsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLookupsMockMvc;

    private Lookups lookups;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LookupsResource lookupsResource = new LookupsResource();
        ReflectionTestUtils.setField(lookupsResource, "lookupsService", lookupsService);
        this.restLookupsMockMvc = MockMvcBuilders.standaloneSetup(lookupsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        lookupsSearchRepository.deleteAll();
        lookups = new Lookups();
        lookups.setTableName(DEFAULT_TABLE_NAME);
        lookups.setFieldName(DEFAULT_FIELD_NAME);
        lookups.setTextValue(DEFAULT_TEXT_VALUE);
    }

    @Test
    @Transactional
    public void createLookups() throws Exception {
        int databaseSizeBeforeCreate = lookupsRepository.findAll().size();

        // Create the Lookups

        restLookupsMockMvc.perform(post("/api/lookups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookups)))
                .andExpect(status().isCreated());

        // Validate the Lookups in the database
        List<Lookups> lookups = lookupsRepository.findAll();
        assertThat(lookups).hasSize(databaseSizeBeforeCreate + 1);
        Lookups testLookups = lookups.get(lookups.size() - 1);
        assertThat(testLookups.getTableName()).isEqualTo(DEFAULT_TABLE_NAME);
        assertThat(testLookups.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
        assertThat(testLookups.getTextValue()).isEqualTo(DEFAULT_TEXT_VALUE);

        // Validate the Lookups in ElasticSearch
        Lookups lookupsEs = lookupsSearchRepository.findOne(testLookups.getId());
        assertThat(lookupsEs).isEqualToComparingFieldByField(testLookups);
    }

    @Test
    @Transactional
    public void getAllLookups() throws Exception {
        // Initialize the database
        lookupsRepository.saveAndFlush(lookups);

        // Get all the lookups
        restLookupsMockMvc.perform(get("/api/lookups?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lookups.getId().intValue())))
                .andExpect(jsonPath("$.[*].tableName").value(hasItem(DEFAULT_TABLE_NAME.toString())))
                .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME.toString())))
                .andExpect(jsonPath("$.[*].textValue").value(hasItem(DEFAULT_TEXT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getLookups() throws Exception {
        // Initialize the database
        lookupsRepository.saveAndFlush(lookups);

        // Get the lookups
        restLookupsMockMvc.perform(get("/api/lookups/{id}", lookups.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(lookups.getId().intValue()))
            .andExpect(jsonPath("$.tableName").value(DEFAULT_TABLE_NAME.toString()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME.toString()))
            .andExpect(jsonPath("$.textValue").value(DEFAULT_TEXT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLookups() throws Exception {
        // Get the lookups
        restLookupsMockMvc.perform(get("/api/lookups/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLookups() throws Exception {
        // Initialize the database
        lookupsService.save(lookups);

        int databaseSizeBeforeUpdate = lookupsRepository.findAll().size();

        // Update the lookups
        Lookups updatedLookups = new Lookups();
        updatedLookups.setId(lookups.getId());
        updatedLookups.setTableName(UPDATED_TABLE_NAME);
        updatedLookups.setFieldName(UPDATED_FIELD_NAME);
        updatedLookups.setTextValue(UPDATED_TEXT_VALUE);

        restLookupsMockMvc.perform(put("/api/lookups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLookups)))
                .andExpect(status().isOk());

        // Validate the Lookups in the database
        List<Lookups> lookups = lookupsRepository.findAll();
        assertThat(lookups).hasSize(databaseSizeBeforeUpdate);
        Lookups testLookups = lookups.get(lookups.size() - 1);
        assertThat(testLookups.getTableName()).isEqualTo(UPDATED_TABLE_NAME);
        assertThat(testLookups.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testLookups.getTextValue()).isEqualTo(UPDATED_TEXT_VALUE);

        // Validate the Lookups in ElasticSearch
        Lookups lookupsEs = lookupsSearchRepository.findOne(testLookups.getId());
        assertThat(lookupsEs).isEqualToComparingFieldByField(testLookups);
    }

    @Test
    @Transactional
    public void deleteLookups() throws Exception {
        // Initialize the database
        lookupsService.save(lookups);

        int databaseSizeBeforeDelete = lookupsRepository.findAll().size();

        // Get the lookups
        restLookupsMockMvc.perform(delete("/api/lookups/{id}", lookups.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean lookupsExistsInEs = lookupsSearchRepository.exists(lookups.getId());
        assertThat(lookupsExistsInEs).isFalse();

        // Validate the database is empty
        List<Lookups> lookups = lookupsRepository.findAll();
        assertThat(lookups).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLookups() throws Exception {
        // Initialize the database
        lookupsService.save(lookups);

        // Search the lookups
        restLookupsMockMvc.perform(get("/api/_search/lookups?query=id:" + lookups.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lookups.getId().intValue())))
            .andExpect(jsonPath("$.[*].tableName").value(hasItem(DEFAULT_TABLE_NAME.toString())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME.toString())))
            .andExpect(jsonPath("$.[*].textValue").value(hasItem(DEFAULT_TEXT_VALUE.toString())));
    }
}
