package com.fps.web.rest.dto;

import com.fps.domain.ContactRelationships;
import com.fps.domain.Contacts;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by macbookpro on 12/16/16.
 */
public class ContactsDTO {

    private Contacts contacts;

    private Set<ContactRelationships> contactRelationships = new HashSet<>();


    public Set<ContactRelationships> getContactRelationships() {
        return contactRelationships;
    }

    public void setContactRelationships(Set<ContactRelationships> contactRelationships) {
        this.contactRelationships = contactRelationships;
    }

    public Contacts getContacts() {
        return contacts;
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }

    public ContactsDTO(Contacts contacts, Set<ContactRelationships> contactRelationships) {
        this.contacts = contacts;
        this.contactRelationships = contactRelationships;
    }


    public ContactsDTO() {
        super();
    }

    @Override
    public String toString() {
        return "ContactsDTO{" +
            "contacts=" + contacts +
            ", contactRelationships=" + contactRelationships +
            '}';
    }
}
