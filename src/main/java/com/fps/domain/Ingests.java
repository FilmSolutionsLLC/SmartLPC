package com.fps.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Created by macbookpro on 2/1/17.
 */
@Entity
@Table(name = "ingests")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "ingests")
public class Ingests implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ingest_job_id")
    private Long id;


    //private Projects projects;
    private Long project_id;


    @Column(name = "ingest_start_time")
    private ZonedDateTime ingestStartTime;

    @Column(name = "ingest_completed_time")
    private ZonedDateTime ingestCompletedTime;

    @Column(name = "total_images")
    private Integer totalImages;

    @Column(name = "total_done")
    private Double totalDone;


    @Column(name = "alfresco_title_1")
    private String alfrescoTitle1;

    @Column(name = "alfresco_title_2")
    private String alfrescoTitle2;


    @Column(name = "watermark")
    private Boolean watermark;

    @Column(name = "wm_file")
    private String wmFile;


    @Column(name = "action")
    private Integer action;


    @Column(name = "ingest_processor")
    private String ingestProcessor;


    @Column(name = "source_server")
    private String sourceServer;


    @Column(name = "source_path")
    private String sourcePath;


    @Column(name = "destination_server")
    private String destinationServer;

    @Column(name = "status")
    private String status;

    @ManyToOne
    private User adminOwner;

    @Column(name = "pid")
    private String pid;

    @Column(name = "priority")
    private Integer priority;


    @Column(name = "error_code")
    private Integer errorCode;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProject_id() {
        return project_id;
    }

    public void setProject_id(Long project_id) {
        this.project_id = project_id;
    }

    public ZonedDateTime getIngestStartTime() {
        return ingestStartTime;
    }

    public void setIngestStartTime(ZonedDateTime ingestStartTime) {
        this.ingestStartTime = ingestStartTime;
    }

    public ZonedDateTime getIngestCompletedTime() {
        return ingestCompletedTime;
    }

    public void setIngestCompletedTime(ZonedDateTime ingestCompletedTime) {
        this.ingestCompletedTime = ingestCompletedTime;
    }

    public Integer getTotalImages() {
        return totalImages;
    }

    public void setTotalImages(Integer totalImages) {
        this.totalImages = totalImages;
    }

    public Double getTotalDone() {
        return totalDone;
    }

    public void setTotalDone(Double totalDone) {
        this.totalDone = totalDone;
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

    public Boolean getWatermark() {
        return watermark;
    }

    public void setWatermark(Boolean watermark) {
        this.watermark = watermark;
    }

    public String getWmFile() {
        return wmFile;
    }

    public void setWmFile(String wmFile) {
        this.wmFile = wmFile;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getIngestProcessor() {
        return ingestProcessor;
    }

    public void setIngestProcessor(String ingestProcessor) {
        this.ingestProcessor = ingestProcessor;
    }

    public String getSourceServer() {
        return sourceServer;
    }

    public void setSourceServer(String sourceServer) {
        this.sourceServer = sourceServer;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getDestinationServer() {
        return destinationServer;
    }

    public void setDestinationServer(String destinationServer) {
        this.destinationServer = destinationServer;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getAdminOwner() {
        return adminOwner;
    }

    public void setAdminOwner(User adminOwner) {
        this.adminOwner = adminOwner;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public Ingests() {
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ingests ingest = (Ingests) o;
        if (ingest.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, ingest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Ingests(Long project_id, ZonedDateTime ingestStartTime, ZonedDateTime ingestCompletedTime, Integer totalImages, Double totalDone, String alfrescoTitle1, String alfrescoTitle2, Boolean watermark, String wmFile, Integer action, String ingestProcessor, String sourceServer, String sourcePath, String destinationServer, String status, User adminOwner, String pid, Integer priority, Integer error_code) {
        this.project_id = project_id;
        this.ingestStartTime = ingestStartTime;
        this.ingestCompletedTime = ingestCompletedTime;
        this.totalImages = totalImages;
        this.totalDone = totalDone;
        this.alfrescoTitle1 = alfrescoTitle1;
        this.alfrescoTitle2 = alfrescoTitle2;
        this.watermark = watermark;
        this.wmFile = wmFile;
        this.action = action;
        this.ingestProcessor = ingestProcessor;
        this.sourceServer = sourceServer;
        this.sourcePath = sourcePath;
        this.destinationServer = destinationServer;
        this.status = status;
        this.adminOwner = adminOwner;
        this.pid = pid;
        this.priority = priority;
        this.errorCode = error_code;
    }

	@Override
	public String toString() {
		return "Ingests [id=" + id + ", project_id=" + project_id + ", ingestStartTime=" + ingestStartTime
				+ ", ingestCompletedTime=" + ingestCompletedTime + ", totalImages=" + totalImages + ", totalDone="
				+ totalDone + ", alfrescoTitle1=" + alfrescoTitle1 + ", alfrescoTitle2=" + alfrescoTitle2
				+ ", watermark=" + watermark + ", wmFile=" + wmFile + ", action=" + action + ", ingestProcessor="
				+ ingestProcessor + ", sourceServer=" + sourceServer + ", sourcePath=" + sourcePath
				+ ", destinationServer=" + destinationServer + ", status=" + status + ", adminOwner=" + adminOwner
				+ ", pid=" + pid + ", priority=" + priority + ", errorCode=" + errorCode + "]";
	}


}
