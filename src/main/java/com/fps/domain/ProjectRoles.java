package com.fps.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ProjectRoles.
 */
@Entity
@Table(name = "project_roles")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "projectroles")
public class ProjectRoles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "relationship_type")
    private String relationship_type;

    @Column(name = "solo_kill_pct")
    private Float soloKillPct;

    @Column(name = "group_kill_pct")
    private Float groupKillPct;

    @Column(name = "mini_final_dt")
    private LocalDate miniFullDt;

    @Column(name = "full_final_dt")
    private LocalDate fullFinalDt;

    @Column(name = "disabled")
    private Boolean disabled;

    @Column(name = "character_name")
    private String characterName;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "days_working")
    private Integer daysWorking;

    @Column(name = "exc_sologroup")
    private Boolean excSologroup;

    @Column(name = "notes")
    private String notes;

    @Column(name = "tag_name")
    private String tagName;

    @Column(name = "hotkey_value")
    private String hotkeyValue;

    @Column(name = "expire_date")
    private LocalDate expireDate;

    @Column(name = "tertiary_kill_pct")
    private Float tertiaryKillPct;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @Column(name = "welcome_message")
    String welcomeMessage;

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

    public Float getSoloKillPct() {
        return soloKillPct;
    }

    public void setSoloKillPct(Float soloKillPct) {
        this.soloKillPct = soloKillPct;
    }

    public Float getGroupKillPct() {
        return groupKillPct;
    }

    public void setGroupKillPct(Float groupKillPct) {
        this.groupKillPct = groupKillPct;
    }

    public LocalDate getMiniFullDt() {
        return miniFullDt;
    }

    public void setMiniFullDt(LocalDate miniFullDt) {
        this.miniFullDt = miniFullDt;
    }

    public LocalDate getFullFinalDt() {
        return fullFinalDt;
    }

    public void setFullFinalDt(LocalDate fullFinalDt) {
        this.fullFinalDt = fullFinalDt;
    }

    public Boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Integer getDaysWorking() {
        return daysWorking;
    }

    public void setDaysWorking(Integer daysWorking) {
        this.daysWorking = daysWorking;
    }

    public Boolean isExcSologroup() {
        return excSologroup;
    }

    public void setExcSologroup(Boolean excSologroup) {
        this.excSologroup = excSologroup;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getHotkeyValue() {
        return hotkeyValue;
    }

    public void setHotkeyValue(String hotkeyValue) {
        this.hotkeyValue = hotkeyValue;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public Float getTertiaryKillPct() {
        return tertiaryKillPct;
    }

    public void setTertiaryKillPct(Float tertiaryKillPct) {
        this.tertiaryKillPct = tertiaryKillPct;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
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

    public String getRelationship_type() {
        return relationship_type;
    }

    public void setRelationship_type(String relationship_type) {
        this.relationship_type = relationship_type;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectRoles projectRoles = (ProjectRoles) o;
        if (projectRoles.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, projectRoles.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectRoles{" +
            "id=" + id +
            ", relationship_type='" + relationship_type + '\'' +
            ", soloKillPct=" + soloKillPct +
            ", groupKillPct=" + groupKillPct +
            ", miniFullDt=" + miniFullDt +
            ", fullFinalDt=" + fullFinalDt +
            ", disabled=" + disabled +
            ", characterName='" + characterName + '\'' +
            ", startDate=" + startDate +
            ", daysWorking=" + daysWorking +
            ", excSologroup=" + excSologroup +
            ", notes='" + notes + '\'' +
            ", tagName='" + tagName + '\'' +
            ", hotkeyValue='" + hotkeyValue + '\'' +
            ", expireDate=" + expireDate +
            ", tertiaryKillPct=" + tertiaryKillPct +
            ", createdDate=" + createdDate +
            ", updatedDate=" + updatedDate +
            ", welcomeMessage='" + welcomeMessage + '\'' +
            ", project=" + project +
            ", contact=" + contact +
            ", createdByAdminUser=" + createdByAdminUser +
            ", updatedByAdminUser=" + updatedByAdminUser +
            '}';
    }
}
