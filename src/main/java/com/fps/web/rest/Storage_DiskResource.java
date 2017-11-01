package com.fps.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.domain.Storage_Disk;
import com.fps.repository.Storage_DiskRepository;
import com.fps.elastics.search.Storage_DiskSearchRepository;
import com.fps.web.rest.util.HeaderUtil;
import com.fps.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Storage_Disk.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class Storage_DiskResource {

	private final Logger log = LoggerFactory.getLogger(Storage_DiskResource.class);

	@Inject
	private Storage_DiskRepository storage_DiskRepository;

	@Inject
	private Storage_DiskSearchRepository storage_DiskSearchRepository;

	@Inject
	private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

	@Value("${largest.brick}")
	private String largestBrick;

	final static private String MASTER = "master";
	final static private String SLAVE = "slave";

	/**
	 * POST /storage-disks : Create a new storage_Disk.
	 *
	 * @param storage_Disk
	 *            the storage_Disk to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new storage_Disk, or with status 400 (Bad Request) if the
	 *         storage_Disk has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/storage-disks", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Storage_Disk> createStorage_Disk(@RequestBody Storage_Disk storage_Disk)
			throws URISyntaxException {
		log.debug("REST request to save Storage_Disk : {}", storage_Disk);
		if (storage_Disk.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("storage_Disk", "idexists",
					"A new storage_Disk cannot already have an ID")).body(null);
		}
		currentTenantIdentifierResolver.setTenant(MASTER);
		Storage_Disk result = storage_DiskRepository.save(storage_Disk);
		storage_DiskSearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/storage-disks/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("storage_Disk", result.getId().toString())).body(result);
	}

	/**
	 * PUT /storage-disks : Updates an existing storage_Disk.
	 *
	 * @param storage_Disk
	 *            the storage_Disk to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         storage_Disk, or with status 400 (Bad Request) if the
	 *         storage_Disk is not valid, or with status 500 (Internal Server
	 *         Error) if the storage_Disk couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/storage-disks", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Storage_Disk> updateStorage_Disk(@RequestBody Storage_Disk storage_Disk)
			throws URISyntaxException {
		log.debug("REST request to update Storage_Disk : {}", storage_Disk);
		if (storage_Disk.getId() == null) {
			return createStorage_Disk(storage_Disk);
		}
		currentTenantIdentifierResolver.setTenant(MASTER);
		Storage_Disk result = storage_DiskRepository.save(storage_Disk);
		storage_DiskSearchRepository.save(result);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert("storage_Disk", storage_Disk.getId().toString()))
				.body(result);
	}

	/**
	 * GET /storage-disks : get all the storage_Disks.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         storage_Disks in body
	 * @throws URISyntaxException
	 *             if there is an error to generate the pagination HTTP headers
	 */
	@RequestMapping(value = "/storage-disks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Storage_Disk>> getAllStorage_Disks(Pageable pageable) throws URISyntaxException {
		log.debug("REST request to get a page of Storage_Disks");
		currentTenantIdentifierResolver.setTenant(SLAVE);
		Page<Storage_Disk> page = storage_DiskRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/storage-disks");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /storage-disks/:id : get the "id" storage_Disk.
	 *
	 * @param id
	 *            the id of the storage_Disk to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         storage_Disk, or with status 404 (Not Found)
	 */
	@RequestMapping(value = "/storage-disks/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Storage_Disk> getStorage_Disk(@PathVariable Long id) {
		log.debug("REST request to get Storage_Disk : {}", id);
		currentTenantIdentifierResolver.setTenant(SLAVE);
		Storage_Disk storage_Disk = storage_DiskRepository.findOne(id);
		return Optional.ofNullable(storage_Disk).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /storage-disks/:id : delete the "id" storage_Disk.
	 *
	 * @param id
	 *            the id of the storage_Disk to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@RequestMapping(value = "/storage-disks/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deleteStorage_Disk(@PathVariable Long id) {
		log.debug("REST request to delete Storage_Disk : {}", id);
		currentTenantIdentifierResolver.setTenant(MASTER);
		storage_DiskRepository.delete(id);
		storage_DiskSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("storage_Disk", id.toString())).build();
	}

	/**
	 * SEARCH /_search/storage-disks?query=:query : search for the storage_Disk
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the storage_Disk search
	 * @return the result of the search
	 */
	@RequestMapping(value = "/_search/storage-disks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Storage_Disk>> searchStorage_Disks(@RequestParam String query, Pageable pageable)
			throws URISyntaxException {
		log.debug("REST request to search for a page of Storage_Disks for query {}", query);
		Page<Storage_Disk> page = storage_DiskSearchRepository.search(queryStringQuery(query), pageable);
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
				"/api/_search/storage-disks");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@SuppressWarnings("resource")
	@RequestMapping(value = "/largest/storage-disks", method = RequestMethod.GET,  produces = "text/plain")
	@Timed
	public String largestDisk() throws URISyntaxException {
		log.debug("REST request to get a page of Storage_Disks");
		currentTenantIdentifierResolver.setTenant(SLAVE);
		String everything = null;
		try {


			final BufferedReader br = new BufferedReader(new FileReader(largestBrick));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			everything = sb.toString().trim();
			log.info("Largest Drive : "+everything);
			//final Storage_Disk storage_Disk = storage_DiskRepository.findByName(everything);
			return everything;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
