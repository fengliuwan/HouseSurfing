package com.fengliuwan.staybooking.service;

import com.fengliuwan.staybooking.exception.GeoCodingException;
import com.fengliuwan.staybooking.exception.InvalidStayAddressException;
import com.fengliuwan.staybooking.model.Location;
import com.google.maps.errors.ApiException;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeoCodingService {

    private GeoApiContext context;

    @Autowired
    public GeoCodingService(GeoApiContext context) {
        this.context = context;
    }

    public Location getLatLng(Long id, String address) throws GeoCodingException {
        try {
            GeocodingResult result = GeocodingApi.geocode(context, address).await()[0];
            if (result.partialMatch) {
                throw new InvalidStayAddressException("failed to find stay address");
            }

            return new Location(id, new GeoPoint(result.geometry.location.lat, result.geometry.location.lng));
        } catch (IOException | ApiException | InterruptedException ex) { // exception thrown by await()
            ex.printStackTrace();
            throw new GeoCodingException("failed to encode stay address");
        }
    }


}
