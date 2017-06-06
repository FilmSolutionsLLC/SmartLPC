package com.fps.repository;

import com.fps.domain.Storage_Disk;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Storage_Disk entity.
 */
public interface Storage_DiskRepository extends JpaRepository<Storage_Disk,Long> {

}
