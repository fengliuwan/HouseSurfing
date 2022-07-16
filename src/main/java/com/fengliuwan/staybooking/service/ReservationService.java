package com.fengliuwan.staybooking.service;

import com.fengliuwan.staybooking.exception.ReservationCollisionException;
import com.fengliuwan.staybooking.exception.ReservationNotFoundException;
import com.fengliuwan.staybooking.model.*;
import com.fengliuwan.staybooking.repository.ReservationRepository;
import com.fengliuwan.staybooking.repository.StayReservationDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;
    private StayReservationDateRepository stayReservationDateRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, StayReservationDateRepository stayReservationDateRepository) {
        this.reservationRepository = reservationRepository;
        this.stayReservationDateRepository = stayReservationDateRepository;
    }

    /**
     *
     * @param username
     * @return reservation by user with the username
     */
    public List<Reservation> listByGuest(String username) {
        return reservationRepository.findByGuest(new User.Builder().setUsername(username).build());
    }

    public List<Reservation> listByStay(Long stayId) {
        return reservationRepository.findByStay(new Stay.Builder().setId(stayId).build());
    }

    public void add(Reservation reservation) throws ReservationCollisionException {
        Set<Long> stayIds = stayReservationDateRepository
                .findByIdInAndDateBetween(Arrays.asList(reservation.getStay().getId()),
                                            reservation.getCheckinDate(),
                                            reservation.getCheckoutDate().minusDays(1));
        if (!stayIds.isEmpty()) {
            throw new ReservationCollisionException("Duplicate reservation");
        }

        List<StayReservedDate> reservedDates = new ArrayList<>();
        for (LocalDate date = reservation.getCheckinDate(); date.isBefore(reservation.getCheckoutDate()); date = date.plusDays(1)) {
            reservedDates.add(new StayReservedDate(new StayReservedDateKey(reservation.getStay().getId(), date), reservation.getStay()));
        }
        // multiple date reservation saved as multiple entries in reservationDate table
        stayReservationDateRepository.saveAll(reservedDates);
        reservationRepository.save(reservation);

    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delete(Long reservationId, String username) {
        Reservation reservation = reservationRepository.
                findByIdAndGuest(reservationId, new User.Builder().setUsername(username).build());

        if (reservation == null) {
            throw new ReservationNotFoundException("Reservation is not available");
        }

        for (LocalDate date = reservation.getCheckinDate(); date.isBefore(reservation.getCheckoutDate()); date = date.plusDays(1)) {
            stayReservationDateRepository.deleteById(new StayReservedDateKey(reservation.getStay().getId(), date)); // need to create key for search
        }
        // multiple date reservation saved as multiple entries in reservationDate table, need to delete one by one

        reservationRepository.deleteById(reservationId);
    }

}
