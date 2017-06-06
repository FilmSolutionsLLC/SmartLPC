package com.fps.service.impl;

import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.service.IngestService;
import com.fps.domain.Ingest;
import com.fps.repository.IngestRepository;
import com.fps.elastics.search.IngestSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Ingest.
 */
@Service
@Transactional
public class IngestServiceImpl implements IngestService{

    private final Logger log = LoggerFactory.getLogger(IngestServiceImpl.class);

    @Inject
    private IngestRepository ingestRepository;

    @Inject
    private IngestSearchRepository ingestSearchRepository;

    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    final static private String MASTER ="master";
    final static private String SLAVE ="slave";


    /**
     * Save a ingest.
     *
     * @param ingest the entity to save
     * @return the persisted entity
     */
    public Ingest save(Ingest ingest) {
        log.debug("Request to save Ingest : {}", ingest);
        currentTenantIdentifierResolver.setTenant(MASTER);
        Ingest result = ingestRepository.save(ingest);
        ingestSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the ingests.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Ingest> findAll(Pageable pageable) {
        log.debug("Request to get all Ingests");
        currentTenantIdentifierResolver.setTenant(SLAVE);
        Page<Ingest> result = ingestRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one ingest by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Ingest findOne(Long id) {
        log.debug("Request to get Ingest : {}", id);
        currentTenantIdentifierResolver.setTenant(SLAVE);
        Ingest ingest = ingestRepository.findOne(id);
        return ingest;
    }

    /**
     *  Delete the  ingest by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Ingest : {}", id);
        currentTenantIdentifierResolver.setTenant(MASTER);
        ingestRepository.delete(id);
        ingestSearchRepository.delete(id);
    }

    /**
     * Search for the ingest corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Ingest> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Ingests for query {}", query);
        return ingestSearchRepository.search(queryStringQuery(query), pageable);
    }
}
