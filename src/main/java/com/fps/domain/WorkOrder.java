package com.fps.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * A WorkOrder.
 */
@Entity
@Table(name = "work_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "workorder")
public class WorkOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "is_print")
    private Boolean isPrint;

    @Column(name = "is_proof")
    private Boolean isProof;

    @Column(name = "is_abc")
    private Boolean isAbc;

    @Column(name = "is_tracking")
    private Boolean isTracking;

    @Column(name = "request_date")
    private LocalDate requestDate;

    @Column(name = "request_description")
    private String requestDescription;

    @Column(name = "audited_flag")
    private Integer auditedFlag;

    @Column(name = "po_record")
    private String poRecord;

    @Column(name = "invoiced")
    private Integer invoiced;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "is_alt_credit")
    private Boolean isAltCredit;

    @Column(name = "overwrite")
    private String overwrite;

    @Column(name = "print_set")
    private String printSet;

    @Column(name = "print_quantity")
    private String printQuantity;

    @Column(name = "proof_days_from")
    private String printDaysFrom;

    @Column(name = "proof_days_to")
    private String printDaysTo;

    @Column(name = "proof_pages_from")
    private String printPagesFrom;

    @Column(name = "proof_pages_to")
    private String printPagesTo;

    @Column(name = "reminder_date_1")
    private LocalDate reminderDate1;

    @Column(name = "reminder_message_1")
    private String reminderMsg1;

    @Column(name = "reminder_date_2")
    private LocalDate reminderDate2;

    @Column(name = "reminder_message_2")
    private String reminderMsg2;

    @Column(name = "reminder_date_3")
    private LocalDate reminderDate3;

    @Column(name = "reminder_message_3")
    private String reminderMsg3;

    @Column(name = "processing_date_recieved")
    private LocalDate processingDateRecieved;

    @Column(name = "processing_hdd_id")
    private String processingHDDid;

    @Column(name = "processing_date_shipped")
    private LocalDate processingDateShipped;

    @Column(name = "processing_note")
    private String processingNote;

    @Column(name = "processing_location")
    private String processingLocation;

    @Column(name = "processing_image_range")
    private String processingImageRange;

    @Column(name = "processing_image_qty")
    private String processingImageQty;

    @Column(name = "due_to_client_reminder")
    private LocalDate dueToClientReminder;

    @Column(name = "due_to_mounter_reminder")
    private LocalDate dueToMounterReminder;

    @Column(name = "received_from_mounter_reminder")
    private LocalDate recievedFromMounterReminder;

    @Column(name = "abc_instruction")
    private String abcInstruction;

    @Column(name = "abc_raw_dvd")
    private Integer abcRawDvd;

    @Column(name = "abc_talent_count")
    private Integer abcTalentCount;

    @Column(name = "kick_back")
    private String kickBack;

    @Column(name = "created_on")
    private ZonedDateTime createdDate;

    @Column(name = "updated_on")
    private ZonedDateTime updatedDate;

    @Column(name = "completion_date")
    private LocalDate completionDate;

    @Column(name = "duration_of_service")
    private String durationOfService;

    @Column(name = "processing_proof_shipped")
    private LocalDate processingProofShipped;

    @ManyToOne
    @JoinColumn(name = "project_type")
    private Lookups type;

    @ManyToOne
    @JoinColumn(name = "status")
    private Lookups status;

    @ManyToOne
    @JoinColumn(name = "print_size")
    private Lookups printSize;

    @ManyToOne
    @JoinColumn(name = "print_surface")
    private Lookups printSurface;

    @ManyToOne
    @JoinColumn(name = "print_bleed")
    private Lookups printBleed;

    @ManyToOne
    @JoinColumn(name = "filename_position")
    private Lookups filenamePosition;

    @ManyToOne
    @JoinColumn(name = "photo_credit")
    private Lookups photoCredit;

    @ManyToOne
    @JoinColumn(name = "credit_location")
    private Lookups creditLocation;

    @ManyToOne
    private Projects project;

    @ManyToOne
    private User assignedToUser;

    @ManyToOne
    private Contacts requestor;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @JsonIgnore
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    @JsonIgnore
    private User updatedBy;

    @ManyToOne
    @JoinColumn(name = "is_filename")
    private Lookups isFilename;

    @ManyToOne
    @JoinColumn(name = "processing_pko_flag")
    private Lookups processing_pko_flag;

    @ManyToOne
    private Lookups priority;

    @ManyToOne
    @JoinColumn(name = "audited_by")
    private User auditedBy;

  
    @OneToMany(cascade=CascadeType.ALL, mappedBy="workOrder",fetch = FetchType.EAGER) 
    Set<WorkOrdersAdminRelation> workOrdersAdminRelations;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsPrint() {
        return isPrint;
    }

    public void setIsPrint(Boolean isPrint) {
        this.isPrint = isPrint;
    }

    public Boolean isIsProof() {
        return isProof;
    }

    public void setIsProof(Boolean isProof) {
        this.isProof = isProof;
    }

    public Boolean isIsAbc() {
        return isAbc;
    }

    public void setIsAbc(Boolean isAbc) {
        this.isAbc = isAbc;
    }

    public Boolean isIsTracking() {
        return isTracking;
    }

    public void setIsTracking(Boolean isTracking) {
        this.isTracking = isTracking;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestDescription() {
        return requestDescription;
    }

    public void setRequestDescription(String requestDescription) {
        this.requestDescription = requestDescription;
    }

    public Integer getAuditedFlag() {
        return auditedFlag;
    }

    public void setAuditedFlag(Integer auditedFlag) {
        this.auditedFlag = auditedFlag;
    }

    public String getPoRecord() {
        return poRecord;
    }

    public void setPoRecord(String poRecord) {
        this.poRecord = poRecord;
    }

    public Integer getInvoiced() {
        return invoiced;
    }

    public void setInvoiced(Integer invoiced) {
        this.invoiced = invoiced;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Boolean isIsAltCredit() {
        return isAltCredit;
    }

    public void setIsAltCredit(Boolean isAltCredit) {
        this.isAltCredit = isAltCredit;
    }

    public String getOverwrite() {
        return overwrite;
    }

    public void setOverwrite(String overwrite) {
        this.overwrite = overwrite;
    }

    public String getPrintSet() {
        return printSet;
    }

    public void setPrintSet(String printSet) {
        this.printSet = printSet;
    }

    public String getPrintQuantity() {
        return printQuantity;
    }

    public void setPrintQuantity(String printQuantity) {
        this.printQuantity = printQuantity;
    }

    public String getPrintDaysFrom() {
        return printDaysFrom;
    }

    public void setPrintDaysFrom(String printDaysFrom) {
        this.printDaysFrom = printDaysFrom;
    }

    public String getPrintDaysTo() {
        return printDaysTo;
    }

    public void setPrintDaysTo(String printDaysTo) {
        this.printDaysTo = printDaysTo;
    }

    public String getPrintPagesFrom() {
        return printPagesFrom;
    }

    public void setPrintPagesFrom(String printPagesFrom) {
        this.printPagesFrom = printPagesFrom;
    }

    public String getPrintPagesTo() {
        return printPagesTo;
    }

    public void setPrintPagesTo(String printPagesTo) {
        this.printPagesTo = printPagesTo;
    }

    public LocalDate getReminderDate1() {
        return reminderDate1;
    }

    public void setReminderDate1(LocalDate reminderDate1) {
        this.reminderDate1 = reminderDate1;
    }

    public String getReminderMsg1() {
        return reminderMsg1;
    }

    public void setReminderMsg1(String reminderMsg1) {
        this.reminderMsg1 = reminderMsg1;
    }

    public LocalDate getReminderDate2() {
        return reminderDate2;
    }

    public void setReminderDate2(LocalDate reminderDate2) {
        this.reminderDate2 = reminderDate2;
    }

    public String getReminderMsg2() {
        return reminderMsg2;
    }

    public void setReminderMsg2(String reminderMsg2) {
        this.reminderMsg2 = reminderMsg2;
    }

    public LocalDate getReminderDate3() {
        return reminderDate3;
    }

    public void setReminderDate3(LocalDate reminderDate3) {
        this.reminderDate3 = reminderDate3;
    }

    public String getReminderMsg3() {
        return reminderMsg3;
    }

    public void setReminderMsg3(String reminderMsg3) {
        this.reminderMsg3 = reminderMsg3;
    }

    public LocalDate getProcessingDateRecieved() {
        return processingDateRecieved;
    }

    public void setProcessingDateRecieved(LocalDate processingDateRecieved) {
        this.processingDateRecieved = processingDateRecieved;
    }

    public String getProcessingHDDid() {
        return processingHDDid;
    }

    public void setProcessingHDDid(String processingHDDid) {
        this.processingHDDid = processingHDDid;
    }

    public LocalDate getProcessingDateShipped() {
        return processingDateShipped;
    }

    public void setProcessingDateShipped(LocalDate processingDateShipped) {
        this.processingDateShipped = processingDateShipped;
    }

    public String getProcessingNote() {
        return processingNote;
    }

    public void setProcessingNote(String processingNote) {
        this.processingNote = processingNote;
    }

    public String getProcessingLocation() {
        return processingLocation;
    }

    public void setProcessingLocation(String processingLocation) {
        this.processingLocation = processingLocation;
    }

    public String getProcessingImageRange() {
        return processingImageRange;
    }

    public void setProcessingImageRange(String processingImageRange) {
        this.processingImageRange = processingImageRange;
    }

    public String getProcessingImageQty() {
        return processingImageQty;
    }

    public void setProcessingImageQty(String processingImageQty) {
        this.processingImageQty = processingImageQty;
    }

    public LocalDate getDueToClientReminder() {
        return dueToClientReminder;
    }

    public void setDueToClientReminder(LocalDate dueToClientReminder) {
        this.dueToClientReminder = dueToClientReminder;
    }

    public LocalDate getDueToMounterReminder() {
        return dueToMounterReminder;
    }

    public void setDueToMounterReminder(LocalDate dueToMounterReminder) {
        this.dueToMounterReminder = dueToMounterReminder;
    }

    public LocalDate getRecievedFromMounterReminder() {
        return recievedFromMounterReminder;
    }

    public void setRecievedFromMounterReminder(LocalDate recievedFromMounterReminder) {
        this.recievedFromMounterReminder = recievedFromMounterReminder;
    }

    public String getAbcInstruction() {
        return abcInstruction;
    }

    public void setAbcInstruction(String abcInstruction) {
        this.abcInstruction = abcInstruction;
    }

    public Integer getAbcRawDvd() {
        return abcRawDvd;
    }

    public void setAbcRawDvd(Integer abcRawDvd) {
        this.abcRawDvd = abcRawDvd;
    }

    public Integer getAbcTalentCount() {
        return abcTalentCount;
    }

    public void setAbcTalentCount(Integer abcTalentCount) {
        this.abcTalentCount = abcTalentCount;
    }

    public String getKickBack() {
        return kickBack;
    }

    public void setKickBack(String kickBack) {
        this.kickBack = kickBack;
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

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public String getDurationOfService() {
        return durationOfService;
    }

    public void setDurationOfService(String durationOfService) {
        this.durationOfService = durationOfService;
    }

    public LocalDate getProcessingProofShipped() {
        return processingProofShipped;
    }

    public void setProcessingProofShipped(LocalDate processingProofShipped) {
        this.processingProofShipped = processingProofShipped;
    }

    public Lookups getType() {
        return type;
    }

    public void setType(Lookups lookups) {
        this.type = lookups;
    }

    public Lookups getStatus() {
        return status;
    }

    public void setStatus(Lookups lookups) {
        this.status = lookups;
    }

    public Lookups getPrintSize() {
        return printSize;
    }

    public void setPrintSize(Lookups lookups) {
        this.printSize = lookups;
    }

    public Lookups getPrintSurface() {
        return printSurface;
    }

    public void setPrintSurface(Lookups lookups) {
        this.printSurface = lookups;
    }

    public Lookups getPrintBleed() {
        return printBleed;
    }

    public void setPrintBleed(Lookups lookups) {
        this.printBleed = lookups;
    }

    public Lookups getFilenamePosition() {
        return filenamePosition;
    }

    public void setFilenamePosition(Lookups lookups) {
        this.filenamePosition = lookups;
    }

    public Lookups getPhotoCredit() {
        return photoCredit;
    }

    public void setPhotoCredit(Lookups lookups) {
        this.photoCredit = lookups;
    }

    public Lookups getCreditLocation() {
        return creditLocation;
    }

    public void setCreditLocation(Lookups lookups) {
        this.creditLocation = lookups;
    }

    public Projects getProject() {
        return project;
    }

    public void setProject(Projects projects) {
        this.project = projects;
    }

    public User getAssignedToUser() {
        return assignedToUser;
    }

    public void setAssignedToUser(User user) {
        this.assignedToUser = user;
    }

    public Contacts getRequestor() {
        return requestor;
    }

    public void setRequestor(Contacts contacts) {
        this.requestor = contacts;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public Lookups getIsFilename() {
        return isFilename;
    }

    public void setIsFilename(Lookups lookups) {
        this.isFilename = lookups;
    }

    public Lookups getProcessing_pko_flag() {
        return processing_pko_flag;
    }

    public void setProcessing_pko_flag(Lookups lookups) {
        this.processing_pko_flag = lookups;
    }

    public Lookups getPriority() {
        return priority;
    }

    public void setPriority(Lookups lookups) {
        this.priority = lookups;
    }

    public User getAuditedBy() {
        return auditedBy;
    }

    public void setAuditedBy(User user) {
        this.auditedBy = user;
    }

    public Set<WorkOrdersAdminRelation> getWorkOrdersAdminRelations() {
		return workOrdersAdminRelations;
	}

	public void setWorkOrdersAdminRelations(Set<WorkOrdersAdminRelation> workOrdersAdminRelations) {
		this.workOrdersAdminRelations = workOrdersAdminRelations;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkOrder workOrder = (WorkOrder) o;
        if (workOrder.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, workOrder.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WorkOrder{" +
            "id=" + id +
            ", isPrint='" + isPrint + "'" +
            ", isProof='" + isProof + "'" +
            ", isAbc='" + isAbc + "'" +
            ", isTracking='" + isTracking + "'" +
            ", requestDate='" + requestDate + "'" +
            ", requestDescription='" + requestDescription + "'" +
            ", auditedFlag='" + auditedFlag + "'" +
            ", poRecord='" + poRecord + "'" +
            ", invoiced='" + invoiced + "'" +
            ", invoiceNumber='" + invoiceNumber + "'" +
            ", isAltCredit='" + isAltCredit + "'" +
            ", overwrite='" + overwrite + "'" +
            ", printSet='" + printSet + "'" +
            ", printQuantity='" + printQuantity + "'" +
            ", printDaysFrom='" + printDaysFrom + "'" +
            ", printDaysTo='" + printDaysTo + "'" +
            ", printPagesFrom='" + printPagesFrom + "'" +
            ", printPagesTo='" + printPagesTo + "'" +
            ", reminderDate1='" + reminderDate1 + "'" +
            ", reminderMsg1='" + reminderMsg1 + "'" +
            ", reminderDate2='" + reminderDate2 + "'" +
            ", reminderMsg2='" + reminderMsg2 + "'" +
            ", reminderDate3='" + reminderDate3 + "'" +
            ", reminderMsg3='" + reminderMsg3 + "'" +
            ", processingDateRecieved='" + processingDateRecieved + "'" +
            ", processingHDDid='" + processingHDDid + "'" +
            ", processingDateShipped='" + processingDateShipped + "'" +
            ", processingNote='" + processingNote + "'" +
            ", processingLocation='" + processingLocation + "'" +
            ", processingImageRange='" + processingImageRange + "'" +
            ", processingImageQty='" + processingImageQty + "'" +
            ", dueToClientReminder='" + dueToClientReminder + "'" +
            ", dueToMounterReminder='" + dueToMounterReminder + "'" +
            ", recievedFromMounterReminder='" + recievedFromMounterReminder + "'" +
            ", abcInstruction='" + abcInstruction + "'" +
            ", abcRawDvd='" + abcRawDvd + "'" +
            ", abcTalentCount='" + abcTalentCount + "'" +
            ", kickBack='" + kickBack + "'" +
            ", createdDate='" + createdDate + "'" +
            ", updatedDate='" + updatedDate + "'" +
            ", completionDate='" + completionDate + "'" +
            ", durationOfService='" + durationOfService + "'" +
            ", processingProofShipped='" + processingProofShipped + "'" +
            '}';
    }
}
