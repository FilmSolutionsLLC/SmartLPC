package com.fps.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Contacts.
 */
@Entity
@Table(name = "contacts")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "contacts")
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password_smartlpc")
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "title")
    private String title;

    @Column(name = "email")
    private String email;

    @Column(name = "email_2")
    private String email2;

    @Column(name = "phone_office")
    private String phoneOffice;

    @Column(name = "phone_alternate")
    private String phoneAlternate;

    @Column(name = "phone_mobile")
    private String phoneMobile;

    @Column(name = "phone_fax")
    private String phoneFax;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "street_address_2")
    private String streetAddress2;

    @Column(name = "street_address_3")
    private String streetAddress3;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "postal_code")
    private String zipcode;

    @Column(name = "country")
    private String country;

    @Column(name = "website")
    private String website;

    @Column(name = "notes")
    private String notes;

    @Column(name = "source_id")
    private String sourceId;

    @Column(name = "created_date")
    @JsonIgnore
    private LocalDate createdDate;

    @Column(name = "updated_date")
    @JsonIgnore
    private LocalDate updatedDate;

    @Column(name = "global_restart_columns")
    private Integer globalRestartColumns;

    @Column(name = "global_restart_images_per_page")
    private Integer globalRestartImagesPerPage;

    @Column(name = "global_restart_image_size")
    private String globalRestartImageSize;

    @Column(name = "global_restart_time")
    private Long globalRestartTime;

    @Column(name = "dashboard")
    private Boolean dashboard;

    @Column(name = "internal_only_access")
    private Boolean internalAccessOnly;

    @Column(name = "adhoc_expires_in")
    private Integer adhocExpiresIn;

    @Column(name = "adhoc_limit_views")
    private Integer adhocLimitViews;

    @Column(name = "adhoc_download")
    private Integer adhocDownload;

    @Column(name = "adhoc_watermark_text")
    private String adhocWatermarkText;

    @Column(name = "login_ip")
    @JsonIgnore
    private String loginIp;

    @Column(name = "login_attempt")
    @JsonIgnore
    private Integer loginAttempt;

    @Column(name = "attempt_based_login")
    @JsonIgnore
    private Boolean attemptBasedLogin;

    @Column(name = "ip_based_login")
    @JsonIgnore
    private Boolean ipBasedLogin;

    @ManyToOne
    private Lookups type;

    @ManyToOne
    private Departments defaultDepartment;

    /*
     * @OneToMany(cascade=CascadeType.ALL, mappedBy="contact_a",fetch =
     * FetchType.EAGER)
     *
     * @JsonBackReference private List<ContactRelationships> relatedContact;
     */
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

    public Boolean isDashboard() {
        return dashboard;
    }

    public void setDashboard(Boolean dashboard) {
        this.dashboard = dashboard;
    }

    public Boolean isInternalAccessOnly() {
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

    public Boolean isAttemptBasedLogin() {
        return attemptBasedLogin;
    }

    public void setAttemptBasedLogin(Boolean attemptBasedLogin) {
        this.attemptBasedLogin = attemptBasedLogin;
    }

    public Boolean isIpBasedLogin() {
        return ipBasedLogin;
    }

    public void setIpBasedLogin(Boolean ipBasedLogin) {
        this.ipBasedLogin = ipBasedLogin;
    }

    public Lookups getType() {
        return type;
    }

    public void setType(Lookups lookups) {
        this.type = lookups;
    }

    public Departments getDefaultDepartment() {
        return defaultDepartment;
    }

    public void setDefaultDepartment(Departments departments) {
        this.defaultDepartment = departments;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contact contact = (Contact) o;
        if (contact.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, contact.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
