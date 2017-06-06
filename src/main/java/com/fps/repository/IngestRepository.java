package com.fps.repository;

import com.fps.domain.Ingest;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Ingest entity.
 */
public interface IngestRepository extends JpaRepository<Ingest,Long> {

    @Query("select ingest from Ingest ingest where ingest.user.login = ?#{principal.username}")
    List<Ingest> findByUserIsCurrentUser();

}
