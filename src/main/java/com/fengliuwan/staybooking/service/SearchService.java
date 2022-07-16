package com.fengliuwan.staybooking.service;

import com.fengliuwan.staybooking.model.Stay;
import com.fengliuwan.staybooking.repository.LocationRepository;
import com.fengliuwan.staybooking.repository.StayRepository;
import com.fengliuwan.staybooking.repository.StayReservationDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SearchService {

    private StayRepository stayRepository;
    private StayReservationDateRepository stayReservationDateRepository;
    private LocationRepository locationRepository;

    @Autowired
    public SearchService(StayRepository stayRepository, StayReservationDateRepository stayReservationDateRepository, LocationRepository locationRepository) {
        this.stayRepository = stayRepository;
        this.stayReservationDateRepository = stayReservationDateRepository;
        this.locationRepository = locationRepository;
    }

    public List<Stay> search(int guestNumber, LocalDate checkinDate, LocalDate checkoutDate, double lat, double lon, String distance) {

        // find if there are any stays within distance first
        List<Long> stayIds = locationRepository.searchByDistance(lat, lon, distance);
        if (stayIds == null || stayIds.isEmpty()) {
            return new ArrayList<>();
        }

        // in all the stays within the geo range, query stay ids are booked between checkin checkout date, using Query anno in repo
        Set<Long> reservedStayIds = stayReservationDateRepository
                .findByIdInAndDateBetween(stayIds, checkinDate, checkoutDate.minusDays(1));

        // filter stayIds that are booked and keep stayIds available
        List<Long> filteredStayIds = new ArrayList<>();
        for (Long stayId : stayIds) {
            if (!reservedStayIds.contains(stayId)) {
                filteredStayIds.add(stayId);
            }
        }
        // search for available stayIds that matches guest number, return found stayids
        return stayRepository.findByIdInAndGuestNumberGreaterThanEqual(filteredStayIds, guestNumber);
    }

}
