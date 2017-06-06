package com.fps.web.rest;

import com.fps.SmartLpcApp;
import com.fps.domain.RelationType;
import com.fps.repository.RelationTypeRepository;
import com.fps.elastics.search.RelationTypeSearchRepository;

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
 * Test class for the RelationTypeResource REST controller.
 *
 * @see RelationTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartLpcApp.class)
@WebAppConfiguration
@IntegrationTest
public class RelationTypeResourceIntTest {

    private static final String DEFAULT_RELATION = "AAAAA";
    private static final String UPDATED_RELATION = "BBBBB";

    @Inject
    private RelationTypeRepository relationTypeRepository;

    @Inject
    private RelationTypeSearchRepository relationTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRelationTypeMockMvc;

    private RelationType relationType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RelationTypeResource relationTypeResource = new RelationTypeResource();
        ReflectionTestUtils.setField(relationTypeResource, "relationTypeSearchRepository", relationTypeSearchRepository);
        ReflectionTestUtils.setField(relationTypeResource, "relationTypeRepository", relationTypeRepository);
        this.restRelationTypeMockMvc = MockMvcBuilders.standaloneSetup(relationTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        relationTypeSearchRepository.deleteAll();
        relationType = new RelationType();
        relationType.setRelation(DEFAULT_RELATION);
    }

    @Test
    @Transactional
    public void createRelationType() throws Exception {
        int databaseSizeBeforeCreate = relationTypeRepository.findAll().size();

        // Create the RelationType

        restRelationTypeMockMvc.perform(post("/api/relation-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(relationType)))
                .andExpect(status().isCreated());

        // Validate the RelationType in the database
        List<RelationType> relationTypes = relationTypeRepository.findAll();
        assertThat(relationTypes).hasSize(databaseSizeBeforeCreate + 1);
        RelationType testRelationType = relationTypes.get(relationTypes.size() - 1);
        assertThat(testRelationType.getRelation()).isEqualTo(DEFAULT_RELATION);

        // Validate the RelationType in ElasticSearch
        RelationType relationTypeEs = relationTypeSearchRepository.findOne(testRelationType.getId());
        assertThat(relationTypeEs).isEqualToComparingFieldByField(testRelationType);
    }

    @Test
    @Transactional
    public void getAllRelationTypes() throws Exception {
        // Initialize the database
        relationTypeRepository.saveAndFlush(relationType);

        // Get all the relationTypes
        restRelationTypeMockMvc.perform(get("/api/relation-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(relationType.getId().intValue())))
                .andExpect(jsonPath("$.[*].relation").value(hasItem(DEFAULT_RELATION.toString())));
    }

    @Test
    @Transactional
    public void getRelationType() throws Exception {
        // Initialize the database
        relationTypeRepository.saveAndFlush(relationType);

        // Get the relationType
        restRelationTypeMockMvc.perform(get("/api/relation-types/{id}", relationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(relationType.getId().intValue()))
            .andExpect(jsonPath("$.relation").value(DEFAULT_RELATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRelationType() throws Exception {
        // Get the relationType
        restRelationTypeMockMvc.perform(get("/api/relation-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRelationType() throws Exception {
        // Initialize the database
        relationTypeRepository.saveAndFlush(relationType);
        relationTypeSearchRepository.save(relationType);
        int databaseSizeBeforeUpdate = relationTypeRepository.findAll().size();

        // Update the relationType
        RelationType updatedRelationType = new RelationType();
        updatedRelationType.setId(relationType.getId());
        updatedRelationType.setRelation(UPDATED_RELATION);

        restRelationTypeMockMvc.perform(put("/api/relation-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRelationType)))
                .andExpect(status().isOk());

        // Validate the RelationType in the database
        List<RelationType> relationTypes = relationTypeRepository.findAll();
        assertThat(relationTypes).hasSize(databaseSizeBeforeUpdate);
        RelationType testRelationType = relationTypes.get(relationTypes.size() - 1);
        assertThat(testRelationType.getRelation()).isEqualTo(UPDATED_RELATION);

        // Validate the RelationType in ElasticSearch
        RelationType relationTypeEs = relationTypeSearchRepository.findOne(testRelationType.getId());
        assertThat(relationTypeEs).isEqualToComparingFieldByField(testRelationType);
    }

    @Test
    @Transactional
    public void deleteRelationType() throws Exception {
        // Initialize the database
        relationTypeRepository.saveAndFlush(relationType);
        relationTypeSearchRepository.save(relationType);
        int databaseSizeBeforeDelete = relationTypeRepository.findAll().size();

        // Get the relationType
        restRelationTypeMockMvc.perform(delete("/api/relation-types/{id}", relationType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean relationTypeExistsInEs = relationTypeSearchRepository.exists(relationType.getId());
        assertThat(relationTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<RelationType> relationTypes = relationTypeRepository.findAll();
        assertThat(relationTypes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRelationType() throws Exception {
        // Initialize the database
        relationTypeRepository.saveAndFlush(relationType);
        relationTypeSearchRepository.save(relationType);

        // Search the relationType
        restRelationTypeMockMvc.perform(get("/api/_search/relation-types?query=id:" + relationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].relation").value(hasItem(DEFAULT_RELATION.toString())));
    }
}
