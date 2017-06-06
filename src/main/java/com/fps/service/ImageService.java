package com.fps.service;

import com.fps.domain.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Image.
 */
public interface ImageService {

    /**
     * Save a image.
     * 
     * @param image the entity to save
     * @return the persisted entity
     */
    Image save(Image image);

    /**
     *  Get all the images.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Image> findAll(Pageable pageable);

    /**
     *  Get the "id" image.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Image findOne(Long id);

    /**
     *  Delete the "id" image.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the image corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Image> search(String query, Pageable pageable);
}
