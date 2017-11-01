package com.fps.web.rest.dto;

import java.time.LocalDate;
import java.util.Objects;

public class ContactDTO {

    private Long id;


    private String username;


    private String password;


    private String fullName;


    private String title;

    private String email;

    private String email2;

    private String phoneOffice;

    private String phoneAlternate;

    private String phoneMobile;

    private String phoneFax;

    private String streetAddress;

    private String streetAddress2;

    private String streetAddress3;

    private String city;

    private String state;

    private String zipcode;


    private String country;


    private String website;


    private String notes;

    private String sourceId;


    private LocalDate createdDate;


    private LocalDate updatedDate;


    private Integer globalRestartColumns;


    private Integer globalRestartImagesPerPage;


    private String globalRestartImageSize;


    private Long globalRestartTime;


    private Boolean dashboard;


    private Boolean internalAccessOnly;


    private Integer adhocExpiresIn;


    private Integer adhocLimitViews;


    private Integer adhocDownload;


    private String adhocWatermarkText;


    private String loginIp;


    private Integer loginAttempt;

    private Boolean attemptBasedLogin;

    private Boolean ipBasedLogin;


    private Long typeId;
    private String typeValue;


    private Long departmentId;
    private String departmentName;

    private Long companyContactId;

    private String companyContactFullName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getPhoneOffice() {
        return phoneOffice;
    }

    public void setPhoneOffice(String phoneOffice) {
        this.phoneOffice = phoneOffice;
    }

    public String getPhoneAlternate() {
        return phoneAlternate;
    }

    public void setPhoneAlternate(String phoneAlternate) {
        this.phoneAlternate = phoneAlternate;
    }

    public String getPhoneMobile() {
        return phoneMobile;
    }

    public void setPhoneMobile(String phoneMobile) {
        this.phoneMobile = phoneMobile;
    }

    public String getPhoneFax() {
        return phoneFax;
    }

    public void setPhoneFax(String phoneFax) {
        this.phoneFax = phoneFax;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getStreetAddress2() {
        return streetAddress2;
    }

    public void setStreetAddress2(String streetAddress2) {
        this.streetAddress2 = streetAddress2;
    }

    public String getStreetAddress3() {
        return streetAddress3;
    }

    public void setStreetAddress3(String streetAddress3) {
        this.streetAddress3 = streetAddress3;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
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

    public Integer getGlobalRestartColumns() {
        return globalRestartColumns;
    }

    public void setGlobalRestartColumns(Integer globalRestartColumns) {
        this.globalRestartColumns = globalRestartColumns;
    }

    public Integer getGlobalRestartImagesPerPage() {
        return globalRestartImagesPerPage;
    }

    public void setGlobalRestartImagesPerPage(Integer globalRestartImagesPerPage) {
        this.globalRestartImagesPerPage = globalRestartImagesPerPage;
    }

    public String getGlobalRestartImageSize() {
        return globalRestartImageSize;
    }

    public void setGlobalRestartImageSize(String globalRestartImageSize) {
        this.globalRestartImageSize = globalRestartImageSize;
    }

    public Long getGlobalRestartTime() {
        return globalRestartTime;
    }

    public void setGlobalRestartTime(Long globalRestartTime) {
        this.globalRestartTime = globalRestartTime;
    }

    public Boolean getDashboard() {
        return dashboard;
    }

    public void setDashboard(Boolean dashboard) {
        this.dashboard = dashboard;
    }

    public Boolean getInternalAccessOnly() {
        return internalAccessOnly;
    }

    public void setInternalAccessOnly(Boolean internalAccessOnly) {
        this.internalAccessOnly = internalAccessOnly;
    }

    public Integer getAdhocExpiresIn() {
        return adhocExpiresIn;
    }

    public void setAdhocExpiresIn(Integer adhocExpiresIn) {
        this.adhocExpiresIn = adhocExpiresIn;
    }

    public Integer getAdhocLimitViews() {
        return adhocLimitViews;
    }

    public void setAdhocLimitViews(Integer adhocLimitViews) {
        this.adhocLimitViews = adhocLimitViews;
    }

    public Integer getAdhocDownload() {
        return adhocDownload;
    }

    public void setAdhocDownload(Integer adhocDownload) {
        this.adhocDownload = adhocDownload;
    }

    public String getAdhocWatermarkText() {
        return adhocWatermarkText;
    }

    public void setAdhocWatermarkText(String adhocWatermarkText) {
        this.adhocWatermarkText = adhocWatermarkText;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Integer getLoginAttempt() {
        return loginAttempt;
    }

    public void setLoginAttempt(Integer loginAttempt) {
        this.loginAttempt = loginAttempt;
    }

    public Boolean getAttemptBasedLogin() {
        return attemptBasedLogin;
    }

    public void setAttemptBasedLogin(Boolean attemptBasedLogin) {
        this.attemptBasedLogin = attemptBasedLogin;
    }

    public Boolean getIpBasedLogin() {
        return ipBasedLogin;
    }

    public void setIpBasedLogin(Boolean ipBasedLogin) {
        this.ipBasedLogin = ipBasedLogin;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getCompanyContactId() {
        return companyContactId;
    }

    public void setCompanyContactId(Long companyContactId) {
        this.companyContactId = companyContactId;
    }

    public String getCompanyContactFullName() {
        return companyContactFullName;
    }

    public void setCompanyContactFullName(String companyContactFullName) {
        this.companyContactFullName = companyContactFullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContactDTO contactDTO = (ContactDTO) o;

        if ( ! Objects.equals(id, contactDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
