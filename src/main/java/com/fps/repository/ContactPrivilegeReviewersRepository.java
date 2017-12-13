package com.fps.repository;

import com.fps.domain.ContactPrivilegeReviewers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactPrivilegeReviewersRepository extends JpaRepository<ContactPrivilegeReviewers,Long> {

    List<ContactPrivilegeReviewers> findByContactPrivilegeID(Long contactPrivilege);

    void deleteContactPrivilegeReviewersByContactPrivilegeIDAndTagID(Long contactPrivilegeID ,Long tagID);

}
