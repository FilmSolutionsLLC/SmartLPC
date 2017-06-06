package com.fps.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ContactPrivileges.
 */
@Entity
@Table(name = "contact_privileges")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "contactprivileges")
public class ContactPrivileges implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "author")
    private String author;

    @Column(name = "exec")
    private Boolean exec;

    @Column(name = "captioning")
    private Boolean captioning;

    @Column(name = "download_type")
    private Integer downloadType;

    @Column(name = "email")
    private Boolean email;

    @Column(name = "print")
    private Boolean print;

    @Column(name = "lock_approve_restriction")
    private Boolean lockApproveRestriction;

    @Column(name = "prioritypix")
    private Boolean priorityPix;

    @Column(name = "release_exclude")
    private Boolean releaseExclude;

    @Column(name = "view_sensitive")
    private Boolean viewSensitive;

    @Column(name = "watermark")
    private Boolean watermark;

    @Column(name = "watermark_inner_transparency")
    private Double watermarkInnerTransparency;

    @Column(name = "watermark_outer_transparency")
    private Double watermarkOuterTransparency;

    @Column(name = "internal")
    private Boolean internal;

    @Column(name = "vendor")
    private Boolean vendor;

    @Column(name = "restart_role")
    private String restartRole;

    @Column(name = "restart_image")
    private String restartImage;

    @Column(name = "restart_page")
    private Long restartPage;

    @Column(name = "last_login_dt")
    private ZonedDateTime lastLoginDt;

    @Column(name = "last_logout_dt")
    private ZonedDateTime lastLogoutDt;

    @Column(name = "disabled")
    private Boolean disabled;

    @Column(name = "welcome_message")
    private Boolean welcomeMessage;

    @Column(name = "isABCViewer")
    private Boolean isABCViewer;

    @Column(name = "talent_management")
    private Boolean talentManagement;

    @Column(name = "signoff_management")
    private Boolean signoffManagement;

    @Column(name = "datgedit_management")
    private Boolean datgeditManagement;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;

    @Column(name = "expire_date")
    private ZonedDateTime expireDate;

    @Column(name = "restart_filter")
    private String restartFilter;

    @Column(name = "restart_columns")
    private Integer restartColumns;

    @Column(name = "restart_images_per_page")
    private Integer restartImagesPerPage;

    @Column(name = "restart_image_size")
    private String restartImageSize;

    @Column(name = "restart_time")
    private Long restartTime;

    @Column(name = "show_finalizations")
    private Boolean showFinalizations;

    @Column(name = "read_only")
    private Boolean readOnly;

    @Column(name = "has_video")
    private Boolean hasVideo;

    @Column(name = "global_album")
    private Boolean globalAlbum;

    @Column(name = "sees_untagged")
    private Boolean seesUntagged;

    @Column(name = "login_count")
    private Integer loginCount;

    @Column(name = "exclusives")
    private Integer exclusives;

    @Column(name = "default_album")
    private Long defaultAlbum;

    @Column(name = "critique_it")
    private Boolean critiqueIt;

    @Column(name = "adhoc_link")
    private Boolean adhocLink;

    @Column(name = "retouch")
    private Boolean retouch;

    @Column(name = "file_upload")
    private Boolean fileUpload;

    @Column(name = "delete_assets")
    private Boolean deleteAssets;

    @ManyToOne
    private Projects project;

    @ManyToOne
    private Contacts contact;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boolean isExec() {
        return exec;
    }

    public void setExec(Boolean exec) {
        this.exec = exec;
    }

    public Boolean isCaptioning() {
        return captioning;
    }

    public void setCaptioning(Boolean captioning) {
        this.captioning = captioning;
    }

    public Integer getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(Integer downloadType) {
        this.downloadType = downloadType;
    }

    public Boolean isEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

    public Boolean isPrint() {
        return print;
    }

    public void setPrint(Boolean print) {
        this.print = print;
    }

    public Boolean isLockApproveRestriction() {
        return lockApproveRestriction;
    }

    public void setLockApproveRestriction(Boolean lockApproveRestriction) {
        this.lockApproveRestriction = lockApproveRestriction;
    }

    public Boolean isPriorityPix() {
        return priorityPix;
    }

    public void setPriorityPix(Boolean priorityPix) {
        this.priorityPix = priorityPix;
    }

    public Boolean isReleaseExclude() {
        return releaseExclude;
    }

    public void setReleaseExclude(Boolean releaseExclude) {
        this.releaseExclude = releaseExclude;
    }

    public Boolean isViewSensitive() {
        return viewSensitive;
    }

    public void setViewSensitive(Boolean viewSensitive) {
        this.viewSensitive = viewSensitive;
    }

    public Boolean isWatermark() {
        return watermark;
    }

    public void setWatermark(Boolean watermark) {
        this.watermark = watermark;
    }

    public Double getWatermarkInnerTransparency() {
        return watermarkInnerTransparency;
    }

    public void setWatermarkInnerTransparency(Double watermarkInnerTransparency) {
        this.watermarkInnerTransparency = watermarkInnerTransparency;
    }

    public Double getWatermarkOuterTransparency() {
        return watermarkOuterTransparency;
    }

    public void setWatermarkOuterTransparency(Double watermarkOuterTransparency) {
        this.watermarkOuterTransparency = watermarkOuterTransparency;
    }

    public Boolean isInternal() {
        return internal;
    }

    public void setInternal(Boolean internal) {
        this.internal = internal;
    }

    public Boolean isVendor() {
        return vendor;
    }

    public void setVendor(Boolean vendor) {
        this.vendor = vendor;
    }

    public String getRestartRole() {
        return restartRole;
    }

    public void setRestartRole(String restartRole) {
        this.restartRole = restartRole;
    }

    public String getRestartImage() {
        return restartImage;
    }

    public void setRestartImage(String restartImage) {
        this.restartImage = restartImage;
    }

    public Long getRestartPage() {
        return restartPage;
    }

    public void setRestartPage(Long restartPage) {
        this.restartPage = restartPage;
    }

    public ZonedDateTime getLastLoginDt() {
        return lastLoginDt;
    }

    public void setLastLoginDt(ZonedDateTime lastLoginDt) {
        this.lastLoginDt = lastLoginDt;
    }

    public ZonedDateTime getLastLogoutDt() {
        return lastLogoutDt;
    }

    public void setLastLogoutDt(ZonedDateTime lastLogoutDt) {
        this.lastLogoutDt = lastLogoutDt;
    }

    public Boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean isWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(Boolean welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public Boolean isIsABCViewer() {
        return isABCViewer;
    }

    public void setIsABCViewer(Boolean isABCViewer) {
        this.isABCViewer = isABCViewer;
    }

    public Boolean isTalentManagement() {
        return talentManagement;
    }

    public void setTalentManagement(Boolean talentManagement) {
        this.talentManagement = talentManagement;
    }

    public Boolean isSignoffManagement() {
        return signoffManagement;
    }

    public void setSignoffManagement(Boolean signoffManagement) {
        this.signoffManagement = signoffManagement;
    }

    public Boolean isDatgeditManagement() {
        return datgeditManagement;
    }

    public void setDatgeditManagement(Boolean datgeditManagement) {
        this.datgeditManagement = datgeditManagement;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public ZonedDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(ZonedDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public String getRestartFilter() {
        return restartFilter;
    }

    public void setRestartFilter(String restartFilter) {
        this.restartFilter = restartFilter;
    }

    public Integer getRestartColumns() {
        return restartColumns;
    }

    public void setRestartColumns(Integer restartColumns) {
        this.restartColumns = restartColumns;
    }

    public Integer getRestartImagesPerPage() {
        return restartImagesPerPage;
    }

    public void setRestartImagesPerPage(Integer restartImagesPerPage) {
        this.restartImagesPerPage = restartImagesPerPage;
    }

    public String getRestartImageSize() {
        return restartImageSize;
    }

    public void setRestartImageSize(String restartImageSize) {
        this.restartImageSize = restartImageSize;
    }

    public Long getRestartTime() {
        return restartTime;
    }

    public void setRestartTime(Long restartTime) {
        this.restartTime = restartTime;
    }

    public Boolean isShowFinalizations() {
        return showFinalizations;
    }

    public void setShowFinalizations(Boolean showFinalizations) {
        this.showFinalizations = showFinalizations;
    }

    public Boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Boolean isHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(Boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public Boolean isGlobalAlbum() {
        return globalAlbum;
    }

    public void setGlobalAlbum(Boolean globalAlbum) {
        this.globalAlbum = globalAlbum;
    }

    public Boolean isSeesUntagged() {
        return seesUntagged;
    }

    public void setSeesUntagged(Boolean seesUntagged) {
        this.seesUntagged = seesUntagged;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Integer getExclusives() {
        return exclusives;
    }

    public void setExclusives(Integer exclusives) {
        this.exclusives = exclusives;
    }

    public Long getDefaultAlbum() {
        return defaultAlbum;
    }

    public void setDefaultAlbum(Long defaultAlbum) {
        this.defaultAlbum = defaultAlbum;
    }

    public Boolean isCritiqueIt() {
        return critiqueIt;
    }

    public void setCritiqueIt(Boolean critiqueIt) {
        this.critiqueIt = critiqueIt;
    }

    public Boolean isAdhocLink() {
        return adhocLink;
    }

    public void setAdhocLink(Boolean adhocLink) {
        this.adhocLink = adhocLink;
    }

    public Boolean isRetouch() {
        return retouch;
    }

    public void setRetouch(Boolean retouch) {
        this.retouch = retouch;
    }

    public Boolean isFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(Boolean fileUpload) {
        this.fileUpload = fileUpload;
    }

    public Boolean isDeleteAssets() {
        return deleteAssets;
    }

    public void setDeleteAssets(Boolean deleteAssets) {
        this.deleteAssets = deleteAssets;
    }

    public Projects getProject() {
        return project;
    }

    public void setProject(Projects projects) {
        this.project = projects;
    }

    public Contacts getContact() {
        return contact;
    }

    public void setContact(Contacts contacts) {
        this.contact = contacts;
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
        ContactPrivileges contactPrivileges = (ContactPrivileges) o;
        if (contactPrivileges.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, contactPrivileges.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ContactPrivileges{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", author='" + author + '\'' +
            ", exec=" + exec +
            ", captioning=" + captioning +
            ", downloadType=" + downloadType +
            ", email=" + email +
            ", print=" + print +
            ", lockApproveRestriction=" + lockApproveRestriction +
            ", priorityPix=" + priorityPix +
            ", releaseExclude=" + releaseExclude +
            ", viewSensitive=" + viewSensitive +
            ", watermark=" + watermark +
            ", watermarkInnerTransparency=" + watermarkInnerTransparency +
            ", watermarkOuterTransparency=" + watermarkOuterTransparency +
            ", internal=" + internal +
            ", vendor=" + vendor +
            ", restartRole='" + restartRole + '\'' +
            ", restartImage='" + restartImage + '\'' +
            ", restartPage=" + restartPage +
            ", lastLoginDt=" + lastLoginDt +
            ", lastLogoutDt=" + lastLogoutDt +
            ", disabled=" + disabled +
            ", welcomeMessage=" + welcomeMessage +
            ", isABCViewer=" + isABCViewer +
            ", talentManagement=" + talentManagement +
            ", signoffManagement=" + signoffManagement +
            ", datgeditManagement=" + datgeditManagement +
            ", createdDate=" + createdDate +
            ", updatedDate=" + updatedDate +
            ", expireDate=" + expireDate +
            ", restartFilter='" + restartFilter + '\'' +
            ", restartColumns=" + restartColumns +
            ", restartImagesPerPage=" + restartImagesPerPage +
            ", restartImageSize='" + restartImageSize + '\'' +
            ", restartTime=" + restartTime +
            ", showFinalizations=" + showFinalizations +
            ", readOnly=" + readOnly +
            ", hasVideo=" + hasVideo +
            ", globalAlbum=" + globalAlbum +
            ", seesUntagged=" + seesUntagged +
            ", loginCount=" + loginCount +
            ", exclusives=" + exclusives +
            ", defaultAlbum=" + defaultAlbum +
            ", critiqueIt=" + critiqueIt +
            ", adhocLink=" + adhocLink +
            ", retouch=" + retouch +
            ", fileUpload=" + fileUpload +
            ", deleteAssets=" + deleteAssets +
            ", project=" + project +
            ", contact=" + contact +
            ", createdByAdminUser=" + createdByAdminUser +
            ", updatedByAdminUser=" + updatedByAdminUser +
            '}';

    }
}
