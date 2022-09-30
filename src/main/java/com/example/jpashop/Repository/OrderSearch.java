package com.example.jpashop.Repository;

import com.example.jpashop.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch {

    private String memberName;
    private OrderStatus orderStatus; //주문상태 [ORDER, CANCEL]
}
