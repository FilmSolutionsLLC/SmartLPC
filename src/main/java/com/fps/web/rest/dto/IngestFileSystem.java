package com.fps.web.rest.dto;

import java.util.List;

/**
 * Created by macbookpro on 2/1/17.
 */
public class IngestFileSystem {

    private Long ingestID;
    private String project;
    private Boolean exists;
    private String status;
    private String totalFiles;
    private String error;
    private List<String> lightroomExports;
    private String sourceIP;
    private String action;
    private String logo;
    private String priority;

    public Long getIngestID() {
        return ingestID;
    }

    public void setIngestID(Long ingestID) {
        this.ingestID = ingestID;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Boolean getExists() {
        return exists;
    }

    public void setExists(Boolean exists) {
        this.exists = exists;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalFiles() {
        return totalFiles;
    }

    public void setTotalFiles(String totalFiles) {
        this.totalFiles = totalFiles;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getLightroomExports() {
        return lightroomExports;
    }

    public void setLightroomExports(List<String> lightroomExports) {
        this.lightroomExports = lightroomExports;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public IngestFileSystem() {

    }

    public IngestFileSystem(Long ingestID, String project, Boolean exists, String status, String totalFiles, String error, List<String> lightroomExports, String sourceIP, String action, String logo, String priority) {
        this.ingestID = ingestID;
        this.project = project;
        this.exists = exists;
        this.status = status;
        this.totalFiles = totalFiles;
        this.error = error;
        this.lightroomExports = lightroomExports;
        this.sourceIP = sourceIP;
        this.action = action;
        this.logo = logo;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "IngestFileSystem{" +
            "ingestID=" + ingestID +
            ", project='" + project + '\'' +
            ", exists=" + exists +
            ", status='" + status + '\'' +
            ", totalFiles='" + totalFiles + '\'' +
            ", error='" + error + '\'' +
            '}';
    }
}

