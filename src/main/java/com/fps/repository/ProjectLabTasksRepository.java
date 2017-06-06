package com.fps.repository;

import com.fps.domain.ProjectLabTasks;

import com.fps.domain.Projects;
import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the ProjectLabTasks entity.
 */
public interface ProjectLabTasksRepository extends JpaRepository<ProjectLabTasks, Long> {

    @Query("select projectLabTasks from ProjectLabTasks projectLabTasks where projectLabTasks.created_by_admin_user.login = ?#{principal.username}")
    List<ProjectLabTasks> findByCreated_by_admin_userIsCurrentUser();

    @Query("select projectLabTasks from ProjectLabTasks projectLabTasks where projectLabTasks.updated_by_admin_user.login = ?#{principal.username}")
    List<ProjectLabTasks> findByUpdated_by_admin_userIsCurrentUser();

    List<ProjectLabTasks> findByProject(Projects projects);

    @Transactional
    void deleteByProject(Projects projects);

    @Query("select lab.task_name from ProjectLabTasks lab where lab.project=?1")
    List<ProjectLabTasks> findByProjectID(Projects projects);


}
