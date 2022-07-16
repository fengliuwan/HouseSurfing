package com.fengliuwan.staybooking.repository;

import com.fengliuwan.staybooking.model.Location;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

//extends ElasticsearchRepository instead of JpaRepository
//since Elasticsearch has a different query implementation than MySQL. and not a relational db
//similar to JpaRepository, LocationRepository also provides some basic query functions
// like find(), save() and delete()
//our service needs to support search based on Geolocation,
// we need to implement the search function ourselves.
// extends two interfaces???
// 当spring创建LR implmenetation class的时候， 会生成具体的class拥有两个interface的implementation
// 会找到CLR implementation的class中的方法
public interface LocationRepository extends
        ElasticsearchRepository<Location, Long>, CustomLocationRepository {
}
