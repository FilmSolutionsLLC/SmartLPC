package com.fps.service.impl;

import com.fps.config.Constants;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.Contacts;
import com.fps.elastics.search.ContactsSearchRepository;
import com.fps.repository.ContactRelationshipsRepository;
import com.fps.repository.ContactsRepository;
import com.fps.service.ContactsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Contacts.
 */
@Service
@Transactional
public class ContactsServiceImpl implements ContactsService {

    private final Logger log = LoggerFactory.getLogger(ContactsServiceImpl.class);

    @Inject
    private ContactsRepository contactsRepository;

    @Inject
    private ContactsSearchRepository contactsSearchRepository;

    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    @Inject
    private ContactRelationshipsRepository contactRelationshipsRepository;

    /**
     * Save a contacts.
     *
     * @param contacts the entity to save
     * @return the persisted entity
     */
    public Contacts save(Contacts contacts) {
        log.debug("Request to save Contacts : {}", contacts);
        currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);
        Contacts result = contactsRepository.save(contacts);
        contactsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the contacts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Contacts> findAll(Pageable pageable) {
        log.debug("Request to get all Contacts");
        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        Page<Contacts> result = contactsRepository.findAll(pageable);

        return result;
    }

    /**
     * Get one contacts by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Contacts findOne(Long id) {
        log.debug("Request to get Contacts : {}", id);
        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);


        Contacts contacts = contactsRepository.findOne(id);
        return contacts;
    }

    /**
     * Delete the  contacts by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Contacts : {}", id);

        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        Contacts contacts = contactsRepository.findOne(id);
        if (contactRelationshipsRepository.findByContact_A(contacts).size() > 0) {
            currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);
            contactRelationshipsRepository.deleteByContacts(contacts);
        }
        currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);
        contactsRepository.delete(id);
        contactsSearchRepository.delete(id);
    }

    /**
     * Search for the contacts corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Contacts> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Contacts for query {}", query);
        return contactsSearchRepository.search(queryStringQuery(query), pageable);
    }

    /**
     * Search for the contacts corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Iterable<Contacts> searchForList(String query) {
        log.debug("Request to search for a page of Contacts for query {}", query);
        return contactsSearchRepository.search(queryStringQuery(query));
    }
}
