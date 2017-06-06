package com.fps.web.rest;

import com.fps.SmartLpcApp;
import com.fps.domain.Storage_Disk;
import com.fps.repository.Storage_DiskRepository;
import com.fps.elastics.search.Storage_DiskSearchRepository;

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
 * Test class for the Storage_DiskResource REST controller.
 *
 * @see Storage_DiskResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartLpcApp.class)
@WebAppConfiguration
@IntegrationTest
public class Storage_DiskResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Float DEFAULT_SIZE = 1F;
    private static final Float UPDATED_SIZE = 2F;

    private static final Float DEFAULT_USED = 1F;
    private static final Float UPDATED_USED = 2F;

    private static final Float DEFAULT_AVAILABLE = 1F;
    private static final Float UPDATED_AVAILABLE = 2F;

    private static final Integer DEFAULT_USE_PERCENT = 1;
    private static final Integer UPDATED_USE_PERCENT = 2;

    private static final LocalDate DEFAULT_LAST_UPDATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_UPDATED = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_RESERVED = false;
    private static final Boolean UPDATED_RESERVED = true;

    @Inject
    private Storage_DiskRepository storage_DiskRepository;

    @Inject
    private Storage_DiskSearchRepository storage_DiskSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStorage_DiskMockMvc;

    private Storage_Disk storage_Disk;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Storage_DiskResource storage_DiskResource = new Storage_DiskResource();
        ReflectionTestUtils.setField(storage_DiskResource, "storage_DiskSearchRepository", storage_DiskSearchRepository);
        ReflectionTestUtils.setField(storage_DiskResource, "storage_DiskRepository", storage_DiskRepository);
        this.restStorage_DiskMockMvc = MockMvcBuilders.standaloneSetup(storage_DiskResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        storage_DiskSearchRepository.deleteAll();
        storage_Disk = new Storage_Disk();
        storage_Disk.setName(DEFAULT_NAME);
        storage_Disk.setSize(DEFAULT_SIZE);
        storage_Disk.setUsed(DEFAULT_USED);
        storage_Disk.setAvailable(DEFAULT_AVAILABLE);
        storage_Disk.setUsePercent(DEFAULT_USE_PERCENT);
        storage_Disk.setLastUpdated(DEFAULT_LAST_UPDATED);
        storage_Disk.setReserved(DEFAULT_RESERVED);
    }

    @Test
    @Transactional
    public void createStorage_Disk() throws Exception {
        int databaseSizeBeforeCreate = storage_DiskRepository.findAll().size();

        // Create the Storage_Disk

        restStorage_DiskMockMvc.perform(post("/api/storage-disks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(storage_Disk)))
                .andExpect(status().isCreated());

        // Validate the Storage_Disk in the database
        List<Storage_Disk> storage_Disks = storage_DiskRepository.findAll();
        assertThat(storage_Disks).hasSize(databaseSizeBeforeCreate + 1);
        Storage_Disk testStorage_Disk = storage_Disks.get(storage_Disks.size() - 1);
        assertThat(testStorage_Disk.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStorage_Disk.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testStorage_Disk.getUsed()).isEqualTo(DEFAULT_USED);
        assertThat(testStorage_Disk.getAvailable()).isEqualTo(DEFAULT_AVAILABLE);
        assertThat(testStorage_Disk.getUsePercent()).isEqualTo(DEFAULT_USE_PERCENT);
        assertThat(testStorage_Disk.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testStorage_Disk.isReserved()).isEqualTo(DEFAULT_RESERVED);

        // Validate the Storage_Disk in ElasticSearch
        Storage_Disk storage_DiskEs = storage_DiskSearchRepository.findOne(testStorage_Disk.getId());
        assertThat(storage_DiskEs).isEqualToComparingFieldByField(testStorage_Disk);
    }

    @Test
    @Transactional
    public void getAllStorage_Disks() throws Exception {
        // Initialize the database
        storage_DiskRepository.saveAndFlush(storage_Disk);

        // Get all the storage_Disks
        restStorage_DiskMockMvc.perform(get("/api/storage-disks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(storage_Disk.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.doubleValue())))
                .andExpect(jsonPath("$.[*].used").value(hasItem(DEFAULT_USED.doubleValue())))
                .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE.doubleValue())))
                .andExpect(jsonPath("$.[*].usePercent").value(hasItem(DEFAULT_USE_PERCENT)))
                .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
                .andExpect(jsonPath("$.[*].reserved").value(hasItem(DEFAULT_RESERVED.booleanValue())));
    }

    @Test
    @Transactional
    public void getStorage_Disk() throws Exception {
        // Initialize the database
        storage_DiskRepository.saveAndFlush(storage_Disk);

        // Get the storage_Disk
        restStorage_DiskMockMvc.perform(get("/api/storage-disks/{id}", storage_Disk.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(storage_Disk.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.doubleValue()))
            .andExpect(jsonPath("$.used").value(DEFAULT_USED.doubleValue()))
            .andExpect(jsonPath("$.available").value(DEFAULT_AVAILABLE.doubleValue()))
            .andExpect(jsonPath("$.usePercent").value(DEFAULT_USE_PERCENT))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.reserved").value(DEFAULT_RESERVED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStorage_Disk() throws Exception {
        // Get the storage_Disk
        restStorage_DiskMockMvc.perform(get("/api/storage-disks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStorage_Disk() throws Exception {
        // Initialize the database
        storage_DiskRepository.saveAndFlush(storage_Disk);
        storage_DiskSearchRepository.save(storage_Disk);
        int databaseSizeBeforeUpdate = storage_DiskRepository.findAll().size();

        // Update the storage_Disk
        Storage_Disk updatedStorage_Disk = new Storage_Disk();
        updatedStorage_Disk.setId(storage_Disk.getId());
        updatedStorage_Disk.setName(UPDATED_NAME);
        updatedStorage_Disk.setSize(UPDATED_SIZE);
        updatedStorage_Disk.setUsed(UPDATED_USED);
        updatedStorage_Disk.setAvailable(UPDATED_AVAILABLE);
        updatedStorage_Disk.setUsePercent(UPDATED_USE_PERCENT);
        updatedStorage_Disk.setLastUpdated(UPDATED_LAST_UPDATED);
        updatedStorage_Disk.setReserved(UPDATED_RESERVED);

        restStorage_DiskMockMvc.perform(put("/api/storage-disks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedStorage_Disk)))
                .andExpect(status().isOk());

        // Validate the Storage_Disk in the database
        List<Storage_Disk> storage_Disks = storage_DiskRepository.findAll();
        assertThat(storage_Disks).hasSize(databaseSizeBeforeUpdate);
        Storage_Disk testStorage_Disk = storage_Disks.get(storage_Disks.size() - 1);
        assertThat(testStorage_Disk.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStorage_Disk.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testStorage_Disk.getUsed()).isEqualTo(UPDATED_USED);
        assertThat(testStorage_Disk.getAvailable()).isEqualTo(UPDATED_AVAILABLE);
        assertThat(testStorage_Disk.getUsePercent()).isEqualTo(UPDATED_USE_PERCENT);
        assertThat(testStorage_Disk.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testStorage_Disk.isReserved()).isEqualTo(UPDATED_RESERVED);

        // Validate the Storage_Disk in ElasticSearch
        Storage_Disk storage_DiskEs = storage_DiskSearchRepository.findOne(testStorage_Disk.getId());
        assertThat(storage_DiskEs).isEqualToComparingFieldByField(testStorage_Disk);
    }

    @Test
    @Transactional
    public void deleteStorage_Disk() throws Exception {
        // Initialize the database
        storage_DiskRepository.saveAndFlush(storage_Disk);
        storage_DiskSearchRepository.save(storage_Disk);
        int databaseSizeBeforeDelete = storage_DiskRepository.findAll().size();

        // Get the storage_Disk
        restStorage_DiskMockMvc.perform(delete("/api/storage-disks/{id}", storage_Disk.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean storage_DiskExistsInEs = storage_DiskSearchRepository.exists(storage_Disk.getId());
        assertThat(storage_DiskExistsInEs).isFalse();

        // Validate the database is empty
        List<Storage_Disk> storage_Disks = storage_DiskRepository.findAll();
        assertThat(storage_Disks).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStorage_Disk() throws Exception {
        // Initialize the database
        storage_DiskRepository.saveAndFlush(storage_Disk);
        storage_DiskSearchRepository.save(storage_Disk);

        // Search the storage_Disk
        restStorage_DiskMockMvc.perform(get("/api/_search/storage-disks?query=id:" + storage_Disk.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storage_Disk.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.doubleValue())))
            .andExpect(jsonPath("$.[*].used").value(hasItem(DEFAULT_USED.doubleValue())))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE.doubleValue())))
            .andExpect(jsonPath("$.[*].usePercent").value(hasItem(DEFAULT_USE_PERCENT)))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].reserved").value(hasItem(DEFAULT_RESERVED.booleanValue())));
    }
}
