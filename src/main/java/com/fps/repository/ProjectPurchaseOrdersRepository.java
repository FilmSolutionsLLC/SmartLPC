package com.fps.repository;

import com.fps.domain.ProjectLabTasks;
import com.fps.domain.ProjectPurchaseOrders;

import com.fps.domain.Projects;
import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the ProjectPurchaseOrders entity.
 */
public interface ProjectPurchaseOrdersRepository extends JpaRepository<ProjectPurchaseOrders, Long> {

    @Query("select projectPurchaseOrders from ProjectPurchaseOrders projectPurchaseOrders where projectPurchaseOrders.created_by_admin_user.login = ?#{principal.username}")
    List<ProjectPurchaseOrders> findByCreated_by_admin_userIsCurrentUser();

    @Query("select projectPurchaseOrders from ProjectPurchaseOrders projectPurchaseOrders where projectPurchaseOrders.updated_by_admin_user.login = ?#{principal.username}")
    List<ProjectPurchaseOrders> findByUpdated_by_admin_userIsCurrentUser();

    List<ProjectPurchaseOrders> findByProject(Projects projects);

    @Query("select projectPurchaseOrders.po_number,projectPurchaseOrders.po_notes from ProjectPurchaseOrders projectPurchaseOrders where projectPurchaseOrders.project=?1")
    Set<ProjectPurchaseOrders> findByProjectID(Projects project);

    @Transactional
    void deleteByProject(Projects projects);
}
