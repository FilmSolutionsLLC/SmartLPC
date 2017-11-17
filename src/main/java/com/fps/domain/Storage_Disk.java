package com.fps.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Storage_Disk.
 */
@Entity
@Table(name = "storage_disk")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "storage_disk")
public class Storage_Disk implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "size")
    private Float size;

    @Column(name = "used")
    private Float used;

    @Column(name = "available")
    private Float available;

    @Column(name = "last_updated")
    private LocalDate lastUpdated;

    @Column(name = "reserved")
    private Boolean reserved;

    @ManyToOne
    private Storage_Servers server;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public Float getUsed() {
        return used;
    }

    public void setUsed(Float used) {
        this.used = used;
    }

    public Float getAvailable() {
        return available;
    }

    public void setAvailable(Float available) {
        this.available = available;
    }


    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Boolean isReserved() {
        return reserved;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
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
        Storage_Disk storage_Disk = (Storage_Disk) o;
        if(storage_Disk.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, storage_Disk.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Storage_Disk{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", size='" + size + "'" +
            ", used='" + used + "'" +
            ", available='" + available + "'" +
            ", lastUpdated='" + lastUpdated + "'" +
            ", reserved='" + reserved + "'" +
            '}';
    }
}
