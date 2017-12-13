package com.fps.service.impl;

import com.fps.config.Constants;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.Contact;
import com.fps.domain.Contacts;
import com.fps.elastics.search.ContactsSearchRepository;
import com.fps.repository.ContactPrivilegesRepository;
import com.fps.repository.ContactRelationshipsRepository;
import com.fps.repository.ContactsRepository;
import com.fps.repository.ProjectRolesRepository;
import com.fps.service.ContactsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    @Inject
    private ContactPrivilegesRepository contactPrivilegesRepository;

    @Inject
    private ProjectRolesRepository projectRolesRepository;

    @Value("${javalocation.path}")
    String javaLocation;

    @Value("${javalocation.jarFile}")
    String encryptorJar;

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
       /* try {
            String command = javaLocation + " -jar " + encryptorJar + " '" + result.getId() + "' '" + result.getPassword() + "'";
            log.info("Password Encrypt : " + command);


            ProcessBuilder pb = new ProcessBuilder(command);
            Process p = pb.start();
            if(p.waitFor()==0){
                log.info("Password Encryption Complete");
            }else{
                log.info("Password Encryption Error");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/
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
        log.info("Request to get all Contacts");
        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        //Page<Contacts> result = contactsSearchRepository.findAll(pageable);
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


        log.info(contacts.toString());
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
        //TODO: delete from all tables such as ContactPrivileges,ProjectRoles
        Contact contact = new Contact();
        contact.setId(contacts.getId());

        contactPrivilegesRepository.removeByContact(contact);
        projectRolesRepository.removeByContact(contact);


        if (contactRelationshipsRepository.findByContact_A(contact).size() > 0) {
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
        //return contactsSearchRepository.findByFullNameContaining(queryStringQuery(query), pageable);
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
