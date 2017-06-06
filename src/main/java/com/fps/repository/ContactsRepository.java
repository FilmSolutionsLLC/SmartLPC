package com.fps.repository;

import com.fps.domain.Contacts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Objects;

/**
 * Spring Data JPA repository for the Contacts entity.
 */
public interface ContactsRepository extends JpaRepository<Contacts, Long> {

    //@Query("select contacts from Contacts contacts where contacts.createdByAdmin.login = ?#{principal.username}")
    //List<Contacts> findByCreatedByAdminIsCurrentUser();

    //@Query("select contacts from Contacts contacts where contacts.updatedByAdmin.login = ?#{principal.username}")
    //List<Contacts> findByUpdatedByAdminIsCurrentUser();


    String sql = "select c.id 'id',c.full_name 'fullName',c.title 'title',c.email 'email',c.phone_office 'phoneOffice',c.phone_mobile 'phoneMobile', l.text_value 'type' ,cr.id 'companyContactId', cr.full_name 'companyContactName' from contacts c left join lookups l on c.type_id  = l.id left join contacts cr on c.company_contact_id = cr.id  \n#pageable\n";

    @Query(value = sql, nativeQuery = true)
    public Page<Objects[]> getAllContacts(Pageable pageable);

}
