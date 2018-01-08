package com.fps.config.util;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by macbookpro on 12/11/16.
 */
@Component
public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_TENANT_ID = "master";
    @Autowired
    private DataSource master;

    @Autowired
    private DataSource slave;

    @Autowired
    private DataSource archive;


    private Map<String, DataSource> map;
    private final Logger log = LoggerFactory.getLogger(DataSourceBasedMultiTenantConnectionProviderImpl.class);

    @PostConstruct
    public void load() {
        map = new HashMap<>();
        map.put("master", master);
        map.put("slave", slave);
        //map.put("archive",archive);
    }

    @Override
    public DataSource selectAnyDataSource() {

        return map.get(DEFAULT_TENANT_ID);
    }

    @Override
    public DataSource selectDataSource(String tenantIdentifier) {
        if(tenantIdentifier.equals("master")){
            log.debug("Writing to Master Database");
        }else{
            log.debug("Reading from Slave Database");
        }


        return map.get(tenantIdentifier);
    }

}
