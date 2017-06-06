package com.fps.repository;

import com.fps.domain.Ingests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Created by macbookpro on 2/1/17.
 */
public interface IngestsRepository extends JpaRepository<Ingests, Long> {

    //Ingests findByIngestJobAndStatus(String ingestJob, String status);

    Ingests findByAlfrescoTitle1AndAlfrescoTitle2(String alfrescoTitle1, String alfrescoTitle2);

    List<Ingests> findByStatus(String status);

    @Query("select i from Ingests i where i.status in :statuses")
    Set<Ingests> getByStatus(@Param("statuses") Set<String> statuses);


}
