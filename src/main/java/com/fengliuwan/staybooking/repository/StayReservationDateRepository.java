package com.fengliuwan.staybooking.repository;

import com.fengliuwan.staybooking.model.Stay;
import com.fengliuwan.staybooking.model.StayReservedDate;
import com.fengliuwan.staybooking.model.StayReservedDateKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface StayReservationDateRepository extends JpaRepository<StayReservedDate, StayReservedDateKey> {

    //JpaRepository cannot support the custom findByIdInAndDateBetween() method,
    // so we need to provide the implementation by ourselves.
    // 1 use the same solution as LocationRepository to create an implementation class,
    // 2 in this case, just write the SQL query on top of the method.

    /* find stay ids in the stayIds that are already booked between startDate and endDate. */
    // method name need to match field name (database column name). group by -- > multiple record with same id, return one id

    @Query(value = "SELECT srd.id.stay_id FROM StayReservedDate srd WHERE srd.id.stay_id IN ?1 AND srd.id.date BETWEEN ?2 AND ?3 GROUP BY srd.id.stay_id")
    Set<Long> findByIdInAndDateBetween(List<Long> stayIds, LocalDate startDate, LocalDate endDate);

    List<StayReservedDate> findByStay(Stay stay);

}
