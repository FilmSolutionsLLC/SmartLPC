package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.ContactRelationships;
import com.fps.domain.Contacts;
import com.fps.domain.User;
import com.fps.elastics.search.ContactsSearchRepository;
import com.fps.repository.ContactRelationshipsRepository;
import com.fps.repository.ContactsRepository;
import com.fps.repository.UserRepository;
import com.fps.security.SecurityUtils;
import com.fps.service.ContactsService;
import com.fps.web.rest.dto.ContactsDTO;
import com.fps.web.rest.util.HeaderUtil;
import com.fps.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing Contacts.
 */
@RestController
@RequestMapping("/api")
public class ContactsResource {

	private final Logger log = LoggerFactory.getLogger(ContactsResource.class);

	@Inject
	private ContactsService contactsService;

	@Inject
	private UserRepository userRepository;

	@Inject
	private ContactRelationshipsRepository contactRelationshipsRepository;

	@Inject
	private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

	@Inject
	private JdbcTemplate jdbcTemplate;

	final static private String MASTER = "master";
	final static private String SLAVE = "slave";

	/**
	 * POST /contacts : Create a new contacts.
	 *
	 * @param contactsDTO
	 *            the contacts to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new contacts, or with status 400 (Bad Request) if the contacts
	 *         has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/contacts", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Contacts> createContacts(@RequestBody ContactsDTO contactsDTO) throws URISyntaxException {

		log.debug("REST request to save Contacts : {}", contactsDTO);

		System.out.println("Inside saving of contacts");
		Contacts contacts = contactsDTO.getContacts();
		if (contacts.getId() != null) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert("contacts", "idexists", "A new contacts cannot already have an ID"))
					.body(null);
		}

		currentTenantIdentifierResolver.setTenant(SLAVE);
		User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

		contacts.setCreatedByAdmin(user);
		contacts.setCreatedDate(LocalDate.now());


		Contacts result = contactsService.save(contacts);

		
		if (contactsDTO.getContactRelationships().size() > 0) {
            
			Set<ContactRelationships> contactRelationshipses = new HashSet<>();
			for (ContactRelationships contactRelationships : contactsDTO.getContactRelationships()) {
				log.debug("REST request to save ContactsRelationships : {}", contactRelationships);
				contactRelationships.setContact_a(contacts);
				contactRelationships.setCreatedByAdminUser(user);
				contactRelationships.setCreatedDate(LocalDate.now());
				contactRelationshipses.add(contactRelationships);
			}
			currentTenantIdentifierResolver.setTenant(MASTER);
			contactRelationshipsRepository.save(contactRelationshipses);
		}

		return ResponseEntity.created(new URI("/api/contacts/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("contacts", result.getId().toString())).body(result);
	}

	/**
	 * PUT /contacts : Updates an existing contacts.
	 *
	 * @param contactsDTO
	 *            the contacts to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         contacts, or with status 400 (Bad Request) if the contacts is not
	 *         valid, or with status 500 (Internal Server Error) if the contacts
	 *         couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/contacts", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Contacts> updateContacts(@RequestBody ContactsDTO contactsDTO) throws URISyntaxException {
		log.debug("REST request to update Contacts : {}", contactsDTO);
		currentTenantIdentifierResolver.setTenant(SLAVE);
		User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

		Contacts contacts = contactsDTO.getContacts();
		if (contacts.getId() == null) {
			return createContacts(contactsDTO);
		}
		contacts.setUpdatedByAdmin(user);
		contacts.setUpdatedDate(LocalDate.now());
		Contacts result = contactsService.save(contacts);
		if (contactsDTO.getContactRelationships().size() > 0) {
			Set<ContactRelationships> contactRelationshipses = new HashSet<>();
			for (ContactRelationships contactRelationships : contactsDTO.getContactRelationships()) {
				contactRelationships.setUpdatedByAdminUser(user);
				contactRelationships.setUpdatedDate(LocalDate.now());
				contactRelationships.setContact_a(contacts);
				contactRelationshipses.add(contactRelationships);

			}
			currentTenantIdentifierResolver.setTenant(MASTER);
			contactRelationshipsRepository.save(contactRelationshipses);
		}

		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("contacts", contacts.getId().toString()))
				.body(result);
	}

	/**
	 * GET /contacts : get all the contacts.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of contacts
	 *         in body
	 * @throws URISyntaxException
	 *             if there is an error to generate the pagination HTTP headers
	 */
	@RequestMapping(value = "/contacts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Contacts>> getAllContacts(Pageable pageable) throws URISyntaxException {
		log.debug("REST request to get a page of Contacts");
		Page<Contacts> page = contactsService.findAll(pageable);

		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contacts");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	/**
	 * GET /contacts/:id : get the "id" contacts.
	 *
	 * @param id
	 *            the id of the contacts to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         contacts, or with status 404 (Not Found)
	 */
	@RequestMapping(value = "/contacts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<ContactsDTO> getContacts(@PathVariable Long id) {

		log.debug("REST request to get Contacts : {}", id);
		Contacts contacts = contactsService.findOne(id);
		List<ContactRelationships> contactRelationshipses = contactRelationshipsRepository.findByContact_A(contacts);
		Set<ContactRelationships> contactRelationshipsSet = new HashSet<>(contactRelationshipses);
		ContactsDTO contactsDTO = new ContactsDTO();
		contactsDTO.setContacts(contacts);
		contactsDTO.setContactRelationships(contactRelationshipsSet);
		return Optional.ofNullable(contactsDTO).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /contacts/:id : delete the "id" contacts.
	 *
	 * @param id
	 *            the id of the contacts to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@RequestMapping(value = "/contacts/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deleteContacts(@PathVariable Long id) {
		log.debug("REST request to delete Contacts : {}", id);
		contactsService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("contacts", id.toString())).build();
	}

	/**
	 * SEARCH /_search/contacts?query=:query : search for the contacts
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the contacts search
	 * @return the result of the search
	 */
	@RequestMapping(value = "/_search/contacts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Contacts>> searchContacts(@RequestParam String query, Pageable pageable)
			throws URISyntaxException {
		log.debug("REST request to search for a page of Contacts for query {}", query);
		Page<Contacts> page = contactsService.search(query, pageable);
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/contacts");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * SEARCH /_search/contacts?query=:query : search for the contacts
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the contacts search
	 * @return the result of the search
	 */
	@RequestMapping(value = "/_search/modal/contacts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Contacts> searchFromModalBox(@RequestParam String query) throws URISyntaxException {
		List<Contacts> contacts = (List<Contacts>) contactsService.searchForList(query);
		return contacts;
	}

	@Inject
	ContactsRepository contactsRepository;
	@Inject
	ContactsSearchRepository contactsSearchRepository;

	@RequestMapping(value = "/cons", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Objects[]>> getAllContactss(Pageable pageable) throws URISyntaxException {
		log.debug("REST request to get a page of Contacts");
		Page<Objects[]> page = contactsRepository.getAllContacts(pageable);

		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cons");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/elastic/cons", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Iterable<Contacts>> getAllContactsFromElastic() throws URISyntaxException {
		log.info("-----------------------------------------------------------");
		log.info("FROM ELASTIC SEARCH SERVER");
		log.info("------------------------------------------------------------");
		long startTime = System.nanoTime();
		String query = "";
		Iterable<Contacts> contactsSet = contactsSearchRepository.search(queryStringQuery(query));
		long elapsedTimeNs = System.nanoTime() - startTime;
		log.info("=========> " + elapsedTimeNs);

		return new ResponseEntity<>(contactsSet, HttpStatus.OK);
	}

	@RequestMapping(value = "/database/cons", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Iterable<Contacts>> getAllContactsFromDatabase() throws URISyntaxException {
		log.info("------------------------------------------------------------");
		log.info("FROM DATABASE SERVER");
		log.info("------------------------------------------------------------");
		long startTime = System.nanoTime();
		Iterable<Contacts> contactsSet = contactsRepository.findAll();
		long elapsedTimeNs = System.nanoTime() - startTime;
		log.info("=========> " + elapsedTimeNs);
		return new ResponseEntity<>(contactsSet, HttpStatus.OK);
	}

	@RequestMapping(value = "/contacts/related/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public Contacts getRelatedContactTalent(@PathVariable Long id) throws URISyntaxException {
		try {
			String getPrimary = "select cr.contact_b_id as id from contacts c inner join contact_relationships cr on c.id = cr.contact_a_id where c.id ="
					+ id + " and cr.is_primary_contact = 1";
			System.out.println("getPrimarySql :" + getPrimary);

			Long primaryContactId = jdbcTemplate.queryForObject(getPrimary, new Object[] {}, Long.class);

			final Contacts contact = contactsRepository.findOne(primaryContactId);
			System.out.println("Returning Related Contact : "+contact.getId());
			return contact;

		} catch (EmptyResultDataAccessException e) {
			final Contacts contact = contactsRepository.findOne(id);
			return contact;
		}

	}

}
