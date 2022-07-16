package com.fengliuwan.staybooking.exception;

/**
 * exception when calling geoencoding api
 */
public class GeoCodingException extends RuntimeException{

    public GeoCodingException(String message) {
        super(message);
    }
}
