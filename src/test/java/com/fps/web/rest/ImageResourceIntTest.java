package com.fps.web.rest;

import com.fps.SmartLpcApp;
import com.fps.domain.Image;
import com.fps.repository.ImageRepository;
import com.fps.service.ImageService;
import com.fps.elastics.search.ImageSearchRepository;

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
 * Test class for the ImageResource REST controller.
 *
 * @see ImageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartLpcApp.class)
@WebAppConfiguration
@IntegrationTest
public class ImageResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Long DEFAULT_CAPTION_ID = 1L;
    private static final Long UPDATED_CAPTION_ID = 2L;
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_COMMENT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_COMMENT_DESCRIPTION = "BBBBB";

    private static final ZonedDateTime DEFAULT_RELEASE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_RELEASE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_RELEASE_TIME_STR = dateTimeFormatter.format(DEFAULT_RELEASE_TIME);

    private static final ZonedDateTime DEFAULT_INGEST_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_INGEST_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_INGEST_TIME_STR = dateTimeFormatter.format(DEFAULT_INGEST_TIME);

    private static final ZonedDateTime DEFAULT_QUICKPICK_SELECTED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_QUICKPICK_SELECTED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_QUICKPICK_SELECTED_TIME_STR = dateTimeFormatter.format(DEFAULT_QUICKPICK_SELECTED_TIME);

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_TIME_STR = dateTimeFormatter.format(DEFAULT_CREATED_TIME);

    private static final ZonedDateTime DEFAULT_UPDATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_UPDATED_TIME_STR = dateTimeFormatter.format(DEFAULT_UPDATED_TIME);
    private static final String DEFAULT_PHOTOGRAPHER = "AAAAA";
    private static final String UPDATED_PHOTOGRAPHER = "BBBBB";

    private static final Boolean DEFAULT_VIDEO = false;
    private static final Boolean UPDATED_VIDEO = true;

    private static final Boolean DEFAULT_HIDDEN = false;
    private static final Boolean UPDATED_HIDDEN = true;

    private static final Boolean DEFAULT_WEB_UPLOAD = false;
    private static final Boolean UPDATED_WEB_UPLOAD = true;

    @Inject
    private ImageRepository imageRepository;

    @Inject
    private ImageService imageService;

    @Inject
    private ImageSearchRepository imageSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restImageMockMvc;

    private Image image;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ImageResource imageResource = new ImageResource();
        ReflectionTestUtils.setField(imageResource, "imageService", imageService);
        this.restImageMockMvc = MockMvcBuilders.standaloneSetup(imageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        imageSearchRepository.deleteAll();
        image = new Image();
        image.setCaptionId(DEFAULT_CAPTION_ID);
        image.setName(DEFAULT_NAME);
        image.setCommentDescription(DEFAULT_COMMENT_DESCRIPTION);
        image.setReleaseTime(DEFAULT_RELEASE_TIME);
        image.setIngestTime(DEFAULT_INGEST_TIME);
        image.setQuickpickSelectedTime(DEFAULT_QUICKPICK_SELECTED_TIME);
        image.setCreatedTime(DEFAULT_CREATED_TIME);
        image.setUpdatedTime(DEFAULT_UPDATED_TIME);
        image.setPhotographer(DEFAULT_PHOTOGRAPHER);
        image.setVideo(DEFAULT_VIDEO);
        image.setHidden(DEFAULT_HIDDEN);
        image.setWebUpload(DEFAULT_WEB_UPLOAD);
    }

    @Test
    @Transactional
    public void createImage() throws Exception {
        int databaseSizeBeforeCreate = imageRepository.findAll().size();

        // Create the Image

        restImageMockMvc.perform(post("/api/images")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(image)))
                .andExpect(status().isCreated());

        // Validate the Image in the database
        List<Image> images = imageRepository.findAll();
        assertThat(images).hasSize(databaseSizeBeforeCreate + 1);
        Image testImage = images.get(images.size() - 1);
        assertThat(testImage.getCaptionId()).isEqualTo(DEFAULT_CAPTION_ID);
        assertThat(testImage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testImage.getCommentDescription()).isEqualTo(DEFAULT_COMMENT_DESCRIPTION);
        assertThat(testImage.getReleaseTime()).isEqualTo(DEFAULT_RELEASE_TIME);
        assertThat(testImage.getIngestTime()).isEqualTo(DEFAULT_INGEST_TIME);
        assertThat(testImage.getQuickpickSelectedTime()).isEqualTo(DEFAULT_QUICKPICK_SELECTED_TIME);
        assertThat(testImage.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testImage.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testImage.getPhotographer()).isEqualTo(DEFAULT_PHOTOGRAPHER);
        assertThat(testImage.isVideo()).isEqualTo(DEFAULT_VIDEO);
        assertThat(testImage.isHidden()).isEqualTo(DEFAULT_HIDDEN);
        assertThat(testImage.isWebUpload()).isEqualTo(DEFAULT_WEB_UPLOAD);

        // Validate the Image in ElasticSearch
        Image imageEs = imageSearchRepository.findOne(testImage.getId());
        assertThat(imageEs).isEqualToComparingFieldByField(testImage);
    }

    @Test
    @Transactional
    public void getAllImages() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the images
        restImageMockMvc.perform(get("/api/images?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(image.getId().intValue())))
                .andExpect(jsonPath("$.[*].captionId").value(hasItem(DEFAULT_CAPTION_ID.intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].commentDescription").value(hasItem(DEFAULT_COMMENT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].releaseTime").value(hasItem(DEFAULT_RELEASE_TIME_STR)))
                .andExpect(jsonPath("$.[*].ingestTime").value(hasItem(DEFAULT_INGEST_TIME_STR)))
                .andExpect(jsonPath("$.[*].quickpickSelectedTime").value(hasItem(DEFAULT_QUICKPICK_SELECTED_TIME_STR)))
                .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME_STR)))
                .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME_STR)))
                .andExpect(jsonPath("$.[*].photographer").value(hasItem(DEFAULT_PHOTOGRAPHER.toString())))
                .andExpect(jsonPath("$.[*].video").value(hasItem(DEFAULT_VIDEO.booleanValue())))
                .andExpect(jsonPath("$.[*].hidden").value(hasItem(DEFAULT_HIDDEN.booleanValue())))
                .andExpect(jsonPath("$.[*].webUpload").value(hasItem(DEFAULT_WEB_UPLOAD.booleanValue())));
    }

    @Test
    @Transactional
    public void getImage() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get the image
        restImageMockMvc.perform(get("/api/images/{id}", image.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(image.getId().intValue()))
            .andExpect(jsonPath("$.captionId").value(DEFAULT_CAPTION_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.commentDescription").value(DEFAULT_COMMENT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.releaseTime").value(DEFAULT_RELEASE_TIME_STR))
            .andExpect(jsonPath("$.ingestTime").value(DEFAULT_INGEST_TIME_STR))
            .andExpect(jsonPath("$.quickpickSelectedTime").value(DEFAULT_QUICKPICK_SELECTED_TIME_STR))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME_STR))
            .andExpect(jsonPath("$.updatedTime").value(DEFAULT_UPDATED_TIME_STR))
            .andExpect(jsonPath("$.photographer").value(DEFAULT_PHOTOGRAPHER.toString()))
            .andExpect(jsonPath("$.video").value(DEFAULT_VIDEO.booleanValue()))
            .andExpect(jsonPath("$.hidden").value(DEFAULT_HIDDEN.booleanValue()))
            .andExpect(jsonPath("$.webUpload").value(DEFAULT_WEB_UPLOAD.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingImage() throws Exception {
        // Get the image
        restImageMockMvc.perform(get("/api/images/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateImage() throws Exception {
        // Initialize the database
        imageService.save(image);

        int databaseSizeBeforeUpdate = imageRepository.findAll().size();

        // Update the image
        Image updatedImage = new Image();
        updatedImage.setId(image.getId());
        updatedImage.setCaptionId(UPDATED_CAPTION_ID);
        updatedImage.setName(UPDATED_NAME);
        updatedImage.setCommentDescription(UPDATED_COMMENT_DESCRIPTION);
        updatedImage.setReleaseTime(UPDATED_RELEASE_TIME);
        updatedImage.setIngestTime(UPDATED_INGEST_TIME);
        updatedImage.setQuickpickSelectedTime(UPDATED_QUICKPICK_SELECTED_TIME);
        updatedImage.setCreatedTime(UPDATED_CREATED_TIME);
        updatedImage.setUpdatedTime(UPDATED_UPDATED_TIME);
        updatedImage.setPhotographer(UPDATED_PHOTOGRAPHER);
        updatedImage.setVideo(UPDATED_VIDEO);
        updatedImage.setHidden(UPDATED_HIDDEN);
        updatedImage.setWebUpload(UPDATED_WEB_UPLOAD);

        restImageMockMvc.perform(put("/api/images")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedImage)))
                .andExpect(status().isOk());

        // Validate the Image in the database
        List<Image> images = imageRepository.findAll();
        assertThat(images).hasSize(databaseSizeBeforeUpdate);
        Image testImage = images.get(images.size() - 1);
        assertThat(testImage.getCaptionId()).isEqualTo(UPDATED_CAPTION_ID);
        assertThat(testImage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testImage.getCommentDescription()).isEqualTo(UPDATED_COMMENT_DESCRIPTION);
        assertThat(testImage.getReleaseTime()).isEqualTo(UPDATED_RELEASE_TIME);
        assertThat(testImage.getIngestTime()).isEqualTo(UPDATED_INGEST_TIME);
        assertThat(testImage.getQuickpickSelectedTime()).isEqualTo(UPDATED_QUICKPICK_SELECTED_TIME);
        assertThat(testImage.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testImage.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testImage.getPhotographer()).isEqualTo(UPDATED_PHOTOGRAPHER);
        assertThat(testImage.isVideo()).isEqualTo(UPDATED_VIDEO);
        assertThat(testImage.isHidden()).isEqualTo(UPDATED_HIDDEN);
        assertThat(testImage.isWebUpload()).isEqualTo(UPDATED_WEB_UPLOAD);

        // Validate the Image in ElasticSearch
        Image imageEs = imageSearchRepository.findOne(testImage.getId());
        assertThat(imageEs).isEqualToComparingFieldByField(testImage);
    }

    @Test
    @Transactional
    public void deleteImage() throws Exception {
        // Initialize the database
        imageService.save(image);

        int databaseSizeBeforeDelete = imageRepository.findAll().size();

        // Get the image
        restImageMockMvc.perform(delete("/api/images/{id}", image.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean imageExistsInEs = imageSearchRepository.exists(image.getId());
        assertThat(imageExistsInEs).isFalse();

        // Validate the database is empty
        List<Image> images = imageRepository.findAll();
        assertThat(images).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchImage() throws Exception {
        // Initialize the database
        imageService.save(image);

        // Search the image
        restImageMockMvc.perform(get("/api/_search/images?query=id:" + image.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(image.getId().intValue())))
            .andExpect(jsonPath("$.[*].captionId").value(hasItem(DEFAULT_CAPTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].commentDescription").value(hasItem(DEFAULT_COMMENT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].releaseTime").value(hasItem(DEFAULT_RELEASE_TIME_STR)))
            .andExpect(jsonPath("$.[*].ingestTime").value(hasItem(DEFAULT_INGEST_TIME_STR)))
            .andExpect(jsonPath("$.[*].quickpickSelectedTime").value(hasItem(DEFAULT_QUICKPICK_SELECTED_TIME_STR)))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME_STR)))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME_STR)))
            .andExpect(jsonPath("$.[*].photographer").value(hasItem(DEFAULT_PHOTOGRAPHER.toString())))
            .andExpect(jsonPath("$.[*].video").value(hasItem(DEFAULT_VIDEO.booleanValue())))
            .andExpect(jsonPath("$.[*].hidden").value(hasItem(DEFAULT_HIDDEN.booleanValue())))
            .andExpect(jsonPath("$.[*].webUpload").value(hasItem(DEFAULT_WEB_UPLOAD.booleanValue())));
    }
}
