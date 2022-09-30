package com.example.jpashop.service;

import com.example.jpashop.domain.*;
import com.example.jpashop.domain.item.Book;
import com.example.jpashop.domain.item.Item;
import com.example.jpashop.exception.NotEnoughStockException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Fail.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
        //given
        Member member = new Member();
        member.setName("member1");
        member.setAddress(new Address("seoul", "경기", "123-123"));
        em.persist(member);

        Book book = new Book();
        book.setName("jpa");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount=2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findById(orderId).orElseThrow(()->new IllegalArgumentException("no order!!!"));

        Assert.assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        Assert.assertEquals("주문한 가격은 가격*수량", 10000*orderCount,getOrder.getTotalPrice() );
        Assert.assertEquals("주문한 상품 종류 수가 정확해야함", 1,getOrder.getOrderItems().size() );
        Assert.assertEquals("주문 수량만큼 재고 감소", 8, book.getStockQuantity());
    }


    @Test
    public void 주문취소() throws Exception{
        //given
        Member member = new Member();
        member.setName("member1");
        member.setAddress(new Address("seoul", "경기", "123-123"));
        em.persist(member);

        Book book = new Book();
        book.setName("jpa");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount=2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findById(orderId).orElseThrow(()->new IllegalArgumentException("no Order!!!!!!"));

        Assert.assertEquals("주문취소시 상태는 CANCEL", OrderStatus.CANCEL, getOrder.getStatus());
        Assert.assertEquals("주문이 취소된 상품은 재고 원복",10, book.getStockQuantity());

    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception{
        //given
        Member member = new Member();
        member.setName("member1");
        member.setAddress(new Address("seoul", "경기", "123-123"));
        em.persist(member);

        Book book = new Book();
        book.setName("jpa");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount=11;

        //when
        orderService.order(member.getId(), book.getId(), orderCount);
        //then
        fail("재고 수량 부족 예외가 발생해야함");

    }
}