package com.fps.repository;

import com.fps.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Contacts entity.
 */
public interface ContactRepository extends JpaRepository<Contact, Long> {


}
