package com.fps.service.impl;

import com.fps.config.util.CurrentTenantIdentifierResolverImpl;
import com.fps.service.ImageService;
import com.fps.domain.Image;
import com.fps.repository.ImageRepository;
import com.fps.elastics.search.ImageSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Image.
 */
@Service
@Transactional
public class ImageServiceImpl implements ImageService{

    private final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Inject
    private ImageRepository imageRepository;

    @Inject
    private ImageSearchRepository imageSearchRepository;

    @Inject
    private CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver;

    final static private String MASTER ="master";
    final static private String SLAVE ="slave";

    /**
     * Save a image.
     *
     * @param image the entity to save
     * @return the persisted entity
     */
    public Image save(Image image) {
        log.debug("Request to save Image : {}", image);
        currentTenantIdentifierResolver.setTenant(MASTER);
        Image result = imageRepository.save(image);
        imageSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the images.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Image> findAll(Pageable pageable) {
        log.debug("Request to get all Images");

        currentTenantIdentifierResolver.setTenant(SLAVE);
        Page<Image> result = imageRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one image by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Image findOne(Long id) {
        log.debug("Request to get Image : {}", id);
        currentTenantIdentifierResolver.setTenant(SLAVE);
        Image image = imageRepository.findOne(id);
        return image;
    }

    /**
     *  Delete the  image by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Image : {}", id);
        currentTenantIdentifierResolver.setTenant(MASTER);
        imageRepository.delete(id);
        imageSearchRepository.delete(id);
    }

    /**
     * Search for the image corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Image> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Images for query {}", query);
        return imageSearchRepository.search(queryStringQuery(query), pageable);
    }
}
