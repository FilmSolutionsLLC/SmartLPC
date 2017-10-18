package com.fps.repository;

import com.fps.domain.Contacts;
import com.fps.domain.ProjectRoles;
import com.fps.domain.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the ProjectRoles entity.
 */
public interface ProjectRolesRepository extends JpaRepository<ProjectRoles, Long> {

    @Query("select projectRoles from ProjectRoles projectRoles where projectRoles.createdByAdminUser.login = ?#{principal.username}")
    List<ProjectRoles> findByCreatedByAdminUserIsCurrentUser();

    @Query("select projectRoles from ProjectRoles projectRoles where projectRoles.updatedByAdminUser.login = ?#{principal.username}")
    List<ProjectRoles> findByUpdatedByAdminUserIsCurrentUser();

    List<ProjectRoles> findByProject(Projects projects);

    @Transactional
    void deleteByProject(Projects projects);


    @Query("select  p from ProjectRoles p where p.project = ?1 and p.relationship_type in (?2,?3)")
    Set<ProjectRoles> findByProjectAndRelationshipType(Projects projects, String MainC, String UPub);

    @Query("select  p from ProjectRoles p where p.project = ?1 and p.contact <> ?2")
    Set<ProjectRoles> findByProjectAndContact(Projects projects, Contacts contacts);

    @Transactional
    List<ProjectRoles> removeByContact(Contacts contact);

}
