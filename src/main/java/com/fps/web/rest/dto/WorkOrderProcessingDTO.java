package com.fps.web.rest.dto;

import java.util.Date;

public class WorkOrderProcessingDTO{

    private Long id;
    private String projectName;
    private Date dateRecieved;
    private Long workOrderId;
    private String hddID;
    private Date proofShipped;
    private Date driveShipped;
    private String imageRange;
    private String imageQty;
    private String page1;
    private String page2;
    private String notes;
    private String location;
    private String isPko;
    private String status;
    private String audited;
    private String assignedTo;
    private String processedBy;
    private String ingestedBy;
    private Date completion;
    private String color;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getDateRecieved() {
        return dateRecieved;
    }

    public void setDateRecieved(Date dateRecieved) {
        this.dateRecieved = dateRecieved;
    }

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getHddID() {
        return hddID;
    }

    public void setHddID(String hddID) {
        this.hddID = hddID;
    }

    public Date getProofShipped() {
        return proofShipped;
    }

    public void setProofShipped(Date proofShipped) {
        this.proofShipped = proofShipped;
    }

    public Date getDriveShipped() {
        return driveShipped;
    }

    public void setDriveShipped(Date driveShipped) {
        this.driveShipped = driveShipped;
    }

    public String getImageRange() {
        return imageRange;
    }

    public void setImageRange(String imageRange) {
        this.imageRange = imageRange;
    }

    public String getImageQty() {
        return imageQty;
    }

    public void setImageQty(String imageQty) {
        this.imageQty = imageQty;
    }

    public String getPage1() {
        return page1;
    }

    public void setPage1(String page1) {
        this.page1 = page1;
    }

    public String getPage2() {
        return page2;
    }

    public void setPage2(String page2) {
        this.page2 = page2;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIsPko() {
        return isPko;
    }

    public void setIsPko(String isPko) {
        this.isPko = isPko;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAudited() {
        return audited;
    }

    public void setAudited(String audited) {
        this.audited = audited;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(String processedBy) {
        this.processedBy = processedBy;
    }

    public String getIngestedBy() {
        return ingestedBy;
    }

    public void setIngestedBy(String ingestedBy) {
        this.ingestedBy = ingestedBy;
    }

    public Date getCompletion() {
        return completion;
    }

    public void setCompletion(Date completion) {
        this.completion = completion;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
