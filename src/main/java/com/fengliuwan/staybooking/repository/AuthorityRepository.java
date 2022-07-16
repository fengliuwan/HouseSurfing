package com.fengliuwan.staybooking.repository;

import com.fengliuwan.staybooking.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {

    // username extracted from token from user request find authority of user in db
    // to protect stay management API with token authentication
    Authority findAuthorityByUsername(String username);

}
