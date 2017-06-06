package com.fps.web.rest;

import com.fps.SmartLpcApp;
import com.fps.domain.Storage_Servers;
import com.fps.repository.Storage_ServersRepository;
import com.fps.elastics.search.Storage_ServersSearchRepository;

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
 * Test class for the Storage_ServersResource REST controller.
 *
 * @see Storage_ServersResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartLpcApp.class)
@WebAppConfiguration
@IntegrationTest
public class Storage_ServersResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_IP_ADDRESS = "AAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBB";

    private static final Boolean DEFAULT_REMOTE = false;
    private static final Boolean UPDATED_REMOTE = true;

    @Inject
    private Storage_ServersRepository storage_ServersRepository;

    @Inject
    private Storage_ServersSearchRepository storage_ServersSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStorage_ServersMockMvc;

    private Storage_Servers storage_Servers;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Storage_ServersResource storage_ServersResource = new Storage_ServersResource();
        ReflectionTestUtils.setField(storage_ServersResource, "storage_ServersSearchRepository", storage_ServersSearchRepository);
        ReflectionTestUtils.setField(storage_ServersResource, "storage_ServersRepository", storage_ServersRepository);
        this.restStorage_ServersMockMvc = MockMvcBuilders.standaloneSetup(storage_ServersResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        storage_ServersSearchRepository.deleteAll();
        storage_Servers = new Storage_Servers();
        storage_Servers.setName(DEFAULT_NAME);
        storage_Servers.setIpAddress(DEFAULT_IP_ADDRESS);
        storage_Servers.setRemote(DEFAULT_REMOTE);
    }

    @Test
    @Transactional
    public void createStorage_Servers() throws Exception {
        int databaseSizeBeforeCreate = storage_ServersRepository.findAll().size();

        // Create the Storage_Servers

        restStorage_ServersMockMvc.perform(post("/api/storage-servers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(storage_Servers)))
                .andExpect(status().isCreated());

        // Validate the Storage_Servers in the database
        List<Storage_Servers> storage_Servers = storage_ServersRepository.findAll();
        assertThat(storage_Servers).hasSize(databaseSizeBeforeCreate + 1);
        Storage_Servers testStorage_Servers = storage_Servers.get(storage_Servers.size() - 1);
        assertThat(testStorage_Servers.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStorage_Servers.getIpAddress()).isEqualTo(DEFAULT_IP_ADDRESS);
        assertThat(testStorage_Servers.isRemote()).isEqualTo(DEFAULT_REMOTE);

        // Validate the Storage_Servers in ElasticSearch
        Storage_Servers storage_ServersEs = storage_ServersSearchRepository.findOne(testStorage_Servers.getId());
        assertThat(storage_ServersEs).isEqualToComparingFieldByField(testStorage_Servers);
    }

    @Test
    @Transactional
    public void getAllStorage_Servers() throws Exception {
        // Initialize the database
        storage_ServersRepository.saveAndFlush(storage_Servers);

        // Get all the storage_Servers
        restStorage_ServersMockMvc.perform(get("/api/storage-servers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(storage_Servers.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].remote").value(hasItem(DEFAULT_REMOTE.booleanValue())));
    }

    @Test
    @Transactional
    public void getStorage_Servers() throws Exception {
        // Initialize the database
        storage_ServersRepository.saveAndFlush(storage_Servers);

        // Get the storage_Servers
        restStorage_ServersMockMvc.perform(get("/api/storage-servers/{id}", storage_Servers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(storage_Servers.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS.toString()))
            .andExpect(jsonPath("$.remote").value(DEFAULT_REMOTE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStorage_Servers() throws Exception {
        // Get the storage_Servers
        restStorage_ServersMockMvc.perform(get("/api/storage-servers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStorage_Servers() throws Exception {
        // Initialize the database
        storage_ServersRepository.saveAndFlush(storage_Servers);
        storage_ServersSearchRepository.save(storage_Servers);
        int databaseSizeBeforeUpdate = storage_ServersRepository.findAll().size();

        // Update the storage_Servers
        Storage_Servers updatedStorage_Servers = new Storage_Servers();
        updatedStorage_Servers.setId(storage_Servers.getId());
        updatedStorage_Servers.setName(UPDATED_NAME);
        updatedStorage_Servers.setIpAddress(UPDATED_IP_ADDRESS);
        updatedStorage_Servers.setRemote(UPDATED_REMOTE);

        restStorage_ServersMockMvc.perform(put("/api/storage-servers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedStorage_Servers)))
                .andExpect(status().isOk());

        // Validate the Storage_Servers in the database
        List<Storage_Servers> storage_Servers = storage_ServersRepository.findAll();
        assertThat(storage_Servers).hasSize(databaseSizeBeforeUpdate);
        Storage_Servers testStorage_Servers = storage_Servers.get(storage_Servers.size() - 1);
        assertThat(testStorage_Servers.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStorage_Servers.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testStorage_Servers.isRemote()).isEqualTo(UPDATED_REMOTE);

        // Validate the Storage_Servers in ElasticSearch
        Storage_Servers storage_ServersEs = storage_ServersSearchRepository.findOne(testStorage_Servers.getId());
        assertThat(storage_ServersEs).isEqualToComparingFieldByField(testStorage_Servers);
    }

    @Test
    @Transactional
    public void deleteStorage_Servers() throws Exception {
        // Initialize the database
        storage_ServersRepository.saveAndFlush(storage_Servers);
        storage_ServersSearchRepository.save(storage_Servers);
        int databaseSizeBeforeDelete = storage_ServersRepository.findAll().size();

        // Get the storage_Servers
        restStorage_ServersMockMvc.perform(delete("/api/storage-servers/{id}", storage_Servers.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean storage_ServersExistsInEs = storage_ServersSearchRepository.exists(storage_Servers.getId());
        assertThat(storage_ServersExistsInEs).isFalse();

        // Validate the database is empty
        List<Storage_Servers> storage_Servers = storage_ServersRepository.findAll();
        assertThat(storage_Servers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStorage_Servers() throws Exception {
        // Initialize the database
        storage_ServersRepository.saveAndFlush(storage_Servers);
        storage_ServersSearchRepository.save(storage_Servers);

        // Search the storage_Servers
        restStorage_ServersMockMvc.perform(get("/api/_search/storage-servers?query=id:" + storage_Servers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storage_Servers.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].remote").value(hasItem(DEFAULT_REMOTE.booleanValue())));
    }
}
