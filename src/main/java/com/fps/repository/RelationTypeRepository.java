package com.fps.repository;

import com.fps.domain.RelationType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RelationType entity.
 */
public interface RelationTypeRepository extends JpaRepository<RelationType,Long> {

}
