package com.fps.service;

import com.fps.domain.Contacts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Contacts.
 */
public interface ContactsService {

    /**
     * Save a contacts.
     *
     * @param contacts the entity to save
     * @return the persisted entity
     */
    Contacts save(Contacts contacts);

    /**
     *  Get all the contacts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Contacts> findAll(Pageable pageable);

    /**
     *  Get the "id" contacts.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Contacts findOne(Long id);

    /**
     *  Delete the "id" contacts.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the contacts corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Contacts> search(String query, Pageable pageable);



    /**
     * Search for the contacts corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    Iterable<Contacts> searchForList(String query);




}
