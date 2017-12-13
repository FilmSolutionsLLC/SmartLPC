package com.fps.domain;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_privilege_reviewers")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "contact_privilege_reviewers")
public class ContactPrivilegeReviewers {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "contact_privilege_id")
    private Long contactPrivilegeID;

    @Column(name = "reviewer")
    private String reviewer;

    @Column(name = "created_by_admin_user_id")
    private User createdByAdminUser;

    @Column(name = "updated_by_admin_user_id")
    private User updatedByAdminUser;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "tag_id")
    private Long tagID;

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

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
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

    public Long getTagID() {
        return tagID;
    }

    public void setTagID(Long tagID) {
        this.tagID = tagID;
    }

    @Override
    public String toString() {
        return "ContactPrivilegeReviewers{" +
            "id=" + id +
            ", contactPrivilegeID=" + contactPrivilegeID +
            ", reviewer='" + reviewer + '\'' +
            ", createdByAdminUser=" + createdByAdminUser +
            ", updatedByAdminUser=" + updatedByAdminUser +
            ", createdDate=" + createdDate +
            ", updatedDate=" + updatedDate +
            ", tagID=" + tagID +
            '}';
    }
}
