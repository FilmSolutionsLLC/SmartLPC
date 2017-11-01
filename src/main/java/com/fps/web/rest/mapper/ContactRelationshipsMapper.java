package com.fps.web.rest.mapper;

import com.fps.domain.*;
import com.fps.web.rest.dto.ContactRelationshipsDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ContactRelationships and its DTO ContactRelationshipsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContactRelationshipsMapper {

    @Mapping(source = "contact_a.id", target = "contactAId")
    @Mapping(source = "contact_a.fullName", target = "contactAFullName")
    @Mapping(source = "contact_b.id", target = "contactBId")
    @Mapping(source = "contact_b.fullName", target = "contactBFullName")
    ContactRelationshipsDTO contactRelationshipsToContactRelationshipsDTO(ContactRelationships contactRelationships);

    List<ContactRelationshipsDTO> contactRelationshipsToContactRelationshipsDTOs(List<ContactRelationships> contactRelationships);


    @Mapping(source = "contactAId", target = "contact_a")
    @Mapping(source = "contactBId", target = "contact_b")
    ContactRelationships contactRelationshipsDTOToContactRelationships(ContactRelationshipsDTO contactRelationshipsDTO);

    List<ContactRelationships> contactRelationshipsDTOsToContactRelationships(List<ContactRelationshipsDTO> contactRelationshipsDTOs);

    default Contact contactsFromId(Long id) {
        if (id == null) {
            return null;
        }
        Contact contact = new Contact();
        contact.setId(id);
        return contact;
    }
}
