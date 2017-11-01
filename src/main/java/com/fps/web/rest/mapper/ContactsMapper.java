package com.fps.web.rest.mapper;

import com.fps.domain.Contact;
import com.fps.domain.Contacts;
import com.fps.domain.Departments;
import com.fps.domain.Lookups;
import com.fps.web.rest.dto.ContactDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Contacts and its DTO ContactsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContactsMapper {

    @Mapping(source = "companyContact.id", target = "companyContactId")
    @Mapping(source = "companyContact.fullName", target = "companyContactFullName")
    @Mapping(source = "type.id", target = "typeId")
    @Mapping(source = "type.textValue", target = "typeValue")
    @Mapping(source = "defaultDepartment.id", target = "departmentId")
    @Mapping(source = "defaultDepartment.departmentName", target = "departmentName")
    ContactDTO contactsToContactsDTO(Contacts contacts);

    List<ContactDTO> contactsToContactsDTOs(List<Contacts> contacts);

    @Mapping(source = "companyContactId", target = "companyContact")
    @Mapping(source = "typeId", target = "type")
    @Mapping(source = "departmentId", target = "defaultDepartment")
    Contacts contactsDTOToContacts(ContactDTO contactsDTO);

    List<Contacts> contactsDTOsToContacts(List<ContactDTO> contactsDTOs);

    default Contact contactsFromId(Long id) {
        if (id == null) {
            return null;
        }
        Contact contact = new Contact();
        contact.setId(id);
        return contact;
    }
    default Departments departmentFromId(Long id) {
        if (id == null) {
            return null;
        }
        Departments department = new Departments();
        department.setId(id);
        return department;
    }
    default Lookups typeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Lookups type = new Lookups();
        type.setId(id);
        return type;
    }
}
