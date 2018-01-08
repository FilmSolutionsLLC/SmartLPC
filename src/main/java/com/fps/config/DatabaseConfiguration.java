package com.fps.config;


import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
//@EnableJpaRepositories("com.fps.repository")
//@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
//@EnableTransactionManagement
//@EnableElasticsearchRepositories("com.fps.elastics.search")
public class DatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);


    @Inject
    private Environment env;

    @Autowired(required = false)
    private MetricRegistry metricRegistry;


    @Value("${spring.datasource.url2}")
    String slaveurl;

    @Value("${spring.datasource.urlArchive}")
    String archiveurl;



    @Value("${spring.datasource.username2}")
    String slaveUsername;

    @Value("${spring.datasource.password2}")
    String slavePassword;


    //@Bean(destroyMethod = "close")
    @Bean(name = "master")
    @Primary
    @ConditionalOnExpression("#{!environment.acceptsProfiles('" + Constants.SPRING_PROFILE_CLOUD + "') && !environment.acceptsProfiles('" + Constants.SPRING_PROFILE_HEROKU + "')}")
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
        log.debug("Configuring Datasource");
        if (dataSourceProperties.getUrl() == null) {
            log.error("Your database connection pool configuration is incorrect! The application" +
                    " cannot start. Please check your Spring profile, current profiles are: {}",
                Arrays.toString(env.getActiveProfiles()));

            throw new ApplicationContextException("Database connection pool is not configured correctly");
        }
        HikariDataSource hikariDataSource = (HikariDataSource) DataSourceBuilder
            .create(dataSourceProperties.getClassLoader())
            .type(HikariDataSource.class)
            .driverClassName(dataSourceProperties.getDriverClassName())
            .url(dataSourceProperties.getUrl())
            .username(dataSourceProperties.getUsername())
            .password(dataSourceProperties.getPassword())
            .build();

        if (metricRegistry != null) {
            hikariDataSource.setMetricRegistry(metricRegistry);
        }
        log.debug("Configuring Master Datasource Successfully : " + dataSourceProperties.getUrl());

        return hikariDataSource;
    }

    @Bean(name = "slave")
    @ConditionalOnExpression("#{!environment.acceptsProfiles('" + Constants.SPRING_PROFILE_CLOUD + "') && !environment.acceptsProfiles('" + Constants.SPRING_PROFILE_HEROKU + "')}")
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource slave(DataSourceProperties dataSourceProperties) {
        log.debug("Configuring Slave Datasource");
        if (dataSourceProperties.getUrl() == null) {
            log.error("Your  slave database connection pool configuration is incorrect! The application" +
                    " cannot start. Please check your Spring profile, current profiles are: {}",
                Arrays.toString(env.getActiveProfiles()));

            throw new ApplicationContextException(" Slave Database connection pool is not configured correctly");
        }
        HikariDataSource hikariDataSource = (HikariDataSource) DataSourceBuilder
            .create(dataSourceProperties.getClassLoader())
            .type(HikariDataSource.class)
            .driverClassName(dataSourceProperties.getDriverClassName())
            .url(slaveurl)
            .username(slaveUsername)
            .password(slavePassword)
            .build();

        if (metricRegistry != null) {
            hikariDataSource.setMetricRegistry(metricRegistry);
        }
        log.debug("Configuring Slave Datasource Successfully : " + slaveurl);

        return hikariDataSource;
    }

    /*@Bean(name = "archive")
    @ConditionalOnExpression("#{!environment.acceptsProfiles('" + Constants.SPRING_PROFILE_CLOUD + "') && !environment.acceptsProfiles('" + Constants.SPRING_PROFILE_HEROKU + "')}")
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource archive(DataSourceProperties dataSourceProperties) {
        log.debug("Configuring Archive Datasource");
        if (dataSourceProperties.getUrl() == null) {
            log.error("Your  archive database connection pool configuration is incorrect! The application" +
                    " cannot start. Please check your Spring profile, current profiles are: {}",
                Arrays.toString(env.getActiveProfiles()));

            throw new ApplicationContextException(" Archive Database connection pool is not configured correctly");
        }
        HikariDataSource hikariDataSource = (HikariDataSource) DataSourceBuilder
            .create(dataSourceProperties.getClassLoader())
            .type(HikariDataSource.class)
            .driverClassName(dataSourceProperties.getDriverClassName())
            .url(archiveurl)
            .username(slaveUsername)
            .password(slavePassword)
            .build();

        if (metricRegistry != null) {
            hikariDataSource.setMetricRegistry(metricRegistry);
        }
        log.debug("Configuring Slave Datasource Successfully : " + archiveurl);

        return hikariDataSource;
    }*/


    @Bean
    public Hibernate4Module hibernate4Module() {
        return new Hibernate4Module();
    }
}
