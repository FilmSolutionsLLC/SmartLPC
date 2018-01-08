package com.fps.repository;

import com.fps.domain.Contact;
import com.fps.domain.ContactPrivileges;
import com.fps.domain.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the ContactPrivileges entity.
 */
public interface ContactPrivilegesRepository extends JpaRepository<ContactPrivileges, Long> {

    @Query("select contactPrivileges from ContactPrivileges contactPrivileges where contactPrivileges.createdByAdminUser.login = ?#{principal.username}")
    List<ContactPrivileges> findByCreatedByAdminUserIsCurrentUser();

    @Query("select contactPrivileges from ContactPrivileges contactPrivileges where contactPrivileges.updatedByAdminUser.login = ?#{principal.username}")
    List<ContactPrivileges> findByUpdatedByAdminUserIsCurrentUser();

   // List<ContactPrivileges> findByProjectAndInternal(Projects projects, Boolean internal);
    //List<ContactPrivileges> findByProject(Projects projects);

    List<ContactPrivileges> findByProject(Projects projects);


    @Transactional
    void deleteByProject(Projects projects);

    List<ContactPrivileges> findByContact(Contact contact);

    @Transactional
    List<ContactPrivileges> removeByContact(Contact contact);

    @Transactional
    List<ContactPrivileges> removeByProject(Projects projects);


    ContactPrivileges findByProjectAndContact(Projects projects, Contact contacts);

    @Query("select contact from ContactPrivileges contactPrivileges where contactPrivileges.project.name like CONCAT('%',:project,'%') order by contactPrivileges.contact.fullName ASC ")
    Set<Contact> findByProject(@Param("project") String project);
}
