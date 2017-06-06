package com.fps.service;

import com.fps.domain.Ingest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Ingest.
 */
public interface IngestService {

    /**
     * Save a ingest.
     * 
     * @param ingest the entity to save
     * @return the persisted entity
     */
    Ingest save(Ingest ingest);

    /**
     *  Get all the ingests.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Ingest> findAll(Pageable pageable);

    /**
     *  Get the "id" ingest.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Ingest findOne(Long id);

    /**
     *  Delete the "id" ingest.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the ingest corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Ingest> search(String query, Pageable pageable);
}
