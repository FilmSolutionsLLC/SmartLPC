package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.domain.ContactPrivileges;
import com.fps.domain.Contacts;
import com.fps.domain.User;
import com.fps.repository.ContactPrivilegesRepository;
import com.fps.repository.ContactsRepository;
import com.fps.repository.UserRepository;
import com.fps.security.SecurityUtils;
import com.fps.elastics.search.ContactPrivilegesSearchRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

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
	private ContactsRepository contactRepository;
	@Inject
	private UserRepository userRepository;

	/**
	 * POST /contact-privileges : Create a new contactPrivileges.
	 *
	 * @param contactPrivileges
	 *            the contactPrivileges to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new contactPrivileges, or with status 400 (Bad Request) if the
	 *         contactPrivileges has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
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

		contactPrivilegesSearchRepository.save(result);

		return ResponseEntity.created(new URI("/api/contact-privileges/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("contactPrivileges", result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT /contact-privileges : Updates an existing contactPrivileges.
	 *
	 * @param contactPrivileges
	 *            the contactPrivileges to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         contactPrivileges, or with status 400 (Bad Request) if the
	 *         contactPrivileges is not valid, or with status 500 (Internal
	 *         Server Error) if the contactPrivileges couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
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
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         contactPrivileges in body
	 * @throws URISyntaxException
	 *             if there is an error to generate the pagination HTTP headers
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
	 * GET /contact-privileges/:id : get the "id" contactPrivileges.
	 *
	 * @param id
	 *            the id of the contactPrivileges to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         contactPrivileges, or with status 404 (Not Found)
	 */
	@RequestMapping(value = "/contact-privileges/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<ContactPrivileges> getContactPrivileges(@PathVariable Long id) {
		log.debug("REST request to get ContactPrivileges : {}", id);
		ContactPrivileges contactPrivileges = contactPrivilegesRepository.findOne(id);
		return Optional.ofNullable(contactPrivileges).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /contact-privileges/:id : delete the "id" contactPrivileges.
	 *
	 * @param id
	 *            the id of the contactPrivileges to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@RequestMapping(value = "/contact-privileges/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deleteContactPrivileges(@PathVariable Long id) {
		log.debug("REST request to delete ContactPrivileges : {}", id);
		if(id == null) {
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
	 * @param query
	 *            the query of the contactPrivileges search
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
		final Contacts contact = contactRepository.findOne(id);
		final List<ContactPrivileges> projects = contactPrivilegesRepository.findByContact(contact);
		log.info("Total Contact Privileges : " + projects.size());

		return projects;
	}

}
