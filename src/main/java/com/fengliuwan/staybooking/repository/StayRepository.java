package com.fengliuwan.staybooking.repository;

import com.fengliuwan.staybooking.model.Stay;
import com.fengliuwan.staybooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StayRepository extends JpaRepository<Stay, Long> {
    List<Stay> findByHost(User user); // find in "user" table in the col "host"

    Stay findByIdAndHost(Long id, User host); // find in col "id" and "host"

    //besides location, the guest number is another parameter for search.
    //can also add search by price
    List<Stay> findByIdInAndGuestNumberGreaterThanEqual(List<Long> ids, int guestNumber);


}
