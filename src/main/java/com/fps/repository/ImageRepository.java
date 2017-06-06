package com.fps.repository;

import com.fps.domain.Image;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Image entity.
 */
public interface ImageRepository extends JpaRepository<Image,Long> {

    @Query("select image from Image image where image.createdByUser.login = ?#{principal.username}")
    List<Image> findByCreatedByUserIsCurrentUser();

    @Query("select image from Image image where image.updatedByUser.login = ?#{principal.username}")
    List<Image> findByUpdatedByUserIsCurrentUser();

}
