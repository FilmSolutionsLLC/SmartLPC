package com.fps.repository;

import com.fps.domain.Lookups;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lookups entity.
 */
public interface LookupsRepository extends JpaRepository<Lookups,Long> {
    @Query("SELECT t FROM Lookups t WHERE t.tableName = ?1 AND t.fieldName = ?2")
    List<Lookups> findByTableNameAndFieldName(String tableName, String fieldName);

    @Query("SELECT t FROM Lookups t WHERE t.tableName = ?1")
    List<Lookups> findByTableName(String tableName);

    @Query("SELECT t FROM Lookups t WHERE t.tableName = ?1 AND t.fieldName = ?2")
    Lookups findByConfigAndKey(String tableName, String fieldName);


}
