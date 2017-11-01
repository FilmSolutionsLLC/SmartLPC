package com.fps.web.rest.dto;

import java.util.List;

public class ContactViewSingleDTO {
    private ContactDTO contacts;

    public ContactDTO getContactDTO() {
        return contacts;
    }

    public void setContactDTO(ContactDTO contactDTO) {
        this.contacts = contactDTO;
    }

    public List<ContactRelationshipsDTO> getContactRelationshipsDTOS() {
        return contactRelationships;
    }

    public void setContactRelationshipsDTOS(List<ContactRelationshipsDTO> contactRelationshipsDTOS) {
        this.contactRelationships = contactRelationships;
    }

    private List<ContactRelationshipsDTO> contactRelationships;

    public ContactViewSingleDTO(ContactDTO contacts, List<ContactRelationshipsDTO> contactRelationships) {
        this.contacts = contacts;
        this.contactRelationships = contactRelationships;
    }

    public ContactViewSingleDTO() {
    }
}
