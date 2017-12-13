package com.fps.repository;

import com.fps.domain.Contact;
import com.fps.domain.Lookups;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Contacts entity.
 */
public interface ContactRepository extends JpaRepository<Contact, Long> {


    List<Contact> findByType(Lookups type);
}
