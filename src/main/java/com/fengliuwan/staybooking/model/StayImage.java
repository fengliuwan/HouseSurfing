package com.fengliuwan.staybooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "stay_image")
public class StayImage implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String url;

    @ManyToOne  // one stay may have several stay images
    @JoinColumn(name = "stay_id") // reference stay table, saved under "stay_id" in this table
    @JsonIgnore //jackson annotation, do not serialize stay when returning to front end. will be returned by stay class， stay also has List<StayImage> avoid infinite loop
    private Stay stay;

    // no-arg constructor required by JPA
    public StayImage() {}

    // each stay image is associated with an url and one stay
    public StayImage(String url, Stay stay) {
        this.url = url;
        this.stay = stay;
    }

    public String getUrl() {
        return url;
    }

    public StayImage setUrl(String url) {
        this.url = url;
        return this;
    }

    public Stay getStay() {
        return stay;
    }

    public StayImage setStay(Stay stay) {
        this.stay = stay;
        return this;
    }


}
