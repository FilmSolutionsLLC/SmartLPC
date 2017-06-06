package com.fps.config;

import com.fps.domain.Lookups;
import com.fps.repository.LookupsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by macbookpro on 5/4/17.
 */
@Configuration
@Component
public class IngestConfiguration {
    private final Logger log = LoggerFactory.getLogger(IngestConfiguration.class);


    private LookupsRepository lookupsRepository;

    public List<Lookups> ingestConfigs() {

        List<Lookups> lookupsList = lookupsRepository.findByTableName("config");
        log.info("=============================================================");
        for (Lookups lookups : lookupsList) {
            log.info(lookups.toString());
        }
        return lookupsList;
    }
}
