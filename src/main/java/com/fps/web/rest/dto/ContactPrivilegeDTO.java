package com.fps.web.rest.dto;

import com.fps.domain.ContactPrivilegeAlbums;
import com.fps.domain.ContactPrivilegeReviewers;
import com.fps.domain.ContactPrivileges;

import java.util.List;

public class ContactPrivilegeDTO {

    private ContactPrivileges contactPrivileges;
    private List<ContactPrivilegeAlbums> contactPrivilegeAlbums;
    private List<ContactPrivilegeReviewers> contactPrivilegeReviewers;

    public ContactPrivileges getContactPrivileges() {
        return contactPrivileges;
    }

    public void setContactPrivileges(ContactPrivileges contactPrivileges) {
        this.contactPrivileges = contactPrivileges;
    }

    public List<ContactPrivilegeAlbums> getContactPrivilegeAlbums() {
        return contactPrivilegeAlbums;
    }

    public void setContactPrivilegeAlbums(List<ContactPrivilegeAlbums> contactPrivilegeAlbums) {
        this.contactPrivilegeAlbums = contactPrivilegeAlbums;
    }

    public List<ContactPrivilegeReviewers> getContactPrivilegeReviewers() {
        return contactPrivilegeReviewers;
    }

    public void setContactPrivilegeReviewers(List<ContactPrivilegeReviewers> contactPrivilegeReviewers) {
        this.contactPrivilegeReviewers = contactPrivilegeReviewers;
    }


}
