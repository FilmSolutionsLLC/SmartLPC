package com.fps.web.rest;

import com.fps.SmartLpcApp;
import com.fps.domain.ProjectRoles;
import com.fps.repository.ProjectRolesRepository;
import com.fps.elastics.search.ProjectRolesSearchRepository;

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
 * Test class for the ProjectRolesResource REST controller.
 *
 * @see ProjectRolesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartLpcApp.class)
@WebAppConfiguration
@IntegrationTest
public class ProjectRolesResourceIntTest {


    private static final Float DEFAULT_SOLO_KILL_PCT = 1F;
    private static final Float UPDATED_SOLO_KILL_PCT = 2F;

    private static final Float DEFAULT_GROUP_KILL_PCT = 1F;
    private static final Float UPDATED_GROUP_KILL_PCT = 2F;

    private static final LocalDate DEFAULT_MINI_FULL_DT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MINI_FULL_DT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FULL_FINAL_DT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FULL_FINAL_DT = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_DISABLED = false;
    private static final Boolean UPDATED_DISABLED = true;
    private static final String DEFAULT_CHARACTER_NAME = "AAAAA";
    private static final String UPDATED_CHARACTER_NAME = "BBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_DAYS_WORKING = 1;
    private static final Integer UPDATED_DAYS_WORKING = 2;

    private static final Boolean DEFAULT_EXC_SOLOGROUP = false;
    private static final Boolean UPDATED_EXC_SOLOGROUP = true;
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";
    private static final String DEFAULT_TAG_NAME = "AAAAA";
    private static final String UPDATED_TAG_NAME = "BBBBB";
    private static final String DEFAULT_HOTKEY_VALUE = "AAAAA";
    private static final String UPDATED_HOTKEY_VALUE = "BBBBB";

    private static final LocalDate DEFAULT_EXPIRE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_TERTIARY_KILL_PCT = 1F;
    private static final Float UPDATED_TERTIARY_KILL_PCT = 2F;

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private ProjectRolesRepository projectRolesRepository;

    @Inject
    private ProjectRolesSearchRepository projectRolesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProjectRolesMockMvc;

