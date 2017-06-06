package com.fps.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Departments.
 */
@Entity
@Table(name = "departments")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "departments")
public class Departments implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @Column(name = "logo")
    private String logo;

    @Column(name = "url_override")
    private String urlOverride;

    @Column(name = "self_project")
    private Boolean selfProject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUrlOverride() {
        return urlOverride;
    }

    public void setUrlOverride(String urlOverride) {
        this.urlOverride = urlOverride;
    }

    public Boolean isSelfProject() {
        return selfProject;
    }

    public void setSelfProject(Boolean selfProject) {
        this.selfProject = selfProject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Departments departments = (Departments) o;
        if(departments.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, departments.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Departments{" +
            "id=" + id +
            ", departmentName='" + departmentName + "'" +
            ", companyId='" + companyId + "'" +
            ", createdDate='" + createdDate + "'" +
            ", updatedDate='" + updatedDate + "'" +
            ", logo='" + logo + "'" +
            ", urlOverride='" + urlOverride + "'" +
            ", selfProject='" + selfProject + "'" +
            '}';
    }
}
