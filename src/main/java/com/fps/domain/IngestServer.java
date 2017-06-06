package com.fps.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * Created by macbookpro on 2/1/17.
 */
@Entity
@Table(name = "ingest_server")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "ingest_server")
public class IngestServer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "server_id")
    Long id;

    String dns;

    Integer available;

    ZonedDateTime lastUsed;

    public IngestServer() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long value) {
        this.id = value;
    }

    public String getDns() {
        return this.dns;
    }

    public void setDns(String value) {
        this.dns = value;
    }

    public Integer getAvailable() {
        return this.available;
    }

    public void setAvailable(Integer value) {
        this.available = value;
    }

    public ZonedDateTime getLastUsed() {
        return this.lastUsed;
    }

    public void setLastUsed(ZonedDateTime value) {
        this.lastUsed = value;
    }

    public IngestServer(String dns, Integer available, ZonedDateTime lastUsed) {
        this.dns = dns;
        this.available = available;
        this.lastUsed = lastUsed;
    }
}
