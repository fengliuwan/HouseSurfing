package com.fengliuwan.staybooking.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "stay_reserved_date")
public class StayReservedDate implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId // JPA anno indicating this is the composite id
    private StayReservedDateKey id;

    @ManyToOne // many stay reserved date - to one stay
    @MapsId("stay_id") // means this is a foreign key, reference a PK in stay table
    private Stay stay;


    public StayReservedDate() {}

    public StayReservedDate(StayReservedDateKey id, Stay stay) {
        this.id = id;
        this.stay = stay;
    }

    public StayReservedDateKey getId() {
        return id;
    }

    public Stay getStay() {
        return stay;
    }

}
