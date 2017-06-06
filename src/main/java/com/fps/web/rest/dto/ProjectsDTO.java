package com.fps.web.rest.dto;

import com.fps.domain.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by macbookpro on 1/3/17.
 */
public class ProjectsDTO {


    private Projects projects;

    private Set<ProjectLabTasks> projectLabTaskses = new HashSet<>();

    private Set<ProjectPurchaseOrders> projectPurchaseOrderses = new HashSet<>();

    private Set<ProjectRoles> projectRoles = new HashSet<>();

    private Set<ContactPrivileges> contactPrivileges = new HashSet<>();

    public ProjectsDTO() {
    }

    public ProjectsDTO(Projects projects, Set<ProjectLabTasks> projectLabTaskses, Set<ProjectPurchaseOrders> projectPurchaseOrderses, Set<ProjectRoles> projectRoles, Set<ContactPrivileges> contactPrivileges) {
        this.projects = projects;
        this.projectLabTaskses = projectLabTaskses;
        this.projectPurchaseOrderses = projectPurchaseOrderses;
        this.projectRoles = projectRoles;
        this.contactPrivileges = contactPrivileges;
    }

    public Set<ProjectLabTasks> getProjectLabTaskses() {
        return projectLabTaskses;
    }

    public void setProjectLabTaskses(Set<ProjectLabTasks> projectLabTaskses) {
        this.projectLabTaskses = projectLabTaskses;
    }

    public Set<ProjectPurchaseOrders> getProjectPurchaseOrderses() {
        return projectPurchaseOrderses;
    }

    public void setProjectPurchaseOrderses(Set<ProjectPurchaseOrders> projectPurchaseOrderses) {
        this.projectPurchaseOrderses = projectPurchaseOrderses;
    }

    public Set<ProjectRoles> getProjectRoles() {
        return projectRoles;
    }

    public void setProjectRoles(Set<ProjectRoles> projectRoles) {
        this.projectRoles = projectRoles;
    }

    public Projects getProjects() {
        return projects;
    }

    public void setProjects(Projects projects) {
        this.projects = projects;
    }

    public Set<ContactPrivileges> getContactPrivileges() {
        return contactPrivileges;
    }

    public void setContactPrivileges(Set<ContactPrivileges> contactPrivileges) {
        this.contactPrivileges = contactPrivileges;
    }

    @Override
    public String toString() {
        return "ProjectsDTO{" +
            "projects=" + projects +
            ", projectLabTaskses=" + projectLabTaskses +
            ", projectPurchaseOrderses=" + projectPurchaseOrderses +
            ", projectRoles=" + projectRoles +
            ", contactPrivileges=" + contactPrivileges +
            '}';
    }
}
