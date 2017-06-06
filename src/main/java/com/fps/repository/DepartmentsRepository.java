package com.fps.repository;

import com.fps.domain.Departments;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Departments entity.
 */
public interface DepartmentsRepository extends JpaRepository<Departments,Long> {

}
