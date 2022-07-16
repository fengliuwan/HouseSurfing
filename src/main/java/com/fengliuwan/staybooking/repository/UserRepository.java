package com.fengliuwan.staybooking.repository;

import com.fengliuwan.staybooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //spring data 会找到这个interface， automatically create an implementation for it
public interface UserRepository extends JpaRepository<User, String> {
    //<用于和数据库交互的class，class中Id的type>
    // extend JpaRepository之后 spring提供了default implementation of common database operations
    // interface里的方法的implementation由spring data 框架实现，spring会生成对应的concrete class 依赖注入到我们需要用的地方
    //In order to start leveraging the Spring Data programming model with JPA, a DAO interface needs to
    // extend the JPA specific Repository interface, JpaRepository.
    // This will enable Spring Data to find this interface and automatically create an implementation for it.

}
