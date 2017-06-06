package com.fps.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Batch.
 */
@Entity
@Table(name = "batch")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "batch")
public class Batch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "created_time")
    private LocalDate createdTime;

    @Column(name = "updated_time")
    private LocalDate updatedTime;

    @Column(name = "cover_image_id")
    private Long coverImageId;

    @Column(name = "is_asset")
    private Boolean isAsset;

    @Column(name = "is_hidden")
    private Boolean isHidden;

    @ManyToOne
    private Projects project;

    @ManyToOne
    private User createdByAdminUser;

    @ManyToOne
    private User updatedByAdminUser;

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

    public LocalDate getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDate createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDate getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDate updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Long getCoverImageId() {
        return coverImageId;
    }

    public void setCoverImageId(Long coverImageId) {
        this.coverImageId = coverImageId;
    }

    public Boolean isIsAsset() {
        return isAsset;
    }

    public void setIsAsset(Boolean isAsset) {
        this.isAsset = isAsset;
    }

    public Boolean isIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }

    public Projects getProject() {
        return project;
    }

    public void setProject(Projects projects) {
        this.project = projects;
    }

    public User getCreatedByAdminUser() {
        return createdByAdminUser;
    }

    public void setCreatedByAdminUser(User user) {
        this.createdByAdminUser = user;
    }

    public User getUpdatedByAdminUser() {
        return updatedByAdminUser;
    }

    public void setUpdatedByAdminUser(User user) {
        this.updatedByAdminUser = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Batch batch = (Batch) o;
        if(batch.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, batch.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Batch{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", createdTime='" + createdTime + "'" +
            ", updatedTime='" + updatedTime + "'" +
            ", coverImageId='" + coverImageId + "'" +
            ", isAsset='" + isAsset + "'" +
            ", isHidden='" + isHidden + "'" +
            '}';
    }
}
