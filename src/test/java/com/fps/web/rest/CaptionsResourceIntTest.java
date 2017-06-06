package com.fps.web.rest;

import com.fps.SmartLpcApp;
import com.fps.domain.Captions;
import com.fps.repository.CaptionsRepository;
import com.fps.elastics.search.CaptionsSearchRepository;

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
 * Test class for the CaptionsResource REST controller.
 *
 * @see CaptionsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartLpcApp.class)
@WebAppConfiguration
@IntegrationTest
public class CaptionsResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_CAPTION_TEXT = "AAAAA";
    private static final String UPDATED_CAPTION_TEXT = "BBBBB";

    private static final ZonedDateTime DEFAULT_CAPTION_DTTM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CAPTION_DTTM = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CAPTION_DTTM_STR = dateTimeFormatter.format(DEFAULT_CAPTION_DTTM);

    @Inject
    private CaptionsRepository captionsRepository;

    @Inject
    private CaptionsSearchRepository captionsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCaptionsMockMvc;

    private Captions captions;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CaptionsResource captionsResource = new CaptionsResource();
        ReflectionTestUtils.setField(captionsResource, "captionsSearchRepository", captionsSearchRepository);
        ReflectionTestUtils.setField(captionsResource, "captionsRepository", captionsRepository);
        this.restCaptionsMockMvc = MockMvcBuilders.standaloneSetup(captionsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        captionsSearchRepository.deleteAll();
        captions = new Captions();
        captions.setCaptionText(DEFAULT_CAPTION_TEXT);
        captions.setCaptionDttm(DEFAULT_CAPTION_DTTM);
    }

    @Test
    @Transactional
    public void createCaptions() throws Exception {
        int databaseSizeBeforeCreate = captionsRepository.findAll().size();

        // Create the Captions

        restCaptionsMockMvc.perform(post("/api/captions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(captions)))
                .andExpect(status().isCreated());

        // Validate the Captions in the database
        List<Captions> captions = captionsRepository.findAll();
        assertThat(captions).hasSize(databaseSizeBeforeCreate + 1);
        Captions testCaptions = captions.get(captions.size() - 1);
        assertThat(testCaptions.getCaptionText()).isEqualTo(DEFAULT_CAPTION_TEXT);
        assertThat(testCaptions.getCaptionDttm()).isEqualTo(DEFAULT_CAPTION_DTTM);

        // Validate the Captions in ElasticSearch
        Captions captionsEs = captionsSearchRepository.findOne(testCaptions.getId());
        assertThat(captionsEs).isEqualToComparingFieldByField(testCaptions);
    }

    @Test
    @Transactional
    public void getAllCaptions() throws Exception {
        // Initialize the database
        captionsRepository.saveAndFlush(captions);

        // Get all the captions
        restCaptionsMockMvc.perform(get("/api/captions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(captions.getId().intValue())))
                .andExpect(jsonPath("$.[*].captionText").value(hasItem(DEFAULT_CAPTION_TEXT.toString())))
                .andExpect(jsonPath("$.[*].captionDttm").value(hasItem(DEFAULT_CAPTION_DTTM_STR)));
    }

    @Test
    @Transactional
    public void getCaptions() throws Exception {
        // Initialize the database
        captionsRepository.saveAndFlush(captions);

        // Get the captions
        restCaptionsMockMvc.perform(get("/api/captions/{id}", captions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(captions.getId().intValue()))
            .andExpect(jsonPath("$.captionText").value(DEFAULT_CAPTION_TEXT.toString()))
            .andExpect(jsonPath("$.captionDttm").value(DEFAULT_CAPTION_DTTM_STR));
    }

    @Test
    @Transactional
    public void getNonExistingCaptions() throws Exception {
        // Get the captions
        restCaptionsMockMvc.perform(get("/api/captions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCaptions() throws Exception {
        // Initialize the database
        captionsRepository.saveAndFlush(captions);
        captionsSearchRepository.save(captions);
        int databaseSizeBeforeUpdate = captionsRepository.findAll().size();

        // Update the captions
        Captions updatedCaptions = new Captions();
        updatedCaptions.setId(captions.getId());
        updatedCaptions.setCaptionText(UPDATED_CAPTION_TEXT);
        updatedCaptions.setCaptionDttm(UPDATED_CAPTION_DTTM);

        restCaptionsMockMvc.perform(put("/api/captions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCaptions)))
                .andExpect(status().isOk());

        // Validate the Captions in the database
        List<Captions> captions = captionsRepository.findAll();
        assertThat(captions).hasSize(databaseSizeBeforeUpdate);
        Captions testCaptions = captions.get(captions.size() - 1);
        assertThat(testCaptions.getCaptionText()).isEqualTo(UPDATED_CAPTION_TEXT);
        assertThat(testCaptions.getCaptionDttm()).isEqualTo(UPDATED_CAPTION_DTTM);

        // Validate the Captions in ElasticSearch
        Captions captionsEs = captionsSearchRepository.findOne(testCaptions.getId());
        assertThat(captionsEs).isEqualToComparingFieldByField(testCaptions);
    }

    @Test
    @Transactional
    public void deleteCaptions() throws Exception {
        // Initialize the database
        captionsRepository.saveAndFlush(captions);
        captionsSearchRepository.save(captions);
        int databaseSizeBeforeDelete = captionsRepository.findAll().size();

        // Get the captions
        restCaptionsMockMvc.perform(delete("/api/captions/{id}", captions.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean captionsExistsInEs = captionsSearchRepository.exists(captions.getId());
        assertThat(captionsExistsInEs).isFalse();

        // Validate the database is empty
        List<Captions> captions = captionsRepository.findAll();
        assertThat(captions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCaptions() throws Exception {
        // Initialize the database
        captionsRepository.saveAndFlush(captions);
        captionsSearchRepository.save(captions);

        // Search the captions
        restCaptionsMockMvc.perform(get("/api/_search/captions?query=id:" + captions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(captions.getId().intValue())))
            .andExpect(jsonPath("$.[*].captionText").value(hasItem(DEFAULT_CAPTION_TEXT.toString())))
            .andExpect(jsonPath("$.[*].captionDttm").value(hasItem(DEFAULT_CAPTION_DTTM_STR)));
    }
}
