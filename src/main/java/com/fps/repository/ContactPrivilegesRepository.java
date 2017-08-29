package com.fps.repository;

import com.fps.domain.ContactPrivileges;
import com.fps.domain.Contacts;
import com.fps.domain.Projects;

import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the ContactPrivileges entity.
 */
public interface ContactPrivilegesRepository extends JpaRepository<ContactPrivileges, Long> {

    @Query("select contactPrivileges from ContactPrivileges contactPrivileges where contactPrivileges.createdByAdminUser.login = ?#{principal.username}")
    List<ContactPrivileges> findByCreatedByAdminUserIsCurrentUser();

    @Query("select contactPrivileges from ContactPrivileges contactPrivileges where contactPrivileges.updatedByAdminUser.login = ?#{principal.username}")
    List<ContactPrivileges> findByUpdatedByAdminUserIsCurrentUser();

    List<ContactPrivileges> findByProject(Projects projects);

    List<ContactPrivileges> findByProjectAndInternal(Projects projects, Boolean internal);

    @Transactional
    void deleteByProject(Projects projects);
    
    List<ContactPrivileges> findByContact(Contacts contact);
    
}
