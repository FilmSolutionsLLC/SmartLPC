package com.fps.web.rest.dto;

/**
 * Created by macbookpro on 3/7/17.
 */
public class ProjectsViewDTO {
    private Long id;
    private String projectName;
    private String projectStatus;
    private String mainContactName;
    private String mainContactOffice;
    private String mainContactEmail;
    private String unitPublicistName;
    private String unitPublicistMobile;
    private String unitPublicistOffice;
    private String unitPublicistEmail;

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

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getMainContactName() {
        return mainContactName;
    }

    public void setMainContactName(String mainContactName) {
        this.mainContactName = mainContactName;
    }

    public String getMainContactOffice() {
        return mainContactOffice;
    }

    public void setMainContactOffice(String mainContactOffice) {
        this.mainContactOffice = mainContactOffice;
    }

    public String getMainContactEmail() {
        return mainContactEmail;
    }

    public void setMainContactEmail(String mainContactEmail) {
        this.mainContactEmail = mainContactEmail;
    }

    public String getUnitPublicistName() {
        return unitPublicistName;
    }

    public void setUnitPublicistName(String unitPublicistName) {
        this.unitPublicistName = unitPublicistName;
    }

    public String getUnitPublicistMobile() {
        return unitPublicistMobile;
    }

    public void setUnitPublicistMobile(String unitPublicistMobile) {
        this.unitPublicistMobile = unitPublicistMobile;
    }

    public String getUnitPublicistOffice() {
        return unitPublicistOffice;
    }

    public void setUnitPublicistOffice(String unitPublicistOffice) {
        this.unitPublicistOffice = unitPublicistOffice;
    }

    public String getUnitPublicistEmail() {
        return unitPublicistEmail;
    }

    public void setUnitPublicistEmail(String unitPublicistEmail) {
        this.unitPublicistEmail = unitPublicistEmail;
    }

    @Override
    public String toString() {
        return "ProjectsViewDTO{" +
            "id=" + id +
            ", projectName='" + projectName + '\'' +
            ", projectStatus='" + projectStatus + '\'' +
            ", mainContactName='" + mainContactName + '\'' +
            ", mainContactOffice='" + mainContactOffice + '\'' +
            ", mainContactEmail='" + mainContactEmail + '\'' +
            ", unitPublicistName='" + unitPublicistName + '\'' +
            ", unitPublicistMobile='" + unitPublicistMobile + '\'' +
            ", unitPublicistOffice='" + unitPublicistOffice + '\'' +
            ", unitPublicistEmail='" + unitPublicistEmail + '\'' +
            '}';
    }
}
