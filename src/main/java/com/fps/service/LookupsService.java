package com.fps.service;

import com.fps.domain.Lookups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Lookups.
 */
public interface LookupsService {

    /**
     * Save a lookups.
     *
     * @param lookups the entity to save
     * @return the persisted entity
     */
    Lookups save(Lookups lookups);

    /**
     * Get all the lookups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Lookups> findAll(Pageable pageable);

    /**
     * Get the "id" lookups.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Lookups findOne(Long id);

    /**
     * Delete the "id" lookups.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the lookups corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    Page<Lookups> search(String query, Pageable pageable);


    /**
     * Get lookups for particular table
     *
     * @param table_name name of corresponding table
     * @param field_name name of field
     * @return list of values
     */
    List<Lookups> getLookups(String table_name, String field_name);
}
