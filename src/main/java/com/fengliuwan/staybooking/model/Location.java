package com.fengliuwan.staybooking.model;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import javax.persistence.Id;
import java.io.Serializable;

@Document(indexName = "loc")
//存在elasticsearch这个数据库中
//Elasticsearch related annotations so that we can create the mapping between
//the Location class and an Elasticsearch document.
public class Location implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id // spring data id anno, not mysql
    @Field(type = FieldType.Long)
    private Long id;    //id的值是由mysql生成的 作为值存入elasticsearch

    @GeoPointField
    private GeoPoint geoPoint;

    public Location(Long id, GeoPoint geoPoint) {
        this.id = id;
        this.geoPoint = geoPoint;
    }

    public Long getId() {
        return id;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

}
