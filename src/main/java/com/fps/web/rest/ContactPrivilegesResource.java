package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.config.Constants;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.*;
import com.fps.elastics.search.ContactPrivilegesSearchRepository;
import com.fps.repository.*;
import com.fps.security.SecurityUtils;
import com.fps.web.rest.dto.ContactDTO;
import com.fps.web.rest.dto.ContactPrivilegeDTO;
import com.fps.web.rest.util.HeaderUtil;
import com.fps.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing ContactPrivileges.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ContactPrivilegesResource {

    private final Logger log = LoggerFactory.getLogger(ContactPrivilegesResource.class);

    @Inject
    private ContactPrivilegesRepository contactPrivilegesRepository;

    @Inject
    private ContactPrivilegesSearchRepository contactPrivilegesSearchRepository;

    @Inject
    private ContactRepository contactRepository;
    @Inject
    private UserRepository userRepository;

    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    @Inject
    private ContactPrivilegeAlbumsRepository contactPrivilegeAlbumsRepository;

    @Inject
    private ContactPrivilegeReviewersRepository contactPrivilegeReviewersRepository;


    @Inject
    private JdbcTemplate jdbcTemplate;

    /**
     * POST /contact-privileges : Create a new contactPrivileges.
     *
     * @param contactPrivileges the contactPrivileges to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new contactPrivileges, or with status 400 (Bad Request) if the
     * contactPrivileges has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/contact-privileges", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContactPrivileges> createContactPrivileges(@RequestBody ContactPrivileges contactPrivileges)
        throws URISyntaxException {
        log.debug("REST request to save ContactPrivileges : {}", contactPrivileges);
        if (contactPrivileges.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contactPrivileges", "idexists",
                "A new contactPrivileges cannot already have an ID")).body(null);
        }

        final User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        contactPrivileges.setCreatedDate(ZonedDateTime.now());
        contactPrivileges.setCreatedByAdminUser(user);
        ContactPrivileges result = contactPrivilegesRepository.save(contactPrivileges);

        //contactPrivilegesSearchRepository.save(result);

        return ResponseEntity.created(new URI("/api/contact-privileges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("contactPrivileges", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT /contact-privileges : Updates an existing contactPrivileges.
     *
     * @param contactPrivileges the contactPrivileges to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * contactPrivileges, or with status 400 (Bad Request) if the
     * contactPrivileges is not valid, or with status 500 (Internal
     * Server Error) if the contactPrivileges couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/contact-privileges", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContactPrivileges> updateContactPrivileges(@RequestBody ContactPrivileges contactPrivileges)
        throws URISyntaxException {
        log.debug("REST request to update ContactPrivileges : {}", contactPrivileges);
        final User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        if (contactPrivileges.getId() == null) {
            return createContactPrivileges(contactPrivileges);
        }
        contactPrivileges.setUpdatedDate(ZonedDateTime.now());
        contactPrivileges.setUpdatedByAdminUser(user);
        ContactPrivileges result = contactPrivilegesRepository.save(contactPrivileges);

        contactPrivilegesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("contactPrivileges", contactPrivileges.getId().toString()))
            .body(result);
    }

    /**
     * GET /contact-privileges : get all the contactPrivileges.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of
     * contactPrivileges in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/contact-privileges", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ContactPrivileges>> getAllContactPrivileges(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ContactPrivileges");
        Page<ContactPrivileges> page = contactPrivilegesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contact-privileges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /contact-privileges/:id : get the "id" contactPrivilegeDTO.
     *
     * @param id the id of the contactPrivileges to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the
     * contactPrivileges, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/contact-privileges/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContactPrivilegeDTO> getContactPrivileges(@PathVariable Long id) {
        log.debug("REST request to get ContactPrivileges : {}", id);
        ContactPrivileges contactPrivileges = contactPrivilegesRepository.findOne(id);

        final List<ContactPrivilegeAlbums> contactPrivilegeAlbums = contactPrivilegeAlbumsRepository.findByContactPrivilegeID(id);
        final List<ContactPrivilegeReviewers> contactPrivilegeReviewers = contactPrivilegeReviewersRepository.findByContactPrivilegeID(id);

        ContactPrivilegeDTO contactPrivilegeDTO = new ContactPrivilegeDTO();
        contactPrivilegeDTO.setContactPrivileges(contactPrivileges);
        contactPrivilegeDTO.setContactPrivilegeAlbums(contactPrivilegeAlbums);
        contactPrivilegeDTO.setContactPrivilegeReviewers(contactPrivilegeReviewers);

        return Optional.ofNullable(contactPrivilegeDTO).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE /contact-privileges/:id : delete the "id" contactPrivileges.
     *
     * @param id the id of the contactPrivileges to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/contact-privileges/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteContactPrivileges(@PathVariable Long id) {
        log.debug("REST request to delete ContactPrivileges : {}", id);
        if (id != null) {
            contactPrivilegesRepository.delete(id);
            contactPrivilegesSearchRepository.delete(id);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("contactPrivileges", id.toString()))
            .build();
    }

    /**
     * SEARCH /_search/contact-privileges?query=:query : search for the
     * contactPrivileges corresponding to the query.
     *
     * @param query the query of the contactPrivileges search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/contact-privileges", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ContactPrivileges>> searchContactPrivileges(@RequestParam String query,
                                                                           Pageable pageable) throws URISyntaxException {
        log.debug("REST request to search for a page of ContactPrivileges for query {}", query);
        Page<ContactPrivileges> page = contactPrivilegesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
            "/api/_search/contact-privileges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/contact/project/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ContactPrivileges> contactProjects(@PathVariable Long id) throws URISyntaxException {
        final Contact contact = contactRepository.findOne(id);
        final List<ContactPrivileges> projects = contactPrivilegesRepository.findByContact(contact);
        log.info("Total Contact Privileges : " + projects.size());

        return projects;
    }

    @RequestMapping(value = "/search/contact/project", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ContactDTO> getContactsByProject(@RequestParam String query) throws URISyntaxException {
        log.info("Query : ");
        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        String sql = "select c.id,c.full_name,c.email,c.title,c.phone_office,c.phone_mobile,c.title," +
            "cr.full_name as company_contact_full_name from " +
            "contacts c inner join contact_privileges cp on c.id = cp.contact_id " +
            "inner join projects p on p.id = cp.project_id " +
            "left join contacts cr on c.id = cr.company_contact_id " +
            "where p.name like '%" + query + "%' group by c.id order by c.full_name asc";


        log.info("SQL Query: " + sql);
        List<ContactDTO> contactDTOS = new ArrayList<>();
        List<Map<String, Object>> contacts = jdbcTemplate.queryForList(sql);
        for (Map contact : contacts) {
            ContactDTO contactDTO = new ContactDTO();
            contactDTO.setId(((BigInteger) contact.get("Id")).longValue());
            contactDTO.setFullName((String) contact.get("full_name"));
            contactDTO.setEmail((String) contact.get("email"));
            contactDTO.setTitle((String) contact.get("title"));
            contactDTO.setPhoneOffice((String) contact.get("phone_office"));
            contactDTO.setPhoneMobile((String) contact.get("phone_mobile"));
            contactDTO.setCompanyContactFullName((String) contact.get("company_contact_full_name"));

            contactDTOS.add(contactDTO);
        }
        return contactDTOS;
    }

    @RequestMapping(value = "/get/contact/project", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ContactPrivilegeDTO getByContactAndProject(@RequestParam Long projectID, @RequestParam Long contactID) throws URISyntaxException {
        log.info("Getting contact privilege from Contact and Project");
        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        Projects projects = new Projects();
        projects.setId(projectID);
        Contact contacts = new Contact();
        contacts.setId(contactID);
        ContactPrivileges contactPrivileges = contactPrivilegesRepository.findByProjectAndContact(projects, contacts);


        final List<ContactPrivilegeAlbums> contactPrivilegeAlbums = contactPrivilegeAlbumsRepository.findByContactPrivilegeID(contactPrivileges.getId());
        final List<ContactPrivilegeReviewers> contactPrivilegeReviewers = contactPrivilegeReviewersRepository.findByContactPrivilegeID(contactPrivileges.getId());

        ContactPrivilegeDTO contactPrivilegeDTO = new ContactPrivilegeDTO();
        contactPrivilegeDTO.setContactPrivileges(contactPrivileges);
        contactPrivilegeDTO.setContactPrivilegeAlbums(contactPrivilegeAlbums);
        contactPrivilegeDTO.setContactPrivilegeReviewers(contactPrivilegeReviewers);

        return contactPrivilegeDTO;
    }

    @RequestMapping(value = "/disable/execs/{project}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void disableExecutives(@PathVariable Long project) throws URISyntaxException {
        log.info("disable contact privilege from Project");
        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        String sql = "update contact_privileges set disabled = true where internal = 0 and project_id=" + project;
        jdbcTemplate.execute(sql);

    }

    @RequestMapping(value = "/enable/execs/{project}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void enableExecutives(@PathVariable Long project) throws URISyntaxException {
        log.info("enable contact privilege from Project");
        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        String sql = "update contact_privileges set disabled = false where internal = 0 and project_id=" + project;
        jdbcTemplate.execute(sql);

    }


    @RequestMapping(value = "/cp/projects/{type}/{query}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Set<Contact> findContactByProjects(@PathVariable String type, @PathVariable String query) throws URISyntaxException {
        log.info("enable contact privilege from Project");
        currentTenantIdentifierResolver.setTenant(Constants.SLAVE_DATABASE);
        Set<Contact> contactList = contactPrivilegesRepository.findByProject(query);

        return contactList;
    }


    /**
     * POST /contact-privileges : Create a new contactPrivilegesDTO.
     *
     * @param contactPrivilegeDTO the contactPrivileges to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new contactPrivileges, or with status 400 (Bad Request) if the
     * contactPrivileges has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dto/contact-privileges", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContactPrivileges> updateContactPrivilegesDTO(@RequestBody ContactPrivilegeDTO contactPrivilegeDTO)
        throws URISyntaxException {
        log.debug("REST request to update ContactPrivileges : {}", contactPrivilegeDTO);
        ContactPrivileges contactPrivileges = contactPrivilegeDTO.getContactPrivileges();
        List<ContactPrivilegeAlbums> contactPrivilegeAlbums = contactPrivilegeDTO.getContactPrivilegeAlbums();
        List<ContactPrivilegeReviewers> contactPrivilegeReviewers = contactPrivilegeDTO.getContactPrivilegeReviewers();

        final User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        contactPrivileges.setUpdatedDate(ZonedDateTime.now());
        contactPrivileges.setUpdatedByAdminUser(user);
        ContactPrivileges result = contactPrivilegesRepository.save(contactPrivileges);
        if (contactPrivilegeAlbums.size() > 0) {
            contactPrivilegeAlbumsRepository.save(contactPrivilegeAlbums);
        }
        if (contactPrivilegeReviewers.size() > 0) {
            contactPrivilegeReviewersRepository.save(contactPrivilegeReviewers);
        }


        log.info("Saved Contact Privilege");
        log.info("Saved Contact Privilege Albums");
        log.info("Saved Contact Privilege Reviewers");

        //contactPrivilegesSearchRepository.save(result);

        return ResponseEntity.created(new URI("/api/contact-privileges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("contactPrivileges", result.getId().toString()))
            .body(result);
    }


    @RequestMapping(value = "/delete/contact-privileges/albums", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void deleteAlbums(@RequestBody List<ContactPrivilegeAlbums> contactPrivilegeAlbums)
        throws URISyntaxException {
        log.info("Rest Request to remove albums associated with Exec");
        for (ContactPrivilegeAlbums contactPrivilegeAlbum : contactPrivilegeAlbums) {
            contactPrivilegeAlbumsRepository.deleteContactPrivilegeAlbumsByContactPrivilegeIDAndAlbumNodeID(contactPrivilegeAlbum.getContactPrivilegeID(), contactPrivilegeAlbum.getAlbumNodeID());
        }
        log.info("Albums Removed");
    }

    @RequestMapping(value = "/delete/contact-privileges/reviewers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void deleteReviewers(@RequestBody List<ContactPrivilegeReviewers> contactPrivilegeReviewers)
        throws URISyntaxException {
        log.info("Rest Request to remove actors associated with Exec");
        for (ContactPrivilegeReviewers contactPrivilegeReviewer : contactPrivilegeReviewers) {
            contactPrivilegeReviewersRepository.deleteContactPrivilegeReviewersByContactPrivilegeIDAndTagID(contactPrivilegeReviewer.getContactPrivilegeID(), contactPrivilegeReviewer.getTagID());
        }
        log.info("Reviewers Removed");
    }


    @RequestMapping(value = "/restart/clear", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void restartClear(@RequestParam Long id, @RequestParam Boolean isAll) throws URISyntaxException {
        log.info("clear restart data");
        String sql = "";
        sql += " UPDATE contact_privileges ";
        sql += " SET  ";
        sql += " last_login_dt=null, ";
        sql += " restart_role='', ";
        sql += " restart_image='', ";
        sql += " restart_page=0, ";
        sql += " restart_filter='', ";
        sql += " login_count=0 ";
        sql += " WHERE ";
        if (isAll) {
            sql += " project_id = ? ";
        } else {
            sql += " id = ?";
        }

        currentTenantIdentifierResolver.setTenant(Constants.MASTER_DATABASE);
        jdbcTemplate.update(sql,id );
    }


}
