package com.fps.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fps.domain.AdminDropdown;

public interface AdminDropdownRepository extends JpaRepository<AdminDropdown,Long> {
	
	//List<AdminDropdown> findByAdmin();
	
	//List<AdminDropdown> findByDropdownName(String dropdownName);

}
