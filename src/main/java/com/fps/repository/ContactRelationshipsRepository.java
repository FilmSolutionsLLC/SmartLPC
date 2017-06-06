package com.fps.repository;

import com.fps.domain.ContactRelationships;

import com.fps.domain.Contacts;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the ContactRelationships entity.
 */
public interface ContactRelationshipsRepository extends JpaRepository<ContactRelationships, Long> {

    @Query("select contactRelationships from ContactRelationships contactRelationships where contactRelationships.createdByAdminUser.login = ?#{principal.username}")
    List<ContactRelationships> findByCreatedByAdminUserIsCurrentUser();

    @Query("select contactRelationships from ContactRelationships contactRelationships where contactRelationships.updatedByAdminUser.login = ?#{principal.username}")
    List<ContactRelationships> findByUpdatedByAdminUserIsCurrentUser();


    @Query("select contactRelationships from ContactRelationships contactRelationships where contactRelationships.contact_a= :contact")
    List<ContactRelationships> findByContact_A(@Param("contact") Contacts contacts);

    @Transactional
    @Modifying
    @Query("delete from ContactRelationships c where c.contact_a=:contact")
    void deleteByContacts(@Param("contact") Contacts contacts);

}

