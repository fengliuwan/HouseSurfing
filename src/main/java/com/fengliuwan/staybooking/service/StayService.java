package com.fengliuwan.staybooking.service;

import com.fengliuwan.staybooking.exception.StayDeleteException;
import com.fengliuwan.staybooking.exception.StayNotExistException;
import com.fengliuwan.staybooking.model.*;
import com.fengliuwan.staybooking.repository.LocationRepository;
import com.fengliuwan.staybooking.repository.ReservationRepository;
import com.fengliuwan.staybooking.repository.StayRepository;
import com.fengliuwan.staybooking.repository.StayReservationDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StayService {
    private StayRepository stayRepository;
    private LocationRepository locationRepository;
    private ImageStorageService imageStorageService;
    private GeoCodingService geoCodingService;

    private ReservationRepository reservationRepository;

    // when deleting reservation, need to delete stay reservation date as well
    private StayReservationDateRepository stayReservationDateRepository;

    @Autowired
    public StayService(StayRepository stayRepository,
                       LocationRepository locationRepository,
                       ImageStorageService imageStorageService,
                       GeoCodingService geoCodingService,
                       ReservationRepository reservationRepository,
                       StayReservationDateRepository stayReservationDateRepository) {
        this.stayRepository = stayRepository;
        this.locationRepository = locationRepository;
        this.imageStorageService = imageStorageService;
        this.geoCodingService = geoCodingService;
        this.reservationRepository = reservationRepository;
        this.stayReservationDateRepository = stayReservationDateRepository;
    }

    public List<Stay> listByUser(String username) {
        return stayRepository.findByHost(new User.Builder().setUsername(username).build());
    }

    public Stay findByIdAndHost(Long stayId, String username) throws StayNotExistException {
        Stay stay = stayRepository.findByIdAndHost(stayId, new User.Builder().setUsername(username).build());
        if (stay == null) {
            throw new StayNotExistException("Stay doesn't exist");
        }
        return stay;
    }

    /**
     * delete stay but did not delete images in google cloud storage or location in elastic search
     * for faster delete for better user experience
     */
    @Transactional(isolation = Isolation.SERIALIZABLE) // if an entry is deleted in stay table, record in stay_reserve will not be deleted
    public void delete(Long stayId, String username) throws StayNotExistException {
        Stay stay = stayRepository.findByIdAndHost(stayId, new User.Builder().setUsername(username).build());
        if (stay == null) {
            throw new StayNotExistException("Cannot find stay under the user to delete");
        }

        List<Reservation> reservations = reservationRepository.findByStayAndCheckoutDateAfter(stay, LocalDate.now());
        if (reservations != null && reservations.size() > 0) {
            throw new StayDeleteException("Cannot delete stay with active reservation");
        }
        // need to add call to delete all reservation date associated with the stayid

        List<StayReservedDate> stayReservedDates = stayReservationDateRepository.findByStay(stay);

        for(StayReservedDate date : stayReservedDates) {
            stayReservationDateRepository.deleteById(date.getId());
        }

        stayRepository.deleteById(stayId);
    }

    /**
     * support image saving.
     * @param stay
     * @param images
     */
    @Transactional(isolation = Isolation.SERIALIZABLE) // @transanctional 保证对stayimage 和 stay table 的操作成功与否的一致性
    public void add(Stay stay, MultipartFile[] images) {
        List<String> mediaLinks = Arrays.stream(images)
                .parallel() // upload images concurrently
                .map(image -> imageStorageService.save(image))// arg in map is a function
                .collect(Collectors.toList());

        List<StayImage> stayImages = new ArrayList<>();
        for (String mediaLink : mediaLinks) {
            stayImages.add(new StayImage(mediaLink, stay));
        }
        stay.setImages(stayImages);
        stayRepository.save(stay);  // db autogenerate an id when stay object is saved in db

        // save location in elastic search  此处没有保证不同数据库更新同步，elasticsearch nosql，not atomic
        Location location = geoCodingService.getLatLng(stay.getId(), stay.getAddress());
        locationRepository.save(location);
    }
}
