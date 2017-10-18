package com.fps.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Projects.
 */
@Entity
@Table(name = "projects")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "projects")
public class Projects implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "run_of_show_flag")
    private Boolean runOfShowFlag;

    @Column(name = "template")
    private Boolean template;

    @Column(name = "lab_flag")
    private Boolean labFlag;

    @Column(name = "alfresco_title_1")
    private String alfrescoTitle1;

    @Column(name = "alfresco_title_2")
    private String alfrescoTitle2;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "actors_with_rights")
    private Long actorsWithRights;

    @Column(name = "days_shooting")
    private Long daysShooting;

    @Column(name = "weeks_shooting")
    private Long weeksShooting;

    @Column(name = "notes")
    private String notes;

    @Column(name = "sensitive_viewing")
    private Boolean sensitiveViewing;

    @Column(name = "production_company_notes")
    private String productionCompanyNotes;

    @Column(name = "production_company_shipping_number")
    private String productionCompanyShippingNumber;

    @Column(name = "processing_deliveries")
    private String processingDeliveries;

    @Column(name = "processing_special_instructions")
    private String processingSpecialInstructions;

    @Column(name = "processing_watermark")
    private String processingWatermark;

    @Column(name = "processing_copyright")
    private String processingCopyright;


    @Column(name = "lab_proof_notes")
    private String labProofNotes;

    @Column(name = "lab_last_proof_image_number")
    private String labLastProofImageNumber;

    @Column(name = "lab_last_proof_page_number")
    private String labLastProofPageNumber;

    @Column(name = "lab_image_number_schema")
    private String labImageNumberSchema;

    @Column(name = "lab_folder_batch_schema")
    private String labFolderBatchSchema;

    @Column(name = "photo_lab_info")
    private String photoLabInfo;

    @Column(name = "project_unit_photo_notes")
    private String projectUnitPhotoNotes;

    @Column(name = "project_info_notes")
    private String projectInfoNotes;

    @Column(name = "created_date")
    @JsonIgnore
    private LocalDate createdDate;

    @Column(name = "updated_date")
    @JsonIgnore
    private LocalDate updatedDate;

    @Column(name = "legacy_director")
    private String legacyDirector;

    @Column(name = "legacy_executive_producer")
    private String legacyExecutiveProducer;

    @Column(name = "legacy_executive_producer_2")
    private String legacyExecutiveProducer2;

    @Column(name = "legacy_executive_producer_3")
    private String legacyExecutiveProducer3;

    @Column(name = "legacy_executive_producer_4")
    private String legacyExecutiveProducer4;

    @Column(name = "legacy_producer")
    private String legacyProducer;

    @Column(name = "legacy_producer_2")
    private String legacyProducer2;

    @Column(name = "legacy_producer_3")
    private String legacyProducer3;

    @Column(name = "legacy_producer_4")
    private String legacyProducer4;

    @Column(name = "legacy_additional_talent")
    private String legacyAdditionalTalent;

    @Column(name = "theme_id")
    private Boolean themeId;

    @Column(name = "spt_photo_subtype")
    private String sptPhotoSubtype;

    @Column(name = "photo_credit")
    private String photoCredit;

    @Column(name = "shoot_date")
    private LocalDate shootDate;

    @Column(name = "shoot_date_override")
    private Boolean shootDateOverride;

    @Column(name = "unit_photographer_override")
    private Boolean unitPhotographerOverride;

    @Column(name = "use_setup")
    private Boolean useSetup;

    @Column(name = "use_exif")
    private Boolean useExif;

    @Column(name = "tag_date")
    private LocalDate tagDate;

    @Column(name = "tagreport_index")
    private Integer tagreportIndex;

    @Column(name = "login_message")
    private String loginMessage;

    @Column(name = "login_message_active")
    private Boolean loginMessageActive;

    @Column(name = "top_level_albums")
    private Boolean topLevelAlbums;

    @Column(name = "enable_tertiary")
    private Boolean enableTertiary;

    @Column(name = "invoice_created")
    private Long invoiceCreated;

    @Column(name = "price")
    private String price;

    @Column(name = "fox_title")
    private String foxTitle;

    @Column(name = "is_asset")
    private Boolean isAsset;

    @Column(name = "full_rejection")
    private Integer fullRejection;

    @Column(name = "disabled")
    private Boolean disabled;

    @Column(name = "reminder_date")
    private LocalDate reminderDate;

    @Column(name = "photo_credit_override")
    private Boolean photoCreditOverride;

    @Column(name = "legacy_name_notes")
    private String legacyNameNotes;


    @Column(name = "current_tagger")
    private String currentTagger;

    @Column(name = "current_tagger_1")
    private String currentTagger1;

    @Column(name = "current_tagger_2")
    private String currentTagger2;

    @ManyToOne
    private Lookups status;

    @ManyToOne
    private Lookups type;

    @ManyToOne
    private Contacts productionCompanyContact;

    @ManyToOne
    private Contacts parentCompanyContact;

    @ManyToOne
    private Lookups processingOriginalFileType;

    @ManyToOne
    @JsonIgnore
    private User createdByAdminUser;

    @ManyToOne
    @JsonIgnore
    private User updatedByAdminUser;

    @ManyToOne
    private Departments department;

    @ManyToOne
    private Lookups projectType;

    @ManyToOne
    private Contacts owner;

    @ManyToOne
    private Storage_Disk imageLocation;

    @ManyToOne
    private Storage_Disk imageLocationRemote;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Boolean isRunOfShowFlag() {
        return runOfShowFlag;
    }

    public void setRunOfShowFlag(Boolean runOfShowFlag) {
        this.runOfShowFlag = runOfShowFlag;
    }

    public Boolean isTemplate() {
        return template;
    }

    public void setTemplate(Boolean template) {
        this.template = template;
    }

    public Boolean isLabFlag() {
        return labFlag;
    }

    public void setLabFlag(Boolean labFlag) {
        this.labFlag = labFlag;
    }

    public String getAlfrescoTitle1() {
        return alfrescoTitle1;
    }

    public void setAlfrescoTitle1(String alfrescoTitle1) {
        this.alfrescoTitle1 = alfrescoTitle1;
    }

    public String getAlfrescoTitle2() {
        return alfrescoTitle2;
    }

    public void setAlfrescoTitle2(String alfrescoTitle2) {
        this.alfrescoTitle2 = alfrescoTitle2;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getActorsWithRights() {
        return actorsWithRights;
    }

    public void setActorsWithRights(Long actorsWithRights) {
        this.actorsWithRights = actorsWithRights;
    }

    public Long getDaysShooting() {
        return daysShooting;
    }

    public void setDaysShooting(Long daysShooting) {
        this.daysShooting = daysShooting;
    }

    public Long getWeeksShooting() {
        return weeksShooting;
    }

    public void setWeeksShooting(Long weeksShooting) {
        this.weeksShooting = weeksShooting;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean isSensitiveViewing() {
        return sensitiveViewing;
    }

    public void setSensitiveViewing(Boolean sensitiveViewing) {
        this.sensitiveViewing = sensitiveViewing;
    }

    public String getProductionCompanyNotes() {
        return productionCompanyNotes;
    }

    public void setProductionCompanyNotes(String productionCompanyNotes) {
        this.productionCompanyNotes = productionCompanyNotes;
    }

    public String getProductionCompanyShippingNumber() {
        return productionCompanyShippingNumber;
    }

    public void setProductionCompanyShippingNumber(String productionCompanyShippingNumber) {
        this.productionCompanyShippingNumber = productionCompanyShippingNumber;
    }

    public String getProcessingDeliveries() {
        return processingDeliveries;
    }

    public void setProcessingDeliveries(String processingDeliveries) {
        this.processingDeliveries = processingDeliveries;
    }

    public String getProcessingSpecialInstructions() {
        return processingSpecialInstructions;
    }

    public void setProcessingSpecialInstructions(String processingSpecialInstructions) {
        this.processingSpecialInstructions = processingSpecialInstructions;
    }

    public String getProcessingWatermark() {
        return processingWatermark;
    }

    public void setProcessingWatermark(String processingWatermark) {
        this.processingWatermark = processingWatermark;
    }

    public String getProcessingCopyright() {
        return processingCopyright;
    }

    public void setProcessingCopyright(String processingCopyright) {
        this.processingCopyright = processingCopyright;
    }

    public String getLabProofNotes() {
        return labProofNotes;
    }

    public void setLabProofNotes(String labProofNotes) {
        this.labProofNotes = labProofNotes;
    }

    public String getLabLastProofImageNumber() {
        return labLastProofImageNumber;
    }

    public void setLabLastProofImageNumber(String labLastProofImageNumber) {
        this.labLastProofImageNumber = labLastProofImageNumber;
    }

    public String getLabLastProofPageNumber() {
        return labLastProofPageNumber;
    }

    public void setLabLastProofPageNumber(String labLastProofPageNumber) {
        this.labLastProofPageNumber = labLastProofPageNumber;
    }

    public String getLabImageNumberSchema() {
        return labImageNumberSchema;
    }

    public void setLabImageNumberSchema(String labImageNumberSchema) {
        this.labImageNumberSchema = labImageNumberSchema;
    }

    public String getLabFolderBatchSchema() {
        return labFolderBatchSchema;
    }

    public void setLabFolderBatchSchema(String labFolderBatchSchema) {
        this.labFolderBatchSchema = labFolderBatchSchema;
    }

    public String getPhotoLabInfo() {
        return photoLabInfo;
    }

    public void setPhotoLabInfo(String photoLabInfo) {
        this.photoLabInfo = photoLabInfo;
    }

    public String getProjectUnitPhotoNotes() {
        return projectUnitPhotoNotes;
    }

    public void setProjectUnitPhotoNotes(String projectUnitPhotoNotes) {
        this.projectUnitPhotoNotes = projectUnitPhotoNotes;
    }

    public String getProjectInfoNotes() {
        return projectInfoNotes;
    }

    public void setProjectInfoNotes(String projectInfoNotes) {
        this.projectInfoNotes = projectInfoNotes;
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

    public String getLegacyDirector() {
        return legacyDirector;
    }

    public void setLegacyDirector(String legacyDirector) {
        this.legacyDirector = legacyDirector;
    }

    public String getLegacyExecutiveProducer() {
        return legacyExecutiveProducer;
    }

    public void setLegacyExecutiveProducer(String legacyExecutiveProducer) {
        this.legacyExecutiveProducer = legacyExecutiveProducer;
    }

    public String getLegacyExecutiveProducer2() {
        return legacyExecutiveProducer2;
    }

    public void setLegacyExecutiveProducer2(String legacyExecutiveProducer2) {
        this.legacyExecutiveProducer2 = legacyExecutiveProducer2;
    }

    public String getLegacyExecutiveProducer3() {
        return legacyExecutiveProducer3;
    }

    public void setLegacyExecutiveProducer3(String legacyExecutiveProducer3) {
        this.legacyExecutiveProducer3 = legacyExecutiveProducer3;
    }

    public String getLegacyExecutiveProducer4() {
        return legacyExecutiveProducer4;
    }

    public void setLegacyExecutiveProducer4(String legacyExecutiveProducer4) {
        this.legacyExecutiveProducer4 = legacyExecutiveProducer4;
    }

    public String getLegacyProducer() {
        return legacyProducer;
    }

    public void setLegacyProducer(String legacyProducer) {
        this.legacyProducer = legacyProducer;
    }

    public String getLegacyProducer2() {
        return legacyProducer2;
    }

    public void setLegacyProducer2(String legacyProducer2) {
        this.legacyProducer2 = legacyProducer2;
    }

    public String getLegacyProducer3() {
        return legacyProducer3;
    }

    public void setLegacyProducer3(String legacyProducer3) {
        this.legacyProducer3 = legacyProducer3;
    }

    public String getLegacyProducer4() {
        return legacyProducer4;
    }

    public void setLegacyProducer4(String legacyProducer4) {
        this.legacyProducer4 = legacyProducer4;
    }

    public String getLegacyAdditionalTalent() {
        return legacyAdditionalTalent;
    }

    public void setLegacyAdditionalTalent(String legacyAdditionalTalent) {
        this.legacyAdditionalTalent = legacyAdditionalTalent;
    }

    public Boolean isThemeId() {
        return themeId;
    }

    public void setThemeId(Boolean themeId) {
        this.themeId = themeId;
    }

    public String getSptPhotoSubtype() {
        return sptPhotoSubtype;
    }

    public void setSptPhotoSubtype(String sptPhotoSubtype) {
        this.sptPhotoSubtype = sptPhotoSubtype;
    }

    public String getPhotoCredit() {
        return photoCredit;
    }

    public void setPhotoCredit(String photoCredit) {
        this.photoCredit = photoCredit;
    }

    public LocalDate getShootDate() {
        return shootDate;
    }

    public void setShootDate(LocalDate shootDate) {
        this.shootDate = shootDate;
    }

    public Boolean isShootDateOverride() {
        return shootDateOverride;
    }

    public void setShootDateOverride(Boolean shootDateOverride) {
        this.shootDateOverride = shootDateOverride;
    }

    public Boolean isUnitPhotographerOverride() {
        return unitPhotographerOverride;
    }

    public void setUnitPhotographerOverride(Boolean unitPhotographerOverride) {
        this.unitPhotographerOverride = unitPhotographerOverride;
    }

    public Boolean isUseSetup() {
        return useSetup;
    }

    public void setUseSetup(Boolean useSetup) {
        this.useSetup = useSetup;
    }

    public Boolean isUseExif() {
        return useExif;
    }

    public void setUseExif(Boolean useExif) {
        this.useExif = useExif;
    }

    public LocalDate getTagDate() {
        return tagDate;
    }

    public void setTagDate(LocalDate tagDate) {
        this.tagDate = tagDate;
    }

    public Integer getTagreportIndex() {
        return tagreportIndex;
    }

    public void setTagreportIndex(Integer tagreportIndex) {
        this.tagreportIndex = tagreportIndex;
    }

    public String getLoginMessage() {
        return loginMessage;
    }

    public void setLoginMessage(String loginMessage) {
        this.loginMessage = loginMessage;
    }

    public Boolean isLoginMessageActive() {
        return loginMessageActive;
    }

    public void setLoginMessageActive(Boolean loginMessageActive) {
        this.loginMessageActive = loginMessageActive;
    }

    public Boolean isTopLevelAlbums() {
        return topLevelAlbums;
    }

    public void setTopLevelAlbums(Boolean topLevelAlbums) {
        this.topLevelAlbums = topLevelAlbums;
    }

    public Boolean isEnableTertiary() {
        return enableTertiary;
    }

    public void setEnableTertiary(Boolean enableTertiary) {
        this.enableTertiary = enableTertiary;
    }

    public Long getInvoiceCreated() {
        return invoiceCreated;
    }

    public void setInvoiceCreated(Long invoiceCreated) {
        this.invoiceCreated = invoiceCreated;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFoxTitle() {
        return foxTitle;
    }

    public void setFoxTitle(String foxTitle) {
        this.foxTitle = foxTitle;
    }

    public Boolean isIsAsset() {
        return isAsset;
    }

    public void setIsAsset(Boolean isAsset) {
        this.isAsset = isAsset;
    }

    public Integer getFullRejection() {
        return fullRejection;
    }

    public void setFullRejection(Integer fullRejection) {
        this.fullRejection = fullRejection;
    }

    public Boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public LocalDate getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
    }

    public Boolean isPhotoCreditOverride() {
        return photoCreditOverride;
    }

    public void setPhotoCreditOverride(Boolean photoCreditOverride) {
        this.photoCreditOverride = photoCreditOverride;
    }

    public Lookups getStatus() {
        return status;
    }

    public void setStatus(Lookups lookups) {
        this.status = lookups;
    }

    public Lookups getType() {
        return type;
    }

    public void setType(Lookups lookups) {
        this.type = lookups;
    }

    public Contacts getProductionCompanyContact() {
        return productionCompanyContact;
    }

    public void setProductionCompanyContact(Contacts contacts) {
        this.productionCompanyContact = contacts;
    }

    public Contacts getParentCompanyContact() {
        return parentCompanyContact;
    }

    public void setParentCompanyContact(Contacts contacts) {
        this.parentCompanyContact = contacts;
    }

    public Lookups getProcessingOriginalFileType() {
        return processingOriginalFileType;
    }

    public void setProcessingOriginalFileType(Lookups lookups) {
        this.processingOriginalFileType = lookups;
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

    public Departments getDepartment() {
        return department;
    }

    public void setDepartment(Departments departments) {
        this.department = departments;
    }

    public Lookups getProjectType() {
        return projectType;
    }

    public void setProjectType(Lookups lookups) {
        this.projectType = lookups;
    }

    public Contacts getOwner() {
        return owner;
    }

    public void setOwner(Contacts contacts) {
        this.owner = contacts;
    }

    public Storage_Disk getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(Storage_Disk storage_Disk) {
        this.imageLocation = storage_Disk;
    }

    public Storage_Disk getImageLocationRemote() {
        return imageLocationRemote;
    }

    public void setImageLocationRemote(Storage_Disk storage_Disk) {
        this.imageLocationRemote = storage_Disk;
    }

    public String getCurrentTagger2() {
        return currentTagger2;
    }

    public void setCurrentTagger2(String currentTagger2) {
        this.currentTagger2 = currentTagger2;
    }

    public String getCurrentTagger1() {
        return currentTagger1;
    }

    public void setCurrentTagger1(String currentTagger1) {
        this.currentTagger1 = currentTagger1;
    }

    public String getCurrentTagger() {
        return currentTagger;
    }

    public void setCurrentTagger(String currentTagger) {
        this.currentTagger = currentTagger;
    }

    public String getLegacyNameNotes() {
        return legacyNameNotes;
    }

    public void setLegacyNameNotes(String legacyNameNotes) {
        this.legacyNameNotes = legacyNameNotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Projects projects = (Projects) o;
        if (projects.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, projects.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Projects{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", fullName='" + fullName + '\'' +
            ", runOfShowFlag=" + runOfShowFlag +
            ", template=" + template +
            ", labFlag=" + labFlag +
            ", alfrescoTitle1='" + alfrescoTitle1 + '\'' +
            ", alfrescoTitle2='" + alfrescoTitle2 + '\'' +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", actorsWithRights=" + actorsWithRights +
            ", daysShooting=" + daysShooting +
            ", weeksShooting=" + weeksShooting +
            ", notes='" + notes + '\'' +
            ", sensitiveViewing=" + sensitiveViewing +
            ", productionCompanyNotes='" + productionCompanyNotes + '\'' +
            ", productionCompanyShippingNumber='" + productionCompanyShippingNumber + '\'' +
            ", processingDeliveries='" + processingDeliveries + '\'' +
            ", processingSpecialInstructions='" + processingSpecialInstructions + '\'' +
            ", processingWatermark='" + processingWatermark + '\'' +
            ", processingCopyright='" + processingCopyright + '\'' +
            ", labProofNotes='" + labProofNotes + '\'' +
            ", labLastProofImageNumber='" + labLastProofImageNumber + '\'' +
            ", labLastProofPageNumber='" + labLastProofPageNumber + '\'' +
            ", labImageNumberSchema='" + labImageNumberSchema + '\'' +
            ", labFolderBatchSchema='" + labFolderBatchSchema + '\'' +
            ", photoLabInfo='" + photoLabInfo + '\'' +
            ", projectUnitPhotoNotes='" + projectUnitPhotoNotes + '\'' +
            ", projectInfoNotes='" + projectInfoNotes + '\'' +
            ", createdDate=" + createdDate +
            ", updatedDate=" + updatedDate +
            ", legacyDirector='" + legacyDirector + '\'' +
            ", legacyExecutiveProducer='" + legacyExecutiveProducer + '\'' +
            ", legacyExecutiveProducer2='" + legacyExecutiveProducer2 + '\'' +
            ", legacyExecutiveProducer3='" + legacyExecutiveProducer3 + '\'' +
            ", legacyExecutiveProducer4='" + legacyExecutiveProducer4 + '\'' +
            ", legacyProducer='" + legacyProducer + '\'' +
            ", legacyProducer2='" + legacyProducer2 + '\'' +
            ", legacyProducer3='" + legacyProducer3 + '\'' +
            ", legacyProducer4='" + legacyProducer4 + '\'' +
            ", legacyAdditionalTalent='" + legacyAdditionalTalent + '\'' +
            ", themeId=" + themeId +
            ", sptPhotoSubtype='" + sptPhotoSubtype + '\'' +
            ", photoCredit='" + photoCredit + '\'' +
            ", shootDate=" + shootDate +
            ", shootDateOverride=" + shootDateOverride +
            ", unitPhotographerOverride=" + unitPhotographerOverride +
            ", useSetup=" + useSetup +
            ", useExif=" + useExif +
            ", tagDate=" + tagDate +
            ", tagreportIndex=" + tagreportIndex +
            ", loginMessage='" + loginMessage + '\'' +
            ", loginMessageActive=" + loginMessageActive +
            ", topLevelAlbums=" + topLevelAlbums +
            ", enableTertiary=" + enableTertiary +
            ", invoiceCreated=" + invoiceCreated +
            ", price='" + price + '\'' +
            ", foxTitle='" + foxTitle + '\'' +
            ", isAsset=" + isAsset +
            ", fullRejection=" + fullRejection +
            ", disabled=" + disabled +
            ", reminderDate=" + reminderDate +
            ", photoCreditOverride=" + photoCreditOverride +
            ", legacyNameNotes='" + legacyNameNotes + '\'' +
            ", currentTagger='" + currentTagger + '\'' +
            ", currentTagger1='" + currentTagger1 + '\'' +
            ", currentTagger2='" + currentTagger2 + '\'' +
            ", status=" + status +
            ", type=" + type +
            ", productionCompanyContact=" + productionCompanyContact +
            ", parentCompanyContact=" + parentCompanyContact +
            ", processingOriginalFileType=" + processingOriginalFileType +
            ", createdByAdminUser=" + createdByAdminUser +
            ", updatedByAdminUser=" + updatedByAdminUser +
            ", department=" + department +
            ", projectType=" + projectType +
            ", owner=" + owner +
            ", imageLocation=" + imageLocation +
            ", imageLocationRemote=" + imageLocationRemote +
            '}';
    }
}
