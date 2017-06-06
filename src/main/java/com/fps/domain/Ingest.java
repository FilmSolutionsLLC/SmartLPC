package com.fps.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Ingest.
 */
@Entity
@Table(name = "ingest")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "ingest")
public class Ingest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ingest_start_time")
    private ZonedDateTime ingestStartTime;

    @Column(name = "ingest_completed_time")
    private ZonedDateTime ingestCompletedTime;

    @Column(name = "total_images")
    private Integer totalImages;

    @Column(name = "total_done")
    private Double totalDone;

    @Column(name = "completed")
        private Boolean completed;

    @ManyToOne
    private User user;

    @ManyToOne
    private Lookups action;

    @ManyToOne
    private Storage_Servers server;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getIngestStartTime() {
        return ingestStartTime;
    }

    public void setIngestStartTime(ZonedDateTime ingestStartTime) {
        this.ingestStartTime = ingestStartTime;
    }

    public ZonedDateTime getIngestCompletedTime() {
        return ingestCompletedTime;
    }

    public void setIngestCompletedTime(ZonedDateTime ingestCompletedTime) {
        this.ingestCompletedTime = ingestCompletedTime;
    }

    public Integer getTotalImages() {
        return totalImages;
    }

    public void setTotalImages(Integer totalImages) {
        this.totalImages = totalImages;
    }

    public Double getTotalDone() {
        return totalDone;
    }

    public void setTotalDone(Double totalDone) {
        this.totalDone = totalDone;
    }

    public Boolean isCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Lookups getAction() {
        return action;
    }

    public void setAction(Lookups lookups) {
        this.action = lookups;
    }

    public Storage_Servers getServer() {
        return server;
    }

    public void setServer(Storage_Servers storage_Servers) {
        this.server = storage_Servers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ingest ingest = (Ingest) o;
        if(ingest.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, ingest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Ingest{" +
            "id=" + id +
            ", ingestStartTime='" + ingestStartTime + "'" +
            ", ingestCompletedTime='" + ingestCompletedTime + "'" +
            ", totalImages='" + totalImages + "'" +
            ", totalDone='" + totalDone + "'" +
            ", completed='" + completed + "'" +
            '}';
    }
}
