package com.fps.web.rest.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fps.domain.Contacts;
import com.fps.domain.Lookups;
import com.fps.domain.Projects;
import com.fps.domain.User;
import com.fps.domain.WorkOrdersAdminRelation;
 public class WorkOrderProcessingDTO{
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
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
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

    private Set<WorkOrdersAdminRelation> workOrderAdminRelation;
    
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

	public Set<WorkOrdersAdminRelation> getWorkOrderAdminRelation() {
		return workOrderAdminRelation;
	}

	public void setWorkOrderAdminRelation(Set<WorkOrdersAdminRelation> workOrderAdminRelation) {
		this.workOrderAdminRelation = workOrderAdminRelation;
	}

	public WorkOrderProcessingDTO(Long id, Boolean isPrint, Boolean isProof, Boolean isAbc, Boolean isTracking,
			LocalDate requestDate, String requestDescription, Integer auditedFlag, String poRecord, Integer invoiced,
			String invoiceNumber, Boolean isAltCredit, String overwrite, String printSet, String printQuantity,
			String printDaysFrom, String printDaysTo, String printPagesFrom, String printPagesTo,
			LocalDate reminderDate1, String reminderMsg1, LocalDate reminderDate2, String reminderMsg2,
			LocalDate reminderDate3, String reminderMsg3, LocalDate processingDateRecieved, String processingHDDid,
			LocalDate processingDateShipped, String processingNote, String processingLocation,
			String processingImageRange, String processingImageQty, LocalDate dueToClientReminder,
			LocalDate dueToMounterReminder, LocalDate recievedFromMounterReminder, String abcInstruction,
			Integer abcRawDvd, Integer abcTalentCount, String kickBack, ZonedDateTime createdDate,
			ZonedDateTime updatedDate, LocalDate completionDate, String durationOfService,
			LocalDate processingProofShipped, Lookups type, Lookups status, Lookups printSize, Lookups printSurface,
			Lookups printBleed, Lookups filenamePosition, Lookups photoCredit, Lookups creditLocation, Projects project,
			User assignedToUser, Contacts requestor, User createdBy, User updatedBy, Lookups isFilename,
			Lookups processing_pko_flag, Lookups priority, User auditedBy,
			Set<WorkOrdersAdminRelation> workOrderAdminRelation) {
		super();
		this.id = id;
		this.isPrint = isPrint;
		this.isProof = isProof;
		this.isAbc = isAbc;
		this.isTracking = isTracking;
		this.requestDate = requestDate;
		this.requestDescription = requestDescription;
		this.auditedFlag = auditedFlag;
		this.poRecord = poRecord;
		this.invoiced = invoiced;
		this.invoiceNumber = invoiceNumber;
		this.isAltCredit = isAltCredit;
		this.overwrite = overwrite;
		this.printSet = printSet;
		this.printQuantity = printQuantity;
		this.printDaysFrom = printDaysFrom;
		this.printDaysTo = printDaysTo;
		this.printPagesFrom = printPagesFrom;
		this.printPagesTo = printPagesTo;
		this.reminderDate1 = reminderDate1;
		this.reminderMsg1 = reminderMsg1;
		this.reminderDate2 = reminderDate2;
		this.reminderMsg2 = reminderMsg2;
		this.reminderDate3 = reminderDate3;
		this.reminderMsg3 = reminderMsg3;
		this.processingDateRecieved = processingDateRecieved;
		this.processingHDDid = processingHDDid;
		this.processingDateShipped = processingDateShipped;
		this.processingNote = processingNote;
		this.processingLocation = processingLocation;
		this.processingImageRange = processingImageRange;
		this.processingImageQty = processingImageQty;
		this.dueToClientReminder = dueToClientReminder;
		this.dueToMounterReminder = dueToMounterReminder;
		this.recievedFromMounterReminder = recievedFromMounterReminder;
		this.abcInstruction = abcInstruction;
		this.abcRawDvd = abcRawDvd;
		this.abcTalentCount = abcTalentCount;
		this.kickBack = kickBack;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.completionDate = completionDate;
		this.durationOfService = durationOfService;
		this.processingProofShipped = processingProofShipped;
		this.type = type;
		this.status = status;
		this.printSize = printSize;
		this.printSurface = printSurface;
		this.printBleed = printBleed;
		this.filenamePosition = filenamePosition;
		this.photoCredit = photoCredit;
		this.creditLocation = creditLocation;
		this.project = project;
		this.assignedToUser = assignedToUser;
		this.requestor = requestor;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.isFilename = isFilename;
		this.processing_pko_flag = processing_pko_flag;
		this.priority = priority;
		this.auditedBy = auditedBy;
		this.workOrderAdminRelation = workOrderAdminRelation;
	}

	public WorkOrderProcessingDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
 }