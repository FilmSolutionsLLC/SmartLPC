package com.fps.repository;

import com.fps.domain.Projects;

import com.fps.web.rest.dto.ProjectsListDTO;
import com.fps.web.rest.dto.ProjectsViewDTO;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;
import org.hibernate.annotations.NamedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Objects;

/**
 * Spring Data JPA repository for the Projects entity.
 */
public interface ProjectsRepository extends JpaRepository<Projects, Long> {

    @Query("select projects from Projects projects where projects.createdByAdminUser.login = ?#{principal.username}")
    List<Projects> findByCreatedByAdminUserIsCurrentUser();

    @Query("select projects from Projects projects where projects.updatedByAdminUser.login = ?#{principal.username}")
    List<Projects> findByUpdatedByAdminUserIsCurrentUser();


    Projects findByAlfrescoTitle1AndAlfrescoTitle2(String alfrescoTitle1, String alfrescoTitle2);

    @Query("select p.id,p.name from Projects p")
    List<Objects[]> findProjects();


    String getallQuery = "SELECT pko.id 'id',pko.name 'projectName',pko.status_text 'projectStatus',main_c.full_name 'mainContactName',main_c.phone_office 'mainContactOffice',main_c.email 'mainContactEmail',c_upub.full_name 'unitPublicistName',c_upub.phone_mobile 'unitPublicistMobile',c_upub.phone_office 'unitPublicistOffice',c_upub.email 'unitPublicistEmail' from  (((  SELECT p.id id,  p.template template,  p.name name,  pr_main.contact_id main,  pr_upub.contact_id upub,  l.text_value status_text  FROM (((projects p  left join project_roles pr_main  on p.id = pr_main.project_id  and pr_main.relationship_type = 'Main Contact' )  left join project_roles pr_upub  on p.id = pr_upub.project_id  and pr_upub.relationship_type = 'Unit Publicist' )  left join lookups l  on p.status_id = l.id)  ) pko  left join pko.contacts main_c on main_c.id = pko.main)  left join contacts c_upub on c_upub.id = pko.upub)  where 1=1  \n#pageable\n";

    @Query(value = getallQuery, nativeQuery = true)
    public Page<Objects[]> getAllProjects(Pageable pageable);

}
