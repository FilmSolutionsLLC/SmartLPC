package com.fps.service;

import com.fps.domain.Ingests;
import com.fps.web.rest.dto.IngestFileSystem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * Created by macbookpro on 2/1/17.
 */
public interface IngestFileSystemService {

    Set<IngestFileSystem> newIngests();

    Boolean startIngest(IngestFileSystem ingestFileSystem);

    Set<Ingests> getAllRunningIngests();

    Ingests getIngest(Long id);

    void delete(Long id);

    Ingests save(Ingests ingests);

    Page<Ingests> findAll(Pageable pageable);

    /**
     * Search for the ingest corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    Page<Ingests> search(String query, Pageable pageable);

    List<String> getLogos();

    void stopIngest(Ingests ingests);

    void pause(Ingests ingests);

    void resume(Ingests ingests);

    public Ingests progress(Long id);

    public Boolean idleservers();
    public void doingSchedule();
}
