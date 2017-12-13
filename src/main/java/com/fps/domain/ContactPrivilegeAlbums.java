package com.fps.domain;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_privilege_albums")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "contact_privilege_albums")
public class ContactPrivilegeAlbums {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "contact_privilege_id")
    private Long contactPrivilegeID;

    @Column(name = "album_id")
    private String albumID;

    @Column(name = "created_by_admin_user_id")
    private User createdByAdminUser;

    @Column(name = "updated_by_admin_user_id")
    private User updatedByAdminUser;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "album_node_id")
    private Long albumNodeID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContactPrivilegeID() {
        return contactPrivilegeID;
    }

    public void setContactPrivilegeID(Long contactPrivilegeID) {
        this.contactPrivilegeID = contactPrivilegeID;
    }

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public User getCreatedByAdminUser() {
        return createdByAdminUser;
    }

    public void setCreatedByAdminUser(User createdByAdminUser) {
        this.createdByAdminUser = createdByAdminUser;
    }

    public User getUpdatedByAdminUser() {
        return updatedByAdminUser;
    }

    public void setUpdatedByAdminUser(User updatedByAdminUser) {
        this.updatedByAdminUser = updatedByAdminUser;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getAlbumNodeID() {
        return albumNodeID;
    }

    public void setAlbumNodeID(Long albumNodeID) {
        this.albumNodeID = albumNodeID;
    }

    @Override
    public String toString() {
        return "ContactPrivilegeAlbums{" +
            "id=" + id +
            ", contactPrivilegeID=" + contactPrivilegeID +
            ", albumID='" + albumID + '\'' +
            ", createdByAdminUser=" + createdByAdminUser +
            ", updatedByAdminUser=" + updatedByAdminUser +
            ", createdDate=" + createdDate +
            ", updatedDate=" + updatedDate +
            ", albumNodeID=" + albumNodeID +
            '}';
    }

}
