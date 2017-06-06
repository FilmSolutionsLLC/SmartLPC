package com.fps.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Image.
 */
@Entity
@Table(name = "image")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "image")
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "caption_id")
    private Long captionId;

    @Column(name = "name")
    private String name;

    @Column(name = "comment_description")
    private String commentDescription;

    @Column(name = "release_time")
    private ZonedDateTime releaseTime;

    @Column(name = "ingest_time")
    private ZonedDateTime ingestTime;

    @Column(name = "quickpick_selected_time")
    private ZonedDateTime quickpickSelectedTime;

    @Column(name = "created_time")
    private ZonedDateTime createdTime;

    @Column(name = "updated_time")
    private ZonedDateTime updatedTime;

    @Column(name = "photographer")
    private String photographer;

    @Column(name = "video")
    private Boolean video;

    @Column(name = "hidden")
    private Boolean hidden;

    @Column(name = "web_upload")
    private Boolean webUpload;

    @ManyToOne
    private Batch batch;

    @ManyToOne
    private User createdByUser;

    @ManyToOne
    private User updatedByUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCaptionId() {
        return captionId;
    }

    public void setCaptionId(Long captionId) {
        this.captionId = captionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommentDescription() {
        return commentDescription;
    }

    public void setCommentDescription(String commentDescription) {
        this.commentDescription = commentDescription;
    }

    public ZonedDateTime getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(ZonedDateTime releaseTime) {
        this.releaseTime = releaseTime;
    }

    public ZonedDateTime getIngestTime() {
        return ingestTime;
    }

    public void setIngestTime(ZonedDateTime ingestTime) {
        this.ingestTime = ingestTime;
    }

    public ZonedDateTime getQuickpickSelectedTime() {
        return quickpickSelectedTime;
    }

    public void setQuickpickSelectedTime(ZonedDateTime quickpickSelectedTime) {
        this.quickpickSelectedTime = quickpickSelectedTime;
    }

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(ZonedDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getPhotographer() {
        return photographer;
    }

    public void setPhotographer(String photographer) {
        this.photographer = photographer;
    }

    public Boolean isVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Boolean isHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean isWebUpload() {
        return webUpload;
    }

    public void setWebUpload(Boolean webUpload) {
        this.webUpload = webUpload;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User user) {
        this.createdByUser = user;
    }

    public User getUpdatedByUser() {
        return updatedByUser;
    }

    public void setUpdatedByUser(User user) {
        this.updatedByUser = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Image image = (Image) o;
        if(image.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, image.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Image{" +
            "id=" + id +
            ", captionId='" + captionId + "'" +
            ", name='" + name + "'" +
            ", commentDescription='" + commentDescription + "'" +
            ", releaseTime='" + releaseTime + "'" +
            ", ingestTime='" + ingestTime + "'" +
            ", quickpickSelectedTime='" + quickpickSelectedTime + "'" +
            ", createdTime='" + createdTime + "'" +
            ", updatedTime='" + updatedTime + "'" +
            ", photographer='" + photographer + "'" +
            ", video='" + video + "'" +
            ", hidden='" + hidden + "'" +
            ", webUpload='" + webUpload + "'" +
            '}';
    }
}
