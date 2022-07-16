package com.fengliuwan.staybooking.model;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * composite key class required by JPA for composite key in stay reserved table
 */
@Embeddable // JPA annotation, means this class is primary key class
public class StayReservedDateKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long stay_id;
    private LocalDate date;

    //https://www.baeldung.com/jpa-composite-primary-keys
    // The composite primary key class must be public.
    //It must have a no-arg constructor.
    //It must define the equals() and hashCode() methods.
    //It must be Serializable.
    public StayReservedDateKey() {}

    public StayReservedDateKey(Long stay_id, LocalDate date) {
        this.stay_id = stay_id;
        this.date = date;
    }

    public Long getStay_id() {
        return stay_id;
    }

    // return this for concatenation of method calls
    public StayReservedDateKey setStay_id(Long stay_id) {
        this.stay_id = stay_id;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public StayReservedDateKey setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StayReservedDateKey)) return false;
        StayReservedDateKey that = (StayReservedDateKey) o;
        return stay_id.equals(that.stay_id) && date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stay_id, date);
    }
}
