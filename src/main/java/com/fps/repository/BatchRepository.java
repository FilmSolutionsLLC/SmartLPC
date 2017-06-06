package com.fps.repository;

import com.fps.domain.Batch;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Batch entity.
 */
public interface BatchRepository extends JpaRepository<Batch,Long> {

    @Query("select batch from Batch batch where batch.createdByAdminUser.login = ?#{principal.username}")
    List<Batch> findByCreatedByAdminUserIsCurrentUser();

    @Query("select batch from Batch batch where batch.updatedByAdminUser.login = ?#{principal.username}")
    List<Batch> findByUpdatedByAdminUserIsCurrentUser();

}
