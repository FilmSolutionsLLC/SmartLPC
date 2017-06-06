package com.fps.web.rest;

import com.fps.SmartLpcApp;
import com.fps.domain.Batch;
import com.fps.repository.BatchRepository;
import com.fps.elastics.search.BatchSearchRepository;

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
 * Test class for the BatchResource REST controller.
 *
 * @see BatchResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartLpcApp.class)
@WebAppConfiguration
@IntegrationTest
public class BatchResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final LocalDate DEFAULT_CREATED_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_COVER_IMAGE_ID = 1L;
    private static final Long UPDATED_COVER_IMAGE_ID = 2L;

    private static final Boolean DEFAULT_IS_ASSET = false;
    private static final Boolean UPDATED_IS_ASSET = true;

    private static final Boolean DEFAULT_IS_HIDDEN = false;
    private static final Boolean UPDATED_IS_HIDDEN = true;

    @Inject
    private BatchRepository batchRepository;

    @Inject
    private BatchSearchRepository batchSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBatchMockMvc;

    private Batch batch;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BatchResource batchResource = new BatchResource();
        ReflectionTestUtils.setField(batchResource, "batchSearchRepository", batchSearchRepository);
        ReflectionTestUtils.setField(batchResource, "batchRepository", batchRepository);
        this.restBatchMockMvc = MockMvcBuilders.standaloneSetup(batchResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        batchSearchRepository.deleteAll();
        batch = new Batch();
        batch.setName(DEFAULT_NAME);
        batch.setCreatedTime(DEFAULT_CREATED_TIME);
        batch.setUpdatedTime(DEFAULT_UPDATED_TIME);
        batch.setCoverImageId(DEFAULT_COVER_IMAGE_ID);
        batch.setIsAsset(DEFAULT_IS_ASSET);
        batch.setIsHidden(DEFAULT_IS_HIDDEN);
    }

    @Test
    @Transactional
    public void createBatch() throws Exception {
        int databaseSizeBeforeCreate = batchRepository.findAll().size();

        // Create the Batch

        restBatchMockMvc.perform(post("/api/batches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(batch)))
                .andExpect(status().isCreated());

        // Validate the Batch in the database
        List<Batch> batches = batchRepository.findAll();
        assertThat(batches).hasSize(databaseSizeBeforeCreate + 1);
        Batch testBatch = batches.get(batches.size() - 1);
        assertThat(testBatch.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBatch.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testBatch.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testBatch.getCoverImageId()).isEqualTo(DEFAULT_COVER_IMAGE_ID);
        assertThat(testBatch.isIsAsset()).isEqualTo(DEFAULT_IS_ASSET);
        assertThat(testBatch.isIsHidden()).isEqualTo(DEFAULT_IS_HIDDEN);

        // Validate the Batch in ElasticSearch
        Batch batchEs = batchSearchRepository.findOne(testBatch.getId());
        assertThat(batchEs).isEqualToComparingFieldByField(testBatch);
    }

    @Test
    @Transactional
    public void getAllBatches() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);

        // Get all the batches
        restBatchMockMvc.perform(get("/api/batches?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(batch.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
                .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())))
                .andExpect(jsonPath("$.[*].coverImageId").value(hasItem(DEFAULT_COVER_IMAGE_ID.intValue())))
                .andExpect(jsonPath("$.[*].isAsset").value(hasItem(DEFAULT_IS_ASSET.booleanValue())))
                .andExpect(jsonPath("$.[*].isHidden").value(hasItem(DEFAULT_IS_HIDDEN.booleanValue())));
    }

    @Test
    @Transactional
    public void getBatch() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);

        // Get the batch
        restBatchMockMvc.perform(get("/api/batches/{id}", batch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(batch.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME.toString()))
            .andExpect(jsonPath("$.updatedTime").value(DEFAULT_UPDATED_TIME.toString()))
            .andExpect(jsonPath("$.coverImageId").value(DEFAULT_COVER_IMAGE_ID.intValue()))
            .andExpect(jsonPath("$.isAsset").value(DEFAULT_IS_ASSET.booleanValue()))
            .andExpect(jsonPath("$.isHidden").value(DEFAULT_IS_HIDDEN.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBatch() throws Exception {
        // Get the batch
        restBatchMockMvc.perform(get("/api/batches/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBatch() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);
        batchSearchRepository.save(batch);
        int databaseSizeBeforeUpdate = batchRepository.findAll().size();

        // Update the batch
        Batch updatedBatch = new Batch();
        updatedBatch.setId(batch.getId());
        updatedBatch.setName(UPDATED_NAME);
        updatedBatch.setCreatedTime(UPDATED_CREATED_TIME);
        updatedBatch.setUpdatedTime(UPDATED_UPDATED_TIME);
        updatedBatch.setCoverImageId(UPDATED_COVER_IMAGE_ID);
        updatedBatch.setIsAsset(UPDATED_IS_ASSET);
        updatedBatch.setIsHidden(UPDATED_IS_HIDDEN);

        restBatchMockMvc.perform(put("/api/batches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBatch)))
                .andExpect(status().isOk());

        // Validate the Batch in the database
        List<Batch> batches = batchRepository.findAll();
        assertThat(batches).hasSize(databaseSizeBeforeUpdate);
        Batch testBatch = batches.get(batches.size() - 1);
        assertThat(testBatch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBatch.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testBatch.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testBatch.getCoverImageId()).isEqualTo(UPDATED_COVER_IMAGE_ID);
        assertThat(testBatch.isIsAsset()).isEqualTo(UPDATED_IS_ASSET);
        assertThat(testBatch.isIsHidden()).isEqualTo(UPDATED_IS_HIDDEN);

        // Validate the Batch in ElasticSearch
        Batch batchEs = batchSearchRepository.findOne(testBatch.getId());
        assertThat(batchEs).isEqualToComparingFieldByField(testBatch);
    }

    @Test
    @Transactional
    public void deleteBatch() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);
        batchSearchRepository.save(batch);
        int databaseSizeBeforeDelete = batchRepository.findAll().size();

        // Get the batch
        restBatchMockMvc.perform(delete("/api/batches/{id}", batch.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean batchExistsInEs = batchSearchRepository.exists(batch.getId());
        assertThat(batchExistsInEs).isFalse();

        // Validate the database is empty
        List<Batch> batches = batchRepository.findAll();
        assertThat(batches).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBatch() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);
        batchSearchRepository.save(batch);

        // Search the batch
        restBatchMockMvc.perform(get("/api/_search/batches?query=id:" + batch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(batch.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].coverImageId").value(hasItem(DEFAULT_COVER_IMAGE_ID.intValue())))
            .andExpect(jsonPath("$.[*].isAsset").value(hasItem(DEFAULT_IS_ASSET.booleanValue())))
            .andExpect(jsonPath("$.[*].isHidden").value(hasItem(DEFAULT_IS_HIDDEN.booleanValue())));
    }
}
