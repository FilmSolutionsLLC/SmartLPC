package com.fps.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ProjectLabTasks.
 */
@Entity
@Table(name = "project_lab_tasks")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "projectlabtasks")
public class ProjectLabTasks implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "created_date")
    @JsonIgnore
    private ZonedDateTime createdDate;

    @Column(name = "updated_date")
    @JsonIgnore
    private ZonedDateTime updatedDate;

    @Column(name = "qb_rid")
    private Integer qb_rid;

    @ManyToOne
    @JsonIgnore
    private Projects project;

    @ManyToOne
    private Lookups task_name;

    @ManyToOne
    @JsonIgnore
    private User created_by_admin_user;

    @ManyToOne
    @JsonIgnore
    private User updated_by_admin_user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getQb_rid() {
        return qb_rid;
    }

    public void setQb_rid(Integer qb_rid) {
        this.qb_rid = qb_rid;
    }

    public Projects getProject() {
        return project;
    }

    public void setProject(Projects projects) {
        this.project = projects;
    }

    public Lookups getTask_name() {
        return task_name;
    }

    public void setTask_name(Lookups lookups) {
        this.task_name = lookups;
    }

    public User getCreated_by_admin_user() {
        return created_by_admin_user;
    }

    public void setCreated_by_admin_user(User user) {
        this.created_by_admin_user = user;
    }

    public User getUpdated_by_admin_user() {
        return updated_by_admin_user;
    }

    public void setUpdated_by_admin_user(User user) {
        this.updated_by_admin_user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectLabTasks projectLabTasks = (ProjectLabTasks) o;
        if(projectLabTasks.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, projectLabTasks.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectLabTasks{" +
            "id=" + id +
            ", createdDate='" + createdDate + "'" +
            ", updatedDate='" + updatedDate + "'" +
            ", qb_rid='" + qb_rid + "'" +
            '}';
    }
}
