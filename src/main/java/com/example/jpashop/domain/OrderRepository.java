package com.example.jpashop.domain;

import com.example.jpashop.Repository.OrderSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o join o.member m " +
            "where o.status =:status " +
            "and m.name like :name")
    //List<Order> findAll(@Param("status") OrderStatus orderStatus ,@Param("name") String name);
    List<Order> findAll(OrderSearch orderSearch);

}