    private ProjectRoles projectRoles;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectRolesResource projectRolesResource = new ProjectRolesResource();
        ReflectionTestUtils.setField(projectRolesResource, "projectRolesSearchRepository", projectRolesSearchRepository);
        ReflectionTestUtils.setField(projectRolesResource, "projectRolesRepository", projectRolesRepository);
        this.restProjectRolesMockMvc = MockMvcBuilders.standaloneSetup(projectRolesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        projectRolesSearchRepository.deleteAll();
        projectRoles = new ProjectRoles();
        projectRoles.setSoloKillPct(DEFAULT_SOLO_KILL_PCT);
        projectRoles.setGroupKillPct(DEFAULT_GROUP_KILL_PCT);
        projectRoles.setMiniFullDt(DEFAULT_MINI_FULL_DT);
        projectRoles.setFullFinalDt(DEFAULT_FULL_FINAL_DT);
        projectRoles.setDisabled(DEFAULT_DISABLED);
        projectRoles.setCharacterName(DEFAULT_CHARACTER_NAME);
        projectRoles.setStartDate(DEFAULT_START_DATE);
        projectRoles.setDaysWorking(DEFAULT_DAYS_WORKING);
        projectRoles.setExcSologroup(DEFAULT_EXC_SOLOGROUP);
        projectRoles.setNotes(DEFAULT_NOTES);
        projectRoles.setTagName(DEFAULT_TAG_NAME);
        projectRoles.setHotkeyValue(DEFAULT_HOTKEY_VALUE);
        projectRoles.setExpireDate(DEFAULT_EXPIRE_DATE);
        projectRoles.setTertiaryKillPct(DEFAULT_TERTIARY_KILL_PCT);
        projectRoles.setCreatedDate(DEFAULT_CREATED_DATE);
        projectRoles.setUpdatedDate(DEFAULT_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createProjectRoles() throws Exception {
        int databaseSizeBeforeCreate = projectRolesRepository.findAll().size();

        // Create the ProjectRoles

        restProjectRolesMockMvc.perform(post("/api/project-roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectRoles)))
                .andExpect(status().isCreated());

        // Validate the ProjectRoles in the database
        List<ProjectRoles> projectRoles = projectRolesRepository.findAll();
        assertThat(projectRoles).hasSize(databaseSizeBeforeCreate + 1);
        ProjectRoles testProjectRoles = projectRoles.get(projectRoles.size() - 1);
        assertThat(testProjectRoles.getSoloKillPct()).isEqualTo(DEFAULT_SOLO_KILL_PCT);
        assertThat(testProjectRoles.getGroupKillPct()).isEqualTo(DEFAULT_GROUP_KILL_PCT);
        assertThat(testProjectRoles.getMiniFullDt()).isEqualTo(DEFAULT_MINI_FULL_DT);
        assertThat(testProjectRoles.getFullFinalDt()).isEqualTo(DEFAULT_FULL_FINAL_DT);
        assertThat(testProjectRoles.isDisabled()).isEqualTo(DEFAULT_DISABLED);
        assertThat(testProjectRoles.getCharacterName()).isEqualTo(DEFAULT_CHARACTER_NAME);
        assertThat(testProjectRoles.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testProjectRoles.getDaysWorking()).isEqualTo(DEFAULT_DAYS_WORKING);
        assertThat(testProjectRoles.isExcSologroup()).isEqualTo(DEFAULT_EXC_SOLOGROUP);
        assertThat(testProjectRoles.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testProjectRoles.getTagName()).isEqualTo(DEFAULT_TAG_NAME);
        assertThat(testProjectRoles.getHotkeyValue()).isEqualTo(DEFAULT_HOTKEY_VALUE);
        assertThat(testProjectRoles.getExpireDate()).isEqualTo(DEFAULT_EXPIRE_DATE);
        assertThat(testProjectRoles.getTertiaryKillPct()).isEqualTo(DEFAULT_TERTIARY_KILL_PCT);
        assertThat(testProjectRoles.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProjectRoles.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);

        // Validate the ProjectRoles in ElasticSearch
        ProjectRoles projectRolesEs = projectRolesSearchRepository.findOne(testProjectRoles.getId());
        assertThat(projectRolesEs).isEqualToComparingFieldByField(testProjectRoles);
    }

    @Test
    @Transactional
    public void getAllProjectRoles() throws Exception {
        // Initialize the database
        projectRolesRepository.saveAndFlush(projectRoles);

        // Get all the projectRoles
        restProjectRolesMockMvc.perform(get("/api/project-roles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(projectRoles.getId().intValue())))
                .andExpect(jsonPath("$.[*].soloKillPct").value(hasItem(DEFAULT_SOLO_KILL_PCT.doubleValue())))
                .andExpect(jsonPath("$.[*].groupKillPct").value(hasItem(DEFAULT_GROUP_KILL_PCT.doubleValue())))
                .andExpect(jsonPath("$.[*].miniFullDt").value(hasItem(DEFAULT_MINI_FULL_DT.toString())))
                .andExpect(jsonPath("$.[*].fullFinalDt").value(hasItem(DEFAULT_FULL_FINAL_DT.toString())))
                .andExpect(jsonPath("$.[*].disabled").value(hasItem(DEFAULT_DISABLED.booleanValue())))
                .andExpect(jsonPath("$.[*].characterName").value(hasItem(DEFAULT_CHARACTER_NAME.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].daysWorking").value(hasItem(DEFAULT_DAYS_WORKING)))
                .andExpect(jsonPath("$.[*].excSologroup").value(hasItem(DEFAULT_EXC_SOLOGROUP.booleanValue())))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
                .andExpect(jsonPath("$.[*].tagName").value(hasItem(DEFAULT_TAG_NAME.toString())))
                .andExpect(jsonPath("$.[*].hotkeyValue").value(hasItem(DEFAULT_HOTKEY_VALUE.toString())))
                .andExpect(jsonPath("$.[*].expireDate").value(hasItem(DEFAULT_EXPIRE_DATE.toString())))
                .andExpect(jsonPath("$.[*].tertiaryKillPct").value(hasItem(DEFAULT_TERTIARY_KILL_PCT.doubleValue())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
                .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())));
    }

    @Test
    @Transactional
    public void getProjectRoles() throws Exception {
        // Initialize the database
        projectRolesRepository.saveAndFlush(projectRoles);

        // Get the projectRoles
        restProjectRolesMockMvc.perform(get("/api/project-roles/{id}", projectRoles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(projectRoles.getId().intValue()))
            .andExpect(jsonPath("$.soloKillPct").value(DEFAULT_SOLO_KILL_PCT.doubleValue()))
            .andExpect(jsonPath("$.groupKillPct").value(DEFAULT_GROUP_KILL_PCT.doubleValue()))
            .andExpect(jsonPath("$.miniFullDt").value(DEFAULT_MINI_FULL_DT.toString()))
            .andExpect(jsonPath("$.fullFinalDt").value(DEFAULT_FULL_FINAL_DT.toString()))
            .andExpect(jsonPath("$.disabled").value(DEFAULT_DISABLED.booleanValue()))
            .andExpect(jsonPath("$.characterName").value(DEFAULT_CHARACTER_NAME.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.daysWorking").value(DEFAULT_DAYS_WORKING))
            .andExpect(jsonPath("$.excSologroup").value(DEFAULT_EXC_SOLOGROUP.booleanValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.tagName").value(DEFAULT_TAG_NAME.toString()))
            .andExpect(jsonPath("$.hotkeyValue").value(DEFAULT_HOTKEY_VALUE.toString()))
            .andExpect(jsonPath("$.expireDate").value(DEFAULT_EXPIRE_DATE.toString()))
            .andExpect(jsonPath("$.tertiaryKillPct").value(DEFAULT_TERTIARY_KILL_PCT.doubleValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectRoles() throws Exception {
        // Get the projectRoles
        restProjectRolesMockMvc.perform(get("/api/project-roles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectRoles() throws Exception {
        // Initialize the database
        projectRolesRepository.saveAndFlush(projectRoles);
        projectRolesSearchRepository.save(projectRoles);
        int databaseSizeBeforeUpdate = projectRolesRepository.findAll().size();

        // Update the projectRoles
        ProjectRoles updatedProjectRoles = new ProjectRoles();
        updatedProjectRoles.setId(projectRoles.getId());
        updatedProjectRoles.setSoloKillPct(UPDATED_SOLO_KILL_PCT);
        updatedProjectRoles.setGroupKillPct(UPDATED_GROUP_KILL_PCT);
        updatedProjectRoles.setMiniFullDt(UPDATED_MINI_FULL_DT);
        updatedProjectRoles.setFullFinalDt(UPDATED_FULL_FINAL_DT);
        updatedProjectRoles.setDisabled(UPDATED_DISABLED);
        updatedProjectRoles.setCharacterName(UPDATED_CHARACTER_NAME);
        updatedProjectRoles.setStartDate(UPDATED_START_DATE);
        updatedProjectRoles.setDaysWorking(UPDATED_DAYS_WORKING);
        updatedProjectRoles.setExcSologroup(UPDATED_EXC_SOLOGROUP);
        updatedProjectRoles.setNotes(UPDATED_NOTES);
        updatedProjectRoles.setTagName(UPDATED_TAG_NAME);
        updatedProjectRoles.setHotkeyValue(UPDATED_HOTKEY_VALUE);
        updatedProjectRoles.setExpireDate(UPDATED_EXPIRE_DATE);
        updatedProjectRoles.setTertiaryKillPct(UPDATED_TERTIARY_KILL_PCT);
        updatedProjectRoles.setCreatedDate(UPDATED_CREATED_DATE);
        updatedProjectRoles.setUpdatedDate(UPDATED_UPDATED_DATE);

        restProjectRolesMockMvc.perform(put("/api/project-roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProjectRoles)))
                .andExpect(status().isOk());

        // Validate the ProjectRoles in the database
        List<ProjectRoles> projectRoles = projectRolesRepository.findAll();
        assertThat(projectRoles).hasSize(databaseSizeBeforeUpdate);
        ProjectRoles testProjectRoles = projectRoles.get(projectRoles.size() - 1);
        assertThat(testProjectRoles.getSoloKillPct()).isEqualTo(UPDATED_SOLO_KILL_PCT);
        assertThat(testProjectRoles.getGroupKillPct()).isEqualTo(UPDATED_GROUP_KILL_PCT);
        assertThat(testProjectRoles.getMiniFullDt()).isEqualTo(UPDATED_MINI_FULL_DT);
        assertThat(testProjectRoles.getFullFinalDt()).isEqualTo(UPDATED_FULL_FINAL_DT);
        assertThat(testProjectRoles.isDisabled()).isEqualTo(UPDATED_DISABLED);
        assertThat(testProjectRoles.getCharacterName()).isEqualTo(UPDATED_CHARACTER_NAME);
        assertThat(testProjectRoles.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testProjectRoles.getDaysWorking()).isEqualTo(UPDATED_DAYS_WORKING);
        assertThat(testProjectRoles.isExcSologroup()).isEqualTo(UPDATED_EXC_SOLOGROUP);
        assertThat(testProjectRoles.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testProjectRoles.getTagName()).isEqualTo(UPDATED_TAG_NAME);
        assertThat(testProjectRoles.getHotkeyValue()).isEqualTo(UPDATED_HOTKEY_VALUE);
        assertThat(testProjectRoles.getExpireDate()).isEqualTo(UPDATED_EXPIRE_DATE);
        assertThat(testProjectRoles.getTertiaryKillPct()).isEqualTo(UPDATED_TERTIARY_KILL_PCT);
        assertThat(testProjectRoles.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProjectRoles.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);

        // Validate the ProjectRoles in ElasticSearch
        ProjectRoles projectRolesEs = projectRolesSearchRepository.findOne(testProjectRoles.getId());
        assertThat(projectRolesEs).isEqualToComparingFieldByField(testProjectRoles);
    }

    @Test
    @Transactional
    public void deleteProjectRoles() throws Exception {
        // Initialize the database
        projectRolesRepository.saveAndFlush(projectRoles);
        projectRolesSearchRepository.save(projectRoles);
        int databaseSizeBeforeDelete = projectRolesRepository.findAll().size();

        // Get the projectRoles
        restProjectRolesMockMvc.perform(delete("/api/project-roles/{id}", projectRoles.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean projectRolesExistsInEs = projectRolesSearchRepository.exists(projectRoles.getId());
        assertThat(projectRolesExistsInEs).isFalse();

        // Validate the database is empty
        List<ProjectRoles> projectRoles = projectRolesRepository.findAll();
        assertThat(projectRoles).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProjectRoles() throws Exception {
        // Initialize the database
        projectRolesRepository.saveAndFlush(projectRoles);
        projectRolesSearchRepository.save(projectRoles);

        // Search the projectRoles
        restProjectRolesMockMvc.perform(get("/api/_search/project-roles?query=id:" + projectRoles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectRoles.getId().intValue())))
            .andExpect(jsonPath("$.[*].soloKillPct").value(hasItem(DEFAULT_SOLO_KILL_PCT.doubleValue())))
            .andExpect(jsonPath("$.[*].groupKillPct").value(hasItem(DEFAULT_GROUP_KILL_PCT.doubleValue())))
            .andExpect(jsonPath("$.[*].miniFullDt").value(hasItem(DEFAULT_MINI_FULL_DT.toString())))
            .andExpect(jsonPath("$.[*].fullFinalDt").value(hasItem(DEFAULT_FULL_FINAL_DT.toString())))
            .andExpect(jsonPath("$.[*].disabled").value(hasItem(DEFAULT_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].characterName").value(hasItem(DEFAULT_CHARACTER_NAME.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].daysWorking").value(hasItem(DEFAULT_DAYS_WORKING)))
            .andExpect(jsonPath("$.[*].excSologroup").value(hasItem(DEFAULT_EXC_SOLOGROUP.booleanValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].tagName").value(hasItem(DEFAULT_TAG_NAME.toString())))
            .andExpect(jsonPath("$.[*].hotkeyValue").value(hasItem(DEFAULT_HOTKEY_VALUE.toString())))
            .andExpect(jsonPath("$.[*].expireDate").value(hasItem(DEFAULT_EXPIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].tertiaryKillPct").value(hasItem(DEFAULT_TERTIARY_KILL_PCT.doubleValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())));
    }
}
