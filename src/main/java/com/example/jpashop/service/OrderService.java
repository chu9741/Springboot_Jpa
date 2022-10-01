package com.example.jpashop.service;

import com.example.jpashop.domain.*;
import com.example.jpashop.domain.item.Item;
import com.example.jpashop.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final com.example.jpashop.Repository.OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //주문

    @Transactional
    public Long order(Long memberId, Long itemId, int count){

        //엔티티 조회
        Member member = memberRepository.findById(memberId).orElseThrow(()->new IllegalArgumentException("no member."));
        Item item  = itemRepository.findById(itemId).orElseThrow(()->new IllegalArgumentException("no item."));

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);//order + delivery 저장됨

        return order.getId();
    }
    //취소

    @Transactional
    public void cancelOrder(Long orderId){
        //주문 엔티티 조회
        Order order= orderRepository.findOne(orderId);

        //주문 취소
        order.cancel();
    }
    //검색


}
