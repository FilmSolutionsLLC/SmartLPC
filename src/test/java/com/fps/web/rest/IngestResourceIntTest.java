package com.fps.web.rest;

import com.fps.SmartLpcApp;
import com.fps.domain.Ingest;
import com.fps.repository.IngestRepository;
import com.fps.service.IngestService;
import com.fps.elastics.search.IngestSearchRepository;

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
 * Test class for the IngestResource REST controller.
 *
 * @see IngestResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartLpcApp.class)
@WebAppConfiguration
@IntegrationTest
public class IngestResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_INGEST_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_INGEST_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_INGEST_START_TIME_STR = dateTimeFormatter.format(DEFAULT_INGEST_START_TIME);

    private static final ZonedDateTime DEFAULT_INGEST_COMPLETED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_INGEST_COMPLETED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_INGEST_COMPLETED_TIME_STR = dateTimeFormatter.format(DEFAULT_INGEST_COMPLETED_TIME);

    private static final Integer DEFAULT_TOTAL_IMAGES = 1;
    private static final Integer UPDATED_TOTAL_IMAGES = 2;

    private static final Double DEFAULT_TOTAL_DONE = 1D;
    private static final Double UPDATED_TOTAL_DONE = 2D;

    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    @Inject
    private IngestRepository ingestRepository;

    @Inject
    private IngestService ingestService;

    @Inject
    private IngestSearchRepository ingestSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restIngestMockMvc;

    private Ingest ingest;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IngestResource ingestResource = new IngestResource();
        ReflectionTestUtils.setField(ingestResource, "ingestService", ingestService);
        this.restIngestMockMvc = MockMvcBuilders.standaloneSetup(ingestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        ingestSearchRepository.deleteAll();
        ingest = new Ingest();
        ingest.setIngestStartTime(DEFAULT_INGEST_START_TIME);
        ingest.setIngestCompletedTime(DEFAULT_INGEST_COMPLETED_TIME);
        ingest.setTotalImages(DEFAULT_TOTAL_IMAGES);
        ingest.setTotalDone(DEFAULT_TOTAL_DONE);
        ingest.setCompleted(DEFAULT_COMPLETED);
    }

    @Test
    @Transactional
    public void createIngest() throws Exception {
        int databaseSizeBeforeCreate = ingestRepository.findAll().size();

        // Create the Ingest

        restIngestMockMvc.perform(post("/api/ingests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ingest)))
                .andExpect(status().isCreated());

        // Validate the Ingest in the database
        List<Ingest> ingests = ingestRepository.findAll();
        assertThat(ingests).hasSize(databaseSizeBeforeCreate + 1);
        Ingest testIngest = ingests.get(ingests.size() - 1);
        assertThat(testIngest.getIngestStartTime()).isEqualTo(DEFAULT_INGEST_START_TIME);
        assertThat(testIngest.getIngestCompletedTime()).isEqualTo(DEFAULT_INGEST_COMPLETED_TIME);
        assertThat(testIngest.getTotalImages()).isEqualTo(DEFAULT_TOTAL_IMAGES);
        assertThat(testIngest.getTotalDone()).isEqualTo(DEFAULT_TOTAL_DONE);
        assertThat(testIngest.isCompleted()).isEqualTo(DEFAULT_COMPLETED);

        // Validate the Ingest in ElasticSearch
        Ingest ingestEs = ingestSearchRepository.findOne(testIngest.getId());
        assertThat(ingestEs).isEqualToComparingFieldByField(testIngest);
    }

    @Test
    @Transactional
    public void getAllIngests() throws Exception {
        // Initialize the database
        ingestRepository.saveAndFlush(ingest);

        // Get all the ingests
        restIngestMockMvc.perform(get("/api/ingests?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ingest.getId().intValue())))
                .andExpect(jsonPath("$.[*].ingestStartTime").value(hasItem(DEFAULT_INGEST_START_TIME_STR)))
                .andExpect(jsonPath("$.[*].ingestCompletedTime").value(hasItem(DEFAULT_INGEST_COMPLETED_TIME_STR)))
                .andExpect(jsonPath("$.[*].totalImages").value(hasItem(DEFAULT_TOTAL_IMAGES)))
                .andExpect(jsonPath("$.[*].totalDone").value(hasItem(DEFAULT_TOTAL_DONE.doubleValue())))
                .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getIngest() throws Exception {
        // Initialize the database
        ingestRepository.saveAndFlush(ingest);

        // Get the ingest
        restIngestMockMvc.perform(get("/api/ingests/{id}", ingest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ingest.getId().intValue()))
            .andExpect(jsonPath("$.ingestStartTime").value(DEFAULT_INGEST_START_TIME_STR))
            .andExpect(jsonPath("$.ingestCompletedTime").value(DEFAULT_INGEST_COMPLETED_TIME_STR))
            .andExpect(jsonPath("$.totalImages").value(DEFAULT_TOTAL_IMAGES))
            .andExpect(jsonPath("$.totalDone").value(DEFAULT_TOTAL_DONE.doubleValue()))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingIngest() throws Exception {
        // Get the ingest
        restIngestMockMvc.perform(get("/api/ingests/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIngest() throws Exception {
        // Initialize the database
        ingestService.save(ingest);

        int databaseSizeBeforeUpdate = ingestRepository.findAll().size();

        // Update the ingest
        Ingest updatedIngest = new Ingest();
        updatedIngest.setId(ingest.getId());
        updatedIngest.setIngestStartTime(UPDATED_INGEST_START_TIME);
        updatedIngest.setIngestCompletedTime(UPDATED_INGEST_COMPLETED_TIME);
        updatedIngest.setTotalImages(UPDATED_TOTAL_IMAGES);
        updatedIngest.setTotalDone(UPDATED_TOTAL_DONE);
        updatedIngest.setCompleted(UPDATED_COMPLETED);

        restIngestMockMvc.perform(put("/api/ingests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedIngest)))
                .andExpect(status().isOk());

        // Validate the Ingest in the database
        List<Ingest> ingests = ingestRepository.findAll();
        assertThat(ingests).hasSize(databaseSizeBeforeUpdate);
        Ingest testIngest = ingests.get(ingests.size() - 1);
        assertThat(testIngest.getIngestStartTime()).isEqualTo(UPDATED_INGEST_START_TIME);
        assertThat(testIngest.getIngestCompletedTime()).isEqualTo(UPDATED_INGEST_COMPLETED_TIME);
        assertThat(testIngest.getTotalImages()).isEqualTo(UPDATED_TOTAL_IMAGES);
        assertThat(testIngest.getTotalDone()).isEqualTo(UPDATED_TOTAL_DONE);
        assertThat(testIngest.isCompleted()).isEqualTo(UPDATED_COMPLETED);

        // Validate the Ingest in ElasticSearch
        Ingest ingestEs = ingestSearchRepository.findOne(testIngest.getId());
        assertThat(ingestEs).isEqualToComparingFieldByField(testIngest);
    }

    @Test
    @Transactional
    public void deleteIngest() throws Exception {
        // Initialize the database
        ingestService.save(ingest);

        int databaseSizeBeforeDelete = ingestRepository.findAll().size();

        // Get the ingest
        restIngestMockMvc.perform(delete("/api/ingests/{id}", ingest.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean ingestExistsInEs = ingestSearchRepository.exists(ingest.getId());
        assertThat(ingestExistsInEs).isFalse();

        // Validate the database is empty
        List<Ingest> ingests = ingestRepository.findAll();
        assertThat(ingests).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchIngest() throws Exception {
        // Initialize the database
        ingestService.save(ingest);

        // Search the ingest
        restIngestMockMvc.perform(get("/api/_search/ingests?query=id:" + ingest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingest.getId().intValue())))
            .andExpect(jsonPath("$.[*].ingestStartTime").value(hasItem(DEFAULT_INGEST_START_TIME_STR)))
            .andExpect(jsonPath("$.[*].ingestCompletedTime").value(hasItem(DEFAULT_INGEST_COMPLETED_TIME_STR)))
            .andExpect(jsonPath("$.[*].totalImages").value(hasItem(DEFAULT_TOTAL_IMAGES)))
            .andExpect(jsonPath("$.[*].totalDone").value(hasItem(DEFAULT_TOTAL_DONE.doubleValue())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())));
    }
}
