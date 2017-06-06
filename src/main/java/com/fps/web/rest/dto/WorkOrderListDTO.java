package com.fps.web.rest.dto;

/**
 * Created by macbookpro on 2/16/17.
 */
public class WorkOrderListDTO {

    private Long id;
    private String project_name;
    private Long workOrderId;
    private String po;
    private String date;
    private String type;
    private String workDesc;
    private String status;
    private Float time;
    private String assignedTo;
    private String pko;
    private String audited;
    private String auditedBy;
    private String invoiced;
    private String color;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWorkDesc() {
        return workDesc;
    }

    public void setWorkDesc(String workDesc) {
        this.workDesc = workDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getTime() {
        return time;
    }

    public void setTime(Float time) {
        this.time = time;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPko() {
        return pko;
    }

    public void setPko(String pko) {
        this.pko = pko;
    }

    public String getAudited() {
        return audited;
    }

    public void setAudited(String audited) {
        this.audited = audited;
    }

    public String getAuditedBy() {
        return auditedBy;
    }

    public void setAuditedBy(String auditedBy) {
        this.auditedBy = auditedBy;
    }

    public String getInvoiced() {
        return invoiced;
    }

    public void setInvoiced(String invoiced) {
        this.invoiced = invoiced;
    }

    @Override
    public String toString() {
        return "WorkOrderListDTO{" +
            "id=" + id +
            ", project_name='" + project_name + '\'' +
            ", workOrderId=" + workOrderId +
            ", po='" + po + '\'' +
            ", date='" + date + '\'' +
            ", type='" + type + '\'' +
            ", workDesc='" + workDesc + '\'' +
            ", status='" + status + '\'' +
            ", time=" + time +
            ", assignedTo='" + assignedTo + '\'' +
            ", pko='" + pko + '\'' +
            ", audited='" + audited + '\'' +
            ", auditedBy='" + auditedBy + '\'' +
            ", invoiced='" + invoiced + '\'' +
            ", color='" + color + '\'' +
            '}';
    }
}
