package com.fps.service.impl;

import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.Lookups;
import com.fps.repository.LookupsRepository;
import com.fps.elastics.search.LookupsSearchRepository;
import com.fps.service.LookupsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Lookups.
 */
@Service
@Transactional
public class LookupsServiceImpl implements LookupsService{

    private final Logger log = LoggerFactory.getLogger(LookupsServiceImpl.class);

    @Inject
    private LookupsRepository lookupsRepository;

    @Inject
    private LookupsSearchRepository lookupsSearchRepository;

    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    final static private String MASTER ="master";
    final static private String SLAVE ="slave";


    /**
     * Save a lookups.
     *
     * @param lookups the entity to save
     * @return the persisted entity
     */
    public Lookups save(Lookups lookups) {
        log.debug("Request to save Lookups : {}", lookups);
        currentTenantIdentifierResolver.setTenant(MASTER);
        Lookups result = lookupsRepository.save(lookups);
        lookupsSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the lookups.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Lookups> findAll(Pageable pageable) {
        log.debug("Request to get all Lookups");
        currentTenantIdentifierResolver.setTenant(SLAVE);
        Page<Lookups> result = lookupsSearchRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one lookups by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Lookups findOne(Long id) {
        log.debug("Request to get Lookups : {}", id);
        currentTenantIdentifierResolver.setTenant(SLAVE);
        Lookups lookups = lookupsRepository.findOne(id);
        return lookups;
    }

    /**
     *  Delete the  lookups by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Lookups : {}", id);
        currentTenantIdentifierResolver.setTenant(MASTER);
        lookupsRepository.delete(id);
        lookupsSearchRepository.delete(id);
    }

    /**
     * Search for the lookups corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Lookups> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Lookups for query {}", query);
        return lookupsSearchRepository.search(queryStringQuery(query), pageable);
    }

    /**
     *
     * @param table_name name of corresponding table
     * @param field_name name of field
     * @return
     */
    @Transactional(readOnly = true)
    public List<Lookups> getLookups(String table_name, String field_name) {
        currentTenantIdentifierResolver.setTenant(SLAVE);
        final List<Lookups> getLooks = lookupsRepository.findByTableNameAndFieldName(table_name, field_name);
        return getLooks;
    }
}
