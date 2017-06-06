package com.fps.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ContactRelationships.
 */
@Entity
@Table(name = "contact_relationships")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "contactrelationships")
public class ContactRelationships implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "is_primary_contact")
    private Boolean isPrimaryContact;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @ManyToOne
    private User createdByAdminUser;

    @ManyToOne
    private User updatedByAdminUser;

    @ManyToOne
    private Contacts contact_a;

    @ManyToOne
    private Contacts contact_b;

    @ManyToOne
    private Contacts contact_a_qb_rid;

    @ManyToOne
    private Contacts contact_b_qb_rid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsPrimaryContact() {
        return isPrimaryContact;
    }

    public void setIsPrimaryContact(Boolean isPrimaryContact) {
        this.isPrimaryContact = isPrimaryContact;
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

    public User getCreatedByAdminUser() {
        return createdByAdminUser;
    }

    public void setCreatedByAdminUser(User user) {
        this.createdByAdminUser = user;
    }

    public User getUpdatedByAdminUser() {
        return updatedByAdminUser;
    }

    public void setUpdatedByAdminUser(User user) {
        this.updatedByAdminUser = user;
    }

    public Contacts getContact_a() {
        return contact_a;
    }

    public void setContact_a(Contacts contacts) {
        this.contact_a = contacts;
    }

    public Contacts getContact_b() {
        return contact_b;
    }

    public void setContact_b(Contacts contacts) {
        this.contact_b = contacts;
    }

    public Contacts getContact_a_qb_rid() {
        return contact_a_qb_rid;
    }

    public void setContact_a_qb_rid(Contacts contacts) {
        this.contact_a_qb_rid = contacts;
    }

    public Contacts getContact_b_qb_rid() {
        return contact_b_qb_rid;
    }

    public void setContact_b_qb_rid(Contacts contacts) {
        this.contact_b_qb_rid = contacts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContactRelationships contactRelationships = (ContactRelationships) o;
        if(contactRelationships.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, contactRelationships.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ContactRelationships{" +
            "id=" + id +
            ", isPrimaryContact='" + isPrimaryContact + "'" +
            ", createdDate='" + createdDate + "'" +
            ", updatedDate='" + updatedDate + "'" +
            '}';
    }
}
