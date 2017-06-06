package com.fps.repository;

import com.fps.domain.Captions;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Captions entity.
 */
public interface CaptionsRepository extends JpaRepository<Captions,Long> {

}
