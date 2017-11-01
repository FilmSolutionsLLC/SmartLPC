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
 * A ProjectPurchaseOrders.
 */
@Entity
@Table(name = "project_purchase_orders")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "projectpurchaseorders")
public class ProjectPurchaseOrders implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "po_number")
    private String po_number;

    @Column(name = "po_notes")
    private String po_notes;

    @Column(name = "qb_rid")
    private Integer qb_rid;

    @Column(name = "created_date")
    @JsonIgnore
    private ZonedDateTime created_date;

    @Column(name = "updated_date")
    @JsonIgnore
    private ZonedDateTime updated_date;

    @ManyToOne
    @JsonIgnore
    private Projects project;

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

    public String getPo_number() {
        return po_number;
    }

    public void setPo_number(String po_number) {
        this.po_number = po_number;
    }

    public String getPo_notes() {
        return po_notes;
    }

    public void setPo_notes(String po_notes) {
        this.po_notes = po_notes;
    }

    public Integer getQb_rid() {
        return qb_rid;
    }

    public void setQb_rid(Integer qb_rid) {
        this.qb_rid = qb_rid;
    }

    public ZonedDateTime getCreated_date() {
        return created_date;
    }

    public void setCreated_date(ZonedDateTime created_date) {
        this.created_date = created_date;
    }

    public ZonedDateTime getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(ZonedDateTime updated_date) {
        this.updated_date = updated_date;
    }

    public Projects getProject() {
        return project;
    }

    public void setProject(Projects projects) {
        this.project = projects;
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
        ProjectPurchaseOrders projectPurchaseOrders = (ProjectPurchaseOrders) o;
        if(projectPurchaseOrders.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, projectPurchaseOrders.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectPurchaseOrders{" +
            "id=" + id +
            ", po_number='" + po_number + "'" +
            ", po_notes='" + po_notes + "'" +
            ", qb_rid='" + qb_rid + "'" +
            ", created_date='" + created_date + "'" +
            ", updated_date='" + updated_date + "'" +
            '}';
    }


}
