package com.fps.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the ContactRelationships entity.
 */
public class ContactRelationshipsDTO implements Serializable {

    private Long id;

    private Boolean isPrimary;


    private Long contactAId;

    private String contactAFullName;

    private Long contactBId;

    private String contactBFullName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public Long getContactAId() {
        return contactAId;
    }

    public void setContactAId(Long contactsId) {
        this.contactAId = contactsId;
    }

    public String getContactAFullName() {
        return contactAFullName;
    }

    public void setContactAFullName(String contactsFullName) {
        this.contactAFullName = contactsFullName;
    }

    public Long getContactBId() {
        return contactBId;
    }

    public void setContactBId(Long contactsId) {
        this.contactBId = contactsId;
    }

    public String getContactBFullName() {
        return contactBFullName;
    }

    public void setContactBFullName(String contactsFullName) {
        this.contactBFullName = contactsFullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContactRelationshipsDTO contactRelationshipsDTO = (ContactRelationshipsDTO) o;

        if ( ! Objects.equals(id, contactRelationshipsDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ContactRelationshipsDTO{" +
            "id=" + id +
            ", isPrimary='" + isPrimary + "'" +
            '}';
    }
}
