package com.fps.repository;

import com.fps.domain.ContactPrivilegeAlbums;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactPrivilegeAlbumsRepository extends JpaRepository<ContactPrivilegeAlbums,Long> {

    List<ContactPrivilegeAlbums> findByContactPrivilegeID(Long conttactPrivilege);

    void deleteContactPrivilegeAlbumsByContactPrivilegeIDAndAlbumNodeID(Long contactPrivilegeID,Long albumNodeID);


}
