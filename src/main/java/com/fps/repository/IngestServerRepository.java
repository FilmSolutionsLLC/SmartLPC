package com.fps.repository;

import com.fps.domain.IngestServer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by macbookpro on 6/5/17.
 */
public interface IngestServerRepository extends JpaRepository<IngestServer, Long> {

    List<IngestServer> findByAvailable(Integer available);
}
